package com.example.dapanda.domain.member;

import com.example.dapanda.domain.member.dto.MemberRequestInfo;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name="member")
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int memberId;
    private String name;
    private String phoneNum;
    private String address;
    private String email;
    private byte memStatus;
    private String memberString;
    public Member(MemberRequestInfo.MemberReqInfo memberRequestInfo) {
        //this.memberId=memberRequestInfo.getMemberId();
        this.name=memberRequestInfo.getName();
        this.phoneNum=memberRequestInfo.getPhoneNum();
        this.address=memberRequestInfo.getPhoneNum();
        this.email=memberRequestInfo.getEmail();
        //this.memStatus=memberRequestInfo.getMemStatus();
    }
}