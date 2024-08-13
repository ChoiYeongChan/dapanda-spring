package com.example.dapanda.service;

import com.example.dapanda.domain.bid.Bid;
import com.example.dapanda.domain.bid.dto.BidRequestInfo;
import com.example.dapanda.domain.member.Member;
import com.example.dapanda.domain.productHistory.ProductHistory;
import com.example.dapanda.domain.productHistory.dto.ProductHistoryResponseInfo;
import com.example.dapanda.repository.BidRepository;
import com.example.dapanda.repository.MemberRepository;
import com.example.dapanda.repository.ProductHistoryRepository;
import com.example.dapanda.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BidService {

    @Value("${cloud.aws.credentials.accessKey}")
    private String AWS_ACCESS_KEY;

    @Value("${cloud.aws.credentials.secretKey}")
    private String AWS_SECRET_KEY;

    @Value("${cloud.aws.region.static}")
    private Region AWS_REGION;

    @Value("${cloud.aws.sqs.queue.url}")
    private String queueUrl;

    private SqsClient sqsClient;


    @PostConstruct
    private void init() {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(AWS_ACCESS_KEY, AWS_SECRET_KEY);
        sqsClient = SqsClient.builder()
                .region(AWS_REGION)
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    }

    private final BidRepository bidRepository;
    private final ProductHistoryRepository productHistoryRepository;
    private final MemberRepository memberRepository;

    private final EmailSenderService emailSenderService;

    public String requestBid(BidRequestInfo.BidReqInfo bidReqInfo) {
        int bidProductId = bidReqInfo.getBidProductId();
        int bidMemberId = bidReqInfo.getBidMemberId();
        int bidPrice = bidReqInfo.getBidPrice();
        String transactionId = RandomStringUtils.randomAlphanumeric(20);
        if (bidMemberId == 0) {
            return "로그인이 필요합니다.";
        } else {
            LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

            log.info("입찰자 : {}", bidMemberId);
            log.info("{}", transactionId);
            //BidRequestInfo.TransactionReqInfo transactionReqInfo = new BidRequestInfo.TransactionReqInfo(bidProductId, bidMemberId, bidPrice, now, transactionId, 0);

            Map<String, Object> message = new HashMap<>();
            message.put("bidProductId", bidProductId);
            message.put("bidMemberId", bidMemberId);
            message.put("bidPrice", bidPrice);
            message.put("transactionId", transactionId);
            message.put("bidDate", now.toString());
            message.put("isSuccess", 0);

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String messageBody = objectMapper.writeValueAsString(message);
                String messageGroupId = "bid_group";
                String messageDeduplicationId = Integer.toString(messageBody.hashCode());

                SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                        .queueUrl(queueUrl)
                        .messageBody(messageBody)
                        .messageGroupId(messageGroupId)
                        .messageDeduplicationId(messageDeduplicationId)
                        .build();

                SendMessageResponse response = sqsClient.sendMessage(sendMsgRequest);
                log.info("response : {} ",response);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return transactionId;
    }

    public Optional<Bid> findBidByTransactionId(String transactionId) {
        return bidRepository.findByTransactionId(transactionId);
    }

    public String sendEmail(int productId) {
        ProductHistory productHistoryResDto= productHistoryRepository.findProductHistoryByProductId(productId);
        int registerMemberId = productHistoryResDto.getRegisterMemberId();
        int endPrice = productHistoryResDto.getEndPrice();
        int awardMemberId = productHistoryResDto.getAwardMemberId();
        int numBid = productHistoryResDto.getNumBid();
        String productName = productHistoryResDto.getProductName();
        LocalDateTime lastBidDate = productHistoryResDto.getLastBidDate();
        String productUrl="https://awscloudschool.online/product_history/"+productId;

        Member registerMember = memberRepository.findByMemberId(registerMemberId);
        String registerMemberEmail = registerMember.getEmail();
        List<String> registerEmailList = new ArrayList<>();
        registerEmailList.add(registerMemberEmail);
        if (numBid==0) { // 유찰
            Map<String, Object> failVariables = Map.of(
                    "productName", productName,
                    "productUrl", productUrl
            );
            emailSenderService.send("fail","[DAPANDA] 물품 유찰 안내 " + productName,failVariables,registerEmailList);
        }
        else { // 낙찰
            log.info("numBid : {} 낙찰", numBid);
            Member awardMember = memberRepository.findByMemberId(awardMemberId);
            String deliveryAddress = awardMember.getAddress();
            String awardMemberEmail = awardMember.getEmail();
            List<String> awardEmailList = new ArrayList<>();
            awardEmailList.add(awardMemberEmail);
            Map<String, Object> successVariables = Map.of(
                    "productName", productName,
                    "productUrl", productUrl,
                    "bidPrice", endPrice,
                    "deliveryAddress", deliveryAddress,
                    "purchaseTime", lastBidDate
            );
            emailSenderService.send("success","[DAPANDA] 낙찰을 축하합니다! " + productName,successVariables,awardEmailList);
            log.info("낙찰 이메일 전송 완료");
            Map<String, Object> registerVariables = Map.of(
                    "productName", productName,
                    "productUrl", productUrl,
                    "bidPrice", endPrice,
                    "address", deliveryAddress
            );
            emailSenderService.send("sell","[DAPANDA] 물품 판매 완료 안내 " + productName, registerVariables, registerEmailList);
            log.info("판매 이메일 전송 완료");
        }
        return "메일 전송 완료";
    }
}
