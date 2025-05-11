package com.example.todolist.controller;

import com.example.todolist.model.Todo;
import com.example.todolist.model.TodoStatus;
import com.example.todolist.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TodoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TodoService todoService;

    @Mock
    private Model model;

    @InjectMocks
    private TodoController todoController;

    private Todo todo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(todoController).build();
        todo = new Todo("Test Todo", "Test Description", LocalDate.now().plusDays(7));
        todo.setId(1L);
    }

    @Test
    void listTodos() throws Exception {
        when(todoService.getAllTodos()).thenReturn(Arrays.asList(todo));
        
        mockMvc.perform(get("/todos"))
               .andExpect(status().isOk())
               .andExpect(view().name("todos/list-todos"));
        
        verify(todoService, times(1)).getAllTodos();
    }

    @Test
    void showAddForm() throws Exception {
        mockMvc.perform(get("/todos/add"))
               .andExpect(status().isOk())
               .andExpect(view().name("todos/add-todo"));
    }

    @Test
    void addTodo_Valid() throws Exception {
        when(todoService.saveTodo(any(Todo.class))).thenReturn(todo);
        
        mockMvc.perform(post("/todos/add")
                .param("title", "Test Todo")
                .param("description", "Test Description"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/todos"));
    }

    @Test
    void addTodo_Invalid() throws Exception {
        mockMvc.perform(post("/todos/add")
                .param("title", "") // Invalid - empty title
                .param("description", "Test Description"))
               .andExpect(status().isOk())
               .andExpect(view().name("todos/add-todo"));
    }

    @Test
    void completeTodo() throws Exception {
        mockMvc.perform(get("/todos/complete/1"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/todos"));
        
        verify(todoService, times(1)).updateTodoStatus(1L, TodoStatus.COMPLETED);
    }

    @Test
    void deleteTodo() throws Exception {
        mockMvc.perform(get("/todos/delete/1"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/todos"));
        
        verify(todoService, times(1)).deleteTodo(1L);
    }
}