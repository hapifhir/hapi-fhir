package ca.uhn.fhir.model.api;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.XhtmlDt;
import ca.uhn.fhir.util.ElementUtil;

public class BundleEntry extends BaseBundle {

	//@formatter:off
	/* ****************************************************
	 * NB: add any new fields to the isEmpty() method!!!
	 *****************************************************/
	//@formatter:on
	private StringDt myEntryId;
	private StringDt myLinkSelf;
	private InstantDt myPublished;
	private IResource myResource;
	private StringDt myTitle;
	private InstantDt myUpdated;
	private XhtmlDt mySummary;
	private List<BundleCategory> myCategories;

	@Override
	public boolean isEmpty() {
		//@formatter:off
		return super.isEmpty() && 
				ElementUtil.isEmpty(myEntryId, myLinkSelf, myPublished, myResource, myTitle, myUpdated, mySummary) &&
				ElementUtil.isEmpty(myCategories);
		//@formatter:on
	}
	
	public StringDt getEntryId() {
		if (myEntryId == null) {
			myEntryId = new StringDt();
		}
		return myEntryId;
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

	public IResource getResource() {
		return myResource;
	}

	public StringDt getTitle() {
		if (myTitle == null) {
			myTitle = new StringDt();
		}
		return myTitle;
	}

	public InstantDt getUpdated() {
		if (myUpdated == null) {
			myUpdated = new InstantDt();
		}
		return myUpdated;
	}

	public void setLinkSelf(StringDt theLinkSelf) {
		if (myLinkSelf == null) {
			myLinkSelf = new StringDt();
		}
		myLinkSelf = theLinkSelf;
	}

	public void setResource(IResource theResource) {
		myResource = theResource;
	}

	public XhtmlDt getSummary() {
		if (mySummary == null) {
			mySummary = new XhtmlDt();
		}
		return mySummary;
	}

	public BundleCategory addCategory() {
		BundleCategory retVal = new BundleCategory();
		getCategories().add(retVal);
		return retVal;
	}

	public List<BundleCategory> getCategories() {
		if (myCategories == null) {
			myCategories = new ArrayList<BundleCategory>();
		}
		return myCategories;
	}

}
