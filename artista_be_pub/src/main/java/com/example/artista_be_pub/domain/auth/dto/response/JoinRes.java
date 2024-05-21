package com.artista.main.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JoinRes {
    /**
     * 아이디
     */
    private String userId;

    /**
     * 이름
     */
    private String name;
}
