package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParserErrorHandler;
import ca.uhn.fhir.parser.JsonParser;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.commons.lang3.StringUtils.defaultString;

public class TolerantJsonParser extends JsonParser {

	private static final Logger ourLog = LoggerFactory.getLogger(TolerantJsonParser.class);

	/**
	 * Do not use this constructor, the recommended way to obtain a new instance of the JSON parser is to invoke
	 * {@link FhirContext#newJsonParser()}.
	 */
	public TolerantJsonParser(FhirContext theContext, IParserErrorHandler theParserErrorHandler) {
		super(theContext, theParserErrorHandler);
	}

	@Override
	public <T extends IBaseResource> T parseResource(Class<T> theResourceType, String theMessageString) {
		try {
			return super.parseResource(theResourceType, theMessageString);
		} catch (DataFormatException e) {
			if (defaultString(e.getMessage()).contains("Unexpected character ('.' (code 46))")) {

				/*
				 * The following is a hacky and gross workaround until the following PR is hopefully merged:
				 * https://github.com/FasterXML/jackson-core/pull/611
				 *
				 * The issue this solves is that under Gson it was possible to store JSON containing
				 * decimal numbers with no leading integer, e.g. .123
				 *
				 * These don't parse in Jackson, meaning we can be stuck with data in the database
				 * that can't be loaded back out.
				 *
				 * Note that if we fix this in the future to rely on Jackson natively handing this
				 * nicely we may or may not be able to remove some code from
				 * ParserState.Primitive state too.
				 */

				Gson gson = new Gson();

				JsonObject object = gson.fromJson(theMessageString, JsonObject.class);
				String corrected = gson.toJson(object);

				return super.parseResource(theResourceType, corrected);

			}
			throw e;
		}
	}

}
