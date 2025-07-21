package com.deepdhamala.filmpatro.film.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class SimpleGenreService implements GenreService{

    private final GenreRepository genreRepository;

    @Override
    public GenreEntity createGenre(GenreAddRequestDto genreAddRequestDto) {
        return genreRepository.save(GenreMapper.toEntity(genreAddRequestDto));
    }

    @Override
    public GenreEntity updateGenre(Long id, GenreEntity genre) {
        if (!genreRepository.existsById(id)) {
            throw new IllegalArgumentException("Genre with id " + id + " does not exist.");
        }
        genre.setId(id);
        return genreRepository.save(genre);
    }

    @Override
    public void deleteGenre(Long id) {
        if (!genreRepository.existsById(id)) {
            throw new IllegalArgumentException("Genre with id " + id + " does not exist.");
        }
        genreRepository.deleteById(id);
    }

    @Override
    public GenreEntity getGenreById(Long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Genre with id " + id + " does not exist."));
    }

    @Override
    public Iterable<GenreEntity> getAllGenres() {
        return genreRepository.findAll();
    }

    @Override
    public Iterable<GenreEntity> searchGenres(String query) {
        // Assuming a method exists in GenreRepository for searching genres by name
        return genreRepository.findByNameContainingIgnoreCase(query);
    }



}
