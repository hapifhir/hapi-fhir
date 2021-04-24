package ca.uhn.fhir.test.utilities.server;

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
