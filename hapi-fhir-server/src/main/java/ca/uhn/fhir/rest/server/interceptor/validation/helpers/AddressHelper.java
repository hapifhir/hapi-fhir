package ca.uhn.fhir.rest.server.interceptor.validation.helpers;

/*-
 * #%L
 * HAPI FHIR - Server Framework
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.PropertyModifyingHelper;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBase;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Helper class for working with FHIR Address element
 */
public class AddressHelper extends PropertyModifyingHelper {

	public static final String FIELD_LINE = "line";
	public static final String FIELD_CITY = "city";
	public static final String FIELD_TEXT = "text";
	public static final String FIELD_DISTRICT = "district";
	public static final String FIELD_STATE = "state";
	public static final String FIELD_POSTAL = "postalCode";
	public static final String FIELD_COUNTRY = "country";

	public static final String[] FIELD_NAMES = {FIELD_TEXT, FIELD_LINE, FIELD_CITY, FIELD_DISTRICT, FIELD_STATE,
		FIELD_POSTAL, FIELD_COUNTRY};

	public static final String[] ADDRESS_PARTS = {FIELD_CITY, FIELD_DISTRICT, FIELD_STATE, FIELD_POSTAL};

	public AddressHelper(FhirContext theFhirContext, IBase theBase) {
		super(theFhirContext, theBase);
	}

	public String getCountry() {
		return get(FIELD_COUNTRY);
	}

	public String getCity() {
		return get(FIELD_CITY);
	}

	public String getState() {
		return get(FIELD_STATE);
	}

	public String getPostalCode() {
		return get(FIELD_POSTAL);
	}

	public String getText() {
		return get(FIELD_TEXT);
	}

	public void setCountry(String theCountry) {
		set(FIELD_COUNTRY, theCountry);
	}

	public void setCity(String theCity) {
		set(FIELD_CITY, theCity);
	}

	public void setState(String theState) {
		set(FIELD_STATE, theState);
	}

	public void setPostalCode(String thePostal) {
		set(FIELD_POSTAL, thePostal);
	}

	public void setText(String theText) {
		set(FIELD_TEXT, theText);
	}

	public String getParts() {
		return Arrays.stream(ADDRESS_PARTS)
			.map(this::get)
			.filter(s -> !StringUtils.isBlank(s))
			.collect(Collectors.joining(getDelimiter()));
	}

	public String getLine() {
		return get(FIELD_LINE);
	}

	public List<String> getLines() {
		return getMultiple(FIELD_LINE);
	}

	public AddressHelper addLine(String theLine) {
		set(FIELD_LINE, theLine);
		return this;
	}

	public <T> T getAddress() {
		return (T) getBase();
	}

	@Override
	public String toString() {
		return getFields(FIELD_NAMES);
	}

}
