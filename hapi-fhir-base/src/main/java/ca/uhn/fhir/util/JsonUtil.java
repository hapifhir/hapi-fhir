/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.util;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.model.api.annotation.SensitiveNoDisplay;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import jakarta.annotation.Nonnull;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

public class JsonUtil {

	private static final ObjectMapper ourMapperPrettyPrint;
	private static final ObjectMapper ourMapperNonPrettyPrint;
	private static final ObjectMapper ourMapperIncludeSensitive;

	public static final SimpleBeanPropertyFilter SIMPLE_BEAN_PROPERTY_FILTER = new SensitiveDataFilter();

	public static final SimpleFilterProvider SENSITIVE_DATA_FILTER_PROVIDER =
			new SimpleFilterProvider().addFilter(IModelJson.SENSITIVE_DATA_FILTER_NAME, SIMPLE_BEAN_PROPERTY_FILTER);
	public static final SimpleFilterProvider SHOW_ALL_DATA_FILTER_PROVIDER = new SimpleFilterProvider()
			.addFilter(IModelJson.SENSITIVE_DATA_FILTER_NAME, SimpleBeanPropertyFilter.serializeAll());

	static {
		ourMapperPrettyPrint = new ObjectMapper();
		ourMapperPrettyPrint.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		ourMapperPrettyPrint.setFilterProvider(SENSITIVE_DATA_FILTER_PROVIDER);
		ourMapperPrettyPrint.enable(SerializationFeature.INDENT_OUTPUT);

		ourMapperNonPrettyPrint = new ObjectMapper();
		ourMapperNonPrettyPrint.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		ourMapperNonPrettyPrint.setFilterProvider(SENSITIVE_DATA_FILTER_PROVIDER);
		ourMapperNonPrettyPrint.disable(SerializationFeature.INDENT_OUTPUT);

		ourMapperIncludeSensitive = new ObjectMapper();
		ourMapperIncludeSensitive.setFilterProvider(SHOW_ALL_DATA_FILTER_PROVIDER);
		ourMapperIncludeSensitive.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		ourMapperIncludeSensitive.disable(SerializationFeature.INDENT_OUTPUT);
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
	 * Parse JSON
	 */
	public static <T> T deserialize(@Nonnull InputStream theInput, @Nonnull Class<T> theType) throws IOException {
		return ourMapperPrettyPrint.readerFor(theType).readValue(theInput);
	}

	/**
	 * Includes fields which are annotated with {@link SensitiveNoDisplay}. Currently only meant to be used for serialization
	 * for batch job parameters.
	 */
	public static String serializeWithSensitiveData(@Nonnull IModelJson theInput) {
		try {
			return ourMapperIncludeSensitive.writeValueAsString(theInput);
		} catch (JsonProcessingException e) {
			throw new InvalidRequestException(Msg.code(2487) + "Failed to encode " + theInput.getClass(), e);
		}
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

	public FilterProvider getSensitiveDataFilterProvider() {
		return SENSITIVE_DATA_FILTER_PROVIDER;
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

	private static class SensitiveDataFilter extends SimpleBeanPropertyFilter {

		@Override
		protected boolean include(PropertyWriter writer) {
			return true; // Default include all except explicitly checked and excluded
		}

		@Override
		public void serializeAsField(Object pojo, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer)
				throws Exception {
			if (include(writer)) {
				if (!isFieldSensitive(writer)) {
					super.serializeAsField(pojo, gen, provider, writer);
				}
			}
		}

		private boolean isFieldSensitive(PropertyWriter writer) {
			return writer.getAnnotation(SensitiveNoDisplay.class) != null;
		}
	}
}
