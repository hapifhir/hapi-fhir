package ca.uhn.fhir.cr.r4.measure;

import ca.uhn.fhir.cr.enumeration.CareGapsStatusCode;
import ca.uhn.fhir.cr.common.IDaoRegistryUser;
import ca.uhn.fhir.cr.common.Searches;
import ca.uhn.fhir.cr.config.CrProperties;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;
import com.google.common.base.Strings;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.DetectedIssue;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Group;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Measure;
import org.hl7.fhir.r4.model.MeasureReport;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.opencds.cqf.cql.evaluator.fhir.builder.BundleBuilder;
import org.opencds.cqf.cql.evaluator.fhir.builder.CodeableConceptSettings;
import org.opencds.cqf.cql.evaluator.fhir.builder.CompositionBuilder;
import org.opencds.cqf.cql.evaluator.fhir.builder.CompositionSectionComponentBuilder;
import org.opencds.cqf.cql.evaluator.fhir.builder.DetectedIssueBuilder;
import org.opencds.cqf.cql.evaluator.fhir.builder.NarrativeSettings;
import org.opencds.cqf.cql.evaluator.fhir.util.Ids;
import org.opencds.cqf.cql.evaluator.fhir.util.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static ca.uhn.fhir.cr.constant.CareCapsConstants.CARE_GAPS_BUNDLE_PROFILE;
import static ca.uhn.fhir.cr.constant.CareCapsConstants.CARE_GAPS_COMPOSITION_PROFILE;
import static ca.uhn.fhir.cr.constant.CareCapsConstants.CARE_GAPS_DETECTED_ISSUE_PROFILE;
import static ca.uhn.fhir.cr.constant.CareCapsConstants.CARE_GAPS_GAP_STATUS_EXTENSION;
import static ca.uhn.fhir.cr.constant.CareCapsConstants.CARE_GAPS_GAP_STATUS_SYSTEM;
import static ca.uhn.fhir.cr.constant.CareCapsConstants.CARE_GAPS_REPORT_PROFILE;
import static ca.uhn.fhir.cr.constant.HtmlConstants.HTML_DIV_PARAGRAPH_CONTENT;
import static ca.uhn.fhir.cr.constant.MeasureReportConstants.MEASUREREPORT_IMPROVEMENT_NOTATION_SYSTEM;
import static ca.uhn.fhir.cr.constant.MeasureReportConstants.MEASUREREPORT_MEASURE_POPULATION_SYSTEM;
import static ca.uhn.fhir.cr.constant.MeasureReportConstants.MEASUREREPORT_MEASURE_SUPPLEMENTALDATA_EXTENSION;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Map.ofEntries;
import static org.hl7.fhir.r4.model.Factory.newId;
import static org.opencds.cqf.cql.evaluator.fhir.util.Resources.newResource;

@Component
public class CareGapsService implements IDaoRegistryUser {

	private static final Logger ourLog = LoggerFactory.getLogger(CareGapsService.class);
	public static final Map<String, CodeableConceptSettings> CARE_GAPS_CODES = ofEntries(
		new AbstractMap.SimpleEntry<>("http://loinc.org/96315-7",
			new CodeableConceptSettings().add(
				"http://loinc.org", "96315-7", "Gaps in care report")),
		new AbstractMap.SimpleEntry<>("http://terminology.hl7.org/CodeSystem/v3-ActCode/CAREGAP",
			new CodeableConceptSettings().add(
				"http://terminology.hl7.org/CodeSystem/v3-ActCode", "CAREGAP", "Care Gaps")));

	private RequestDetails myRequestDetails;

	private CrProperties myCrProperties;

	private MeasureService myR4MeasureService;

	private Executor myCqlExecutor;

	private DaoRegistry myDaoRegistry;

	private final Map<String, Resource> configuredResources = new HashMap<>();

	public CareGapsService(CrProperties crProperties,
								  MeasureService measureService,
								  DaoRegistry daoRegistry,
								  Executor executor,
								  RequestDetails theRequestDetails){
		this.myDaoRegistry = daoRegistry;
		this.myCrProperties = crProperties;
		this.myR4MeasureService = measureService;
		this.myCqlExecutor = executor;
		this.myRequestDetails = theRequestDetails;
	}

	public Parameters getCareGapsReport(IPrimitiveType<Date> periodStart,
													IPrimitiveType<Date> periodEnd,
													List<String> topic,
													String subject,
													String practitioner,
													String organization,
													List<String> status,
													List<String> measureId,
													List<String> measureIdentifier,
													List<CanonicalType> measureUrl,
													List<String> program) {

		validateConfiguration();

		List<Measure> measures = ensureMeasures(getMeasures(measureId, measureIdentifier, measureUrl, myRequestDetails));

		List<Patient> patients;
		if (!Strings.isNullOrEmpty(subject)) {
			patients = getPatientListFromSubject(subject);
		} else {
			throw new NotImplementedOperationException(Msg.code(2275) + "Only the subject parameter has been implemented.");
		}

		List<CompletableFuture<Parameters.ParametersParameterComponent>> futures = new ArrayList<>();
		Parameters result = initializeResult();
		if (myCrProperties.getMeasure().getThreadedCareGapsEnabled()) {
			(patients)
				.forEach(
					patient -> futures.add(CompletableFuture.supplyAsync(() -> patientReports(myRequestDetails,
						periodStart.getValueAsString(), periodEnd.getValueAsString(), patient, status, measures,
						organization), myCqlExecutor)));

			futures.forEach(x -> result.addParameter(x.join()));
		} else {
			(patients).forEach(
				patient -> {
					Parameters.ParametersParameterComponent patientParameter = patientReports(myRequestDetails,
						periodStart.getValueAsString(), periodEnd.getValueAsString(), patient, status, measures,
						organization);
					if (patientParameter != null) {
						result.addParameter(patientParameter);
					}
				});
		}
		return result;
	}

	public void validateConfiguration() {
		checkNotNull(myCrProperties.getMeasure(),
			"The measure_report setting properties are required for the $care-gaps operation.");
		checkNotNull(myCrProperties.getMeasure().getMeasureReport(),
			"The measure_report setting is required for the $care-gaps operation.");
		checkArgument(!Strings.isNullOrEmpty(myCrProperties.getMeasure().getMeasureReport().getReporter()),
			"The measure_report.care_gaps_reporter setting is required for the $care-gaps operation.");
		checkArgument(!Strings.isNullOrEmpty(myCrProperties.getMeasure().getMeasureReport().getCompositionAuthor()),
			"The measure_report.care_gaps_composition_section_author setting is required for the $care-gaps operation.");

		Resource configuredReporter = putConfiguredResource(Organization.class,
			myCrProperties.getMeasure().getMeasureReport().getReporter(), "care_gaps_reporter");
		Resource configuredAuthor = putConfiguredResource(Organization.class,
			myCrProperties.getMeasure().getMeasureReport().getCompositionAuthor(),
								"care_gaps_composition_section_author");

		checkNotNull(configuredReporter, String.format(
			"The %s Resource is configured as the measure_report.care_gaps_reporter but the Resource could not be read.",
			myCrProperties.getMeasure().getMeasureReport().getReporter()));
		checkNotNull(configuredAuthor, String.format(
			"The %s Resource is configured as the measure_report.care_gaps_composition_section_author but the Resource could not be read.",
			myCrProperties.getMeasure().getMeasureReport().getCompositionAuthor()));
	}
	List<Patient> getPatientListFromSubject(String subject) {
		if (subject.startsWith("Patient/")) {
			return Collections.singletonList(ensurePatient(subject));
		} else if (subject.startsWith("Group/")) {
			return getPatientListFromGroup(subject);
		}

		ourLog.info("Subject member was not a Patient or a Group, so skipping. \n{}", subject);
		return Collections.emptyList();
	}

	List<Patient> getPatientListFromGroup(String subjectGroupId) {
		List<Patient> patientList = new ArrayList<>();

		Group group = read(newId(subjectGroupId));
		if (group == null) {
			throw new IllegalArgumentException(Msg.code(2276) + "Could not find Group: " + subjectGroupId);
		}

		group.getMember().forEach(member -> {
			Reference reference = member.getEntity();
			if (reference.getReferenceElement().getResourceType().equals("Patient")) {
				Patient patient = ensurePatient(reference.getReference());
				patientList.add(patient);
			} else if (reference.getReferenceElement().getResourceType().equals("Group")) {
				patientList.addAll(getPatientListFromGroup(reference.getReference()));
			} else {
				ourLog.info("Group member was not a Patient or a Group, so skipping. \n{}", reference.getReference());
			}
		});

		return patientList;
	}

	Patient ensurePatient(String patientRef) {
		Patient patient = read(newId(patientRef));
		if (patient == null) {
			throw new IllegalArgumentException(Msg.code(2277) + "Could not find Patient: " + patientRef);
		}

		return patient;
	}

	List<Measure> getMeasures(List<String> measureIds, List<String> measureIdentifiers,
									  List<CanonicalType> measureCanonicals, RequestDetails theRequestDetails) {
		boolean hasMeasureIds = measureIds != null && !measureIds.isEmpty();
		boolean hasMeasureIdentifiers = measureIdentifiers != null && !measureIdentifiers.isEmpty();
		boolean hasMeasureUrls = measureCanonicals != null && !measureCanonicals.isEmpty();
		if (!hasMeasureIds && !hasMeasureIdentifiers && !hasMeasureUrls) {
			return Collections.emptyList();
		}

		List<Measure> measureList = new ArrayList<>();

		if (hasMeasureIds) {
			measureList
				.addAll(search(Measure.class, Searches.byIds(measureIds), theRequestDetails)
					.getAllResourcesTyped());
		}

		// TODO: implement searching by measure identifiers
		if (hasMeasureIdentifiers) {
			throw new NotImplementedOperationException(Msg.code(2278) + "Measure identifiers have not yet been implemented.");
		}

		if (hasMeasureUrls) {
			measureList.addAll(search(Measure.class, Searches.byCanonicals(measureCanonicals), theRequestDetails)
				.getAllResourcesTyped());
		}

		Map<String, Measure> result = new HashMap<>();
		measureList.forEach(measure -> result.putIfAbsent(measure.getUrl(), measure));

		return new ArrayList<>(result.values());
	}

	private <T extends Resource> T putConfiguredResource(Class<T> theResourceClass, String theId, String theKey) {
		//T resource = repo.search(theResourceClass, Searches.byId(theId)).firstOrNull();
		T resource = search(theResourceClass, Searches.byId(theId), myRequestDetails).firstOrNull();
		if (resource != null) {
			configuredResources.put(theKey, resource);
		}
		return resource;
	}

	private List<Measure> ensureMeasures(List<Measure> measures) {
		measures.forEach(measure -> {
			if (!measure.hasScoring()) {
				ourLog.info("Measure does not specify a scoring so skipping: {}.", measure.getId());
				measures.remove(measure);
			}
			if (!measure.hasImprovementNotation()) {
				ourLog.info("Measure does not specify an improvement notation so skipping: {}.", measure.getId());
				measures.remove(measure);
			}
		});
		return measures;
	}

	private Parameters.ParametersParameterComponent patientReports(RequestDetails requestDetails, String periodStart,
																						String periodEnd, Patient patient, List<String> status, List<Measure> measures, String organization) {
		// TODO: add organization to report, if it exists.
		Composition composition = getComposition(patient);
		List<DetectedIssue> detectedIssues = new ArrayList<>();
		Map<String, Resource> evalPlusSDE = new HashMap<>();
		List<MeasureReport> reports = getReports(requestDetails, periodStart, periodEnd, patient, status, measures,
			composition, detectedIssues, evalPlusSDE);

		if (reports.isEmpty()) {
			return null;
		}

		return initializePatientParameter(patient).setResource(
			addBundleEntries(requestDetails.getFhirServerBase(), composition, detectedIssues, reports, evalPlusSDE));
	}

	private List<MeasureReport> getReports(RequestDetails requestDetails, String periodStart, String periodEnd,
														Patient patient, List<String> status, List<Measure> measures, Composition composition,
														List<DetectedIssue> detectedIssues, Map<String, Resource> evalPlusSDE) {
		List<MeasureReport> reports = new ArrayList<>();
		MeasureReport report;
		for (Measure measure : measures) {
			report = myR4MeasureService.evaluateMeasure(measure.getIdElement(), periodStart,
				periodEnd, "patient", Ids.simple(patient), null, null, null, null, null);
			if (!report.hasGroup()) {
				ourLog.info("Report does not include a group so skipping.\nSubject: {}\nMeasure: {}",
					Ids.simple(patient),
					Ids.simplePart(measure));
				continue;
			}

			initializeReport(report);

			CareGapsStatusCode gapStatus = getGapStatus(measure, report);
			if (!status.contains(gapStatus.toString())) {
				continue;
			}

			DetectedIssue detectedIssue = getDetectedIssue(patient, report, gapStatus);
			detectedIssues.add(detectedIssue);
			composition.addSection(getSection(measure, report, detectedIssue, gapStatus));
			populateEvaluatedResources(report, evalPlusSDE);
			populateSDEResources(report, evalPlusSDE);
			reports.add(report);
		}

		return reports;
	}

	private void initializeReport(MeasureReport report) {
		if (Strings.isNullOrEmpty(report.getId())) {
			IIdType id = Ids.newId(MeasureReport.class, UUID.randomUUID().toString());
			report.setId(id);
		}
		Reference reporter = new Reference().setReference(myCrProperties.getMeasure().getMeasureReport().getReporter());
		// TODO: figure out what this extension is for
		// reporter.addExtension(new
		// Extension().setUrl(CARE_GAPS_MEASUREREPORT_REPORTER_EXTENSION));
		report.setReporter(reporter);
		if (report.hasMeta()) {
			report.getMeta().addProfile(CARE_GAPS_REPORT_PROFILE);
		} else {
			report.setMeta(new Meta().addProfile(CARE_GAPS_REPORT_PROFILE));
		}
	}

	private Parameters.ParametersParameterComponent initializePatientParameter(Patient patient) {
		Parameters.ParametersParameterComponent patientParameter = Resources
			.newBackboneElement(Parameters.ParametersParameterComponent.class)
			.setName("return");
		patientParameter.setId("subject-" + Ids.simplePart(patient));
		return patientParameter;
	}

	private Bundle addBundleEntries(String serverBase, Composition composition, List<DetectedIssue> detectedIssues,
											  List<MeasureReport> reports, Map<String, Resource> evalPlusSDE) {
		Bundle reportBundle = getBundle();
		reportBundle.addEntry(getBundleEntry(serverBase, composition));
		reports.forEach(report -> reportBundle.addEntry(getBundleEntry(serverBase, report)));
		detectedIssues.forEach(detectedIssue -> reportBundle.addEntry(getBundleEntry(serverBase, detectedIssue)));
		configuredResources.values().forEach(resource -> reportBundle.addEntry(getBundleEntry(serverBase, resource)));
		evalPlusSDE.values().forEach(resource -> reportBundle.addEntry(getBundleEntry(serverBase, resource)));
		return reportBundle;
	}

	private CareGapsStatusCode getGapStatus(Measure measure, MeasureReport report) {
		Pair<String, Boolean> inNumerator = new MutablePair<>("numerator", false);
		report.getGroup().forEach(group -> group.getPopulation().forEach(population -> {
			if (population.hasCode()
				&& population.getCode().hasCoding(MEASUREREPORT_MEASURE_POPULATION_SYSTEM, inNumerator.getKey())
				&& population.getCount() == 1) {
				inNumerator.setValue(true);
			}
		}));

		boolean isPositive = measure.getImprovementNotation().hasCoding(MEASUREREPORT_IMPROVEMENT_NOTATION_SYSTEM,
			"increase");

		if ((isPositive && !inNumerator.getValue()) || (!isPositive && inNumerator.getValue())) {
			return CareGapsStatusCode.OPEN_GAP;
		}

		return CareGapsStatusCode.CLOSED_GAP;
	}

	private Bundle.BundleEntryComponent getBundleEntry(String serverBase, Resource resource) {
		return new Bundle.BundleEntryComponent().setResource(resource)
			.setFullUrl(getFullUrl(serverBase, resource));
	}

	private Composition.SectionComponent getSection(Measure measure, MeasureReport report, DetectedIssue detectedIssue,
																	CareGapsStatusCode gapStatus) {
		String narrative = String.format(HTML_DIV_PARAGRAPH_CONTENT,
			gapStatus == CareGapsStatusCode.CLOSED_GAP ? "No detected issues."
				: String.format("Issues detected.  See %s for details.", Ids.simple(detectedIssue)));
		return new CompositionSectionComponentBuilder<>(Composition.SectionComponent.class)
			.withTitle(measure.hasTitle() ? measure.getTitle() : measure.getUrl())
			.withFocus(Ids.simple(report))
			.withText(new NarrativeSettings(narrative))
			.withEntry(Ids.simple(detectedIssue))
			.build();
	}

	private Bundle getBundle() {
		return new BundleBuilder<>(Bundle.class)
			.withProfile(CARE_GAPS_BUNDLE_PROFILE)
			.withType(Bundle.BundleType.DOCUMENT.toString())
			.build();
	}

	private Composition getComposition(Patient patient) {
		return new CompositionBuilder<>(Composition.class)
			.withProfile(CARE_GAPS_COMPOSITION_PROFILE)
			.withType(CARE_GAPS_CODES.get("http://loinc.org/96315-7"))
			.withStatus(Composition.CompositionStatus.FINAL.toString())
			.withTitle("Care Gap Report for " + Ids.simplePart(patient))
			.withSubject(Ids.simple(patient))
			.withAuthor(Ids.simple(configuredResources.get("care_gaps_composition_section_author")))
			// .withCustodian(organization) // TODO: Optional: identifies the organization
			// who is responsible for ongoing maintenance of and accessing to this gaps in
			// care report. Add as a setting and optionally read if it's there.
			.build();
	}

	private DetectedIssue getDetectedIssue(Patient patient, MeasureReport report, CareGapsStatusCode gapStatus) {
		return new DetectedIssueBuilder<>(DetectedIssue.class)
			.withProfile(CARE_GAPS_DETECTED_ISSUE_PROFILE)
			.withStatus(DetectedIssue.DetectedIssueStatus.FINAL.toString())
			.withCode(CARE_GAPS_CODES.get("http://terminology.hl7.org/CodeSystem/v3-ActCode/CAREGAP"))
			.withPatient(Ids.simple(patient))
			.withEvidenceDetail(Ids.simple(report))
			.withModifierExtension(new ImmutablePair<>(
				CARE_GAPS_GAP_STATUS_EXTENSION,
				new CodeableConceptSettings().add(CARE_GAPS_GAP_STATUS_SYSTEM, gapStatus.toString(),
					gapStatus.toDisplayString())))
			.build();
	}

	protected void populateEvaluatedResources(MeasureReport report, Map<String, Resource> resources) {
		report.getEvaluatedResource().forEach(evaluatedResource -> {
			IIdType resourceId = evaluatedResource.getReferenceElement();
			if (resourceId.getResourceType() == null || resources.containsKey(Ids.simple(resourceId))) {
				return;
			}
			IBaseResource resourceBase = this.read(resourceId);
			if (resourceBase instanceof Resource) {
				Resource resource = (Resource) resourceBase;
				resources.put(Ids.simple(resourceId), resource);
			}
		});
	}

	protected void populateSDEResources(MeasureReport report, Map<String, Resource> resources) {
		if (report.hasExtension()) {
			for (Extension extension : report.getExtension()) {
				if (extension.hasUrl() && extension.getUrl().equals(MEASUREREPORT_MEASURE_SUPPLEMENTALDATA_EXTENSION)) {
					Reference sdeRef = extension.hasValue() && extension.getValue() instanceof Reference
						? (Reference) extension.getValue()
						: null;
					if (sdeRef != null && sdeRef.hasReference() && !sdeRef.getReference().startsWith("#")) {
						IdType sdeId = new IdType(sdeRef.getReference());
						if (!resources.containsKey(Ids.simple(sdeId))) {
							resources.put(Ids.simple(sdeId), read(sdeId));
						}
					}
				}
			}
		}
	}
	private Parameters initializeResult() {
		return newResource(Parameters.class, "care-gaps-report-" + UUID.randomUUID());
	}

	public static String getFullUrl(String serverAddress, IBaseResource resource) {
		checkArgument(resource.getIdElement().hasIdPart(),
			"Cannot generate a fullUrl because the resource does not have an id.");
		return getFullUrl(serverAddress, resource.fhirType(), Ids.simplePart(resource));
	}

	public static String getFullUrl(String serverAddress, String fhirType, String elementId) {
		return String.format("%s%s/%s", serverAddress + (serverAddress.endsWith("/") ? "" : "/"), fhirType,
			elementId);
	}

	@Override
	public DaoRegistry getDaoRegistry() {
		return myDaoRegistry;
	}
}
