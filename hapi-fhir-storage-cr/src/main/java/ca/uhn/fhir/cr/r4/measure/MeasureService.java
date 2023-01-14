package ca.uhn.fhir.cr.r4.measure;

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
import ca.uhn.fhir.cr.common.SupplementalDataConstants;
import ca.uhn.fhir.cr.r4.ISupplementalDataSearchParamUser;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.param.ReferenceParam;
import org.apache.commons.lang3.StringUtils;
import org.cqframework.cql.cql2elm.LibrarySourceProvider;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Endpoint;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Measure;
import org.hl7.fhir.r4.model.MeasureReport;
import org.hl7.fhir.r4.model.StringType;
import org.opencds.cqf.cql.engine.data.DataProvider;
import org.opencds.cqf.cql.engine.fhir.terminology.R4FhirTerminologyProvider;
import org.opencds.cqf.cql.engine.terminology.TerminologyProvider;
import org.opencds.cqf.cql.evaluator.CqlOptions;
import org.opencds.cqf.cql.evaluator.fhir.dal.FhirDal;
import org.opencds.cqf.cql.evaluator.fhir.util.Clients;
import org.opencds.cqf.cql.evaluator.measure.MeasureEvaluationOptions;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MeasureService implements ISupplementalDataSearchParamUser {

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

	public RequestDetails getRequestDetails() {
		return this.myRequestDetails;
	}

	/**
	 * Get The details (such as tenant) of this request. Usually auto-populated HAPI.
	 *
	 * @return RequestDetails
	 */
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

		ensureSupplementalDataElementSearchParameter(myRequestDetails);

		Measure measure = read(theId, myRequestDetails);

		TerminologyProvider terminologyProvider;

		if (theTerminologyEndpoint != null) {
			IGenericClient client = Clients.forEndpoint(getFhirContext(), theTerminologyEndpoint);
			terminologyProvider = new R4FhirTerminologyProvider(client);
		} else {
			terminologyProvider = this.myTerminologyProviderFactory.create(myRequestDetails);
		}

		DataProvider dataProvider = this.myCqlDataProviderFactory.create(myRequestDetails, terminologyProvider);
		LibrarySourceProvider libraryContentProvider = this.myLibraryContentProviderFactory.create(myRequestDetails);
		FhirDal fhirDal = this.myFhirDalFactory.create(myRequestDetails);

		org.opencds.cqf.cql.evaluator.measure.r4.R4MeasureProcessor measureProcessor = new org.opencds.cqf.cql.evaluator.measure.r4.R4MeasureProcessor(
			null, this.myDataProviderFactory, null, null, null, terminologyProvider, libraryContentProvider, dataProvider,
			fhirDal, myMeasureEvaluationOptions, myCqlOptions,
			this.myGlobalLibraryCache);

		MeasureReport measureReport = null;

		if (StringUtils.isBlank(theSubject) && StringUtils.isNotBlank(thePractitioner)) {
			List<String> subjectIds = getPractitionerPatients(thePractitioner, myRequestDetails);
			measureReport = measureProcessor.evaluateMeasure(measure.getUrl(), thePeriodStart, thePeriodEnd, theReportType,
				subjectIds, theLastReceivedOn, null, null, null, theAdditionalData);
		} else if (StringUtils.isNotBlank(theSubject)) {
			measureReport = measureProcessor.evaluateMeasure(measure.getUrl(), thePeriodStart, thePeriodEnd, theReportType,
				theSubject, null, theLastReceivedOn, null, null, null, theAdditionalData);
		} else if (StringUtils.isBlank(theSubject) && StringUtils.isBlank(thePractitioner)) {
			measureReport = measureProcessor.evaluateMeasure(measure.getUrl(), thePeriodStart, thePeriodEnd, theReportType,
				null, null, theLastReceivedOn, null, null, null, theAdditionalData);
		}

		addProductLineExtension(measureReport, theProductLine);

		return measureReport;
	}

	private List<String> getPractitionerPatients(String practitioner, RequestDetails theRequestDetails) {
		SearchParameterMap map = SearchParameterMap.newSynchronous();
		map.add("general-practitioner", new ReferenceParam(
			practitioner.startsWith("Practitioner/") ? practitioner : "Practitioner/" + practitioner));
		List<String> patients = new ArrayList<>();
		IBundleProvider patientProvider = myDaoRegistry.getResourceDao("Patient").search(map, theRequestDetails);
		List<IBaseResource> patientList = patientProvider.getAllResources();
		patientList.forEach(x -> patients.add(x.getIdElement().getResourceType() + "/" + x.getIdElement().getIdPart()));
		return patients;
	}

	private void addProductLineExtension(MeasureReport measureReport, String productLine) {
		if (productLine != null) {
			Extension ext = new Extension();
			ext.setUrl(SupplementalDataConstants.MEASUREREPORT_PRODUCT_LINE_EXT_URL);
			ext.setValue(new StringType(productLine));
			measureReport.addExtension(ext);
		}
	}

	@Override
	public DaoRegistry getDaoRegistry() {
		return this.myDaoRegistry;
	}

}
