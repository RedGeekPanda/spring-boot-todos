package com.redgeekpanda.todos.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

	@Autowired
	private TodoRepository todoRepository;

	@PostMapping("/")
	public Todo addTodo(@Valid @RequestBody Todo todo) {
		return todoRepository.save(todo);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Todo> updateTodo(@PathVariable(value = "id") Long todoId,
	                                       @Valid @RequestBody Todo todoDetails) {
		Todo todo = todoRepository.findOne(todoId);
		if (todo == null) {
			return ResponseEntity.notFound().build();
		}
		todo.setCompleted(todoDetails.isCompleted());
		todo.setMessage(todoDetails.getMessage());
		Todo updatedTodo = todoRepository.save(todo);
		return ResponseEntity.ok(updatedTodo);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Todo> deleteTodo(@PathVariable(value = "id") Long todoId) {
		Todo todo = todoRepository.findOne(todoId);
		if (todo == null) {
			return ResponseEntity.notFound().build();
		}
		todoRepository.delete(todo);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/")
	public List<Todo> getAllTodos() {
		return todoRepository.findAll();
	}

	private static Specification<Todo> completed(boolean completed) {
		return (root, query, cb) -> cb.equal(root.get(Todo_.completed), completed);
	}

	@GetMapping("/uncompleted")
	public List<Todo> getUncompletedTodos() {
		return todoRepository.findAll(completed(false));
	}

	@GetMapping("/completed")
	public List<Todo> getCompletedTodos() {
		return todoRepository.findAll(completed(true));
	}
}
