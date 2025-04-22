package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationTests {


    @Autowired
    private MockMvcTester mockMvcTester;

    @WithMockUser(roles = "USER")
    @Test
    void contextLoads() {
        mockMvcTester.get().uri("/user/id/123").assertThat().apply(result-> {
            MockHttpServletResponse response = result.getResponse();
            int status = response.getStatus();
            System.out.println(status);
        }).doesNotHaveFailed();
    }

}
