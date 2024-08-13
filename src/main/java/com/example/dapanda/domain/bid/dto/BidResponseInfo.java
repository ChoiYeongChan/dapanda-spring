package com.example.dapanda.domain.bid.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class BidResponseInfo {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BidResInfo {
        private int id;
        private int bidProductId;
        private int bidMemberId;
        private int bidPrice;
        private LocalDateTime bidDate;
        //private String transactionId;
        private int isSuccess;
    }
}
