package ca.uhn.fhir.jpa.dao.search;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class TermHelper {

	/** characters which indicate the string parameter is a simple query string */
	private static final char[] simpleQuerySyntaxCharacters = new char[] { '+', '|', '-', '"', '*', '(', ')', '~' };


	/**
	 * Each input set element is:
	 *   _ copied to the output set unchanged if it contains a '*' character or is quoted
	 *   _ trimmed, tokenized by spaces, and suffixed by ' *', and each resulting string copied to the output set
	 */
	public static Set<String> makePrefixSearchTerm(Set<String> theStringSet) {
		return theStringSet.stream()
			.map(s -> isToLeftUntouched(s) || isQuoted(s) ? s : suffixTokensWithStar(s) )
			.collect(Collectors.toSet());
	}


	private static String suffixTokensWithStar(String theStr) {
		StringBuilder sb = new StringBuilder();

		Arrays.stream(theStr.trim().split(" "))
			.forEach(s -> sb.append(s).append("* "));

		return sb.toString().trim();
	}


	private static boolean isQuoted(String theS) {
		return ( theS.startsWith("\"") && theS.endsWith("\"") ) ||
			( theS.startsWith("'") && theS.endsWith("'") );
	}


	private static boolean isToLeftUntouched(String theS) {
		return StringUtils.containsAny(theS, simpleQuerySyntaxCharacters);
	}



}
