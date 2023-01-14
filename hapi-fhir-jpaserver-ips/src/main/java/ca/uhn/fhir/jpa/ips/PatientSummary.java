package ca.uhn.fhir.jpa.ips;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeChildChoiceDefinition;
import ca.uhn.fhir.context.RuntimeChildNarrativeDefinition;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.narrative.CustomThymeleafNarrativeGenerator;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.i18n.Msg;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Narrative;
import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.ClinicalImpression;
import org.hl7.fhir.r4.model.Consent;
import org.hl7.fhir.r4.model.DeviceUseStatement;
import org.hl7.fhir.r4.model.MedicationStatement;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.MedicationAdministration;
import org.hl7.fhir.r4.model.MedicationDispense;
import org.hl7.fhir.r4.model.Immunization;        
import org.hl7.fhir.r4.model.Procedure;        
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Observation.ObservationStatus;

import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.DomainResource;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.ResourceType;
import org.hl7.fhir.r4.model.UriType;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.hibernate.type.CustomType;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.INarrative;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.time.format.DateTimeFormatter;
import java.lang.reflect.Field;
import java.time.LocalDate;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

public class PatientSummary {

	private enum IPSSection {
		ALLERGY_INTOLERANCE,
		MEDICATION_SUMMARY,
		PROBLEM_LIST,
		IMMUNIZATIONS,
		PROCEDURES,
		MEDICAL_DEVICES,
		DIAGNOSTIC_RESULTS,
		VITAL_SIGNS,
		ILLNESS_HISTORY,
		PREGNANCY,
		SOCIAL_HISTORY,
		FUNCTIONAL_STATUS,
		PLAN_OF_CARE,
		ADVANCE_DIRECTIVES
	}

	private static final Map<IPSSection, Map<String, String>> SectionText = Map.ofEntries(
		Map.entry(IPSSection.ALLERGY_INTOLERANCE, Map.of("title", "Allergies and Intolerances", "code", "48765-2", "display", "Allergies and Adverse Reactions")),
		Map.entry(IPSSection.MEDICATION_SUMMARY, Map.of("title", "Medication List", "code", "10160-0", "display", "Medication List")),
		Map.entry(IPSSection.PROBLEM_LIST, Map.of("title", "Problem List", "code", "11450-4", "display", "Problem List")),
		Map.entry(IPSSection.IMMUNIZATIONS, Map.of("title", "History of Immunizations", "code", "11369-6", "display", "History of Immunizations")),
		Map.entry(IPSSection.PROCEDURES, Map.of("title", "History of Procedures", "code", "47519-4", "display", "History of Procedures")),
		Map.entry(IPSSection.MEDICAL_DEVICES, Map.of("title", "Medical Devices", "code", "46240-8", "display", "Medical Devices")),
		Map.entry(IPSSection.DIAGNOSTIC_RESULTS, Map.of("title", "Diagnostic Results", "code", "30954-2", "display", "Diagnostic Results")),
		Map.entry(IPSSection.VITAL_SIGNS, Map.of("title", "Vital Signs", "code", "8716-3", "display", "Vital Signs")),
		Map.entry(IPSSection.PREGNANCY, Map.of("title", "Pregnancy Information", "code", "11362-0", "display", "Pregnancy Information")),
		Map.entry(IPSSection.SOCIAL_HISTORY, Map.of("title", "Social History", "code", "29762-2", "display", "Social History")),
		Map.entry(IPSSection.ILLNESS_HISTORY, Map.of("title", "History of Past Illness", "code", "11348-0", "display", "History of Past Illness")),
		Map.entry(IPSSection.FUNCTIONAL_STATUS, Map.of("title", "Functional Status", "code", "47420-5", "display", "Functional Status")),
		Map.entry(IPSSection.PLAN_OF_CARE, Map.of("title", "Plan of Care", "code", "18776-5", "display", "Plan of Care")),
		Map.entry(IPSSection.ADVANCE_DIRECTIVES, Map.of("title", "Advance Directives", "code", "42349-0", "display", "Advance Directives"))
	);

	private static final Map<IPSSection, List<ResourceType>> SectionTypes = Map.ofEntries(
		Map.entry(IPSSection.ALLERGY_INTOLERANCE, List.of(ResourceType.AllergyIntolerance)),
		Map.entry(IPSSection.MEDICATION_SUMMARY, List.of(ResourceType.MedicationStatement, ResourceType.MedicationRequest)),
		Map.entry(IPSSection.PROBLEM_LIST, List.of(ResourceType.Condition)),
		Map.entry(IPSSection.IMMUNIZATIONS, List.of(ResourceType.Immunization)),
		Map.entry(IPSSection.PROCEDURES, List.of(ResourceType.Procedure)),
		Map.entry(IPSSection.MEDICAL_DEVICES, List.of(ResourceType.DeviceUseStatement)),
		Map.entry(IPSSection.DIAGNOSTIC_RESULTS, List.of(ResourceType.DiagnosticReport, ResourceType.Observation)),
		Map.entry(IPSSection.VITAL_SIGNS, List.of(ResourceType.Observation)),
		Map.entry(IPSSection.PREGNANCY, List.of(ResourceType.Observation)),
		Map.entry(IPSSection.SOCIAL_HISTORY, List.of(ResourceType.Observation)),
		Map.entry(IPSSection.ILLNESS_HISTORY, List.of(ResourceType.Condition)),
		Map.entry(IPSSection.FUNCTIONAL_STATUS, List.of(ResourceType.ClinicalImpression)),
		Map.entry(IPSSection.PLAN_OF_CARE, List.of(ResourceType.CarePlan)),
		Map.entry(IPSSection.ADVANCE_DIRECTIVES, List.of(ResourceType.Consent))
	);

	private static final List<String> PregnancyCodes = List.of("82810-3", "11636-8", "11637-6", "11638-4", "11639-2", "11640-0", "11612-9", "11613-7", "11614-5", "33065-4");

	// Could not locate these profiles so tried to follow the pattern of the other profiles
	private static final Map<IPSSection, String> SectionProfiles = Map.ofEntries(
		Map.entry(IPSSection.ALLERGY_INTOLERANCE, "http://hl7.org/fhir/uv/ips/StructureDefinition/AllergiesAndIntolerances-uv-ips"),
		Map.entry(IPSSection.MEDICATION_SUMMARY, "http://hl7.org/fhir/uv/ips/StructureDefinition/MedicationSummary-uv-ips"),
		Map.entry(IPSSection.PROBLEM_LIST, "http://hl7.org/fhir/uv/ips/StructureDefinition/ProblemList-uv-ips"),
		Map.entry(IPSSection.IMMUNIZATIONS, "http://hl7.org/fhir/uv/ips/StructureDefinition/Immunizations-uv-ips"),
		Map.entry(IPSSection.PROCEDURES, "http://hl7.org/fhir/uv/ips/StructureDefinition/HistoryOfProcedures-uv-ips"),
		Map.entry(IPSSection.MEDICAL_DEVICES, "http://hl7.org/fhir/uv/ips/StructureDefinition/MedicalDevices-uv-ips"),
		Map.entry(IPSSection.DIAGNOSTIC_RESULTS, "http://hl7.org/fhir/uv/ips/StructureDefinition/DiagnosticResults-uv-ips"),
		Map.entry(IPSSection.VITAL_SIGNS, "http://hl7.org/fhir/uv/ips/StructureDefinition/VitalSigns-uv-ips"),
		Map.entry(IPSSection.PREGNANCY, "http://hl7.org/fhir/uv/ips/StructureDefinition/Pregnancy-uv-ips"),
		Map.entry(IPSSection.SOCIAL_HISTORY, "http://hl7.org/fhir/uv/ips/StructureDefinition/SocialHistory-uv-ips"),
		Map.entry(IPSSection.ILLNESS_HISTORY, "http://hl7.org/fhir/uv/ips/StructureDefinition/PastHistoryOfIllnesses-uv-ips"),
		Map.entry(IPSSection.FUNCTIONAL_STATUS, "http://hl7.org/fhir/uv/ips/StructureDefinition/FunctionalStatus-uv-ips"),
		Map.entry(IPSSection.PLAN_OF_CARE, "http://hl7.org/fhir/uv/ips/StructureDefinition/PlanOfCare-uv-ips"),
		Map.entry(IPSSection.ADVANCE_DIRECTIVES, "http://hl7.org/fhir/uv/ips/StructureDefinition/AdvanceDirectives-uv-ips")
	);

	public static Bundle buildFromSearch(IBundleProvider searchSet, FhirContext ctx) {			
		List<Resource> searchResources = createResourceList(searchSet.getAllResources());
                // For sequential ids on an UUID indexed server, 404s are not returned for empty search results
                // The following prevents a Java error inside this operation but changes could also be made upstream at IdHelperService 
                // See hapi-fhir-jpaserver-base/src/main/java/ca/uhn/fhir/jpa/dao/index/IdHelperService.java 
		if (searchResources.isEmpty()) {
                        throw new ResourceNotFoundException(Msg.code(2001) + "Resource is not known"); 
      }
      
		Patient patient = (Patient) searchResources.get(0);
		Organization author = createAuthor();
		Composition composition = createIPSComposition(patient, author);

		ImmutablePair<List<Resource>, HashMap<IPSSection, List<Resource>>> resourceTuple = buildResourceTuple(searchResources, ctx, composition, patient);
		List<Resource> resources = resourceTuple.getLeft();
		HashMap<IPSSection, List<Resource>> sectionPrimaries = resourceTuple.getRight();

		HashMap<IPSSection, String> sectionNarratives = createNarratives(sectionPrimaries, resources, ctx);
		composition = addIPSSections(composition, sectionPrimaries, sectionNarratives);

		Bundle bundle = createIPSBundle(composition, resources, author);

		return bundle;
	}

	private static Bundle createIPSBundle(Composition composition, List<Resource> resources, Organization author) {
		Identifier iden = new Identifier();
		iden.setSystem("urn:ietf:rfc:4122");
		iden.setValue(UUID.randomUUID().toString());

		Bundle bundle = new Bundle();
		bundle.setType(BundleType.DOCUMENT)
			.setTimestamp(new Date())
			.setIdentifier(iden);
			
		bundle.addEntry().setResource(composition).setFullUrl(formatAsUrn(composition));
		for (Resource resource : resources) {
			bundle.addEntry().setResource(resource).setFullUrl(formatAsUrn(resource));
		}
		bundle.addEntry().setResource(author).setFullUrl(formatAsUrn(author));
		return bundle;
	}

	private static Organization createAuthor() {
		Organization organization = new Organization();
		organization.setName("eHealthLab - University of Cyprus")
			.setAddress(List.of( new Address()
				.addLine("1 University Avenue")   
				.setCity("Nicosia")
				.setPostalCode("2109")
				.setCountry("CY")))
			.setId(IdDt.newRandomUuid());
		return organization;
	}

	private static List<Resource> createResourceList(List<IBaseResource> iBaseResourceList) {
		List<Resource> resourceList = new ArrayList<Resource>();
		for (IBaseResource ibaseResource : iBaseResourceList) {
			resourceList.add((Resource) ibaseResource);
		}
		return resourceList;
	}

	private static ImmutablePair<List<Resource>, HashMap<IPSSection, List<Resource>>> buildResourceTuple(List<Resource> searchResources, FhirContext ctx, Composition composition, Patient patient) {
		HashMap<IPSSection, List<Resource>> initialHashedPrimaries = hashPrimaries(searchResources);
		List<Resource> expandedResources = addNoInfoResources(searchResources, initialHashedPrimaries, patient);
		HashMap<IPSSection, List<Resource>> hashedExpandedPrimaries = hashPrimaries(expandedResources);
		List<Resource> linkedResources = addLinkToResources(expandedResources, hashedExpandedPrimaries, composition);
		HashMap<IPSSection, List<Resource>> hashedPrimaries = hashPrimaries(linkedResources);
		HashMap<IPSSection, List<Resource>> filteredPrimaries = filterPrimaries(hashedPrimaries);
		List<Resource> resources = pruneResources(patient, linkedResources, filteredPrimaries, ctx);

		return new ImmutablePair<List<Resource>, HashMap<IPSSection, List<Resource>>>(resources, filteredPrimaries);
	}

	private static HashMap<IPSSection, List<Resource>> hashPrimaries(List<Resource> resourceList) {
		HashMap<IPSSection, List<Resource>> iPSResourceMap = new HashMap<IPSSection, List<Resource>>();

		for (Resource resource : resourceList) {
			for (IPSSection iPSSection : IPSSection.values()) {
				if ( SectionTypes.get(iPSSection).contains(resource.getResourceType()) ) {
					if ( !(resource.getResourceType() == ResourceType.Observation) || isObservationinSection(iPSSection, (Observation) resource)) {
						if (iPSResourceMap.get(iPSSection) == null) {
							iPSResourceMap.put(iPSSection, new ArrayList<Resource>());
						}
						iPSResourceMap.get(iPSSection).add(resource);
					}
				}
			}
		}

		return iPSResourceMap;
	}

	private static List<Resource> addNoInfoResources(List<Resource> resources,  HashMap<IPSSection, List<Resource>> sectionPrimaries, Patient patient) {

		if (sectionPrimaries.get(IPSSection.ALLERGY_INTOLERANCE) == null) {
			AllergyIntolerance noInfoAllergies = noInfoAllergies(patient);
			resources.add(noInfoAllergies);
		}

		if (sectionPrimaries.get(IPSSection.MEDICATION_SUMMARY) == null) {
			MedicationStatement noInfoMedications = noInfoMedications(patient);
			resources.add(noInfoMedications);
		}

		if (sectionPrimaries.get(IPSSection.PROBLEM_LIST) == null) {
			Condition noInfoProblems = noInfoProblems(patient);
			resources.add(noInfoProblems);
		}

		return resources;
	}

	private static HashMap<IPSSection, List<Resource>> filterPrimaries(HashMap<IPSSection, List<Resource>> sectionPrimaries) {
		HashMap<IPSSection, List<Resource>> filteredPrimaries = new HashMap<IPSSection, List<Resource>>();
		for ( IPSSection section : sectionPrimaries.keySet() ) {
			List<Resource> filteredList = new ArrayList<Resource>();
			for (Resource resource : sectionPrimaries.get(section)) {
				if (passesFilter(section, resource)) {
					filteredList.add(resource);
				}
			}
			if (filteredList.size() > 0) {
				filteredPrimaries.put(section, filteredList);
			}
		}
		return filteredPrimaries;
	}

	private static List<Resource> pruneResources(Patient patient, List<Resource> resources,  HashMap<IPSSection, List<Resource>> sectionPrimaries, FhirContext ctx) {		
		List<String> resourceIds = new ArrayList<String>();
		List<String> followedIds = new ArrayList<String>();

		HashMap<String, Resource> resourcesById = new HashMap<String, Resource>();
		for (Resource resource : resources) {
			resourcesById.put(resource.getIdElement().getIdPart(), resource);
		}
		String patientId = patient.getIdElement().getIdPart();
		resourcesById.put(patientId, patient);
		
		recursivePrune(patientId, resourceIds, followedIds, resourcesById, ctx);

		for (IPSSection section : sectionPrimaries.keySet()) {
			for (Resource resource : sectionPrimaries.get(section)) {
				String resourceId = resource.getIdElement().getIdPart();
				recursivePrune(resourceId, resourceIds, followedIds, resourcesById, ctx);
			}
		}

		List<Resource> prunedResources = new ArrayList<Resource>();

		for (Resource resource : resources) {
			if (resourceIds.contains(resource.getIdElement().getIdPart())) {
				prunedResources.add(resource);
			}
		}

		return prunedResources;
	}

	private static Void recursivePrune(String resourceId, List<String> resourceIds,  List<String> followedIds, HashMap<String, Resource> resourcesById, FhirContext ctx) {
		if (!resourceIds.contains(resourceId)) {
			resourceIds.add(resourceId);
		}

		Resource resource = resourcesById.get(resourceId);
		if (resource != null) {
			ctx.newTerser().getAllResourceReferences(resource).stream()
			.map( r -> r.getResourceReference().getReferenceElement().getIdPart() )
			.forEach( id ->  {
				if (!followedIds.contains(id)) {
					followedIds.add(id);
					recursivePrune(id, resourceIds, followedIds, resourcesById, ctx);
				}
			});
		}

		return null;
	}

	private static List<Resource> addLinkToResources(List<Resource> resources, HashMap<IPSSection, List<Resource>> sectionPrimaries, Composition composition) {
		List<Resource> linkedResources = new ArrayList<Resource>();
		HashMap<String, String> valueUrls = new HashMap<String, String>();
		
		String url = "http://hl7.org/fhir/StructureDefinition/NarrativeLink";
		String valueUrlBase = composition.getId() + "#"; 
		
		for (IPSSection section : sectionPrimaries.keySet()) {
			String profile = SectionProfiles.get(section);
			String[] arr = profile.split("/");
			String profileName = arr[arr.length - 1];
			String sectionValueUrlBase = valueUrlBase + profileName.split("-uv-")[0];

			for (Resource resource : sectionPrimaries.get(section)) {
				String valueUrl = sectionValueUrlBase + "-" + resource.getIdElement().getIdPart();
				valueUrls.put(resource.getIdElement().getIdPart(), valueUrl);
			}
		}

		for (Resource resource : resources) {
			if (valueUrls.containsKey(resource.getIdElement().getIdPart())) {
				String valueUrl = valueUrls.get(resource.getIdElement().getIdPart());
				Extension extension = new Extension();
				extension.setUrl(url);
				extension.setValue(new UriType(valueUrl));
				DomainResource domainResource = (DomainResource) resource;
				domainResource.addExtension(extension);
				resource = (Resource) domainResource;
			}
			linkedResources.add(resource);
		}

		return linkedResources;
	} 

	private static HashMap<IPSSection, String> createNarratives(HashMap<IPSSection, List<Resource>> sectionPrimaries, List<Resource> resources, FhirContext ctx) {
		HashMap<IPSSection, String> hashedNarratives = new HashMap<IPSSection, String>();

		for (IPSSection section : sectionPrimaries.keySet()) {
			String narrative = createSectionNarrative(section, resources, ctx);
			hashedNarratives.put(section, narrative);
		}

		return hashedNarratives;
	}

	private static String createSectionNarrative(IPSSection iPSSection, List<Resource> resources, FhirContext ctx) {
		// Use the narrative generator
		String NARRATIVES_PROPERTIES = "classpath:ca/uhn/fhir/narrative/narratives.properties";
		String HAPISERVER_NARRATIVES_PROPERTIES = "classpath:ca/uhn/fhir/narrative/narratives-hapiserver.properties";
		CustomThymeleafNarrativeGenerator generator = new CustomThymeleafNarrativeGenerator("classpath:narrative/ips_narratives.properties",NARRATIVES_PROPERTIES,HAPISERVER_NARRATIVES_PROPERTIES);

		// ctx.setNarrativeGenerator(new CustomThymeleafNarrativeGenerator("classpath:narrative/ips_narratives.properties"));
		//ctx.setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());
		ctx.setNarrativeGenerator(generator);
		// Create a bundle to hold the resources
		Bundle bundle = new Bundle();
		Composition composition = new Composition();

		bundle.addEntry().setResource(composition);
		for (Resource resource : resources) {
			bundle.addEntry().setResource(resource);
		}

		String profile = SectionProfiles.get(iPSSection);
		bundle.setMeta(new Meta().addProfile(profile));
		

		// Generate the narrative
		//CustomThymeleafNarrativeGenerator generator = new CustomThymeleafNarrativeGenerator("classpath:narrative/ips_narratives.properties");
		//DefaultThymeleafNarrativeGenerator generator = new DefaultThymeleafNarrativeGenerator();
		generator.populateResourceNarrative(ctx, bundle);
		
		// Get the narrative
		String narrative = composition.getText().getDivAsString();
		
		return narrative;
	}

	private static Boolean passesFilter(IPSSection section, Resource resource) {
		if (section == IPSSection.ALLERGY_INTOLERANCE) {
                    if (resource.getResourceType() == ResourceType.AllergyIntolerance) {
                        AllergyIntolerance alint = (AllergyIntolerance) resource;
                        if (!alint.getClinicalStatus().hasCoding("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical", "inactive")
                         && !alint.getClinicalStatus().hasCoding("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical", "resolved")
                         && !alint.getVerificationStatus().hasCoding("http://terminology.hl7.org/CodeSystem/allergyintolerance-verification", "entered-in-error")) {
                            return true;
                        } else {
                            return false;
                        }
                    } else if (resource.getResourceType() == ResourceType.DocumentReference) {
                        // no DocumentReference filtering yet
                        return true;
                    } else {
                        return false;
                    }                                                        
		}
		if (section == IPSSection.MEDICATION_SUMMARY) {
                        if (resource.getResourceType() == ResourceType.MedicationStatement) {
                            MedicationStatement medstat = (MedicationStatement) resource;
                            if (medstat.getStatus() == MedicationStatement.MedicationStatementStatus.ACTIVE
                             || medstat.getStatus() == MedicationStatement.MedicationStatementStatus.INTENDED
                             || medstat.getStatus() == MedicationStatement.MedicationStatementStatus.UNKNOWN
                             || medstat.getStatus() == MedicationStatement.MedicationStatementStatus.ONHOLD) {
                                return true;
                            } else {
                                return false;
                            }                            
                        } else if (resource.getResourceType() == ResourceType.MedicationRequest) {
                            MedicationRequest medreq = (MedicationRequest) resource;
                            if (medreq.getStatus() == MedicationRequest.MedicationRequestStatus.ACTIVE
                             || medreq.getStatus() == MedicationRequest.MedicationRequestStatus.UNKNOWN
                             || medreq.getStatus() == MedicationRequest.MedicationRequestStatus.ONHOLD) {
                                return true;
                            } else {
                                return false;
                            }                                                        
                        } else if (resource.getResourceType() == ResourceType.MedicationAdministration) {
                            MedicationAdministration medadmin = (MedicationAdministration) resource;
                            if (medadmin.getStatus() == MedicationAdministration.MedicationAdministrationStatus.INPROGRESS
                             || medadmin.getStatus() == MedicationAdministration.MedicationAdministrationStatus.UNKNOWN
                             || medadmin.getStatus() == MedicationAdministration.MedicationAdministrationStatus.ONHOLD) {
                                return true;
                            } else {
                                return false;
                            }                                                        
                        } else if (resource.getResourceType() == ResourceType.MedicationDispense) {
                            MedicationDispense meddisp = (MedicationDispense) resource;
                            if (meddisp.getStatus() == MedicationDispense.MedicationDispenseStatus.INPROGRESS
                             || meddisp.getStatus() == MedicationDispense.MedicationDispenseStatus.UNKNOWN
                             || meddisp.getStatus() == MedicationDispense.MedicationDispenseStatus.ONHOLD) {
                                return true;
                            } else {
                                return false;
                            }                                                        
                        } else if (resource.getResourceType() == ResourceType.DocumentReference) {
                            // no DocumentReference filtering yet
                            return true;
                        } else {
                            return false;
                        }                                                        
		}
		if (section == IPSSection.PROBLEM_LIST) {
                    if (resource.getResourceType() == ResourceType.Condition) {
                        Condition prob = (Condition) resource;
                        if (!prob.getClinicalStatus().hasCoding("http://terminology.hl7.org/CodeSystem/condition-clinical", "inactive")
                         && !prob.getClinicalStatus().hasCoding("http://terminology.hl7.org/CodeSystem/condition-clinical", "resolved")
                         && !prob.getVerificationStatus().hasCoding("http://terminology.hl7.org/CodeSystem/condition-ver-status", "entered-in-error")) {
                            return true;
                        } else {
                            return false;
                        }
                    } else if (resource.getResourceType() == ResourceType.DocumentReference) {
                        // no DocumentReference filtering yet
                        return true;
                    } else {
                        return false;
                    }                                                        
		}
		if (section == IPSSection.IMMUNIZATIONS) {
                    if (resource.getResourceType() == ResourceType.Immunization) {
                        Immunization immun = (Immunization) resource;
                        if (immun.getStatus() != Immunization.ImmunizationStatus.ENTEREDINERROR) {
                            return true;
                        } else {
                            return false;
                        }                            
                    } else if (resource.getResourceType() == ResourceType.DocumentReference) {
                        // no DocumentReference filtering yet
                        return true;
                    } else {
                        return false;
                    }                                                        
		}
		if (section == IPSSection.PROCEDURES) {
      if (resource.getResourceType() == ResourceType.Procedure) {
        Procedure proc = (Procedure) resource;
        if (proc.getStatus() != Procedure.ProcedureStatus.ENTEREDINERROR
         && proc.getStatus() != Procedure.ProcedureStatus.NOTDONE) {
            return true;
        } 
        else {
            return false;
        }
      }
      else {
        return false;
      } 
		}
		if (section == IPSSection.MEDICAL_DEVICES) {
			if (resource.getResourceType() == ResourceType.DeviceUseStatement) {
        DeviceUseStatement devuse = (DeviceUseStatement) resource;
        if (devuse.getStatus() != DeviceUseStatement.DeviceUseStatementStatus.ENTEREDINERROR) {
            return true;
        } 
        else {
            return false;
        }
      } 
      else {
        return false;
      } 
    }
		if (section == IPSSection.DIAGNOSTIC_RESULTS) {
			if (resource.getResourceType() == ResourceType.DiagnosticReport) {
        return true;
      }
      else if (resource.getResourceType() == ResourceType.Observation) {
        // code filtering not yet applied
        Observation observation = (Observation) resource;
        return (observation.getStatus() != ObservationStatus.PRELIMINARY);
      }
      else {
        return false;
      }
		}
		if (section == IPSSection.VITAL_SIGNS) {
      if (resource.getResourceType() == ResourceType.Observation) {
        // code filtering not yet applied
        return true;
      }
      else {
        return false;
      }
		}
		if (section == IPSSection.PREGNANCY) {
			Observation observation = (Observation) resource;
			return (observation.getStatus() != ObservationStatus.PRELIMINARY);
		}
		if (section == IPSSection.SOCIAL_HISTORY) {
			Observation observation = (Observation) resource;
			return (observation.getStatus() != ObservationStatus.PRELIMINARY);
		}
		if (section == IPSSection.ILLNESS_HISTORY) {
      Condition prob = (Condition) resource;
      if (prob.getVerificationStatus().hasCoding("http://terminology.hl7.org/CodeSystem/condition-ver-status", "entered-in-error")) {
        return false;
      }
      else if (prob.getClinicalStatus().hasCoding("http://terminology.hl7.org/CodeSystem/condition-clinical", "inactive")
       || prob.getClinicalStatus().hasCoding("http://terminology.hl7.org/CodeSystem/condition-clinical", "resolved")
       || prob.getClinicalStatus().hasCoding("http://terminology.hl7.org/CodeSystem/condition-clinical", "remission")){
        return true;
       }
      else {
        return false;
      }
		}
		if (section == IPSSection.FUNCTIONAL_STATUS) {
      ClinicalImpression clinimp = (ClinicalImpression) resource;
      if (clinimp.getStatus() != ClinicalImpression.ClinicalImpressionStatus.INPROGRESS
       && clinimp.getStatus() != ClinicalImpression.ClinicalImpressionStatus.ENTEREDINERROR) {
        return true;
      }
      else {
        return false;
      }
		}
		if (section == IPSSection.PLAN_OF_CARE) {
			CarePlan carep = (CarePlan) resource;
      if (carep.getStatus() == CarePlan.CarePlanStatus.ACTIVE
       || carep.getStatus() == CarePlan.CarePlanStatus.ONHOLD
       || carep.getStatus() == CarePlan.CarePlanStatus.UNKNOWN) {
        return true;
      }
      else {
        return false;
      }
		}
		if (section == IPSSection.ADVANCE_DIRECTIVES) {
      Consent advdir = (Consent) resource;
      if (advdir.getStatus() == Consent.ConsentState.ACTIVE) {
        return true;
      }
      else {
        return false;
      }
		}
		return false;
	}

	private static Composition createIPSComposition(Patient patient, Organization author) {
		Composition composition = new Composition();
		composition.setStatus(Composition.CompositionStatus.FINAL)
			.setType(new CodeableConcept().addCoding(new Coding().setCode("60591-5").setSystem("http://loinc.org").setDisplay("Patient Summary Document")))
			.setSubject(new Reference(patient))
			.setDate(new Date())
			.setTitle("Patient Summary as of " + DateTimeFormatter.ofPattern("MM/dd/yyyy").format(LocalDate.now()))
			.setConfidentiality(Composition.DocumentConfidentiality.N)
			.setAuthor(List.of(new Reference(author)))
			// .setCustodian(new Reference(organization))
			// .setRelatesTo(List.of(new Composition.RelatedComponent().setType(Composition.RelatedTypeEnum.SUBJECT).setTarget(new Reference(patient))))
			// .setEvent(List.of(new Composition.EventComponent().setCode(new CodeableConcept().addCoding(new Coding().setCode("PCPR").setSystem("http://terminology.hl7.org/CodeSystem/v3-ActClass").setDisplay("")))))
			.setId(IdDt.newRandomUuid());
		return composition;
	}

	private static Composition addIPSSections(Composition composition, HashMap<IPSSection, List<Resource>> sectionPrimaries, HashMap<IPSSection, String> hashedNarratives) {
		// Add sections
		for (IPSSection iPSSection : IPSSection.values()) {
			if (sectionPrimaries.get(iPSSection) != null && sectionPrimaries.get(iPSSection).size() > 0) {
				Composition.SectionComponent section = createSection(SectionText.get(iPSSection), sectionPrimaries.get(iPSSection), hashedNarratives.get(iPSSection));
				composition.addSection(section);
			}
		}
		return composition;
	}

	private static Composition.SectionComponent createSection(Map<String, String> text, List<Resource> resources, String narrative) {
		Composition.SectionComponent section = new Composition.SectionComponent();
		
		section.setTitle(text.get("title"))
			.setCode(new CodeableConcept().addCoding(new Coding().setSystem("http://loinc.org")
			.setCode(text.get("code")).setDisplay(text.get("display"))))
			.getText().setStatus(Narrative.NarrativeStatus.GENERATED).setDivAsString(narrative);
		
		HashMap<ResourceType, List<Resource>> resourcesByType = new HashMap<ResourceType, List<Resource>>();
		
		for (Resource resource : resources) {
			if ( !resourcesByType.containsKey(resource.getResourceType()) ) {
				resourcesByType.put(resource.getResourceType(), new ArrayList<Resource>());
			}
			resourcesByType.get(resource.getResourceType()).add(resource);
		}
		
		for (List<Resource> resourceList : resourcesByType.values()) {	
			for (Resource resource : resourceList) {
				section.addEntry(new Reference(resource));
			}
		}

		return section;
	}

	private static AllergyIntolerance noInfoAllergies(Patient patient) {
		AllergyIntolerance allergy = new AllergyIntolerance();
		allergy.setCode(new CodeableConcept().addCoding(new Coding().setCode("no-allergy-info").setSystem("http://hl7.org/fhir/uv/ips/CodeSystem/absent-unknown-uv-ips").setDisplay("No information about allergies")))
			.setPatient(new Reference(patient))
			.setClinicalStatus(new CodeableConcept().addCoding(new Coding().setCode("active").setSystem("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical")))
			.setId(IdDt.newRandomUuid());
		return allergy;
	}

	private static MedicationStatement noInfoMedications(Patient patient) {
		MedicationStatement medication = new MedicationStatement();
		// setMedicationCodeableConcept is not available
		medication.setMedication(new CodeableConcept().addCoding(new Coding().setCode("no-medication-info").setSystem("http://hl7.org/fhir/uv/ips/CodeSystem/absent-unknown-uv-ips").setDisplay("No information about medications")))
			.setSubject(new Reference(patient))
			.setStatus(MedicationStatement.MedicationStatementStatus.UNKNOWN)
			// .setEffective(new Period().addExtension().setUrl("http://hl7.org/fhir/StructureDefinition/data-absent-reason").setValue((new Coding().setCode("not-applicable"))))
			.setId(IdDt.newRandomUuid());
		return medication;
	}

	private static Condition noInfoProblems(Patient patient) {
		Condition condition = new Condition();
		condition.setCode(new CodeableConcept().addCoding(new Coding().setCode("no-problem-info").setSystem("http://hl7.org/fhir/uv/ips/CodeSystem/absent-unknown-uv-ips").setDisplay("No information about problems")))
			.setSubject(new Reference(patient))
			.setClinicalStatus(new CodeableConcept().addCoding(new Coding().setCode("active").setSystem("http://terminology.hl7.org/CodeSystem/condition-clinical")))
			.setId(IdDt.newRandomUuid());
		return condition;
	}

	private static Boolean isObservationinSection(IPSSection iPSSection, Observation observation) {
		Boolean inSection = false;
		
		switch(iPSSection) {
			case VITAL_SIGNS:
				if (observation.hasCategory() && hasSpecficCode(observation.getCategory(), "vital-signs")) {
					inSection = true;
				}
				break;
			case PREGNANCY:
				if (observation.hasCode() && hasPregnancyCode(observation.getCode())) {
					inSection = true;
				}
				break;
			case SOCIAL_HISTORY:
				if (observation.hasCategory() && hasSpecficCode(observation.getCategory(), "social-history")) {
					inSection = true;
				}
				break;
			case DIAGNOSTIC_RESULTS:
				if (observation.hasCategory() && hasSpecficCode(observation.getCategory(), "laboratory")) {
					inSection = true;
				}
				break;
			}
		return inSection;
	}

	private static boolean hasPregnancyCode(CodeableConcept concept) {
		for (Coding c : concept.getCoding()) {
			if (PregnancyCodes.contains(c.getCode()))
				return true;
		}
	   return false;
	}

	private static boolean hasSpecficCode(List<CodeableConcept> ccList, String code) {
	   for (CodeableConcept concept : ccList) {
			for (Coding c : concept.getCoding()) {
				if (code.equals(c.getCode()))
					return true;
			}
		}
	   return false;
	}
	
	private static String formatAsUrn(Resource resource) {
		if (resource.getIdElement().isUrn()) {
			return resource.getId();
		} else {
			return "urn:uuid:" + resource.getIdElement().getIdPart();
		}
	}
}
