package com.example.timecard;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:timecard_test;DB_CLOSE_DELAY=-1;MODE=MySQL")
@AutoConfigureMockMvc
class AccessControlTests {
    @Autowired MockMvc mvc;

    @Test
    void anyoneCanUseTimecard() throws Exception {
        mvc.perform(get("/timecard")).andExpect(status().isOk());
    }

    @Test
    void anonymousCannotRegisterEmployees() throws Exception {
        mvc.perform(get("/personedit")).andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(roles = "USER")
    void employeeCannotRegisterOrOpenAdminEdit() throws Exception {
        mvc.perform(get("/personedit")).andExpect(status().isForbidden());
        mvc.perform(get("/shiftedit")).andExpect(status().isForbidden());
        mvc.perform(get("/dayClick")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminCanOpenEmployeeRegistration() throws Exception {
        mvc.perform(get("/personedit")).andExpect(status().isOk());
    }
}
