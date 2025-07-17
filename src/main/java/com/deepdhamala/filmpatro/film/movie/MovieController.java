package com.deepdhamala.filmpatro.film.movie;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController(value = "/movies")
class MovieController {

    private final MovieRepository movieRepository;
    private final Utils utils;

    @PostMapping("/register")
    public MovieEntity registerMovie(RegisterMovieDto registerMovieDto) {
        var movieEntity = utils.toMovieEntity(registerMovieDto);
        return movieRepository.save(movieEntity);
    }
}
