package ca.uhn.fhir.jpa.validation;

import javax.annotation.PostConstruct;

import org.hl7.fhir.dstu21.hapi.validation.DefaultProfileValidationSupport;
import org.hl7.fhir.dstu21.hapi.validation.ValidationSupportChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class JpaValidationSupportChainDstu21 extends ValidationSupportChain {

	private DefaultProfileValidationSupport myDefaultProfileValidationSupport = new DefaultProfileValidationSupport();
	
	@Autowired
	@Qualifier("myJpaValidationSupportDstu21")
	public ca.uhn.fhir.jpa.dao.IJpaValidationSupportDstu21 myJpaValidationSupportDstu21;
	
	public JpaValidationSupportChainDstu21() {
		super();
	}
	
	public void flush() {
		myDefaultProfileValidationSupport.flush();
	}

	@PostConstruct
	public void postConstruct() {
		addValidationSupport(myDefaultProfileValidationSupport);
		addValidationSupport(myJpaValidationSupportDstu21);
	}
}
