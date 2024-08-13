package com.example.dapanda.repository;

import com.example.dapanda.domain.bid.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {


    @Query("SELECT b FROM Bid b WHERE b.bidProductId = ?1")
    List<Bid> findByBidProductId(int bidProductId);

    @Query ("SELECT b FROM Bid b WHERE b.bidMemberId = ?1")
    List<Bid> findByBidMemberId(int bidMemberId);

    Optional<Bid> findByTransactionId(String transactionId);

    //Optional<Bid> findByBidProductIdAndBidMemberId(int productId, int memberId);
}
