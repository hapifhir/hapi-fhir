package ca.uhn.fhir.cr.dstu3.service;

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

import ca.uhn.fhir.cr.common.DataProviderFactory;
import ca.uhn.fhir.cr.common.FhirDalFactory;
import ca.uhn.fhir.cr.common.LibrarySourceProviderFactory;
import ca.uhn.fhir.cr.common.TerminologyProviderFactory;
import ca.uhn.fhir.cr.common.behavior.dstu3.MeasureReportUser;
import ca.uhn.fhir.cr.common.utility.Clients;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.cqframework.cql.cql2elm.LibrarySourceProvider;
import org.hl7.fhir.dstu3.model.*;
import org.opencds.cqf.cql.engine.data.DataProvider;
import org.opencds.cqf.cql.engine.fhir.terminology.Dstu3FhirTerminologyProvider;
import org.opencds.cqf.cql.engine.terminology.TerminologyProvider;
import org.opencds.cqf.cql.evaluator.CqlOptions;
import org.opencds.cqf.cql.evaluator.fhir.dal.FhirDal;
import org.opencds.cqf.cql.evaluator.measure.MeasureEvaluationOptions;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class MeasureService implements MeasureReportUser {

	@Autowired
	private TerminologyProviderFactory terminologyProviderFactory;

	@Autowired
	private DataProviderFactory cqlDataProviderFactory;

	@Autowired
	private org.opencds.cqf.cql.evaluator.builder.DataProviderFactory dataProviderFactory;

	@Autowired
	private LibrarySourceProviderFactory libraryContentProviderFactory;

	@Autowired
	private FhirDalFactory fhirDalFactory;

	@Autowired
	private Map<org.cqframework.cql.elm.execution.VersionedIdentifier, org.cqframework.cql.elm.execution.Library> globalLibraryCache;

	@Autowired
	private CqlOptions cqlOptions;

	@Autowired
	private MeasureEvaluationOptions measureEvaluationOptions;

	@Autowired
	private DaoRegistry daoRegistry;

	private RequestDetails requestDetails;

	public void setRequestDetails(RequestDetails requestDetails) {
		this.requestDetails = requestDetails;
	}

	/**
	 * Get The details (such as tenant) of this request. Usually auto-populated HAPI.
	 * @return RequestDetails
	 */
	public RequestDetails getRequestDetails() {
		return this.requestDetails;
	}
	/**
	 * Implements the <a href=
	 * "https://www.hl7.org/fhir/operation-measure-evaluate-measure.html">$evaluate-measure</a>
	 * operation found in the
	 * <a href="http://www.hl7.org/fhir/clinicalreasoning-module.html">FHIR Clinical
	 * Reasoning Module</a>. This implementation aims to be compatible with the CQF
	 * IG.
	 *
	 * @param theId          the Id of the Measure to evaluate
	 * @param periodStart    The start of the reporting period
	 * @param periodEnd      The end of the reporting period
	 * @param reportType     The type of MeasureReport to generate
	 * @param practitioner   the practitioner to use for the evaluation
	 * @param lastReceivedOn the date the results of this measure were last
	 *                       received.
	 * @param productLine    the productLine (e.g. Medicare, Medicaid, etc) to use
	 *                       for the evaluation. This is a non-standard parameter.
	 * @param additionalData the data bundle containing additional data
	 * @param terminologyEndpoint the endpoint of terminology services for your measure valuesets
	 * @return the calculated MeasureReport
	 */
	public MeasureReport evaluateMeasure(IdType theId,
													 String periodStart,
													 String periodEnd,
													 String reportType,
													 String subject,
													 String practitioner,
													 String lastReceivedOn,
													 String productLine,
													 Bundle additionalData,
													 Endpoint terminologyEndpoint) {

		ensureSupplementalDataElementSearchParameter(requestDetails);

		Measure measure = read(theId);

		TerminologyProvider terminologyProvider;

		if (terminologyEndpoint != null) {
			IGenericClient client = Clients.forEndpoint(getFhirContext(), terminologyEndpoint);
			terminologyProvider = new Dstu3FhirTerminologyProvider(client);
		} else {
			terminologyProvider = this.terminologyProviderFactory.create(requestDetails);
		}

		DataProvider dataProvider = this.cqlDataProviderFactory.create(requestDetails, terminologyProvider);
		LibrarySourceProvider libraryContentProvider = this.libraryContentProviderFactory.create(requestDetails);
		FhirDal fhirDal = this.fhirDalFactory.create(requestDetails);

		var measureProcessor = new org.opencds.cqf.cql.evaluator.measure.dstu3.Dstu3MeasureProcessor(
			null, this.dataProviderFactory, null, null, null, terminologyProvider, libraryContentProvider, dataProvider,
			fhirDal, measureEvaluationOptions, cqlOptions,
			this.globalLibraryCache);

		MeasureReport report = measureProcessor.evaluateMeasure(measure.getUrl(), periodStart, periodEnd, reportType,
			subject, null, lastReceivedOn, null, null, null, additionalData);

		if (productLine != null) {
			Extension ext = new Extension();
			ext.setUrl("http://hl7.org/fhir/us/cqframework/cqfmeasures/StructureDefinition/cqfm-productLine");
			ext.setValue(new StringType(productLine));
			report.addExtension(ext);
		}

		return report;
	}

	@Override
	public DaoRegistry getDaoRegistry() {
		return this.daoRegistry;
	}

}
