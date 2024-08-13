package com.example.dapanda.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ProductRequestInfo {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductReqInfo {
        private int productId;
        private int registerMemberId;
        private String category;
        private String productName;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private LocalDateTime lastBidDate;
        private int termPrice;
        private int startPrice;
        private int highestPrice;
        private Integer bidMemberId;
        private int numBid;
        private byte auctionStatus;
        private byte fileCount;
        private String productInfo;
        private int viewNum;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductPriceUpdateDto {
        private int productId;
        private int price;
        private int bidMemberId;
        private String replyTo;
        private String correlationId;
    }
}
