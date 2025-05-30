/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.util.rdf;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.WriterOutputStream;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;

public class RDFUtil {

	public static Model initializeRDFModel() {
		// Create the model
		return ModelFactory.createDefaultModel();
	}

	public static Model readRDFToModel(final Reader reader, final Lang lang) throws IOException {
		// Jena has removed methods that use a generic Reader.
		// reads only from InputStream, StringReader, or String
		// Reader must be explicitly cast to StringReader
		Model rdfModel = initializeRDFModel();
		if (reader instanceof StringReader) {
			RDFDataMgr.read(rdfModel, (StringReader) reader, null, lang);
		} else if (reader instanceof InputStreamReader) {
			String content = IOUtils.toString(reader);
			RDFDataMgr.read(rdfModel, new StringReader(content), null, lang);
		}
		return rdfModel;
	}

	public static void writeRDFModel(Writer writer, Model rdfModel, Lang lang) throws IOException {
		// Jena has removed methods that use a generic Writer.
		// Writer must be explicitly cast to StringWriter or OutputStream
		// in order to hit a write method.
		OutputStream outputStream = WriterOutputStream.builder()
				.setWriter(writer)
				.setCharset("UTF-8")
				.get();
		RDFDataMgr.write(outputStream, rdfModel, lang);
	}
}
