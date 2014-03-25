package ca.uhn.fhir.model.api;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.util.ElementUtil;

public class Bundle extends BaseBundle /*implements IElement*/ {
	
	//@formatter:off
	/* ****************************************************
	 * NB: add any new fields to the isEmpty() method!!!
	 *****************************************************/
	//@formatter:on
	private List<BundleEntry> myEntries;
	private StringDt myBundleId;
	private StringDt myLinkBase;
	private StringDt myLinkFirst;
	private StringDt myLinkLast;
	private StringDt myLinkNext;
	private StringDt myLinkPrevious;
	private StringDt myLinkSelf;
	private InstantDt myPublished;
	private StringDt myTitle;
	private IntegerDt myTotalResults;
	private InstantDt myUpdated;

	@Override
	public boolean isEmpty() {
		//@formatter:off
		return super.isEmpty() && 
				ElementUtil.isEmpty(myBundleId, myLinkBase, myLinkFirst, myLinkLast, myLinkNext, myLinkPrevious, myLinkSelf, myPublished, myTitle, myTotalResults) &&
				ElementUtil.isEmpty(myEntries);
		//@formatter:on
	}

	/**
	 * Adds and returns a new bundle entry
	 */
	public BundleEntry addEntry() {
		BundleEntry retVal = new BundleEntry();
		getEntries().add(retVal);
		return retVal;
	}

	public List<BundleEntry> getEntries() {
		if (myEntries == null) {
			myEntries = new ArrayList<BundleEntry>();
		}
		return myEntries;
	}

	public StringDt getBundleId() {
		if (myBundleId == null) {
			myBundleId = new StringDt();
		}
		return myBundleId;
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
			myTitle = new StringDt();
		}
		return myTitle;
	}

	public IntegerDt getTotalResults() {
		if (myTotalResults == null) {
			myTotalResults = new IntegerDt();
		}
		return myTotalResults;
	}

	public InstantDt getUpdated() {
		if (myUpdated == null) {
			myUpdated = new InstantDt();
		}
		return myUpdated;
	}

	public List<IResource> toListOfResources() {
		ArrayList<IResource> retVal = new ArrayList<IResource>();
		for (BundleEntry next : getEntries()) {
			if (next.getResource() != null) {
				retVal.add(next.getResource());
			}
		}
		return retVal;
	}

	public static Bundle withSingleResource(IResource theResource) {
		Bundle retVal = new Bundle();
		retVal.addEntry().setResource(theResource);
		return retVal;
	}

}
