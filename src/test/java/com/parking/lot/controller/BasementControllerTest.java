package com.parking.lot.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.parking.lot.service.BasementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BasementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BasementService basementService;

    @BeforeEach
    public void mock() {
        MockitoAnnotations.openMocks(this);
    }

    private static String basementId;

    @Test
    @Order(1)
    public void addBasementTest() throws Exception {
        String managerId = "b1e1946e-101c-403a-92fa-859081905dcf";
        String staff = "{\"id\":\"" + managerId + "\",\"size\":10}";
        String result = mockMvc.perform(post("/basement").content(staff)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("200")))
                .andExpect(jsonPath("$.data.emptyNumber", is(10)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        JSONObject jsonObj = JSON.parseObject(result);
        basementId = jsonObj.getJSONObject("data").getString("id");
    }

    @Test
    @Order(2)
    public void deleteBasementTest() throws Exception {
        String managerId = "b1e1946e-101c-403a-92fa-859081905dcf";
        if(basementId == null){
            addBasementTest();
        }
        mockMvc.perform(delete("/basement/" + basementId).param("id", managerId))
                .andExpect(jsonPath("$.code", is("200")))
                .andExpect(status().isOk())
                .andReturn();
    }
}