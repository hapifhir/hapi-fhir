package ca.uhn.fhir.cql.common.helper;

/*-
 * #%L
 * HAPI FHIR JPA Server - Clinical Quality Language
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
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.DateTimeType;

import java.util.Date;

/**
 * Helper class to resolve period dates used by {@link ca.uhn.fhir.cql.dstu3.evaluation.MeasureEvaluationSeed}
 * and {@link ca.uhn.fhir.cql.r4.evaluation.MeasureEvaluationSeed}.
 */
public class DateHelper {

	/**
	 * @param date A date String in the format YYYY-MM-DD.
	 * @return A {@link java.util.Date} object representing the String data that was passed in.
	 */
	public static Date resolveRequestDate(String paramName, String date) {
		if (StringUtils.isBlank(date)) {
			throw new IllegalArgumentException(Msg.code(1662) + paramName + " parameter cannot be blank!");
		}
		return new DateTimeType(date).getValue();
	}
}
