package com.deepdhamala.filmpatro.film.genre;

public class GenreMapper {
    public static GenreEntity toEntity(GenreAddRequestDto genreAddRequestDto) {
        return GenreEntity.builder()
                .name(genreAddRequestDto.getName())
                .build();
    }
}
