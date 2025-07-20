package com.deepdhamala.filmpatro.film.actor;

import com.deepdhamala.filmpatro.storage.FileStorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimpleActorServiceTest {

    @Mock
    private ActorRepository actorRepository;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private SimpleActorService actorService;

    @Test
    void createActor_ShouldSaveActorWithImageUrl() {
        ActorDto dto = ActorDto.builder()
                .name("Tom Hanks")
                .build();

        MultipartFile file = mock(MultipartFile.class);
        String storedFileName = "tom_hanks.jpg";

        when(fileStorageService.storeFile(file)).thenReturn(storedFileName);

        ActorEntity savedActor = ActorEntity.builder()
                .name(dto.getName())
                .imageUrl(storedFileName)
                .build();

        when(actorRepository.save(any(ActorEntity.class))).thenReturn(savedActor);

        ActorEntity result = actorService.createActor(dto, file);

        assertNotNull(result);
        assertEquals(dto.getName(), result.getName());
        assertEquals(storedFileName, result.getImageUrl());

        verify(fileStorageService).storeFile(file);
        verify(actorRepository).save(any(ActorEntity.class));
    }

    @Test
    void updateActor_ShouldUpdateExistingActor() {
        Long actorId = 1L;

        ActorEntity updatedData = ActorEntity.builder()
                .name("Updated Name")
                .imageUrl("updated.jpg")
                .build();

        when(actorRepository.existsById(actorId)).thenReturn(true);
        when(actorRepository.save(any(ActorEntity.class))).thenReturn(updatedData);

        ActorEntity result = actorService.updateActor(actorId, updatedData);

        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertEquals("updated.jpg", result.getImageUrl());

        verify(actorRepository).existsById(actorId);
        verify(actorRepository).save(updatedData);
    }

    @Test
    void updateActor_ShouldThrowExceptionWhenActorNotFound() {
        Long actorId = 999L;
        ActorEntity actor = new ActorEntity();

        when(actorRepository.existsById(actorId)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            actorService.updateActor(actorId, actor);
        });

        assertEquals("Actor with id 999 does not exist.", exception.getMessage());
        verify(actorRepository).existsById(actorId);
        verify(actorRepository, never()).save(any());
    }

    @Test
    void deleteActor_ShouldDeleteIfExists() {
        Long actorId = 1L;

        when(actorRepository.existsById(actorId)).thenReturn(true);

        actorService.deleteActor(actorId);

        verify(actorRepository).existsById(actorId);
        verify(actorRepository).deleteById(actorId);
    }

    @Test
    void deleteActor_ShouldThrowExceptionWhenActorNotFound() {
        Long actorId = 999L;

        when(actorRepository.existsById(actorId)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            actorService.deleteActor(actorId);
        });

        assertEquals("Actor with id 999 does not exist.", exception.getMessage());
        verify(actorRepository, never()).deleteById(any());
    }

    @Test
    void getActorById_ShouldReturnActorIfExists() {
        Long actorId = 1L;
        ActorEntity actor = ActorEntity.builder()
                .id(actorId)
                .name("Tom Hanks")
                .build();

        when(actorRepository.findById(actorId)).thenReturn(Optional.of(actor));

        ActorEntity result = actorService.getActorById(actorId);

        assertNotNull(result);
        assertEquals(actorId, result.getId());
        assertEquals("Tom Hanks", result.getName());

        verify(actorRepository).findById(actorId);
    }

    @Test
    void getActorById_ShouldThrowExceptionIfNotFound() {
        Long actorId = 999L;

        when(actorRepository.findById(actorId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            actorService.getActorById(actorId);
        });

        assertEquals("Actor with id 999 does not exist.", exception.getMessage());
    }

    @Test
    void getAllActors_ShouldReturnAllActors() {
        List<ActorEntity> actors = Arrays.asList(
                ActorEntity.builder().name("Actor 1").build(),
                ActorEntity.builder().name("Actor 2").build()
        );

        when(actorRepository.findAll()).thenReturn(actors);

        List<ActorEntity> result = actorService.getAllActors();

        assertEquals(2, result.size());
        assertEquals("Actor 1", result.get(0).getName());
        assertEquals("Actor 2", result.get(1).getName());

        verify(actorRepository).findAll();
    }

    @Test
    void searchActors_ShouldReturnMatchingActors() {
        String query = "Tom";

        List<ActorEntity> matchingActors = List.of(
                ActorEntity.builder().name("Tom Hanks").build()
        );

        when(actorRepository.findByNameContainingIgnoreCase(query)).thenReturn(matchingActors);

        Iterable<ActorEntity> result = actorService.searchActors(query);

        assertNotNull(result);
        assertEquals("Tom Hanks", result.iterator().next().getName());

        verify(actorRepository).findByNameContainingIgnoreCase(query);
    }
}
