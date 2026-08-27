package com.uap.proiv.jobs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uap.proiv.jobs.client.UserApiRepository;
import org.springframework.http.MediaType;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.autoconfigure.AutoConfigurationPackage;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.net.http.HttpClient;
import java.time.Duration;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserApiRepository userApiRepository;

    static MockWebServer mockWebServer;
    
    @BeforeAll
    static void setup() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    public static void tearDown() throws IOException {
        mockWebServer.close();
    }

    @TestConfiguration
    static class TestConfig
    {
        @Bean
        @Primary
        public UserApiRepository userApiRepository(ObjectMapper objectMapper)
        {
            HttpClient httpClient = HttpClient.newBuilder()  
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();
            String baseUrl = mockWebServer.url("/api/users").toString();
            String apikey = "free_user_3HYTiqu2JKQ4TfGq884xW5mqfrd";

            return  new UserApiRepository(httpClient, objectMapper, baseUrl, apikey);
        }
    }

    @Test
    @DisplayName("Get api/users/{id} integracuion UserController, userService, UserRepository Mock de la API exterma")
        void  getUserById()throws Exception {
            String jsonResponse = """
                    {
                    "id":2,
                    "email":"juan.perez@gmail.com",
                    "first_name":"Juan",
                    "last_name":"Perez",
                    "avatar": "https://reqres.in/img/faces/2.jpg"
                    }
                    """;
            mockWebServer.enqueue(new MockResponse() 
                    .setBody(jsonResponse)
                    .setResponseCode(200)
                    .addHeader("Content-Type", "application/json")
            );

            mockMvc.perform(get("/api/user/id/2")) 
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(2))
                    .andExpect(jsonPath("$.email").value("juan.perez@gmail.com"))
                    .andExpect(jsonPath("$.first_name").value("Juan"))
                    .andExpect(jsonPath("$.last_name").value("Perez"))
                    .andExpect(jsonPath("$.avatar").value("https://reqres.in/img/faces/2.jpg"));
        }

}