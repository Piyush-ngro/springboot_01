package com.example.todolist.service;

import com.example.todolist.model.Todo;
import com.example.todolist.model.TodoStatus;
import com.example.todolist.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TodoService {
    private final TodoRepository todoRepository;

    @Autowired
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    public List<Todo> getTodosByStatus(TodoStatus status) {
        return todoRepository.findByStatus(status);
    }

    public List<Todo> searchTodos(String searchTerm) {
        return todoRepository.findByTitleContainingIgnoreCase(searchTerm);
    }

    public Todo getTodoById(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found with id: " + id));
    }

    public Todo saveTodo(Todo todo) {
        if (todo.getDueDate() == null) {
            todo.setDueDate(LocalDate.now().plusDays(7)); // Default due date: 1 week from now
        }
        return todoRepository.save(todo);
    }

    public Todo updateTodoStatus(Long id, TodoStatus status) {
        Todo todo = getTodoById(id);
        todo.setStatus(status);
        return todoRepository.save(todo);
    }

    public void deleteTodo(Long id) {
        todoRepository.deleteById(id);
    }
}