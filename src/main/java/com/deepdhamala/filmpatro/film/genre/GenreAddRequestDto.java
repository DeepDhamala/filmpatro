package com.deepdhamala.filmpatro.film.genre;

import jakarta.validation.Valid;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenreAddRequestDto {

    @Valid
    private String name;
}
