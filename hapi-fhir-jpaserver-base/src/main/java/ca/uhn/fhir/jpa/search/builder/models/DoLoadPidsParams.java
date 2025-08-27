package ca.uhn.fhir.jpa.search.builder.models;

import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;

import java.util.Collection;
import java.util.Map;

public class DoLoadPidsParams {

	/**
	 * The search parameter map used to generate the current list of PIDs
	 */
	private SearchParameterMap myParameterMap;
	/**
	 * The list of pids that need to be expanded into actual resources
	 */
	private Collection<JpaPid> myPids;
	/**
	 * The pids to include
	 */
	private Collection<JpaPid> myIncludedPids;
	/**
	 * Whether or not this is for a history operation
	 */
	private boolean myForHistoryOperation;
	/**
	 * The position map
	 */
	private Map<Long, Integer> myPosition;

	public SearchParameterMap getParameterMap() {
		return myParameterMap;
	}

	public DoLoadPidsParams setParameterMap(SearchParameterMap theParameterMap) {
		myParameterMap = theParameterMap;
		return this;
	}

	public Collection<JpaPid> getPids() {
		return myPids;
	}

	public DoLoadPidsParams setPids(Collection<JpaPid> thePids) {
		myPids = thePids;
		return this;
	}

	public Collection<JpaPid> getIncludedPids() {
		return myIncludedPids;
	}

	public DoLoadPidsParams setIncludedPids(Collection<JpaPid> theIncludedPids) {
		myIncludedPids = theIncludedPids;
		return this;
	}

	public boolean isForHistoryOperation() {
		return myForHistoryOperation;
	}

	public DoLoadPidsParams setForHistoryOperation(boolean theForHistoryOperation) {
		myForHistoryOperation = theForHistoryOperation;
		return this;
	}

	public Map<Long, Integer> getPosition() {
		return myPosition;
	}

	public DoLoadPidsParams setPosition(Map<Long, Integer> thePosition) {
		myPosition = thePosition;
		return this;
	}
}
