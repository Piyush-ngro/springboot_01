package com.example.todolist.repository;

import com.example.todolist.model.Todo;
import com.example.todolist.model.TodoStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TodoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TodoRepository todoRepository;

    @Test
    void whenFindByStatus_thenReturnTodos() {
        // given
        Todo todo = new Todo("Test Todo", "Test Description", LocalDate.now());
        todo.setStatus(TodoStatus.PENDING);
        entityManager.persist(todo);
        entityManager.flush();

        // when
        List<Todo> found = todoRepository.findByStatus(TodoStatus.PENDING);

        // then
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getTitle()).isEqualTo(todo.getTitle());
    }

    @Test
    void whenFindByTitleContaining_thenReturnTodos() {
        // given
        Todo todo = new Todo("Find Me", "Test Description", LocalDate.now());
        entityManager.persist(todo);
        entityManager.flush();

        // when
        List<Todo> found = todoRepository.findByTitleContainingIgnoreCase("find");

        // then
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getTitle()).isEqualTo(todo.getTitle());
    }
}