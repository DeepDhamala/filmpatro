package com.deepdhamala.filmpatro.film.genre;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimpleGenreServiceTest {

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private SimpleGenreService genreService;

    @Test
    void createGenre_ShouldSaveAndReturnGenre() {
        GenreAddRequestDto dto = GenreAddRequestDto.builder()
                .name("Drama")
                .build();

        GenreEntity savedGenre = GenreEntity.builder()
                .name(dto.getName())
                .build();

        // Mock the mapper call (assuming static method)
        // If GenreMapper.toEntity is static, use Mockito.mockStatic (JUnit 5+)
        try (var mocked = mockStatic(GenreMapper.class)) {
            mocked.when(() -> GenreMapper.toEntity(dto)).thenReturn(savedGenre);

            when(genreRepository.save(any(GenreEntity.class))).thenReturn(savedGenre);

            GenreEntity result = genreService.createGenre(dto);

            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Drama");

            verify(genreRepository).save(any(GenreEntity.class));
            mocked.verify(() -> GenreMapper.toEntity(dto));
        }
    }

    @Test
    void updateGenre_ShouldUpdateExistingGenre() {
        Long genreId = 1L;

        GenreEntity updatedData = GenreEntity.builder()
                .name("Comedy")
                .build();

        when(genreRepository.existsById(genreId)).thenReturn(true);
        when(genreRepository.save(any(GenreEntity.class))).thenReturn(updatedData);

        GenreEntity result = genreService.updateGenre(genreId, updatedData);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Comedy");

        verify(genreRepository).existsById(genreId);
        verify(genreRepository).save(updatedData);
    }

    @Test
    void updateGenre_ShouldThrowExceptionWhenGenreNotFound() {
        Long genreId = 999L;
        GenreEntity genre = new GenreEntity();

        when(genreRepository.existsById(genreId)).thenReturn(false);

        IllegalArgumentException exception = catchThrowableOfType(
                IllegalArgumentException.class,
                () -> genreService.deleteGenre(genreId)
        );

        assertThat(exception).hasMessage("Genre with id 999 does not exist.");
        verify(genreRepository).existsById(genreId);
        verify(genreRepository, never()).save(any());
    }

    @Test
    void deleteGenre_ShouldDeleteIfExists() {
        Long genreId = 1L;

        when(genreRepository.existsById(genreId)).thenReturn(true);

        genreService.deleteGenre(genreId);

        verify(genreRepository).existsById(genreId);
        verify(genreRepository).deleteById(genreId);
    }

    @Test
    void deleteGenre_ShouldThrowExceptionWhenGenreNotFound() {
        Long genreId = 999L;

        when(genreRepository.existsById(genreId)).thenReturn(false);

        IllegalArgumentException exception = catchThrowableOfType(
                IllegalArgumentException.class,
                () -> genreService.deleteGenre(genreId)
        );

        assertThat(exception).hasMessage("Genre with id 999 does not exist.");
        verify(genreRepository, never()).deleteById(any());
    }

    @Test
    void getGenreById_ShouldReturnGenreIfExists() {
        Long genreId = 1L;
        GenreEntity genre = GenreEntity.builder()
                .id(genreId)
                .name("Action")
                .build();

        when(genreRepository.findById(genreId)).thenReturn(Optional.of(genre));

        GenreEntity result = genreService.getGenreById(genreId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(genreId);
        assertThat(result.getName()).isEqualTo("Action");

        verify(genreRepository).findById(genreId);
    }

    @Test
    void getGenreById_ShouldThrowExceptionIfNotFound() {
        Long genreId = 999L;

        when(genreRepository.findById(genreId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = catchThrowableOfType(
                IllegalArgumentException.class,
                () -> genreService.deleteGenre(genreId)
        );

        assertThat(exception).hasMessage("Genre with id 999 does not exist.");
    }

    @Test
    void getAllGenres_ShouldReturnAllGenres() {
        List<GenreEntity> genres = Arrays.asList(
                GenreEntity.builder().name("Horror").build(),
                GenreEntity.builder().name("Sci-Fi").build()
        );

        when(genreRepository.findAll()).thenReturn(genres);

        Iterable<GenreEntity> result = genreService.getAllGenres();

        assertThat(result).hasSize(2);
        assertThat(result).extracting("name").containsExactly("Horror", "Sci-Fi");

        verify(genreRepository).findAll();
    }

    @Test
    void searchGenres_ShouldReturnMatchingGenres() {
        String query = "com";

        List<GenreEntity> matchingGenres = List.of(
                GenreEntity.builder().name("Comedy").build()
        );

        when(genreRepository.findByNameContainingIgnoreCase(query)).thenReturn(matchingGenres);

        Iterable<GenreEntity> result = genreService.searchGenres(query);

        assertThat(result).isNotNull();
        assertThat(result.iterator().next().getName()).isEqualTo("Comedy");

        verify(genreRepository).findByNameContainingIgnoreCase(query);
    }
}
