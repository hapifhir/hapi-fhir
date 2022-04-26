package ca.uhn.fhir.jpa.search.builder;

import ca.uhn.fhir.jpa.dao.IResultIterator;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.hibernate.search.engine.search.query.SearchScroll;
import org.hibernate.search.engine.search.query.SearchScrollResult;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

/**
 * Adapts a SearchScroll<Long> as a IResultIterator
 */
public class FreetextQueryIteratorScrollAdapter implements IResultIterator {

	private SearchScroll<Long> myScroll;
	private Iterator<Long> myCurrentIterator;

	public FreetextQueryIteratorScrollAdapter(SearchScroll<Long> theScroll) {
		myScroll = theScroll;
		advanceNextScrollPage();
	}


	/**
	 * Unused in freetext search
	 */
	@Override
	public int getSkippedCount() { return 0; }

	/**
	 * Unused in freetext search
	 */
	@Override
	public int getNonSkippedCount() {
		return 0;
	}

	@Override
	public Collection<ResourcePersistentId> getNextResultBatch(long theBatchSize) {
		SearchScrollResult<Long> scrollResults = myScroll.next();
		return null;
	}

	@Override
	public void close() throws IOException { myScroll.close(); }

	@Override
	public boolean hasNext() {
		return myCurrentIterator.hasNext();
	}

	@Override
	public ResourcePersistentId next() {
		ResourcePersistentId result = new ResourcePersistentId(myCurrentIterator.next());
		if (!myCurrentIterator.hasNext()) {
			advanceNextScrollPage();
		}
		return result;
	}

	private void advanceNextScrollPage() {
		SearchScrollResult<Long> scrollResults = myScroll.next();
		myCurrentIterator = scrollResults.hits().iterator();
	}

}

