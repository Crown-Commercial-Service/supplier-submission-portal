package models;

import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import java.util.List;

@Entity
public class Todo extends Model {

	@Transient
	public static Todo connect(String name) {
		return Todo.find("byName",name).first();
	}

	@Required
	public String name;

	public Todo(Todo todo){
		this.name=todo.name;
	}

	public Todo() {}

	public Todo(String name) {
		this.name = name;
	}
}
