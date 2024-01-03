/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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

import ca.uhn.fhir.cr.common.IRepositoryFactory;
import ca.uhn.fhir.cr.r4.ICareGapsServiceFactory;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.Measure;
import org.hl7.fhir.r4.model.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public class CareGapsOperationProvider {
	private static final Logger ourLog = LoggerFactory.getLogger(CareGapsOperationProvider.class);

	@Autowired
	IRepositoryFactory myRepositoryFactory;

	@Autowired
	ICareGapsServiceFactory myR4CareGapsProcessorFactory;

	/**
	 * Implements the <a href=
	 * "http://build.fhir.org/ig/HL7/davinci-deqm/OperationDefinition-care-gaps.html">$care-gaps</a>
	 * operation found in the
	 * <a href="http://build.fhir.org/ig/HL7/davinci-deqm/index.html">Da Vinci DEQM
	 * FHIR Implementation Guide</a> that overrides the <a href=
	 * "http://build.fhir.org/operation-measure-care-gaps.html">$care-gaps</a>
	 * operation found in the
	 * <a href="http://hl7.org/fhir/R4/clinicalreasoning-module.html">FHIR Clinical
	 * Reasoning Module</a>.
	 *
	 * The operation calculates measures describing gaps in care. For more details,
	 * reference the <a href=
	 * "http://build.fhir.org/ig/HL7/davinci-deqm/gaps-in-care-reporting.html">Gaps
	 * in Care Reporting</a> section of the
	 * <a href="http://build.fhir.org/ig/HL7/davinci-deqm/index.html">Da Vinci DEQM
	 * FHIR Implementation Guide</a>.
	 *
	 * A Parameters resource that includes zero to many document bundles that
	 * include Care Gap Measure Reports will be returned.
	 *
	 * Usage:
	 * URL: [base]/Measure/$care-gaps
	 *
	 * @param theRequestDetails generally auto-populated by the HAPI server
	 *                          framework.
	 * @param thePeriodStart       the start of the gaps through period
	 * @param thePeriodEnd         the end of the gaps through period
	 * @param theTopic             the category of the measures that is of interest for
	 *                          the care gaps report
	 * @param theSubject           a reference to either a Patient or Group for which
	 *                          the gaps in care report(s) will be generated
	 * @param thePractitioner      a reference to a Practitioner for which the gaps in
	 *                          care report(s) will be generated
	 * @param theOrganization      a reference to an Organization for which the gaps in
	 *                          care report(s) will be generated
	 * @param theStatus            the status code of gaps in care reports that will be
	 *                          included in the result
	 * @param theMeasureId         the id of Measure(s) for which the gaps in care
	 *                          report(s) will be calculated
	 * @param theMeasureIdentifier the identifier of Measure(s) for which the gaps in
	 *                          care report(s) will be calculated
	 * @param theMeasureUrl        the canonical URL of Measure(s) for which the gaps
	 *                          in care report(s) will be calculated
	 * @param theProgram           the program that a provider (either clinician or
	 *                          clinical organization) participates in
	 * @return Parameters of bundles of Care Gap Measure Reports
	 */
	@Description(
			shortDefinition = "$care-gaps operation",
			value =
					"Implements the <a href=\"http://build.fhir.org/ig/HL7/davinci-deqm/OperationDefinition-care-gaps.html\">$care-gaps</a> operation found in the <a href=\"http://build.fhir.org/ig/HL7/davinci-deqm/index.html\">Da Vinci DEQM FHIR Implementation Guide</a> which is an extension of the <a href=\"http://build.fhir.org/operation-measure-care-gaps.html\">$care-gaps</a> operation found in the <a href=\"http://hl7.org/fhir/R4/clinicalreasoning-module.html\">FHIR Clinical Reasoning Module</a>.")
	@Operation(name = ProviderConstants.CR_OPERATION_CARE_GAPS, idempotent = true, type = Measure.class)
	public Parameters careGapsReport(
			RequestDetails theRequestDetails,
			@OperationParam(name = "periodStart", typeName = "date") IPrimitiveType<Date> thePeriodStart,
			@OperationParam(name = "periodEnd", typeName = "date") IPrimitiveType<Date> thePeriodEnd,
			@OperationParam(name = "topic") List<String> theTopic,
			@OperationParam(name = "subject") String theSubject,
			@OperationParam(name = "practitioner") String thePractitioner,
			@OperationParam(name = "organization") String theOrganization,
			@OperationParam(name = "status") List<String> theStatus,
			@OperationParam(name = "measureId") List<String> theMeasureId,
			@OperationParam(name = "measureIdentifier") List<String> theMeasureIdentifier,
			@OperationParam(name = "measureUrl") List<CanonicalType> theMeasureUrl,
			@OperationParam(name = "program") List<String> theProgram) {

		return myR4CareGapsProcessorFactory
				.create(theRequestDetails)
				.getCareGapsReport(
						thePeriodStart,
						thePeriodEnd,
						theTopic,
						theSubject,
						thePractitioner,
						theOrganization,
						theStatus,
						theMeasureId,
						theMeasureIdentifier,
						theMeasureUrl,
						theProgram);
	}
}
