package ca.uhn.fhir.jpa.provider;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.dstu2.resource.Encounter;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IBundleProvider;

public class BaseJpaResourceProviderEncounterDstu2 extends JpaResourceProviderDstu2<Encounter> {

	
}
