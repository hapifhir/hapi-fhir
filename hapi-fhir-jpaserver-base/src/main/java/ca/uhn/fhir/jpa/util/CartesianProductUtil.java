package ca.uhn.fhir.jpa.util;

import java.util.List;

/**
 * Utility class for working with cartesian products - Use Guava's
 * {@link com.google.common.collect.Lists#cartesianProduct(List)} method
 * to actually calculate the product.
 */
public class CartesianProductUtil {

	/**
	 * Non instantiable
	 */
	private CartesianProductUtil() {
		// nothing
	}

	/**
	 * Returns the size of the cartesian product
	 *
	 * @throws ArithmeticException If size exceeds {@link Integer#MAX_VALUE}
	 * @since 7.4.0
	 */
	public static <T> int calculateCartesianProductSize(List<List<T>> theLists) throws ArithmeticException {
		int retVal = !theLists.isEmpty() ? 1 : 0;
		for (List<T> theList : theLists) {
			retVal = Math.multiplyExact(retVal, theList.size());
		}
		return retVal;
	}
}
