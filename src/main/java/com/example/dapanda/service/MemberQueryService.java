package com.example.dapanda.service;

import com.example.dapanda.domain.member.Member;
import com.example.dapanda.domain.member.dto.MemberRequestInfo;
import com.example.dapanda.domain.member.dto.MemberResponseInfo;
import com.example.dapanda.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberQueryService {
    private final MemberRepository memberRepository;

    public List<Member> findAllMembers() {
        log.info("모든 회원 출력");
        return memberRepository.findAll();
    }

//    public Member findMemberById(int memberId) {
//        log.info("회원 {} 출력",memberId);
//        return memberRepository.findByMemberId(memberId);
//    }


    public Optional<Member> findMemberByMemberString(String memberString) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        Member member = memberRepository.findByMemberString(memberString);

        if (member == null) {
            //log.info("[{}]<회원 로그인 실패: 해당 회원을 찾을 수 없습니다. findMemberByMemberString 회원id: {}>", now, memberString);
            return Optional.empty();
        }

        log.info("[{}]<회원 로그인 회원id: {} 회원이름: {} >", now, member.getMemberString(), member.getName());
        return Optional.of(member);
    }

    public Member findMemberByMemberName(MemberRequestInfo.MemberLoginReqDto memberLoginReqDto) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        Member member = memberRepository.findByName(memberLoginReqDto.getName());

        if (member == null) {
            //log.info("[{}]<회원 로그인 실패: 해당 회원을 찾을 수 없습니다. findMemberByMemberName 회원이름: {}>", now, memberLoginReqDto.getName());
            return null;
        }

        log.info("[{}]<회원 로그인 회원id: {} 회원이름: {} >", now, member.getMemberString(), member.getName());
        return member;
    }
}
