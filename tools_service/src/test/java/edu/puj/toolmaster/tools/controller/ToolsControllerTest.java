package edu.puj.toolmaster.tools.controller;

import edu.puj.toolmaster.tools.entities.Tool;
import edu.puj.toolmaster.tools.exceptions.ResourceBadRequestException;
import edu.puj.toolmaster.tools.services.ToolService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc(addFilters = false)
class ToolsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ToolService toolService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllTools() throws Exception {
        // Mock the ToolService to return a Page of tools
        Page<Tool> toolsPage = new PageImpl<>(Collections.emptyList());
        when(toolService.getAllTools(ArgumentMatchers.any(PageRequest.class))).thenReturn(toolsPage);

        // Perform the GET request
        mockMvc.perform(get("/api/tools/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.content", hasSize(0)));

        // Verify that the ToolService method was called with the correct arguments
        verify(toolService).getAllTools(ArgumentMatchers.any(PageRequest.class));
    }

    @Test
    void getToolsByName() throws Exception {
        // Mock the ToolService to return a Page of tools
        Page<Tool> toolsPage = new PageImpl<>(Collections.emptyList());
        when(toolService.toolByNameLike(eq("example"), ArgumentMatchers.any(PageRequest.class))).thenReturn(toolsPage);

        // Perform the GET request
        mockMvc.perform(get("/api/tools/search/name/{name}", "example"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.content", hasSize(0)));

        // Verify that the ToolService method was called with the correct arguments
        verify(toolService).toolByNameLike(eq("example"), ArgumentMatchers.any(PageRequest.class));
    }

    @Test
    void createNewTool() throws Exception {
        // Create a new Tool object
        Tool newTool = new Tool();
        newTool = newTool.withName("Example Tool");

        // Perform the POST request
        mockMvc.perform(post("/api/tools/")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(newTool)))
                .andExpect(status().isCreated());
    }

    @Test
    void createNewTool_BadRequest() throws Exception {
        // Create a new Tool object
        Tool newTool = new Tool();
        newTool = newTool.withName("Example Tool");

        when(toolService.addNewTool(any())).thenThrow(ResourceBadRequestException.class);

        // Perform the POST request
        mockMvc.perform(post("/api/tools/")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(newTool)))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void getToolByIdTest() throws Exception {
        Tool tool = new Tool(); // construct your Tool
        when(toolService.getToolById(anyInt()))
                .thenReturn(tool);

        mockMvc.perform(get("/api/tools/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteToolByIdTest() throws Exception {
        doNothing().when(toolService).deleteToolById(anyInt());

        mockMvc.perform(delete("/api/tools/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateToolById() throws Exception {

        Tool tool = new Tool().withId(1).withName("ToolName");
        when(toolService.updateToolById(1, tool)).thenReturn(tool);

        String toolJson = objectMapper.writeValueAsString(tool);

        mockMvc.perform(put("/api/tools/{id}", 1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toolJson))
                .andExpect(status().isOk());
    }

}
