package ca.uhn.fhir.jpa.entity;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class FhirPersister {

	public static void main(String[] args) {
		
		new ClassPathXmlApplicationContext("fhir-spring-config.xml");
		
	}
	
}



