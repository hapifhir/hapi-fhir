package ca.uhn.fhir.jpa.mdm.svc.concurrency;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * The MDM state produced by a scenario, described by source labels so that it can be compared with a hand-written
 * expectation.
 * <ul>
 *     <li>A golden resource is named by the labels of the sources MATCHed to it, sorted and joined with {@code +}
 *     ({@code "a+b"}), or {@code ""} if none are.</li>
 *     <li>A POSSIBLE_MATCH is {@code "source~golden"}, for example {@code "c~a+b"}.</li>
 *     <li>A POSSIBLE_DUPLICATE is the two golden resources, sorted and joined with {@code |}, for example
 *     {@code "a|b"}.</li>
 * </ul>
 *
 * @param goldenResources   the golden resources
 * @param possibleMatches   the POSSIBLE_MATCH links
 * @param possibleDuplicates the POSSIBLE_DUPLICATE links
 */
// Created by claude-opus-5-5
public record MdmOutcome(Set<String> goldenResources, Set<String> possibleMatches, Set<String> possibleDuplicates) {

	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Names a golden resource from the labels of its MATCHed sources.
	 */
	public static String goldenName(String... theMembers) {
		return Arrays.stream(theMembers).sorted().collect(Collectors.joining("+"));
	}

	public static String duplicateName(String theGolden, String theOtherGolden) {
		return theGolden.compareTo(theOtherGolden) <= 0
				? theGolden + "|" + theOtherGolden
				: theOtherGolden + "|" + theGolden;
	}

	public static class Builder {
		private final Set<String> myGoldenResources = new TreeSet<>();
		private final Set<String> myPossibleMatches = new TreeSet<>();
		private final Set<String> myPossibleDuplicates = new TreeSet<>();

		/**
		 * A golden resource with these sources MATCHed to it.
		 */
		public Builder golden(String... theMembers) {
			myGoldenResources.add(goldenName(theMembers));
			return this;
		}

		/**
		 * A POSSIBLE_MATCH from a source to the golden resource whose MATCHed sources are given.
		 */
		public Builder possible(String theSource, String... theGoldenMembers) {
			myPossibleMatches.add(theSource + "~" + goldenName(theGoldenMembers));
			return this;
		}

		/**
		 * A POSSIBLE_DUPLICATE between two golden resources, each named by its MATCHed sources joined with {@code +}.
		 */
		public Builder duplicate(String theGolden, String theOtherGolden) {
			myPossibleDuplicates.add(duplicateName(theGolden, theOtherGolden));
			return this;
		}

		public MdmOutcome build() {
			return new MdmOutcome(myGoldenResources, myPossibleMatches, myPossibleDuplicates);
		}
	}
}
