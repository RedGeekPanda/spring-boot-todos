package com.redgeekpanda.todos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "TODO")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Todo implements Serializable {

	@Id
	@GeneratedValue
	@Column(name = "TODO_ID")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long id;
	@Column(nullable = false)
	private Boolean completed;
	@NotBlank
	private String message;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TODOLIST_ID", nullable = false)
	@JsonIgnore
	private TodoList todoList;

	public Todo() {
		completed = false;
	}

	public Todo(String message) {
		this();
		this.message = message;
	}

	public Todo(boolean completed, String message) {
		this(message);
		this.completed = completed;
	}

	public Long getId() {
		return id;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public TodoList getTodoList() {
		return todoList;
	}

	public void setTodoList(TodoList todoList) {
		this.todoList = todoList;
	}

	public Long getTodoListId() {
		return todoList.getId();
	}
}
