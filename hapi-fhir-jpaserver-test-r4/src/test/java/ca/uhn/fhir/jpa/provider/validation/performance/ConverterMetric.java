package ca.uhn.fhir.jpa.provider.validation.performance;

import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ConverterMetric {

	private final String myMethod;
	private final List<ConverterInvocation> myInvocations;

	public ConverterMetric(String theMethod) {
		myMethod = theMethod;
		myInvocations = new ArrayList<>();
	}

	public void addInvocation(String theResource, long theMillis, List<String> theStacktrace, int theThreadOffSet, int theMaxThreadCount) {
		myInvocations.add(new ConverterInvocation(this, theResource, theMillis, theStacktrace, theThreadOffSet, theMaxThreadCount));
	}

	public void reset() {
		myInvocations.clear();
	}

	public String writeMetrics(int theInvocationLimit, @Nullable Comparator<ConverterInvocation> theComparator, @Nullable Predicate<ConverterInvocation> theFilter) {
		StringBuilder sb = new StringBuilder()
			.append("=== Invocations for ").append(myMethod)
			.append(" [")
			.append("time=").append(getElapsedTime()).append("ms")
			.append(", count=").append(getCount())
			.append(", average=").append(getCount() == 0 ? 0 : getElapsedTime() / getCount()).append("ms")
			.append("] ===");


		List<ConverterInvocation> invocations = getInvocations();
		if (theComparator != null) {
			invocations.sort(theComparator);
		}

		Stream<ConverterInvocation> stream = myInvocations.stream().limit(theInvocationLimit);
		if (theFilter != null){
			stream = stream.filter(theFilter);
		}

		stream.forEach(invocation -> sb.append("\n").append(invocation));

		return sb.toString();
	}

	public String getMethod() {
		return myMethod;
	}

	public List<ConverterInvocation> getInvocations() {
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
