package ca.uhn.fhir.jpa.validation;

import javax.annotation.PostConstruct;

import org.hl7.fhir.dstu3.hapi.validation.DefaultProfileValidationSupport;
import org.hl7.fhir.dstu3.hapi.validation.ValidationSupportChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class JpaValidationSupportChainDstu3 extends ValidationSupportChain {

	private DefaultProfileValidationSupport myDefaultProfileValidationSupport = new DefaultProfileValidationSupport();
	
	@Autowired
	@Qualifier("myJpaValidationSupportDstu3")
	public ca.uhn.fhir.jpa.dao.dstu3.IJpaValidationSupportDstu3 myJpaValidationSupportDstu3;
	
	public JpaValidationSupportChainDstu3() {
		super();
	}
	
	public void flush() {
		myDefaultProfileValidationSupport.flush();
	}

	@PostConstruct
	public void postConstruct() {
		addValidationSupport(myDefaultProfileValidationSupport);
		addValidationSupport(myJpaValidationSupportDstu3);
	}
}
