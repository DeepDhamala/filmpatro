package com.deepdhamala.filmpatro.film.actor;

import com.deepdhamala.filmpatro.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/actors")
@RequiredArgsConstructor
class ActorController {

    private final ActorService actorService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<String>> createActor(@RequestPart("actor") ActorDto actorDto, @RequestPart("image")MultipartFile imageFile) {
        actorService.createActor(actorDto, imageFile);
        return ResponseEntity.ok(ApiResponse.success(null, "Actor Registration Successful!"));
    }

    @GetMapping
    public ResponseEntity<List<ActorEntity>> getAllActors() {
        return ResponseEntity.ok(actorService.getAllActors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActorEntity> getActorById(@PathVariable Long id) {
        return ResponseEntity.ok(actorService.getActorById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActor(@PathVariable Long id) {
        actorService.deleteActor(id);
        return ResponseEntity.noContent().build();
    }

}
