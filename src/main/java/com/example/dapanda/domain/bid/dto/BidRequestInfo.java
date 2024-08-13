package com.example.dapanda.domain.bid.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class BidRequestInfo {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BidReqInfo {
        private int bidProductId;
        private int bidMemberId;
        private int bidPrice;
        //private LocalDateTime bidDate;
        //private String transactionId;
        //private int isSuccess;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransactionReqInfo {
        private int bidProductId;
        private int bidMemberId;
        private int bidPrice;
        private LocalDateTime bidDate;
        private String transactionId;
        private int isSuccess;
    }
}
