package ca.uhn.fhir.cr;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.cr.common.helper.ResourceLoader;
import ca.uhn.fhir.cr.config.CrR4Config;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.r4.model.Bundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;


@ContextConfiguration(classes = { TestCrConfig.class, CrR4Config.class })
public class CrR4Test extends BaseJpaR4Test implements ResourceLoader {
	protected static final FhirContext ourFhirContext = FhirContext.forR4Cached();

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
