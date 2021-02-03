package ca.uhn.fhir.cql.dstu3.helper;

/*-
 * #%L
 * HAPI FHIR JPA Server - Clinical Quality Language
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.cql.common.evaluation.LibraryLoader;
import ca.uhn.fhir.cql.common.provider.LibraryResolutionProvider;
import ca.uhn.fhir.cql.common.provider.LibrarySourceProvider;
import org.cqframework.cql.cql2elm.LibraryManager;
import org.cqframework.cql.cql2elm.ModelManager;
import org.cqframework.cql.cql2elm.model.Model;
import org.cqframework.cql.elm.execution.Library;
import org.cqframework.cql.elm.execution.VersionedIdentifier;
import org.hl7.fhir.dstu3.model.Attachment;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Measure;
import org.hl7.fhir.dstu3.model.PlanDefinition;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.RelatedArtifact;
import org.hl7.fhir.dstu3.model.Resource;
import org.opencds.cqf.cql.evaluator.cql2elm.model.CacheAwareModelManager;
import org.opencds.cqf.cql.evaluator.engine.execution.PrivateCachingLibraryLoaderDecorator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LibraryHelper {

	private Map<org.hl7.elm.r1.VersionedIdentifier, Model> modelCache;

	public LibraryHelper(Map<org.hl7.elm.r1.VersionedIdentifier, Model> modelCache) {
		this.modelCache = modelCache;
	}

	public org.opencds.cqf.cql.engine.execution.LibraryLoader createLibraryLoader(
		LibraryResolutionProvider<org.hl7.fhir.dstu3.model.Library> provider) {
		ModelManager modelManager = new CacheAwareModelManager(this.modelCache);
		LibraryManager libraryManager = new LibraryManager(modelManager);
		libraryManager.getLibrarySourceLoader().clearProviders();

		libraryManager.getLibrarySourceLoader().registerProvider(
			new LibrarySourceProvider<org.hl7.fhir.dstu3.model.Library, Attachment>(
				provider, x -> x.getContent(), x -> x.getContentType(), x -> x.getData()));

		return new PrivateCachingLibraryLoaderDecorator(new LibraryLoader(libraryManager, modelManager));
	}

	public List<Library> loadLibraries(Measure measure,
															org.opencds.cqf.cql.engine.execution.LibraryLoader libraryLoader,
															LibraryResolutionProvider<org.hl7.fhir.dstu3.model.Library> libraryResourceProvider) {
		List<org.cqframework.cql.elm.execution.Library> libraries = new ArrayList<Library>();

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

			org.hl7.fhir.dstu3.model.Library library = libraryResourceProvider.resolveLibraryById(id);
			if (library != null && isLogicLibrary(library)) {
				libraries.add(libraryLoader
					.load(new VersionedIdentifier().withId(library.getName()).withVersion(library.getVersion())));
			}
		}

		if (libraries.isEmpty()) {
			throw new IllegalArgumentException(String
				.format("Could not load library source for libraries referenced in Measure %s.", measure.getId()));
		}

		VersionedIdentifier primaryLibraryId = libraries.get(0).getIdentifier();
		org.hl7.fhir.dstu3.model.Library primaryLibrary = libraryResourceProvider.resolveLibraryByName(primaryLibraryId.getId(), primaryLibraryId.getVersion());
		for (RelatedArtifact artifact : primaryLibrary.getRelatedArtifact()) {
			if (artifact.hasType() && artifact.getType().equals(RelatedArtifact.RelatedArtifactType.DEPENDSON) && artifact.hasResource() && artifact.getResource().hasReference()) {
				if (artifact.getResource().getReferenceElement().getResourceType().equals("Library")) {
					org.hl7.fhir.dstu3.model.Library library = libraryResourceProvider.resolveLibraryById(artifact.getResource().getReferenceElement().getIdPart());

					if (library != null && isLogicLibrary(library)) {
						libraries.add(
							libraryLoader.load(new VersionedIdentifier().withId(library.getName()).withVersion(library.getVersion()))
						);
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
																							org.opencds.cqf.cql.engine.execution.LibraryLoader libraryLoader,
																							LibraryResolutionProvider<org.hl7.fhir.dstu3.model.Library> libraryResourceProvider) {
		// Library library = null;

		org.hl7.fhir.dstu3.model.Library fhirLibrary = libraryResourceProvider.resolveLibraryById(libraryId);
		return libraryLoader
			.load(new VersionedIdentifier().withId(fhirLibrary.getName()).withVersion(fhirLibrary.getVersion()));

		// for (Library l : libraryLoader.getLibraries()) {
		// VersionedIdentifier vid = l.getIdentifier();
		// if (vid.getId().equals(fhirLibrary.getName()) &&
		// LibraryResourceHelper.compareVersions(fhirLibrary.getVersion(),
		// vid.getVersion()) == 0) {
		// library = l;
		// break;
		// }
		// }

		// if (library == null) {

		// }

		// return library;
	}

	public Library resolvePrimaryLibrary(Measure measure,
																								org.opencds.cqf.cql.engine.execution.LibraryLoader libraryLoader,
																								LibraryResolutionProvider<org.hl7.fhir.dstu3.model.Library> libraryResourceProvider) {
		// default is the first library reference
		String id = measure.getLibraryFirstRep().getReferenceElement().getIdPart();

		Library library = resolveLibraryById(id, libraryLoader, libraryResourceProvider);

		if (library == null) {
			throw new IllegalArgumentException(String.format("Could not resolve primary library for Measure/%s.",
				measure.getIdElement().getIdPart()));
		}

		return library;
	}

	public Library resolvePrimaryLibrary(PlanDefinition planDefinition, org.opencds.cqf.cql.engine.execution.LibraryLoader libraryLoader,
																								LibraryResolutionProvider<org.hl7.fhir.dstu3.model.Library> libraryResourceProvider) {
		String id = planDefinition.getLibraryFirstRep().getReferenceElement().getIdPart();

		Library library = resolveLibraryById(id, libraryLoader, libraryResourceProvider);

		if (library == null) {
			throw new IllegalArgumentException(String.format("Could not resolve primary library for PlanDefinition/%s",
				planDefinition.getIdElement().getIdPart()));
		}

		return library;
	}
}
