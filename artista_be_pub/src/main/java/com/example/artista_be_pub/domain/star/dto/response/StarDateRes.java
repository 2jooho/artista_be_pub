package com.artista.main.domain.star.dto.response;

import com.artista.main.domain.star.dto.StarDateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StarDateRes {
    List<StarDateDto> starDateDtoList;
}
