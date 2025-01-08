package com.emse.washing_mechine.api;

import com.emse.washing_mechine.dao.MachineDao;
import com.emse.washing_mechine.model.MachineEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

@WebMvcTest(MachineController.class)
class MachineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MachineDao machineDao;

    @Test
    void shouldFindAll() throws Exception {
        Mockito.when(machineDao.findAll()).thenReturn(List.of(
                new MachineEntity(1L, "Machine1", true, 15.0),
                new MachineEntity(2L, "Machine2", false, 20.0)
        ));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/machines").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("[*].name")
                        .value(Matchers.containsInAnyOrder("Machine1", "Machine2")));
    }

    @Test
    void shouldReturnNullWhenFindByUnknownId() throws Exception {
        Mockito.when(machineDao.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/machines/999").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(""));
    }

    @Test
    void shouldFindById() throws Exception {
        MachineEntity machineEntity = new MachineEntity(1L, "Machine1", true, 15.0);
        Mockito.when(machineDao.findById(1L)).thenReturn(Optional.of(machineEntity));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/machines/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Machine1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isUsed").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeLeft").value(15.0));
    }

    @Test
    void shouldNotUpdateUnknownEntity() throws Exception {
        MachineCommand command = new MachineCommand("Machine1", true, 15.0);
        String json = objectMapper.writeValueAsString(command);

        Mockito.when(machineDao.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/machines/1")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void shouldUpdate() throws Exception {
        MachineEntity machineEntity = new MachineEntity(1L, "Machine1", true, 15.0);
        MachineCommand command = new MachineCommand("UpdatedMachine", false, 10.0);
        String json = objectMapper.writeValueAsString(command);

        Mockito.when(machineDao.findById(1L)).thenReturn(Optional.of(machineEntity));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/machines/1")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("UpdatedMachine"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isUsed").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeLeft").value(10.0));
    }

    @Test
    void shouldCreate() throws Exception {
        MachineCommand command = new MachineCommand("Machine1", true, 15.0);
        MachineEntity machineEntity = new MachineEntity(1L, "Machine1", true, 15.0);
        String json = objectMapper.writeValueAsString(command);

        Mockito.when(machineDao.save(Mockito.any(MachineEntity.class))).thenReturn(machineEntity);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/machines")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Machine1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isUsed").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeLeft").value(15.0));
    }

    @Test
    void shouldDelete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/machines/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
