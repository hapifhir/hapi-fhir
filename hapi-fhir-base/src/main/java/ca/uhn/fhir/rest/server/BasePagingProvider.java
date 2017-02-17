package ca.uhn.fhir.rest.server;

import org.apache.commons.lang3.Validate;

public abstract class BasePagingProvider implements IPagingProvider {

	private int myDefaultPageSize = 10;
	private int myMaximumPageSize = 50;

	public BasePagingProvider() {
		super();
	}

	@Override
	public int getDefaultPageSize() {
		return myDefaultPageSize;
	}

	@Override
	public int getMaximumPageSize() {
		return myMaximumPageSize;
	}

	public BasePagingProvider setDefaultPageSize(int theDefaultPageSize) {
		Validate.isTrue(theDefaultPageSize > 0, "size must be greater than 0");
		myDefaultPageSize = theDefaultPageSize;
		return this;
	}

	public BasePagingProvider setMaximumPageSize(int theMaximumPageSize) {
		Validate.isTrue(theMaximumPageSize > 0, "size must be greater than 0");
		myMaximumPageSize = theMaximumPageSize;
		return this;
	}

}