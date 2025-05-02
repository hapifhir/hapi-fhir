package org.hl7.fhir.r4.validation.performance;

import org.apache.commons.lang3.builder.CompareToBuilder;

import java.util.Comparator;
import java.util.List;

public class ConverterInvocation {
	private final String resource;
	private final long time;
  	private final List<String> stacktrace;
	  private final int myThreadOffset;

	public ConverterInvocation(String theResource, long theTime, List<String> theStacktrace, int theThreadOffSet) {
		resource = theResource;
		time = theTime;
    stacktrace = theStacktrace;
	myThreadOffset = theThreadOffSet;
	}

  public long getTime() {
    return time;
  }

  public String getResource() {
    return resource;
  }

  @Override
	public String toString() {

    StringBuilder sb = new StringBuilder();
    sb.append("[").append("resource=").append(resource).append(", time=").append(time).append("ms]\n");

    int maxElements = 10;

    for (int i = 0; i < maxElements; i++){
      if (i != 0){
        sb.append("\n");
      }
      int index = i + myThreadOffset;
      String element = stacktrace.get(index);
      sb.append("\t").append(element);
    }

		return sb.toString();
	}

	public static class TimeComparator implements Comparator<ConverterInvocation> {

		@Override
		public int compare(ConverterInvocation theO1, ConverterInvocation theO2) {
			return new CompareToBuilder()
				.append(theO1.time, theO2.time)
				.toComparison();
		}
	}
}
