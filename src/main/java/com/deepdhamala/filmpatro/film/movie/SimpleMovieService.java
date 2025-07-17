package com.deepdhamala.filmpatro.film.movie;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SimpleMovieService implements MovieService{

    private final MovieRepository movieRepository;

    @Override
    public MovieEntity createMovie(MovieEntity movieEntity) {
        return movieRepository.save(movieEntity);
    }
    @Override
    public MovieEntity updateMovie(Long id, MovieEntity movieEntity) {
        if (!movieRepository.existsById(id)) {
            throw new IllegalArgumentException("Movie with ID " + id + " does not exist.");
        }
        movieEntity.setId(id);
        return movieRepository.save(movieEntity);
    }
    @Override
    public void deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new IllegalArgumentException("Movie with ID " + id + " does not exist.");
        }
        movieRepository.deleteById(id);
    }
    @Override
    public MovieEntity getMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie with ID " + id + " does not exist."));
    }
    @Override
    public List<MovieEntity> getAllMovies() {
        return (List<MovieEntity>) movieRepository.findAll();
    }
    @Override
    public List<MovieEntity> searchMovies(String query) {
        return movieRepository.findByTitleContainingIgnoreCase(query);
    }
    @Override
    public List<MovieEntity> getMoviesByGenre(String genreName) {
        return movieRepository.findByGenresName(genreName);
    }
    @Override
    public List<MovieEntity> getMoviesByActor(String actorName) {
        return movieRepository.findByActorsName(actorName);
    }

}
