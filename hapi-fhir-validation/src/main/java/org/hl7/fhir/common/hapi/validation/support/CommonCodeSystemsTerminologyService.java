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
	public static final String CURRENCIES_CODESYSTEM_URL = "urn:iso:std:iso:4217";
	public static final String CURRENCIES_VALUESET_URL = "http://hl7.org/fhir/ValueSet/currencies";
	private static final String USPS_CODESYSTEM_URL = "https://www.usps.com/";
	private static final String USPS_VALUESET_URL = "http://hl7.org/fhir/us/core/ValueSet/us-core-usps-state";
	private static Map<String, String> USPS_CODES = Collections.unmodifiableMap(buildUspsCodes());
	private static Map<String, String> ISO_4217_CODES = Collections.unmodifiableMap(buildIso4217Codes());


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

		Map<String, String> handlerMap = null;
		String expectSystem = null;
		switch (url) {
			case USPS_VALUESET_URL:
				handlerMap = USPS_CODES;
				expectSystem = USPS_CODESYSTEM_URL;
				break;

			case CURRENCIES_VALUESET_URL:
				handlerMap = ISO_4217_CODES;
				expectSystem = CURRENCIES_CODESYSTEM_URL;
				break;

			case LANGUAGES_VALUESET_URL:
			case MIMETYPES_VALUESET_URL:
				// This is a pretty naive implementation - Should be enhanced in future
				return new CodeValidationResult()
					.setCode(theCode)
					.setDisplay(theDisplay);
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

}
