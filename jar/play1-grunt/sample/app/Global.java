import models.Todo;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

import java.util.List;

@OnApplicationStart
public class Global extends Job {

	public void doJob() {
		if(Todo.count() == 0) {
			Fixtures.loadModels("initial-data.yml");
		}
	}
}
