package com.artista.main.domain.gallery.dto.response;

import com.artista.main.domain.gallery.dto.ArtListDto;
import com.artista.main.domain.gallery.dto.PopularArtDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GalleryInfoRes {
    //인기 작품 리스트 (최대 3개)
    List<PopularArtDto> popularArtList;

    //갤러리 목록
    List<ArtListDto> artListDtoList;

}
