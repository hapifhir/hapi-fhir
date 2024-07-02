/*
 * #%L
 * HAPI FHIR - Server Framework
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
package ca.uhn.fhir.rest.server;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IServerAddressStrategyTest {

	@Mock
	private RestfulServer server;

	@Mock
	private ServletContext servletContext;

	@Mock
	private HttpServletRequest request;

	private final IServerAddressStrategy strategy = new IServerAddressStrategy() {
		@Override
		public String determineServerBase(ServletContext theServletContext, HttpServletRequest theRequest) {
			return "";
		}
	};

	@Test
	public void testDetermineServletContextPath_UsesServletContext() {
		when(server.getServletContext()).thenReturn(servletContext);
		when(servletContext.getMajorVersion()).thenReturn(3);
		when(servletContext.getContextPath()).thenReturn("/servlet/context");
		String result = strategy.determineServletContextPath(request, server);
		assertEquals("/servlet/context", result);
	}

	@Test
	public void testDetermineServletContextPath_DoesNotUseServletContext() {
		when(server.getServletContext()).thenReturn(servletContext);
		when(servletContext.getMajorVersion()).thenReturn(2);
		when(request.getContextPath()).thenReturn("/request/context");
		assertEquals("/request/context", strategy.determineServletContextPath(request, server));

		when(server.getServletContext()).thenReturn(null);
		assertEquals("/request/context", strategy.determineServletContextPath(request, server));
	}
}
