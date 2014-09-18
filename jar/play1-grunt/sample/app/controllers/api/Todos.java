package controllers.api;

import com.google.gson.Gson;
import models.Todo;
import play.Logger;
import play.data.binding.Global;
import play.data.binding.TypeBinder;
import play.data.validation.*;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Scope;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Todos extends Controller {

	public static void index() {
		// [{id: 1245, name: "min todo", added: "2013-01-01"}, {id: 1245, name: "min todo", added: "2013-01-01"}]
		List<Todo> todos = Todo.all().fetch();
		renderJSON(todos);
	}

	public static void view(Long id) {
		Todo todo = Todo.findById(id);
		renderJSON(todo);
	}

	public static void save(Long id, String body) {
		Todo todo = new Gson().fromJson(body, Todo.class);
		todo=todo.merge();

		boolean isValid = (todo.id == 0) ? todo.validateAndCreate() : todo.validateAndSave();
		if(isValid)
			renderJSON(todo);
		else
			badRequest();
	}

	public static void delete(@Required Long id) {
		if(validation.hasErrors()){
			//error
		}else{
			ok();
		}
	}

}
