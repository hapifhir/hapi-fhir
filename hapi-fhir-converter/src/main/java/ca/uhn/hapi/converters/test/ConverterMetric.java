package ca.uhn.hapi.converters.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class ConverterMetric {
	private final String method;
	private final List<ConverterInvocation> invocations = new ArrayList<>();

	public ConverterMetric(String theMethod){
		method = theMethod;
	}

	public void addInvocation(String theResource, long theMillis, List<String> theStacktrace, int theThreadOffSet) {
		invocations.add(new ConverterInvocation(theResource, theMillis, theStacktrace, theThreadOffSet));
	}

	public void reset(){
		invocations.clear();
	}

	public String writeMetrics(int theInvocationLimit) {
		StringBuilder sb = new StringBuilder()
			.append("=== Invocations for ")
			.append(method)
			.append(" [")
			.append("time=").append(getTime()).append("ms")
			.append(", count=").append(getCount())
			.append(", average=").append(getCount() == 0 ? 0 : getTime() /getCount()).append("ms")
			.append("] ===");


		getSortedInvocations().stream()
			.limit(theInvocationLimit)
//      .filter(invocation -> !invocation.getResource().startsWith("StructureDefinition"))
			.forEach(invocation -> sb.append("\n").append(invocation));

		return sb.toString();
	}

	private List<ConverterInvocation> getSortedInvocations() {
		List<ConverterInvocation> copy = new ArrayList<>(invocations);
//		copy.sort(new ConverterInvocation.TimeComparator());
//		Collections.reverse(copy);
		return copy;
	}

	public String getMethod() {
		return method;
	}

	public List<ConverterInvocation> getInvocations() {
		return invocations;
	}

	public long getTime() {
		AtomicLong total = new AtomicLong(0L);
    getInvocations().forEach(invocation -> total.addAndGet(invocation.getTime()));
    return total.get();
	}

	public long getCount() {
		return getInvocations().size();
	}
}
