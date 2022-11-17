package ca.uhn.fhir.cr.behavior.dstu3;

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

import ca.uhn.fhir.cr.behavior.IDaoRegistryUser;
import ca.uhn.fhir.cr.behavior.IIdCreator;
import ca.uhn.fhir.cr.utility.Ids;
import ca.uhn.fhir.cr.utility.Searches;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.ContactDetail;
import org.hl7.fhir.dstu3.model.ContactPoint;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus;
import org.hl7.fhir.dstu3.model.Enumerations.SearchParamType;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.MeasureReport;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.SearchParameter;
import org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IMeasureReportUser extends IDaoRegistryUser, IIdCreator {
	Logger ourLog = LoggerFactory.getLogger(IMeasureReportUser.class);

	static final String MEASUREREPORT_IMPROVEMENT_NOTATION_SYSTEM = "http://terminology.hl7.org/CodeSystem/measure-improvement-notation";
	static final String MEASUREREPORT_MEASURE_POPULATION_SYSTEM = "http://terminology.hl7.org/CodeSystem/measure-population";
	static final String MEASUREREPORT_MEASURE_SUPPLEMENTALDATA_EXTENSION = "http://hl7.org/fhir/us/davinci-deqm/StructureDefinition/extension-supplementalData";
	static final String MEASUREREPORT_SUPPLEMENTALDATA_SEARCHPARAMETER_URL = "http://hl7.org/fhir/us/davinci-deqm/SearchParameter/measurereport-supplemental-data";
	static final String MEASUREREPORT_SUPPLEMENTALDATA_SEARCHPARAMETER_VERSION = "0.1.0";

	static final List<ContactDetail> CQI_CONTACTDETAIL = new ArrayList<ContactDetail>() {
		{
			add(
				new ContactDetail()
					.addTelecom(
						new ContactPoint()
							.setSystem(ContactPointSystem.URL)
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

	default Map<String, Resource> getEvaluatedResources(MeasureReport theReport) {
		Map<String, Resource> resources = new HashMap<>();
		getEvaluatedResources(theReport, resources);

		return resources;
	}

	default IMeasureReportUser getEvaluatedResources(MeasureReport theReport, Map<String, Resource> theResources) {
		var bundle = (Bundle) read(new IdType(theReport.getEvaluatedResources().getReference()));
		bundle.getEntry().forEach(entry -> {
			var resource = entry.getResource();
			IIdType resourceId = resource.getIdElement();
			if (resourceId.getResourceType() == null || theResources.containsKey(Ids.simple(resourceId))) {
				return;
			}
			theResources.put(Ids.simple(resourceId), resource);
		});

		return this;
	}

	default Map<String, Resource> getSDE(MeasureReport theReport) {
		Map<String, Resource> sdeMap = new HashMap<>();
		getSDE(theReport, sdeMap);
		return sdeMap;
	}

	default IMeasureReportUser getSDE(MeasureReport theReport, Map<String, Resource> theResources) {
		if (theReport.hasExtension()) {
			for (Extension extension : theReport.getExtension()) {
				if (extension.hasUrl() && extension.getUrl().equals(MEASUREREPORT_MEASURE_SUPPLEMENTALDATA_EXTENSION)) {
					Reference sdeRef = extension.hasValue() && extension.getValue() instanceof Reference
						? (Reference) extension.getValue()
						: null;
					if (sdeRef != null && sdeRef.hasReference() && !sdeRef.getReference().startsWith("#")) {
						IdType sdeId = new IdType(sdeRef.getReference());
						if (!theResources.containsKey(Ids.simple(sdeId))) {
							theResources.put(Ids.simple(sdeId), read(sdeId));
						}
					}
				}
			}
		}
		return this;
	}

	default OperationOutcome generateIssue(String theSeverity, String theIssue) {
		OperationOutcome error = new OperationOutcome();
		error.addIssue()
			.setSeverity(OperationOutcome.IssueSeverity.fromCode(theSeverity))
			.setCode(OperationOutcome.IssueType.PROCESSING)
			.setDetails(new CodeableConcept().setText(theIssue));
		return error;
	}

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
