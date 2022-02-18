package ca.uhn.fhir.cql.r4.helper;

/*-
 * #%L
 * HAPI FHIR JPA Server - Clinical Quality Language
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.cql.common.provider.LibraryContentProvider;
import ca.uhn.fhir.cql.common.provider.LibraryResolutionProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.cqframework.cql.cql2elm.CqlTranslatorOptions;
import org.cqframework.cql.cql2elm.LibraryManager;
import org.cqframework.cql.cql2elm.ModelManager;
import org.cqframework.cql.cql2elm.model.Model;
import org.cqframework.cql.elm.execution.Library;
import org.cqframework.cql.elm.execution.VersionedIdentifier;
import org.hl7.fhir.r4.model.Attachment;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Measure;
import org.hl7.fhir.r4.model.PlanDefinition;
import org.hl7.fhir.r4.model.RelatedArtifact;
import org.hl7.fhir.r4.model.Resource;
import org.opencds.cqf.cql.engine.execution.LibraryLoader;
import org.opencds.cqf.cql.evaluator.cql2elm.model.CacheAwareModelManager;
import org.opencds.cqf.cql.evaluator.engine.execution.CacheAwareLibraryLoaderDecorator;
import org.opencds.cqf.cql.evaluator.engine.execution.TranslatingLibraryLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by Christopher on 1/11/2017.
 */
public class LibraryHelper {
	private static final Logger ourLog = LoggerFactory.getLogger(LibraryHelper.class);

	private final Map<org.hl7.elm.r1.VersionedIdentifier, Model> modelCache;
	private final Map<VersionedIdentifier, Library> libraryCache;
	private final CqlTranslatorOptions translatorOptions;

	public LibraryHelper(Map<org.hl7.elm.r1.VersionedIdentifier, Model> modelCache,
								Map<VersionedIdentifier, Library> libraryCache, CqlTranslatorOptions translatorOptions) {
		this.modelCache = modelCache;
		this.libraryCache = libraryCache;
		this.translatorOptions = translatorOptions;
	}

	public org.opencds.cqf.cql.engine.execution.LibraryLoader createLibraryLoader(
		LibraryResolutionProvider<org.hl7.fhir.r4.model.Library> provider) {
		ModelManager modelManager = new CacheAwareModelManager(this.modelCache);
		LibraryManager libraryManager = new LibraryManager(modelManager);
		libraryManager.getLibrarySourceLoader().clearProviders();
		List<org.opencds.cqf.cql.evaluator.cql2elm.content.LibraryContentProvider> contentProviders = Collections
				.singletonList(new LibraryContentProvider<org.hl7.fhir.r4.model.Library, Attachment>(provider,
						x -> x.getContent(), x -> x.getContentType(), x -> x.getData()));

		return new CacheAwareLibraryLoaderDecorator(
				new TranslatingLibraryLoader(modelManager, contentProviders, translatorOptions), libraryCache);
	}

	public org.opencds.cqf.cql.engine.execution.LibraryLoader createLibraryLoader(
			org.cqframework.cql.cql2elm.LibrarySourceProvider provider) {
		ModelManager modelManager = new CacheAwareModelManager(this.modelCache);
		LibraryManager libraryManager = new LibraryManager(modelManager);
		libraryManager.getLibrarySourceLoader().clearProviders();

		libraryManager.getLibrarySourceLoader().registerProvider(provider);

		return new CacheAwareLibraryLoaderDecorator(new TranslatingLibraryLoader(modelManager, null, translatorOptions),
				libraryCache);
	}

	public org.hl7.fhir.r4.model.Library resolveLibraryReference(
		LibraryResolutionProvider<org.hl7.fhir.r4.model.Library> libraryResourceProvider, String reference, RequestDetails theRequestDetails) {
		// Raw references to Library/libraryId or libraryId
		if (reference.startsWith("Library/") || !reference.contains("/")) {
			return libraryResourceProvider.resolveLibraryById(reference.replace("Library/", ""), theRequestDetails);
		}
		// Full url (e.g. http://hl7.org/fhir/us/Library/FHIRHelpers)
		else if (reference.contains(("/Library/"))) {
			return libraryResourceProvider.resolveLibraryByCanonicalUrl(reference, theRequestDetails);
		}

		return null;
	}

	public List<org.cqframework.cql.elm.execution.Library> loadLibraries(Measure measure,
																								LibraryLoader libraryLoader,
																								LibraryResolutionProvider<org.hl7.fhir.r4.model.Library> libraryResourceProvider, RequestDetails theRequestDetails) {
		List<org.cqframework.cql.elm.execution.Library> libraries = new ArrayList<org.cqframework.cql.elm.execution.Library>();

		// load libraries
		// TODO: if there's a bad measure argument, this blows up for an obscure error
		org.hl7.fhir.r4.model.Library primaryLibrary = null;
		for (CanonicalType ref : measure.getLibrary()) {
			// if library is contained in measure, load it into server
			String id = ref.getValue(); // CanonicalHelper.getId(ref);
			if (id.startsWith("#")) {
				id = id.substring(1);
				for (Resource resource : measure.getContained()) {
					if (resource instanceof org.hl7.fhir.r4.model.Library
						&& resource.getIdElement().getIdPart().equals(id)) {
						libraryResourceProvider.update((org.hl7.fhir.r4.model.Library) resource);
					}
				}
			}

			// We just loaded it into the server so we can access it by Id
			org.hl7.fhir.r4.model.Library library = resolveLibraryReference(libraryResourceProvider, id, theRequestDetails);
			if (primaryLibrary == null) {
				primaryLibrary = library;
			}

			if (library != null && isLogicLibrary(library)) {
				libraries.add(libraryLoader
					.load(new VersionedIdentifier().withId(library.getName()).withVersion(library.getVersion())));
			}
		}

		if (libraries.isEmpty()) {
			throw new IllegalArgumentException(Msg.code(1677) + String.format("Could not load library source for libraries referenced in %s.", measure.getId()));
		}

		for (RelatedArtifact artifact : primaryLibrary.getRelatedArtifact()) {
			if (artifact.hasType() && artifact.getType().equals(RelatedArtifact.RelatedArtifactType.DEPENDSON)
					&& artifact.hasResource()) {
				org.hl7.fhir.r4.model.Library library = null;
				library = resolveLibraryReference(libraryResourceProvider, artifact.getResource(), theRequestDetails);

				if (library != null) {
					if (isLogicLibrary(library)) {
						libraries.add(libraryLoader
							.load(new VersionedIdentifier().withId(library.getName()).withVersion(library.getVersion())));
					} else {
						ourLog.warn("Library {} not included as part of evaluation context. Only Libraries with the 'logic-library' type are included.", library.getId());
					}
				}
			}
		}

		return libraries;
	}

	private boolean isLogicLibrary(org.hl7.fhir.r4.model.Library library) {
		if (library == null) {
			return false;
		}

		if (!library.hasType()) {
			// If no type is specified, assume it is a logic library based on whether there
			// is a CQL content element.
			if (library.hasContent()) {
				for (Attachment a : library.getContent()) {
					if (a.hasContentType()
							&& (a.getContentType().equals("text/cql") || a.getContentType().equals("application/elm+xml")
									|| a.getContentType().equals("application/elm+json"))) {
						return true;
					}
				}
			}
			return false;
		}

		if (!library.getType().hasCoding()) {
			return false;
		}

		for (Coding c : library.getType().getCoding()) {
			if (c.hasSystem() && c.getSystem().equals("http://terminology.hl7.org/CodeSystem/library-type") && c.hasCode()
				&& c.getCode().equals("logic-library")) {
				return true;
			}
		}

		return false;
	}

	public Library resolveLibraryById(String libraryId, LibraryLoader libraryLoader,
												 LibraryResolutionProvider<org.hl7.fhir.r4.model.Library> libraryResourceProvider, RequestDetails theRequestDetails) {

		org.hl7.fhir.r4.model.Library fhirLibrary = libraryResourceProvider.resolveLibraryById(libraryId, theRequestDetails);
		return libraryLoader
			.load(new VersionedIdentifier().withId(fhirLibrary.getName()).withVersion(fhirLibrary.getVersion()));
	}

	public Library resolvePrimaryLibrary(Measure measure,
													 LibraryLoader libraryLoader,
													 LibraryResolutionProvider<org.hl7.fhir.r4.model.Library> libraryResourceProvider, RequestDetails theRequestDetails) {
		// default is the first library reference
		String id = CanonicalHelper.getId(measure.getLibrary().get(0));

		Library library = resolveLibraryById(id, libraryLoader, libraryResourceProvider, theRequestDetails);

		if (library == null) {
			throw new IllegalArgumentException(Msg.code(1678) + String.format("Could not resolve primary library for Measure/%s.", measure.getIdElement().getIdPart()));
		}

		return library;
	}

	public Library resolvePrimaryLibrary(PlanDefinition planDefinition,
													 LibraryLoader libraryLoader,
													 LibraryResolutionProvider<org.hl7.fhir.r4.model.Library> libraryResourceProvider, RequestDetails theRequestDetails) {
		String id = CanonicalHelper.getId(planDefinition.getLibrary().get(0));

		Library library = resolveLibraryById(id, libraryLoader, libraryResourceProvider, theRequestDetails);

		if (library == null) {
			throw new IllegalArgumentException(Msg.code(1679) + String.format("Could not resolve primary library for PlanDefinition/%s",
				planDefinition.getIdElement().getIdPart()));
		}

		return library;
	}
}
