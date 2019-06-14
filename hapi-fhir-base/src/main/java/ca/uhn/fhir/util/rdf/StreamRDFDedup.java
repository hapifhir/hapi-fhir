package ca.uhn.fhir.util.rdf;

import java.util.HashSet;
import java.util.LinkedList;

import org.apache.jena.graph.Triple;
import org.apache.jena.riot.system.StreamRDF;
import org.apache.jena.sparql.core.Quad;

/**
 * Wraps another {@link StreamRDF} and attempts to remove duplicate
 * triples and quads. To maintain streaming, duplicates are only
 * removed within a sliding window of configurable size. Default
 * size is 10000 triples and quads.
 */
public class StreamRDFDedup implements StreamRDF {
	private final StreamRDF wrapped;
	private final int windowSize;
	private final HashSet<Object> tripleAndQuadCache;
	private final LinkedList<Object> tripleAndQuadList = new LinkedList<Object>();

	public StreamRDFDedup(StreamRDF wrapped) {
		this(wrapped, 10000);
	}

	public StreamRDFDedup(StreamRDF wrapped, int windowSize) {
		this.wrapped = wrapped;
		this.windowSize = windowSize;
		// Initial capacity big enough to avoid rehashing
		this.tripleAndQuadCache = new HashSet<Object>(windowSize * 3 / 2);
	}

	@Override
	public void start() {
		wrapped.start();
	}

	@Override
	public void triple(Triple triple) {
		if (!seen(triple)) {
			wrapped.triple(triple);
		}
	}

	@Override
	public void quad(Quad quad) {
		if (!seen(quad)) {
			wrapped.quad(quad);
		}
	}

	@Override
	public void base(String base) {
		wrapped.base(base);
	}

	@Override
	public void prefix(String prefix, String iri) {
		wrapped.prefix(prefix, iri);
	}

	@Override
	public void finish() {
		wrapped.finish();
	}

	private boolean seen(Object tuple) {
		if (tripleAndQuadCache.contains(tuple)) {
			return true;
		}
		tripleAndQuadCache.add(tuple);
		tripleAndQuadList.add(tuple);
		if (tripleAndQuadList.size() > windowSize) {
			forgetOldest();
		}
		return false;
	}

	private void forgetOldest() {
		tripleAndQuadCache.remove(tripleAndQuadList.removeFirst());
	}
}
