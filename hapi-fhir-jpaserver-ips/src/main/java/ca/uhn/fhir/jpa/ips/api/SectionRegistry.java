/*-
 * #%L
 * HAPI FHIR JPA Server - International Patient Summary (IPS)
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
package ca.uhn.fhir.jpa.ips.api;

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
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;

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

	private final ArrayList<Section> mySections = new ArrayList<>();
	private List<Consumer<SectionBuilder>> myGlobalCustomizers = new ArrayList<>();

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
		addSection(IpsSectionEnum.ALLERGY_INTOLERANCE)
				.withTitle("Allergies and Intolerances")
				.withSectionCode("48765-2")
				.withSectionDisplay("Allergies and adverse reactions Document")
				.withResourceTypes(ResourceType.AllergyIntolerance.name())
				.withProfile(
						"https://hl7.org/fhir/uv/ips/StructureDefinition-Composition-uv-ips-definitions.html#Composition.section:sectionAllergies")
				.withNoInfoGenerator(new AllergyIntoleranceNoInfoR4Generator())
				.build();
	}

	protected void addSectionMedicationSummary() {
		addSection(IpsSectionEnum.MEDICATION_SUMMARY)
				.withTitle("Medication List")
				.withSectionCode("10160-0")
				.withSectionDisplay("History of Medication use Narrative")
				.withResourceTypes(
						ResourceType.MedicationStatement.name(),
						ResourceType.MedicationRequest.name(),
						ResourceType.MedicationAdministration.name(),
						ResourceType.MedicationDispense.name())
				.withProfile(
						"https://hl7.org/fhir/uv/ips/StructureDefinition-Composition-uv-ips-definitions.html#Composition.section:sectionMedications")
				.withNoInfoGenerator(new MedicationNoInfoR4Generator())
				.build();
	}

	protected void addSectionProblemList() {
		addSection(IpsSectionEnum.PROBLEM_LIST)
				.withTitle("Problem List")
				.withSectionCode("11450-4")
				.withSectionDisplay("Problem list - Reported")
				.withResourceTypes(ResourceType.Condition.name())
				.withProfile(
						"https://hl7.org/fhir/uv/ips/StructureDefinition-Composition-uv-ips-definitions.html#Composition.section:sectionProblems")
				.withNoInfoGenerator(new ProblemNoInfoR4Generator())
				.build();
	}

	protected void addSectionImmunizations() {
		addSection(IpsSectionEnum.IMMUNIZATIONS)
				.withTitle("History of Immunizations")
				.withSectionCode("11369-6")
				.withSectionDisplay("History of Immunization Narrative")
				.withResourceTypes(ResourceType.Immunization.name())
				.withProfile(
						"https://hl7.org/fhir/uv/ips/StructureDefinition-Composition-uv-ips-definitions.html#Composition.section:sectionImmunizations")
				.build();
	}

	protected void addSectionProcedures() {
		addSection(IpsSectionEnum.PROCEDURES)
				.withTitle("History of Procedures")
				.withSectionCode("47519-4")
				.withSectionDisplay("History of Procedures Document")
				.withResourceTypes(ResourceType.Procedure.name())
				.withProfile(
						"https://hl7.org/fhir/uv/ips/StructureDefinition-Composition-uv-ips-definitions.html#Composition.section:sectionProceduresHx")
				.build();
	}

	protected void addSectionMedicalDevices() {
		addSection(IpsSectionEnum.MEDICAL_DEVICES)
				.withTitle("Medical Devices")
				.withSectionCode("46264-8")
				.withSectionDisplay("History of medical device use")
				.withResourceTypes(ResourceType.DeviceUseStatement.name())
				.withProfile(
						"https://hl7.org/fhir/uv/ips/StructureDefinition-Composition-uv-ips-definitions.html#Composition.section:sectionMedicalDevices")
				.build();
	}

	protected void addSectionDiagnosticResults() {
		addSection(IpsSectionEnum.DIAGNOSTIC_RESULTS)
				.withTitle("Diagnostic Results")
				.withSectionCode("30954-2")
				.withSectionDisplay("Relevant diagnostic tests/laboratory data Narrative")
				.withResourceTypes(ResourceType.DiagnosticReport.name(), ResourceType.Observation.name())
				.withProfile(
						"https://hl7.org/fhir/uv/ips/StructureDefinition-Composition-uv-ips-definitions.html#Composition.section:sectionResults")
				.build();
	}

	protected void addSectionVitalSigns() {
		addSection(IpsSectionEnum.VITAL_SIGNS)
				.withTitle("Vital Signs")
				.withSectionCode("8716-3")
				.withSectionDisplay("Vital signs")
				.withResourceTypes(ResourceType.Observation.name())
				.withProfile(
						"https://hl7.org/fhir/uv/ips/StructureDefinition-Composition-uv-ips-definitions.html#Composition.section:sectionVitalSigns")
				.build();
	}

	protected void addSectionPregnancy() {
		addSection(IpsSectionEnum.PREGNANCY)
				.withTitle("Pregnancy Information")
				.withSectionCode("10162-6")
				.withSectionDisplay("History of pregnancies Narrative")
				.withResourceTypes(ResourceType.Observation.name())
				.withProfile(
						"https://hl7.org/fhir/uv/ips/StructureDefinition-Composition-uv-ips-definitions.html#Composition.section:sectionPregnancyHx")
				.build();
	}

	protected void addSectionSocialHistory() {
		addSection(IpsSectionEnum.SOCIAL_HISTORY)
				.withTitle("Social History")
				.withSectionCode("29762-2")
				.withSectionDisplay("Social history Narrative")
				.withResourceTypes(ResourceType.Observation.name())
				.withProfile(
						"https://hl7.org/fhir/uv/ips/StructureDefinition-Composition-uv-ips-definitions.html#Composition.section:sectionSocialHistory")
				.build();
	}

	protected void addSectionIllnessHistory() {
		addSection(IpsSectionEnum.ILLNESS_HISTORY)
				.withTitle("History of Past Illness")
				.withSectionCode("11348-0")
				.withSectionDisplay("History of Past illness Narrative")
				.withResourceTypes(ResourceType.Condition.name())
				.withProfile(
						"https://hl7.org/fhir/uv/ips/StructureDefinition-Composition-uv-ips-definitions.html#Composition.section:sectionPastIllnessHx")
				.build();
	}

	protected void addSectionFunctionalStatus() {
		addSection(IpsSectionEnum.FUNCTIONAL_STATUS)
				.withTitle("Functional Status")
				.withSectionCode("47420-5")
				.withSectionDisplay("Functional status assessment note")
				.withResourceTypes(ResourceType.ClinicalImpression.name())
				.withProfile(
						"https://hl7.org/fhir/uv/ips/StructureDefinition-Composition-uv-ips-definitions.html#Composition.section:sectionFunctionalStatus")
				.build();
	}

	protected void addSectionPlanOfCare() {
		addSection(IpsSectionEnum.PLAN_OF_CARE)
				.withTitle("Plan of Care")
				.withSectionCode("18776-5")
				.withSectionDisplay("Plan of care note")
				.withResourceTypes(ResourceType.CarePlan.name())
				.withProfile(
						"https://hl7.org/fhir/uv/ips/StructureDefinition-Composition-uv-ips-definitions.html#Composition.section:sectionPlanOfCare")
				.build();
	}

	protected void addSectionAdvanceDirectives() {
		addSection(IpsSectionEnum.ADVANCE_DIRECTIVES)
				.withTitle("Advance Directives")
				.withSectionCode("42348-3")
				.withSectionDisplay("Advance directives")
				.withResourceTypes(ResourceType.Consent.name())
				.withProfile(
						"https://hl7.org/fhir/uv/ips/StructureDefinition-Composition-uv-ips-definitions.html#Composition.section:sectionAdvanceDirectives")
				.build();
	}

	private SectionBuilder addSection(IpsSectionEnum theSectionEnum) {
		return new SectionBuilder(theSectionEnum);
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

	public Section getSection(IpsSectionEnum theSectionEnum) {
		return getSections().stream()
				.filter(t -> t.getSectionEnum() == theSectionEnum)
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("No section for type: " + theSectionEnum));
	}

	public interface INoInfoGenerator {

		/**
		 * Generate an appropriate no-info resource. The resource does not need to have an ID populated,
		 * although it can if it is a resource found in the repository.
		 */
		IBaseResource generate(IIdType theSubjectId);
	}

	public class SectionBuilder {

		private final IpsSectionEnum mySectionEnum;
		private String myTitle;
		private String mySectionCode;
		private String mySectionDisplay;
		private List<String> myResourceTypes;
		private String myProfile;
		private INoInfoGenerator myNoInfoGenerator;

		public SectionBuilder(IpsSectionEnum theSectionEnum) {
			mySectionEnum = theSectionEnum;
		}

		public SectionBuilder withTitle(String theTitle) {
			Validate.notBlank(theTitle);
			myTitle = theTitle;
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

		public SectionBuilder withNoInfoGenerator(INoInfoGenerator theNoInfoGenerator) {
			myNoInfoGenerator = theNoInfoGenerator;
			return this;
		}

		public void build() {
			myGlobalCustomizers.forEach(t -> t.accept(this));
			mySections.add(new Section(
					mySectionEnum,
					myTitle,
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

		private final IpsSectionEnum mySectionEnum;
		private final String myTitle;
		private final String mySectionCode;
		private final String mySectionDisplay;
		private final List<String> myResourceTypes;
		private final String myProfile;
		private final INoInfoGenerator myNoInfoGenerator;

		public Section(
				IpsSectionEnum theSectionEnum,
				String theTitle,
				String theSectionCode,
				String theSectionDisplay,
				List<String> theResourceTypes,
				String theProfile,
				INoInfoGenerator theNoInfoGenerator) {
			mySectionEnum = theSectionEnum;
			myTitle = theTitle;
			mySectionCode = theSectionCode;
			mySectionDisplay = theSectionDisplay;
			myResourceTypes = Collections.unmodifiableList(new ArrayList<>(theResourceTypes));
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

		public IpsSectionEnum getSectionEnum() {
			return mySectionEnum;
		}

		public String getTitle() {
			return myTitle;
		}

		public String getSectionCode() {
			return mySectionCode;
		}

		public String getSectionDisplay() {
			return mySectionDisplay;
		}
	}
}
