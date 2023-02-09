package ca.uhn.test.util;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;

import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;

public class LogContentsTestExtention extends BaseLogbackCaptureTestExtension<OutputStreamAppender<ILoggingEvent>> implements BeforeEachCallback, AfterEachCallback {
	final ByteArrayOutputStream myOutputStream = new ByteArrayOutputStream();

	public LogContentsTestExtention(Logger theLogger) {
		super(theLogger);
	}

	public LogContentsTestExtention(String theLoggerName) {
		super(theLoggerName); }

	@Nonnull
	@Override
	OutputStreamAppender<ILoggingEvent> buildAppender() {

		Context context = getLogger().getLoggerContext();

		PatternLayout layout = new PatternLayout();
		layout.setContext(context);
		layout.setPattern("%d{yyyy-MM-dd HH:mm:ss.SSS} %logger{36} - %msg%n%xEx");
		layout.start();

		LayoutWrappingEncoder<ILoggingEvent> encoder = new LayoutWrappingEncoder<>();
		encoder.setContext(context);
		encoder.setLayout(layout);
		encoder.start();

		OutputStreamAppender<ILoggingEvent> outputStreamAppender = new OutputStreamAppender<>();
		outputStreamAppender.setContext(context);
		outputStreamAppender.setEncoder(encoder);
		outputStreamAppender.setOutputStream(myOutputStream);
		outputStreamAppender.start();

		return outputStreamAppender;
	}

	public String getLogOutput() {
		return myOutputStream.toString(Charset.defaultCharset());
	}
}
