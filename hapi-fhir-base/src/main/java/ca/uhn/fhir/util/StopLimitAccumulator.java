package ca.uhn.fhir.util;

import jakarta.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

public class StopLimitAccumulator<T> {
	private final boolean isTruncated;
	private final List<T> myList;

	private StopLimitAccumulator(List<T> theList, boolean theIsTruncated) {
		myList = Collections.unmodifiableList(theList);
		isTruncated = theIsTruncated;
	}

	public static <T> StopLimitAccumulator<T> fromStreamAndLimit(@Nonnull Stream<T> thePidStream, long theLimit) {
		assert theLimit > 0;
		AtomicBoolean isBeyondLimit = new AtomicBoolean(false);
		List<T> accumulator = new ArrayList<>();

		thePidStream
			.limit(theLimit + 1) // Fetch one extra item to see if there are more items past our limit
			.forEach(item -> {
				if (accumulator.size() < theLimit) {
					accumulator.add(item);
				} else {
					isBeyondLimit.set(true);
				}
			});
		return new StopLimitAccumulator<>(accumulator, isBeyondLimit.get());
	}

	public boolean isTruncated() {
		return isTruncated;
	}

	public List<T> getItemList() {
		return myList;
	}
}
