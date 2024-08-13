package com.example.dapanda.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberResponseInfo {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public class MemberResInfo {
        private int memberId;
        private String name;
        private String phoneNum;
        private String address;
        private String email;
        private byte memStatus;
    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberLoginResInfo {
        private int memberId;
        private String name;
    }
}