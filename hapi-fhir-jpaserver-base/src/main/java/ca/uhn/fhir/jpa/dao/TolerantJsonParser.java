package ca.uhn.fhir.jpa.dao;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParserErrorHandler;
import ca.uhn.fhir.parser.JsonParser;
import ca.uhn.fhir.parser.LenientErrorHandler;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.defaultString;

public class TolerantJsonParser extends JsonParser {

	private static final Logger ourLog = LoggerFactory.getLogger(TolerantJsonParser.class);
	private final FhirContext myContext;
	private final Long myResourcePid;

	/**
	 * Constructor
	 *
	 * @param theResourcePid The ID of the resource that will be parsed with this parser. It would be ok to change the
	 *                       datatype for this param if we ever need to since it's only used for logging.
	 */
	public TolerantJsonParser(FhirContext theContext, IParserErrorHandler theParserErrorHandler, Long theResourcePid) {
		super(theContext, theParserErrorHandler);
		myContext = theContext;
		myResourcePid = theResourcePid;
	}

	@Override
	public <T extends IBaseResource> T parseResource(Class<T> theResourceType, String theMessageString) {
		try {
			return super.parseResource(theResourceType, theMessageString);
		} catch (DataFormatException e) {

			/*
			 * The following is a hacky and gross workaround until the following PR is hopefully merged:
			 * https://github.com/FasterXML/jackson-core/pull/611
			 *
			 * The issue this solves is that under Gson it was possible to store JSON containing
			 * decimal numbers with no leading integer (e.g. .123) and numbers with double leading
			 * zeros (e.g. 000.123).
			 *
			 * These don't parse in Jackson (which is valid behaviour, these aren't ok according to the
			 * JSON spec), meaning we can be stuck with data in the database that can't be loaded back out.
			 *
			 * Note that if we fix this in the future to rely on Jackson natively handing this
			 * nicely we may or may not be able to remove some code from
			 * ParserState.Primitive state too.
			 */

			String msg = defaultString(e.getMessage(), "");
			if (msg.contains("Unexpected character ('.' (code 46))") || msg.contains("Invalid numeric value: Leading zeroes not allowed")) {
				Gson gson = new Gson();

				JsonObject object = gson.fromJson(theMessageString, JsonObject.class);
				String corrected = gson.toJson(object);

				T parsed = super.parseResource(theResourceType, corrected);

				myContext.newTerser().visit(parsed, (theElement, theContainingElementPath, theChildDefinitionPath, theElementDefinitionPath) -> {

					BaseRuntimeElementDefinition<?> def = theElementDefinitionPath.get(theElementDefinitionPath.size() - 1);
					if (def.getName().equals("decimal")) {
						IPrimitiveType<BigDecimal> decimal = (IPrimitiveType<BigDecimal>) theElement;
						String oldValue = decimal.getValueAsString();
						String newValue = decimal.getValue().toPlainString();
						ourLog.warn("Correcting invalid previously saved decimal number for Resource[pid={}] - Was {} and now is {}",
							Objects.isNull(myResourcePid) ? "" : myResourcePid, oldValue, newValue);
						decimal.setValueAsString(newValue);
					}

					return true;
				});

				return parsed;
			}

			throw e;
		}
	}

	public static TolerantJsonParser createWithLenientErrorHandling(FhirContext theContext, @Nullable Long theResourcePid) {
		LenientErrorHandler errorHandler = new LenientErrorHandler(false).setErrorOnInvalidValue(false);
		return new TolerantJsonParser(theContext, errorHandler, theResourcePid);
	}
}
