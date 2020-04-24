package ca.uhn.fhir.util.rdf;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import org.apache.commons.io.input.ReaderInputStream;
import org.apache.commons.io.output.WriterOutputStream;
import org.apache.jena.graph.Triple;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.system.StreamRDF;
import org.apache.jena.riot.system.StreamRDFWriter;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

public class RDFUtil {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RDFUtil.class);
	private static final Map<String, Integer> VALID_ENTITY_NAMES;

	static {
		HashMap<String, Integer> validEntityNames = new HashMap<>(1448);
		VALID_ENTITY_NAMES = Collections.unmodifiableMap(validEntityNames);
	}

	public static StreamRDF createRDFWriter(final Writer writer, final Lang lang) {
		WriterOutputStream wos = new WriterOutputStream(writer, Charset.defaultCharset());
		return StreamRDFWriter.getWriterStream(wos, lang);
	}

	public static StreamRDF createRDFReader(final Reader reader, final Lang lang) {
		ReaderInputStream ris = new ReaderInputStream(reader, Charset.defaultCharset());
		return StreamRDFWriter.getWriterStream(null, lang);
	}

	public static Triple triple(String tripleAsTurtle) {
		Model m = ModelFactory.createDefaultModel();
		m.read(new StringReader(tripleAsTurtle), "urn:x-base:", "TURTLE");
		return m.listStatements().next().asTriple();
	}

}
