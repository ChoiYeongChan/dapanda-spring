package com.example.dapanda.service;

//import com.example.dapanda.config.WebSocketHandler;
import com.example.dapanda.domain.bid.Bid;
import com.example.dapanda.domain.bid.dto.BidRequestInfo;
import com.example.dapanda.domain.member.Member;
import com.example.dapanda.domain.product.Product;
import com.example.dapanda.domain.product.dto.ProductRequestInfo;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.transaction.annotation.Transactional;
import com.example.dapanda.domain.product.dto.ProductResponseInfo;
import com.example.dapanda.repository.BidRepository;
import com.example.dapanda.repository.MemberRepository;
import com.example.dapanda.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
//import org.springframework.web.socket.WebSocketSession;
import io.awspring.cloud.sqs.annotation.SqsListener;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductQueryService {

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;


    public List<Product> findAllProducts() {
        //log.info("모든 물품 출력");
        return productRepository.findAll();
    }

    public Product findProductByProductId(int productId) {
        //log.info("물품 {} 출력", productId);
        return productRepository.findByProductId(productId);
    }

    public ProductResponseInfo.ProductPriceResDto findPriceByProductId(int productId) {
        //log.info("물품 {}의 가격 정보 출력", productId);
        Product product = productRepository.findByProductId(productId);
        Member member= memberRepository.findById((long) product.getBidMemberId()).orElse(null);
        return new ProductResponseInfo.ProductPriceResDto(
                product.getProductId(),
                product.getStartPrice(),
                product.getHighestPrice(),
                product.getTermPrice(),
                product.getNumBid(),
                member.getName()
        );
    }

    public List<ProductResponseInfo.ProductAllPriceResDto> getAllPrice() {
        //log.info("모든 물품의 가격 정보 출력");
        List<Product> product=productRepository.findAll();
        List<ProductResponseInfo.ProductAllPriceResDto> productAllPriceResDto = new ArrayList<>();
        for(Product p:product){
            productAllPriceResDto.add(new ProductResponseInfo.ProductAllPriceResDto(
                    p.getProductId(),
                    p.getHighestPrice()
            ));
        }
        return productAllPriceResDto;
    }

    public ProductResponseInfo.ProductDetailResDto getProductDetail(int productId) {
        //log.info("물품 {}의 상세 정보 출력", productId);
        Product product = productRepository.findByProductId(productId);
        Member member= memberRepository.findById((long) product.getBidMemberId()).orElse(null);
        return new ProductResponseInfo.ProductDetailResDto (
                product.getProductId(),
                product.getLastBidDate(),
                product.getHighestPrice(),
                member.getName(),
                product.getNumBid()
        );
    }


}
