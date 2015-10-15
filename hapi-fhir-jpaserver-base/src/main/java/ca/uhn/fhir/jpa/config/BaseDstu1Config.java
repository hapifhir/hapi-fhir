package ca.uhn.fhir.jpa.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ca.uhn.fhir.model.api.IResource;

@Configuration
public class BaseDstu1Config extends BaseConfig {

	@Bean(name = "mySystemDaoDstu1", autowire = Autowire.BY_NAME)
	public ca.uhn.fhir.jpa.dao.IFhirSystemDao<List<IResource>> fhirSystemDaoDstu1() {
		ca.uhn.fhir.jpa.dao.FhirSystemDaoDstu1 retVal = new ca.uhn.fhir.jpa.dao.FhirSystemDaoDstu1();
		return retVal;
	}

	@Bean(name = "mySystemProviderDstu1")
	public ca.uhn.fhir.jpa.provider.JpaSystemProviderDstu1 systemDaoDstu1() {
		ca.uhn.fhir.jpa.provider.JpaSystemProviderDstu1 retVal = new ca.uhn.fhir.jpa.provider.JpaSystemProviderDstu1();
		retVal.setDao(fhirSystemDaoDstu1());
		return retVal;
	}

}
