package ca.uhn.fhir.cr.r4;

/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
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

import ca.uhn.fhir.cr.common.IDaoRegistryUser;
import ca.uhn.fhir.cr.common.Searches;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.Enumerations.PublicationStatus;
import org.hl7.fhir.r4.model.Enumerations.SearchParamType;
import org.hl7.fhir.r4.model.SearchParameter.XPathUsageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static ca.uhn.fhir.cr.common.SupplementalDataConstants.*;

public interface ISupplementalDataSearchParamUser extends IDaoRegistryUser {
	Logger ourLog = LoggerFactory.getLogger(ISupplementalDataSearchParamUser.class);


	static final List<ContactDetail> CQI_CONTACTDETAIL = new ArrayList<ContactDetail>() {
		{
			add(
				new ContactDetail()
					.addTelecom(
						new ContactPoint()
							.setSystem(ContactPoint.ContactPointSystem.URL)
							.setValue("http://www.hl7.org/Special/committees/cqi/index.cfm")));
		}
	};

	static final List<CodeableConcept> US_JURISDICTION_CODING = new ArrayList<CodeableConcept>() {
		{
			add(
				new CodeableConcept()
					.addCoding(
						new Coding("urn:iso:std:iso:3166", "US", "United States of America")));
		}
	};


	default void ensureSupplementalDataElementSearchParameter(RequestDetails theRequestDetails) {
		if (!search(SearchParameter.class,
			Searches.byUrl(MEASUREREPORT_SUPPLEMENTALDATA_SEARCHPARAMETER_URL,
				MEASUREREPORT_SUPPLEMENTALDATA_SEARCHPARAMETER_VERSION),
			theRequestDetails).isEmpty()) {
			return;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(2022, 7, 20);

		SearchParameter searchParameter = new SearchParameter()
			.setUrl(MEASUREREPORT_SUPPLEMENTALDATA_SEARCHPARAMETER_URL)
			.setVersion(MEASUREREPORT_SUPPLEMENTALDATA_SEARCHPARAMETER_VERSION)
			.setName("DEQMMeasureReportSupplementalData")
			.setStatus(PublicationStatus.ACTIVE)
			.setDate(calendar.getTime())
			.setPublisher("HL7 International - Clinical Quality Information Work Group")
			.setContact(CQI_CONTACTDETAIL)
			.setDescription(
				String.format(
					"Returns resources (supplemental data) from references on extensions on the MeasureReport with urls matching %s.",
					MEASUREREPORT_MEASURE_SUPPLEMENTALDATA_EXTENSION))
			.setJurisdiction(US_JURISDICTION_CODING)
			.addBase("MeasureReport")
			.setCode("supplemental-data")
			.setType(SearchParamType.REFERENCE)
			.setExpression(
				String.format("MeasureReport.extension('%s').value",
					MEASUREREPORT_MEASURE_SUPPLEMENTALDATA_EXTENSION))
			.setXpath(
				String.format("f:MeasureReport/f:extension[@url='%s'].value",
					MEASUREREPORT_MEASURE_SUPPLEMENTALDATA_EXTENSION))
			.setXpathUsage(XPathUsageType.NORMAL);

		searchParameter.setId("deqm-measurereport-supplemental-data");
		searchParameter.setTitle("Supplemental Data");

		create(searchParameter, theRequestDetails);
	}
}
