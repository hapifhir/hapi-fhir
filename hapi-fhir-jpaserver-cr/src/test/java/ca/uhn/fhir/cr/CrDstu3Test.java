package ca.uhn.fhir.cr;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.cr.common.helper.ResourceLoader;
import ca.uhn.fhir.cr.config.CrDstu3Config;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.test.BaseJpaDstu3Test;
import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.dstu3.model.Bundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = { TestCrConfig.class, CrDstu3Config.class })
public class CrDstu3Test extends BaseJpaDstu3Test implements ResourceLoader {
	protected static final FhirContext ourFhirContext = FhirContext.forDstu3Cached();

	@Autowired
	protected DaoRegistry daoRegistry;

	@Override
	public DaoRegistry getDaoRegistry() {
		return daoRegistry;
	}

	@Override
	public FhirContext getFhirContext() {
		return ourFhirContext;
	}

	public Bundle loadBundle(String theLocation) {
		return loadBundle(Bundle.class, theLocation);
	}
}
