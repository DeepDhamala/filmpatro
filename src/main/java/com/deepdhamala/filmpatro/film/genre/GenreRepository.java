package com.deepdhamala.filmpatro.film.genre;

import org.springframework.data.repository.CrudRepository;

public interface GenreRepository extends CrudRepository<GenreEntity,Long> {

    Iterable<GenreEntity> findByNameContainingIgnoreCase(String name);

}
