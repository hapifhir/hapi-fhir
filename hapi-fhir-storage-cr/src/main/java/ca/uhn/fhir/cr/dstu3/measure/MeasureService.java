package ca.uhn.fhir.cr.dstu3.measure;

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

import ca.uhn.fhir.cr.common.IDataProviderFactory;
import ca.uhn.fhir.cr.common.IFhirDalFactory;
import ca.uhn.fhir.cr.common.ILibrarySourceProviderFactory;
import ca.uhn.fhir.cr.common.ITerminologyProviderFactory;
import ca.uhn.fhir.cr.dstu3.ISupplementalDataSearchParamUser;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.IPagingProvider;
import org.cqframework.cql.cql2elm.LibrarySourceProvider;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Endpoint;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Measure;
import org.hl7.fhir.dstu3.model.MeasureReport;
import org.hl7.fhir.dstu3.model.StringType;
import org.opencds.cqf.cql.engine.data.DataProvider;
import org.opencds.cqf.cql.engine.fhir.terminology.Dstu3FhirTerminologyProvider;
import org.opencds.cqf.cql.engine.terminology.TerminologyProvider;
import org.opencds.cqf.cql.evaluator.CqlOptions;
import org.opencds.cqf.cql.evaluator.fhir.dal.FhirDal;
import org.opencds.cqf.cql.evaluator.fhir.util.Clients;
import org.opencds.cqf.cql.evaluator.measure.MeasureEvaluationOptions;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class MeasureService implements ISupplementalDataSearchParamUser {

	@Autowired
	protected IPagingProvider myPagingProvider;

	@Autowired
	protected ITerminologyProviderFactory myTerminologyProviderFactory;

	@Autowired
	protected IDataProviderFactory myCqlDataProviderFactory;

	@Autowired
	protected org.opencds.cqf.cql.evaluator.builder.DataProviderFactory myDataProviderFactory;

	@Autowired
	protected ILibrarySourceProviderFactory myLibraryContentProviderFactory;

	@Autowired
	protected IFhirDalFactory myFhirDalFactory;

	@Autowired
	protected Map<org.cqframework.cql.elm.execution.VersionedIdentifier, org.cqframework.cql.elm.execution.Library> myGlobalLibraryCache;

	@Autowired
	protected CqlOptions myCqlOptions;

	@Autowired
	protected MeasureEvaluationOptions myMeasureEvaluationOptions;

	@Autowired
	protected DaoRegistry myDaoRegistry;

	protected RequestDetails myRequestDetails;
	/**
	 * Get The details (such as tenant) of this request. Usually auto-populated HAPI.
	 *
	 * @return RequestDetails
	 */
	public RequestDetails getRequestDetails() {
		return this.myRequestDetails;
	}

	public void setRequestDetails(RequestDetails theRequestDetails) {
		this.myRequestDetails = theRequestDetails;
	}

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
	 * @param thePractitioner        the practitioner to use for the evaluation
	 * @param theLastReceivedOn      the date the results of this measure were last
	 *                               received.
	 * @param theProductLine         the productLine (e.g. Medicare, Medicaid, etc) to use
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

		ensureSupplementalDataElementSearchParameter(myRequestDetails);

		Measure measure = read(theId, myRequestDetails);

		TerminologyProvider terminologyProvider;

		if (theTerminologyEndpoint != null) {
			IGenericClient client = Clients.forEndpoint(getFhirContext(), theTerminologyEndpoint);
			terminologyProvider = new Dstu3FhirTerminologyProvider(client);
		} else {
			terminologyProvider = this.myTerminologyProviderFactory.create(myRequestDetails);
		}

		DataProvider dataProvider = this.myCqlDataProviderFactory.create(myRequestDetails, terminologyProvider);
		LibrarySourceProvider libraryContentProvider = this.myLibraryContentProviderFactory.create(myRequestDetails);
		FhirDal fhirDal = this.myFhirDalFactory.create(myRequestDetails);

		var measureProcessor = new org.opencds.cqf.cql.evaluator.measure.dstu3.Dstu3MeasureProcessor(
			null, this.myDataProviderFactory, null, null, null, terminologyProvider, libraryContentProvider, dataProvider,
			fhirDal, myMeasureEvaluationOptions, myCqlOptions,
			this.myGlobalLibraryCache);

		MeasureReport report = measureProcessor.evaluateMeasure(measure.getUrl(), thePeriodStart, thePeriodEnd, theReportType,
			theSubject, null, theLastReceivedOn, null, null, null, theAdditionalData);

		if (theProductLine != null) {
			Extension ext = new Extension();
			ext.setUrl("http://hl7.org/fhir/us/cqframework/cqfmeasures/StructureDefinition/cqfm-productLine");
			ext.setValue(new StringType(theProductLine));
			report.addExtension(ext);
		}

		return report;
	}

	@Override
	public DaoRegistry getDaoRegistry() {
		return this.myDaoRegistry;
	}

	@Override
	public IPagingProvider getPagingProvider() {
		return this.myPagingProvider;
	}

}
