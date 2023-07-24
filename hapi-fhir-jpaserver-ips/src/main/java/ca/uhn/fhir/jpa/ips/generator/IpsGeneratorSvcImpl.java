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
package ca.uhn.fhir.jpa.ips.generator;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.fhirpath.IFhirPathEvaluationContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.ips.api.IIpsGenerationStrategy;
import ca.uhn.fhir.jpa.ips.api.IpsContext;
import ca.uhn.fhir.jpa.ips.api.IpsSectionEnum;
import ca.uhn.fhir.jpa.ips.api.SectionRegistry;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.valueset.BundleEntrySearchModeEnum;
import ca.uhn.fhir.narrative.CustomThymeleafNarrativeGenerator;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.CompositionBuilder;
import ca.uhn.fhir.util.ResourceReferenceInfo;
import ca.uhn.fhir.util.ValidateUtil;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static ca.uhn.fhir.jpa.term.api.ITermLoaderSvc.LOINC_URI;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class IpsGeneratorSvcImpl implements IIpsGeneratorSvc {

	public static final int CHUNK_SIZE = 10;
	private static final Logger ourLog = LoggerFactory.getLogger(IpsGeneratorSvcImpl.class);
	private final IIpsGenerationStrategy myGenerationStrategy;
	private final DaoRegistry myDaoRegistry;
	private final FhirContext myFhirContext;

	/**
	 * Constructor
	 */
	public IpsGeneratorSvcImpl(
			FhirContext theFhirContext, IIpsGenerationStrategy theGenerationStrategy, DaoRegistry theDaoRegistry) {
		myGenerationStrategy = theGenerationStrategy;
		myDaoRegistry = theDaoRegistry;
		myFhirContext = theFhirContext;
	}

	@Override
	public IBaseBundle generateIps(RequestDetails theRequestDetails, IIdType thePatientId) {
		IBaseResource patient = myDaoRegistry.getResourceDao("Patient").read(thePatientId, theRequestDetails);

		return generateIpsForPatient(theRequestDetails, patient);
	}

	@Override
	public IBaseBundle generateIps(RequestDetails theRequestDetails, TokenParam thePatientIdentifier) {
		SearchParameterMap searchParameterMap =
				new SearchParameterMap().setLoadSynchronousUpTo(2).add(Patient.SP_IDENTIFIER, thePatientIdentifier);
		IBundleProvider searchResults =
				myDaoRegistry.getResourceDao("Patient").search(searchParameterMap, theRequestDetails);

		ValidateUtil.isTrueOrThrowInvalidRequest(
				searchResults.sizeOrThrowNpe() > 0, "No Patient could be found matching given identifier");
		ValidateUtil.isTrueOrThrowInvalidRequest(
				searchResults.sizeOrThrowNpe() == 1, "Multiple Patient resources were found matching given identifier");

		IBaseResource patient = searchResults.getResources(0, 1).get(0);

		return generateIpsForPatient(theRequestDetails, patient);
	}

	private IBaseBundle generateIpsForPatient(RequestDetails theRequestDetails, IBaseResource thePatient) {
		IIdType originalSubjectId = myFhirContext
				.getVersion()
				.newIdType()
				.setValue(thePatient.getIdElement().getValue())
				.toUnqualifiedVersionless();
		massageResourceId(null, thePatient);
		IpsContext context = new IpsContext(thePatient, originalSubjectId);

		ResourceInclusionCollection globalResourcesToInclude = new ResourceInclusionCollection();
		globalResourcesToInclude.addResourceIfNotAlreadyPresent(thePatient, originalSubjectId.getValue());

		IBaseResource author = myGenerationStrategy.createAuthor();
		massageResourceId(context, author);

		CompositionBuilder compositionBuilder = createComposition(thePatient, context, author);
		determineInclusions(
				theRequestDetails, originalSubjectId, context, compositionBuilder, globalResourcesToInclude);

		IBaseResource composition = compositionBuilder.getComposition();

		// Create the narrative for the Composition itself
		CustomThymeleafNarrativeGenerator generator = newNarrativeGenerator(globalResourcesToInclude);
		generator.populateResourceNarrative(myFhirContext, composition);

		return createCompositionDocument(author, composition, globalResourcesToInclude);
	}

	private IBaseBundle createCompositionDocument(
			IBaseResource author, IBaseResource composition, ResourceInclusionCollection theResourcesToInclude) {
		BundleBuilder bundleBuilder = new BundleBuilder(myFhirContext);
		bundleBuilder.setType(Bundle.BundleType.DOCUMENT.toCode());
		bundleBuilder.setIdentifier("urn:ietf:rfc:4122", UUID.randomUUID().toString());
		bundleBuilder.setTimestamp(InstantType.now());

		// Add composition to document
		bundleBuilder.addDocumentEntry(composition);

		// Add inclusion candidates
		for (IBaseResource next : theResourcesToInclude.getResources()) {
			bundleBuilder.addDocumentEntry(next);
		}

		// Add author to document
		bundleBuilder.addDocumentEntry(author);

		return bundleBuilder.getBundle();
	}

	@Nonnull
	private ResourceInclusionCollection determineInclusions(
			RequestDetails theRequestDetails,
			IIdType originalSubjectId,
			IpsContext context,
			CompositionBuilder theCompositionBuilder,
			ResourceInclusionCollection theGlobalResourcesToInclude) {
		SectionRegistry sectionRegistry = myGenerationStrategy.getSectionRegistry();
		for (SectionRegistry.Section nextSection : sectionRegistry.getSections()) {
			determineInclusionsForSection(
					theRequestDetails,
					originalSubjectId,
					context,
					theCompositionBuilder,
					theGlobalResourcesToInclude,
					nextSection);
		}
		return theGlobalResourcesToInclude;
	}

	private void determineInclusionsForSection(
			RequestDetails theRequestDetails,
			IIdType theOriginalSubjectId,
			IpsContext theIpsContext,
			CompositionBuilder theCompositionBuilder,
			ResourceInclusionCollection theGlobalResourcesToInclude,
			SectionRegistry.Section theSection) {
		ResourceInclusionCollection sectionResourcesToInclude = new ResourceInclusionCollection();
		for (String nextResourceType : theSection.getResourceTypes()) {

			SearchParameterMap searchParameterMap = new SearchParameterMap();
			String subjectSp = determinePatientCompartmentSearchParameterName(nextResourceType);
			searchParameterMap.add(subjectSp, new ReferenceParam(theOriginalSubjectId));

			IpsSectionEnum sectionEnum = theSection.getSectionEnum();
			IpsContext.IpsSectionContext ipsSectionContext =
					theIpsContext.newSectionContext(sectionEnum, nextResourceType);
			myGenerationStrategy.massageResourceSearch(ipsSectionContext, searchParameterMap);

			Set<Include> includes = myGenerationStrategy.provideResourceSearchIncludes(ipsSectionContext);
			includes.forEach(searchParameterMap::addInclude);

			IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(nextResourceType);
			IBundleProvider searchResult = dao.search(searchParameterMap, theRequestDetails);
			for (int startIndex = 0; ; startIndex += CHUNK_SIZE) {
				int endIndex = startIndex + CHUNK_SIZE;
				List<IBaseResource> resources = searchResult.getResources(startIndex, endIndex);
				if (resources.isEmpty()) {
					break;
				}

				for (IBaseResource nextCandidate : resources) {

					boolean candidateIsSearchInclude = ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.get(nextCandidate)
							== BundleEntrySearchModeEnum.INCLUDE;
					boolean addResourceToBundle;
					if (candidateIsSearchInclude) {
						addResourceToBundle = true;
					} else {
						addResourceToBundle = myGenerationStrategy.shouldInclude(ipsSectionContext, nextCandidate);
					}

					if (addResourceToBundle) {

						String originalResourceId = nextCandidate
								.getIdElement()
								.toUnqualifiedVersionless()
								.getValue();

						// Check if we already have this resource included so that we don't
						// include it twice
						IBaseResource previouslyExistingResource =
								theGlobalResourcesToInclude.getResourceByOriginalId(originalResourceId);
						if (previouslyExistingResource != null) {
							BundleEntrySearchModeEnum candidateSearchEntryMode =
									ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.get(nextCandidate);
							if (candidateSearchEntryMode == BundleEntrySearchModeEnum.MATCH) {
								ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put(
										previouslyExistingResource, BundleEntrySearchModeEnum.MATCH);
							}

							nextCandidate = previouslyExistingResource;
							sectionResourcesToInclude.addResourceIfNotAlreadyPresent(nextCandidate, originalResourceId);
						} else if (theGlobalResourcesToInclude.hasResourceWithReplacementId(originalResourceId)) {
							if (!candidateIsSearchInclude) {
								sectionResourcesToInclude.addResourceIfNotAlreadyPresent(
										nextCandidate, originalResourceId);
							}
						} else {
							IIdType id = myGenerationStrategy.massageResourceId(theIpsContext, nextCandidate);
							nextCandidate.setId(id);
							theGlobalResourcesToInclude.addResourceIfNotAlreadyPresent(
									nextCandidate, originalResourceId);
							if (!candidateIsSearchInclude) {
								sectionResourcesToInclude.addResourceIfNotAlreadyPresent(
										nextCandidate, originalResourceId);
							}
						}
					}
				}
			}
		}

		if (sectionResourcesToInclude.isEmpty() && theSection.getNoInfoGenerator() != null) {
			IBaseResource noInfoResource = theSection.getNoInfoGenerator().generate(theIpsContext.getSubjectId());
			String id = IdType.newRandomUuid().getValue();
			if (noInfoResource.getIdElement().isEmpty()) {
				noInfoResource.setId(id);
			}
			ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put(noInfoResource, BundleEntrySearchModeEnum.MATCH);
			theGlobalResourcesToInclude.addResourceIfNotAlreadyPresent(
					noInfoResource,
					noInfoResource.getIdElement().toUnqualifiedVersionless().getValue());
			sectionResourcesToInclude.addResourceIfNotAlreadyPresent(noInfoResource, id);
		}

		/*
		 * Update any references within the added candidates - This is important
		 * because we might be replacing resource IDs before including them in
		 * the summary, so we need to also update the references to those
		 * resources.
		 */
		for (IBaseResource nextResource : theGlobalResourcesToInclude.getResources()) {
			List<ResourceReferenceInfo> references = myFhirContext.newTerser().getAllResourceReferences(nextResource);
			for (ResourceReferenceInfo nextReference : references) {
				String existingReference = nextReference
						.getResourceReference()
						.getReferenceElement()
						.getValue();
				if (isNotBlank(existingReference)) {
					existingReference = new IdType(existingReference)
							.toUnqualifiedVersionless()
							.getValue();
					String replacement = theGlobalResourcesToInclude.getIdSubstitution(existingReference);
					if (isNotBlank(replacement)) {
						if (!replacement.equals(existingReference)) {
							nextReference.getResourceReference().setReference(replacement);
						}
					} else if (theGlobalResourcesToInclude.getResourceById(existingReference) == null) {
						// If this reference doesn't point to something we have actually
						// included in the bundle, clear the reference.
						nextReference.getResourceReference().setReference(null);
						nextReference.getResourceReference().setResource(null);
					}
				}
			}
		}

		if (sectionResourcesToInclude.isEmpty()) {
			return;
		}

		addSection(theSection, theCompositionBuilder, sectionResourcesToInclude, theGlobalResourcesToInclude);
	}

	@SuppressWarnings("unchecked")
	private void addSection(
			SectionRegistry.Section theSection,
			CompositionBuilder theCompositionBuilder,
			ResourceInclusionCollection theResourcesToInclude,
			ResourceInclusionCollection theGlobalResourcesToInclude) {

		CompositionBuilder.SectionBuilder sectionBuilder = theCompositionBuilder.addSection();

		sectionBuilder.setTitle(theSection.getTitle());
		sectionBuilder.addCodeCoding(LOINC_URI, theSection.getSectionCode(), theSection.getSectionDisplay());

		for (IBaseResource next : theResourcesToInclude.getResources()) {
			if (ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.get(next) == BundleEntrySearchModeEnum.INCLUDE) {
				continue;
			}

			IBaseExtension<?, ?> narrativeLink = ((IBaseHasExtensions) next).addExtension();
			narrativeLink.setUrl("http://hl7.org/fhir/StructureDefinition/narrativeLink");
			String narrativeLinkValue =
					theCompositionBuilder.getComposition().getIdElement().getValue()
							+ "#"
							+ myFhirContext.getResourceType(next)
							+ "-"
							+ next.getIdElement().getValue();
			IPrimitiveType<String> narrativeLinkUri = (IPrimitiveType<String>)
					myFhirContext.getElementDefinition("url").newInstance();
			narrativeLinkUri.setValueAsString(narrativeLinkValue);
			narrativeLink.setValue(narrativeLinkUri);

			sectionBuilder.addEntry(next.getIdElement());
		}

		String narrative = createSectionNarrative(theSection, theResourcesToInclude, theGlobalResourcesToInclude);
		sectionBuilder.setText("generated", narrative);
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
		Set<String> searchParams = resourceDef.getSearchParamsForCompartmentName("Patient").stream()
				.map(RuntimeSearchParam::getName)
				.collect(Collectors.toSet());
		// Prefer "patient", then "subject" then anything else
		if (searchParams.contains(Observation.SP_PATIENT)) {
			return Observation.SP_PATIENT;
		}
		if (searchParams.contains(Observation.SP_SUBJECT)) {
			return Observation.SP_SUBJECT;
		}
		return searchParams.iterator().next();
	}

	private void massageResourceId(IpsContext theIpsContext, IBaseResource theResource) {
		IIdType id = myGenerationStrategy.massageResourceId(theIpsContext, theResource);
		theResource.setId(id);
	}

	private String createSectionNarrative(
			SectionRegistry.Section theSection,
			ResourceInclusionCollection theResources,
			ResourceInclusionCollection theGlobalResourceCollection) {
		CustomThymeleafNarrativeGenerator generator = newNarrativeGenerator(theGlobalResourceCollection);

		Bundle bundle = new Bundle();
		for (IBaseResource resource : theResources.getResources()) {
			BundleEntrySearchModeEnum searchMode = ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.get(resource);
			if (searchMode == BundleEntrySearchModeEnum.MATCH) {
				bundle.addEntry().setResource((Resource) resource);
			}
		}
		String profile = theSection.getProfile();
		bundle.getMeta().addProfile(profile);

		// Generate the narrative
		return generator.generateResourceNarrative(myFhirContext, bundle);
	}

	@Nonnull
	private CustomThymeleafNarrativeGenerator newNarrativeGenerator(
			ResourceInclusionCollection theGlobalResourceCollection) {
		List<String> narrativePropertyFiles = myGenerationStrategy.getNarrativePropertyFiles();
		CustomThymeleafNarrativeGenerator generator = new CustomThymeleafNarrativeGenerator(narrativePropertyFiles);
		generator.setFhirPathEvaluationContext(new IFhirPathEvaluationContext() {
			@Override
			public IBase resolveReference(@Nonnull IIdType theReference, @Nullable IBase theContext) {
				IBaseResource resource = theGlobalResourceCollection.getResourceById(theReference);
				return resource;
			}
		});
		return generator;
	}

	private static class ResourceInclusionCollection {

		private final List<IBaseResource> myResources = new ArrayList<>();
		private final Map<String, IBaseResource> myIdToResource = new HashMap<>();
		private final BiMap<String, String> myOriginalIdToNewId = HashBiMap.create();

		public List<IBaseResource> getResources() {
			return myResources;
		}

		/**
		 * @param theOriginalResourceId Must be an unqualified versionless ID
		 */
		public void addResourceIfNotAlreadyPresent(IBaseResource theResource, String theOriginalResourceId) {
			assert theOriginalResourceId.matches("([A-Z][a-z]([A-Za-z]+)/[a-zA-Z0-9._-]+)|(urn:uuid:[0-9a-z-]+)")
					: "Not an unqualified versionless ID: " + theOriginalResourceId;

			String resourceId =
					theResource.getIdElement().toUnqualifiedVersionless().getValue();
			if (myIdToResource.containsKey(resourceId)) {
				return;
			}

			myResources.add(theResource);
			myIdToResource.put(resourceId, theResource);
			myOriginalIdToNewId.put(theOriginalResourceId, resourceId);
		}

		public String getIdSubstitution(String theExistingReference) {
			return myOriginalIdToNewId.get(theExistingReference);
		}

		public IBaseResource getResourceById(IIdType theReference) {
			return getResourceById(theReference.toUnqualifiedVersionless().getValue());
		}

		public boolean hasResourceWithReplacementId(String theReplacementId) {
			return myOriginalIdToNewId.containsValue(theReplacementId);
		}

		public IBaseResource getResourceById(String theReference) {
			return myIdToResource.get(theReference);
		}

		@Nullable
		public IBaseResource getResourceByOriginalId(String theOriginalResourceId) {
			String newResourceId = myOriginalIdToNewId.get(theOriginalResourceId);
			if (newResourceId != null) {
				return myIdToResource.get(newResourceId);
			}
			return null;
		}

		public boolean isEmpty() {
			return myResources.isEmpty();
		}
	}
}
