package com.deepdhamala.filmpatro.film.genre;

import com.deepdhamala.filmpatro.auth.jwt.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenreController.class)
@AutoConfigureMockMvc(addFilters = false)
class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GenreService genreService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    void createGenre_ShouldReturnSuccessMessage() throws Exception {
        GenreAddRequestDto dto = GenreAddRequestDto.builder()
                .name("Drama")
                .build();

        when(genreService.createGenre(any(GenreAddRequestDto.class)))
                .thenReturn(GenreEntity.builder().id(1L).name("Drama").build());

        mockMvc.perform(post("/api/v1/genre/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Genre Registration Successful!"));

        Mockito.verify(genreService).createGenre(any(GenreAddRequestDto.class));
    }

    @Test
    void getAllGenres_ShouldReturnList() throws Exception {
        List<GenreEntity> genres = List.of(
                GenreEntity.builder().id(1L).name("Action").build(),
                GenreEntity.builder().id(2L).name("Comedy").build()
        );
        when(genreService.getAllGenres()).thenReturn(genres);

        mockMvc.perform(get("/api/v1/genre/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].name").value("Action"))
                .andExpect(jsonPath("$.data[1].name").value("Comedy"));

        Mockito.verify(genreService).getAllGenres();
    }

    @Test
    void getGenreById_ShouldReturnGenre() throws Exception {
        Long genreId = 1L;
        GenreEntity genre = GenreEntity.builder().id(genreId).name("Drama").build();
        when(genreService.getGenreById(genreId)).thenReturn(genre);

        mockMvc.perform(get("/api/v1/genre/{id}", genreId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(genreId))
                .andExpect(jsonPath("$.data.name").value("Drama"));

        Mockito.verify(genreService).getGenreById(genreId);
    }

    @Test
    void deleteGenre_ShouldReturnSuccessMessage() throws Exception {
        Long genreId = 1L;
        doNothing().when(genreService).deleteGenre(genreId);

        mockMvc.perform(delete("/api/v1/genre/{id}", genreId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Genre deleted successfully"));

        Mockito.verify(genreService).deleteGenre(genreId);
    }

    @Test
    void updateGenre_ShouldReturnUpdatedGenre() throws Exception {
        Long genreId = 1L;
        GenreEntity updatedGenre = GenreEntity.builder().id(genreId).name("Updated Drama").build();

        when(genreService.updateGenre(any(Long.class), any(GenreEntity.class))).thenReturn(updatedGenre);

        mockMvc.perform(put("/api/v1/genre/{id}", genreId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedGenre)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(genreId))
                .andExpect(jsonPath("$.data.name").value("Updated Drama"));

        Mockito.verify(genreService).updateGenre(Mockito.eq(genreId), any(GenreEntity.class));
    }
}
