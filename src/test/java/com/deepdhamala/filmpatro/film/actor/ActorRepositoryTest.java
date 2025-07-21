package com.deepdhamala.filmpatro.film.actor;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

// This class is used to test the ActorRepository methods.

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ActorRepositoryTest {

    @Autowired
    private ActorRepository actorRepository;

    @Test
    void saveActor_ShouldPersistAndReturnActor(){

//        Arrange
        ActorEntity actorEntity = ActorEntity.builder()
                .name("Test Actor")
                .imageUrl("http://example.com/image.jpg")
                .build();

//        Act
        ActorEntity savedActor = actorRepository.save(actorEntity);

//        Assert
        Assertions.assertThat(savedActor).isNotNull();
        Assertions.assertThat(savedActor.getId()).isGreaterThan(0);
        Assertions.assertThat(savedActor.getName()).isEqualTo("Test Actor");
        Assertions.assertThat(savedActor.getImageUrl()).isEqualTo("http://example.com/image.jpg");
    }

    @Test
    void findById_ShouldReturnSavedActor(){
        ActorEntity actor = ActorEntity.builder()
                .name("Test Actor")
                .imageUrl("http://example.com/image.jpg")
                .build();
        ActorEntity savedActor = actorRepository.save(actor);

        Optional<ActorEntity> foundActor = actorRepository.findById(savedActor.getId());

        Assertions.assertThat(foundActor).isPresent();
        Assertions.assertThat(foundActor.get()).isEqualTo(savedActor);
    }

    @Test
    void deleteActor_ShouldRemoveFromDatabase() {
        ActorEntity actor = ActorEntity.builder()
                .name("Test Actor")
                .imageUrl("http://example.com/image.jpg")
                .build();
        ActorEntity saved = actorRepository.save(actor);

        actorRepository.delete(saved);
        Optional<ActorEntity> found = actorRepository.findById(saved.getId());

        Assertions.assertThat(found).isNotPresent();
    }


    @Test
    void findByNameContainingIgnoreCase_ShouldReturnActorNameByString() {
        ActorEntity actor1 = ActorEntity.builder()
                .name("Test Actor One")
                .imageUrl("http://example.com/image1.jpg")
                .build();
        ActorEntity actor2 = ActorEntity.builder()
                .name("Test Actor Two")
                .imageUrl("http://example.com/image2.jpg")
                .build();
        actorRepository.save(actor1);
        actorRepository.save(actor2);

        Iterable<ActorEntity> foundActors = actorRepository.findByNameContainingIgnoreCase("Test");

        Assertions.assertThat(foundActors).hasSize(2);
        Assertions.assertThat(foundActors).contains(actor1, actor2);
    }
}