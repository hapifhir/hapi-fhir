/*-
 * #%L
 * HAPI FHIR JPA Server - International Patient Summary (IPS)
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
package ca.uhn.fhir.jpa.ips.remote;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.ips.api.Section;
import ca.uhn.fhir.jpa.ips.strategy.section.SectionSearchStrategyCollection;
import ca.uhn.fhir.jpa.ips.strategy.AllergyIntoleranceNoInfoR4Generator;
import ca.uhn.fhir.jpa.ips.strategy.BaseIpsGenerationStrategy;
import ca.uhn.fhir.jpa.ips.strategy.MedicationNoInfoR4Generator;
import ca.uhn.fhir.jpa.ips.strategy.ProblemNoInfoR4Generator;
import ca.uhn.fhir.jpa.ips.strategy.section.AdvanceDirectivesSectionSearchStrategy;
import ca.uhn.fhir.jpa.ips.strategy.section.AllergyIntoleranceSectionSearchStrategy;
import ca.uhn.fhir.jpa.ips.strategy.section.DiagnosticResultsSectionSearchStrategyDiagnosticReport;
import ca.uhn.fhir.jpa.ips.strategy.section.DiagnosticResultsSectionSearchStrategyObservation;
import ca.uhn.fhir.jpa.ips.strategy.section.FunctionalStatusSectionSearchStrategy;
import ca.uhn.fhir.jpa.ips.strategy.section.IllnessHistorySectionSearchStrategy;
import ca.uhn.fhir.jpa.ips.strategy.section.ImmunizationsSectionSearchStrategy;
import ca.uhn.fhir.jpa.ips.strategy.section.MedicalDevicesSectionSearchStrategy;
import ca.uhn.fhir.jpa.ips.strategy.section.MedicationSummarySectionSearchStrategyMedicationAdministration;
import ca.uhn.fhir.jpa.ips.strategy.section.MedicationSummarySectionSearchStrategyMedicationDispense;
import ca.uhn.fhir.jpa.ips.strategy.section.MedicationSummarySectionSearchStrategyMedicationRequest;
import ca.uhn.fhir.jpa.ips.strategy.section.MedicationSummarySectionSearchStrategyMedicationStatement;
import ca.uhn.fhir.jpa.ips.strategy.section.PlanOfCareSectionSearchStrategy;
import ca.uhn.fhir.jpa.ips.strategy.section.PregnancySectionSearchStrategy;
import ca.uhn.fhir.jpa.ips.strategy.section.ProblemListSectionSearchStrategy;
import ca.uhn.fhir.jpa.ips.strategy.section.ProceduresSectionSearchStrategy;
import ca.uhn.fhir.jpa.ips.strategy.section.SocialHistorySectionSearchStrategy;
import ca.uhn.fhir.jpa.ips.strategy.section.VitalSignsSectionSearchStrategy;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.RequestFormatParamStyleEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.util.ValidateUtil;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.ClinicalImpression;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Consent;
import org.hl7.fhir.r4.model.DeviceUseStatement;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Immunization;
import org.hl7.fhir.r4.model.MedicationAdministration;
import org.hl7.fhir.r4.model.MedicationDispense;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.MedicationStatement;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Procedure;
import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.util.Validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * This {@link ca.uhn.fhir.jpa.ips.api.IIpsGenerationStrategy generation strategy} contains default rules for fetching
 * IPS section contents for each of the base (universal realm) IPS definition sections. It fetches contents for each
 * section from a remote FHIR Server.
 * <p>
 * This class can be used directly, but it can also be subclassed and extended if you want to
 * create an IPS strategy that is based on the defaults but add or change the inclusion rules or
 * sections. If you are subclassing this class, the typical approach is to override the
 * {@link #addSections()} method and replace it with your own implementation. You can include
 * any of the same sections that are defined in the parent class, but you can also omit any
 * you don't want to include, and add your own as well.
 * </p>
 */
public class DefaultRemoteIpsGenerationStrategy extends BaseIpsGenerationStrategy {

	public static final String SECTION_CODE_ALLERGY_INTOLERANCE = "48765-2";
	public static final String SECTION_CODE_MEDICATION_SUMMARY = "10160-0";
	public static final String SECTION_CODE_PROBLEM_LIST = "11450-4";
	public static final String SECTION_CODE_IMMUNIZATIONS = "11369-6";
	public static final String SECTION_CODE_PROCEDURES = "47519-4";
	public static final String SECTION_CODE_MEDICAL_DEVICES = "46264-8";
	public static final String SECTION_CODE_DIAGNOSTIC_RESULTS = "30954-2";
	public static final String SECTION_CODE_VITAL_SIGNS = "8716-3";
	public static final String SECTION_CODE_PREGNANCY = "10162-6";
	public static final String SECTION_CODE_SOCIAL_HISTORY = "29762-2";
	public static final String SECTION_CODE_ILLNESS_HISTORY = "11348-0";
	public static final String SECTION_CODE_FUNCTIONAL_STATUS = "47420-5";
	public static final String SECTION_CODE_PLAN_OF_CARE = "18776-5";
	public static final String SECTION_CODE_ADVANCE_DIRECTIVES = "42348-3";
	public static final String SECTION_SYSTEM_LOINC = ITermLoaderSvc.LOINC_URI;
	private final List<Function<Section, Section>> myGlobalSectionCustomizers = new ArrayList<>();

	private String myRemoteUrl;

	private IGenericClient myClient;

	@Autowired
	private FhirContext myFhirContext;

	private boolean myInitialized;

	public void setRemoteUrl(String theRemoteUrl) {
		myRemoteUrl = theRemoteUrl;
	}

	public void setFhirContext(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	protected IGenericClient getClient() {
		IGenericClient client = myFhirContext.newRestfulGenericClient(myRemoteUrl);
		client.setEncoding(EncodingEnum.JSON);
		client.setFormatParamStyle(RequestFormatParamStyleEnum.NONE);
		return client;
	}

	/**
	 * Subclasses may call this method to add customers that will customize every section
	 * added to the strategy.
	 */
	public void addGlobalSectionCustomizer(@Nonnull Function<Section, Section> theCustomizer) {
		Validate.isTrue(!myInitialized, "This method must not be called after the strategy is initialized");
		Validate.notNull(theCustomizer, "theCustomizer must not be null");
		myGlobalSectionCustomizers.add(theCustomizer);
	}

	@Override
	public final void initialize() {
		Validate.isTrue(!myInitialized, "Strategy must not be initialized twice");
		Validate.isTrue(myRemoteUrl != null, "No RemoteUrl has been supplied");
		myClient = getClient();
		Validate.isTrue(myFhirContext != null, "No FhirContext has been supplied");
		addSections();
		myInitialized = true;
	}

	@Nonnull
	@Override
	public IBaseResource fetchPatient(IIdType thePatientId, RequestDetails theRequestDetails) {
		return myClient.read().resource("Patient").withId(thePatientId).execute();
	}

	@Nonnull
	@Override
	public IBaseResource fetchPatient(TokenParam thePatientIdentifier, RequestDetails theRequestDetails) {
		Map<String, List<IQueryParameterType>> searchParameterMap = new HashMap<>();
		searchParameterMap.put(Patient.SP_IDENTIFIER, List.of(thePatientIdentifier));

		Bundle searchResults = myClient.search()
				.forResource("Patient")
				.where(searchParameterMap)
				.returnBundle(Bundle.class)
				.execute();

		ValidateUtil.isTrueOrThrowResourceNotFound(
				!searchResults.getEntry().isEmpty(), "No Patient could be found matching given identifier");
		ValidateUtil.isTrueOrThrowInvalidRequest(
				searchResults.getEntry().size() == 1,
				"Multiple Patient resources were found matching given identifier");

		return searchResults.getEntry().get(0).getResource();
	}

	/**
	 * Add the various sections to the registry in order. This method can be overridden for
	 * customization.
	 */
	protected void addSections() {
		addJpaSectionAllergyIntolerance();
		addJpaSectionMedicationSummary();
		addJpaSectionProblemList();
		addJpaSectionImmunizations();
		addJpaSectionProcedures();
		addJpaSectionMedicalDevices();
		addJpaSectionDiagnosticResults();
		addJpaSectionVitalSigns();
		addJpaSectionPregnancy();
		addJpaSectionSocialHistory();
		addJpaSectionIllnessHistory();
		addJpaSectionFunctionalStatus();
		addJpaSectionPlanOfCare();
		addJpaSectionAdvanceDirectives();
	}

	protected void addJpaSectionAllergyIntolerance() {
		Section section = Section.newBuilder()
				.withTitle("Allergies and Intolerances")
				.withSectionSystem(SECTION_SYSTEM_LOINC)
				.withSectionSystem(SECTION_SYSTEM_LOINC)
				.withSectionCode(SECTION_CODE_ALLERGY_INTOLERANCE)
				.withSectionDisplay("Allergies and adverse reactions Document")
				.withResourceType(AllergyIntolerance.class)
				.withProfile(
						"https://hl7.org/fhir/uv/ips/StructureDefinition-Composition-uv-ips-definitions.html#Composition.section:sectionAllergies")
				.withNoInfoGenerator(new AllergyIntoleranceNoInfoR4Generator())
				.build();

		SectionSearchStrategyCollection searchStrategyCollection = SectionSearchStrategyCollection.newBuilder()
				.addStrategy(AllergyIntolerance.class, new AllergyIntoleranceSectionSearchStrategy())
				.build();

		addCustomizedSection(section, searchStrategyCollection);
	}

	protected void addJpaSectionMedicationSummary() {
		Section section = Section.newBuilder()
				.withTitle("Medication List")
				.withSectionSystem(SECTION_SYSTEM_LOINC)
				.withSectionCode(SECTION_CODE_MEDICATION_SUMMARY)
				.withSectionDisplay("History of Medication use Narrative")
				.withResourceType(MedicationStatement.class)
				.withResourceType(MedicationRequest.class)
				.withResourceType(MedicationAdministration.class)
				.withResourceType(MedicationDispense.class)
				.withProfile(
						"https://hl7.org/fhir/uv/ips/StructureDefinition-Composition-uv-ips-definitions.html#Composition.section:sectionMedications")
				.withNoInfoGenerator(new MedicationNoInfoR4Generator())
				.build();

		SectionSearchStrategyCollection searchStrategyCollection = SectionSearchStrategyCollection.newBuilder()
				.addStrategy(
						MedicationAdministration.class,
						new MedicationSummarySectionSearchStrategyMedicationAdministration())
				.addStrategy(MedicationDispense.class, new MedicationSummarySectionSearchStrategyMedicationDispense())
				.addStrategy(MedicationRequest.class, new MedicationSummarySectionSearchStrategyMedicationRequest())
				.addStrategy(MedicationStatement.class, new MedicationSummarySectionSearchStrategyMedicationStatement())
				.build();

		addCustomizedSection(section, searchStrategyCollection);
	}

	protected void addJpaSectionProblemList() {
		Section section = Section.newBuilder()
				.withTitle("Problem List")
				.withSectionSystem(SECTION_SYSTEM_LOINC)
				.withSectionCode(SECTION_CODE_PROBLEM_LIST)
				.withSectionDisplay("Problem list - Reported")
				.withResourceType(Condition.class)
				.withProfile(
						"https://hl7.org/fhir/uv/ips/StructureDefinition-Composition-uv-ips-definitions.html#Composition.section:sectionProblems")
				.withNoInfoGenerator(new ProblemNoInfoR4Generator())
				.build();

		SectionSearchStrategyCollection searchStrategyCollection = SectionSearchStrategyCollection.newBuilder()
				.addStrategy(Condition.class, new ProblemListSectionSearchStrategy())
				.build();

		addCustomizedSection(section, searchStrategyCollection);
	}

	protected void addJpaSectionImmunizations() {
		Section section = Section.newBuilder()
				.withTitle("History of Immunizations")
				.withSectionSystem(SECTION_SYSTEM_LOINC)
				.withSectionCode(SECTION_CODE_IMMUNIZATIONS)
				.withSectionDisplay("History of Immunization Narrative")
				.withResourceType(Immunization.class)
				.withProfile(
						"https://hl7.org/fhir/uv/ips/StructureDefinition-Composition-uv-ips-definitions.html#Composition.section:sectionImmunizations")
				.build();

		SectionSearchStrategyCollection searchStrategyCollection = SectionSearchStrategyCollection.newBuilder()
				.addStrategy(Immunization.class, new ImmunizationsSectionSearchStrategy())
				.build();

		addCustomizedSection(section, searchStrategyCollection);
	}

	protected void addJpaSectionProcedures() {
		Section section = Section.newBuilder()
				.withTitle("History of Procedures")
				.withSectionSystem(SECTION_SYSTEM_LOINC)
				.withSectionCode(SECTION_CODE_PROCEDURES)
				.withSectionDisplay("History of Procedures Document")
				.withResourceType(Procedure.class)
				.withProfile(
						"https://hl7.org/fhir/uv/ips/StructureDefinition-Composition-uv-ips-definitions.html#Composition.section:sectionProceduresHx")
				.build();

		SectionSearchStrategyCollection searchStrategyCollection = SectionSearchStrategyCollection.newBuilder()
				.addStrategy(Procedure.class, new ProceduresSectionSearchStrategy())
				.build();

		addCustomizedSection(section, searchStrategyCollection);
	}

	protected void addJpaSectionMedicalDevices() {
		Section section = Section.newBuilder()
				.withTitle("Medical Devices")
				.withSectionSystem(SECTION_SYSTEM_LOINC)
				.withSectionCode(SECTION_CODE_MEDICAL_DEVICES)
				.withSectionDisplay("History of medical device use")
				.withResourceType(DeviceUseStatement.class)
				.withProfile(
						"https://hl7.org/fhir/uv/ips/StructureDefinition-Composition-uv-ips-definitions.html#Composition.section:sectionMedicalDevices")
				.build();

		SectionSearchStrategyCollection searchStrategyCollection = SectionSearchStrategyCollection.newBuilder()
				.addStrategy(DeviceUseStatement.class, new MedicalDevicesSectionSearchStrategy())
				.build();

		addCustomizedSection(section, searchStrategyCollection);
	}

	protected void addJpaSectionDiagnosticResults() {
		Section section = Section.newBuilder()
				.withTitle("Diagnostic Results")
				.withSectionSystem(SECTION_SYSTEM_LOINC)
				.withSectionCode(SECTION_CODE_DIAGNOSTIC_RESULTS)
				.withSectionDisplay("Relevant diagnostic tests/laboratory data Narrative")
				.withResourceType(DiagnosticReport.class)
				.withResourceType(Observation.class)
				.withProfile(
						"https://hl7.org/fhir/uv/ips/StructureDefinition-Composition-uv-ips-definitions.html#Composition.section:sectionResults")
				.build();

		SectionSearchStrategyCollection searchStrategyCollection = SectionSearchStrategyCollection.newBuilder()
				.addStrategy(DiagnosticReport.class, new DiagnosticResultsSectionSearchStrategyDiagnosticReport())
				.addStrategy(Observation.class, new DiagnosticResultsSectionSearchStrategyObservation())
				.build();

		addCustomizedSection(section, searchStrategyCollection);
	}

	protected void addJpaSectionVitalSigns() {
		Section section = Section.newBuilder()
				.withTitle("Vital Signs")
				.withSectionSystem(SECTION_SYSTEM_LOINC)
				.withSectionCode(SECTION_CODE_VITAL_SIGNS)
				.withSectionDisplay("Vital signs")
				.withResourceType(Observation.class)
				.withProfile(
						"https://hl7.org/fhir/uv/ips/StructureDefinition-Composition-uv-ips-definitions.html#Composition.section:sectionVitalSigns")
				.build();

		SectionSearchStrategyCollection searchStrategyCollection = SectionSearchStrategyCollection.newBuilder()
				.addStrategy(Observation.class, new VitalSignsSectionSearchStrategy())
				.build();

		addCustomizedSection(section, searchStrategyCollection);
	}

	protected void addJpaSectionPregnancy() {
		Section section = Section.newBuilder()
				.withTitle("Pregnancy Information")
				.withSectionSystem(SECTION_SYSTEM_LOINC)
				.withSectionCode(SECTION_CODE_PREGNANCY)
				.withSectionDisplay("History of pregnancies Narrative")
				.withResourceType(Observation.class)
				.withProfile(
						"https://hl7.org/fhir/uv/ips/StructureDefinition-Composition-uv-ips-definitions.html#Composition.section:sectionPregnancyHx")
				.build();

		SectionSearchStrategyCollection searchStrategyCollection = SectionSearchStrategyCollection.newBuilder()
				.addStrategy(Observation.class, new PregnancySectionSearchStrategy())
				.build();

		addCustomizedSection(section, searchStrategyCollection);
	}

	protected void addJpaSectionSocialHistory() {
		Section section = Section.newBuilder()
				.withTitle("Social History")
				.withSectionSystem(SECTION_SYSTEM_LOINC)
				.withSectionCode(SECTION_CODE_SOCIAL_HISTORY)
				.withSectionDisplay("Social history Narrative")
				.withResourceType(Observation.class)
				.withProfile(
						"https://hl7.org/fhir/uv/ips/StructureDefinition-Composition-uv-ips-definitions.html#Composition.section:sectionSocialHistory")
				.build();

		SectionSearchStrategyCollection searchStrategyCollection = SectionSearchStrategyCollection.newBuilder()
				.addStrategy(Observation.class, new SocialHistorySectionSearchStrategy())
				.build();

		addCustomizedSection(section, searchStrategyCollection);
	}

	protected void addJpaSectionIllnessHistory() {
		Section section = Section.newBuilder()
				.withTitle("History of Past Illness")
				.withSectionSystem(SECTION_SYSTEM_LOINC)
				.withSectionCode(SECTION_CODE_ILLNESS_HISTORY)
				.withSectionDisplay("History of Past illness Narrative")
				.withResourceType(Condition.class)
				.withProfile(
						"https://hl7.org/fhir/uv/ips/StructureDefinition-Composition-uv-ips-definitions.html#Composition.section:sectionPastIllnessHx")
				.build();

		SectionSearchStrategyCollection searchStrategyCollection = SectionSearchStrategyCollection.newBuilder()
				.addStrategy(Condition.class, new IllnessHistorySectionSearchStrategy())
				.build();

		addCustomizedSection(section, searchStrategyCollection);
	}

	protected void addJpaSectionFunctionalStatus() {
		Section section = Section.newBuilder()
				.withTitle("Functional Status")
				.withSectionSystem(SECTION_SYSTEM_LOINC)
				.withSectionCode(SECTION_CODE_FUNCTIONAL_STATUS)
				.withSectionDisplay("Functional status assessment note")
				.withResourceType(ClinicalImpression.class)
				.withProfile(
						"https://hl7.org/fhir/uv/ips/StructureDefinition-Composition-uv-ips-definitions.html#Composition.section:sectionFunctionalStatus")
				.build();

		SectionSearchStrategyCollection searchStrategyCollection = SectionSearchStrategyCollection.newBuilder()
				.addStrategy(ClinicalImpression.class, new FunctionalStatusSectionSearchStrategy())
				.build();

		addCustomizedSection(section, searchStrategyCollection);
	}

	protected void addJpaSectionPlanOfCare() {
		Section section = Section.newBuilder()
				.withTitle("Plan of Care")
				.withSectionSystem(SECTION_SYSTEM_LOINC)
				.withSectionCode(SECTION_CODE_PLAN_OF_CARE)
				.withSectionDisplay("Plan of care note")
				.withResourceType(CarePlan.class)
				.withProfile(
						"https://hl7.org/fhir/uv/ips/StructureDefinition-Composition-uv-ips-definitions.html#Composition.section:sectionPlanOfCare")
				.build();

		SectionSearchStrategyCollection searchStrategyCollection = SectionSearchStrategyCollection.newBuilder()
				.addStrategy(CarePlan.class, new PlanOfCareSectionSearchStrategy())
				.build();

		addCustomizedSection(section, searchStrategyCollection);
	}

	protected void addJpaSectionAdvanceDirectives() {
		Section section = Section.newBuilder()
				.withTitle("Advance Directives")
				.withSectionSystem(SECTION_SYSTEM_LOINC)
				.withSectionCode(SECTION_CODE_ADVANCE_DIRECTIVES)
				.withSectionDisplay("Advance directives")
				.withResourceType(Consent.class)
				.withProfile(
						"https://hl7.org/fhir/uv/ips/StructureDefinition-Composition-uv-ips-definitions.html#Composition.section:sectionAdvanceDirectives")
				.build();

		SectionSearchStrategyCollection searchStrategyCollection = SectionSearchStrategyCollection.newBuilder()
				.addStrategy(Consent.class, new AdvanceDirectivesSectionSearchStrategy())
				.build();

		addCustomizedSection(section, searchStrategyCollection);
	}

	protected void addCustomizedSection(
			Section theSection, SectionSearchStrategyCollection theSectionSearchStrategyCollection) {
		Section section = theSection;
		for (var next : myGlobalSectionCustomizers) {
			section = next.apply(section);
		}

		Validate.isTrue(
				theSection.getResourceTypes().size()
						== theSectionSearchStrategyCollection.getResourceTypes().size(),
				"Search strategy types does not match section types");
		Validate.isTrue(
				new HashSet<>(theSection.getResourceTypes())
						.containsAll(theSectionSearchStrategyCollection.getResourceTypes()),
				"Search strategy types does not match section types");

		addSection(
				section,
				new RemoteSectionResourceSupplier(theSectionSearchStrategyCollection, myClient, myFhirContext));
	}
}
