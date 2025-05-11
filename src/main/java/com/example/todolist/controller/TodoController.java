package com.example.todolist.controller;

import com.example.todolist.model.Todo;
import com.example.todolist.model.TodoStatus;
import com.example.todolist.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;

@Controller
@RequestMapping("/todos")
public class TodoController {
    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping
    public String listTodos(@RequestParam(required = false) String status, 
                           @RequestParam(required = false) String search,
                           Model model) {
        if (search != null && !search.isEmpty()) {
            model.addAttribute("todos", todoService.searchTodos(search));
        } else if (status != null && !status.isEmpty()) {
            model.addAttribute("todos", todoService.getTodosByStatus(TodoStatus.valueOf(status.toUpperCase())));
        } else {
            model.addAttribute("todos", todoService.getAllTodos());
        }
        return "todos/list-todos";
    }

    

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("todo", new Todo());
        return "todos/add-todo";
    }

    @PostMapping("/add")
    public String addTodo(@Valid @ModelAttribute("todo") Todo todo, BindingResult result) {
        if (result.hasErrors()) {
            return "todos/add-todo";
        }
        todoService.saveTodo(todo);
        return "redirect:/todos";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("todo", todoService.getTodoById(id));
        return "todos/edit-todo";
    }

    @PostMapping("/edit/{id}")
    public String updateTodo(@PathVariable Long id, @Valid @ModelAttribute("todo") Todo todo, 
                            BindingResult result) {
        if (result.hasErrors()) {
            return "todos/edit-todo";
        }
        todo.setId(id);
        todoService.saveTodo(todo);
        return "redirect:/todos";
    }

    @GetMapping("/complete/{id}")
    public String completeTodo(@PathVariable Long id) {
        todoService.updateTodoStatus(id, TodoStatus.COMPLETED);
        return "redirect:/todos";
    }

    @GetMapping("/pending/{id}")
    public String markTodoAsPending(@PathVariable Long id) {
        todoService.updateTodoStatus(id, TodoStatus.PENDING);
        return "redirect:/todos";
    }

    @GetMapping("/delete/{id}")
    public String deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return "redirect:/todos";
    }
}