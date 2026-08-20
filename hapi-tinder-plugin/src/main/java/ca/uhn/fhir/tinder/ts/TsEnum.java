package ca.uhn.fhir.tinder.ts;

// Created by Claude Opus 4.8

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A generated TypeScript enumeration, rendered as a string-literal union type alias
 * (e.g. {@code export type AdministrativeGender = 'male' | 'female' | 'other' | 'unknown';}).
 */
public class TsEnum {

	private final String myName;
	private final List<String> myCodes = new ArrayList<>();

	public TsEnum(String theName, Collection<String> theCodes) {
		myName = theName;
		myCodes.addAll(theCodes);
	}

	public String getName() {
		return myName;
	}

	public List<String> getCodes() {
		return myCodes;
	}

	/**
	 * The right-hand side of the union alias, e.g. {@code "'male' | 'female'"}. Empty enumerations
	 * collapse to {@code string} so the generated TypeScript remains valid.
	 */
	public String getUnion() {
		if (myCodes.isEmpty()) {
			return "string";
		}
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < myCodes.size(); i++) {
			if (i > 0) {
				b.append(" | ");
			}
			b.append("'").append(myCodes.get(i).replace("'", "\\'")).append("'");
		}
		return b.toString();
	}
}
