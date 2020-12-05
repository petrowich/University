package ru.petrowich.university.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class HomeControllerTest {
    private AutoCloseable autoCloseable;
    private MockMvc mockMvc;

    @InjectMocks
    HomeController homeController;

    @BeforeEach
    private void openMocks() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        mockMvc = standaloneSetup(homeController).build();
    }

    @AfterEach
    public void releaseMocks() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testMockMVC() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));
    }
}
