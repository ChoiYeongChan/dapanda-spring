package com.example.dapanda.domain.product;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name="product")
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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
