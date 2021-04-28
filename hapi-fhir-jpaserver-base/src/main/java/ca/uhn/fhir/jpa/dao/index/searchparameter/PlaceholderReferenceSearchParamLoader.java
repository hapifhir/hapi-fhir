package ca.uhn.fhir.jpa.dao.index.searchparameter;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Set;

public class PlaceholderReferenceSearchParamLoader implements IPlaceholderReferenceSearchParamLoader{

	public static final String PLACEHOLDER_FHIRPATH_SUFFIX = ".extension('http://hapifhir.io/fhir/StructureDefinition/resource-placeholder').value.as(boolean)";
	public DaoRegistry myDaoRegistry;
	FhirContext myFhirContext;

	@Autowired
	public PlaceholderReferenceSearchParamLoader(DaoRegistry theDaoRegistry, FhirContext theFhirContext) {
		myDaoRegistry = theDaoRegistry;
		myFhirContext = theFhirContext;
	}

	@PostConstruct
	public void start() {
		Set<String> registeredDaoTypes = myDaoRegistry.getRegisteredDaoTypes();
		registeredDaoTypes.forEach(this::createAndUpdatePlaceholderSearchParamForType);
	}

	private void createAndUpdatePlaceholderSearchParamForType(String type) {
		CanonicalSearchParameter canonicalSearchParameter = buildCanonicalSearchParameter(type);
		myDaoRegistry
			.getResourceDao("SearchParameter")
			.update(canonicalSearchParameter.decanonicalize(myFhirContext));
	}

	private CanonicalSearchParameter buildCanonicalSearchParameter(String theType) {
		CanonicalSearchParameter canonicalSearchParameter = new CanonicalSearchParameter();
		canonicalSearchParameter.setId(theType + "-placeholder-sp");
		canonicalSearchParameter.setUrl("http://example.org/sp/patient/resource-placeholder");
		canonicalSearchParameter.setName(theType + "-resource-placeholder");
		canonicalSearchParameter.setStatus("active");
		canonicalSearchParameter.setDescription("Search for resources of type [" + theType + "] populated with the resource-placeholder extension");
		canonicalSearchParameter.setCode("resource-placeholder");
		canonicalSearchParameter.setBase(Collections.singletonList(theType));
		canonicalSearchParameter.setType("token");
		canonicalSearchParameter.setExpression(theType + PLACEHOLDER_FHIRPATH_SUFFIX);
		return canonicalSearchParameter;
	}

}
