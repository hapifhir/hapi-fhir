package org.hl7.fhir.common.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.dstu2.model.ValueSet;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class CommonCodeSystemsTerminologyService implements IValidationSupport {
	private static final String USPS_CODESYSTEM_URL = "https://www.usps.com/";
	private static Map<String, String> USPS_CODES = new HashMap<>();

	static {
		USPS_CODES.put("AK", "Alaska");
		USPS_CODES.put("AL", "Alabama");
		USPS_CODES.put("AR", "Arkansas");
		USPS_CODES.put("AS", "American Samoa");
		USPS_CODES.put("AZ", "Arizona");
		USPS_CODES.put("CA", "California");
		USPS_CODES.put("CO", "Colorado");
		USPS_CODES.put("CT", "Connecticut");
		USPS_CODES.put("DC", "District of Columbia");
		USPS_CODES.put("DE", "Delaware");
		USPS_CODES.put("FL", "Florida");
		USPS_CODES.put("FM", "Federated States of Micronesia");
		USPS_CODES.put("GA", "Georgia");
		USPS_CODES.put("GU", "Guam");
		USPS_CODES.put("HI", "Hawaii");
		USPS_CODES.put("IA", "Iowa");
		USPS_CODES.put("ID", "Idaho");
		USPS_CODES.put("IL", "Illinois");
		USPS_CODES.put("IN", "Indiana");
		USPS_CODES.put("KS", "Kansas");
		USPS_CODES.put("KY", "Kentucky");
		USPS_CODES.put("LA", "Louisiana");
		USPS_CODES.put("MA", "Massachusetts");
		USPS_CODES.put("MD", "Maryland");
		USPS_CODES.put("ME", "Maine");
		USPS_CODES.put("MH", "Marshall Islands");
		USPS_CODES.put("MI", "Michigan");
		USPS_CODES.put("MN", "Minnesota");
		USPS_CODES.put("MO", "Missouri");
		USPS_CODES.put("MP", "Northern Mariana Islands");
		USPS_CODES.put("MS", "Mississippi");
		USPS_CODES.put("MT", "Montana");
		USPS_CODES.put("NC", "North Carolina");
		USPS_CODES.put("ND", "North Dakota");
		USPS_CODES.put("NE", "Nebraska");
		USPS_CODES.put("NH", "New Hampshire");
		USPS_CODES.put("NJ", "New Jersey");
		USPS_CODES.put("NM", "New Mexico");
		USPS_CODES.put("NV", "Nevada");
		USPS_CODES.put("NY", "New York");
		USPS_CODES.put("OH", "Ohio");
		USPS_CODES.put("OK", "Oklahoma");
		USPS_CODES.put("OR", "Oregon");
		USPS_CODES.put("PA", "Pennsylvania");
		USPS_CODES.put("PR", "Puerto Rico");
		USPS_CODES.put("PW", "Palau");
		USPS_CODES.put("RI", "Rhode Island");
		USPS_CODES.put("SC", "South Carolina");
		USPS_CODES.put("SD", "South Dakota");
		USPS_CODES.put("TN", "Tennessee");
		USPS_CODES.put("TX", "Texas");
		USPS_CODES.put("UM", "U.S. Minor Outlying Islands");
		USPS_CODES.put("UT", "Utah");
		USPS_CODES.put("VA", "Virginia");
		USPS_CODES.put("VI", "Virgin Islands of the U.S.");
		USPS_CODES.put("VT", "Vermont");
		USPS_CODES.put("WA", "Washington");
		USPS_CODES.put("WI", "Wisconsin");
		USPS_CODES.put("WV", "West Virginia");
		USPS_CODES.put("WY", "Wyoming");
	}

	private final FhirContext myFhirContext;

	/**
	 * Constructor
	 */
	public CommonCodeSystemsTerminologyService(FhirContext theFhirContext) {
		Validate.notNull(theFhirContext);

		myFhirContext = theFhirContext;
	}

	@Override
	public CodeValidationResult validateCodeInValueSet(IValidationSupport theRootValidationSupport, ConceptValidationOptions theOptions, String theCodeSystem, String theCode, String theDisplay, @Nonnull IBaseResource theValueSet) {
		String url = getValueSetUrl(theValueSet);

		if ("http://hl7.org/fhir/us/core/ValueSet/us-core-usps-state".equals(url)) {
			String display = USPS_CODES.get(theCode);
			if (display != null) {
				if (USPS_CODESYSTEM_URL.equals(theCodeSystem) || theOptions.isInferSystem()) {
					return new CodeValidationResult()
						.setCode(theCode);
				}
			}

			return new CodeValidationResult()
				.setSeverity("error")
				.setMessage("Code \"" + theCode + "\" is not in system: " + USPS_CODESYSTEM_URL);
		}

		return null;
	}

	public String getValueSetUrl(@Nonnull IBaseResource theValueSet) {
		String url;
		switch (getFhirContext().getVersion().getVersion()) {
			case DSTU2: {
				url = ((ca.uhn.fhir.model.dstu2.resource.ValueSet) theValueSet).getUrl();
				break;
			}
			case DSTU2_HL7ORG:{
				url = ((ValueSet) theValueSet).getUrl();
				break;
			}
			case DSTU3: {
				url = ((org.hl7.fhir.dstu3.model.ValueSet) theValueSet).getUrl();
				break;
			}
			case R4: {
				url = ((org.hl7.fhir.r4.model.ValueSet) theValueSet).getUrl();
				break;
			}
			case R5: {
				url = ((org.hl7.fhir.r5.model.ValueSet) theValueSet).getUrl();
				break;
			}
			case DSTU2_1:
			default:
				throw new IllegalArgumentException("Can not handle version: " + getFhirContext().getVersion().getVersion());
		}
		return url;
	}

	@Override
	public FhirContext getFhirContext() {
		return myFhirContext;
	}
}
