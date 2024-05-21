package com.artista.main.domain.collabor.dto.response;

import com.artista.main.domain.collabor.dto.CollaborInfoDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollaborInfoRes {
    List<CollaborInfoDto> collaborInfoDtoList;
}
