package ca.uhn.fhir.util;

import java.io.InputStream;
import java.math.BigDecimal;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import org.fhir.ucum.Decimal;
import org.fhir.ucum.Pair;
import org.fhir.ucum.UcumEssenceService;
import org.fhir.ucum.UcumException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * It's a wraper of UcumEssenceService
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

	public static Pair getCanonicalForm(String theSystem, BigDecimal theValue, String theCode) {

		//-- only for http://unitsofmeasure.org
		if (!UCUM_CODESYSTEM_URL.equals(theSystem) || theValue == null || theCode == null)
			return null;
		
		init();
		Pair theCanonicalPair = null;

		try {
			Decimal theDecimal = new Decimal(theValue.toPlainString());
			theCanonicalPair = myUcumEssenceService.getCanonicalForm(new Pair(theDecimal, theCode));
		} catch (UcumException e) {
			return null;
		}
		return theCanonicalPair;
	}

	public static Decimal convert(Decimal theValue, String theSourceUnit, String theDestUnit) {

		if (theValue == null || theSourceUnit == null || theDestUnit == null)
			return null;

		init();
		Decimal theDest = null;

		try {
			theDest = myUcumEssenceService.convert(theValue, theSourceUnit, theDestUnit);
		} catch (UcumException e) {
			return null;
		}
		return theDest;
	}
}
