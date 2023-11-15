/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.test.utilities;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;

public class CustomMatchersUtil {

	/**
	 * Asserts that none of the items in theShouldNotContain are in theActual
	 * @param theActual the actual results
	 * @param theShouldNotContain the items that should not be in theActual
	 */
	public static <T> void assertDoesNotContainAnyOf(List<T> theActual, List<T> theShouldNotContain) {
		for (T item : theShouldNotContain) {
			assertThat(theActual, not(hasItem(item)));
		}
	}
}
