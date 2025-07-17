package com.deepdhamala.filmpatro.film.movie;

import com.deepdhamala.filmpatro.film.actor.ActorEntity;
import com.deepdhamala.filmpatro.film.actor.ActorService;
import com.deepdhamala.filmpatro.film.genre.GenreEntity;
import com.deepdhamala.filmpatro.film.genre.GenreService;
import com.deepdhamala.filmpatro.film.media.Media;
import com.deepdhamala.filmpatro.film.media.MediaType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Utils {

    private final GenreService genreService;
    private final ActorService actorService;

    public MovieEntity toMovieEntity(RegisterMovieDto dto) {
        MovieEntity movie = new MovieEntity();
        movie.setTitle(dto.getTitle());
        movie.setDescription(dto.getDescription());
        movie.setReleaseYear(dto.getReleaseYear());

        Set<GenreEntity> genres = dto.getGenreIds().stream()
                .map(genreService::getGenreById)
                .collect(Collectors.toSet());

        Set<ActorEntity> actors = dto.getActorIds().stream()
                .map(actorService::getActorById)
                .collect(Collectors.toSet());

        movie.setGenres(genres);
        movie.setActors(actors);

        List<Media> media = dto.getMediaUrls().stream()
                .map(url -> new Media(url, movie, MediaType.IMAGE))
                .collect(Collectors.toList());
        movie.setMedia(media);

        return movie;
    }

}
