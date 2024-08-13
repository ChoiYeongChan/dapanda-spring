package com.example.dapanda.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberRequestInfo {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberReqInfo {
        private String name;
        private String phoneNum;
        private String address;
        private String email;
        private String memberString;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberLoginReqDto {
        private String name;
    }
}
