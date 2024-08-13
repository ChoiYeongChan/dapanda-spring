package com.example.dapanda.service;


import com.example.dapanda.domain.bid.Bid;
import com.example.dapanda.domain.bid.dto.BidRequestInfo;
import com.example.dapanda.domain.product.Product;
import com.example.dapanda.domain.product.dto.ProductResponseInfo;
import com.example.dapanda.repository.BidRepository;
import com.example.dapanda.repository.MemberRepository;
import com.example.dapanda.repository.ProductRepository;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductCommandService {
    private static final Logger logger = LoggerFactory.getLogger(ProductQueryService.class);
    private final ProductRepository productRepository;
    private final BidRepository bidRepository;
    private final MemberRepository memberRepository;
    private final EmailSenderService emailSenderService;

    @SqsListener(value = "${cloud.aws.sqs.queue.name}",factory = "defaultSqsListenerContainerFactory")
    public void listen(BidRequestInfo.TransactionReqInfo transactionReqInfo) {
        //log.info("-------------------------------------start SqsListener");
        int bidPrice = transactionReqInfo.getBidPrice();
        int productId = transactionReqInfo.getBidProductId();
        int memberId = transactionReqInfo.getBidMemberId();
        //log.info("-------------------------------------info {}, {}, {}", bidPrice,productId,memberId);
        updateProductPrice(transactionReqInfo);
        //수신후 삭제처리
    }


    @Transactional
    public void updateProductPrice(BidRequestInfo.TransactionReqInfo transactionReqInfo) {
        int bidPrice = transactionReqInfo.getBidPrice();
        int productId = transactionReqInfo.getBidProductId();
        int memberId = transactionReqInfo.getBidMemberId();
        log.info("Received message: {}", transactionReqInfo.toString());
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        String nowString = String.valueOf(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        Product product = productRepository.findById((long) productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        int nowPrice = product.getHighestPrice();
        int termPrice=product.getTermPrice();
        int productOwnerMemberId=product.getRegisterMemberId();
        //String transactionId=transactionReqInfo.getTransactionId();
        Bid bid = new Bid();
        bid = bid.builder()
                .bidProductId(productId)
                .bidPrice(bidPrice)
                .bidMemberId(memberId)
                .bidDate(now)
                .transactionId(transactionReqInfo.getTransactionId())
                .isSuccess(0)
                .build();
        ProductResponseInfo.ProductAuctionResult productAuctionResult = new ProductResponseInfo.ProductAuctionResult();
        //log.info("회원 {} : 물품 {}을 {}로 입찰 시도", memberId, productId, price);
        if (memberId == 0) { //비로그인
            //log.info("회원 {} : 물품 {}을 {}로 입찰 시도", memberId, productId, price);
            logger.info("[{}]<물품입찰실패 회원id: {} 물품id: {} 가격: {} 사유: 비로그인>", now, memberId, productId, bidPrice);
            String message = "입찰 실패: 로그인이 필요합니다.";
            bid.setBidResult(message);
            productAuctionResult.setIsSuccess(0);
            productAuctionResult.setMessage(message);

        } else { //로그인
            //입찰
            if (product.getEndDate().isBefore(now) || product.getAuctionStatus() ==0) { // 경매종료
                logger.info("[{}]<물품입찰실패 회원id: {} 물품id: {} 가격: {} 사유: 경매종료>", now, memberId, productId, bidPrice);

                //bidRepository.save(bid);
                String message="입찰 실패: 경매가 종료되었습니다.";
                bid.setBidResult(message);
                saveBid(bid);
                productAuctionResult.setIsSuccess(0);
                productAuctionResult.setMessage(message);
            } else if(memberId==productOwnerMemberId) { // 자신의 물건에 입찰할 때
                logger.info("[{}]<물품입찰실패 회원id: {} 물품id: {} 가격: {} 사유: 자신의 물건에 입찰을 시도함>", now, memberId, productId, bidPrice);
                String message="입찰 실패: 자신의 물건은 입찰할 수 없습니다.";
                bid.setBidResult(message);
                saveBid(bid);
                //bidRepository.save(bid);
                productAuctionResult.setIsSuccess(0);
                productAuctionResult.setMessage(message);
            }
            else if (nowPrice >= bidPrice) { // 입찰가가 현재최고가보다 낮을 때
                logger.info("[{}]<물품입찰실패 회원id: {} 물품id: {} 가격: {} 사유: 현재가보다 낮음>", now, memberId, productId, bidPrice);
                String message="입찰 실패: 입찰가가 현재 최고가보다 낮습니다.";
                bid.setBidResult(message);
                saveBid(bid);
                //bidRepository.save(bid);
                productAuctionResult.setIsSuccess(0);
                productAuctionResult.setMessage(message);
            } else if (bidPrice - nowPrice < termPrice) { // 입찰가 - 현재가 <= 최소 입찰 단가
                logger.info("[{}]<물품입찰실패 회원id: {} 물품id: {} 가격: {} 사유: 최소입찰단가 미만>", now, memberId, productId, bidPrice);
                String message="입찰 실패: 최소 입찰 단가보다 높게 입찰해주세요.";
                bid.setBidResult(message);
                saveBid(bid);
                //bidRepository.save(bid);
                productAuctionResult.setIsSuccess(0);
                productAuctionResult.setMessage(message);
            } else {
                int numBid=product.getNumBid()+1;

                List<String> updateEmailList = new ArrayList<>();
                String productUrl = "https://awscloudschool.online/product/" + productId;
                String productName = productRepository.findByProductId(productId).getProductName();
                //String beforeBidMemberName=memberRepository.findById((long) product.getBidMemberId()).orElse(null).getName();
                if (numBid!=1) {
                    updateEmailList.add(memberRepository.findById(Long.valueOf(product.getBidMemberId())).orElse(null).getEmail());

                    Map<String, Object> updateVariables = Map.of(
                            "productImage", "https://d3jzg5ylljnsnd.cloudfront.net/" + productId + "/1.jpg",
                            "productName", productName,
                            "currentPrice", nowPrice,
                            "productUrl", productUrl
                    );
                    emailSenderService.send("update", "[DAPANDA] 경매 최고가 변경 알림! " + productName, updateVariables, updateEmailList);
                }
                saveProduct(productId, bidPrice, memberId, numBid, now);
                bid.setIsSuccess(1);
                String message="입찰 성공!";
                bid.setBidResult(message);
                saveBid(bid);
                //bidRepository.save(bid);
                logger.info("[{}]<물품입찰성공 회원id: {} 물품id: {} 가격: {}>", now, memberId, productId, bidPrice);

                ProductResponseInfo.ProductUpdateResult productUpdateResult = new ProductResponseInfo.ProductUpdateResult();
                productUpdateResult.setHighestPrice(bidPrice);
                productUpdateResult.setUserName(memberRepository.findById((long) memberId).orElse(null).getName());
                productUpdateResult.setLastBidDate(nowString);
                productUpdateResult.setNumBid(numBid);

                productAuctionResult.setIsSuccess(1);
                productAuctionResult.setMessage(message);
                List<String> bidEmailList = new ArrayList<>();
                bidEmailList.add(memberRepository.findById(Long.valueOf(memberId)).orElse(null).getEmail());


                Map<String, Object> bidVariables = Map.of(
                        "productName", productName,
                        "productUrl", productUrl,
                        "highestPrice", bidPrice
                );

                emailSenderService.send("bid","[DAPANDA] 입찰 성공! " + productName, bidVariables, bidEmailList);

            }
        }
        //return null;
    }

    @Transactional
    public void saveBid(Bid bid) {
        bidRepository.save(bid);
    }

    @Transactional
    public void saveProduct(int productId, int bidPrice, int memberId, int numBid, LocalDateTime now) {
        Product product = productRepository.findById((long) productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setHighestPrice(bidPrice);
        product.setBidMemberId(memberId);
        product.setNumBid(numBid);
        product.setLastBidDate(now);
        productRepository.save(product);
    }

}
