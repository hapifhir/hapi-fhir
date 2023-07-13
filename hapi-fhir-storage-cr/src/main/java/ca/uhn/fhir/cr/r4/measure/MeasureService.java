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
package ca.uhn.fhir.cr.r4.measure;

import ca.uhn.fhir.cr.constant.MeasureReportConstants;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.util.BundleBuilder;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ContactDetail;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.Endpoint;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.MeasureReport;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.SearchParameter;
import org.hl7.fhir.r4.model.StringType;

import org.opencds.cqf.cql.evaluator.measure.MeasureEvaluationOptions;
import org.opencds.cqf.cql.evaluator.measure.r4.R4MeasureProcessor;
import org.opencds.cqf.cql.evaluator.measure.r4.R4RepositorySubjectProvider;
import org.opencds.cqf.fhir.api.Repository;
import org.opencds.cqf.fhir.utility.iterable.BundleIterator;
import org.opencds.cqf.fhir.utility.monad.Eithers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ca.uhn.fhir.cr.constant.MeasureReportConstants.COUNTRY_CODING_SYSTEM_CODE;
import static ca.uhn.fhir.cr.constant.MeasureReportConstants.MEASUREREPORT_MEASURE_SUPPLEMENTALDATA_EXTENSION;
import static ca.uhn.fhir.cr.constant.MeasureReportConstants.MEASUREREPORT_SUPPLEMENTALDATA_SEARCHPARAMETER_DEFINITION_DATE;
import static ca.uhn.fhir.cr.constant.MeasureReportConstants.MEASUREREPORT_SUPPLEMENTALDATA_SEARCHPARAMETER_URL;
import static ca.uhn.fhir.cr.constant.MeasureReportConstants.MEASUREREPORT_SUPPLEMENTALDATA_SEARCHPARAMETER_VERSION;
import static ca.uhn.fhir.cr.constant.MeasureReportConstants.US_COUNTRY_CODE;
import static ca.uhn.fhir.cr.constant.MeasureReportConstants.US_COUNTRY_DISPLAY;

public class MeasureService {

	private Logger ourLogger = LoggerFactory.getLogger(MeasureService.class);

	@Autowired
	protected MeasureEvaluationOptions myMeasureEvaluationOptions;

	@Autowired
	protected Repository myRepository;


	/**
	 * Implements the <a href=
	 * "https://www.hl7.org/fhir/operation-measure-evaluate-measure.html">$evaluate-measure</a>
	 * operation found in the
	 * <a href="http://www.hl7.org/fhir/clinicalreasoning-module.html">FHIR Clinical
	 * Reasoning Module</a>. This implementation aims to be compatible with the CQF
	 * IG.
	 *
	 * @param theId                  the Id of the Measure to evaluate
	 * @param thePeriodStart         The start of the reporting period
	 * @param thePeriodEnd           The end of the reporting period
	 * @param theReportType          The type of MeasureReport to generate
	 * @param thePractitioner        the thePractitioner to use for the evaluation
	 * @param theLastReceivedOn      the date the results of this measure were last
	 *                               received.
	 * @param theProductLine         the theProductLine (e.g. Medicare, Medicaid, etc) to use
	 *                               for the evaluation. This is a non-standard parameter.
	 * @param theAdditionalData      the data bundle containing additional data
	 * @param theTerminologyEndpoint the endpoint of terminology services for your measure valuesets
	 * @return the calculated MeasureReport
	 */
	public MeasureReport evaluateMeasure(IdType theId,
													 String thePeriodStart,
													 String thePeriodEnd,
													 String theReportType,
													 String theSubject,
													 String thePractitioner,
													 String theLastReceivedOn,
													 String theProductLine,
													 Bundle theAdditionalData,
													 Endpoint theTerminologyEndpoint) {

		ensureSupplementalDataElementSearchParameter();

		var r4MeasureProcessor = new R4MeasureProcessor(myRepository, myMeasureEvaluationOptions, new R4RepositorySubjectProvider(myRepository));

		MeasureReport measureReport = null;

		// SUBJECT LIST SETTERS
		if (StringUtils.isBlank(theSubject) && StringUtils.isNotBlank(thePractitioner)) {
			List<String> subjectIds = getPractitionerPatients(thePractitioner, myRepository);

			measureReport = r4MeasureProcessor.evaluateMeasure(Eithers.forMiddle3(theId), thePeriodStart, thePeriodEnd, theReportType, subjectIds, theAdditionalData);

		} else if (StringUtils.isNotBlank(theSubject)) {
			measureReport = r4MeasureProcessor.evaluateMeasure(Eithers.forMiddle3(theId), thePeriodStart, thePeriodEnd, theReportType, Collections.singletonList(theSubject), theAdditionalData);

		} else if (StringUtils.isBlank(theSubject) && StringUtils.isBlank(thePractitioner)) {
			measureReport = r4MeasureProcessor.evaluateMeasure(Eithers.forMiddle3(theId), thePeriodStart, thePeriodEnd, theReportType, null, theAdditionalData);
		}
		// add ProductLine after report is generated
		addProductLineExtension(measureReport, theProductLine);

		return measureReport;
	}

	private List<String> getPractitionerPatients(String thePractitioner, Repository theRepository) {
		List<String> patients = new ArrayList<>();

		Map<String, List< IQueryParameterType>> map = new HashMap<>();
		map.put(
			"general-practitioner", Collections.singletonList(
			new ReferenceParam(
				thePractitioner.startsWith("Practitioner/")
					? thePractitioner
					: "Practitioner/" + thePractitioner)));

		var bundle = theRepository.search(Bundle.class, Patient.class, map);
		var iterator = new BundleIterator<>(theRepository, Bundle.class, bundle);

		while (iterator.hasNext()) {
			var patient = iterator.next().getResource();
			var refString = patient.getIdElement().getResourceType() + "/" + patient.getIdElement().getIdPart();
			patients.add(refString);
		}
		return patients;
	}

	private void addProductLineExtension(MeasureReport theMeasureReport, String theProductLine) {
		if (theProductLine != null) {
			Extension ext = new Extension();
			ext.setUrl(MeasureReportConstants.MEASUREREPORT_PRODUCT_LINE_EXT_URL);
			ext.setValue(new StringType(theProductLine));
			theMeasureReport.addExtension(ext);
		}
	}

	protected void ensureSupplementalDataElementSearchParameter() {
		//create a transaction bundle
		BundleBuilder builder = new BundleBuilder(myRepository.fhirContext());

		//set the request to be condition on code == supplemental data
		builder.addTransactionCreateEntry(SUPPLEMENTAL_DATA_SEARCHPARAMETER).conditional("code=supplemental-data");
		myRepository.transaction(builder.getBundle());
	}

	public static final List<ContactDetail> CQI_CONTACTDETAIL = Collections.singletonList(
		new ContactDetail()
			.addTelecom(
				new ContactPoint()
					.setSystem(ContactPoint.ContactPointSystem.URL)
					.setValue("http://www.hl7.org/Special/committees/cqi/index.cfm")));

	public static final List<CodeableConcept> US_JURISDICTION_CODING =  Collections.singletonList(
		new CodeableConcept()
			.addCoding(
				new Coding(COUNTRY_CODING_SYSTEM_CODE, US_COUNTRY_CODE, US_COUNTRY_DISPLAY)));

	public static final SearchParameter SUPPLEMENTAL_DATA_SEARCHPARAMETER = (SearchParameter) new SearchParameter()
		.setUrl(MEASUREREPORT_SUPPLEMENTALDATA_SEARCHPARAMETER_URL)
		.setVersion(MEASUREREPORT_SUPPLEMENTALDATA_SEARCHPARAMETER_VERSION)
		.setName("DEQMMeasureReportSupplementalData")
		.setStatus(Enumerations.PublicationStatus.ACTIVE)
		.setDate(MEASUREREPORT_SUPPLEMENTALDATA_SEARCHPARAMETER_DEFINITION_DATE)
		.setPublisher("HL7 International - Clinical Quality Information Work Group")
		.setContact(CQI_CONTACTDETAIL)
		.setDescription(
			String.format(
				"Returns resources (supplemental data) from references on extensions on the MeasureReport with urls matching %s.",
				MEASUREREPORT_MEASURE_SUPPLEMENTALDATA_EXTENSION))
		.setJurisdiction(US_JURISDICTION_CODING)
		.addBase("MeasureReport")
		.setCode("supplemental-data")
		.setType(Enumerations.SearchParamType.REFERENCE)
		.setExpression(
			String.format("MeasureReport.extension('%s').value",
				MEASUREREPORT_MEASURE_SUPPLEMENTALDATA_EXTENSION))
		.setXpath(
			String.format("f:MeasureReport/f:extension[@url='%s'].value",
				MEASUREREPORT_MEASURE_SUPPLEMENTALDATA_EXTENSION))
		.setXpathUsage(SearchParameter.XPathUsageType.NORMAL)
		.setTitle("Supplemental Data")
		.setId("deqm-measurereport-supplemental-data");
}
