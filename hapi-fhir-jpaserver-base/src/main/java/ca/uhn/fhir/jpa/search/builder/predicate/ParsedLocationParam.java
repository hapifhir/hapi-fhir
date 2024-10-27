/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.search.builder.predicate;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.model.util.UcumServiceUtil;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.SpecialParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.UrlUtil;
import org.apache.commons.lang3.StringUtils;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class ParsedLocationParam {
	private final double myLatitudeValue;
	private final double myLongitudeValue;
	private final double myDistanceKm;

	private ParsedLocationParam(String theLatitudeValue, String theLongitudeValue, double theDistanceKm) {
		myLatitudeValue = parseLatLonParameter(theLatitudeValue);
		myLongitudeValue = parseLatLonParameter(theLongitudeValue);
		myDistanceKm = theDistanceKm;
	}

	private static double parseLatLonParameter(String theValue) {
		try {
			return Double.parseDouble(defaultString(theValue));
		} catch (NumberFormatException e) {
			throw new InvalidRequestException(
					Msg.code(2308) + "Invalid lat/lon parameter value: " + UrlUtil.sanitizeUrlPart(theValue));
		}
	}

	public double getLatitudeValue() {
		return myLatitudeValue;
	}

	public double getLongitudeValue() {
		return myLongitudeValue;
	}

	public double getDistanceKm() {
		return myDistanceKm;
	}

	public static ParsedLocationParam from(SearchParameterMap theParams, IQueryParameterType theParam) {
		String latitudeValue;
		String longitudeValue;
		double distanceKm = 0.0;

		if (theParam instanceof TokenParam) { // DSTU3
			TokenParam param = (TokenParam) theParam;
			String value = param.getValue();
			String[] parts = value.split(":");
			if (parts.length != 2) {
				throw new IllegalArgumentException(Msg.code(1228) + "Invalid position format '" + value
						+ "'.  Required format is 'latitude:longitude'");
			}
			latitudeValue = parts[0];
			longitudeValue = parts[1];
			if (isBlank(latitudeValue) || isBlank(longitudeValue)) {
				throw new IllegalArgumentException(Msg.code(1229) + "Invalid position format '" + value
						+ "'.  Both latitude and longitude must be provided.");
			}
			QuantityParam distanceParam = theParams.getNearDistanceParam();
			if (distanceParam != null) {
				distanceKm = parseLatLonParameter(distanceParam.getValueAsString());
			}
		} else if (theParam instanceof SpecialParam) { // R4
			SpecialParam param = (SpecialParam) theParam;
			String value = param.getValue();
			String[] parts = StringUtils.split(value, '|');
			if (parts.length < 2 || parts.length > 4) {
				throw new IllegalArgumentException(
						Msg.code(1230) + "Invalid position format '" + value
								+ "'.  Required format is 'latitude|longitude' or 'latitude|longitude|distance' or 'latitude|longitude|distance|units'");
			}
			latitudeValue = parts[0];
			longitudeValue = parts[1];
			if (isBlank(latitudeValue) || isBlank(longitudeValue)) {
				throw new IllegalArgumentException(Msg.code(1231) + "Invalid position format '" + value
						+ "'.  Both latitude and longitude must be provided.");
			}
			if (parts.length >= 3) {
				String distanceString = parts[2];
				if (!isBlank(distanceString)) {
					distanceKm = parseLatLonParameter(distanceString);
				}

				if (parts.length >= 4) {
					String distanceUnits = parts[3];
					distanceKm = UcumServiceUtil.convert(distanceKm, distanceUnits, "km");
				}
			}
		} else {
			throw new IllegalArgumentException(Msg.code(1232) + "Invalid position type: " + theParam.getClass());
		}

		return new ParsedLocationParam(latitudeValue, longitudeValue, distanceKm);
	}
}
