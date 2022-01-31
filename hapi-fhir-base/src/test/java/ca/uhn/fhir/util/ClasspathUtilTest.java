package ca.uhn.fhir.util;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class ClasspathUtilTest {

	@Test
	public void testLoadResourceNotFound() {
		try {
			ClasspathUtil.loadResource("/FOOOOOO");
		} catch (InternalErrorException e) {
			assertEquals(Msg.code(1758) + "Unable to find classpath resource: /FOOOOOO", e.getMessage());
		}
	}

	@Test
	public void testLoadResourceAsStreamNotFound() {
		try {
			ClasspathUtil.loadResourceAsStream("/FOOOOOO");
		} catch (InternalErrorException e) {
			assertEquals(Msg.code(1758) + "Unable to find classpath resource: /FOOOOOO", e.getMessage());
		}
	}

	/**
	 * Should not throw any exception
	 */
	@Test
	public void testClose_Null() {
		ClasspathUtil.close(null);
	}

	/**
	 * Should not throw any exception
	 */
	@Test
	public void testClose_Ok() {
		ClasspathUtil.close(new ByteArrayInputStream(new byte[]{0,1,2}));
	}


	/**
	 * Should not throw any exception
	 */
	@Test
	public void testClose_ThrowException() throws IOException {
		InputStream is = mock(InputStream.class);
		doThrow(new IOException("FOO")).when(is).close();
		ClasspathUtil.close(is);
	}

}
