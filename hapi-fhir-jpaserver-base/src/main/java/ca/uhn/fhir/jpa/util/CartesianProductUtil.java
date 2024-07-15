/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
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
