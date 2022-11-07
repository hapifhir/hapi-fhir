package ca.uhn.fhir.cr.dstu3;

import ca.uhn.fhir.cr.common.JpaDataProviderFactory;
import ca.uhn.fhir.cr.common.JpaFhirDalFactory;
import ca.uhn.fhir.cr.common.JpaLibrarySourceProviderFactory;
import ca.uhn.fhir.cr.common.JpaTerminologyProviderFactory;
import ca.uhn.fhir.cr.common.behavior.r4.MeasureReportUser;
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
import org.opencds.cqf.cql.evaluator.builder.DataProviderFactory;
import org.opencds.cqf.cql.evaluator.fhir.dal.FhirDal;
import org.opencds.cqf.cql.evaluator.measure.MeasureEvaluationOptions;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class MeasureService implements MeasureReportUser {

	@Autowired
	private JpaTerminologyProviderFactory jpaTerminologyProviderFactory;

	@Autowired
	private JpaDataProviderFactory jpaDataProviderFactory;

	@Autowired
	private DataProviderFactory dataProviderFactory;

	@Autowired
	private JpaLibrarySourceProviderFactory libraryContentProviderFactory;

	@Autowired
	private JpaFhirDalFactory fhirDalFactory;

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

	public RequestDetails getRequestDetails() {
		return this.requestDetails;
	}

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
			terminologyProvider = this.jpaTerminologyProviderFactory.create(requestDetails);
		}

		DataProvider dataProvider = this.jpaDataProviderFactory.create(requestDetails, terminologyProvider);
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
