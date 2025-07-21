package com.deepdhamala.filmpatro.film.genre;

public interface GenreService {
    GenreEntity createGenre(GenreAddRequestDto genreAddRequestDto);

    GenreEntity updateGenre(Long id, GenreEntity genre);

    void deleteGenre(Long id);

    GenreEntity getGenreById(Long id);

    Iterable<GenreEntity> getAllGenres();

    Iterable<GenreEntity> searchGenres(String query);
}
