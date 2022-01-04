package ca.uhn.fhir.cql.common.helper;

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
import org.cqframework.cql.cql2elm.CqlTranslator;
import org.cqframework.cql.cql2elm.CqlTranslatorException;
import org.cqframework.cql.cql2elm.LibraryManager;
import org.cqframework.cql.cql2elm.ModelManager;
import org.cqframework.cql.elm.execution.Library;
import org.cqframework.cql.elm.tracking.TrackBack;
import org.opencds.cqf.cql.engine.execution.CqlLibraryReader;

import javax.xml.bind.JAXBException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class TranslatorHelper {
	public static Library readLibrary(InputStream xmlStream) {
		try {
			return CqlLibraryReader.read(xmlStream);
		} catch (IOException | JAXBException e) {
			throw new IllegalArgumentException(Msg.code(1660) + "Error encountered while reading ELM xml: " + e.getMessage());
		}
	}

	public static String errorsToString(Iterable<CqlTranslatorException> exceptions) {
		ArrayList<String> errors = new ArrayList<>();
		for (CqlTranslatorException error : exceptions) {
			TrackBack tb = error.getLocator();
			String lines = tb == null ? "[n/a]"
				: String.format("%s [%d:%d, %d:%d] ",
				(tb.getLibrary() != null ? tb.getLibrary().getId()
					+ (tb.getLibrary().getVersion() != null ? ("-" + tb.getLibrary().getVersion()) : "")
					: ""),
				tb.getStartLine(), tb.getStartChar(), tb.getEndLine(), tb.getEndChar());
			errors.add(lines + error.getMessage());
		}

		return String.join("\n", errors);
	}

	public static CqlTranslator getTranslator(String cql, LibraryManager libraryManager, ModelManager modelManager) {
		return getTranslator(new ByteArrayInputStream(cql.getBytes(StandardCharsets.UTF_8)), libraryManager,
			modelManager);
	}

	public static CqlTranslator getTranslator(InputStream cqlStream, LibraryManager libraryManager,
															ModelManager modelManager) {
		ArrayList<CqlTranslator.Options> options = new ArrayList<>();
		options.add(CqlTranslator.Options.EnableAnnotations);
		options.add(CqlTranslator.Options.EnableLocators);
		options.add(CqlTranslator.Options.DisableListDemotion);
		options.add(CqlTranslator.Options.DisableListPromotion);
		options.add(CqlTranslator.Options.DisableMethodInvocation);
		CqlTranslator translator;
		try {
			translator = CqlTranslator.fromStream(cqlStream, modelManager, libraryManager,
				options.toArray(new CqlTranslator.Options[options.size()]));
		} catch (IOException e) {
			throw new IllegalArgumentException(Msg.code(1661) + String.format("Errors occurred translating library: %s", e.getMessage()));
		}

		return translator;
	}

	public static Library translateLibrary(String cql, LibraryManager libraryManager, ModelManager modelManager) {
		return translateLibrary(new ByteArrayInputStream(cql.getBytes(StandardCharsets.UTF_8)), libraryManager,
			modelManager);
	}

	public static Library translateLibrary(InputStream cqlStream, LibraryManager libraryManager,
														ModelManager modelManager) {
		CqlTranslator translator = getTranslator(cqlStream, libraryManager, modelManager);
		return readLibrary(new ByteArrayInputStream(translator.toXml().getBytes(StandardCharsets.UTF_8)));
	}

	public static Library translateLibrary(CqlTranslator translator) {
		return readLibrary(new ByteArrayInputStream(translator.toXml().getBytes(StandardCharsets.UTF_8)));
	}
}
