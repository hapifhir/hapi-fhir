package ca.uhn.fhir.rest.api;

import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class BundleLinks {
	public final String serverBase;
	public final boolean prettyPrint;
	public final BundleTypeEnum bundleType;
	private final List<Include> includes;

	private String self;
	private String next;
	private String prev;

	public BundleLinks(String theServerBase, Set<Include> theIncludes, boolean thePrettyPrint, BundleTypeEnum theBundleType) {
		serverBase = theServerBase;
		includes = theIncludes == null ? null : new ArrayList<>(theIncludes);
		prettyPrint = thePrettyPrint;
		bundleType = theBundleType;
	}

	public String getSelf() {
		return self;
	}

	public BundleLinks setSelf(String theSelf) {
		self = theSelf;
		return this;
	}

	public String getNext() {
		return next;
	}

	public BundleLinks setNext(String theNext) {
		next = theNext;
		return this;
	}

	public String getPrev() {
		return prev;
	}

	public BundleLinks setPrev(String thePrev) {
		prev = thePrev;
		return this;
	}

	public Collection<Include> getIncludes() {
		if (includes == null) {
			return null;
		}
		return Collections.unmodifiableList(includes);
	}
}
