package ca.uhn.fhir.cr.r4.measure;

import ca.uhn.fhir.cr.common.HapiFhirRetrieveProvider;
import ca.uhn.fhir.cr.common.Searches;
import ca.uhn.fhir.cr.config.CrProperties;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import com.google.common.base.Strings;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.Measure;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.opencds.cqf.cql.evaluator.fhir.util.r4.Parameters.parameters;
import static org.opencds.cqf.cql.evaluator.fhir.util.r4.Parameters.part;

public class CareGapsProvider extends HapiFhirRetrieveProvider {
	private CrProperties crProperties;

	public CareGapsProvider(CrProperties crProperties){
		super();
		this.crProperties = crProperties;
	}

	@Operation(name = "$care-gaps", idempotent = true, type = Measure.class)
	public Parameters careGapsReport(
		RequestDetails theRequestDetails,
		@OperationParam(name = "periodStart", typeName = "date") IPrimitiveType<Date> periodStart,
		@OperationParam(name = "periodEnd", typeName = "date") IPrimitiveType<Date> periodEnd,
		@OperationParam(name = "topic") List<String> topic,
		@OperationParam(name = "subject") String subject,
		@OperationParam(name = "practitioner") String practitioner,
		@OperationParam(name = "organization") String organization,
		@OperationParam(name = "status") List<String> status,
		@OperationParam(name = "measureId") List<String> measureId,
		@OperationParam(name = "measureIdentifier") List<String> measureIdentifier,
		@OperationParam(name = "measureUrl") List<CanonicalType> measureUrl,
		@OperationParam(name = "program") List<String> program) {

		try {
			validateConfiguration(theRequestDetails);
		} catch (Exception e) {
			return parameters(part("Invalid configuration", generateIssue("error", e.getMessage())));
		}
		try {
			validateParameters(theRequestDetails);
		} catch (Exception e) {
			return parameters(part("Invalid parameters", generateIssue("error", e.getMessage())));
		}

		// TODO: filter by topic.
		// TODO: filter by program.
		List<Measure> measures = ensureMeasures(getMeasures(measureId, measureIdentifier, measureUrl, theRequestDetails));

		List<Patient> patients;
		if (!Strings.isNullOrEmpty(subject)) {
			patients = getPatientListFromSubject(subject);
		} else {
			// TODO: implement non subject parameters (practitioner and organization)
			return parameters(part("Unsupported configuration",
				generateIssue("error", "Non subject parameters have not been implemented.")));
		}

		ensureSupplementalDataElementSearchParameter(theRequestDetails);
		List<CompletableFuture<Parameters.ParametersParameterComponent>> futures = new ArrayList<>();
		Parameters result = initializeResult();
		if (crProperties.getThreadedCareGapsEnabled()) {
			(patients)
				.forEach(
					patient -> futures.add(CompletableFuture.supplyAsync(() -> patientReports(theRequestDetails,
						periodStart.getValueAsString(), periodEnd.getValueAsString(), patient, status, measures,
						organization), cqlExecutor)));

			futures.forEach(x -> result.addParameter(x.join()));
		} else {
			(patients).forEach(
				patient -> {
					Parameters.ParametersParameterComponent patientParameter = patientReports(theRequestDetails,
						periodStart.getValueAsString(), periodEnd.getValueAsString(), patient, status, measures,
						organization);
					if (patientParameter != null) {
						result.addParameter(patientParameter);
					}
				});
		}
		return result;
	}

	public void validateConfiguration(RequestDetails theRequestDetails) {
		checkNotNull(crProperties.getMeasure(), "The measure setting is required for the $care-gaps operation.");
		checkNotNull(crProperties.getMeasure().getMeasureReport(),
			"The measure_report setting is required for the $care-gaps operation.");
		checkArgument(!Strings.isNullOrEmpty(crProperties.getMeasure().getMeasureReport().getReporter()),
			"The measure_report.care_gaps_reporter setting is required for the $care-gaps operation.");
		checkArgument(!Strings.isNullOrEmpty(crProperties.getMeasure().getMeasureReport().getCompositionAuthor()),
			"The measure_report.care_gaps_composition_section_author setting is required for the $care-gaps operation.");

		Resource configuredReporter = putConfiguredResource(Organization.class,
			crProperties.getMeasure().getMeasureReport().getReporter(), "care_gaps_reporter", theRequestDetails);
		Resource configuredAuthor = putConfiguredResource(Organization.class,
			crProperties.getMeasure().getMeasureReport().getCompositionAuthor(), "care_gaps_composition_section_author",
			theRequestDetails);

		checkNotNull(configuredReporter, String.format(
			"The %s Resource is configured as the measure_report.care_gaps_reporter but the Resource could not be read.",
			crProperties.getMeasure().getMeasureReport().getReporter()));
		checkNotNull(configuredAuthor, String.format(
			"The %s Resource is configured as the measure_report.care_gaps_composition_section_author but the Resource could not be read.",
			crProperties.getMeasure().getMeasureReport().getCompositionAuthor()));
	}

	private <T extends Resource> T putConfiguredResource(Class<T> theResourceClass, String theId, String theKey,
																		  RequestDetails theRequestDetails) {
		T resource = search(theResourceClass, Searches.byId(theId), theRequestDetails).firstOrNull();
		if (resource != null) {
			configuredResources.put(theKey, resource);
		}
		return resource;
	}
}
