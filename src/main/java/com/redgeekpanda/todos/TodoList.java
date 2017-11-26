package com.redgeekpanda.todos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TODOLIST")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TodoList implements Serializable {

	@Id
	@GeneratedValue
	@Column(name = "TODOLIST_ID")
	private Long id;
	@NotBlank
	private String name;
	private Long color;
	@OneToMany(mappedBy = "todoList", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Todo> todos;

	public TodoList() {
		todos = new ArrayList<>();
	}

	public TodoList(String name) {
		this();
		this.name = name;
	}

	public TodoList(String name, Long color) {
		this(name);
		this.color = color;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getColor() {
		return color;
	}

	public void setColor(Long color) {
		this.color = color;
	}

	public List<Todo> getTodos() {
		return todos;
	}

	public void addTodo(Todo todo) {
		todo.setTodoList(this);
		todos.add(todo);
	}

	public void removeTodo(Todo todo) {
		todo.setTodoList(null);
		todos.remove(todo);
	}
}
