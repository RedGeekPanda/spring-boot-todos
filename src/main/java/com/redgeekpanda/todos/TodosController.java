package com.redgeekpanda.todos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TodosController {

	@Autowired
	private TodoRepository todoRepository;
	@Autowired
	private TodoListRepository todoListRepository;

	private static Specification<Todo> completedTodosInTodoList(boolean completed, TodoList todoList) {
		return (root, query, cb) -> {
			Predicate ifCompletedEquals = cb.equal(root.get(Todo_.completed), completed);
			Predicate ifTodoListEquals = cb.equal(root.get(Todo_.todoList), todoList);
			return cb.and(ifCompletedEquals, ifTodoListEquals);
		};
	}

	@GetMapping("/addTestData")
	public void init() {
		TodoList todoList1 = new TodoList("First");
		TodoList todoList2 = new TodoList("Second");
		Todo todo1 = new Todo("11");
		Todo todo2 = new Todo("22");
		Todo todo3 = new Todo("33");
		todoList1.addTodo(todo1);
		todoList1.addTodo(todo2);
		todoList2.addTodo(todo3);
		todoListRepository.save(todoList1);
		todoListRepository.save(todoList2);
	}

	@GetMapping("/todoLists")
	public List<TodoList> getAllTodoLists() {
		return todoListRepository.findAll();
	}

	@GetMapping("/todoLists/{todoListId}")
	public ResponseEntity<TodoList> getTodoList(@PathVariable(value = "todoListId") Long todoListId) {
		TodoList todoList = todoListRepository.findOne(todoListId);
		if (todoList == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(todoList);
	}

	@GetMapping("/todoLists/{todoListId}/todos")
	public ResponseEntity<List<Todo>> getTodoListAllTodos(@PathVariable(value = "todoListId") Long todoListId) {
		TodoList todoList = todoListRepository.findOne(todoListId);
		if (todoList == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(todoList.getTodos());
	}

	@GetMapping("/todoLists/{todoListId}/todos/uncompleted")
	public ResponseEntity<List<Todo>> getTodoListUncompletedTodos(@PathVariable(value = "todoListId") Long todoListId) {
		TodoList todoList = todoListRepository.findOne(todoListId);
		if (todoList == null) {
			return ResponseEntity.notFound().build();
		}
		Specification<Todo> specification = completedTodosInTodoList(false, todoList);
		return ResponseEntity.ok(todoRepository.findAll(specification));
	}

	@GetMapping("/todoLists/{todoListId}/todos/completed")
	public ResponseEntity<List<Todo>> getTodoListCompletedTodos(@PathVariable(value = "todoListId") Long todoListId) {
		TodoList todoList = todoListRepository.findOne(todoListId);
		if (todoList == null) {
			return ResponseEntity.notFound().build();
		}
		Specification<Todo> specification = completedTodosInTodoList(true, todoList);
		return ResponseEntity.ok(todoRepository.findAll(specification));
	}

	@GetMapping("/todos/{todoId}")
	public ResponseEntity<Todo> getTodo(@PathVariable(value = "todoId") Long todoId) {
		Todo todo = todoRepository.findOne(todoId);
		if (todo == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(todo);
	}

	@PostMapping("/todoLists")
	public TodoList addTodoList(@Valid @RequestBody TodoList todoList) {
		todoListRepository.save(todoList);
		return todoList;
	}

	@PostMapping("/todoLists/{todoListId}/todos")
	public ResponseEntity<Todo> addTodoToTodoList(@PathVariable(value = "todoListId") Long todoListId,
	                                              @Valid @RequestBody Todo todo) {
		TodoList todoList = todoListRepository.findOne(todoListId);
		if (todoList == null) {
			return ResponseEntity.notFound().build();
		}
		todo.setTodoList(todoList);
		todoRepository.save(todo);
		return ResponseEntity.ok(todo);
	}

	@PutMapping("/todolists/{todoListId}")
	public ResponseEntity<TodoList> updateTodoList(@PathVariable(value = "todoListId") Long todoListId,
	                                               @Valid @RequestBody TodoList todoListDetails) {
		TodoList todoList = todoListRepository.findOne(todoListId);
		if (todoList == null) {
			return ResponseEntity.notFound().build();
		}
		todoList.setName(todoListDetails.getName());
		todoList.setColor(todoListDetails.getColor());
		todoListRepository.save(todoList);
		return ResponseEntity.ok(todoList);
	}

	@PutMapping("/todos/{todoId}")
	public ResponseEntity<Todo> updateTodo(@PathVariable(value = "todoId") Long todoId,
	                                       @Valid @RequestBody Todo todoDetails) {
		Todo todo = todoRepository.findOne(todoId);
		if (todo == null) {
			return ResponseEntity.notFound().build();
		}
		todo.setCompleted(todoDetails.isCompleted());
		todo.setMessage(todoDetails.getMessage());
		todoRepository.save(todo);
		return ResponseEntity.ok(todo);
	}

	@PatchMapping("todos/{todoId}/moveTo/{todoListId}")
	public ResponseEntity<Todo> moveTodoToTodoList(@PathVariable(value = "todoId") Long todoId,
	                                               @PathVariable(value = "todoListId") Long todoListId) {
		Todo todo = todoRepository.findOne(todoId);
		if (todo == null) {
			return ResponseEntity.notFound().build();
		}
		TodoList todoList = todoListRepository.findOne(todoListId);
		if (todoList == null) {
			return ResponseEntity.notFound().build();
		}
		todo.setTodoList(todoList);
		todoRepository.save(todo);
		return ResponseEntity.ok(todo);
	}

	@DeleteMapping("/todolists/{todoListId}")
	public ResponseEntity<TodoList> deleteTodoList(@PathVariable(value = "todoId") Long todoListId) {
		TodoList todoList = todoListRepository.findOne(todoListId);
		if (todoList == null) {
			return ResponseEntity.notFound().build();
		}
		todoListRepository.delete(todoList);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/todos/{todoId}")
	public ResponseEntity<Todo> deleteTodo(@PathVariable(value = "todoId") Long todoId) {
		Todo todo = todoRepository.findOne(todoId);
		if (todo == null) {
			return ResponseEntity.notFound().build();
		}
		todoRepository.delete(todo);
		return ResponseEntity.ok().build();
	}
}
