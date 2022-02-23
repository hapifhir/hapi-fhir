package ca.uhn.fhir.mdm.rules.matcher;

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

import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.r4.model.BaseDateTimeType;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DateType;

public class HapiDateMatcherR4 {
	// TODO KHS code duplication (tried generalizing it with generics, but it got too convoluted)
	public boolean match(IBase theLeftBase, IBase theRightBase) {
		if (theLeftBase instanceof BaseDateTimeType && theRightBase instanceof BaseDateTimeType) {
			BaseDateTimeType leftDate = (BaseDateTimeType) theLeftBase;
			BaseDateTimeType rightDate = (BaseDateTimeType) theRightBase;
			int comparison = leftDate.getPrecision().compareTo(rightDate.getPrecision());
			if (comparison == 0) {
				return leftDate.getValueAsString().equals(rightDate.getValueAsString());
			}
			BaseDateTimeType leftPDate;
			BaseDateTimeType rightPDate;
			//Left date is coarser
			if (comparison < 0) {
				leftPDate = leftDate;
				if (rightDate instanceof DateType) {
					rightPDate = new DateType(rightDate.getValue(), leftDate.getPrecision());
				} else {
					rightPDate = new DateTimeType(rightDate.getValue(), leftDate.getPrecision());
				}
			//Right date is coarser
			} else {
				rightPDate = rightDate;
				if (leftDate instanceof DateType) {
					leftPDate = new DateType(leftDate.getValue(), rightDate.getPrecision());
				} else {
					leftPDate = new DateTimeType(leftDate.getValue(), rightDate.getPrecision());
				}
			}
			return leftPDate.getValueAsString().equals(rightPDate.getValueAsString());
		}

		return false;
	}
}
