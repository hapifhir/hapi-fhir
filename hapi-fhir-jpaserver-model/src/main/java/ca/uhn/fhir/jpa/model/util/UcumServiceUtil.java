/*
 * #%L
 * HAPI FHIR JPA Model
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.model.util;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.ClasspathUtil;
import jakarta.annotation.Nullable;
import org.fhir.ucum.Decimal;
import org.fhir.ucum.Pair;
import org.fhir.ucum.UcumEssenceService;
import org.fhir.ucum.UcumException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * It's a wrapper of UcumEssenceService
 *
 */
public class UcumServiceUtil {

	private static final Logger ourLog = LoggerFactory.getLogger(UcumServiceUtil.class);

	public static final String CELSIUS_CODE = "Cel";
	public static final String FAHRENHEIT_CODE = "[degF]";
	public static final float CELSIUS_KELVIN_DIFF = 273.15f;

	public static final String UCUM_CODESYSTEM_URL = "http://unitsofmeasure.org";
	private static final String UCUM_SOURCE = "/ucum-essence.xml";
	private static final String UCUM_SECOND_CODE = "s";
	private static final BigDecimal SECONDS_PER_DAY = BigDecimal.valueOf(86400);

	private static UcumEssenceService myUcumEssenceService = null;

	private UcumServiceUtil() {}

	// lazy load UCUM_SOURCE only once
	private static void init() {

		if (myUcumEssenceService != null) return;

		synchronized (UcumServiceUtil.class) {
			InputStream input = ClasspathUtil.loadResourceAsStream(UCUM_SOURCE);
			try {
				myUcumEssenceService = new UcumEssenceService(input);

			} catch (UcumException e) {
				ourLog.warn("Failed to load ucum code from {}: {}", UCUM_SOURCE, e);
			} finally {
				ClasspathUtil.close(input);
			}
		}
	}

	/**
	 * Get the canonical form of a code, it's define at
	 * <link>http://unitsofmeasure.org</link>
	 *
	 * e.g. 12cm -> 0.12m where m is the canonical form of the length.
	 *
	 * @param theSystem must be http://unitsofmeasure.org
	 * @param theValue  the value in the original form e.g. 0.12
	 * @param theCode   the code in the original form e.g. 'cm'
	 * @return the CanonicalForm if no error, otherwise return null
	 */
	public static Pair getCanonicalForm(String theSystem, BigDecimal theValue, String theCode) {

		// -- only for http://unitsofmeasure.org
		if (!UCUM_CODESYSTEM_URL.equals(theSystem) || theValue == null || theCode == null) return null;

		if (isCelsiusOrFahrenheit(theCode)) {
			try {
				return getCanonicalFormForCelsiusOrFahrenheit(theValue, theCode);
			} catch (UcumException theE) {
				ourLog.error(
						"Exception when trying to obtain canonical form for value {} and code {}: {}",
						theValue,
						theCode,
						theE.getMessage());
				return null;
			}
		}

		init();
		Pair theCanonicalPair;

		try {
			Decimal theDecimal = new Decimal(theValue.toPlainString(), theValue.precision());
			theCanonicalPair = myUcumEssenceService.getCanonicalForm(new Pair(theDecimal, theCode));
			// For some reason code [degF], degree Fahrenheit, can't be converted. it returns value null.
			if (theCanonicalPair.getValue() == null) return null;
		} catch (UcumException e) {
			return null;
		}

		return theCanonicalPair;
	}

	private static Pair getCanonicalFormForCelsiusOrFahrenheit(BigDecimal theValue, String theCode)
			throws UcumException {
		return theCode.equals(CELSIUS_CODE) ? canonicalizeCelsius(theValue) : canonicalizeFahrenheit(theValue);
	}

	/**
	 * Returns the received Fahrenheit value converted to Kelvin units and code
	 * Formula is K = (x°F − 32) × 5/9 + 273.15
	 */
	private static Pair canonicalizeFahrenheit(BigDecimal theValue) throws UcumException {
		BigDecimal converted = theValue.subtract(BigDecimal.valueOf(32))
				.multiply(BigDecimal.valueOf(5f / 9f))
				.add(BigDecimal.valueOf(CELSIUS_KELVIN_DIFF));
		// disallow precision larger than input, as it matters when defining ranges
		BigDecimal adjusted = converted.setScale(theValue.precision(), RoundingMode.HALF_UP);

		Decimal newValue = new Decimal(adjusted.toPlainString());
		return new Pair(newValue, "K");
	}

	/**
	 * Returns the received Celsius value converted to Kelvin units and code
	 */
	private static Pair canonicalizeCelsius(BigDecimal theValue) throws UcumException {
		Decimal valueDec = new Decimal(theValue.toPlainString(), theValue.precision());
		Decimal converted = valueDec.add(new Decimal(Float.toString(CELSIUS_KELVIN_DIFF)));

		return new Pair(converted, "K");
	}

	private static boolean isCelsiusOrFahrenheit(String theCode) {
		return theCode.equals(CELSIUS_CODE) || theCode.equals(FAHRENHEIT_CODE);
	}

	@Nullable
	public static QuantityParam toCanonicalQuantityOrNull(QuantityParam theQuantityParam) {
		Pair canonicalForm = getCanonicalForm(
				theQuantityParam.getSystem(), theQuantityParam.getValue(), theQuantityParam.getUnits());
		if (canonicalForm != null) {
			BigDecimal valueValue = new BigDecimal(canonicalForm.getValue().asDecimal());
			String unitsValue = canonicalForm.getCode();
			return new QuantityParam()
					.setSystem(theQuantityParam.getSystem())
					.setValue(valueValue)
					.setUnits(unitsValue)
					.setPrefix(theQuantityParam.getPrefix());
		} else {
			return null;
		}
	}

	/**
	 * Converts a value expressed in a UCUM unit of time (e.g. {@literal s}, {@literal min}, {@literal h},
	 * {@literal d}, {@literal wk}, {@literal mo}, {@literal a}) into days, using the UCUM definitions
	 * of those units (e.g. {@literal a} is the Julian year of 365.25 days and {@literal mo} is 1/12 of it).
	 *
	 * @param theValue the value in the original unit
	 * @param theCode  the UCUM code of the original unit
	 * @return the value in days, or {@literal null} if the code is blank, is not a valid UCUM code,
	 * or is not a unit of time
	 */
	@Nullable
	public static BigDecimal convertToDaysOrNull(BigDecimal theValue, @Nullable String theCode) {
		if (theValue == null || isBlank(theCode)) {
			return null;
		}

		init();
		try {
			if (!myUcumEssenceService.isComparable(theCode, UCUM_SECOND_CODE)) {
				return null;
			}
			// Convert one unit to seconds (exact for every UCUM time unit) and do the arithmetic in BigDecimal,
			// since UcumEssenceService.convert() truncates its result to the precision of the input value
			BigDecimal secondsPerUnit = new BigDecimal(myUcumEssenceService
					.convert(new Decimal(1), theCode, UCUM_SECOND_CODE)
					.asDecimal());
			return theValue.multiply(secondsPerUnit).divide(SECONDS_PER_DAY, MathContext.DECIMAL128);
		} catch (UcumException e) {
			ourLog.debug("Unable to convert {} {} into days: {}", theValue, theCode, e.getMessage());
			return null;
		}
	}

	public static double convert(double theDistanceKm, String theSourceUnits, String theTargetUnits) {
		init();
		try {
			Decimal distance = new Decimal(Double.toString(theDistanceKm));
			Decimal output = myUcumEssenceService.convert(distance, theSourceUnits, theTargetUnits);
			String decimal = output.asDecimal();
			return Double.parseDouble(decimal);
		} catch (UcumException e) {
			throw new InvalidRequestException(Msg.code(2309) + e.getMessage());
		}
	}
}
