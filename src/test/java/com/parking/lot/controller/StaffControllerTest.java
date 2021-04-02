package com.parking.lot.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.parking.lot.service.StaffService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureOrder
class StaffControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StaffService staffService;

    @BeforeEach
    public void mock() {
        MockitoAnnotations.openMocks(this);
    }

    private static String staffId;

    @Test
    public void addStaffTest() throws Exception {
        String managerId = "b1e1946e-101c-403a-92fa-859081905dcf";
        String staff = "{\"id\":\""+managerId+"\",\"name\":\"Tom\",\"role\":\"3 \"}";
        String result = mockMvc.perform(post("/staff").content(staff)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.removeDate", nullValue()))
                .andExpect(jsonPath("$.code", is("200")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        JSONObject jsonObj = JSON.parseObject(result);
        staffId = jsonObj.getJSONObject("data").getString("id");
    }

    @Test
    public void deleteStaffTest() throws Exception {
        String managerId = "b1e1946e-101c-403a-92fa-859081905dcf";
        if(staffId == null){
            addStaffTest();
        }
        mockMvc.perform(delete("/staff/"+staffId).param("id", managerId))
                .andExpect(jsonPath("$.code", is("200")))
                .andExpect(status().isOk())
                .andReturn();
    }
}