package com.example.dapanda.service;

import com.example.dapanda.domain.member.Member;
import com.example.dapanda.domain.member.dto.MemberRequestInfo;
import com.example.dapanda.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberCommandService {
    private final MemberRepository memberRepository;

    @Transactional
    public String saveMemberInfo(MemberRequestInfo.MemberReqInfo memberRefInfo) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        Member member = memberRepository.findByMemberString(memberRefInfo.getMemberString());
        if (member == null) {
            member = Member.builder()
                    .name(memberRefInfo.getName())
                    .phoneNum(memberRefInfo.getPhoneNum())
                    .address(memberRefInfo.getAddress())
                    .email(memberRefInfo.getEmail())
                    .memStatus((byte) 1)
                    .memberString(memberRefInfo.getMemberString())
                    .build();
            memberRepository.save(member);
            log.info("[{}]<회원가입 완료 회원id: {} 회원이름: {} >", now, member.getMemberString(), member.getName());
            return "회원정보 저장";
        }
        else {
            log.info("[{}]<회원가입 실패: 이미 존재하는 회원입니다. 회원id: {}>", now, member.getMemberString());
            return "회원정보 저장 실패";
        }
    }

    @Transactional
    public String modifyMemberInfo(MemberRequestInfo.MemberReqInfo memberReqInfo) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        Member member= memberRepository.findByMemberString(memberReqInfo.getMemberString());
        member.setName(memberReqInfo.getName());
        member.setPhoneNum(memberReqInfo.getPhoneNum());
        member.setAddress(memberReqInfo.getAddress());
        member.setEmail(memberReqInfo.getEmail());
        memberRepository.save(member);
        log.info("[{}]<회원정보 수정 회원id: {} 물품이름: {} >", now, member.getMemberString(), member.getName());
        return "회원정보 수정";
    }
}
