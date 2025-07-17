package com.deepdhamala.filmpatro.film.movie;

import java.util.List;

public interface MovieService {
    MovieEntity createMovie(MovieEntity movie);

    MovieEntity updateMovie(Long id, MovieEntity movie);

    void deleteMovie(Long id);

    MovieEntity getMovieById(Long id);

    List<MovieEntity> getAllMovies();

    List<MovieEntity> searchMovies(String query);

    List<MovieEntity> getMoviesByGenre(String genreName);

    List<MovieEntity> getMoviesByActor(String actorName);
}
