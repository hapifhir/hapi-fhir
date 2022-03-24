package ca.uhn.fhir.jpa.search.builder;

import java.io.Closeable;
import java.util.Iterator;

public interface ISearchQueryExecutor extends Iterator<Long>, Closeable {
	/**
	 * Narrow the signature - no IOException allowed.
	 */
	@Override
	void close();
}
