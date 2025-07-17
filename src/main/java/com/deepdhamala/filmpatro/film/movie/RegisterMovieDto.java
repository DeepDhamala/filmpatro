package com.deepdhamala.filmpatro.film.movie;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterMovieDto {
    @NotBlank
    private String title;

    private String description;

    private Integer releaseYear;

    private Set<Long> genreIds;

    private Set<Long> actorIds;

    private List<String> mediaUrls;
}
