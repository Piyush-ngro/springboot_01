package com.example.todolist.service;

import com.example.todolist.model.Todo;
import com.example.todolist.model.TodoStatus;
import com.example.todolist.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    private Todo todo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        todo = new Todo("Test Todo", "Test Description", LocalDate.now().plusDays(7));
        todo.setId(1L);
    }

    @Test
    void getAllTodos() {
        when(todoRepository.findAll()).thenReturn(Arrays.asList(todo));
        
        List<Todo> todos = todoService.getAllTodos();
        
        assertEquals(1, todos.size());
        verify(todoRepository, times(1)).findAll();
    }

    @Test
    void getTodosByStatus() {
        when(todoRepository.findByStatus(TodoStatus.PENDING)).thenReturn(Arrays.asList(todo));
        
        List<Todo> todos = todoService.getTodosByStatus(TodoStatus.PENDING);
        
        assertEquals(1, todos.size());
        verify(todoRepository, times(1)).findByStatus(TodoStatus.PENDING);
    }

    @Test
    void getTodoById() {
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));
        
        Todo found = todoService.getTodoById(1L);
        
        assertEquals(todo.getTitle(), found.getTitle());
        verify(todoRepository, times(1)).findById(1L);
    }

    @Test
    void getTodoById_NotFound() {
        when(todoRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(RuntimeException.class, () -> todoService.getTodoById(1L));
    }

    @Test
    void saveTodo() {
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);
        
        Todo saved = todoService.saveTodo(todo);
        
        assertNotNull(saved);
        verify(todoRepository, times(1)).save(todo);
    }

    @Test
    void updateTodoStatus() {
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);
        
        Todo updated = todoService.updateTodoStatus(1L, TodoStatus.COMPLETED);
        
        assertEquals(TodoStatus.COMPLETED, updated.getStatus());
        verify(todoRepository, times(1)).save(todo);
    }

    @Test
    void deleteTodo() {
        doNothing().when(todoRepository).deleteById(1L);
        
        todoService.deleteTodo(1L);
        
        verify(todoRepository, times(1)).deleteById(1L);
    }
}