package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.dstu2.model.ValueSet;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This {@link IValidationSupport validation support module} can be used to validate codes against common
 * CodeSystems that are commonly used, but are not distriuted with the FHIR specification for various reasons
 * (size, complexity, etc.).
 * <p>
 * See <a href="https://hapifhir.io/hapi-fhir/docs/validation/validation_support_modules.html#CommonCodeSystemsTerminologyService">CommonCodeSystemsTerminologyService</a> in the HAPI FHIR documentation
 * for details about what is and isn't covered by this class.
 * </p>
 */
public class CommonCodeSystemsTerminologyService implements IValidationSupport {
	public static final String LANGUAGES_VALUESET_URL = "http://hl7.org/fhir/ValueSet/languages";
	public static final String MIMETYPES_VALUESET_URL = "http://hl7.org/fhir/ValueSet/mimetypes";
	private static final String USPS_CODESYSTEM_URL = "https://www.usps.com/";
	private static final String USPS_VALUESET_URL = "http://hl7.org/fhir/us/core/ValueSet/us-core-usps-state";
	private static Map<String, String> USPS_CODES = Collections.unmodifiableMap(buildUspsCodes());


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

		/* **************************************************************************************
		 * NOTE: Update validation_support_modules.html if any of the support in this module
		 * changes in any way!
		 * **************************************************************************************/

		boolean handled = false;
		if (USPS_VALUESET_URL.equals(url)) {
			handled = true;
			String display = USPS_CODES.get(theCode);
			if (display != null) {
				if (USPS_CODESYSTEM_URL.equals(theCodeSystem) || theOptions.isInferSystem()) {
					return new CodeValidationResult()
						.setCode(theCode)
						.setDisplay(display);
				}
			}
		}

		// This is a pretty naive implementation - Will be enhanced in future
		if (LANGUAGES_VALUESET_URL.equals(url)) {
			return new CodeValidationResult()
				.setCode(theCode)
				.setDisplay(theDisplay);
		}

		// This is a pretty naive implementation - Will be enhanced in future
		if (MIMETYPES_VALUESET_URL.equals(url)) {
			return new CodeValidationResult()
				.setCode(theCode)
				.setDisplay(theDisplay);
		}

		if (handled) {
			return new CodeValidationResult()
				.setSeverity(IssueSeverity.ERROR)
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
			case DSTU2_HL7ORG: {
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

	private static HashMap<String, String> buildUspsCodes() {
		HashMap<String, String> uspsCodes = new HashMap<>();
		uspsCodes.put("AK", "Alaska");
		uspsCodes.put("AL", "Alabama");
		uspsCodes.put("AR", "Arkansas");
		uspsCodes.put("AS", "American Samoa");
		uspsCodes.put("AZ", "Arizona");
		uspsCodes.put("CA", "California");
		uspsCodes.put("CO", "Colorado");
		uspsCodes.put("CT", "Connecticut");
		uspsCodes.put("DC", "District of Columbia");
		uspsCodes.put("DE", "Delaware");
		uspsCodes.put("FL", "Florida");
		uspsCodes.put("FM", "Federated States of Micronesia");
		uspsCodes.put("GA", "Georgia");
		uspsCodes.put("GU", "Guam");
		uspsCodes.put("HI", "Hawaii");
		uspsCodes.put("IA", "Iowa");
		uspsCodes.put("ID", "Idaho");
		uspsCodes.put("IL", "Illinois");
		uspsCodes.put("IN", "Indiana");
		uspsCodes.put("KS", "Kansas");
		uspsCodes.put("KY", "Kentucky");
		uspsCodes.put("LA", "Louisiana");
		uspsCodes.put("MA", "Massachusetts");
		uspsCodes.put("MD", "Maryland");
		uspsCodes.put("ME", "Maine");
		uspsCodes.put("MH", "Marshall Islands");
		uspsCodes.put("MI", "Michigan");
		uspsCodes.put("MN", "Minnesota");
		uspsCodes.put("MO", "Missouri");
		uspsCodes.put("MP", "Northern Mariana Islands");
		uspsCodes.put("MS", "Mississippi");
		uspsCodes.put("MT", "Montana");
		uspsCodes.put("NC", "North Carolina");
		uspsCodes.put("ND", "North Dakota");
		uspsCodes.put("NE", "Nebraska");
		uspsCodes.put("NH", "New Hampshire");
		uspsCodes.put("NJ", "New Jersey");
		uspsCodes.put("NM", "New Mexico");
		uspsCodes.put("NV", "Nevada");
		uspsCodes.put("NY", "New York");
		uspsCodes.put("OH", "Ohio");
		uspsCodes.put("OK", "Oklahoma");
		uspsCodes.put("OR", "Oregon");
		uspsCodes.put("PA", "Pennsylvania");
		uspsCodes.put("PR", "Puerto Rico");
		uspsCodes.put("PW", "Palau");
		uspsCodes.put("RI", "Rhode Island");
		uspsCodes.put("SC", "South Carolina");
		uspsCodes.put("SD", "South Dakota");
		uspsCodes.put("TN", "Tennessee");
		uspsCodes.put("TX", "Texas");
		uspsCodes.put("UM", "U.S. Minor Outlying Islands");
		uspsCodes.put("UT", "Utah");
		uspsCodes.put("VA", "Virginia");
		uspsCodes.put("VI", "Virgin Islands of the U.S.");
		uspsCodes.put("VT", "Vermont");
		uspsCodes.put("WA", "Washington");
		uspsCodes.put("WI", "Wisconsin");
		uspsCodes.put("WV", "West Virginia");
		uspsCodes.put("WY", "Wyoming");
		return uspsCodes;
	}
}
