package com.example.dapanda.controller;

import com.example.dapanda.domain.member.Member;
import com.example.dapanda.domain.member.dto.MemberRequestInfo;
import com.example.dapanda.service.MemberCommandService;
import com.example.dapanda.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/spring")
public class MemberController {
    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;
    //private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

//    @GetMapping("/member")
//    public ResponseEntity<List<Member>> getAllMembers() {
//        //logger.info("모든 회원 출력");
//        return ResponseEntity.ok(memberQueryService.findAllMembers());
//    }

    @GetMapping("/member/{memberString}")
    public ResponseEntity<Optional<Member>> getMemberById(@PathVariable String memberString) {
        //logger.info("선택 회원 출력");
        return ResponseEntity.ok(memberQueryService.findMemberByMemberString(memberString));
    }

    @PostMapping("/member/test")
    public ResponseEntity<Member> getMemberByName(@RequestPart(value="MemberLoginReqDto")MemberRequestInfo.MemberLoginReqDto memberLoginReqDto) {
        //logger.info("선택 회원 출력");
        return ResponseEntity.ok(memberQueryService.findMemberByMemberName(memberLoginReqDto));
    }

    @PostMapping("/member")
    public ResponseEntity<String> saveMemberInfo(@RequestPart(value="MemberReqInfo")MemberRequestInfo.MemberReqInfo memberReqInfo) {
        //logger.info("로그인");
        return ResponseEntity.ok(memberCommandService.saveMemberInfo(memberReqInfo));
    }

    @PostMapping("/member/modify")
    public ResponseEntity<String> modifyMemberInfo(@RequestPart(value="MemberReqInfo")MemberRequestInfo.MemberReqInfo memberReqInfo) {
        return ResponseEntity.ok(memberCommandService.modifyMemberInfo(memberReqInfo));
    }

}
