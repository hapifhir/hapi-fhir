package ca.uhn.fhir.model.api;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.StringDt;

public class Bundle implements IElement {

	private StringDt myAuthorDevice;
	private StringDt myAuthorName;
	private List<BundleEntry> myEntries;
	private StringDt myId;
	private StringDt myLinkBase;
	private StringDt myLinkFirst;
	private StringDt myLinkLast;
	private StringDt myLinkNext;
	private StringDt myLinkPrevious;
	private StringDt myLinkSelf;
	private InstantDt myPublished;
	private StringDt myTitle;
	private IntegerDt myTotalResults;
	private InstantDt  myUpdated;

	public StringDt getAuthorDevice() {
		if (myAuthorDevice == null) {
			myAuthorDevice = new StringDt();
		}
		return myAuthorDevice;
	}

	public StringDt getAuthorName() {
		if (myAuthorName == null) {
			myAuthorName = new StringDt();
		}
		return myAuthorName;
	}

	public List<BundleEntry> getEntries() {
		if (myEntries == null) {
			myEntries = new ArrayList<BundleEntry>();
		}
		return myEntries;
	}

	public StringDt getId() {
		if (myId == null) {
			myId = new StringDt();
		}
		return myId;
	}

	public StringDt getLinkBase() {
		if (myLinkBase == null) {
			myLinkBase = new StringDt();
		}
		return myLinkBase;
	}

	public StringDt getLinkFirst() {
		if (myLinkFirst == null) {
			myLinkFirst = new StringDt();
		}
		return myLinkFirst;
	}

	public StringDt getLinkLast() {
		if (myLinkLast == null) {
			myLinkLast = new StringDt();
		}
		return myLinkLast;
	}

	public StringDt getLinkNext() {
		if (myLinkNext == null) {
			myLinkNext = new StringDt();
		}
		return myLinkNext;
	}

	public StringDt getLinkPrevious() {
		if (myLinkPrevious == null) {
			myLinkPrevious = new StringDt();
		}
		return myLinkPrevious;
	}

	public StringDt getLinkSelf() {
		if (myLinkSelf == null) {
			myLinkSelf = new StringDt();
		}
		return myLinkSelf;
	}

	public InstantDt getPublished() {
		if (myPublished == null) {
			myPublished = new InstantDt();
		}
		return myPublished;
	}

	public StringDt getTitle() {
		if (myTitle == null) {
			myTitle= new StringDt();
		}
		return myTitle;
	}

	public IntegerDt getTotalResults() {
		if (myTotalResults== null) {
			myTotalResults= new IntegerDt();
		}
		return myTotalResults;
	}

	public InstantDt getUpdated() {
		if (myUpdated == null) {
			myUpdated= new InstantDt();
		}
		return myUpdated;
	}

	

}
