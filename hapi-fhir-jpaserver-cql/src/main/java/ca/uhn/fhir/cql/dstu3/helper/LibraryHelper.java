package ca.uhn.fhir.cql.dstu3.helper;

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
import org.apache.commons.lang3.StringUtils;
import org.cqframework.cql.cql2elm.CqlTranslatorOptions;
import org.cqframework.cql.cql2elm.ModelManager;
import org.cqframework.cql.cql2elm.model.Model;
import org.cqframework.cql.elm.execution.Library;
import org.cqframework.cql.elm.execution.VersionedIdentifier;
import org.hl7.fhir.dstu3.model.Attachment;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Measure;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.RelatedArtifact;
import org.hl7.fhir.dstu3.model.Resource;
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

public class LibraryHelper {
	private static final Logger ourLog = LoggerFactory.getLogger(LibraryHelper.class);

	private final Map<org.hl7.elm.r1.VersionedIdentifier, Model> modelCache;
	private final Map<VersionedIdentifier, Library> libraryCache;
	private final CqlTranslatorOptions translatorOptions;


	public LibraryHelper(Map<org.hl7.elm.r1.VersionedIdentifier, Model> modelCache, Map<VersionedIdentifier, Library> libraryCache, CqlTranslatorOptions translatorOptions) {
		this.modelCache = modelCache;
		this.libraryCache = libraryCache;
		this.translatorOptions = translatorOptions;
	}

	public org.opencds.cqf.cql.engine.execution.LibraryLoader createLibraryLoader(
		LibraryResolutionProvider<org.hl7.fhir.dstu3.model.Library> provider) {
		ModelManager modelManager = new CacheAwareModelManager(this.modelCache);

		List<org.opencds.cqf.cql.evaluator.cql2elm.content.LibraryContentProvider> contentProviders = Collections.singletonList(new LibraryContentProvider<org.hl7.fhir.dstu3.model.Library, Attachment>(
			provider, x -> x.getContent(), x -> x.getContentType(), x -> x.getData()));

		return new CacheAwareLibraryLoaderDecorator(new TranslatingLibraryLoader(modelManager, contentProviders, translatorOptions), libraryCache);
	}

	public List<Library> loadLibraries(Measure measure,
												  LibraryLoader libraryLoader,
												  LibraryResolutionProvider<org.hl7.fhir.dstu3.model.Library> libraryResourceProvider, RequestDetails theRequestDetails) {
		List<org.cqframework.cql.elm.execution.Library> libraries = new ArrayList<Library>();
		List<String> messages = new ArrayList<>();

		// load libraries
		//TODO: if there's a bad measure argument, this blows up for an obscure error
		for (Reference ref : measure.getLibrary()) {
			// if library is contained in measure, load it into server
			if (ref.getReferenceElement().getIdPart().startsWith("#")) {
				for (Resource resource : measure.getContained()) {
					if (resource instanceof org.hl7.fhir.dstu3.model.Library && resource.getIdElement().getIdPart()
						.equals(ref.getReferenceElement().getIdPart().substring(1))) {
						libraryResourceProvider.update((org.hl7.fhir.dstu3.model.Library) resource);
					}
				}
			}

			// We just loaded it into the server so we can access it by Id
			String id = ref.getReferenceElement().getIdPart();
			if (id.startsWith("#")) {
				id = id.substring(1);
			}

			org.hl7.fhir.dstu3.model.Library library = libraryResourceProvider.resolveLibraryById(id, theRequestDetails);
			if (library != null) {
				if (isLogicLibrary(library)) {
					libraries.add(libraryLoader
						.load(new VersionedIdentifier().withId(library.getName()).withVersion(library.getVersion())));
				} else {
					String message = "Skipping library " + library.getId() + " is not a logic library.  Probably missing type.coding.system=\"http://hl7.org/fhir/library-type\"";
					messages.add(message);
					ourLog.warn(message);
				}
			}
		}

		if (libraries.isEmpty()) {
			throw new IllegalArgumentException(Msg.code(1651) + String
				.format("Could not load library source for libraries referenced in %s:\n%s", measure.getId(), StringUtils.join("\n", messages)));
		}

		VersionedIdentifier primaryLibraryId = libraries.get(0).getIdentifier();
		org.hl7.fhir.dstu3.model.Library primaryLibrary = libraryResourceProvider.resolveLibraryByName(primaryLibraryId.getId(), primaryLibraryId.getVersion());
		for (RelatedArtifact artifact : primaryLibrary.getRelatedArtifact()) {
			if (artifact.hasType() && artifact.getType().equals(RelatedArtifact.RelatedArtifactType.DEPENDSON) && artifact.hasResource() && artifact.getResource().hasReference()) {
				if (artifact.getResource().getReferenceElement().getResourceType().equals("Library")) {
					org.hl7.fhir.dstu3.model.Library library = libraryResourceProvider.resolveLibraryById(artifact.getResource().getReferenceElement().getIdPart(), theRequestDetails);
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
		}

		return libraries;
	}

	private boolean isLogicLibrary(org.hl7.fhir.dstu3.model.Library library) {
		if (library == null) {
			return false;
		}

		if (!library.hasType()) {
			// If no type is specified, assume it is a logic library based on whether there is a CQL content element.
			if (library.hasContent()) {
				for (Attachment a : library.getContent()) {
					if (a.hasContentType() && (a.getContentType().equals("text/cql")
						|| a.getContentType().equals("application/elm+xml")
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
			if (c.hasSystem() && c.getSystem().equals("http://hl7.org/fhir/library-type")
				&& c.hasCode() && c.getCode().equals("logic-library")) {
				return true;
			}
		}

		return false;
	}

	public Library resolveLibraryById(String libraryId,
												 LibraryLoader libraryLoader,
												 LibraryResolutionProvider<org.hl7.fhir.dstu3.model.Library> libraryResourceProvider, RequestDetails theRequestDetails) {
		// Library library = null;

		org.hl7.fhir.dstu3.model.Library fhirLibrary = libraryResourceProvider.resolveLibraryById(libraryId, theRequestDetails);
		return libraryLoader
			.load(new VersionedIdentifier().withId(fhirLibrary.getName()).withVersion(fhirLibrary.getVersion()));
	}

	public Library resolvePrimaryLibrary(Measure measure,
													 LibraryLoader libraryLoader,
													 LibraryResolutionProvider<org.hl7.fhir.dstu3.model.Library> libraryResourceProvider, RequestDetails theRequestDetails) {
		// default is the first library reference
		String id = measure.getLibraryFirstRep().getReferenceElement().getIdPart();

		Library library = resolveLibraryById(id, libraryLoader, libraryResourceProvider, theRequestDetails);

		if (library == null) {
			throw new IllegalArgumentException(Msg.code(1652) + String.format("Could not resolve primary library for Measure/%s.",
				measure.getIdElement().getIdPart()));
		}

		return library;
	}
}
