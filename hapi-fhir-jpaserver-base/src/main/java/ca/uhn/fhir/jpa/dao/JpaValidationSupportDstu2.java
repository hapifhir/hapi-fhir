package ca.uhn.fhir.jpa.dao;

import org.hl7.fhir.instance.model.ValueSet;
import org.hl7.fhir.instance.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.instance.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.validation.IValidationSupport;

public class JpaValidationSupportDstu2 implements IValidationSupport {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(JpaValidationSupportDstu2.class);

	private FhirContext myRiCtx = FhirContext.forDstu2Hl7Org();

	@Autowired
	@Qualifier("myStructureDefinitionDaoDstu2")
	private IFhirResourceDao<ca.uhn.fhir.model.dstu2.resource.StructureDefinition> myStructureDefinitionDao;

	@Autowired
	@Qualifier("myValueSetDaoDstu2")
	private IFhirResourceDao<ca.uhn.fhir.model.dstu2.resource.ValueSet> myValueSetDao;

	@Autowired
	@Qualifier("myFhirContextDstu2")
	private FhirContext myDstu2Ctx;

	@Override
	public ValueSetExpansionComponent expandValueSet(ConceptSetComponent theInclude) {
		return null;
	}

	@Override
	public ValueSet fetchCodeSystem(String theSystem) {
		return null;
	}

	@Override
	public <T extends IBaseResource> T fetchResource(FhirContext theContext, Class<T> theClass, String theUri) {
		String resourceName = myRiCtx.getResourceDefinition(theClass).getName();
		IBundleProvider search;
		if ("ValueSet".equals(resourceName)) {
			search = myValueSetDao.search(ca.uhn.fhir.model.dstu2.resource.ValueSet.SP_URL, new UriParam(theUri));
		} else if ("StructureDefinition".equals(resourceName)) {
			search = myStructureDefinitionDao.search(ca.uhn.fhir.model.dstu2.resource.StructureDefinition.SP_URL, new UriParam(theUri));
		} else {
			throw new IllegalArgumentException("Can't fetch resource type: " + resourceName);
		}

		if (search.size() == 0) {
			return null;
		}

		if (search.size() > 1) {
			ourLog.warn("Found multiple {} instances with URL search value of: {}", resourceName, theUri);
		}

		IBaseResource res = search.getResources(0, 1).get(0);

		/*
		 * Validator wants RI structures and not HAPI ones, so convert
		 * 
		 * TODO: we really need a more efficient way of converting.. Or maybe this will
		 * just go away when we move to RI structures 
		 */
		String encoded = myDstu2Ctx.newJsonParser().encodeResourceToString(res);
		return myRiCtx.newJsonParser().parseResource(theClass, encoded);
	}

	@Override
	public boolean isCodeSystemSupported(String theSystem) {
		return false;
	}

	@Override
	public CodeValidationResult validateCode(String theCodeSystem, String theCode, String theDisplay) {
		return null;
	}

}
