package ca.uhn.fhir.util.rdf;

/*-
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

import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.jena.atlas.io.IndentedWriter;
import org.apache.jena.graph.Triple;
import org.apache.jena.riot.system.RiotLib;
import org.apache.jena.riot.system.StreamOps;
import org.apache.jena.riot.system.StreamRDF;
import org.apache.jena.riot.writer.WriterStreamRDFBlocks;
import org.apache.jena.riot.writer.WriterStreamRDFPlain;
import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.shared.impl.PrefixMappingImpl;
import org.apache.jena.vocabulary.RDF;


/**
 * Writes an iterator over triples to N-Triples or Turtle
 * in a streaming fashion, that is, without needing to hold
 * the entire thing in memory.
 * <p>
 * Instances are single-use.
 * <p>
 * There doesn't seem to be a pre-packaged version of this
 * functionality in Jena/ARQ that doesn't require a Graph or Model.
 */
public class StreamingRDFWriter {
	private final OutputStream out;
	private final Iterator<Triple> triples;
	private int dedupWindowSize = 10000;

	public StreamingRDFWriter(OutputStream out, Iterator<Triple> triples) {
		this.out = out;
		this.triples = triples;
	}

	public void setDedupWindowSize(int newSize) {
		this.dedupWindowSize = newSize;
	}

	public void writeNTriples() {
		StreamRDF writer = new WriterStreamRDFPlain(new IndentedWriter(out));
		if (dedupWindowSize > 0) {
			writer = new StreamRDFDedup(writer, dedupWindowSize);
		}
		writer.start();
		StreamOps.sendTriplesToStream(triples, writer);
		writer.finish();
	}

	public void writeTurtle(String baseIRI, PrefixMapping prefixes, boolean writeBase) {
		// Auto-register RDF prefix so that rdf:type is displayed well
		// All other prefixes come from the query and should be as author intended
		prefixes = ensureRDFPrefix(prefixes);

		if (writeBase) {
			// Jena's streaming Turtle writers don't output base even if it is provided,
			// so we write it directly.
			IndentedWriter w = new IndentedWriter(out);
			RiotLib.writeBase(w, baseIRI);
			w.flush();
		}

		StreamRDF writer = new WriterStreamRDFBlocks(out);
		if (dedupWindowSize > 0) {
			writer = new StreamRDFDedup(writer, dedupWindowSize);
		}
		writer.start();
		writer.base(baseIRI);
		for (Entry<String, String> e : prefixes.getNsPrefixMap().entrySet()) {
			writer.prefix(e.getKey(), e.getValue());
		}
		StreamOps.sendTriplesToStream(triples, writer);
		writer.finish();
	}

	private PrefixMapping ensureRDFPrefix(PrefixMapping prefixes) {
		// Some prefix already registered for the RDF namespace -- good enough
		if (prefixes.getNsURIPrefix(RDF.getURI()) != null) return prefixes;
		// rdf: is registered to something else -- give up
		if (prefixes.getNsPrefixURI("rdf") != null) return prefixes;
		// Register rdf:
		PrefixMapping newPrefixes = new PrefixMappingImpl();
		newPrefixes.setNsPrefixes(prefixes);
		newPrefixes.setNsPrefix("rdf", RDF.getURI());
		return newPrefixes;
	}
}
