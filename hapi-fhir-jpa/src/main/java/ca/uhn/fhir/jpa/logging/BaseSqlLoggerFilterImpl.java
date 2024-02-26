package ca.uhn.fhir.jpa.logging;

import com.google.common.annotations.VisibleForTesting;

import java.util.LinkedList;
import java.util.List;

public abstract class BaseSqlLoggerFilterImpl implements ISqlLoggerFilter {
	protected final List<String> myFilterDefinitions = new LinkedList<>();

	@Override
	public boolean evaluateFilterLine(String theFilterLine) {
		boolean matched = theFilterLine.startsWith(getPrefix());
		if (matched) {
			myFilterDefinitions.add(
					theFilterLine.substring(getPrefix().length()).trim());
		}
		return matched;
	}

	@Override
	public void clearDefinitions() {
		myFilterDefinitions.clear();
	}

	@Override
	public Object getLockingObject() {
		return myFilterDefinitions;
	}

	@VisibleForTesting
	public void setFilterDefinitions(List<String> theFilterDefinitions) {
		synchronized (myFilterDefinitions) {
			myFilterDefinitions.clear();
			myFilterDefinitions.addAll(theFilterDefinitions);
		}
	}
}
