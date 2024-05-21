package com.artista.main.domain.star.dto.response;

import com.artista.main.domain.star.dto.StarInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StarInfoRes {
    /**
     * 스타 정보 목록
     */
    List<StarInfoDto> starInfoDtoList;
}
