package ca.uhn.fhir.jpa.provider.validation.performance;

import org.apache.commons.lang3.builder.CompareToBuilder;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ConverterInvocation {

	private static final DateTimeFormatter OUR_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss.SSS").withZone(ZoneId.systemDefault());

	private final ConverterMetric myMetric;
	private final String myResourceId;
	private final long myElapsedTime;
	private final List<String> myStacktrace;
	private final int myThreadOffset;
	private final int myMaxThreadCount;
	private final Instant myInvocatedAt;

	public ConverterInvocation(ConverterMetric theMetric, String theResource, long theTime, List<String> theStacktrace, int theThreadOffSet, int theMaxThreadCount) {
		myMetric = theMetric;
		myResourceId = theResource;
		myElapsedTime = theTime;
		myStacktrace = theStacktrace;
		myThreadOffset = theThreadOffSet;
		myMaxThreadCount = theMaxThreadCount;
		myInvocatedAt = new Date().toInstant();
	}

	public long getElapsedTime() {
		return myElapsedTime;
	}

	public String getResourceId() {
		return myResourceId;
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder()
			.append("[")
			.append("method=").append(myMetric.getMethod())
			.append(", timestamp=").append(OUR_FORMATTER.format(myInvocatedAt))
			.append(", resource=").append(myResourceId)
			.append(", time=").append(myElapsedTime).append("ms")
			.append("]\n");

		for (int i = 0; i < myMaxThreadCount; i++) {
			if (i != 0) {
				sb.append("\n");
			}
			int index = i + myThreadOffset;
			String element = myStacktrace.get(index);
			sb.append("\t").append(element);
		}
		return sb.toString();
	}

	public static class ElapsedTimeComparator implements Comparator<ConverterInvocation> {
		@Override
		public int compare(ConverterInvocation theO1, ConverterInvocation theO2) {
			return new CompareToBuilder()
				.append(theO1.myElapsedTime, theO2.myElapsedTime)
				.toComparison();
		}
	}
}
