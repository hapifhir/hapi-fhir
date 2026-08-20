package ca.uhn.fhir.tinder.ts;

// Created by Claude Opus 4.8

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The complete set of TypeScript interfaces and enumerations extracted for a single FHIR version.
 * Insertion order is preserved so generated output is deterministic. Interfaces and enums are keyed by
 * name to de-duplicate datatypes that are referenced by many resources.
 */
public class TsModel {

	private final String myVersion;
	private final Map<String, TsInterface> myInterfaces = new LinkedHashMap<>();
	private final Map<String, TsEnum> myEnums = new LinkedHashMap<>();

	public TsModel(String theVersion) {
		myVersion = theVersion;
	}

	public String getVersion() {
		return myVersion;
	}

	public boolean hasInterface(String theName) {
		return myInterfaces.containsKey(theName);
	}

	public void addInterface(TsInterface theInterface) {
		myInterfaces.put(theInterface.getName(), theInterface);
	}

	public TsInterface getInterface(String theName) {
		return myInterfaces.get(theName);
	}

	public List<TsInterface> getInterfaces() {
		return new ArrayList<>(myInterfaces.values());
	}

	public boolean hasEnum(String theName) {
		return myEnums.containsKey(theName);
	}

	public void addEnum(TsEnum theEnum) {
		myEnums.put(theEnum.getName(), theEnum);
	}

	public List<TsEnum> getEnums() {
		return new ArrayList<>(myEnums.values());
	}
}
