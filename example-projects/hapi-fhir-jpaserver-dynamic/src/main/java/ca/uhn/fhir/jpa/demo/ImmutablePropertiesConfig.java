package ca.uhn.fhir.jpa.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Load immutable properties from /resources/config/<STU_VERSION>/immutable.properties file. STU_VERSION environment variable can be set to dstu2 or dstu3, which will translate to the 
 * file path, e.g.: /resources/config/immutable/dstu3/immutable.properties
 * By default it'll set path to dstu3.
 * 
 * @author anoushmouradian
 *
 */
@Configuration
@PropertySource("classpath:config/${STU_VERSION:dstu3}/immutable.properties")
public class ImmutablePropertiesConfig {
	
	@Value("${FHIR_VERSION}")
	private String fhirVersion;
	@Value("${fhirServerConfigClass}")
	private String fhirServerConfigClass;
	@Value("${fhirTesterConfigClass}")
	private String fhirTesterConfigClass;
	@Value("${jpaDemoClass}")
	private String jpaDemoClass;
	@Value("${jpaDemoMapping}")
	private String jpaDemoMapping;


	public String getFhirVersion() {
		return fhirVersion;
	}
	public void setFhirVersion(String fhirVersion) {
		this.fhirVersion = fhirVersion;
	}
	public String getFhirServerConfigClass() {
		return fhirServerConfigClass;
	}
	public void setFhirServerConfigClass(String fhirServerConfigClass) {
		this.fhirServerConfigClass = fhirServerConfigClass;
	}
	public String getFhirTesterConfigClass() {
		return fhirTesterConfigClass;
	}
	public void setFhirTesterConfigClass(String fhirTesterConfigClass) {
		this.fhirTesterConfigClass = fhirTesterConfigClass;
	}
	public String getJpaDemoClass() {
		return jpaDemoClass;
	}
	public void setJpaDemoClass(String jpaDemoClass) {
		this.jpaDemoClass = jpaDemoClass;
	}
	public String getJpaDemoMapping() {
		return jpaDemoMapping;
	}
	public void setJpaDemoMapping(String jpaDemoMapping) {
		this.jpaDemoMapping = jpaDemoMapping;
	}
	
	
	
}
