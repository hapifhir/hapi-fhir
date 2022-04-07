package ca.uhn.fhir.util.rdf;

/*
 * #%L
 * HAPI FHIR - Core Library
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

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import java.io.*;

public class RDFUtil {

	public static Model initializeRDFModel() {
		// Create the model
		return ModelFactory.createDefaultModel();
	}

	public static Model readRDFToModel(final Reader reader, final Lang lang) {
		Model rdfModel = initializeRDFModel();
		RDFDataMgr.read(rdfModel, reader, null, lang);
		return rdfModel;
	}

	public static void writeRDFModel(Writer writer, Model rdfModel, Lang lang) {
		// This writes to the provided Writer.
		// Jena has deprecated methods that use a generic Writer
		// writer could be explicitly casted to StringWriter in order to hit a
		// non-deprecated overload
		RDFDataMgr.write(writer, rdfModel, lang);
	}
}
