package ca.uhn.fhir.jpa.sched;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.annotation.PostConstruct;

public class QuartzTableSeeder {

	@Autowired
	private LocalContainerEntityManagerFactoryBean myEntityManagerFactory;

	@PostConstruct
	public void start() {

	}

}
