package ca.uhn.fhir.cli;

import org.apache.commons.io.output.TeeOutputStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * This class splits output stream to both STDOUT, and a capturing byte array output stream, which can later be inspected.
 */
public class ConsoleOutputCapturingBaseTest {

	protected final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
	protected final TeeOutputStream myTeeOutputStream = new TeeOutputStream(System.out, outputStreamCaptor);

	@BeforeEach
	public void setUp() {
		System.setOut(new PrintStream(myTeeOutputStream));
	}

	@AfterEach
	public void tearDown() {
		outputStreamCaptor.reset();
		System.setOut(System.out);
	}
	protected String getConsoleOutput() {
		return outputStreamCaptor.toString().trim();
	}

}
