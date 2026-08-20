package com.uap.proiv.jobs.controller;

import com.uap.proiv.jobs.dto.User;
import com.uap.proiv.jobs.dto.UserApiResponse;
import com.uap.proiv.jobs.service.JobService;
import com.uap.proiv.jobs.service.UserJobAssignedService;
import com.uap.proiv.jobs.service.UserService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class JobControllerTest {
    @Mock
    UserService userService;

    @Mock
    JobService jobService;

    @Mock
    UserJobAssignedService userJobAssignedService;

    @InjectMocks
    JobController jobController;

    private MockMvc mockMvc;
    
    private UserApiResponse userApiResponse;
    private List<User> users;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(jobController).build();

        users = new ArrayList<>();

        User user1 = new User();
        user1.setId(10);
        user1.setEmail("user1@example.com");
        user1.setAvatar("null");
        user1.setFirstName("Juan");
        user1.setLastName("Garcia");
        users.add(user1);

        User user2 = new User();
        user2.setId(20);
        user2.setEmail("user2@example.com");
        user2.setAvatar("null");
        user2.setFirstName("Diana");
        user2.setLastName("Diaz");
        users.add(user2);

        userApiResponse = new UserApiResponse();
        userApiResponse.setPage(1);
        userApiResponse.setPerPage(2);
        userApiResponse.setTotal(2);
        userApiResponse.setTotalPages(1);
        userApiResponse.setData(users);
    }

    @Test
    @DisplayName("GET /api/job/user/{page} retorna usuarios")
    void  getUsers_succes() throws Exception {
        when(userService.search(1)).thenReturn(userApiResponse);

        //Usar MockMvc para realizar la solicitud GET y verificar la respuesta
        mockMvc.perform(get("/api/job/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").isArray());
    }
}