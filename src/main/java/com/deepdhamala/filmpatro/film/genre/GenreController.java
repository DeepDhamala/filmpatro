package com.deepdhamala.filmpatro.film.genre;

import com.deepdhamala.filmpatro.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/genre")
public class GenreController {

    private final GenreService genreService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<String>> createGenre(@Valid @RequestBody GenreAddRequestDto genreDto) {
        genreService.createGenre(genreDto);
        return ResponseEntity.ok(ApiResponse.success(null, "Genre Registration Successful!"));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<Iterable<GenreEntity>>> getAllGenres() {
        Iterable<GenreEntity> genres = genreService.getAllGenres();
        return ResponseEntity.ok(ApiResponse.success(genres, "Genres fetched successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GenreEntity>> getGenreById(@PathVariable Long id) {
        GenreEntity genre = genreService.getGenreById(id);
        return ResponseEntity.ok(ApiResponse.success(genre, "Genre fetched successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteGenre(@PathVariable Long id) {
        genreService.deleteGenre(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Genre deleted successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GenreEntity>> updateGenre(@PathVariable Long id, @Valid @RequestBody GenreEntity genre) {
        GenreEntity updatedGenre = genreService.updateGenre(id, genre);
        return ResponseEntity.ok(ApiResponse.success(updatedGenre, "Genre updated successfully"));
    }
}