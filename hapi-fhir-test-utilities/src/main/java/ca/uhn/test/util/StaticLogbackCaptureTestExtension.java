package ca.uhn.test.util;

import ch.qos.logback.classic.spi.ILoggingEvent;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.List;

/**
 * This is a static wrapper around LogbackCaptureTestExtension for use in IT tests when you need to assert on App
 * startup log entries
 */

public class StaticLogbackCaptureTestExtension implements BeforeAllCallback, AfterAllCallback {
	private final LogbackCaptureTestExtension myLogbackCaptureTestExtension;

    public StaticLogbackCaptureTestExtension(LogbackCaptureTestExtension theLogbackCaptureTestExtension) {
        myLogbackCaptureTestExtension = theLogbackCaptureTestExtension;
    }

	@Override
	public void beforeAll(ExtensionContext theExtensionContext) throws Exception {
		myLogbackCaptureTestExtension.beforeEach(theExtensionContext);
	}

	@Override
	public void afterAll(ExtensionContext theExtensionContext) throws Exception {
		myLogbackCaptureTestExtension.afterEach(theExtensionContext);
	}

	public List<ILoggingEvent> filterLoggingEventsWithMessageEqualTo(String theMessageText) {
		return myLogbackCaptureTestExtension.filterLoggingEventsWithMessageEqualTo(theMessageText);
	}
}
