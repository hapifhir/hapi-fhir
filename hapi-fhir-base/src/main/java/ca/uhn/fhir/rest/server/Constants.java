package ca.uhn.fhir.rest.server;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Constants {

	public static final String CT_FHIR_JSON = "application/json+fhir";
	public static final String CT_FHIR_XML = "application/xml+fhir";
	public static final String PARAM_FORMAT = "_format";
	public static final String URL_TOKEN_HISTORY = "_history";
	public static final String CT_ATOM_XML = "application/atom+xml";
	public static final Set<String> FORMAT_VAL_XML;
	public static final Set<String> FORMAT_VAL_JSON;
	public static final Map<String, EncodingUtil> FORMAT_VAL_TO_ENCODING;

	static {
		Map<String, EncodingUtil> valToEncoding = new HashMap<String, EncodingUtil>();
		
		HashSet<String> valXml = new HashSet<String>();
		valXml.add(CT_FHIR_XML);
		valXml.add("application/xml");
		valXml.add("xml");
		FORMAT_VAL_XML = Collections.unmodifiableSet(valXml);
		for (String string : valXml) {
			valToEncoding.put(string, EncodingUtil.XML);
		}

		HashSet<String> valJson = new HashSet<String>();
		valJson.add(CT_FHIR_JSON);
		valJson.add("application/json");
		valJson.add("json");
		FORMAT_VAL_JSON = Collections.unmodifiableSet(valJson);
		for (String string : valJson) {
			valToEncoding.put(string, EncodingUtil.JSON);
		}

		FORMAT_VAL_TO_ENCODING=Collections.unmodifiableMap(valToEncoding);
	}

}
