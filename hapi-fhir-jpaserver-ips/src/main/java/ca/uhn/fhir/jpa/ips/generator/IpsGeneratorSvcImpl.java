package ca.uhn.fhir.jpa.ips.generator;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.ips.api.IIpsGenerationStrategy;
import ca.uhn.fhir.jpa.ips.api.IpsContext;
import ca.uhn.fhir.jpa.ips.api.IpsSectionEnum;
import ca.uhn.fhir.jpa.ips.api.SectionRegistry;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.narrative.CustomThymeleafNarrativeGenerator;
import ca.uhn.fhir.narrative.INarrativeGenerator;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.CompositionBuilder;
import ca.uhn.fhir.util.ValidateUtil;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static ca.uhn.fhir.jpa.term.api.ITermLoaderSvc.LOINC_URI;

public class IpsGeneratorSvcImpl implements IIpsGeneratorSvc {

	public static final int CHUNK_SIZE = 10;
	private static final Logger ourLog = LoggerFactory.getLogger(IpsGeneratorSvcImpl.class);
	private final IIpsGenerationStrategy myGenerationStrategy;
	private final DaoRegistry myDaoRegistry;
	private final FhirContext myFhirContext;

	/**
	 * Constructor
	 */
	public IpsGeneratorSvcImpl(FhirContext theFhirContext, IIpsGenerationStrategy theGenerationStrategy, DaoRegistry theDaoRegistry) {
		myGenerationStrategy = theGenerationStrategy;
		myDaoRegistry = theDaoRegistry;
		myFhirContext = theFhirContext;
	}

	@Override
	public IBaseBundle generateIps(RequestDetails theRequestDetails, IIdType thePatientId) {
		IBaseResource patient = myDaoRegistry
			.getResourceDao("Patient")
			.read(thePatientId, theRequestDetails);

		return generateIpsForPatient(theRequestDetails, patient);
	}

	@Override
	public IBaseBundle generateIps(RequestDetails theRequestDetails, TokenParam thePatientIdentifier) {
		SearchParameterMap searchParameterMap = new SearchParameterMap()
			.setLoadSynchronousUpTo(2)
			.add(Patient.SP_IDENTIFIER, thePatientIdentifier);
		IBundleProvider searchResults = myDaoRegistry
			.getResourceDao("Patient")
			.search(searchParameterMap, theRequestDetails);

		ValidateUtil.isTrueOrThrowInvalidRequest(searchResults.sizeOrThrowNpe() > 0, "No Patient could be found matching given identifier");
		ValidateUtil.isTrueOrThrowInvalidRequest(searchResults.sizeOrThrowNpe() == 1, "Multiple Patient resources were found matching given identifier");

		IBaseResource patient = searchResults.getResources(0, 1).get(0);

		return generateIpsForPatient(theRequestDetails, patient);
	}

	private IBaseBundle generateIpsForPatient(RequestDetails theRequestDetails, IBaseResource thePatient) {
		IIdType originalSubjectId = myFhirContext.getVersion().newIdType().setValue(thePatient.getIdElement().getValue());
		massageResourceId(null, thePatient);
		IpsContext context = new IpsContext(thePatient, originalSubjectId);

		IBaseResource author = myGenerationStrategy.createAuthor();
		massageResourceId(context, author);

		CompositionBuilder compositionBuilder = createComposition(thePatient, context, author);

		List<IBaseResource> globalResourcesToInclude = determineInclusions(theRequestDetails, originalSubjectId, context, compositionBuilder);


//		Composition composition = createIPSComposition(patient, author);
//
//		ImmutablePair<List<Resource>, HashMap<PatientSummary.IPSSection, List<Resource>>> resourceTuple = buildResourceTuple(searchResources, ctx, composition, patient);
//		List<Resource> resources = resourceTuple.getLeft();
//		HashMap<PatientSummary.IPSSection, List<Resource>> sectionPrimaries = resourceTuple.getRight();
//
//		HashMap<PatientSummary.IPSSection, String> sectionNarratives = createNarratives(sectionPrimaries, resources, ctx);
//		composition = addIPSSections(composition, sectionPrimaries, sectionNarratives);
//
//		Bundle bundle = createIPSBundle(composition, resources, author);
//
//		return bundle;
//

		IBaseResource composition = compositionBuilder.getComposition();
		IBaseBundle bundle = createCompositionDocument(thePatient, author, composition, globalResourcesToInclude);
		return bundle;
	}

	private IBaseBundle createCompositionDocument(IBaseResource thePatient, IBaseResource author, IBaseResource composition, List<IBaseResource> globalResourcesToInclude) {
		BundleBuilder bundleBuilder = new BundleBuilder(myFhirContext);
		bundleBuilder.setType(Bundle.BundleType.DOCUMENT.toCode());
		bundleBuilder.setIdentifier("urn:ietf:rfc:4122", UUID.randomUUID().toString());
		bundleBuilder.setTimestamp(InstantType.now());

		// Add composition to document
		bundleBuilder.addDocumentEntry(composition);

		// Add subject to document
		bundleBuilder.addDocumentEntry(thePatient);

		// Add inclusion candidates
		for (IBaseResource next : globalResourcesToInclude) {
			bundleBuilder.addDocumentEntry(next);
		}

		// Add author to document
		bundleBuilder.addDocumentEntry(author);

		return bundleBuilder.getBundle();
	}

	@Nonnull
	private List<IBaseResource> determineInclusions(RequestDetails theRequestDetails, IIdType originalSubjectId, IpsContext context, CompositionBuilder theCompositionBuilder) {
		List<IBaseResource> globalResourcesToInclude = new ArrayList<>();
		SectionRegistry sectionRegistry = myGenerationStrategy.getSectionRegistry();
		for (SectionRegistry.Section nextSection : sectionRegistry.getSections()) {
			determineInclusionsForSection(theRequestDetails, originalSubjectId, context, theCompositionBuilder, globalResourcesToInclude, nextSection);
		}
		return globalResourcesToInclude;
	}

	private void determineInclusionsForSection(RequestDetails theRequestDetails, IIdType theOriginalSubjectId, IpsContext theIpsContext, CompositionBuilder theCompositionBuilder, List<IBaseResource> theGlobalResourcesToInclude, SectionRegistry.Section theSection) {
		List<IBaseResource> sectionResourcesToInclude = new ArrayList<>();
		for (String nextResourceType : theSection.getResourceTypes()) {

			SearchParameterMap searchParameterMap = new SearchParameterMap();
			String subjectSp = determinePatientCompartmentSearchParameterName(nextResourceType);
			searchParameterMap.add(subjectSp, new ReferenceParam(theOriginalSubjectId));

			IpsSectionEnum sectionEnum = theSection.getSectionEnum();
			IpsContext.IpsSectionContext ipsSectionContext = theIpsContext.newSectionContext(sectionEnum, nextResourceType);
			myGenerationStrategy.massageResourceSearch(ipsSectionContext, searchParameterMap);

			IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(nextResourceType);
			IBundleProvider searchResult = dao.search(searchParameterMap, theRequestDetails);
			for (int startIndex = 0; ; startIndex += CHUNK_SIZE) {
				int endIndex = startIndex + CHUNK_SIZE;
				List<IBaseResource> resources = searchResult.getResources(startIndex, endIndex);
				if (resources.isEmpty()) {
					break;
				}

				for (IBaseResource nextCandidate : resources) {

					boolean include = myGenerationStrategy.shouldInclude(ipsSectionContext, nextCandidate);
					if (include) {
						IIdType id = myGenerationStrategy.massageResourceId(theIpsContext, nextCandidate);
						nextCandidate.setId(id);
						theGlobalResourcesToInclude.add(nextCandidate);
						sectionResourcesToInclude.add(nextCandidate);
					}

				}

			}

		}

		if (sectionResourcesToInclude.isEmpty() && theSection.getNoInfoGenerator() != null) {
			IBaseResource noInfoResource = theSection.getNoInfoGenerator().generate(theIpsContext.getSubjectId());
			theGlobalResourcesToInclude.add(noInfoResource);
			sectionResourcesToInclude.add(noInfoResource);
		}

		addSection(theSection, theCompositionBuilder, sectionResourcesToInclude);
	}

	private void addSection(SectionRegistry.Section theSection, CompositionBuilder theCompositionBuilder, List<IBaseResource> theResourcesToInclude) {

		CompositionBuilder.SectionBuilder sectionBuilder = theCompositionBuilder.addSection();

		sectionBuilder.setTitle(theSection.getTitle());
		sectionBuilder.addCodeCoding(LOINC_URI, theSection.getSectionCode(), theSection.getSectionDisplay());

		String narrative = createSectionNarrative(theSection, theResourcesToInclude);
		sectionBuilder.setText("generated", narrative);

		for (IBaseResource next : theResourcesToInclude) {
			sectionBuilder.addEntry(next.getIdElement());
		}

		/*
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
		*/
	}

	private CompositionBuilder createComposition(IBaseResource thePatient, IpsContext context, IBaseResource author) {
		CompositionBuilder compositionBuilder = new CompositionBuilder(myFhirContext);
		compositionBuilder.setId(IdType.newRandomUuid());

		compositionBuilder.setStatus(Composition.CompositionStatus.FINAL.toCode());
		compositionBuilder.setSubject(thePatient.getIdElement().toUnqualifiedVersionless());
		compositionBuilder.addTypeCoding("http://loinc.org", "60591-5", "Patient Summary Document");
		compositionBuilder.setDate(InstantType.now());
		compositionBuilder.setTitle(myGenerationStrategy.createTitle(context));
		compositionBuilder.setConfidentiality(myGenerationStrategy.createConfidentiality(context));
		compositionBuilder.addAuthor(author.getIdElement());

		return compositionBuilder;
	}

	private String determinePatientCompartmentSearchParameterName(String theResourceType) {
		RuntimeResourceDefinition resourceDef = myFhirContext.getResourceDefinition(theResourceType);
		return resourceDef.getSearchParamsForCompartmentName("Patient").get(0).getName();
	}

	private void massageResourceId(IpsContext theIpsContext, IBaseResource theResource) {
		IIdType id = myGenerationStrategy.massageResourceId(theIpsContext, theResource);
		theResource.setId(id);
	}

	private String createSectionNarrative(SectionRegistry.Section theSection, List<IBaseResource> theResources) {
		// Use the narrative generator
		String ipsNarratives = "classpath:ca/uhn/fhir/jpa/ips/narrative/ips_narratives.properties";
		String narratives = "classpath:ca/uhn/fhir/narrative/narratives.properties";
		String hapiServerNarratives = "classpath:ca/uhn/fhir/narrative/narratives-hapiserver.properties";
		INarrativeGenerator generator = new CustomThymeleafNarrativeGenerator(ipsNarratives, narratives, hapiServerNarratives);

		// ctx.setNarrativeGenerator(new CustomThymeleafNarrativeGenerator("classpath:narrative/ips_narratives.properties"));
		//ctx.setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());
//		ctx.setNarrativeGenerator(generator);
		// Create a bundle to hold the theResources

		Bundle bundle = new Bundle();
		for (IBaseResource resource : theResources) {
			bundle.addEntry().setResource((Resource) resource);
		}
		String profile = theSection.getProfile();
		bundle.getMeta().addProfile(profile);

		// Generate the narrative
		return generator.generateResourceNarrative(myFhirContext, bundle);
	}
















/*






	private static final List<String> PregnancyCodes = List.of("82810-3", "11636-8", "11637-6", "11638-4", "11639-2", "11640-0", "11612-9", "11613-7", "11614-5", "33065-4");

	// Could not locate these profiles so tried to follow the pattern of the other profiles

	private static List<Resource> createResourceList(List<IBaseResource> iBaseResourceList) {
		List<Resource> resourceList = new ArrayList<Resource>();
		for (IBaseResource ibaseResource : iBaseResourceList) {
			resourceList.add((Resource) ibaseResource);
		}
		return resourceList;
	}

	private static ImmutablePair<List<Resource>, HashMap<PatientSummary.IPSSection, List<Resource>>> buildResourceTuple(List<Resource> searchResources, FhirContext ctx, Composition composition, Patient patient) {
		HashMap<PatientSummary.IPSSection, List<Resource>> initialHashedPrimaries = hashPrimaries(searchResources);
		List<Resource> expandedResources = addNoInfoResources(searchResources, initialHashedPrimaries, patient);
		HashMap<PatientSummary.IPSSection, List<Resource>> hashedExpandedPrimaries = hashPrimaries(expandedResources);
		List<Resource> linkedResources = addLinkToResources(expandedResources, hashedExpandedPrimaries, composition);
		HashMap<PatientSummary.IPSSection, List<Resource>> hashedPrimaries = hashPrimaries(linkedResources);
		HashMap<PatientSummary.IPSSection, List<Resource>> filteredPrimaries = filterPrimaries(hashedPrimaries);
		List<Resource> resources = pruneResources(patient, linkedResources, filteredPrimaries, ctx);

		return new ImmutablePair<List<Resource>, HashMap<PatientSummary.IPSSection, List<Resource>>>(resources, filteredPrimaries);
	}

	private static HashMap<PatientSummary.IPSSection, List<Resource>> hashPrimaries(List<Resource> resourceList) {
		HashMap<PatientSummary.IPSSection, List<Resource>> iPSResourceMap = new HashMap<PatientSummary.IPSSection, List<Resource>>();

		for (Resource resource : resourceList) {
			for (PatientSummary.IPSSection iPSSection : PatientSummary.IPSSection.values()) {
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

	private static List<Resource> addNoInfoResources(List<Resource> resources, HashMap<PatientSummary.IPSSection, List<Resource>> sectionPrimaries, Patient patient) {

		if (sectionPrimaries.get(PatientSummary.IPSSection.ALLERGY_INTOLERANCE) == null) {
			AllergyIntolerance noInfoAllergies = noInfoAllergies(patient);
			resources.add(noInfoAllergies);
		}

		if (sectionPrimaries.get(PatientSummary.IPSSection.MEDICATION_SUMMARY) == null) {
			MedicationStatement noInfoMedications = noInfoMedications(patient);
			resources.add(noInfoMedications);
		}

		if (sectionPrimaries.get(PatientSummary.IPSSection.PROBLEM_LIST) == null) {
			Condition noInfoProblems = noInfoProblems(patient);
			resources.add(noInfoProblems);
		}

		return resources;
	}

	private static HashMap<PatientSummary.IPSSection, List<Resource>> filterPrimaries(HashMap<PatientSummary.IPSSection, List<Resource>> sectionPrimaries) {
		HashMap<PatientSummary.IPSSection, List<Resource>> filteredPrimaries = new HashMap<PatientSummary.IPSSection, List<Resource>>();
		for ( PatientSummary.IPSSection section : sectionPrimaries.keySet() ) {
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

	private static List<Resource> pruneResources(Patient patient, List<Resource> resources, HashMap<PatientSummary.IPSSection, List<Resource>> sectionPrimaries, FhirContext ctx) {
		List<String> resourceIds = new ArrayList<String>();
		List<String> followedIds = new ArrayList<String>();

		HashMap<String, Resource> resourcesById = new HashMap<String, Resource>();
		for (Resource resource : resources) {
			resourcesById.put(resource.getIdElement().getIdPart(), resource);
		}
		String patientId = patient.getIdElement().getIdPart();
		resourcesById.put(patientId, patient);

		recursivePrune(patientId, resourceIds, followedIds, resourcesById, ctx);

		for (PatientSummary.IPSSection section : sectionPrimaries.keySet()) {
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

	private static List<Resource> addLinkToResources(List<Resource> resources, HashMap<PatientSummary.IPSSection, List<Resource>> sectionPrimaries, Composition composition) {
		List<Resource> linkedResources = new ArrayList<Resource>();
		HashMap<String, String> valueUrls = new HashMap<String, String>();

		String url = "http://hl7.org/fhir/StructureDefinition/NarrativeLink";
		String valueUrlBase = composition.getId() + "#";

		for (PatientSummary.IPSSection section : sectionPrimaries.keySet()) {
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

	private static HashMap<PatientSummary.IPSSection, String> createNarratives(HashMap<PatientSummary.IPSSection, List<Resource>> sectionPrimaries, List<Resource> resources, FhirContext ctx) {
		HashMap<PatientSummary.IPSSection, String> hashedNarratives = new HashMap<PatientSummary.IPSSection, String>();

		for (PatientSummary.IPSSection section : sectionPrimaries.keySet()) {
			String narrative = createSectionNarrative(section, resources, ctx);
			hashedNarratives.put(section, narrative);
		}

		return hashedNarratives;
	}



	private static Boolean passesFilter(PatientSummary.IPSSection section, Resource resource) {
		if (section == PatientSummary.IPSSection.ALLERGY_INTOLERANCE) {
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
		if (section == PatientSummary.IPSSection.MEDICATION_SUMMARY) {
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
		if (section == PatientSummary.IPSSection.PROBLEM_LIST) {
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
		if (section == PatientSummary.IPSSection.IMMUNIZATIONS) {
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
		if (section == PatientSummary.IPSSection.PROCEDURES) {
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
		if (section == PatientSummary.IPSSection.MEDICAL_DEVICES) {
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
		if (section == PatientSummary.IPSSection.DIAGNOSTIC_RESULTS) {
			if (resource.getResourceType() == ResourceType.DiagnosticReport) {
				return true;
			}
			else if (resource.getResourceType() == ResourceType.Observation) {
				// code filtering not yet applied
				Observation observation = (Observation) resource;
				return (observation.getStatus() != Observation.ObservationStatus.PRELIMINARY);
			}
			else {
				return false;
			}
		}
		if (section == PatientSummary.IPSSection.VITAL_SIGNS) {
			if (resource.getResourceType() == ResourceType.Observation) {
				// code filtering not yet applied
				return true;
			}
			else {
				return false;
			}
		}
		if (section == PatientSummary.IPSSection.PREGNANCY) {
			Observation observation = (Observation) resource;
			return (observation.getStatus() != Observation.ObservationStatus.PRELIMINARY);
		}
		if (section == PatientSummary.IPSSection.SOCIAL_HISTORY) {
			Observation observation = (Observation) resource;
			return (observation.getStatus() != Observation.ObservationStatus.PRELIMINARY);
		}
		if (section == PatientSummary.IPSSection.ILLNESS_HISTORY) {
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
		if (section == PatientSummary.IPSSection.FUNCTIONAL_STATUS) {
			ClinicalImpression clinimp = (ClinicalImpression) resource;
			if (clinimp.getStatus() != ClinicalImpression.ClinicalImpressionStatus.INPROGRESS
				&& clinimp.getStatus() != ClinicalImpression.ClinicalImpressionStatus.ENTEREDINERROR) {
				return true;
			}
			else {
				return false;
			}
		}
		if (section == PatientSummary.IPSSection.PLAN_OF_CARE) {
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
		if (section == PatientSummary.IPSSection.ADVANCE_DIRECTIVES) {
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

	private static Composition addIPSSections(Composition composition, HashMap<PatientSummary.IPSSection, List<Resource>> sectionPrimaries, HashMap<PatientSummary.IPSSection, String> hashedNarratives) {
		// Add sections
		for (PatientSummary.IPSSection iPSSection : PatientSummary.IPSSection.values()) {
			if (sectionPrimaries.get(iPSSection) != null && sectionPrimaries.get(iPSSection).size() > 0) {
				Composition.SectionComponent section = createSection(SectionText.get(iPSSection), sectionPrimaries.get(iPSSection), hashedNarratives.get(iPSSection));
				composition.addSection(section);
			}
		}
		return composition;
	}

	private static Composition.SectionComponent createSection(Map<String, String> text, List<Resource> resources, String narrative) {

	}



	private static Boolean isObservationinSection(PatientSummary.IPSSection iPSSection, Observation observation) {
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




 */


	private static String formatAsUrn(IBaseResource theResource) {
		// TODO: Make version independent - And we should probably ask the server to
		// fully qualify IDs instead of always prepending urn:uuid:
		Resource resource = (Resource) theResource;
		if (resource.getIdElement().isUrn()) {
			return resource.getId();
		} else {
			return "urn:uuid:" + resource.getIdElement().getIdPart();
		}
	}

}
