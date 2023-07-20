package ca.uhn.hapi.fhir.cdshooks.svc.cr.discovery;

import ca.uhn.fhir.cr.common.IDaoRegistryUser;
import ca.uhn.fhir.cr.common.Searches;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceJson;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.DataRequirement;
import org.hl7.fhir.r5.model.Library;
import org.hl7.fhir.r5.model.PlanDefinition;
import org.hl7.fhir.r5.model.ValueSet;

import java.util.ArrayList;
import java.util.List;

public class DiscoveryResolutionR5 implements IDaoRegistryUser {

	private final String PATIENT_ID_CONTEXT = "{{context.patientId}}";
	private final int DEFAULT_MAX_URI_LENGTH = 8000;
	private int maxUriLength;

	private DaoRegistry daoRegistry;

	public DiscoveryResolutionR5(DaoRegistry daoRegistry) {
		this.daoRegistry = daoRegistry;
		this.maxUriLength = DEFAULT_MAX_URI_LENGTH;
	}

	@Override
	public DaoRegistry getDaoRegistry() {
		return this.daoRegistry;
	}

	public int getMaxUriLength() {
		return this.maxUriLength;
	}

	public void setMaxUriLength(int maxUriLength) {
		if (maxUriLength <= 0) {
			throw new IllegalArgumentException("maxUriLength must be >0");
		}

		this.maxUriLength = maxUriLength;
	}

	public boolean isEca(PlanDefinition planDefinition) {
		if (planDefinition.hasType() && planDefinition.getType().hasCoding()) {
			for (Coding coding : planDefinition.getType().getCoding()) {
				if (coding.getCode().equals("eca-rule")) {
					return true;
				}
			}
		}
		return false;
	}

	public Library resolvePrimaryLibrary(PlanDefinition planDefinition) {
		// The CPGComputablePlanDefinition profile limits the cardinality of library to 1
		Library library = null;
		if (planDefinition.hasLibrary() && !planDefinition.getLibrary().isEmpty()) {
			library = (Library) search(
				Library.class,
				Searches.byCanonical(planDefinition.getLibrary().get(0)))
				.iterator()
				.next();
		}
		return library;
	}

	public List<String> resolveValueCodingCodes(List<Coding> valueCodings) {
		List<String> result = new ArrayList<>();

		StringBuilder codes = new StringBuilder();
		for (Coding coding : valueCodings) {
			if (coding.hasCode()) {
				String system = coding.getSystem();
				String code = coding.getCode();

				codes = getCodesStringBuilder(result, codes, system, code);
			}
		}

		result.add(codes.toString());
		return result;
	}

	public List<String> resolveValueSetCodes(String valueSetId) {
		ValueSet valueSet = (ValueSet) search(ValueSet.class, Searches.byCanonical(valueSetId)).iterator().next();
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

	private StringBuilder getCodesStringBuilder(List<String> ret, StringBuilder codes, String system, String code) {
		String codeToken = system + "|" + code;
		int postAppendLength = codes.length() + codeToken.length();

		if (codes.length() > 0 && postAppendLength < this.maxUriLength) {
			codes.append(",");
		} else if (postAppendLength > this.maxUriLength) {
			ret.add(codes.toString());
			codes = new StringBuilder();
		}
		codes.append(codeToken);
		return codes;
	}

	public List<String> createRequestUrl(DataRequirement dataRequirement) {
		if (!isPatientCompartment(dataRequirement.getType().toCode())) return null;
		String patientRelatedResource = dataRequirement.getType() + "?"
			+ getPatientSearchParam(dataRequirement.getType().toCode())
			+ "=Patient/" + PATIENT_ID_CONTEXT;
		List<String> ret = new ArrayList<>();
		if (dataRequirement.hasCodeFilter()) {
			for (DataRequirement.DataRequirementCodeFilterComponent codeFilterComponent :
				dataRequirement.getCodeFilter()) {
				if (!codeFilterComponent.hasPath()) continue;
				String path = mapCodePathToSearchParam(dataRequirement.getType().toCode(), codeFilterComponent.getPath());
				if (codeFilterComponent.hasValueSetElement()) {
					for (String codes : resolveValueSetCodes(codeFilterComponent.getValueSet())) {
						ret.add(patientRelatedResource + "&" + path + "=" + codes);
					}
				} else if (codeFilterComponent.hasCode()) {
					List<Coding> codeFilterValueCodings = codeFilterComponent.getCode();
					boolean isFirstCodingInFilter = true;
					for (String code : resolveValueCodingCodes(codeFilterValueCodings)) {
						if (isFirstCodingInFilter) {
							ret.add(patientRelatedResource + "&" + path + "=" + code);
						} else {
							ret.add("," + code);
						}

						isFirstCodingInFilter = false;
					}
				}
			}
			return ret;
		} else {
			ret.add(patientRelatedResource);
			return ret;
		}
	}

	public PrefetchUrlList getPrefetchUrlList(PlanDefinition planDefinition) {
		PrefetchUrlList prefetchList = new PrefetchUrlList();
		if (planDefinition == null) return null;
		if (!isEca(planDefinition)) return null;
		Library library = resolvePrimaryLibrary(planDefinition);
		// TODO: resolve data requirements
		if (library == null || !library.hasDataRequirement()) return null;
		for (DataRequirement dataRequirement : library.getDataRequirement()) {
			List<String> requestUrls = createRequestUrl(dataRequirement);
			if (requestUrls != null) {
				prefetchList.addAll(requestUrls);
			}
		}
		return prefetchList;
	}

	public CdsServiceJson resolveService(PlanDefinition planDefinition) {
		return new DiscoveryElementR5(planDefinition, getPrefetchUrlList(planDefinition)).getCdsServiceJson();
	}

	private String mapCodePathToSearchParam(String dataType, String path) {
		switch (dataType) {
			case "MedicationAdministration":
				if (path.equals("medication")) return "code";
				break;
			case "MedicationDispense":
				if (path.equals("medication")) return "code";
				break;
			case "MedicationRequest":
				if (path.equals("medication")) return "code";
				break;
			case "MedicationStatement":
				if (path.equals("medication")) return "code";
				break;
			default:
				if (path.equals("vaccineCode")) return "vaccine-code";
				break;
		}
		return path.replace('.', '-').toLowerCase();
	}

	public static boolean isPatientCompartment(String dataType) {
		switch (dataType) {
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

	public String getPatientSearchParam(String dataType) {
		switch (dataType) {
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
				return "policy-holder";
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
		}

		return null;
	}
}

