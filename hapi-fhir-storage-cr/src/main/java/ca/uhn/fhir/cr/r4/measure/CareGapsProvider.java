package ca.uhn.fhir.cr.r4.measure;

import ca.uhn.fhir.cr.common.Operations;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Measure;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

import static ca.uhn.fhir.cr.r4.measure.CareGapsService.CARE_GAPS_STATUS;
import static org.opencds.cqf.cql.evaluator.fhir.util.r4.Parameters.parameters;
import static org.opencds.cqf.cql.evaluator.fhir.util.r4.Parameters.part;

@Component
public class CareGapsProvider {
	private static final Logger ourLog = LoggerFactory.getLogger(CareGapsProvider.class);

	Function<RequestDetails, CareGapsService> myCareGapsServiceFunction;

	public CareGapsProvider(Function<RequestDetails, CareGapsService> careGapsServiceFunction) {
		this.myCareGapsServiceFunction = careGapsServiceFunction;
	}

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
	 * @param periodStart       the start of the gaps through period
	 * @param periodEnd         the end of the gaps through period
	 * @param topic             the category of the measures that is of interest for
	 *                          the care gaps report
	 * @param subject           a reference to either a Patient or Group for which
	 *                          the gaps in care report(s) will be generated
	 * @param practitioner      a reference to a Practitioner for which the gaps in
	 *                          care report(s) will be generated
	 * @param organization      a reference to an Organization for which the gaps in
	 *                          care report(s) will be generated
	 * @param status            the status code of gaps in care reports that will be
	 *                          included in the result
	 * @param measureId         the id of Measure(s) for which the gaps in care
	 *                          report(s) will be calculated
	 * @param measureIdentifier the identifier of Measure(s) for which the gaps in
	 *                          care report(s) will be calculated
	 * @param measureUrl        the canonical URL of Measure(s) for which the gaps
	 *                          in care report(s) will be calculated
	 * @param program           the program that a provider (either clinician or
	 *                          clinical organization) participates in
	 * @return Parameters of bundles of Care Gap Measure Reports
	 */
	@Description(shortDefinition = "$care-gaps operation", value = "Implements the <a href=\"http://build.fhir.org/ig/HL7/davinci-deqm/OperationDefinition-care-gaps.html\">$care-gaps</a> operation found in the <a href=\"http://build.fhir.org/ig/HL7/davinci-deqm/index.html\">Da Vinci DEQM FHIR Implementation Guide</a> which is an extension of the <a href=\"http://build.fhir.org/operation-measure-care-gaps.html\">$care-gaps</a> operation found in the <a href=\"http://hl7.org/fhir/R4/clinicalreasoning-module.html\">FHIR Clinical Reasoning Module</a>.")
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
			validateParameters(theRequestDetails);
		} catch (Exception e) {
			return parameters(part("Invalid parameters", generateIssue("error", e.getMessage())));
		}
		return myCareGapsServiceFunction
			.apply(theRequestDetails)
					.getCareGapsReport(
						periodStart,
						periodEnd,
						topic,
						subject,
						practitioner,
						organization,
						status,
						measureId,
						measureIdentifier,
						measureUrl,
						program
					);
	}

	OperationOutcome generateIssue(String severity, String issue) {
		OperationOutcome error = new OperationOutcome();
		error.addIssue()
			.setSeverity(OperationOutcome.IssueSeverity.fromCode(severity))
			.setCode(OperationOutcome.IssueType.PROCESSING)
			.setDetails(new CodeableConcept().setText(issue));
		return error;
	}

	public void validateParameters(RequestDetails theRequestDetails) {
		Operations.validatePeriod(theRequestDetails, "periodStart", "periodEnd");
		Operations.validateCardinality(theRequestDetails, "subject", 0, 1);
		Operations.validateSingularPattern(theRequestDetails, "subject", Operations.PATIENT_OR_GROUP_REFERENCE);
		Operations.validateCardinality(theRequestDetails, "status", 1);
		Operations.validateSingularPattern(theRequestDetails, "status", CARE_GAPS_STATUS);
		Operations.validateExclusive(theRequestDetails, "subject", "organization", "practitioner");
		Operations.validateExclusive(theRequestDetails, "organization", "subject");
		Operations.validateInclusive(theRequestDetails, "practitioner", "organization");
		Operations.validateExclusiveOr(theRequestDetails, "subject", "organization");
		Operations.validateAtLeastOne(theRequestDetails, "measureId", "measureIdentifier", "measureUrl");
	}
}
