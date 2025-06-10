package com.inssider.api.common;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ControllerExceptionTests {

  @Autowired private MockMvc mockMvc;

  @Test
  void getNonExistentEntity_shouldReturn404() throws Exception {
    var request = get("/api/profiles/0").accept(MediaType.APPLICATION_JSON);
    mockMvc.perform(request).andExpect(status().is4xxClientError());
  }
}
