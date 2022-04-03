package ca.uhn.fhir.util;

/*-
 * #%L
 * HAPI FHIR - Core Library
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

public class JsonUtil {

	private static final ObjectMapper ourMapperPrettyPrint;
	private static final ObjectMapper ourMapperNonPrettyPrint;

	static {
		ourMapperPrettyPrint = new ObjectMapper();
		ourMapperPrettyPrint.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		ourMapperPrettyPrint.enable(SerializationFeature.INDENT_OUTPUT);

		ourMapperNonPrettyPrint = new ObjectMapper();
		ourMapperNonPrettyPrint.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		ourMapperNonPrettyPrint.disable(SerializationFeature.INDENT_OUTPUT);
	}

	/**
	 * Parse JSON
	 */
	public static <T> T deserialize(@Nonnull String theInput, @Nonnull Class<T> theType) {
		try {
			return ourMapperPrettyPrint.readerFor(theType).readValue(theInput);
		} catch (IOException e) {
			// Should not happen
			throw new InternalErrorException(Msg.code(2060) + e);
		}
	}

	/**
	 * Parse JSON
	 */
	public static <T> List<T> deserializeList(@Nonnull String theInput, @Nonnull Class<T> theType) throws IOException {
		return ourMapperPrettyPrint.readerForListOf(theType).readValue(theInput);
	}

	/**
	 * Encode JSON
	 */
	public static String serialize(@Nonnull Object theInput) {
		return serialize(theInput, true);
	}

	/**
	 * Encode JSON
	 */
	public static String serialize(@Nonnull Object theInput, boolean thePrettyPrint) {
		try {
			StringWriter sw = new StringWriter();
			if (thePrettyPrint) {
				ourMapperPrettyPrint.writeValue(sw, theInput);
			} else {
				ourMapperNonPrettyPrint.writeValue(sw, theInput);
			}
			return sw.toString();
		} catch (IOException e) {
			// Should not happen
			throw new InternalErrorException(Msg.code(2061) + e);
		}
	}

	/**
	 * Encode JSON
	 */
	public static void serialize(@Nonnull Object theInput, @Nonnull Writer theWriter) throws IOException {
		// Note: We append a string here rather than just having ourMapper write directly
		// to the Writer because ourMapper seems to close the writer for some stupid
		// reason.. There's probably a way of preventing that bit I'm not sure what that
		// is and it's not a big deal here.
		theWriter.append(serialize(theInput));
	}

	public static String serializeOrInvalidRequest(IModelJson theJson) {
		try {
			return ourMapperNonPrettyPrint.writeValueAsString(theJson);
		} catch (JsonProcessingException e) {
			throw new InvalidRequestException(Msg.code(1741) + "Failed to encode " + theJson.getClass(), e);
		}
	}
}
