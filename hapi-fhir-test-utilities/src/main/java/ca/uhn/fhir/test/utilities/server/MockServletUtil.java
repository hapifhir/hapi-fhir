package ca.uhn.fhir.test.utilities.server;

/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import javax.servlet.ServletConfig;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockServletUtil {

	/**
	 * Non instantiable
	 */
	private MockServletUtil() {
		super();
	}

	public static ServletConfig createServletConfig() {
		ServletConfig sc = mock(ServletConfig.class);
		when(sc.getServletContext()).thenReturn(null);
		return sc;
	}
}
