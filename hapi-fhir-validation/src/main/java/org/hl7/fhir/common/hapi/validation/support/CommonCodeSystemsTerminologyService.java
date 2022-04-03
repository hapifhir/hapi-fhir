package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.util.ClasspathUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.fhir.ucum.UcumEssenceService;
import org.fhir.ucum.UcumException;
import org.hl7.fhir.common.hapi.validation.validator.VersionSpecificWorkerContextWrapper;
import org.hl7.fhir.convertors.advisors.impl.BaseAdvisor_30_40;
import org.hl7.fhir.convertors.advisors.impl.BaseAdvisor_40_50;
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_30_40;
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_40_50;
import org.hl7.fhir.dstu2.model.ValueSet;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.hl7.fhir.common.hapi.validation.support.SnapshotGeneratingValidationSupport.newVersionTypeConverter;

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
	public static final String LANGUAGES_CODESYSTEM_URL = "urn:ietf:bcp:47";
	public static final String MIMETYPES_VALUESET_URL = "http://hl7.org/fhir/ValueSet/mimetypes";
	public static final String MIMETYPES_CODESYSTEM_URL = "urn:ietf:bcp:13";
	public static final String CURRENCIES_CODESYSTEM_URL = "urn:iso:std:iso:4217";
	public static final String CURRENCIES_VALUESET_URL = "http://hl7.org/fhir/ValueSet/currencies";
	public static final String COUNTRIES_CODESYSTEM_URL = "urn:iso:std:iso:3166";
	public static final String UCUM_CODESYSTEM_URL = "http://unitsofmeasure.org";
	public static final String UCUM_VALUESET_URL = "http://hl7.org/fhir/ValueSet/ucum-units";
	public static final String ALL_LANGUAGES_VALUESET_URL = "http://hl7.org/fhir/ValueSet/all-languages";
	private static final String USPS_CODESYSTEM_URL = "https://www.usps.com/";
	private static final String USPS_VALUESET_URL = "http://hl7.org/fhir/us/core/ValueSet/us-core-usps-state";
	private static final Logger ourLog = LoggerFactory.getLogger(CommonCodeSystemsTerminologyService.class);
	private static Map<String, String> USPS_CODES = Collections.unmodifiableMap(buildUspsCodes());
	private static Map<String, String> ISO_4217_CODES = Collections.unmodifiableMap(buildIso4217Codes());
	private static Map<String, String> ISO_3166_CODES = Collections.unmodifiableMap(buildIso3166Codes());
	private final FhirContext myFhirContext;
	private final VersionSpecificWorkerContextWrapper.IVersionTypeConverter myVersionConverter;
	private volatile org.hl7.fhir.r5.model.ValueSet myLanguagesVs;
	private volatile Map<String, String> myLanguagesLanugageMap;
	private volatile Map<String, String> myLanguagesRegionMap;

	/**
	 * Constructor
	 */
	public CommonCodeSystemsTerminologyService(FhirContext theFhirContext) {
		Validate.notNull(theFhirContext);

		myFhirContext = theFhirContext;
		myVersionConverter = newVersionTypeConverter(myFhirContext.getVersion().getVersion());
	}

	@Override
	public CodeValidationResult validateCodeInValueSet(ValidationSupportContext theValidationSupportContext, ConceptValidationOptions theOptions, String theCodeSystem, String theCode, String theDisplay, @Nonnull IBaseResource theValueSet) {
		String url = getValueSetUrl(theValueSet);
		return validateCode(theValidationSupportContext, theOptions, theCodeSystem, theCode, theDisplay, url);
	}

	@Override
	public CodeValidationResult validateCode(@Nonnull ValidationSupportContext theValidationSupportContext, @Nonnull ConceptValidationOptions theOptions, String theCodeSystem, String theCode, String theDisplay, String theValueSetUrl) {
		/* **************************************************************************************
		 * NOTE: Update validation_support_modules.html if any of the support in this module
		 * changes in any way!
		 * **************************************************************************************/

		Map<String, String> handlerMap = null;
		String expectSystem = null;
		switch (defaultString(theValueSetUrl)) {
			case USPS_VALUESET_URL:
				handlerMap = USPS_CODES;
				expectSystem = USPS_CODESYSTEM_URL;
				break;

			case CURRENCIES_VALUESET_URL:
				handlerMap = ISO_4217_CODES;
				expectSystem = CURRENCIES_CODESYSTEM_URL;
				break;

			case LANGUAGES_VALUESET_URL:
				if (!LANGUAGES_CODESYSTEM_URL.equals(theCodeSystem) && !(theCodeSystem == null && theOptions.isInferSystem())) {
					return new CodeValidationResult()
						.setSeverity(IssueSeverity.ERROR)
						.setMessage("Inappropriate CodeSystem URL \"" + theCodeSystem + "\" for ValueSet: " + theValueSetUrl);
				}

				IBaseResource languagesVs = myLanguagesVs;
				if (languagesVs == null) {
					languagesVs = theValidationSupportContext.getRootValidationSupport().fetchValueSet("http://hl7.org/fhir/ValueSet/languages");
					myLanguagesVs = (org.hl7.fhir.r5.model.ValueSet) myVersionConverter.toCanonical(languagesVs);
				}
				Optional<org.hl7.fhir.r5.model.ValueSet.ConceptReferenceComponent> match = myLanguagesVs
					.getCompose()
					.getInclude()
					.stream()
					.flatMap(t -> t.getConcept().stream())
					.filter(t -> theCode.equals(t.getCode()))
					.findFirst();
				if (match.isPresent()) {
					return new CodeValidationResult()
						.setCode(theCode)
						.setDisplay(match.get().getDisplay());
				} else {
					return new CodeValidationResult()
						.setSeverity(IssueSeverity.ERROR)
						.setMessage("Code \"" + theCode + "\" is not in valueset: " + theValueSetUrl);
				}

			case ALL_LANGUAGES_VALUESET_URL:
				if (!LANGUAGES_CODESYSTEM_URL.equals(theCodeSystem) && !(theCodeSystem == null && theOptions.isInferSystem())) {
					return new CodeValidationResult()
						.setSeverity(IssueSeverity.ERROR)
						.setMessage("Inappropriate CodeSystem URL \"" + theCodeSystem + "\" for ValueSet: " + theValueSetUrl);
				}

				LookupCodeResult outcome = lookupLanguageCode(theCode);
				if (outcome.isFound()) {
					return new CodeValidationResult()
						.setCode(theCode)
						.setDisplay(outcome.getCodeDisplay());
				} else {
					return new CodeValidationResult()
						.setSeverity(IssueSeverity.ERROR)
						.setMessage("Code \"" + theCode + "\" is not in valueset: " + theValueSetUrl);
				}

			case MIMETYPES_VALUESET_URL:
				// This is a pretty naive implementation - Should be enhanced in future
				return new CodeValidationResult()
					.setCode(theCode)
					.setDisplay(theDisplay);

			case UCUM_VALUESET_URL: {
				String system = theCodeSystem;
				if (system == null && theOptions.isInferSystem()) {
					system = UCUM_CODESYSTEM_URL;
				}
				CodeValidationResult validationResult = validateLookupCode(theValidationSupportContext, theCode, system);
				if (validationResult != null) {
					return validationResult;
				}
			}
		}

		if (handlerMap != null) {
			String display = handlerMap.get(theCode);
			if (display != null) {
				if (expectSystem.equals(theCodeSystem) || theOptions.isInferSystem()) {
					return new CodeValidationResult()
						.setCode(theCode)
						.setDisplay(display);
				}
			}

			return new CodeValidationResult()
				.setSeverity(IssueSeverity.ERROR)
				.setMessage("Code \"" + theCode + "\" is not in system: " + USPS_CODESYSTEM_URL);
		}

		if (isBlank(theValueSetUrl)) {
			CodeValidationResult validationResult = validateLookupCode(theValidationSupportContext, theCode, theCodeSystem);
			return validationResult;
		}

		return null;
	}

	@Nullable
	public CodeValidationResult validateLookupCode(ValidationSupportContext theValidationSupportContext, String theCode, String theSystem) {
		LookupCodeResult lookupResult = lookupCode(theValidationSupportContext, theSystem, theCode);
		CodeValidationResult validationResult = null;
		if (lookupResult != null) {
			if (lookupResult.isFound()) {
				validationResult = new CodeValidationResult()
					.setCode(lookupResult.getSearchedForCode())
					.setDisplay(lookupResult.getCodeDisplay());
			}
		}

		return validationResult;
	}


	@Override
	public LookupCodeResult lookupCode(ValidationSupportContext theValidationSupportContext, String theSystem, String theCode, String theDisplayLanguage) {
		Map<String, String> map;
		switch (theSystem) {
			case LANGUAGES_CODESYSTEM_URL:
				return lookupLanguageCode(theCode);
			case UCUM_CODESYSTEM_URL:
				return lookupUcumCode(theCode);
			case MIMETYPES_CODESYSTEM_URL:
				return lookupMimetypeCode(theCode);
			case COUNTRIES_CODESYSTEM_URL:
				map = ISO_3166_CODES;
				break;
			case CURRENCIES_CODESYSTEM_URL:
				map = ISO_4217_CODES;
				break;
			case USPS_CODESYSTEM_URL:
				map = USPS_CODES;
				break;
			default:
				return null;
		}

		String display = map.get(theCode);
		if (isNotBlank(display)) {
			LookupCodeResult retVal = new LookupCodeResult();
			retVal.setSearchedForCode(theCode);
			retVal.setSearchedForSystem(theSystem);
			retVal.setFound(true);
			retVal.setCodeDisplay(display);
			return retVal;
		}

		// If we get here it means we know the codesystem but the code was bad
		LookupCodeResult retVal = new LookupCodeResult();
		retVal.setSearchedForCode(theCode);
		retVal.setSearchedForSystem(theSystem);
		retVal.setFound(false);
		return retVal;

	}

	private LookupCodeResult lookupLanguageCode(String theCode) {
		if (myLanguagesLanugageMap == null || myLanguagesRegionMap == null) {
			initializeBcp47LanguageMap();
		}

		int langRegionSeparatorIndex = StringUtils.indexOfAny(theCode, '-', '_');
		boolean hasRegionAndCodeSegments = langRegionSeparatorIndex > 0;
		String language;
		String region;

		if (hasRegionAndCodeSegments) {
			// we look for languages in lowercase only
			// this will allow case insensitivity for language portion of code
			language = myLanguagesLanugageMap.get(theCode.substring(0, langRegionSeparatorIndex).toLowerCase());
			region = myLanguagesRegionMap.get(theCode.substring(langRegionSeparatorIndex + 1).toUpperCase());

			if (language == null || region == null) {
				//In case the user provides both a language and a region, they must both be valid for the lookup to succeed.
				ourLog.warn("Couldn't find a valid bcp47 language-region combination from code: {}", theCode);
				return buildNotFoundLookupCodeResult(theCode);
			} else {
				return buildLookupResultForLanguageAndRegion(theCode, language, region);
			}
		} else {
			//In case user has only provided a language, we build the lookup from only that.
			//NB: we only use the lowercase version of the language
			language = myLanguagesLanugageMap.get(theCode.toLowerCase());
			if (language == null) {
				ourLog.warn("Couldn't find a valid bcp47 language from code: {}", theCode);
				return buildNotFoundLookupCodeResult(theCode);
			} else {
				return buildLookupResultForLanguage(theCode, language);
			}
		}
	}
	private LookupCodeResult buildLookupResultForLanguageAndRegion(@Nonnull String theOriginalCode, @Nonnull String theLanguage, @Nonnull String theRegion) {
		LookupCodeResult lookupCodeResult = buildNotFoundLookupCodeResult(theOriginalCode);
		lookupCodeResult.setCodeDisplay(theLanguage + " " + theRegion);
		lookupCodeResult.setFound(true);
		return lookupCodeResult;
	}
	private LookupCodeResult buildLookupResultForLanguage(@Nonnull String theOriginalCode, @Nonnull String theLanguage) {
		LookupCodeResult lookupCodeResult = buildNotFoundLookupCodeResult(theOriginalCode);
		lookupCodeResult.setCodeDisplay(theLanguage);
		lookupCodeResult.setFound(true);
		return lookupCodeResult;
	}

	private LookupCodeResult buildNotFoundLookupCodeResult(@Nonnull String theOriginalCode) {
		LookupCodeResult lookupCodeResult = new LookupCodeResult();
		lookupCodeResult.setFound(false);
		lookupCodeResult.setSearchedForSystem(LANGUAGES_CODESYSTEM_URL);
		lookupCodeResult.setSearchedForCode(theOriginalCode);
		return lookupCodeResult;
	}

	private void initializeBcp47LanguageMap() {
		Map<String, String> regionsMap;
		Map<String, String> languagesMap;
		ourLog.info("Loading BCP47 Language Registry");

		String input = ClasspathUtil.loadResource("org/hl7/fhir/common/hapi/validation/support/registry.json");
		ArrayNode map;
		try {
			map = (ArrayNode) new ObjectMapper().readTree(input);
		} catch (JsonProcessingException e) {
			throw new ConfigurationException(Msg.code(694) + e);
		}

		languagesMap = new HashMap<>();
		regionsMap = new HashMap<>();

		for (int i = 0; i < map.size(); i++) {
			ObjectNode next = (ObjectNode) map.get(i);
			String type = next.get("Type").asText();
			if ("language".equals(type)) {
				String language = next.get("Subtag").asText();
				ArrayNode descriptions = (ArrayNode) next.get("Description");
				String description = null;
				if (descriptions.size() > 0) {
					description = descriptions.get(0).asText();
				}
				languagesMap.put(language, description);
			}
			if ("region".equals(type)) {
				String region = next.get("Subtag").asText();
				ArrayNode descriptions = (ArrayNode) next.get("Description");
				String description = null;
				if (descriptions.size() > 0) {
					description = descriptions.get(0).asText();
				}
				regionsMap.put(region, description);
			}
		}

		ourLog.info("Have {} languages and {} regions", languagesMap.size(), regionsMap.size());

		myLanguagesLanugageMap = languagesMap;
		myLanguagesRegionMap = regionsMap;
	}

	@Nonnull
	private LookupCodeResult lookupMimetypeCode(String theCode) {
		// This is a pretty naive implementation - Should be enhanced in future
		LookupCodeResult mimeRetVal = new LookupCodeResult();
		mimeRetVal.setSearchedForCode(theCode);
		mimeRetVal.setSearchedForSystem(MIMETYPES_CODESYSTEM_URL);
		mimeRetVal.setFound(true);
		return mimeRetVal;
	}

	@Nonnull
	private LookupCodeResult lookupUcumCode(String theCode) {
		InputStream input = ClasspathUtil.loadResourceAsStream("/ucum-essence.xml");
		String outcome = null;
		try {
			UcumEssenceService svc = new UcumEssenceService(input);
			outcome = svc.analyse(theCode);
		} catch (UcumException e) {
			ourLog.warn("Failed parse UCUM code: {}", theCode, e);
		} finally {
			ClasspathUtil.close(input);
		}
		LookupCodeResult retVal = new LookupCodeResult();
		retVal.setSearchedForCode(theCode);
		retVal.setSearchedForSystem(UCUM_CODESYSTEM_URL);
		if (outcome != null) {
			retVal.setFound(true);
			retVal.setCodeDisplay(outcome);
		}
		return retVal;
	}

	@Override
	public IBaseResource fetchCodeSystem(String theSystem) {

		Map<String, String> map;
		switch (defaultString(theSystem)) {
			case COUNTRIES_CODESYSTEM_URL:
				map = ISO_3166_CODES;
				break;
			case CURRENCIES_CODESYSTEM_URL:
				map = ISO_4217_CODES;
				break;
			default:
				return null;
		}

		CodeSystem retVal = new CodeSystem();
		retVal.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		retVal.setUrl(theSystem);
		for (Map.Entry<String, String> nextEntry : map.entrySet()) {
			retVal.addConcept().setCode(nextEntry.getKey()).setDisplay(nextEntry.getValue());
		}

		IBaseResource normalized = null;
		switch (getFhirContext().getVersion().getVersion()) {
			case DSTU2:
			case DSTU2_HL7ORG:
			case DSTU2_1:
				return null;
			case DSTU3:
				normalized = VersionConvertorFactory_30_40.convertResource(retVal, new BaseAdvisor_30_40(false));
				break;
			case R4:
				normalized = retVal;
				break;
			case R5:
				normalized = VersionConvertorFactory_40_50.convertResource(retVal, new BaseAdvisor_40_50(false));
				break;
		}

		Validate.notNull(normalized);

		return normalized;
	}

	@Override
	public boolean isCodeSystemSupported(ValidationSupportContext theValidationSupportContext, String theSystem) {

		switch (theSystem) {
			case COUNTRIES_CODESYSTEM_URL:
			case UCUM_CODESYSTEM_URL:
			case MIMETYPES_CODESYSTEM_URL:
			case USPS_CODESYSTEM_URL:
			case LANGUAGES_CODESYSTEM_URL:
				return true;
		}

		return false;
	}

	@Override
	public boolean isValueSetSupported(ValidationSupportContext theValidationSupportContext, String theValueSetUrl) {

		switch (theValueSetUrl) {
			case CURRENCIES_VALUESET_URL:
			case LANGUAGES_VALUESET_URL:
			case ALL_LANGUAGES_VALUESET_URL:
			case MIMETYPES_VALUESET_URL:
			case UCUM_VALUESET_URL:
			case USPS_VALUESET_URL:
				return true;
		}

		return false;
	}

	@Override
	public FhirContext getFhirContext() {
		return myFhirContext;
	}

	public static String getValueSetUrl(@Nonnull IBaseResource theValueSet) {
		String url;
		switch (theValueSet.getStructureFhirVersionEnum()) {
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
				throw new IllegalArgumentException(Msg.code(695) + "Can not handle version: " + theValueSet.getStructureFhirVersionEnum());
		}
		return url;
	}

	public static String getCodeSystemUrl(@Nonnull IBaseResource theCodeSystem) {
		String url;
		switch (theCodeSystem.getStructureFhirVersionEnum()) {
			case R4: {
				url = ((org.hl7.fhir.r4.model.CodeSystem) theCodeSystem).getUrl();
				break;
			}
			case R5: {
				url = ((org.hl7.fhir.r5.model.CodeSystem) theCodeSystem).getUrl();
				break;
			}
			case DSTU3:
			default:
				throw new IllegalArgumentException(Msg.code(696) + "Can not handle version: " + theCodeSystem.getStructureFhirVersionEnum());
		}
		return url;
	}

	public static String getValueSetVersion(@Nonnull IBaseResource theValueSet) {
		String version;
		switch (theValueSet.getStructureFhirVersionEnum()) {
			case DSTU3: {
				version = ((org.hl7.fhir.dstu3.model.ValueSet) theValueSet).getVersion();
				break;
			}
			case R4: {
				version = ((org.hl7.fhir.r4.model.ValueSet) theValueSet).getVersion();
				break;
			}
			case R5: {
				version = ((org.hl7.fhir.r5.model.ValueSet) theValueSet).getVersion();
				break;
			}
			case DSTU2:
			case DSTU2_HL7ORG:
			case DSTU2_1:
			default:
				version = null;
		}
		return version;
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

	private static HashMap<String, String> buildIso4217Codes() {
		HashMap<String, String> iso4217Codes = new HashMap<>();
		iso4217Codes.put("AED", "United Arab Emirates dirham");
		iso4217Codes.put("AFN", "Afghan afghani");
		iso4217Codes.put("ALL", "Albanian lek");
		iso4217Codes.put("AMD", "Armenian dram");
		iso4217Codes.put("ANG", "Netherlands Antillean guilder");
		iso4217Codes.put("AOA", "Angolan kwanza");
		iso4217Codes.put("ARS", "Argentine peso");
		iso4217Codes.put("AUD", "Australian dollar");
		iso4217Codes.put("AWG", "Aruban florin");
		iso4217Codes.put("AZN", "Azerbaijani manat");
		iso4217Codes.put("BAM", "Bosnia and Herzegovina convertible mark");
		iso4217Codes.put("BBD", "Barbados dollar");
		iso4217Codes.put("BDT", "Bangladeshi taka");
		iso4217Codes.put("BGN", "Bulgarian lev");
		iso4217Codes.put("BHD", "Bahraini dinar");
		iso4217Codes.put("BIF", "Burundian franc");
		iso4217Codes.put("BMD", "Bermudian dollar");
		iso4217Codes.put("BND", "Brunei dollar");
		iso4217Codes.put("BOB", "Boliviano");
		iso4217Codes.put("BOV", "Bolivian Mvdol (funds code)");
		iso4217Codes.put("BRL", "Brazilian real");
		iso4217Codes.put("BSD", "Bahamian dollar");
		iso4217Codes.put("BTN", "Bhutanese ngultrum");
		iso4217Codes.put("BWP", "Botswana pula");
		iso4217Codes.put("BYN", "Belarusian ruble");
		iso4217Codes.put("BZD", "Belize dollar");
		iso4217Codes.put("CAD", "Canadian dollar");
		iso4217Codes.put("CDF", "Congolese franc");
		iso4217Codes.put("CHE", "WIR Euro (complementary currency)");
		iso4217Codes.put("CHF", "Swiss franc");
		iso4217Codes.put("CHW", "WIR Franc (complementary currency)");
		iso4217Codes.put("CLF", "Unidad de Fomento (funds code)");
		iso4217Codes.put("CLP", "Chilean peso");
		iso4217Codes.put("CNY", "Renminbi (Chinese) yuan[8]");
		iso4217Codes.put("COP", "Colombian peso");
		iso4217Codes.put("COU", "Unidad de Valor Real (UVR) (funds code)[9]");
		iso4217Codes.put("CRC", "Costa Rican colon");
		iso4217Codes.put("CUC", "Cuban convertible peso");
		iso4217Codes.put("CUP", "Cuban peso");
		iso4217Codes.put("CVE", "Cape Verde escudo");
		iso4217Codes.put("CZK", "Czech koruna");
		iso4217Codes.put("DJF", "Djiboutian franc");
		iso4217Codes.put("DKK", "Danish krone");
		iso4217Codes.put("DOP", "Dominican peso");
		iso4217Codes.put("DZD", "Algerian dinar");
		iso4217Codes.put("EGP", "Egyptian pound");
		iso4217Codes.put("ERN", "Eritrean nakfa");
		iso4217Codes.put("ETB", "Ethiopian birr");
		iso4217Codes.put("EUR", "Euro");
		iso4217Codes.put("FJD", "Fiji dollar");
		iso4217Codes.put("FKP", "Falkland Islands pound");
		iso4217Codes.put("GBP", "Pound sterling");
		iso4217Codes.put("GEL", "Georgian lari");
		iso4217Codes.put("GGP", "Guernsey Pound");
		iso4217Codes.put("GHS", "Ghanaian cedi");
		iso4217Codes.put("GIP", "Gibraltar pound");
		iso4217Codes.put("GMD", "Gambian dalasi");
		iso4217Codes.put("GNF", "Guinean franc");
		iso4217Codes.put("GTQ", "Guatemalan quetzal");
		iso4217Codes.put("GYD", "Guyanese dollar");
		iso4217Codes.put("HKD", "Hong Kong dollar");
		iso4217Codes.put("HNL", "Honduran lempira");
		iso4217Codes.put("HRK", "Croatian kuna");
		iso4217Codes.put("HTG", "Haitian gourde");
		iso4217Codes.put("HUF", "Hungarian forint");
		iso4217Codes.put("IDR", "Indonesian rupiah");
		iso4217Codes.put("ILS", "Israeli new shekel");
		iso4217Codes.put("IMP", "Isle of Man Pound");
		iso4217Codes.put("INR", "Indian rupee");
		iso4217Codes.put("IQD", "Iraqi dinar");
		iso4217Codes.put("IRR", "Iranian rial");
		iso4217Codes.put("ISK", "Icelandic króna");
		iso4217Codes.put("JEP", "Jersey Pound");
		iso4217Codes.put("JMD", "Jamaican dollar");
		iso4217Codes.put("JOD", "Jordanian dinar");
		iso4217Codes.put("JPY", "Japanese yen");
		iso4217Codes.put("KES", "Kenyan shilling");
		iso4217Codes.put("KGS", "Kyrgyzstani som");
		iso4217Codes.put("KHR", "Cambodian riel");
		iso4217Codes.put("KMF", "Comoro franc");
		iso4217Codes.put("KPW", "North Korean won");
		iso4217Codes.put("KRW", "South Korean won");
		iso4217Codes.put("KWD", "Kuwaiti dinar");
		iso4217Codes.put("KYD", "Cayman Islands dollar");
		iso4217Codes.put("KZT", "Kazakhstani tenge");
		iso4217Codes.put("LAK", "Lao kip");
		iso4217Codes.put("LBP", "Lebanese pound");
		iso4217Codes.put("LKR", "Sri Lankan rupee");
		iso4217Codes.put("LRD", "Liberian dollar");
		iso4217Codes.put("LSL", "Lesotho loti");
		iso4217Codes.put("LYD", "Libyan dinar");
		iso4217Codes.put("MAD", "Moroccan dirham");
		iso4217Codes.put("MDL", "Moldovan leu");
		iso4217Codes.put("MGA", "Malagasy ariary");
		iso4217Codes.put("MKD", "Macedonian denar");
		iso4217Codes.put("MMK", "Myanmar kyat");
		iso4217Codes.put("MNT", "Mongolian tögrög");
		iso4217Codes.put("MOP", "Macanese pataca");
		iso4217Codes.put("MRU", "Mauritanian ouguiya");
		iso4217Codes.put("MUR", "Mauritian rupee");
		iso4217Codes.put("MVR", "Maldivian rufiyaa");
		iso4217Codes.put("MWK", "Malawian kwacha");
		iso4217Codes.put("MXN", "Mexican peso");
		iso4217Codes.put("MXV", "Mexican Unidad de Inversion (UDI) (funds code)");
		iso4217Codes.put("MYR", "Malaysian ringgit");
		iso4217Codes.put("MZN", "Mozambican metical");
		iso4217Codes.put("NAD", "Namibian dollar");
		iso4217Codes.put("NGN", "Nigerian naira");
		iso4217Codes.put("NIO", "Nicaraguan córdoba");
		iso4217Codes.put("NOK", "Norwegian krone");
		iso4217Codes.put("NPR", "Nepalese rupee");
		iso4217Codes.put("NZD", "New Zealand dollar");
		iso4217Codes.put("OMR", "Omani rial");
		iso4217Codes.put("PAB", "Panamanian balboa");
		iso4217Codes.put("PEN", "Peruvian Sol");
		iso4217Codes.put("PGK", "Papua New Guinean kina");
		iso4217Codes.put("PHP", "Philippine piso[13]");
		iso4217Codes.put("PKR", "Pakistani rupee");
		iso4217Codes.put("PLN", "Polish złoty");
		iso4217Codes.put("PYG", "Paraguayan guaraní");
		iso4217Codes.put("QAR", "Qatari riyal");
		iso4217Codes.put("RON", "Romanian leu");
		iso4217Codes.put("RSD", "Serbian dinar");
		iso4217Codes.put("RUB", "Russian ruble");
		iso4217Codes.put("RWF", "Rwandan franc");
		iso4217Codes.put("SAR", "Saudi riyal");
		iso4217Codes.put("SBD", "Solomon Islands dollar");
		iso4217Codes.put("SCR", "Seychelles rupee");
		iso4217Codes.put("SDG", "Sudanese pound");
		iso4217Codes.put("SEK", "Swedish krona/kronor");
		iso4217Codes.put("SGD", "Singapore dollar");
		iso4217Codes.put("SHP", "Saint Helena pound");
		iso4217Codes.put("SLL", "Sierra Leonean leone");
		iso4217Codes.put("SOS", "Somali shilling");
		iso4217Codes.put("SRD", "Surinamese dollar");
		iso4217Codes.put("SSP", "South Sudanese pound");
		iso4217Codes.put("STN", "São Tomé and Príncipe dobra");
		iso4217Codes.put("SVC", "Salvadoran colón");
		iso4217Codes.put("SYP", "Syrian pound");
		iso4217Codes.put("SZL", "Swazi lilangeni");
		iso4217Codes.put("THB", "Thai baht");
		iso4217Codes.put("TJS", "Tajikistani somoni");
		iso4217Codes.put("TMT", "Turkmenistan manat");
		iso4217Codes.put("TND", "Tunisian dinar");
		iso4217Codes.put("TOP", "Tongan paʻanga");
		iso4217Codes.put("TRY", "Turkish lira");
		iso4217Codes.put("TTD", "Trinidad and Tobago dollar");
		iso4217Codes.put("TVD", "Tuvalu Dollar");
		iso4217Codes.put("TWD", "New Taiwan dollar");
		iso4217Codes.put("TZS", "Tanzanian shilling");
		iso4217Codes.put("UAH", "Ukrainian hryvnia");
		iso4217Codes.put("UGX", "Ugandan shilling");
		iso4217Codes.put("USD", "United States dollar");
		iso4217Codes.put("USN", "United States dollar (next day) (funds code)");
		iso4217Codes.put("UYI", "Uruguay Peso en Unidades Indexadas (URUIURUI) (funds code)");
		iso4217Codes.put("UYU", "Uruguayan peso");
		iso4217Codes.put("UZS", "Uzbekistan som");
		iso4217Codes.put("VEF", "Venezuelan bolívar");
		iso4217Codes.put("VND", "Vietnamese đồng");
		iso4217Codes.put("VUV", "Vanuatu vatu");
		iso4217Codes.put("WST", "Samoan tala");
		iso4217Codes.put("XAF", "CFA franc BEAC");
		iso4217Codes.put("XAG", "Silver (one troy ounce)");
		iso4217Codes.put("XAU", "Gold (one troy ounce)");
		iso4217Codes.put("XBA", "European Composite Unit (EURCO) (bond market unit)");
		iso4217Codes.put("XBB", "European Monetary Unit (E.M.U.-6) (bond market unit)");
		iso4217Codes.put("XBC", "European Unit of Account 9 (E.U.A.-9) (bond market unit)");
		iso4217Codes.put("XBD", "European Unit of Account 17 (E.U.A.-17) (bond market unit)");
		iso4217Codes.put("XCD", "East Caribbean dollar");
		iso4217Codes.put("XDR", "Special drawing rights");
		iso4217Codes.put("XOF", "CFA franc BCEAO");
		iso4217Codes.put("XPD", "Palladium (one troy ounce)");
		iso4217Codes.put("XPF", "CFP franc (franc Pacifique)");
		iso4217Codes.put("XPT", "Platinum (one troy ounce)");
		iso4217Codes.put("XSU", "SUCRE");
		iso4217Codes.put("XTS", "Code reserved for testing purposes");
		iso4217Codes.put("XUA", "ADB Unit of Account");
		iso4217Codes.put("XXX", "No currency");
		iso4217Codes.put("YER", "Yemeni rial");
		iso4217Codes.put("ZAR", "South African rand");
		iso4217Codes.put("ZMW", "Zambian kwacha");
		iso4217Codes.put("ZWL", "Zimbabwean dollar A/10");
		return iso4217Codes;
	}


	private static HashMap<String, String> buildIso3166Codes() {
		HashMap<String, String> codes = new HashMap<>();

		// 2 letter codes
		codes.put("AF", "Afghanistan");
		codes.put("AX", "Åland Islands");
		codes.put("AL", "Albania");
		codes.put("DZ", "Algeria");
		codes.put("AS", "American Samoa");
		codes.put("AD", "Andorra");
		codes.put("AO", "Angola");
		codes.put("AI", "Anguilla");
		codes.put("AQ", "Antarctica");
		codes.put("AG", "Antigua & Barbuda");
		codes.put("AR", "Argentina");
		codes.put("AM", "Armenia");
		codes.put("AW", "Aruba");
		codes.put("AU", "Australia");
		codes.put("AT", "Austria");
		codes.put("AZ", "Azerbaijan");
		codes.put("BS", "Bahamas");
		codes.put("BH", "Bahrain");
		codes.put("BD", "Bangladesh");
		codes.put("BB", "Barbados");
		codes.put("BY", "Belarus");
		codes.put("BE", "Belgium");
		codes.put("BZ", "Belize");
		codes.put("BJ", "Benin");
		codes.put("BM", "Bermuda");
		codes.put("BT", "Bhutan");
		codes.put("BO", "Bolivia");
		codes.put("BA", "Bosnia & Herzegovina");
		codes.put("BW", "Botswana");
		codes.put("BV", "Bouvet Island");
		codes.put("BR", "Brazil");
		codes.put("IO", "British Indian Ocean Territory");
		codes.put("VG", "British Virgin Islands");
		codes.put("BN", "Brunei");
		codes.put("BG", "Bulgaria");
		codes.put("BF", "Burkina Faso");
		codes.put("BI", "Burundi");
		codes.put("KH", "Cambodia");
		codes.put("CM", "Cameroon");
		codes.put("CA", "Canada");
		codes.put("CV", "Cape Verde");
		codes.put("BQ", "Caribbean Netherlands");
		codes.put("KY", "Cayman Islands");
		codes.put("CF", "Central African Republic");
		codes.put("TD", "Chad");
		codes.put("CL", "Chile");
		codes.put("CN", "China");
		codes.put("CX", "Christmas Island");
		codes.put("CC", "Cocos (Keeling) Islands");
		codes.put("CO", "Colombia");
		codes.put("KM", "Comoros");
		codes.put("CG", "Congo - Brazzaville");
		codes.put("CD", "Congo - Kinshasa");
		codes.put("CK", "Cook Islands");
		codes.put("CR", "Costa Rica");
		codes.put("CI", "Côte d’Ivoire");
		codes.put("HR", "Croatia");
		codes.put("CU", "Cuba");
		codes.put("CW", "Curaçao");
		codes.put("CY", "Cyprus");
		codes.put("CZ", "Czechia");
		codes.put("DK", "Denmark");
		codes.put("DJ", "Djibouti");
		codes.put("DM", "Dominica");
		codes.put("DO", "Dominican Republic");
		codes.put("EC", "Ecuador");
		codes.put("EG", "Egypt");
		codes.put("SV", "El Salvador");
		codes.put("GQ", "Equatorial Guinea");
		codes.put("ER", "Eritrea");
		codes.put("EE", "Estonia");
		codes.put("SZ", "Eswatini");
		codes.put("ET", "Ethiopia");
		codes.put("FK", "Falkland Islands");
		codes.put("FO", "Faroe Islands");
		codes.put("FJ", "Fiji");
		codes.put("FI", "Finland");
		codes.put("FR", "France");
		codes.put("GF", "French Guiana");
		codes.put("PF", "French Polynesia");
		codes.put("TF", "French Southern Territories");
		codes.put("GA", "Gabon");
		codes.put("GM", "Gambia");
		codes.put("GE", "Georgia");
		codes.put("DE", "Germany");
		codes.put("GH", "Ghana");
		codes.put("GI", "Gibraltar");
		codes.put("GR", "Greece");
		codes.put("GL", "Greenland");
		codes.put("GD", "Grenada");
		codes.put("GP", "Guadeloupe");
		codes.put("GU", "Guam");
		codes.put("GT", "Guatemala");
		codes.put("GG", "Guernsey");
		codes.put("GN", "Guinea");
		codes.put("GW", "Guinea-Bissau");
		codes.put("GY", "Guyana");
		codes.put("HT", "Haiti");
		codes.put("HM", "Heard & McDonald Islands");
		codes.put("HN", "Honduras");
		codes.put("HK", "Hong Kong SAR China");
		codes.put("HU", "Hungary");
		codes.put("IS", "Iceland");
		codes.put("IN", "India");
		codes.put("ID", "Indonesia");
		codes.put("IR", "Iran");
		codes.put("IQ", "Iraq");
		codes.put("IE", "Ireland");
		codes.put("IM", "Isle of Man");
		codes.put("IL", "Israel");
		codes.put("IT", "Italy");
		codes.put("JM", "Jamaica");
		codes.put("JP", "Japan");
		codes.put("JE", "Jersey");
		codes.put("JO", "Jordan");
		codes.put("KZ", "Kazakhstan");
		codes.put("KE", "Kenya");
		codes.put("KI", "Kiribati");
		codes.put("KW", "Kuwait");
		codes.put("KG", "Kyrgyzstan");
		codes.put("LA", "Laos");
		codes.put("LV", "Latvia");
		codes.put("LB", "Lebanon");
		codes.put("LS", "Lesotho");
		codes.put("LR", "Liberia");
		codes.put("LY", "Libya");
		codes.put("LI", "Liechtenstein");
		codes.put("LT", "Lithuania");
		codes.put("LU", "Luxembourg");
		codes.put("MO", "Macao SAR China");
		codes.put("MG", "Madagascar");
		codes.put("MW", "Malawi");
		codes.put("MY", "Malaysia");
		codes.put("MV", "Maldives");
		codes.put("ML", "Mali");
		codes.put("MT", "Malta");
		codes.put("MH", "Marshall Islands");
		codes.put("MQ", "Martinique");
		codes.put("MR", "Mauritania");
		codes.put("MU", "Mauritius");
		codes.put("YT", "Mayotte");
		codes.put("MX", "Mexico");
		codes.put("FM", "Micronesia");
		codes.put("MD", "Moldova");
		codes.put("MC", "Monaco");
		codes.put("MN", "Mongolia");
		codes.put("ME", "Montenegro");
		codes.put("MS", "Montserrat");
		codes.put("MA", "Morocco");
		codes.put("MZ", "Mozambique");
		codes.put("MM", "Myanmar (Burma)");
		codes.put("NA", "Namibia");
		codes.put("NR", "Nauru");
		codes.put("NP", "Nepal");
		codes.put("NL", "Netherlands");
		codes.put("NC", "New Caledonia");
		codes.put("NZ", "New Zealand");
		codes.put("NI", "Nicaragua");
		codes.put("NE", "Niger");
		codes.put("NG", "Nigeria");
		codes.put("NU", "Niue");
		codes.put("NF", "Norfolk Island");
		codes.put("KP", "North Korea");
		codes.put("MK", "North Macedonia");
		codes.put("MP", "Northern Mariana Islands");
		codes.put("NO", "Norway");
		codes.put("OM", "Oman");
		codes.put("PK", "Pakistan");
		codes.put("PW", "Palau");
		codes.put("PS", "Palestinian Territories");
		codes.put("PA", "Panama");
		codes.put("PG", "Papua New Guinea");
		codes.put("PY", "Paraguay");
		codes.put("PE", "Peru");
		codes.put("PH", "Philippines");
		codes.put("PN", "Pitcairn Islands");
		codes.put("PL", "Poland");
		codes.put("PT", "Portugal");
		codes.put("PR", "Puerto Rico");
		codes.put("QA", "Qatar");
		codes.put("RE", "Réunion");
		codes.put("RO", "Romania");
		codes.put("RU", "Russia");
		codes.put("RW", "Rwanda");
		codes.put("WS", "Samoa");
		codes.put("SM", "San Marino");
		codes.put("ST", "São Tomé & Príncipe");
		codes.put("SA", "Saudi Arabia");
		codes.put("SN", "Senegal");
		codes.put("RS", "Serbia");
		codes.put("SC", "Seychelles");
		codes.put("SL", "Sierra Leone");
		codes.put("SG", "Singapore");
		codes.put("SX", "Sint Maarten");
		codes.put("SK", "Slovakia");
		codes.put("SI", "Slovenia");
		codes.put("SB", "Solomon Islands");
		codes.put("SO", "Somalia");
		codes.put("ZA", "South Africa");
		codes.put("GS", "South Georgia & South Sandwich Islands");
		codes.put("KR", "South Korea");
		codes.put("SS", "South Sudan");
		codes.put("ES", "Spain");
		codes.put("LK", "Sri Lanka");
		codes.put("BL", "St. Barthélemy");
		codes.put("SH", "St. Helena");
		codes.put("KN", "St. Kitts & Nevis");
		codes.put("LC", "St. Lucia");
		codes.put("MF", "St. Martin");
		codes.put("PM", "St. Pierre & Miquelon");
		codes.put("VC", "St. Vincent & Grenadines");
		codes.put("SD", "Sudan");
		codes.put("SR", "Suriname");
		codes.put("SJ", "Svalbard & Jan Mayen");
		codes.put("SE", "Sweden");
		codes.put("CH", "Switzerland");
		codes.put("SY", "Syria");
		codes.put("TW", "Taiwan");
		codes.put("TJ", "Tajikistan");
		codes.put("TZ", "Tanzania");
		codes.put("TH", "Thailand");
		codes.put("TL", "Timor-Leste");
		codes.put("TG", "Togo");
		codes.put("TK", "Tokelau");
		codes.put("TO", "Tonga");
		codes.put("TT", "Trinidad & Tobago");
		codes.put("TN", "Tunisia");
		codes.put("TR", "Turkey");
		codes.put("TM", "Turkmenistan");
		codes.put("TC", "Turks & Caicos Islands");
		codes.put("TV", "Tuvalu");
		codes.put("UM", "U.S. Outlying Islands");
		codes.put("VI", "U.S. Virgin Islands");
		codes.put("UG", "Uganda");
		codes.put("UA", "Ukraine");
		codes.put("AE", "United Arab Emirates");
		codes.put("GB", "United Kingdom");
		codes.put("US", "United States");
		codes.put("UY", "Uruguay");
		codes.put("UZ", "Uzbekistan");
		codes.put("VU", "Vanuatu");
		codes.put("VA", "Vatican City");
		codes.put("VE", "Venezuela");
		codes.put("VN", "Vietnam");
		codes.put("WF", "Wallis & Futuna");
		codes.put("EH", "Western Sahara");
		codes.put("YE", "Yemen");
		codes.put("ZM", "Zambia");
		codes.put("ZW", "Zimbabwe");

		// 3 letter codes
		codes.put("ABW", "Aruba");
		codes.put("AFG", "Afghanistan");
		codes.put("AGO", "Angola");
		codes.put("AIA", "Anguilla");
		codes.put("ALA", "Åland Islands");
		codes.put("ALB", "Albania");
		codes.put("AND", "Andorra");
		codes.put("ARE", "United Arab Emirates");
		codes.put("ARG", "Argentina");
		codes.put("ARM", "Armenia");
		codes.put("ASM", "American Samoa");
		codes.put("ATA", "Antarctica");
		codes.put("ATF", "French Southern Territories");
		codes.put("ATG", "Antigua and Barbuda");
		codes.put("AUS", "Australia");
		codes.put("AUT", "Austria");
		codes.put("AZE", "Azerbaijan");
		codes.put("BDI", "Burundi");
		codes.put("BEL", "Belgium");
		codes.put("BEN", "Benin");
		codes.put("BES", "Bonaire, Sint Eustatius and Saba");
		codes.put("BFA", "Burkina Faso");
		codes.put("BGD", "Bangladesh");
		codes.put("BGR", "Bulgaria");
		codes.put("BHR", "Bahrain");
		codes.put("BHS", "Bahamas");
		codes.put("BIH", "Bosnia and Herzegovina");
		codes.put("BLM", "Saint Barthélemy");
		codes.put("BLR", "Belarus");
		codes.put("BLZ", "Belize");
		codes.put("BMU", "Bermuda");
		codes.put("BOL", "Bolivia, Plurinational State of");
		codes.put("BRA", "Brazil");
		codes.put("BRB", "Barbados");
		codes.put("BRN", "Brunei Darussalam");
		codes.put("BTN", "Bhutan");
		codes.put("BVT", "Bouvet Island");
		codes.put("BWA", "Botswana");
		codes.put("CAF", "Central African Republic");
		codes.put("CAN", "Canada");
		codes.put("CCK", "Cocos (Keeling) Islands");
		codes.put("CHE", "Switzerland");
		codes.put("CHL", "Chile");
		codes.put("CHN", "China");
		codes.put("CIV", "Côte d'Ivoire");
		codes.put("CMR", "Cameroon");
		codes.put("COD", "Congo, the Democratic Republic of the");
		codes.put("COG", "Congo");
		codes.put("COK", "Cook Islands");
		codes.put("COL", "Colombia");
		codes.put("COM", "Comoros");
		codes.put("CPV", "Cabo Verde");
		codes.put("CRI", "Costa Rica");
		codes.put("CUB", "Cuba");
		codes.put("CUW", "Curaçao");
		codes.put("CXR", "Christmas Island");
		codes.put("CYM", "Cayman Islands");
		codes.put("CYP", "Cyprus");
		codes.put("CZE", "Czechia");
		codes.put("DEU", "Germany");
		codes.put("DJI", "Djibouti");
		codes.put("DMA", "Dominica");
		codes.put("DNK", "Denmark");
		codes.put("DOM", "Dominican Republic");
		codes.put("DZA", "Algeria");
		codes.put("ECU", "Ecuador");
		codes.put("EGY", "Egypt");
		codes.put("ERI", "Eritrea");
		codes.put("ESH", "Western Sahara");
		codes.put("ESP", "Spain");
		codes.put("EST", "Estonia");
		codes.put("ETH", "Ethiopia");
		codes.put("FIN", "Finland");
		codes.put("FJI", "Fiji");
		codes.put("FLK", "Falkland Islands (Malvinas)");
		codes.put("FRA", "France");
		codes.put("FRO", "Faroe Islands");
		codes.put("FSM", "Micronesia, Federated States of");
		codes.put("GAB", "Gabon");
		codes.put("GBR", "United Kingdom");
		codes.put("GEO", "Georgia");
		codes.put("GGY", "Guernsey");
		codes.put("GHA", "Ghana");
		codes.put("GIB", "Gibraltar");
		codes.put("GIN", "Guinea");
		codes.put("GLP", "Guadeloupe");
		codes.put("GMB", "Gambia");
		codes.put("GNB", "Guinea-Bissau");
		codes.put("GNQ", "Equatorial Guinea");
		codes.put("GRC", "Greece");
		codes.put("GRD", "Grenada");
		codes.put("GRL", "Greenland");
		codes.put("GTM", "Guatemala");
		codes.put("GUF", "French Guiana");
		codes.put("GUM", "Guam");
		codes.put("GUY", "Guyana");
		codes.put("HKG", "Hong Kong");
		codes.put("HMD", "Heard Island and McDonald Islands");
		codes.put("HND", "Honduras");
		codes.put("HRV", "Croatia");
		codes.put("HTI", "Haiti");
		codes.put("HUN", "Hungary");
		codes.put("IDN", "Indonesia");
		codes.put("IMN", "Isle of Man");
		codes.put("IND", "India");
		codes.put("IOT", "British Indian Ocean Territory");
		codes.put("IRL", "Ireland");
		codes.put("IRN", "Iran, Islamic Republic of");
		codes.put("IRQ", "Iraq");
		codes.put("ISL", "Iceland");
		codes.put("ISR", "Israel");
		codes.put("ITA", "Italy");
		codes.put("JAM", "Jamaica");
		codes.put("JEY", "Jersey");
		codes.put("JOR", "Jordan");
		codes.put("JPN", "Japan");
		codes.put("KAZ", "Kazakhstan");
		codes.put("KEN", "Kenya");
		codes.put("KGZ", "Kyrgyzstan");
		codes.put("KHM", "Cambodia");
		codes.put("KIR", "Kiribati");
		codes.put("KNA", "Saint Kitts and Nevis");
		codes.put("KOR", "Korea, Republic of");
		codes.put("KWT", "Kuwait");
		codes.put("LAO", "Lao People's Democratic Republic");
		codes.put("LBN", "Lebanon");
		codes.put("LBR", "Liberia");
		codes.put("LBY", "Libya");
		codes.put("LCA", "Saint Lucia");
		codes.put("LIE", "Liechtenstein");
		codes.put("LKA", "Sri Lanka");
		codes.put("LSO", "Lesotho");
		codes.put("LTU", "Lithuania");
		codes.put("LUX", "Luxembourg");
		codes.put("LVA", "Latvia");
		codes.put("MAC", "Macao");
		codes.put("MAF", "Saint Martin (French part)");
		codes.put("MAR", "Morocco");
		codes.put("MCO", "Monaco");
		codes.put("MDA", "Moldova, Republic of");
		codes.put("MDG", "Madagascar");
		codes.put("MDV", "Maldives");
		codes.put("MEX", "Mexico");
		codes.put("MHL", "Marshall Islands");
		codes.put("MKD", "Macedonia, the former Yugoslav Republic of");
		codes.put("MLI", "Mali");
		codes.put("MLT", "Malta");
		codes.put("MMR", "Myanmar");
		codes.put("MNE", "Montenegro");
		codes.put("MNG", "Mongolia");
		codes.put("MNP", "Northern Mariana Islands");
		codes.put("MOZ", "Mozambique");
		codes.put("MRT", "Mauritania");
		codes.put("MSR", "Montserrat");
		codes.put("MTQ", "Martinique");
		codes.put("MUS", "Mauritius");
		codes.put("MWI", "Malawi");
		codes.put("MYS", "Malaysia");
		codes.put("MYT", "Mayotte");
		codes.put("NAM", "Namibia");
		codes.put("NCL", "New Caledonia");
		codes.put("NER", "Niger");
		codes.put("NFK", "Norfolk Island");
		codes.put("NGA", "Nigeria");
		codes.put("NIC", "Nicaragua");
		codes.put("NIU", "Niue");
		codes.put("NLD", "Netherlands");
		codes.put("NOR", "Norway");
		codes.put("NPL", "Nepal");
		codes.put("NRU", "Nauru");
		codes.put("NZL", "New Zealand");
		codes.put("OMN", "Oman");
		codes.put("PAK", "Pakistan");
		codes.put("PAN", "Panama");
		codes.put("PCN", "Pitcairn");
		codes.put("PER", "Peru");
		codes.put("PHL", "Philippines");
		codes.put("PLW", "Palau");
		codes.put("PNG", "Papua New Guinea");
		codes.put("POL", "Poland");
		codes.put("PRI", "Puerto Rico");
		codes.put("PRK", "Korea, Democratic People's Republic of");
		codes.put("PRT", "Portugal");
		codes.put("PRY", "Paraguay");
		codes.put("PSE", "Palestine, State of");
		codes.put("PYF", "French Polynesia");
		codes.put("QAT", "Qatar");
		codes.put("REU", "Réunion");
		codes.put("ROU", "Romania");
		codes.put("RUS", "Russian Federation");
		codes.put("RWA", "Rwanda");
		codes.put("SAU", "Saudi Arabia");
		codes.put("SDN", "Sudan");
		codes.put("SEN", "Senegal");
		codes.put("SGP", "Singapore");
		codes.put("SGS", "South Georgia and the South Sandwich Islands");
		codes.put("SHN", "Saint Helena, Ascension and Tristan da Cunha");
		codes.put("SJM", "Svalbard and Jan Mayen");
		codes.put("SLB", "Solomon Islands");
		codes.put("SLE", "Sierra Leone");
		codes.put("SLV", "El Salvador");
		codes.put("SMR", "San Marino");
		codes.put("SOM", "Somalia");
		codes.put("SPM", "Saint Pierre and Miquelon");
		codes.put("SRB", "Serbia");
		codes.put("SSD", "South Sudan");
		codes.put("STP", "Sao Tome and Principe");
		codes.put("SUR", "Suriname");
		codes.put("SVK", "Slovakia");
		codes.put("SVN", "Slovenia");
		codes.put("SWE", "Sweden");
		codes.put("SWZ", "Swaziland");
		codes.put("SXM", "Sint Maarten (Dutch part)");
		codes.put("SYC", "Seychelles");
		codes.put("SYR", "Syrian Arab Republic");
		codes.put("TCA", "Turks and Caicos Islands");
		codes.put("TCD", "Chad");
		codes.put("TGO", "Togo");
		codes.put("THA", "Thailand");
		codes.put("TJK", "Tajikistan");
		codes.put("TKL", "Tokelau");
		codes.put("TKM", "Turkmenistan");
		codes.put("TLS", "Timor-Leste");
		codes.put("TON", "Tonga");
		codes.put("TTO", "Trinidad and Tobago");
		codes.put("TUN", "Tunisia");
		codes.put("TUR", "Turkey");
		codes.put("TUV", "Tuvalu");
		codes.put("TWN", "Taiwan, Province of China");
		codes.put("TZA", "Tanzania, United Republic of");
		codes.put("UGA", "Uganda");
		codes.put("UKR", "Ukraine");
		codes.put("UMI", "United States Minor Outlying Islands");
		codes.put("URY", "Uruguay");
		codes.put("USA", "United States of America");
		codes.put("UZB", "Uzbekistan");
		codes.put("VAT", "Holy See");
		codes.put("VCT", "Saint Vincent and the Grenadines");
		codes.put("VEN", "Venezuela, Bolivarian Republic of");
		codes.put("VGB", "Virgin Islands, British");
		codes.put("VIR", "Virgin Islands, U.S.");
		codes.put("VNM", "Viet Nam");
		codes.put("VUT", "Vanuatu");
		codes.put("WLF", "Wallis and Futuna");
		codes.put("WSM", "Samoa");
		codes.put("YEM", "Yemen");
		codes.put("ZAF", "South Africa");
		codes.put("ZMB", "Zambia");
		codes.put("ZWE", "Zimbabwe");
		return codes;
	}

}
