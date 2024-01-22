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
package ca.uhn.fhir.jpa.ips.api;

import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.MedicationStatement;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.ResourceType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * This class is the registry for sections for the IPS document. It can be extended
 * and customized if you wish to add / remove / change sections.
 * <p>
 * By default, all standard sections in the
 * <a href="http://hl7.org/fhir/uv/ips/">base IPS specification IG</a>
 * are included. You can customize this to remove sections, or to add new ones
 * as permitted by the IG.
 * </p>
 * <p>
 * To customize the sections, you may override the {@link #addSections()} method
 * in order to add new sections or remove them. You may also override individual
 * section methods such as {@link #addSectionAllergyIntolerance()} or
 * {@link #addSectionAdvanceDirectives()}.
 * </p>
 */
public class SectionRegistry {

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
	private final ArrayList<Section> mySections = new ArrayList<>();
	private final List<Consumer<SectionBuilder>> myGlobalCustomizers = new ArrayList<>();
	private final List<SectionBuilder> mySectionBuilders = new ArrayList<>();

	/**
	 * Constructor
	 */
	public SectionRegistry() {
		super();
	}

	/**
	 * This method should be automatically called by the Spring context. It initializes
	 * the registry.
	 */
	@PostConstruct
	public final void initialize() {
		Validate.isTrue(mySections.isEmpty(), "Sections are already initialized");
		addSections();
		mySectionBuilders.forEach(SectionBuilder::done);
	}

	public boolean isInitialized() {
		return !mySections.isEmpty();
	}

	/**
	 * Add the various sections to the registry in order. This method can be overridden for
	 * customization.
	 */
	protected void addSections() {
		addSectionAllergyIntolerance();
		addSectionMedicationSummary();
		addSectionProblemList();
		addSectionImmunizations();
		addSectionProcedures();
		addSectionMedicalDevices();
		addSectionDiagnosticResults();
		addSectionVitalSigns();
		addSectionPregnancy();
		addSectionSocialHistory();
		addSectionIllnessHistory();
		addSectionFunctionalStatus();
		addSectionPlanOfCare();
		addSectionAdvanceDirectives();
	}

	protected void addSectionAllergyIntolerance() {
		addSection()
				.withTitle("Allergies and Intolerances")
				.withSectionSystem(SECTION_SYSTEM_LOINC)
				.withSectionSystem(SECTION_SYSTEM_LOINC)
				.withSectionCode(SECTION_CODE_ALLERGY_INTOLERANCE)
				.withSectionDisplay("Allergies and adverse reactions Document")
				.withResourceTypes(ResourceType.AllergyIntolerance.name())
				.withProfile(
						"https://hl7.org/fhir/uv/ips/StructureDefinition-Composition-uv-ips-definitions.html#Composition.section:sectionAllergies")
				.withNoInfoGenerator(new AllergyIntoleranceNoInfoR4Generator());
	}

	protected void addSectionMedicationSummary() {
		addSection()
				.withTitle("Medication List")
				.withSectionSystem(SECTION_SYSTEM_LOINC)
				.withSectionCode(SECTION_CODE_MEDICATION_SUMMARY)
				.withSectionDisplay("History of Medication use Narrative")
				.withResourceTypes(
						ResourceType.MedicationStatement.name(),
						ResourceType.MedicationRequest.name(),
						ResourceType.MedicationAdministration.name(),
						ResourceType.MedicationDispense.name())
				.withProfile(
						"https://hl7.org/fhir/uv/ips/StructureDefinition-Composition-uv-ips-definitions.html#Composition.section:sectionMedications")
				.withNoInfoGenerator(new MedicationNoInfoR4Generator());
	}

	protected void addSectionProblemList() {
		addSection()
				.withTitle("Problem List")
				.withSectionSystem(SECTION_SYSTEM_LOINC)
				.withSectionCode(SECTION_CODE_PROBLEM_LIST)
				.withSectionDisplay("Problem list - Reported")
				.withResourceTypes(ResourceType.Condition.name())
				.withProfile(
						"https://hl7.org/fhir/uv/ips/StructureDefinition-Composition-uv-ips-definitions.html#Composition.section:sectionProblems")
				.withNoInfoGenerator(new ProblemNoInfoR4Generator());
	}

	protected void addSectionImmunizations() {
		addSection()
				.withTitle("History of Immunizations")
				.withSectionSystem(SECTION_SYSTEM_LOINC)
				.withSectionCode(SECTION_CODE_IMMUNIZATIONS)
				.withSectionDisplay("History of Immunization Narrative")
				.withResourceTypes(ResourceType.Immunization.name())
				.withProfile(
						"https://hl7.org/fhir/uv/ips/StructureDefinition-Composition-uv-ips-definitions.html#Composition.section:sectionImmunizations");
	}

	protected void addSectionProcedures() {
		addSection()
				.withTitle("History of Procedures")
				.withSectionSystem(SECTION_SYSTEM_LOINC)
				.withSectionCode(SECTION_CODE_PROCEDURES)
				.withSectionDisplay("History of Procedures Document")
				.withResourceTypes(ResourceType.Procedure.name())
				.withProfile(
						"https://hl7.org/fhir/uv/ips/StructureDefinition-Composition-uv-ips-definitions.html#Composition.section:sectionProceduresHx");
	}

	protected void addSectionMedicalDevices() {
		addSection()
				.withTitle("Medical Devices")
				.withSectionSystem(SECTION_SYSTEM_LOINC)
				.withSectionCode(SECTION_CODE_MEDICAL_DEVICES)
				.withSectionDisplay("History of medical device use")
				.withResourceTypes(ResourceType.DeviceUseStatement.name())
				.withProfile(
						"https://hl7.org/fhir/uv/ips/StructureDefinition-Composition-uv-ips-definitions.html#Composition.section:sectionMedicalDevices");
	}

	protected void addSectionDiagnosticResults() {
		addSection()
				.withTitle("Diagnostic Results")
				.withSectionSystem(SECTION_SYSTEM_LOINC)
				.withSectionCode(SECTION_CODE_DIAGNOSTIC_RESULTS)
				.withSectionDisplay("Relevant diagnostic tests/laboratory data Narrative")
				.withResourceTypes(ResourceType.DiagnosticReport.name(), ResourceType.Observation.name())
				.withProfile(
						"https://hl7.org/fhir/uv/ips/StructureDefinition-Composition-uv-ips-definitions.html#Composition.section:sectionResults");
	}

	protected void addSectionVitalSigns() {
		addSection()
				.withTitle("Vital Signs")
				.withSectionSystem(SECTION_SYSTEM_LOINC)
				.withSectionCode(SECTION_CODE_VITAL_SIGNS)
				.withSectionDisplay("Vital signs")
				.withResourceTypes(ResourceType.Observation.name())
				.withProfile(
						"https://hl7.org/fhir/uv/ips/StructureDefinition-Composition-uv-ips-definitions.html#Composition.section:sectionVitalSigns");
	}

	protected void addSectionPregnancy() {
		addSection()
				.withTitle("Pregnancy Information")
				.withSectionSystem(SECTION_SYSTEM_LOINC)
				.withSectionCode(SECTION_CODE_PREGNANCY)
				.withSectionDisplay("History of pregnancies Narrative")
				.withResourceTypes(ResourceType.Observation.name())
				.withProfile(
						"https://hl7.org/fhir/uv/ips/StructureDefinition-Composition-uv-ips-definitions.html#Composition.section:sectionPregnancyHx");
	}

	protected void addSectionSocialHistory() {
		addSection()
				.withTitle("Social History")
				.withSectionSystem(SECTION_SYSTEM_LOINC)
				.withSectionCode(SECTION_CODE_SOCIAL_HISTORY)
				.withSectionDisplay("Social history Narrative")
				.withResourceTypes(ResourceType.Observation.name())
				.withProfile(
						"https://hl7.org/fhir/uv/ips/StructureDefinition-Composition-uv-ips-definitions.html#Composition.section:sectionSocialHistory");
	}

	protected void addSectionIllnessHistory() {
		addSection()
				.withTitle("History of Past Illness")
				.withSectionSystem(SECTION_SYSTEM_LOINC)
				.withSectionCode(SECTION_CODE_ILLNESS_HISTORY)
				.withSectionDisplay("History of Past illness Narrative")
				.withResourceTypes(ResourceType.Condition.name())
				.withProfile(
						"https://hl7.org/fhir/uv/ips/StructureDefinition-Composition-uv-ips-definitions.html#Composition.section:sectionPastIllnessHx");
	}

	protected void addSectionFunctionalStatus() {
		addSection()
				.withTitle("Functional Status")
				.withSectionSystem(SECTION_SYSTEM_LOINC)
				.withSectionCode(SECTION_CODE_FUNCTIONAL_STATUS)
				.withSectionDisplay("Functional status assessment note")
				.withResourceTypes(ResourceType.ClinicalImpression.name())
				.withProfile(
						"https://hl7.org/fhir/uv/ips/StructureDefinition-Composition-uv-ips-definitions.html#Composition.section:sectionFunctionalStatus");
	}

	protected void addSectionPlanOfCare() {
		addSection()
				.withTitle("Plan of Care")
				.withSectionSystem(SECTION_SYSTEM_LOINC)
				.withSectionCode(SECTION_CODE_PLAN_OF_CARE)
				.withSectionDisplay("Plan of care note")
				.withResourceTypes(ResourceType.CarePlan.name())
				.withProfile(
						"https://hl7.org/fhir/uv/ips/StructureDefinition-Composition-uv-ips-definitions.html#Composition.section:sectionPlanOfCare");
	}

	protected void addSectionAdvanceDirectives() {
		addSection()
				.withTitle("Advance Directives")
				.withSectionSystem(SECTION_SYSTEM_LOINC)
				.withSectionCode(SECTION_CODE_ADVANCE_DIRECTIVES)
				.withSectionDisplay("Advance directives")
				.withResourceTypes(ResourceType.Consent.name())
				.withProfile(
						"https://hl7.org/fhir/uv/ips/StructureDefinition-Composition-uv-ips-definitions.html#Composition.section:sectionAdvanceDirectives");
	}

	protected SectionBuilder addSection() {
		SectionBuilder retVal = new SectionBuilder();
		mySectionBuilders.add(retVal);
		return retVal;
	}

	public SectionRegistry addGlobalCustomizer(Consumer<SectionBuilder> theGlobalCustomizer) {
		Validate.notNull(theGlobalCustomizer, "theGlobalCustomizer must not be null");
		myGlobalCustomizers.add(theGlobalCustomizer);
		return this;
	}

	public List<Section> getSections() {
		Validate.isTrue(isInitialized(), "Section registry has not been initialized");
		return Collections.unmodifiableList(mySections);
	}

	public Section getSection(String theSectionSystem, String theSectionCode) {
		return getSections().stream()
				.filter(t -> t.getSectionSystem().equals(theSectionSystem))
				.filter(t -> t.getSectionCode().equals(theSectionCode))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException(
						"No section for system[" + theSectionSystem + "] and code[" + theSectionCode + "]"));
	}

	public interface INoInfoGenerator {

		/**
		 * Generate an appropriate no-info resource. The resource does not need to have an ID populated,
		 * although it can if it is a resource found in the repository.
		 */
		IBaseResource generate(IIdType theSubjectId);
	}

	public class SectionBuilder {

		private String myTitle;
		private String mySectionSystem;
		private String mySectionCode;
		private String mySectionDisplay;
		private List<String> myResourceTypes;
		private String myProfile;
		private INoInfoGenerator myNoInfoGenerator;

		public SectionBuilder() {
			super();
		}

		public SectionBuilder withTitle(String theTitle) {
			Validate.notBlank(theTitle);
			myTitle = theTitle;
			return this;
		}

		public SectionBuilder withSectionSystem(String theSectionSystem) {
			Validate.notBlank(theSectionSystem);
			mySectionSystem = theSectionSystem;
			return this;
		}

		public SectionBuilder withSectionCode(String theSectionCode) {
			Validate.notBlank(theSectionCode);
			mySectionCode = theSectionCode;
			return this;
		}

		public SectionBuilder withSectionDisplay(String theSectionDisplay) {
			Validate.notBlank(theSectionDisplay);
			mySectionDisplay = theSectionDisplay;
			return this;
		}

		public SectionBuilder withResourceTypes(String... theResourceTypes) {
			Validate.isTrue(theResourceTypes.length > 0);
			myResourceTypes = Arrays.asList(theResourceTypes);
			return this;
		}

		public SectionBuilder withProfile(String theProfile) {
			Validate.notBlank(theProfile);
			myProfile = theProfile;
			return this;
		}

		@SuppressWarnings("UnusedReturnValue")
		public SectionBuilder withNoInfoGenerator(INoInfoGenerator theNoInfoGenerator) {
			myNoInfoGenerator = theNoInfoGenerator;
			return this;
		}

		private void done() {
			Validate.notBlank(mySectionSystem, "No section system has been defined for this section");
			Validate.notBlank(mySectionCode, "No section code has been defined for this section");
			Validate.notBlank(mySectionDisplay, "No section display has been defined for this section");

			myGlobalCustomizers.forEach(t -> t.accept(this));
			mySections.add(new Section(
					myTitle,
					mySectionSystem,
					mySectionCode,
					mySectionDisplay,
					myResourceTypes,
					myProfile,
					myNoInfoGenerator));
		}
	}

	private static class AllergyIntoleranceNoInfoR4Generator implements INoInfoGenerator {
		@Override
		public IBaseResource generate(IIdType theSubjectId) {
			AllergyIntolerance allergy = new AllergyIntolerance();
			allergy.setCode(new CodeableConcept()
							.addCoding(new Coding()
									.setCode("no-allergy-info")
									.setSystem("http://hl7.org/fhir/uv/ips/CodeSystem/absent-unknown-uv-ips")
									.setDisplay("No information about allergies")))
					.setPatient(new Reference(theSubjectId))
					.setClinicalStatus(new CodeableConcept()
							.addCoding(new Coding()
									.setCode("active")
									.setSystem("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical")));
			return allergy;
		}
	}

	private static class MedicationNoInfoR4Generator implements INoInfoGenerator {
		@Override
		public IBaseResource generate(IIdType theSubjectId) {
			MedicationStatement medication = new MedicationStatement();
			// setMedicationCodeableConcept is not available
			medication
					.setMedication(new CodeableConcept()
							.addCoding(new Coding()
									.setCode("no-medication-info")
									.setSystem("http://hl7.org/fhir/uv/ips/CodeSystem/absent-unknown-uv-ips")
									.setDisplay("No information about medications")))
					.setSubject(new Reference(theSubjectId))
					.setStatus(MedicationStatement.MedicationStatementStatus.UNKNOWN);
			// .setEffective(new
			// Period().addExtension().setUrl("http://hl7.org/fhir/StructureDefinition/data-absent-reason").setValue((new Coding().setCode("not-applicable"))))
			return medication;
		}
	}

	private static class ProblemNoInfoR4Generator implements INoInfoGenerator {
		@Override
		public IBaseResource generate(IIdType theSubjectId) {
			Condition condition = new Condition();
			condition
					.setCode(new CodeableConcept()
							.addCoding(new Coding()
									.setCode("no-problem-info")
									.setSystem("http://hl7.org/fhir/uv/ips/CodeSystem/absent-unknown-uv-ips")
									.setDisplay("No information about problems")))
					.setSubject(new Reference(theSubjectId))
					.setClinicalStatus(new CodeableConcept()
							.addCoding(new Coding()
									.setCode("active")
									.setSystem("http://terminology.hl7.org/CodeSystem/condition-clinical")));
			return condition;
		}
	}

	public static class Section {

		private final String myTitle;
		private final String mySectionCode;
		private final String mySectionDisplay;
		private final List<String> myResourceTypes;
		private final String myProfile;
		private final INoInfoGenerator myNoInfoGenerator;

		private final String mySectionSystem;

		public Section(
				String theTitle,
				String theSectionSystem,
				String theSectionCode,
				String theSectionDisplay,
				List<String> theResourceTypes,
				String theProfile,
				INoInfoGenerator theNoInfoGenerator) {
			myTitle = theTitle;
			mySectionSystem = theSectionSystem;
			mySectionCode = theSectionCode;
			mySectionDisplay = theSectionDisplay;
			myResourceTypes = List.copyOf(theResourceTypes);
			myProfile = theProfile;
			myNoInfoGenerator = theNoInfoGenerator;
		}

		@Nullable
		public INoInfoGenerator getNoInfoGenerator() {
			return myNoInfoGenerator;
		}

		public List<String> getResourceTypes() {
			return myResourceTypes;
		}

		public String getProfile() {
			return myProfile;
		}

		public String getTitle() {
			return myTitle;
		}

		public String getSectionSystem() {
			return mySectionSystem;
		}

		public String getSectionCode() {
			return mySectionCode;
		}

		public String getSectionDisplay() {
			return mySectionDisplay;
		}
	}
}
