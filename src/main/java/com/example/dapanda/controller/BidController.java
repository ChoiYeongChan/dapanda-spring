package com.example.dapanda.controller;

import com.example.dapanda.domain.bid.Bid;
import com.example.dapanda.domain.bid.dto.BidRequestInfo;
import com.example.dapanda.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.json.JSONObject;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/spring")
public class BidController {

    private final BidService bidService;

//    @GetMapping("/bid")
//    public ResponseEntity<List<Bid>> getAllBids() {
//        return ResponseEntity.ok(bidService.findAllBids());
//    }

//    @GetMapping("/bid/product/{bidProductId}")
//    public ResponseEntity<List<Bid>> getBidByProductId(@PathVariable int bidProductId) {
//        return ResponseEntity.ok(bidService.findBidByProductId(bidProductId));
//    }

//    @GetMapping("/bid/member/{bidMemberId}")
//    public ResponseEntity<List<Bid>> getBidByMemberId(@PathVariable int bidMemberId) {
//        return ResponseEntity.ok(bidService.findBidByMemberId(bidMemberId));
//    }

    @PostMapping("/bid")
    public ResponseEntity<String> requestBid(@RequestPart(value="BidReqInfo") BidRequestInfo.BidReqInfo bidReqInfo) {
        return ResponseEntity.ok(bidService.requestBid(bidReqInfo));
    }

    @GetMapping("/bid/{transactionId}")
    public ResponseEntity<Optional<Bid>> getBidByTransactionId(@PathVariable String transactionId) {
        return ResponseEntity.ok(bidService.findBidByTransactionId(transactionId));
    }

    @PostMapping("/ses")
    public ResponseEntity<String> sendEmail(@RequestBody String productId) {
        JSONObject jsonObj = new JSONObject(productId);
        int parsedProductId = jsonObj.getInt("productId");
        return ResponseEntity.ok(bidService.sendEmail(parsedProductId));
    }
}
