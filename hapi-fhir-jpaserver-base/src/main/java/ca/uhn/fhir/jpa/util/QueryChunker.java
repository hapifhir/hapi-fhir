package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.jpa.dao.SearchBuilder;

import java.util.List;
import java.util.function.Consumer;

/**
 * As always, Oracle can't handle things that other databases don't mind.. In this
 * case it doesn't like more than ~1000 IDs in a single load, so we break this up
 * if it's lots of IDs. I suppose maybe we should be doing this as a join anyhow
 * but this should work too. Sigh.
 */
public class QueryChunker<T> {

	public void chunk(List<T> theInput, Consumer<List<T>> theBatchConsumer) {
		for (int i = 0; i < theInput.size(); i += SearchBuilder.MAXIMUM_PAGE_SIZE) {
			int to = i + SearchBuilder.MAXIMUM_PAGE_SIZE;
			to = Math.min(to, theInput.size());
			List<T> batch = theInput.subList(i, to);
			theBatchConsumer.accept(batch);
		}
	}

}
