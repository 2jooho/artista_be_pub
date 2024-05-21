package com.artista.main.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhoneAuthResultDto {
    String sRequestNumber;
    String sResponseNumber;
    String sAuthType;
    String sCipherTime;
    String name;
    String birth;
    String gender;
    String dupInfo;
    String connInfo;
    String phone;
    String mobileCompany;
    String sErrorCode;
}