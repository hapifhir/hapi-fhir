package ca.uhn.fhir.jpa.provider.validation.performance;

import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class CanonicalizationMethod {

	private final String name;
	private final List<CanonicalizationMethodInvocation> myInvocations;

	public CanonicalizationMethod(String theName) {
		name = theName;
		myInvocations = new ArrayList<>();
	}

	public void addInvocation(String theResource, long theMillis, List<String> theStacktrace, int theThreadOffSet, int theMaxThreadCount) {
		myInvocations.add(new CanonicalizationMethodInvocation(this, theResource, theMillis, theStacktrace, theThreadOffSet, theMaxThreadCount));
	}

	public void reset() {
		myInvocations.clear();
	}

	public String writeMetrics(int theInvocationLimit, @Nullable Comparator<CanonicalizationMethodInvocation> theComparator, @Nullable Predicate<CanonicalizationMethodInvocation> theFilter) {
		StringBuilder sb = new StringBuilder()
			.append("=== Invocations for ").append(name)
			.append(" [")
			.append("time=").append(getElapsedTime()).append("ms")
			.append(", count=").append(getCount())
			.append(", average=").append(getCount() == 0 ? 0 : getElapsedTime() / getCount()).append("ms")
			.append("] ===");


		List<CanonicalizationMethodInvocation> invocations = getInvocations();
		if (theComparator != null) {
			invocations.sort(theComparator);
		}

		Stream<CanonicalizationMethodInvocation> stream = myInvocations.stream().limit(theInvocationLimit);
		if (theFilter != null){
			stream = stream.filter(theFilter);
		}

		stream.forEach(invocation -> sb.append("\n").append(invocation));

		return sb.toString();
	}

	public String getName() {
		return name;
	}

	public List<CanonicalizationMethodInvocation> getInvocations() {
		return myInvocations;
	}

	public long getElapsedTime() {
		AtomicLong total = new AtomicLong(0L);
		getInvocations().forEach(invocation -> total.addAndGet(invocation.getElapsedTime()));
		return total.get();
	}

	public long getCount() {
		return getInvocations().size();
	}
}
