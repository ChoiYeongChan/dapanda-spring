package com.example.dapanda.domain.productHistory;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name="product_history")
public class ProductHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;
    private int registerMemberId;
    private String category;
    private String productName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime lastBidDate;
    private int termPrice;
    private int startPrice;
    private int endPrice;
    private Integer awardMemberId;
    private int numBid;
    private byte auctionStatus;
    private byte fileCount;
    private String productInfo;
    private int viewNum;
    private int payStatus;
}
