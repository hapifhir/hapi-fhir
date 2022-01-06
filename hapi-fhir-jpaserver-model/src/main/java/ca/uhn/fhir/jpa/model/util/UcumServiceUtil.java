package ca.uhn.fhir.jpa.model.util;

/*
 * #%L
 * HAPI FHIR JPA Model
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

import java.io.InputStream;
import java.math.BigDecimal;

import ca.uhn.fhir.rest.param.QuantityParam;
import org.fhir.ucum.Decimal;
import org.fhir.ucum.Pair;
import org.fhir.ucum.UcumEssenceService;
import org.fhir.ucum.UcumException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.util.ClasspathUtil;

import javax.annotation.Nullable;

/**
 * It's a wrapper of UcumEssenceService
 *
 */
public class UcumServiceUtil {

	private static final Logger ourLog = LoggerFactory.getLogger(UcumServiceUtil.class);

	public static final String UCUM_CODESYSTEM_URL = "http://unitsofmeasure.org";
	private static final String UCUM_SOURCE = "/ucum-essence.xml";

	private static UcumEssenceService myUcumEssenceService = null;

	private UcumServiceUtil() {
	}

	// lazy load UCUM_SOURCE only once
	private static void init() {

		if (myUcumEssenceService != null)
			return;

		synchronized (UcumServiceUtil.class) {
			InputStream input = ClasspathUtil.loadResourceAsStream(UCUM_SOURCE);
			try {
				myUcumEssenceService = new UcumEssenceService(input);

			} catch (UcumException e) {
				ourLog.warn("Failed to load ucum code from ", UCUM_SOURCE, e);
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
		if (!UCUM_CODESYSTEM_URL.equals(theSystem) || theValue == null || theCode == null)
			return null;

		init();
		Pair theCanonicalPair;

		try {
			Decimal theDecimal = new Decimal(theValue.toPlainString(), theValue.precision());
			theCanonicalPair = myUcumEssenceService.getCanonicalForm(new Pair(theDecimal, theCode));
			// For some reason code [degF], degree Fahrenheit, can't be converted. it returns value null.
			if (theCanonicalPair.getValue() == null)
				return null;
		} catch (UcumException e) {
			return null;
		}

		return theCanonicalPair;
	}

    @Nullable
    public static QuantityParam toCanonicalQuantityOrNull(QuantityParam theQuantityParam) {
        Pair canonicalForm = getCanonicalForm(theQuantityParam.getSystem(), theQuantityParam.getValue(), theQuantityParam.getUnits());
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
}
