/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.cr.constant;

import java.sql.Date;
import java.time.LocalDate;

public class MeasureReportConstants {

	private MeasureReportConstants() {}

	public static final String MEASUREREPORT_IMPROVEMENT_NOTATION_SYSTEM =
			"http://terminology.hl7.org/CodeSystem/measure-improvement-notation";
	public static final String MEASUREREPORT_MEASURE_POPULATION_SYSTEM =
			"http://terminology.hl7.org/CodeSystem/measure-population";
	public static final String MEASUREREPORT_MEASURE_SUPPLEMENTALDATA_EXTENSION =
			"http://hl7.org/fhir/us/davinci-deqm/StructureDefinition/extension-supplementalData";
	public static final String MEASUREREPORT_SUPPLEMENTALDATA_SEARCHPARAMETER_URL =
			"http://hl7.org/fhir/us/davinci-deqm/SearchParameter/measurereport-supplemental-data";
	public static final String MEASUREREPORT_PRODUCT_LINE_EXT_URL =
			"http://hl7.org/fhir/us/cqframework/cqfmeasures/StructureDefinition/cqfm-productLine";
	public static final String MEASUREREPORT_SUPPLEMENTALDATA_SEARCHPARAMETER_VERSION = "0.1.0";
	public static final Date MEASUREREPORT_SUPPLEMENTALDATA_SEARCHPARAMETER_DEFINITION_DATE =
			Date.valueOf(LocalDate.of(2022, 7, 20));
	public static final String COUNTRY_CODING_SYSTEM_CODE = "urn:iso:std:iso:3166";
	public static final String US_COUNTRY_CODE = "US";
	public static final String US_COUNTRY_DISPLAY = "United States of America";
}
