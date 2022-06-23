package ca.uhn.fhir.model.api;

import ca.uhn.fhir.i18n.Msg;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

public class PagingIterator<T> implements Iterator<T> {

	public interface PageFetcher<T> {
		void fetchNextPage(int thePageIndex, int theBatchSize, Consumer<T> theConsumer);
	}

	static final int PAGE_SIZE = 100;

	private int myPage;

	private boolean myIsFinished;

	private final LinkedList<T> myCurrentBatch = new LinkedList<>();

	private final PageFetcher<T> myFetcher;

	public PagingIterator(PageFetcher<T> theFetcher) {
		myFetcher = theFetcher;
	}

	@Override
	public boolean hasNext() {
		fetchNextBatch();

		return !myCurrentBatch.isEmpty();
	}

	@Override
	public T next() {
		fetchNextBatch();

		if (myCurrentBatch.isEmpty()) {
			throw new NoSuchElementException(Msg.code(2098) + " Nothing to fetch");
		}

		return myCurrentBatch.remove(0);
	}

	private void fetchNextBatch() {
		if (!myIsFinished && myCurrentBatch.isEmpty()) {
			myFetcher.fetchNextPage(myPage, PAGE_SIZE, myCurrentBatch::add);
			myPage++;
			myIsFinished = myCurrentBatch.size() < PAGE_SIZE;
		}
	}
}
