package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.jpa.config.HibernatePropertiesProvider;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;
import org.hibernate.dialect.Oracle12cDialect;

import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utilities to assist SQL query building
 */
public class SqlQueryUtil {

	private static final int ORACLE_MAX_IN_PARAM_ENTRIES = 1_000;

	private final HibernatePropertiesProvider myHibernatePropertiesProvider;


	public SqlQueryUtil(HibernatePropertiesProvider theHibernatePropertiesProvider) {
		myHibernatePropertiesProvider = theHibernatePropertiesProvider;
	}


	/**
	 * Intended to partition 'in' clause parameter lists for Oracle, which doesn't accept more than 1,000 parameters
	 *
	 * Builds the string to be appended to a query to add as many 'in' statements as needed
	 * to avoid an 'in' statement element overflow.
	 * Example: For input = [1, 2, 3, 4, 5, 6, 7] and maxListSize = 3
	 * output:
	 *   (x.getid in (1, 2, 3)
	 *   or x.getid in (4, 5, 6)
	 *   or x.getid in (7) )
	 */
	public String buildInList(String theInClause, Collection<Long> theInElements, int maxListSize) {
		if (theInElements.isEmpty())  {
			return " 1=2 -- replaced empty 'in' parameter list: " + theInClause + " in () " + NL;
		}

		StringBuilder sb = new StringBuilder();
		UnmodifiableIterator<List<Long>> iter = Iterators.partition(theInElements.iterator(), maxListSize);
		while(iter.hasNext()) {
			List<Long> subList = iter.next();

			sb.append( sb.length() == 0 ? " ( " : " or " )
				.append(theInClause)
				.append(" in (")
				.append( getCsvString(subList) )
				.append(")");
		}
		sb.append(" ) ");

		return sb.toString();
	}


	/**
	 * Returns a split 'in' clause only if the data source is Oracle and there are more than 1,000 parameters
	 * and a not split clause otherwise.
	 */
	public <T> String buildInListIfNeeded(String theInClause, Collection<Long> theInElements) {
		if ( ! isOracleDialect() || theInElements.size() <= ORACLE_MAX_IN_PARAM_ENTRIES) {
			return  " " + theInClause + " in (" + getCsvString(theInElements) + ") ";
		}

		return buildInList(theInClause, theInElements, ORACLE_MAX_IN_PARAM_ENTRIES);
	}


	/**
	 * Return input collection elements as a CSV string
	 */
	private String getCsvString(Collection<Long> theInElements) {
		if (theInElements.isEmpty()) {
			throw new InvalidParameterException("Input collection is empty");
		}

		return theInElements.stream()
			.map(String::valueOf)
			.collect(Collectors.joining(", "));
	}


	private boolean isOracleDialect() {
		return myHibernatePropertiesProvider.getDialect().getClass().equals(Oracle12cDialect.class);
	}

	private static final String NL = System.getProperty("line.separator");

}
