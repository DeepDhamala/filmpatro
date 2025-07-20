// src/test/java/com/deepdhamala/filmpatro/film/actor/ActorControllerTest.java
package com.deepdhamala.filmpatro.film.actor;

import com.deepdhamala.filmpatro.auth.jwt.JwtService;
import com.deepdhamala.filmpatro.common.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ActorController.class)
@AutoConfigureMockMvc(addFilters = false)
class ActorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ActorService actorService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createActor_ShouldReturnSuccessMessage() throws Exception {
        ActorDto dto = ActorDto.builder().name("Tom Hanks").build();

        MockMultipartFile actorPart = new MockMultipartFile("actor", "", "application/json",
                objectMapper.writeValueAsBytes(dto));
        MockMultipartFile imagePart = new MockMultipartFile("image", "tom.jpg", "image/jpeg", "fake-image".getBytes());

        mockMvc.perform(multipart("/api/v1/actors/create")
                        .file(actorPart)
                        .file(imagePart)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Actor Registration Successful!"));

        verify(actorService).createActor(any(ActorDto.class), any());
    }

    @Test
    void getAllActors_ShouldReturnListOfActors() throws Exception {
        List<ActorEntity> actors = List.of(
                ActorEntity.builder().id(1L).name("Actor 1").build(),
                ActorEntity.builder().id(2L).name("Actor 2").build()
        );
        when(actorService.getAllActors()).thenReturn(actors);

        mockMvc.perform(get("/api/v1/actors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Actor 1"))
                .andExpect(jsonPath("$[1].name").value("Actor 2"));

        verify(actorService).getAllActors();
    }

    @Test
    void getActorById_ShouldReturnActor() throws Exception {
        Long actorId = 1L;
        ActorEntity actor = ActorEntity.builder().id(actorId).name("Tom Hanks").build();
        when(actorService.getActorById(actorId)).thenReturn(actor);

        mockMvc.perform(get("/api/v1/actors/{id}", actorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(actorId))
                .andExpect(jsonPath("$.name").value("Tom Hanks"));

        verify(actorService).getActorById(actorId);
    }

    @Test
    void deleteActor_ShouldReturnNoContent() throws Exception {
        Long actorId = 1L;
        doNothing().when(actorService).deleteActor(actorId);

        mockMvc.perform(delete("/api/v1/actors/{id}", actorId))
                .andExpect(status().isNoContent());

        verify(actorService).deleteActor(actorId);
    }
}