package ca.uhn.fhir.cql.common.evaluation;

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
import org.apache.commons.lang3.Validate;
import org.cqframework.cql.cql2elm.CqlTranslator;
import org.cqframework.cql.cql2elm.CqlTranslatorException;
import org.cqframework.cql.cql2elm.CqlTranslatorOptions;
import org.cqframework.cql.cql2elm.LibraryManager;
import org.cqframework.cql.cql2elm.ModelManager;
import org.cqframework.cql.elm.execution.Library;
import org.cqframework.cql.elm.execution.VersionedIdentifier;

import javax.xml.bind.JAXBException;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ca.uhn.fhir.cql.common.helper.TranslatorHelper.errorsToString;
import static ca.uhn.fhir.cql.common.helper.TranslatorHelper.getTranslator;
import static ca.uhn.fhir.cql.common.helper.TranslatorHelper.readLibrary;

public class LibraryLoader implements org.opencds.cqf.cql.engine.execution.LibraryLoader {
	private LibraryManager libraryManager;
	private ModelManager modelManager;
	private Map<String, Library> libraries = new HashMap<>();

	// private static final Logger logger =
	// LoggerFactory.getLogger(LibraryLoader.class);

	public Collection<Library> getLibraries() {
		return this.libraries.values();
	}

	public LibraryManager getLibraryManager() {
		return this.libraryManager;
	}

	public ModelManager getModelManager() {
		return this.modelManager;
	}

	public LibraryLoader(LibraryManager libraryManager, ModelManager modelManager) {
		this.libraryManager = libraryManager;
		this.modelManager = modelManager;
	}

	private Library resolveLibrary(VersionedIdentifier libraryIdentifier) {
		Validate.notNull(libraryIdentifier, "Library identifier is null.");

		Validate.notNull(libraryIdentifier.getId(), "Library identifier id is null.");

		String mangledId = this.mangleIdentifer(libraryIdentifier);

		Library library = libraries.get(mangledId);
		if (library == null) {
			library = loadLibrary(libraryIdentifier);
			libraries.put(mangledId, library);
		}

		return library;
	}

	private String mangleIdentifer(VersionedIdentifier libraryIdentifier) {
		String id = libraryIdentifier.getId();
		String version = libraryIdentifier.getVersion();

		return version == null ? id : id + "-" + version;
	}

	private Library loadLibrary(VersionedIdentifier libraryIdentifier) {
		org.hl7.elm.r1.VersionedIdentifier identifier = new org.hl7.elm.r1.VersionedIdentifier()
			.withId(libraryIdentifier.getId()).withSystem(libraryIdentifier.getSystem())
			.withVersion(libraryIdentifier.getVersion());

		ArrayList<CqlTranslatorException> errors = new ArrayList<>();
		org.hl7.elm.r1.Library translatedLibrary = libraryManager.resolveLibrary(identifier, CqlTranslatorOptions.defaultOptions(), errors).getLibrary();

		if (CqlTranslatorException.HasErrors(errors)) {
			throw new IllegalArgumentException(Msg.code(1657) + errorsToString(errors));
		}
		try {
			CqlTranslator translator = getTranslator("", libraryManager, modelManager);

			if (translator.getErrors().size() > 0) {
				throw new IllegalArgumentException(Msg.code(1658) + errorsToString(translator.getErrors()));
			}

			return readLibrary(new ByteArrayInputStream(
				translator.convertToXml(translatedLibrary).getBytes(StandardCharsets.UTF_8)));
		} catch (JAXBException e) {
			throw new IllegalArgumentException(Msg.code(1659) + String.format("Errors occurred translating library %s%s.",
				identifier.getId(), identifier.getVersion() != null ? ("-" + identifier.getVersion()) : ""));
		}
	}

	@Override
	public Library load(VersionedIdentifier versionedIdentifier) {
		return resolveLibrary(versionedIdentifier);
	}
}
