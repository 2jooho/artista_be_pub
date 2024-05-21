package com.artista.main.domain.gallery.dto.response;

import com.artista.main.domain.gallery.dto.SerchListDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SerchInfoRes {
    //작품 목록
    List<SerchListDto> artListDtoList;

}
