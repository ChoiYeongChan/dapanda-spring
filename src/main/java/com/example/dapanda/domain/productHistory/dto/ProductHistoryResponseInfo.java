package com.example.dapanda.domain.productHistory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

public class ProductHistoryResponseInfo {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductHistoryResDto {
        private int productId;
        private int registerMemberId;
        private int endPrice;
        private int awardMemberId;
        private int numBid;
        private String productName;
        private LocalDateTime lastBidDate;
    }
}
