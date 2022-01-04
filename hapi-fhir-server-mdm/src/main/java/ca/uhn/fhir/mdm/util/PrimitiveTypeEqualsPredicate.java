package ca.uhn.fhir.mdm.util;

/*-
 * #%L
 * HAPI FHIR - Master Data Management
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

import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.lang.reflect.Field;
import java.util.function.BiPredicate;

public class PrimitiveTypeEqualsPredicate implements BiPredicate {

	@Override
	public boolean test(Object theBase1, Object theBase2) {
		if (theBase1 == null) {
			return theBase2 == null;
		}
		if (theBase2 == null) {
			return false;
		}
		if (!theBase1.getClass().equals(theBase2.getClass())) {
			return false;
		}

		for (Field f : theBase1.getClass().getDeclaredFields()) {
			Class<?> fieldClass = f.getType();

			if (!IPrimitiveType.class.isAssignableFrom(fieldClass)) {
				continue;
			}

			IPrimitiveType<?> val1, val2;

			f.setAccessible(true);
			try {
				val1 = (IPrimitiveType<?>) f.get(theBase1);
				val2 = (IPrimitiveType<?>) f.get(theBase2);
			} catch (Exception e) {
				// swallow
				continue;
			}

			if (val1 == null && val2 == null) {
				continue;
			}

			if (val1 == null || val2 == null) {
				return false;
			}

			Object actualVal1 = val1.getValue();
			Object actualVal2 = val2.getValue();

			if (actualVal1 == null && actualVal2 == null) {
				continue;
			}
			if (actualVal1 == null) {
				return false;
			}
			if (!actualVal1.equals(actualVal2)) {
				return false;
			}
		}

		return true;
	}
}
