package com.deepdhamala.filmpatro.film.genre;

import org.junit.jupiter.api.Test;

import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    @Test
    void saveGenre_ShouldPersistAndReturnGenre() {
        GenreEntity genre = GenreEntity.builder()
                .name("Action")
                .build();

        GenreEntity saved = genreRepository.save(genre);

        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.getId()).isGreaterThan(0);
        Assertions.assertThat(saved.getName()).isEqualTo("Action");
    }

    @Test
    void findById_ShouldReturnGenre() {
        GenreEntity genre = GenreEntity.builder()
                .name("Drama")
                .build();
        GenreEntity saved = genreRepository.save(genre);

        Optional<GenreEntity> found = genreRepository.findById(saved.getId());

        Assertions.assertThat(found).isPresent();
        Assertions.assertThat(found.get().getName()).isEqualTo("Drama");
    }

    @Test
    void deleteGenre_ShouldRemoveGenre() {
        GenreEntity genre = GenreEntity.builder()
                .name("Horror")
                .build();
        GenreEntity saved = genreRepository.save(genre);

        genreRepository.delete(saved);

        Optional<GenreEntity> found = genreRepository.findById(saved.getId());

        Assertions.assertThat(found).isNotPresent();
    }

    @Test
    void findByNameContainingIgnoreCase_ShouldReturnMatchingGenres() {
        GenreEntity genre1 = GenreEntity.builder().name("Comedy").build();
        GenreEntity genre2 = GenreEntity.builder().name("Romantic Comedy").build();

        genreRepository.save(genre1);
        genreRepository.save(genre2);

        Iterable<GenreEntity> found = genreRepository.findByNameContainingIgnoreCase("comedy");

        Assertions.assertThat(found).hasSize(2);
        Assertions.assertThat(found).contains(genre1, genre2);
    }
}
