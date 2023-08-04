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

import ca.uhn.fhir.cr.common.IDaoRegistryUser;
import ca.uhn.fhir.cr.common.Searches;
import ca.uhn.fhir.cr.config.CareGapsProperties;
import ca.uhn.fhir.cr.enumeration.CareGapsStatusCode;
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
import org.springframework.beans.factory.annotation.Autowired;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;

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

public class CareGapsService implements IDaoRegistryUser {

	private static final Logger ourLog = LoggerFactory.getLogger(CareGapsService.class);
	public static final Map<String, CodeableConceptSettings> CARE_GAPS_CODES = ofEntries(
			new AbstractMap.SimpleEntry<>(
					"http://loinc.org/96315-7",
					new CodeableConceptSettings().add("http://loinc.org", "96315-7", "Gaps in care report")),
			new AbstractMap.SimpleEntry<>(
					"http://terminology.hl7.org/CodeSystem/v3-ActCode/CAREGAP",
					new CodeableConceptSettings()
							.add("http://terminology.hl7.org/CodeSystem/v3-ActCode", "CAREGAP", "Care Gaps")));

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

	@Autowired
	protected CareGapsProperties myCareGapsProperties;

	@Autowired
	Function<RequestDetails, MeasureService> myR4MeasureServiceFactory;

	@Autowired
	protected Executor myCqlExecutor;

	@Autowired
	protected DaoRegistry myDaoRegistry;

	private final Map<String, Resource> myConfiguredResources = new HashMap<>();

	/**
	 * Calculate measures describing gaps in care
	 * @param thePeriodStart
	 * @param thePeriodEnd
	 * @param theTopic
	 * @param theSubject
	 * @param thePractitioner
	 * @param theOrganization
	 * @param theStatuses
	 * @param theMeasureIds
	 * @param theMeasureIdentifiers
	 * @param theMeasureUrls
	 * @param thePrograms
	 * @return Parameters that includes zero to many document bundles that
	 * include Care Gap Measure Reports will be returned.
	 */
	public Parameters getCareGapsReport(
			IPrimitiveType<Date> thePeriodStart,
			IPrimitiveType<Date> thePeriodEnd,
			List<String> theTopic,
			String theSubject,
			String thePractitioner,
			String theOrganization,
			List<String> theStatuses,
			List<String> theMeasureIds,
			List<String> theMeasureIdentifiers,
			List<CanonicalType> theMeasureUrls,
			List<String> thePrograms) {

		validateConfiguration();

		List<Measure> measures =
				ensureMeasures(getMeasures(theMeasureIds, theMeasureIdentifiers, theMeasureUrls, myRequestDetails));

		List<Patient> patients;
		if (!Strings.isNullOrEmpty(theSubject)) {
			patients = getPatientListFromSubject(theSubject);
		} else {
			throw new NotImplementedOperationException(
					Msg.code(2275) + "Only the subject parameter has been implemented.");
		}

		List<CompletableFuture<Parameters.ParametersParameterComponent>> futures = new ArrayList<>();
		Parameters result = initializeResult();
		if (myCareGapsProperties.getThreadedCareGapsEnabled()) {
			patients.forEach(patient -> {
				Parameters.ParametersParameterComponent patientReports = patientReports(
						myRequestDetails,
						thePeriodStart.getValueAsString(),
						thePeriodEnd.getValueAsString(),
						patient,
						theStatuses,
						measures,
						theOrganization);
				futures.add(CompletableFuture.supplyAsync(() -> patientReports, myCqlExecutor));
			});

			futures.forEach(x -> result.addParameter(x.join()));
		} else {
			patients.forEach(patient -> {
				Parameters.ParametersParameterComponent patientReports = patientReports(
						myRequestDetails,
						thePeriodStart.getValueAsString(),
						thePeriodEnd.getValueAsString(),
						patient,
						theStatuses,
						measures,
						theOrganization);
				if (patientReports != null) {
					result.addParameter(patientReports);
				}
			});
		}
		return result;
	}

	public void validateConfiguration() {
		checkArgument(
				!Strings.isNullOrEmpty(myCareGapsProperties.getCareGapsReporter()),
				"The measure_report.care_gaps_reporter setting is required for the $care-gaps operation.");
		checkArgument(
				!Strings.isNullOrEmpty(myCareGapsProperties.getCareGapsCompositionSectionAuthor()),
				"The measure_report.care_gaps_composition_section_author setting is required for the $care-gaps operation.");

		Resource configuredReporter = addConfiguredResource(
				Organization.class, myCareGapsProperties.getCareGapsReporter(), "care_gaps_reporter");
		Resource configuredAuthor = addConfiguredResource(
				Organization.class,
				myCareGapsProperties.getCareGapsCompositionSectionAuthor(),
				"care_gaps_composition_section_author");

		checkNotNull(
				configuredReporter,
				String.format(
						"The %s Resource is configured as the measure_report.care_gaps_reporter but the Resource could not be read.",
						myCareGapsProperties.getCareGapsReporter()));
		checkNotNull(
				configuredAuthor,
				String.format(
						"The %s Resource is configured as the measure_report.care_gaps_composition_section_author but the Resource could not be read.",
						myCareGapsProperties.getCareGapsCompositionSectionAuthor()));
	}

	List<Patient> getPatientListFromSubject(String theSubject) {
		if (theSubject.startsWith("Patient/")) {
			return Collections.singletonList(validatePatientExists(theSubject));
		} else if (theSubject.startsWith("Group/")) {
			return getPatientListFromGroup(theSubject);
		}

		ourLog.info("Subject member was not a Patient or a Group, so skipping. \n{}", theSubject);
		return Collections.emptyList();
	}

	List<Patient> getPatientListFromGroup(String theSubjectGroupId) {
		List<Patient> patientList = new ArrayList<>();

		Group group = read(newId(theSubjectGroupId));
		if (group == null) {
			throw new IllegalArgumentException(Msg.code(2276) + "Could not find Group: " + theSubjectGroupId);
		}

		group.getMember().forEach(member -> {
			Reference reference = member.getEntity();
			if (reference.getReferenceElement().getResourceType().equals("Patient")) {
				Patient patient = validatePatientExists(reference.getReference());
				patientList.add(patient);
			} else if (reference.getReferenceElement().getResourceType().equals("Group")) {
				patientList.addAll(getPatientListFromGroup(reference.getReference()));
			} else {
				ourLog.info("Group member was not a Patient or a Group, so skipping. \n{}", reference.getReference());
			}
		});

		return patientList;
	}

	Patient validatePatientExists(String thePatientRef) {
		Patient patient = read(newId(thePatientRef));
		if (patient == null) {
			throw new IllegalArgumentException(Msg.code(2277) + "Could not find Patient: " + thePatientRef);
		}

		return patient;
	}

	List<Measure> getMeasures(
			List<String> theMeasureIds,
			List<String> theMeasureIdentifiers,
			List<CanonicalType> theMeasureCanonicals,
			RequestDetails theRequestDetails) {
		boolean hasMeasureIds = theMeasureIds != null && !theMeasureIds.isEmpty();
		boolean hasMeasureIdentifiers = theMeasureIdentifiers != null && !theMeasureIdentifiers.isEmpty();
		boolean hasMeasureUrls = theMeasureCanonicals != null && !theMeasureCanonicals.isEmpty();
		if (!hasMeasureIds && !hasMeasureIdentifiers && !hasMeasureUrls) {
			return Collections.emptyList();
		}

		List<Measure> measureList = new ArrayList<>();
		Iterable<IBaseResource> measureSearchResults;
		if (hasMeasureIds) {
			measureSearchResults = search(Measure.class, Searches.byIds(theMeasureIds), theRequestDetails);
			populateMeasures(measureList, measureSearchResults);
		}

		if (hasMeasureUrls) {
			measureSearchResults =
					search(Measure.class, Searches.byCanonicals(theMeasureCanonicals), theRequestDetails);
			populateMeasures(measureList, measureSearchResults);
		}

		// TODO: implement searching by measure identifiers
		if (hasMeasureIdentifiers) {
			throw new NotImplementedOperationException(
					Msg.code(2278) + "Measure identifiers have not yet been implemented.");
		}

		Map<String, Measure> result = new HashMap<>();
		measureList.forEach(measure -> result.putIfAbsent(measure.getUrl(), measure));

		return new ArrayList<>(result.values());
	}

	private void populateMeasures(List<Measure> measureList, Iterable<IBaseResource> measureSearchResults) {
		if (measureSearchResults != null) {
			Iterator<IBaseResource> measures = measureSearchResults.iterator();
			while (measures.hasNext()) {
				measureList.add((Measure) measures.next());
			}
		}
	}

	private <T extends Resource> T addConfiguredResource(Class<T> theResourceClass, String theId, String theKey) {
		// T resource = repo.search(theResourceClass, Searches.byId(theId)).firstOrNull();
		Iterable<IBaseResource> resourceResult = search(theResourceClass, Searches.byId(theId), myRequestDetails);
		T resource = null;
		if (resourceResult != null) {
			Iterator<IBaseResource> resources = resourceResult.iterator();
			while (resources.hasNext()) {
				resource = (T) resources.next();
				break;
			}
			if (resource != null) {
				myConfiguredResources.put(theKey, resource);
			}
		}
		return resource;
	}

	private List<Measure> ensureMeasures(List<Measure> theMeasures) {
		theMeasures.forEach(measure -> {
			if (!measure.hasScoring()) {
				ourLog.info("Measure does not specify a scoring so skipping: {}.", measure.getId());
				theMeasures.remove(measure);
			}
			if (!measure.hasImprovementNotation()) {
				ourLog.info("Measure does not specify an improvement notation so skipping: {}.", measure.getId());
				theMeasures.remove(measure);
			}
		});
		return theMeasures;
	}

	private Parameters.ParametersParameterComponent patientReports(
			RequestDetails theRequestDetails,
			String thePeriodStart,
			String thePeriodEnd,
			Patient thePatient,
			List<String> theStatuses,
			List<Measure> theMeasures,
			String theOrganization) {
		// TODO: add organization to report, if it exists.
		Composition composition = getComposition(thePatient);
		List<DetectedIssue> detectedIssues = new ArrayList<>();
		Map<String, Resource> evalPlusSDE = new HashMap<>();
		List<MeasureReport> reports = getReports(
				theRequestDetails,
				thePeriodStart,
				thePeriodEnd,
				thePatient,
				theStatuses,
				theMeasures,
				composition,
				detectedIssues,
				evalPlusSDE);

		if (reports.isEmpty()) {
			return null;
		}

		return initializePatientParameter(thePatient)
				.setResource(addBundleEntries(
						theRequestDetails.getFhirServerBase(), composition, detectedIssues, reports, evalPlusSDE));
	}

	private List<MeasureReport> getReports(
			RequestDetails theRequestDetails,
			String thePeriodStart,
			String thePeriodEnd,
			Patient thePatient,
			List<String> theStatuses,
			List<Measure> theMeasures,
			Composition theComposition,
			List<DetectedIssue> theDetectedIssues,
			Map<String, Resource> theEvalPlusSDEs) {
		List<MeasureReport> reports = new ArrayList<>();
		MeasureReport report;
		for (Measure measure : theMeasures) {
			report = myR4MeasureServiceFactory
					.apply(myRequestDetails)
					.evaluateMeasure(
							measure.getIdElement(),
							thePeriodStart,
							thePeriodEnd,
							"patient",
							Ids.simple(thePatient),
							null,
							null,
							null,
							null,
							null);
			if (!report.hasGroup()) {
				ourLog.info(
						"Report does not include a group so skipping.\nSubject: {}\nMeasure: {}",
						Ids.simple(thePatient),
						Ids.simplePart(measure));
				continue;
			}

			initializeReport(report);

			CareGapsStatusCode gapStatus = getGapStatus(measure, report);
			if (!theStatuses.contains(gapStatus.toString())) {
				continue;
			}

			DetectedIssue detectedIssue = getDetectedIssue(thePatient, report, gapStatus);
			theDetectedIssues.add(detectedIssue);
			theComposition.addSection(getSection(measure, report, detectedIssue, gapStatus));
			populateEvaluatedResources(report, theEvalPlusSDEs);
			populateSDEResources(report, theEvalPlusSDEs);
			reports.add(report);
		}

		return reports;
	}

	private void initializeReport(MeasureReport theMeasureReport) {
		if (Strings.isNullOrEmpty(theMeasureReport.getId())) {
			IIdType id = Ids.newId(MeasureReport.class, UUID.randomUUID().toString());
			theMeasureReport.setId(id);
		}
		Reference reporter = new Reference().setReference(myCareGapsProperties.getCareGapsReporter());
		// TODO: figure out what this extension is for
		// reporter.addExtension(new
		// Extension().setUrl(CARE_GAPS_MEASUREREPORT_REPORTER_EXTENSION));
		theMeasureReport.setReporter(reporter);
		if (theMeasureReport.hasMeta()) {
			theMeasureReport.getMeta().addProfile(CARE_GAPS_REPORT_PROFILE);
		} else {
			theMeasureReport.setMeta(new Meta().addProfile(CARE_GAPS_REPORT_PROFILE));
		}
	}

	private Parameters.ParametersParameterComponent initializePatientParameter(Patient thePatient) {
		Parameters.ParametersParameterComponent patientParameter = Resources.newBackboneElement(
						Parameters.ParametersParameterComponent.class)
				.setName("return");
		patientParameter.setId("subject-" + Ids.simplePart(thePatient));
		return patientParameter;
	}

	private Bundle addBundleEntries(
			String theServerBase,
			Composition theComposition,
			List<DetectedIssue> theDetectedIssues,
			List<MeasureReport> theMeasureReports,
			Map<String, Resource> theEvalPlusSDEs) {
		Bundle reportBundle = getBundle();
		reportBundle.addEntry(getBundleEntry(theServerBase, theComposition));
		theMeasureReports.forEach(report -> reportBundle.addEntry(getBundleEntry(theServerBase, report)));
		theDetectedIssues.forEach(detectedIssue -> reportBundle.addEntry(getBundleEntry(theServerBase, detectedIssue)));
		myConfiguredResources
				.values()
				.forEach(resource -> reportBundle.addEntry(getBundleEntry(theServerBase, resource)));
		theEvalPlusSDEs.values().forEach(resource -> reportBundle.addEntry(getBundleEntry(theServerBase, resource)));
		return reportBundle;
	}

	private CareGapsStatusCode getGapStatus(Measure theMeasure, MeasureReport theMeasureReport) {
		Pair<String, Boolean> inNumerator = new MutablePair<>("numerator", false);
		theMeasureReport.getGroup().forEach(group -> group.getPopulation().forEach(population -> {
			if (population.hasCode()
					&& population.getCode().hasCoding(MEASUREREPORT_MEASURE_POPULATION_SYSTEM, inNumerator.getKey())
					&& population.getCount() == 1) {
				inNumerator.setValue(true);
			}
		}));

		boolean isPositive =
				theMeasure.getImprovementNotation().hasCoding(MEASUREREPORT_IMPROVEMENT_NOTATION_SYSTEM, "increase");

		if ((isPositive && !inNumerator.getValue()) || (!isPositive && inNumerator.getValue())) {
			return CareGapsStatusCode.OPEN_GAP;
		}

		return CareGapsStatusCode.CLOSED_GAP;
	}

	private Bundle.BundleEntryComponent getBundleEntry(String theServerBase, Resource theResource) {
		return new Bundle.BundleEntryComponent()
				.setResource(theResource)
				.setFullUrl(getFullUrl(theServerBase, theResource));
	}

	private Composition.SectionComponent getSection(
			Measure theMeasure,
			MeasureReport theMeasureReport,
			DetectedIssue theDetectedIssue,
			CareGapsStatusCode theGapStatus) {
		String narrative = String.format(
				HTML_DIV_PARAGRAPH_CONTENT,
				theGapStatus == CareGapsStatusCode.CLOSED_GAP
						? "No detected issues."
						: String.format("Issues detected.  See %s for details.", Ids.simple(theDetectedIssue)));
		return new CompositionSectionComponentBuilder<>(Composition.SectionComponent.class)
				.withTitle(theMeasure.hasTitle() ? theMeasure.getTitle() : theMeasure.getUrl())
				.withFocus(Ids.simple(theMeasureReport))
				.withText(new NarrativeSettings(narrative))
				.withEntry(Ids.simple(theDetectedIssue))
				.build();
	}

	private Bundle getBundle() {
		return new BundleBuilder<>(Bundle.class)
				.withProfile(CARE_GAPS_BUNDLE_PROFILE)
				.withType(Bundle.BundleType.DOCUMENT.toString())
				.build();
	}

	private Composition getComposition(Patient thePatient) {
		return new CompositionBuilder<>(Composition.class)
				.withProfile(CARE_GAPS_COMPOSITION_PROFILE)
				.withType(CARE_GAPS_CODES.get("http://loinc.org/96315-7"))
				.withStatus(Composition.CompositionStatus.FINAL.toString())
				.withTitle("Care Gap Report for " + Ids.simplePart(thePatient))
				.withSubject(Ids.simple(thePatient))
				.withAuthor(Ids.simple(myConfiguredResources.get("care_gaps_composition_section_author")))
				// .withCustodian(organization) // TODO: Optional: identifies the organization
				// who is responsible for ongoing maintenance of and accessing to this gaps in
				// care report. Add as a setting and optionally read if it's there.
				.build();
	}

	private DetectedIssue getDetectedIssue(
			Patient thePatient, MeasureReport theMeasureReport, CareGapsStatusCode theCareGapStatusCode) {
		return new DetectedIssueBuilder<>(DetectedIssue.class)
				.withProfile(CARE_GAPS_DETECTED_ISSUE_PROFILE)
				.withStatus(DetectedIssue.DetectedIssueStatus.FINAL.toString())
				.withCode(CARE_GAPS_CODES.get("http://terminology.hl7.org/CodeSystem/v3-ActCode/CAREGAP"))
				.withPatient(Ids.simple(thePatient))
				.withEvidenceDetail(Ids.simple(theMeasureReport))
				.withModifierExtension(new ImmutablePair<>(
						CARE_GAPS_GAP_STATUS_EXTENSION,
						new CodeableConceptSettings()
								.add(
										CARE_GAPS_GAP_STATUS_SYSTEM,
										theCareGapStatusCode.toString(),
										theCareGapStatusCode.toDisplayString())))
				.build();
	}

	protected void populateEvaluatedResources(MeasureReport theMeasureReport, Map<String, Resource> theResources) {
		theMeasureReport.getEvaluatedResource().forEach(evaluatedResource -> {
			IIdType resourceId = evaluatedResource.getReferenceElement();
			if (resourceId.getResourceType() == null || theResources.containsKey(Ids.simple(resourceId))) {
				return;
			}
			IBaseResource resourceBase = this.read(resourceId);
			if (resourceBase instanceof Resource) {
				Resource resource = (Resource) resourceBase;
				theResources.put(Ids.simple(resourceId), resource);
			}
		});
	}

	protected void populateSDEResources(MeasureReport theMeasureReport, Map<String, Resource> theResources) {
		if (theMeasureReport.hasExtension()) {
			for (Extension extension : theMeasureReport.getExtension()) {
				if (extension.hasUrl() && extension.getUrl().equals(MEASUREREPORT_MEASURE_SUPPLEMENTALDATA_EXTENSION)) {
					Reference sdeRef = extension.hasValue() && extension.getValue() instanceof Reference
							? (Reference) extension.getValue()
							: null;
					if (sdeRef != null
							&& sdeRef.hasReference()
							&& !sdeRef.getReference().startsWith("#")) {
						IdType sdeId = new IdType(sdeRef.getReference());
						if (!theResources.containsKey(Ids.simple(sdeId))) {
							theResources.put(Ids.simple(sdeId), read(sdeId));
						}
					}
				}
			}
		}
	}

	private Parameters initializeResult() {
		return newResource(Parameters.class, "care-gaps-report-" + UUID.randomUUID());
	}

	public static String getFullUrl(String theServerAddress, IBaseResource theResource) {
		checkArgument(
				theResource.getIdElement().hasIdPart(),
				"Cannot generate a fullUrl because the resource does not have an id.");
		return getFullUrl(theServerAddress, theResource.fhirType(), Ids.simplePart(theResource));
	}

	public static String getFullUrl(String theServerAddress, String theFhirType, String theElementId) {
		return String.format(
				"%s%s/%s", theServerAddress + (theServerAddress.endsWith("/") ? "" : "/"), theFhirType, theElementId);
	}

	@Override
	public DaoRegistry getDaoRegistry() {
		return myDaoRegistry;
	}

	public CareGapsProperties getCrProperties() {
		return myCareGapsProperties;
	}
}
