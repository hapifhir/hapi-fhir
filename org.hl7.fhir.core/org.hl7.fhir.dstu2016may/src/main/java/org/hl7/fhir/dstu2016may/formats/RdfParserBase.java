package org.hl7.fhir.dstu2016may.formats;

/*-
 * #%L
 * org.hl7.fhir.dstu2016may
 * %%
 * Copyright (C) 2014 - 2019 Health Level 7
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


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.hl7.fhir.dstu2016may.formats.RdfGenerator.Complex;
import org.hl7.fhir.dstu2016may.formats.RdfGenerator.Section;
import org.hl7.fhir.dstu2016may.formats.RdfGenerator.Subject;
import org.hl7.fhir.dstu2016may.model.CodeType;
import org.hl7.fhir.dstu2016may.model.CodeableConcept;
import org.hl7.fhir.dstu2016may.model.Coding;
import org.hl7.fhir.dstu2016may.model.Enumeration;
import org.hl7.fhir.dstu2016may.model.Resource;
import org.hl7.fhir.dstu2016may.model.Type;
import org.hl7.fhir.exceptions.FHIRFormatError;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;

public abstract class RdfParserBase extends ParserBase implements IParser  {

	protected abstract void composeResource(Complex complex, Resource resource) throws IOException;

	@Override
	public ParserType getType() {
		return ParserType.RDF_TURTLE;
	}

	@Override
	public Resource parse(InputStream input) throws IOException, FHIRFormatError {
		throw new Error("Parsing not implemented yet");
	}

	@Override
	public Type parseType(InputStream input, String knownType) throws IOException, FHIRFormatError {
		throw new Error("Parsing not implemented yet");
	}

	private String url;

	@Override
	public void compose(OutputStream stream, Resource resource) throws IOException {
		RdfGenerator ttl = new RdfGenerator(stream);
		//      ttl.setFormat(FFormat);
		ttl.prefix("fhir", "http://hl7.org/fhir/");
		ttl.prefix("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
		Section section = ttl.section("resource");
		Subject subject;
		if (url != null) 
			subject = section.triple("<"+url+">", "a", "fhir:"+resource.getResourceType().toString());
		else
			subject = section.triple("_", "a", "fhir:"+resource.getResourceType().toString());

		composeResource(subject, resource);
		try {
			ttl.commit(false);
		} catch (Exception e) {
			throw new IOException(e); 
		}
	}

	@Override
	public void compose(OutputStream stream, Type type, String rootName) throws IOException {
		throw new Error("Not supported in RDF");  
	}

	protected String ttlLiteral(String value) {
		return "\"" +RdfGenerator.escape(value, true) + "\"";
	}

	protected void composeXhtml(Complex t, String string, String string2, XhtmlNode div, int i) {
	}

	protected void decorateCode(Complex t, Enumeration<? extends Enum> value) {
	}

	protected void decorateCode(Complex t, CodeType value) {
	}

	protected void decorateCoding(Complex t, Coding element) {
		if (!element.hasSystem())
			return;
		if ("http://snomed.info/sct".equals(element.getSystem())) {
			t.prefix("sct", "http://snomed.info/sct/");
			t.predicate("a", "sct:"+element.getCode());
		} else if ("http://snomed.info/sct".equals(element.getSystem())) {
			t.prefix("loinc", "http://loinc.org/rdf#");
			t.predicate("a", "loinc:"+element.getCode());
		}  
	}

	protected void decorateCodeableConcept(Complex t, CodeableConcept element) {
		for (Coding c : element.getCoding())
			decorateCoding(t, c);
	}


}
