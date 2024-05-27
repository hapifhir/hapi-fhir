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
package ca.uhn.fhir.jpa.ips.generator;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.fhirpath.IFhirPathEvaluationContext;
import ca.uhn.fhir.jpa.ips.api.IIpsGenerationStrategy;
import ca.uhn.fhir.jpa.ips.api.ISectionResourceSupplier;
import ca.uhn.fhir.jpa.ips.api.IpsContext;
import ca.uhn.fhir.jpa.ips.api.IpsSectionContext;
import ca.uhn.fhir.jpa.ips.api.Section;
import ca.uhn.fhir.narrative.CustomThymeleafNarrativeGenerator;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.CompositionBuilder;
import ca.uhn.fhir.util.ResourceReferenceInfo;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Validate;
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
import org.hl7.fhir.r4.model.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class IpsGeneratorSvcImpl implements IIpsGeneratorSvc {

	public static final String RESOURCE_ENTRY_INCLUSION_TYPE = "RESOURCE_ENTRY_INCLUSION_TYPE";
	public static final String URL_NARRATIVE_LINK = "http://hl7.org/fhir/StructureDefinition/narrativeLink";
	private final List<IIpsGenerationStrategy> myGenerationStrategies;
	private final FhirContext myFhirContext;

	/**
	 * Constructor
	 */
	public IpsGeneratorSvcImpl(FhirContext theFhirContext, IIpsGenerationStrategy theGenerationStrategy) {
		this(theFhirContext, List.of(theGenerationStrategy));
	}

	public IpsGeneratorSvcImpl(FhirContext theFhirContext, List<IIpsGenerationStrategy> theIpsGenerationStrategies) {
		myGenerationStrategies = theIpsGenerationStrategies;
		myFhirContext = theFhirContext;

		myGenerationStrategies.forEach(IIpsGenerationStrategy::initialize);
	}

	/**
	 * Generate an IPS using a patient ID
	 */
	@Override
	public IBaseBundle generateIps(RequestDetails theRequestDetails, IIdType thePatientId, String theProfile) {
		IIpsGenerationStrategy strategy = selectGenerationStrategy(theProfile);
		IBaseResource patient = strategy.fetchPatient(thePatientId, theRequestDetails);
		return generateIpsForPatient(strategy, theRequestDetails, patient);
	}

	/**
	 * Generate an IPS using a patient identifier
	 */
	@Override
	public IBaseBundle generateIps(
			RequestDetails theRequestDetails, TokenParam thePatientIdentifier, String theProfile) {
		IIpsGenerationStrategy strategy = selectGenerationStrategy(theProfile);
		IBaseResource patient = strategy.fetchPatient(thePatientIdentifier, theRequestDetails);
		return generateIpsForPatient(strategy, theRequestDetails, patient);
	}

	IIpsGenerationStrategy selectGenerationStrategy(@Nullable String theRequestedProfile) {
		return myGenerationStrategies.stream()
				.filter(t -> isBlank(theRequestedProfile) || theRequestedProfile.equals(t.getBundleProfile()))
				.findFirst()
				.orElse(myGenerationStrategies.get(0));
	}

	private IBaseBundle generateIpsForPatient(
			IIpsGenerationStrategy theStrategy, RequestDetails theRequestDetails, IBaseResource thePatient) {
		IIdType originalSubjectId = myFhirContext
				.getVersion()
				.newIdType()
				.setValue(thePatient.getIdElement().getValue())
				.toUnqualifiedVersionless();
		massageResourceId(theStrategy, theRequestDetails, null, thePatient);
		IpsContext context = new IpsContext(thePatient, originalSubjectId);

		ResourceInclusionCollection globalResourcesToInclude = new ResourceInclusionCollection();
		globalResourcesToInclude.addResourceIfNotAlreadyPresent(thePatient, originalSubjectId.getValue());

		IBaseResource author = theStrategy.createAuthor();
		massageResourceId(theStrategy, theRequestDetails, context, author);

		CompositionBuilder compositionBuilder = createComposition(theStrategy, thePatient, context, author);
		determineInclusions(theStrategy, theRequestDetails, context, compositionBuilder, globalResourcesToInclude);

		IBaseResource composition = compositionBuilder.getComposition();

		// Create the narrative for the Composition itself
		CustomThymeleafNarrativeGenerator generator = newNarrativeGenerator(theStrategy, globalResourcesToInclude);
		generator.populateResourceNarrative(myFhirContext, composition);

		return createDocumentBundleForComposition(theStrategy, author, composition, globalResourcesToInclude);
	}

	private IBaseBundle createDocumentBundleForComposition(
			IIpsGenerationStrategy theStrategy,
			IBaseResource author,
			IBaseResource composition,
			ResourceInclusionCollection theResourcesToInclude) {
		BundleBuilder bundleBuilder = new BundleBuilder(myFhirContext);
		bundleBuilder.setType(Bundle.BundleType.DOCUMENT.toCode());
		bundleBuilder.setIdentifier("urn:ietf:rfc:4122", UUID.randomUUID().toString());
		bundleBuilder.setTimestamp(InstantType.now());
		bundleBuilder.addProfile(theStrategy.getBundleProfile());

		// Add composition to document
		bundleBuilder.addDocumentEntry(composition);

		// Add inclusion candidates
		for (IBaseResource next : theResourcesToInclude.getResources()) {
			bundleBuilder.addDocumentEntry(next);
		}

		// Add author to document
		bundleBuilder.addDocumentEntry(author);

		IBaseBundle retVal = bundleBuilder.getBundle();

		theStrategy.postManipulateIpsBundle(retVal);

		return retVal;
	}

	private void determineInclusions(
			IIpsGenerationStrategy theStrategy,
			RequestDetails theRequestDetails,
			IpsContext theIpsContext,
			CompositionBuilder theCompositionBuilder,
			ResourceInclusionCollection theGlobalResourcesToInclude) {
		for (Section nextSection : theStrategy.getSections()) {
			determineInclusionsForSection(
					theStrategy,
					theRequestDetails,
					theIpsContext,
					theCompositionBuilder,
					theGlobalResourcesToInclude,
					nextSection);
		}
	}

	private void determineInclusionsForSection(
			IIpsGenerationStrategy theStrategy,
			RequestDetails theRequestDetails,
			IpsContext theIpsContext,
			CompositionBuilder theCompositionBuilder,
			ResourceInclusionCollection theGlobalResourceCollectionToPopulate,
			Section theSection) {
		ResourceInclusionCollection sectionResourceCollectionToPopulate = new ResourceInclusionCollection();
		ISectionResourceSupplier resourceSupplier = theStrategy.getSectionResourceSupplier(theSection);

		determineInclusionsForSectionResourceTypes(
				theStrategy,
				theRequestDetails,
				theIpsContext,
				theGlobalResourceCollectionToPopulate,
				theSection,
				resourceSupplier,
				sectionResourceCollectionToPopulate);

		generateSectionNoInfoResourceIfNoInclusionsFound(
				theIpsContext, theGlobalResourceCollectionToPopulate, theSection, sectionResourceCollectionToPopulate);

		/*
		 * Update any references within the added candidates - This is important
		 * because we might be replacing resource IDs before including them in
		 * the summary, so we need to also update the references to those
		 * resources.
		 */
		updateReferencesInInclusionsForSection(theGlobalResourceCollectionToPopulate);

		if (sectionResourceCollectionToPopulate.isEmpty()) {
			return;
		}

		addSection(
				theStrategy,
				theSection,
				theCompositionBuilder,
				sectionResourceCollectionToPopulate,
				theGlobalResourceCollectionToPopulate);
	}

	private void updateReferencesInInclusionsForSection(
			ResourceInclusionCollection theGlobalResourceCollectionToPopulate) {
		for (IBaseResource nextResource : theGlobalResourceCollectionToPopulate.getResources()) {
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
					String replacement = theGlobalResourceCollectionToPopulate.getIdSubstitution(existingReference);
					if (isNotBlank(replacement)) {
						if (!replacement.equals(existingReference)) {
							nextReference.getResourceReference().setReference(replacement);
						}
					} else if (theGlobalResourceCollectionToPopulate.getResourceById(existingReference) == null) {
						// If this reference doesn't point to something we have actually
						// included in the bundle, clear the reference.
						nextReference.getResourceReference().setReference(null);
						nextReference.getResourceReference().setResource(null);
					}
				}
			}
		}
	}

	private static void generateSectionNoInfoResourceIfNoInclusionsFound(
			IpsContext theIpsContext,
			ResourceInclusionCollection theGlobalResourceCollectionToPopulate,
			Section theSection,
			ResourceInclusionCollection sectionResourceCollectionToPopulate) {
		if (sectionResourceCollectionToPopulate.isEmpty() && theSection.getNoInfoGenerator() != null) {
			IBaseResource noInfoResource = theSection.getNoInfoGenerator().generate(theIpsContext.getSubjectId());
			String id = IdType.newRandomUuid().getValue();
			if (noInfoResource.getIdElement().isEmpty()) {
				noInfoResource.setId(id);
			}
			noInfoResource.setUserData(
					RESOURCE_ENTRY_INCLUSION_TYPE, ISectionResourceSupplier.InclusionTypeEnum.PRIMARY_RESOURCE);
			theGlobalResourceCollectionToPopulate.addResourceIfNotAlreadyPresent(
					noInfoResource,
					noInfoResource.getIdElement().toUnqualifiedVersionless().getValue());
			sectionResourceCollectionToPopulate.addResourceIfNotAlreadyPresent(noInfoResource, id);
		}
	}

	private void determineInclusionsForSectionResourceTypes(
			IIpsGenerationStrategy theStrategy,
			RequestDetails theRequestDetails,
			IpsContext theIpsContext,
			ResourceInclusionCollection theGlobalResourceCollectionToPopulate,
			Section theSection,
			ISectionResourceSupplier resourceSupplier,
			ResourceInclusionCollection sectionResourceCollectionToPopulate) {
		for (Class<? extends IBaseResource> nextResourceType : theSection.getResourceTypes()) {
			determineInclusionsForSectionResourceType(
					theStrategy,
					theRequestDetails,
					theIpsContext,
					theGlobalResourceCollectionToPopulate,
					theSection,
					nextResourceType,
					resourceSupplier,
					sectionResourceCollectionToPopulate);
		}
	}

	private <T extends IBaseResource> void determineInclusionsForSectionResourceType(
			IIpsGenerationStrategy theStrategy,
			RequestDetails theRequestDetails,
			IpsContext theIpsContext,
			ResourceInclusionCollection theGlobalResourceCollectionToPopulate,
			Section theSection,
			Class<T> nextResourceType,
			ISectionResourceSupplier resourceSupplier,
			ResourceInclusionCollection sectionResourceCollectionToPopulate) {
		IpsSectionContext<T> ipsSectionContext = theIpsContext.newSectionContext(theSection, nextResourceType);

		List<ISectionResourceSupplier.ResourceEntry> resources =
				resourceSupplier.fetchResourcesForSection(theIpsContext, ipsSectionContext, theRequestDetails);
		if (resources != null) {
			for (ISectionResourceSupplier.ResourceEntry nextEntry : resources) {
				IBaseResource resource = nextEntry.getResource();
				Validate.isTrue(
						resource.getIdElement().hasIdPart(),
						"fetchResourcesForSection(..) returned resource(s) with no ID populated");
				resource.setUserData(RESOURCE_ENTRY_INCLUSION_TYPE, nextEntry.getInclusionType());
			}
			addResourcesToIpsContents(
					theStrategy,
					theRequestDetails,
					theIpsContext,
					resources,
					theGlobalResourceCollectionToPopulate,
					sectionResourceCollectionToPopulate);
		}
	}

	/**
	 * Given a collection of resources that have been fetched, analyze them and add them as appropriate
	 * to the collection that will be included in a given IPS section context.
	 *
	 * @param theStrategy           The generation strategy
	 * @param theIpsContext         The overall IPS generation context for this IPS.
	 * @param theCandidateResources The resources that have been fetched for inclusion in the IPS bundle
	 */
	private void addResourcesToIpsContents(
			IIpsGenerationStrategy theStrategy,
			RequestDetails theRequestDetails,
			IpsContext theIpsContext,
			List<ISectionResourceSupplier.ResourceEntry> theCandidateResources,
			ResourceInclusionCollection theGlobalResourcesCollectionToPopulate,
			ResourceInclusionCollection theSectionResourceCollectionToPopulate) {
		for (ISectionResourceSupplier.ResourceEntry nextCandidateEntry : theCandidateResources) {
			if (nextCandidateEntry.getInclusionType() == ISectionResourceSupplier.InclusionTypeEnum.EXCLUDE) {
				continue;
			}

			IBaseResource nextCandidate = nextCandidateEntry.getResource();
			boolean primaryResource = nextCandidateEntry.getInclusionType()
					== ISectionResourceSupplier.InclusionTypeEnum.PRIMARY_RESOURCE;

			String originalResourceId =
					nextCandidate.getIdElement().toUnqualifiedVersionless().getValue();

			// Check if we already have this resource included so that we don't
			// include it twice
			IBaseResource previouslyExistingResource =
					theGlobalResourcesCollectionToPopulate.getResourceByOriginalId(originalResourceId);

			if (previouslyExistingResource != null) {
				reuseAlreadyIncludedGlobalResourceInSectionCollection(
						theSectionResourceCollectionToPopulate,
						previouslyExistingResource,
						primaryResource,
						originalResourceId);
			} else if (theGlobalResourcesCollectionToPopulate.hasResourceWithReplacementId(originalResourceId)) {
				addResourceToSectionCollectionOnlyIfPrimary(
						theSectionResourceCollectionToPopulate, primaryResource, nextCandidate, originalResourceId);
			} else {
				addResourceToGlobalCollectionAndSectionCollection(
						theStrategy,
						theRequestDetails,
						theIpsContext,
						theGlobalResourcesCollectionToPopulate,
						theSectionResourceCollectionToPopulate,
						nextCandidate,
						originalResourceId,
						primaryResource);
			}
		}
	}

	private static void addResourceToSectionCollectionOnlyIfPrimary(
			ResourceInclusionCollection theSectionResourceCollectionToPopulate,
			boolean primaryResource,
			IBaseResource nextCandidate,
			String originalResourceId) {
		if (primaryResource) {
			theSectionResourceCollectionToPopulate.addResourceIfNotAlreadyPresent(nextCandidate, originalResourceId);
		}
	}

	private void addResourceToGlobalCollectionAndSectionCollection(
			IIpsGenerationStrategy theStrategy,
			RequestDetails theRequestDetails,
			IpsContext theIpsContext,
			ResourceInclusionCollection theGlobalResourcesCollectionToPopulate,
			ResourceInclusionCollection theSectionResourceCollectionToPopulate,
			IBaseResource nextCandidate,
			String originalResourceId,
			boolean primaryResource) {
		massageResourceId(theStrategy, theRequestDetails, theIpsContext, nextCandidate);
		theGlobalResourcesCollectionToPopulate.addResourceIfNotAlreadyPresent(nextCandidate, originalResourceId);
		addResourceToSectionCollectionOnlyIfPrimary(
				theSectionResourceCollectionToPopulate, primaryResource, nextCandidate, originalResourceId);
	}

	private static void reuseAlreadyIncludedGlobalResourceInSectionCollection(
			ResourceInclusionCollection theSectionResourceCollectionToPopulate,
			IBaseResource previouslyExistingResource,
			boolean primaryResource,
			String originalResourceId) {
		IBaseResource nextCandidate;
		ISectionResourceSupplier.InclusionTypeEnum previouslyIncludedResourceInclusionType =
				(ISectionResourceSupplier.InclusionTypeEnum)
						previouslyExistingResource.getUserData(RESOURCE_ENTRY_INCLUSION_TYPE);
		if (previouslyIncludedResourceInclusionType != ISectionResourceSupplier.InclusionTypeEnum.PRIMARY_RESOURCE) {
			if (primaryResource) {
				previouslyExistingResource.setUserData(
						RESOURCE_ENTRY_INCLUSION_TYPE, ISectionResourceSupplier.InclusionTypeEnum.PRIMARY_RESOURCE);
			}
		}

		nextCandidate = previouslyExistingResource;
		theSectionResourceCollectionToPopulate.addResourceIfNotAlreadyPresent(nextCandidate, originalResourceId);
	}

	@SuppressWarnings("unchecked")
	private void addSection(
			IIpsGenerationStrategy theStrategy,
			Section theSection,
			CompositionBuilder theCompositionBuilder,
			ResourceInclusionCollection theResourcesToInclude,
			ResourceInclusionCollection theGlobalResourcesToInclude) {

		CompositionBuilder.SectionBuilder sectionBuilder = theCompositionBuilder.addSection();

		sectionBuilder.setTitle(theSection.getTitle());
		sectionBuilder.addCodeCoding(
				theSection.getSectionSystem(), theSection.getSectionCode(), theSection.getSectionDisplay());

		for (IBaseResource next : theResourcesToInclude.getResources()) {
			ISectionResourceSupplier.InclusionTypeEnum inclusionType =
					(ISectionResourceSupplier.InclusionTypeEnum) next.getUserData(RESOURCE_ENTRY_INCLUSION_TYPE);
			if (inclusionType != ISectionResourceSupplier.InclusionTypeEnum.PRIMARY_RESOURCE) {
				continue;
			}

			IBaseHasExtensions extensionHolder = (IBaseHasExtensions) next;
			if (extensionHolder.getExtension().stream()
					.noneMatch(t -> t.getUrl().equals(URL_NARRATIVE_LINK))) {
				IBaseExtension<?, ?> narrativeLink = extensionHolder.addExtension();
				narrativeLink.setUrl(URL_NARRATIVE_LINK);
				String narrativeLinkValue =
						theCompositionBuilder.getComposition().getIdElement().getValue()
								+ "#"
								+ myFhirContext.getResourceType(next)
								+ "-"
								+ next.getIdElement().getValue();
				IPrimitiveType<String> narrativeLinkUri =
						(IPrimitiveType<String>) requireNonNull(myFhirContext.getElementDefinition("url"))
								.newInstance();
				narrativeLinkUri.setValueAsString(narrativeLinkValue);
				narrativeLink.setValue(narrativeLinkUri);
			}

			sectionBuilder.addEntry(next.getIdElement());
		}

		String narrative =
				createSectionNarrative(theStrategy, theSection, theResourcesToInclude, theGlobalResourcesToInclude);
		sectionBuilder.setText("generated", narrative);
	}

	private CompositionBuilder createComposition(
			IIpsGenerationStrategy theStrategy, IBaseResource thePatient, IpsContext context, IBaseResource author) {
		CompositionBuilder compositionBuilder = new CompositionBuilder(myFhirContext);
		compositionBuilder.setId(IdType.newRandomUuid());

		compositionBuilder.setStatus(Composition.CompositionStatus.FINAL.toCode());
		compositionBuilder.setSubject(thePatient.getIdElement().toUnqualifiedVersionless());
		compositionBuilder.addTypeCoding("http://loinc.org", "60591-5", "Patient Summary Document");
		compositionBuilder.setDate(InstantType.now());
		compositionBuilder.setTitle(theStrategy.createTitle(context));
		compositionBuilder.setConfidentiality(theStrategy.createConfidentiality(context));
		compositionBuilder.addAuthor(author.getIdElement());

		return compositionBuilder;
	}

	private void massageResourceId(
			IIpsGenerationStrategy theStrategy,
			RequestDetails theRequestDetails,
			IpsContext theIpsContext,
			IBaseResource theResource) {
		String base = theRequestDetails.getFhirServerBase();

		IIdType id = theResource.getIdElement();
		if (!id.hasBaseUrl() && id.hasResourceType() && id.hasIdPart()) {
			id = id.withServerBase(base, id.getResourceType());
			theResource.setId(id);
		}

		id = theStrategy.massageResourceId(theIpsContext, theResource);
		if (id != null) {
			theResource.setId(id);
		}
	}

	private String createSectionNarrative(
			IIpsGenerationStrategy theStrategy,
			Section theSection,
			ResourceInclusionCollection theResources,
			ResourceInclusionCollection theGlobalResourceCollection) {
		CustomThymeleafNarrativeGenerator generator = newNarrativeGenerator(theStrategy, theGlobalResourceCollection);

		Bundle bundle = new Bundle();
		for (IBaseResource resource : theResources.getResources()) {
			ISectionResourceSupplier.InclusionTypeEnum inclusionType =
					(ISectionResourceSupplier.InclusionTypeEnum) resource.getUserData(RESOURCE_ENTRY_INCLUSION_TYPE);
			if (inclusionType == ISectionResourceSupplier.InclusionTypeEnum.PRIMARY_RESOURCE) {
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
			IIpsGenerationStrategy theStrategy, ResourceInclusionCollection theGlobalResourceCollection) {
		List<String> narrativePropertyFiles = theStrategy.getNarrativePropertyFiles();
		CustomThymeleafNarrativeGenerator generator = new CustomThymeleafNarrativeGenerator(narrativePropertyFiles);
		generator.setFhirPathEvaluationContext(new IFhirPathEvaluationContext() {
			@Override
			public IBase resolveReference(@Nonnull IIdType theReference, @Nullable IBase theContext) {
				return theGlobalResourceCollection.getResourceById(theReference);
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
