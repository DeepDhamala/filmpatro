package com.deepdhamala.filmpatro.film.actor;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ActorService {
    ActorEntity createActor(ActorDto actorDto, MultipartFile imageFile);

    ActorEntity updateActor(Long id, ActorEntity actor);

    void deleteActor(Long id);

    ActorEntity getActorById(Long id);

    List<ActorEntity> getAllActors();

    Iterable<ActorEntity> searchActors(String query);
}
