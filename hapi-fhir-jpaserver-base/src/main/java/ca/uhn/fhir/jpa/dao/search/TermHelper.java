package ca.uhn.fhir.jpa.dao.search;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TermHelper {

	/**
	 * Each input set element is:
	 *   _ copied to the output set unchanged if it contains a '*' character or is quoted
	 *   _ trimmed, tokenized by spaces, and suffixed by ' *', and each resulting string copied to the output set
	 */
	public static Set<String> makePrefixSearchTerm(Set<String> theStringSet) {
		return theStringSet.stream()
			.flatMap(s -> isQuoted(s) || s.contains("*") ? Stream.of(s) : splitInBlanksAndStarSuffix(s) )
			.collect(Collectors.toSet());
	}

	private static Stream<String> splitInBlanksAndStarSuffix(String theStr) {
		return Arrays.stream(theStr.trim().split(" "))
			.map(s -> s + "*");
	}

	private static boolean isQuoted(String theS) {
		return ( theS.startsWith("\"") && theS.endsWith("\"") ) ||
			( theS.startsWith("'") && theS.endsWith("'") );
	}



}
