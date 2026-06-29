package ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand;

import ca.uhn.fhir.batch2.api.IJobParametersValidator;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.util.UrlUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class PreExpandValueSetParametersValidator implements IJobParametersValidator<PreExpandValueSetParameters> {

	@Autowired
	private IValidationSupport myValidationSupport;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private FhirContext myFhirContext;

	@Nullable
	@Override
	public List<String> validate(RequestDetails theRequestDetails, @Nonnull PreExpandValueSetParameters theParameters) {
		List<String> retVal = new ArrayList<>();

		if (theParameters.getId() != null) {
			if (isNotBlank(theParameters.getUrl()) || isNotBlank(theParameters.getVersion())) {
				retVal.add("Can not combine ValueSet ID with URL or version parameters");
				return retVal;
			}

			IIdType id = myFhirContext.getVersion().newIdType(theParameters.getId());
			IBaseResource valueSet = myDaoRegistry.getResourceDao("ValueSet").read(id, theRequestDetails);
			Optional<String> urlValueOpt = myFhirContext.newTerser().getSinglePrimitiveValue(valueSet, "url");
			if (urlValueOpt.isEmpty()) {
				retVal.add("ValueSet does not have a URL and can not be pre-expanded: " + theParameters.getId());
				return retVal;
			}
			urlValueOpt.ifPresent(theParameters::setUrl);
			Optional<String> versionValueOpt = myFhirContext.newTerser().getSinglePrimitiveValue(valueSet, "version");
			versionValueOpt.ifPresent(theParameters::setVersion);
		}

		UrlUtil.CanonicalUrlParts canonicalUrl = theParameters.getCanonicalUrl();
		IBaseResource valueSet = myValidationSupport.fetchValueSet(canonicalUrl.toString());
		if (valueSet == null) {
			retVal.add("ValueSet not found: " + canonicalUrl);
		}

		return retVal;
	}
}
