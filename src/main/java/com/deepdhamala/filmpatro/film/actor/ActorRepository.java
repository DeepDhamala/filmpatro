package com.deepdhamala.filmpatro.film.actor;

import org.springframework.data.repository.CrudRepository;

public interface ActorRepository extends CrudRepository<ActorEntity, Long> {
    Iterable<ActorEntity> findByNameContainingIgnoreCase(String title);
}
