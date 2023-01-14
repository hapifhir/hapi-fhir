package ca.uhn.fhir.jpa.ips.api;

import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.MedicationStatement;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.ResourceType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SectionRegistry {

	private final ArrayList<Section> mySections = new ArrayList<>();

	public SectionRegistry() {
		addSection(IpsSectionEnum.ALLERGY_INTOLERANCE)
			.withTitle("Allergies and Intolerances")
			.withSectionCode("48765-2")
			.withSectionDisplay("Allergies and Adverse Reactions")
			.withResourceTypes(ResourceType.AllergyIntolerance.name())
			.withProfile("http://hl7.org/fhir/uv/ips/StructureDefinition/AllergiesAndIntolerances-uv-ips")
			.withNoInfoGenerator(new AllergyIntoleranceNoInfoR4Generator())
			.build();

		addSection(IpsSectionEnum.MEDICATION_SUMMARY)
			.withTitle("Medication List")
			.withSectionCode("10160-0")
			.withSectionDisplay("Medication List")
			.withResourceTypes(ResourceType.MedicationStatement.name(), ResourceType.MedicationRequest.name())
			.withProfile("http://hl7.org/fhir/uv/ips/StructureDefinition/MedicationSummary-uv-ips")
			.build();

		addSection(IpsSectionEnum.PROBLEM_LIST)
			.withTitle("Problem List")
			.withSectionCode("11450-4")
			.withSectionDisplay("Problem List")
			.withResourceTypes(ResourceType.Condition.name())
			.withProfile("http://hl7.org/fhir/uv/ips/StructureDefinition/ProblemList-uv-ips")
			.build();

		addSection(IpsSectionEnum.IMMUNIZATIONS)
			.withTitle("History of Immunizations")
			.withSectionCode("11369-6")
			.withSectionDisplay("History of Immunizations")
			.withResourceTypes(ResourceType.Immunization.name())
			.withProfile("http://hl7.org/fhir/uv/ips/StructureDefinition/Immunizations-uv-ips")
			.build();

		addSection(IpsSectionEnum.PROCEDURES)
			.withTitle("History of Procedures")
			.withSectionCode("47519-4")
			.withSectionDisplay("History of Procedures")
			.withResourceTypes(ResourceType.Procedure.name())
			.withProfile("http://hl7.org/fhir/uv/ips/StructureDefinition/HistoryOfProcedures-uv-ips")
			.build();

		addSection(IpsSectionEnum.MEDICAL_DEVICES)
			.withTitle("Medical Devices")
			.withSectionCode("46240-8")
			.withSectionDisplay("Medical Devices")
			.withResourceTypes(ResourceType.DeviceUseStatement.name())
			.withProfile("http://hl7.org/fhir/uv/ips/StructureDefinition/MedicalDevices-uv-ips")
			.build();

		addSection(IpsSectionEnum.DIAGNOSTIC_RESULTS)
			.withTitle("Diagnostic Results")
			.withSectionCode("30954-2")
			.withSectionDisplay("Diagnostic Results")
			.withResourceTypes(ResourceType.DiagnosticReport.name(), ResourceType.Observation.name())
			.withProfile("http://hl7.org/fhir/uv/ips/StructureDefinition/DiagnosticResults-uv-ips")
			.build();

		addSection(IpsSectionEnum.VITAL_SIGNS)
			.withTitle("Vital Signs")
			.withSectionCode("8716-3")
			.withSectionDisplay("Vital Signs")
			.withResourceTypes(ResourceType.Observation.name())
			.withProfile("http://hl7.org/fhir/uv/ips/StructureDefinition/VitalSigns-uv-ips")
			.build();

		addSection(IpsSectionEnum.PREGNANCY)
			.withTitle("Pregnancy Information")
			.withSectionCode("11362-0")
			.withSectionDisplay("Pregnancy Information")
			.withResourceTypes(ResourceType.Observation.name())
			.withProfile("http://hl7.org/fhir/uv/ips/StructureDefinition/Pregnancy-uv-ips")
			.build();

		addSection(IpsSectionEnum.SOCIAL_HISTORY)
			.withTitle("Social History")
			.withSectionCode("29762-2")
			.withSectionDisplay("Social History")
			.withResourceTypes(ResourceType.Observation.name())
			.withProfile("http://hl7.org/fhir/uv/ips/StructureDefinition/SocialHistory-uv-ips")
			.build();

		addSection(IpsSectionEnum.ILLNESS_HISTORY)
			.withTitle("History of Past Illness")
			.withSectionCode("11348-0")
			.withSectionDisplay("History of Past Illness")
			.withResourceTypes(ResourceType.Condition.name())
			.withProfile("http://hl7.org/fhir/uv/ips/StructureDefinition/PastHistoryOfIllnesses-uv-ips")
			.build();

		addSection(IpsSectionEnum.FUNCTIONAL_STATUS)
			.withTitle("Functional Status")
			.withSectionCode("47420-5")
			.withSectionDisplay("Functional Status")
			.withResourceTypes(ResourceType.ClinicalImpression.name())
			.withProfile("http://hl7.org/fhir/uv/ips/StructureDefinition/FunctionalStatus-uv-ips")
			.build();

		addSection(IpsSectionEnum.PLAN_OF_CARE)
			.withTitle("Plan of Care")
			.withSectionCode("18776-5")
			.withSectionDisplay("Plan of Care")
			.withResourceTypes(ResourceType.CarePlan.name())
			.withProfile("http://hl7.org/fhir/uv/ips/StructureDefinition/PlanOfCare-uv-ips")
			.build();

		addSection(IpsSectionEnum.ADVANCE_DIRECTIVES)
			.withTitle("Advance Directives")
			.withSectionCode("42349-0")
			.withSectionDisplay("Advance Directives")
			.withResourceTypes(ResourceType.Consent.name())
			.withProfile("http://hl7.org/fhir/uv/ips/StructureDefinition/AdvanceDirectives-uv-ips")
			.build();
	}

	private SectionBuilder addSection(IpsSectionEnum theSectionEnum) {
		return new SectionBuilder(theSectionEnum);
	}

	public List<Section> getSections() {
		return Collections.unmodifiableList(mySections);
	}


	public interface INoInfoGenerator {

		IBaseResource generate(IIdType theSubjectId);

	}

	private class SectionBuilder {

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
			mySections.add(new Section(mySectionEnum, myTitle, mySectionCode, mySectionDisplay, myResourceTypes, myProfile, myNoInfoGenerator));
		}
	}

	private static class AllergyIntoleranceNoInfoR4Generator implements INoInfoGenerator {
		@Override
		public IBaseResource generate(IIdType theSubjectId) {
			AllergyIntolerance allergy = new AllergyIntolerance();
			allergy.setCode(new CodeableConcept().addCoding(new Coding().setCode("no-allergy-info").setSystem("http://hl7.org/fhir/uv/ips/CodeSystem/absent-unknown-uv-ips").setDisplay("No information about allergies")))
				.setPatient(new Reference(theSubjectId))
				.setClinicalStatus(new CodeableConcept().addCoding(new Coding().setCode("active").setSystem("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical")))
				.setId(IdType.newRandomUuid());
			return allergy;
		}
	}

	private static class MedicationNoInfoR4Generator implements INoInfoGenerator {
		@Override
		public IBaseResource generate(IIdType theSubjectId) {
			MedicationStatement medication = new MedicationStatement();
			// setMedicationCodeableConcept is not available
			medication.setMedication(new CodeableConcept().addCoding(new Coding().setCode("no-medication-info").setSystem("http://hl7.org/fhir/uv/ips/CodeSystem/absent-unknown-uv-ips").setDisplay("No information about medications")))
				.setSubject(new Reference(theSubjectId))
				.setStatus(MedicationStatement.MedicationStatementStatus.UNKNOWN)
				// .setEffective(new Period().addExtension().setUrl("http://hl7.org/fhir/StructureDefinition/data-absent-reason").setValue((new Coding().setCode("not-applicable"))))
				.setId(IdType.newRandomUuid());
			return medication;
		}
	}

	private static class ProblemNoInfoR4Generator implements INoInfoGenerator {
		@Override
		public IBaseResource generate(IIdType theSubjectId) {
			Condition condition = new Condition();
			condition.setCode(new CodeableConcept().addCoding(new Coding().setCode("no-problem-info").setSystem("http://hl7.org/fhir/uv/ips/CodeSystem/absent-unknown-uv-ips").setDisplay("No information about problems")))
				.setSubject(new Reference(theSubjectId))
				.setClinicalStatus(new CodeableConcept().addCoding(new Coding().setCode("active").setSystem("http://terminology.hl7.org/CodeSystem/condition-clinical")))
				.setId(IdType.newRandomUuid());
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

		public Section(IpsSectionEnum theSectionEnum, String theTitle, String theSectionCode, String theSectionDisplay, List<String> theResourceTypes, String theProfile, INoInfoGenerator theNoInfoGenerator) {
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
