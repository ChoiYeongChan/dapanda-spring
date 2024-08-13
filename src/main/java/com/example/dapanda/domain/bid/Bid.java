package com.example.dapanda.domain.bid;

import com.example.dapanda.domain.bid.dto.BidRequestInfo;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name="bid")
public class Bid {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int bidProductId;
    private int bidMemberId;
    private int bidPrice;
    private LocalDateTime bidDate;
    private String transactionId;
    private int isSuccess;
    private String bidResult;
    public Bid(BidRequestInfo.BidReqInfo bidReqInfo) {
        this.bidProductId = bidProductId;
        this.bidMemberId = bidMemberId;
        this.bidPrice = bidPrice;
        this.bidDate = bidDate;
    }
}
