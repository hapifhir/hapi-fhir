package ca.uhn.fhir.util;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.event.Level;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class LogUtilTest {

	@Test
	public void testLevels() {
		Logger log = mock(Logger.class);
		LogUtil.log(log, Level.TRACE, "HELLO");
		LogUtil.log(log, Level.DEBUG, "HELLO");
		LogUtil.log(log, Level.INFO, "HELLO");
		LogUtil.log(log, Level.WARN, "HELLO");
		LogUtil.log(log, Level.ERROR, "HELLO");

		verify(log, times(1)).trace(anyString(),any(Object[].class));
		verify(log, times(1)).debug(anyString(),any(Object[].class));
		verify(log, times(1)).info(anyString(),any(Object[].class));
		verify(log, times(1)).warn(anyString(),any(Object[].class));
		verify(log, times(1)).error(anyString(),any(Object[].class));
		verifyNoMoreInteractions(log);
	}

}
