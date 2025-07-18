package com.deepdhamala.filmpatro.film.actor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ActorController.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false) // ✅ disable security filters like JWT
class ActorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActorService actorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createActor() throws Exception {
        ActorDto dto = new ActorDto();
        dto.setName("Tom Hanks");

        ActorEntity mockActor = ActorEntity.builder().id(1L).name("Tom Hanks").build();
        Mockito.when(actorService.createActor(any(ActorDto.class), any())).thenReturn(mockActor);

        MockMultipartFile imageFile = new MockMultipartFile(
                "image",
                "dummy.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "dummy content".getBytes()
        );

        MockMultipartFile actorJson = new MockMultipartFile(
                "actor",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(dto)
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/actors/create")
                        .file(imageFile)
                        .file(actorJson)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Actor Registration Successful!"))
                .andExpect(jsonPath("$.status").value("success"))
                .andDo(print());
    }

    @Test
    void getAllActors() throws Exception {
        ActorEntity actor = ActorEntity.builder().id(1L).name("Tom Hanks").build();
        Mockito.when(actorService.getAllActors()).thenReturn(List.of(actor));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/actors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Tom Hanks"))
                .andDo(print());
    }

    @Test
    void getActorById() throws Exception {
        ActorEntity actor = ActorEntity.builder().id(1L).name("Tom Hanks").build();
        Mockito.when(actorService.getActorById(1L)).thenReturn(actor);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/actors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Tom Hanks"))
                .andDo(print());
    }

    @Test
    void deleteActor() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/actors/1"))
                .andExpect(status().isNoContent()) // 204 No Content
                .andDo(print());
    }
}