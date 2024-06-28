package ca.uhn.fhir.jpa.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for
 */
public class PermutationBuilder {

	/**
	 * Non instantiable
	 */
	private PermutationBuilder() {
		// nothing
	}

	/**
	 * Given a list of lists of options, returns a list of every possible combination of options.
	 * For example, given the input lists:
	 * <pre>
	 *   [
	 *     [ A0, A1 ],
	 *     [ B0, B1, B2 ]
	 *   ]
	 * </pre>
	 * This method will return the following output lists:
	 * <pre>
	 *   [
	 *     [ A0, B0 ],
	 *     [ A1, B0 ],
	 *     [ A0, B1 ],
	 *     [ A1, B1 ],
	 *     [ A0, B2 ],
	 *     [ A1, B2 ]
	 *   ]
	 * </pre>
	 * <p>
	 * This method may or may not return a newly created list.
	 * </p>
	 *
	 * @param theInput A list of lists to calculate permutations of
	 * @param <T>      The type associated with {@literal theInput}. The actual type doesn't matter, this method does not look at the
	 *                 values at all other than to copy them to the output lists.
	 * @since 7.4.0
	 */
	public static <T> List<List<T>> calculatePermutations(List<List<T>> theInput) {
		if (theInput.size() == 1) {
			return theInput;
		}
		List<List<T>> listToPopulate = new ArrayList<>(calculatePermutationCount(theInput));
		int[] indices = new int[theInput.size()];
		doCalculatePermutationsIntoIndicesArrayAndPopulateList(0, indices, theInput, listToPopulate);
		return listToPopulate;
	}

	/**
	 * Recursively called/calling method which navigates across a list of input ArrayLists and populates the array of indices
	 *
	 * @param thePositionX The offset within {@literal theInput}. We navigate recursively from 0 through {@literal theInput.size()}
	 * @param theIndices   The array to populate. This array has a length matching the size of {@literal theInput} and each entry
	 *                     represents an offset within the list at {@literal theInput.get(arrayIndex)}
	 * @param theInput     The input List of ArrayLists
	 * @param theOutput    A list to populate with all permutations
	 * @param <T>          The type associated with {@literal theInput}. The actual type doesn't matter, this method does not look at the
	 *                     values at all other than to copy them to the output lists.
	 */
	private static <T> void doCalculatePermutationsIntoIndicesArrayAndPopulateList(int thePositionX, int[] theIndices, List<List<T>> theInput, List<List<T>> theOutput) {
		if (thePositionX != theInput.size()) {
			// If we're not at the end of the list of input vectors, recursively self-invoke once for each
			// possible option at the current position in the list of input vectors.
			for (int positionY = 0; positionY < theInput.get(thePositionX).size(); positionY++) {
				theIndices[thePositionX] = positionY;
				doCalculatePermutationsIntoIndicesArrayAndPopulateList(thePositionX + 1, theIndices, theInput, theOutput);
			}
		} else {
			// If we're at the end of the list of input vectors, then we've been passed the
			List<T> nextList = new ArrayList<>(theInput.size());
			for (int positionX = 0; positionX < theInput.size(); positionX++) {
				nextList.add(theInput.get(positionX).get(theIndices[positionX]));
			}
			theOutput.add(nextList);
		}
	}

	/**
	 * Returns the number of permutations that {@link #calculatePermutations(List)} will
	 * calculate for the given list.
	 *
	 * @throws ArithmeticException If the number of permutations exceeds {@link Integer#MAX_VALUE}
	 * @since 7.4.0
	 */
	public static <T> int calculatePermutationCount(List<List<T>> theLists) throws ArithmeticException {
		int retVal = !theLists.isEmpty() ? 1 : 0;
		for (List<T> theList : theLists) {
			retVal = Math.multiplyExact(retVal, theList.size());
		}
		return retVal;
	}

}
