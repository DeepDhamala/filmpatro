package com.deepdhamala.filmpatro.film.movie;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MovieRepository extends CrudRepository<MovieEntity, Long> {

    List<MovieEntity> findByGenresName(String genreName);
    List<MovieEntity> findByActorsName(String actorName);
    List<MovieEntity> findByTitleContainingIgnoreCase(String title);
}
