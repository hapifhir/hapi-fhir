/*-
 * #%L
 * HAPI FHIR - CDS Hooks
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
package ca.uhn.hapi.fhir.cdshooks.svc.cr.discovery;

import org.hl7.fhir.r5.model.CanonicalType;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.DataRequirement;
import org.hl7.fhir.r5.model.Extension;
import org.hl7.fhir.r5.model.Library;
import org.hl7.fhir.r5.model.PlanDefinition;
import org.hl7.fhir.r5.model.ValueSet;
import org.opencds.cqf.fhir.api.Repository;
import org.opencds.cqf.fhir.utility.SearchHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ca.uhn.hapi.fhir.cdshooks.svc.cr.CdsCrConstants.CQF_FHIR_QUERY_PATTERN;
import static ca.uhn.hapi.fhir.cdshooks.svc.cr.CdsCrConstants.CRMI_EFFECTIVE_DATA_REQUIREMENTS;

public class PrefetchTemplateBuilderR5 extends BasePrefetchTemplateBuilder {

	public PrefetchTemplateBuilderR5(Repository theRepository) {
		super(theRepository);
	}

	public PrefetchUrlList getPrefetchUrlList(PlanDefinition thePlanDefinition) {
		if (thePlanDefinition == null) return null;
		PrefetchUrlList prefetchList = new PrefetchUrlList();
		Library library = resolvePrimaryLibrary(thePlanDefinition);
		if (library == null || !library.hasDataRequirement()) return null;
		for (DataRequirement dataRequirement : library.getDataRequirement()) {
			List<String> requestUrls = createRequestUrl(dataRequirement);
			if (requestUrls != null) {
				prefetchList.addAll(requestUrls);
			}
		}
		return prefetchList;
	}

	@SuppressWarnings("ReassignedVariable")
	protected Library resolvePrimaryLibrary(PlanDefinition thePlanDefinition) {
		Library library = null;
		Extension dataReqExt = thePlanDefinition.getExtensionByUrl(CRMI_EFFECTIVE_DATA_REQUIREMENTS);
		// Use a Module Definition Library with Effective Data Requirements for the Plan Definition if it exists
		if (dataReqExt != null && dataReqExt.hasValue()) {
			CanonicalType moduleDefCanonical = (CanonicalType) dataReqExt.getValue();
			library = (Library) SearchHelper.searchRepositoryByCanonical(myRepository, moduleDefCanonical);
		}
		// Otherwise use the primary Library
		if (library == null && thePlanDefinition.hasLibrary()) {
			// The CPGComputablePlanDefinition profile limits the cardinality of library to 1
			library = (Library) SearchHelper.searchRepositoryByCanonical(
					myRepository, thePlanDefinition.getLibrary().get(0));
		}
		return library;
	}

	protected List<String> createRequestUrl(DataRequirement theDataRequirement) {
		List<String> urlList = new ArrayList<>();
		// if we have a fhirQueryPattern extensions, use them
		List<Extension> fhirQueryExtList = theDataRequirement.getExtension().stream()
				.filter(e -> e.getUrl().equals(CQF_FHIR_QUERY_PATTERN) && e.hasValue())
				.collect(Collectors.toList());
		if (!fhirQueryExtList.isEmpty()) {
			for (Extension fhirQueryExt : fhirQueryExtList) {
				urlList.add(fhirQueryExt.getValueAsPrimitive().getValueAsString());
			}
			return urlList;
		}

		// else build the query
		if (!isPatientCompartment(theDataRequirement.getType().toCode())) return null;
		String patientRelatedResource = theDataRequirement.getType() + "?"
				+ getPatientSearchParam(theDataRequirement.getType().toCode())
				+ "=Patient/" + PATIENT_ID_CONTEXT;

		// TODO: Add valueFilter extension resolution

		if (theDataRequirement.hasCodeFilter()) {
			resolveCodeFilter(theDataRequirement, urlList, patientRelatedResource);
		} else {
			urlList.add(patientRelatedResource);
		}
		return urlList;
	}

	@SuppressWarnings("ReassignedVariable")
	protected void resolveCodeFilter(DataRequirement theDataRequirement, List<String> theUrlList, String theBaseQuery) {
		for (DataRequirement.DataRequirementCodeFilterComponent codeFilterComponent :
				theDataRequirement.getCodeFilter()) {
			if (!codeFilterComponent.hasPath()) continue;
			String path =
					mapCodePathToSearchParam(theDataRequirement.getType().toCode(), codeFilterComponent.getPath());
			if (codeFilterComponent.hasValueSetElement()) {
				for (String codes : resolveValueSetCodes(codeFilterComponent.getValueSetElement())) {
					theUrlList.add(theBaseQuery + "&" + path + "=" + codes);
				}
			} else if (codeFilterComponent.hasCode()) {
				List<Coding> codeFilterValueCodings = codeFilterComponent.getCode();
				boolean isFirstCodingInFilter = true;
				for (String code : resolveValueCodingCodes(codeFilterValueCodings)) {
					if (isFirstCodingInFilter) {
						theUrlList.add(theBaseQuery + "&" + path + "=" + code);
					} else {
						theUrlList.add("," + code);
					}
					isFirstCodingInFilter = false;
				}
			}
		}
	}

	@SuppressWarnings("ReassignedVariable")
	protected List<String> resolveValueCodingCodes(List<Coding> theValueCodings) {
		List<String> result = new ArrayList<>();
		StringBuilder codes = new StringBuilder();
		for (Coding coding : theValueCodings) {
			if (coding.hasCode()) {
				String system = coding.getSystem();
				String code = coding.getCode();
				codes = getCodesStringBuilder(result, codes, system, code);
			}
		}
		result.add(codes.toString());
		return result;
	}

	protected List<String> resolveValueSetCodes(CanonicalType theValueSetId) {
		ValueSet valueSet = (ValueSet) SearchHelper.searchRepositoryByCanonical(myRepository, theValueSetId);
		List<String> result = new ArrayList<>();
		StringBuilder codes = new StringBuilder();
		if (valueSet.hasExpansion() && valueSet.getExpansion().hasContains()) {
			for (ValueSet.ValueSetExpansionContainsComponent contains :
					valueSet.getExpansion().getContains()) {
				String system = contains.getSystem();
				String code = contains.getCode();

				codes = getCodesStringBuilder(result, codes, system, code);
			}
		} else if (valueSet.hasCompose() && valueSet.getCompose().hasInclude()) {
			for (ValueSet.ConceptSetComponent concepts : valueSet.getCompose().getInclude()) {
				String system = concepts.getSystem();
				if (concepts.hasConcept()) {
					for (ValueSet.ConceptReferenceComponent concept : concepts.getConcept()) {
						String code = concept.getCode();

						codes = getCodesStringBuilder(result, codes, system, code);
					}
				}
			}
		}
		result.add(codes.toString());
		return result;
	}

	@SuppressWarnings("ReassignedVariable")
	protected StringBuilder getCodesStringBuilder(
			List<String> theStrings, StringBuilder theCodes, String theSystem, String theCode) {
		StringBuilder codes = theCodes;
		String codeToken = theSystem + "|" + theCode;
		int postAppendLength = codes.length() + codeToken.length();
		if (codes.length() > 0 && postAppendLength < myMaxUriLength) {
			codes.append(",");
		} else if (postAppendLength > myMaxUriLength) {
			theStrings.add(codes.toString());
			codes = new StringBuilder();
		}
		codes.append(codeToken);
		return codes;
	}

	protected String mapCodePathToSearchParam(String theDataType, String thePath) {
		switch (theDataType) {
			case "MedicationAdministration":
				if (thePath.equals("medication")) return "code";
				break;
			case "MedicationDispense":
				if (thePath.equals("medication")) return "code";
				break;
			case "MedicationRequest":
				if (thePath.equals("medication")) return "code";
				break;
			case "MedicationStatement":
				if (thePath.equals("medication")) return "code";
				break;
			default:
				if (thePath.equals("vaccineCode")) return "vaccine-code";
				break;
		}
		return thePath.replace('.', '-').toLowerCase();
	}

	public static boolean isPatientCompartment(String theDataType) {
		if (theDataType == null) {
			return false;
		}
		switch (theDataType) {
			case "Account":
			case "AdverseEvent":
			case "AllergyIntolerance":
			case "Appointment":
			case "AppointmentResponse":
			case "AuditEvent":
			case "Basic":
			case "BodyStructure":
			case "CarePlan":
			case "CareTeam":
			case "ChargeItem":
			case "Claim":
			case "ClaimResponse":
			case "ClinicalImpression":
			case "Communication":
			case "CommunicationRequest":
			case "Composition":
			case "Condition":
			case "Consent":
			case "Coverage":
			case "CoverageEligibilityRequest":
			case "CoverageEligibilityResponse":
			case "DetectedIssue":
			case "DeviceRequest":
			case "DeviceUseStatement":
			case "DiagnosticReport":
			case "DocumentManifest":
			case "DocumentReference":
			case "Encounter":
			case "EnrollmentRequest":
			case "EpisodeOfCare":
			case "ExplanationOfBenefit":
			case "FamilyMemberHistory":
			case "Flag":
			case "Goal":
			case "Group":
			case "ImagingStudy":
			case "Immunization":
			case "ImmunizationEvaluation":
			case "ImmunizationRecommendation":
			case "Invoice":
			case "List":
			case "MeasureReport":
			case "Media":
			case "MedicationAdministration":
			case "MedicationDispense":
			case "MedicationRequest":
			case "MedicationStatement":
			case "MolecularSequence":
			case "NutritionOrder":
			case "Observation":
			case "Patient":
			case "Person":
			case "Procedure":
			case "Provenance":
			case "QuestionnaireResponse":
			case "RelatedPerson":
			case "RequestGroup":
			case "ResearchSubject":
			case "RiskAssessment":
			case "Schedule":
			case "ServiceRequest":
			case "Specimen":
			case "SupplyDelivery":
			case "SupplyRequest":
			case "VisionPrescription":
				return true;
			default:
				return false;
		}
	}

	public String getPatientSearchParam(String theDataType) {
		switch (theDataType) {
			case "Account":
				return "subject";
			case "AdverseEvent":
				return "subject";
			case "AllergyIntolerance":
				return "patient";
			case "Appointment":
				return "actor";
			case "AppointmentResponse":
				return "actor";
			case "AuditEvent":
				return "patient";
			case "Basic":
				return "patient";
			case "BodyStructure":
				return "patient";
			case "CarePlan":
				return "patient";
			case "CareTeam":
				return "patient";
			case "ChargeItem":
				return "subject";
			case "Claim":
				return "patient";
			case "ClaimResponse":
				return "patient";
			case "ClinicalImpression":
				return "subject";
			case "Communication":
				return "subject";
			case "CommunicationRequest":
				return "subject";
			case "Composition":
				return "subject";
			case "Condition":
				return "patient";
			case "Consent":
				return "patient";
			case "Coverage":
				return "beneficiary";
			case "DetectedIssue":
				return "patient";
			case "DeviceRequest":
				return "subject";
			case "DeviceUseStatement":
				return "subject";
			case "DiagnosticReport":
				return "subject";
			case "DocumentManifest":
				return "subject";
			case "DocumentReference":
				return "subject";
			case "Encounter":
				return "patient";
			case "EnrollmentRequest":
				return "subject";
			case "EpisodeOfCare":
				return "patient";
			case "ExplanationOfBenefit":
				return "patient";
			case "FamilyMemberHistory":
				return "patient";
			case "Flag":
				return "patient";
			case "Goal":
				return "patient";
			case "Group":
				return "member";
			case "ImagingStudy":
				return "patient";
			case "Immunization":
				return "patient";
			case "ImmunizationRecommendation":
				return "patient";
			case "Invoice":
				return "subject";
			case "List":
				return "subject";
			case "MeasureReport":
				return "patient";
			case "Media":
				return "subject";
			case "MedicationAdministration":
				return "patient";
			case "MedicationDispense":
				return "patient";
			case "MedicationRequest":
				return "subject";
			case "MedicationStatement":
				return "subject";
			case "MolecularSequence":
				return "patient";
			case "NutritionOrder":
				return "patient";
			case "Observation":
				return "subject";
			case "Patient":
				return "_id";
			case "Person":
				return "patient";
			case "Procedure":
				return "patient";
			case "Provenance":
				return "patient";
			case "QuestionnaireResponse":
				return "subject";
			case "RelatedPerson":
				return "patient";
			case "RequestGroup":
				return "subject";
			case "ResearchSubject":
				return "individual";
			case "RiskAssessment":
				return "subject";
			case "Schedule":
				return "actor";
			case "ServiceRequest":
				return "patient";
			case "Specimen":
				return "subject";
			case "SupplyDelivery":
				return "patient";
			case "SupplyRequest":
				return "subject";
			case "VisionPrescription":
				return "patient";

			default:
				return null;
		}
	}
}
