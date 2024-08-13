package com.example.dapanda.domain.product.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ProductResponseInfo {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductResInfo {
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
    public static class ProductPriceResDto {
        private int productId;
        private int startPrice;
        private int highestPrice;
        private int termPrice;
        private int numBid;
        private String bidMemberName;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductAllPriceResDto {
        private int productId;
        private int startPrice;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductDetailResDto {
        private int productId;
        private LocalDateTime lastBidDate;
        private int highestPrice;
        private String bidMemberName;
        private int numBid;
    }

    @Data
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductAuctionResult implements Serializable {
        private static final long serialVersionUID = 1L;
        private int isSuccess;
        private String message;
    }

    @Data
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductUpdateResult implements Serializable {
        private static final long serialVersionUID = 1L;
        private String userName;
        //private LocalDateTime lastBidDate;
        String lastBidDate;
        private int highestPrice;
        private int numBid;
    }

}
