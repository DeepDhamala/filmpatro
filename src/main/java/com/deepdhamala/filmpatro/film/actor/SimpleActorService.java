package com.deepdhamala.filmpatro.film.actor;

import com.deepdhamala.filmpatro.storage.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SimpleActorService implements ActorService{

    private final ActorRepository actorRepository;
    private final FileStorageService fileStorageService;

    @Override
    public ActorEntity createActor(ActorDto dto, MultipartFile multipartFile) {
        String fileName = fileStorageService.storeFile(multipartFile);
        ActorEntity actor = ActorEntity.builder()
                .name(dto.getName())
                .imageUrl(fileName)
                .build();
        return actorRepository.save(actor);
    }

    @Override
    public ActorEntity updateActor(Long id, ActorEntity actor) {
        if (!actorRepository.existsById(id)) {
            throw new IllegalArgumentException("Actor with id " + id + " does not exist.");
        }
        actor.setId(id);
        return actorRepository.save(actor);
    }

    @Override
    public void deleteActor(Long id) {
        if (!actorRepository.existsById(id)) {
            throw new IllegalArgumentException("Actor with id " + id + " does not exist.");
        }
        actorRepository.deleteById(id);
    }

    @Override
    public ActorEntity getActorById(Long id) {
        return actorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Actor with id " + id + " does not exist."));
    }

    @Override
    public List<ActorEntity> getAllActors() {
        List<ActorEntity> actors = new ArrayList<>();
        actorRepository.findAll().forEach(actors::add);
        return actors;
    }

    @Override
    public Iterable<ActorEntity> searchActors(String query) {
        // Assuming a method exists in ActorRepository for searching actors by name
        return actorRepository.findByNameContainingIgnoreCase(query);
    }

}
