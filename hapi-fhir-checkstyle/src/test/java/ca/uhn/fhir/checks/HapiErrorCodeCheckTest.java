package ca.uhn.fhir.checks;

import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.DefaultConfiguration;
import com.puppycrawl.tools.checkstyle.DefaultLogger;
import com.puppycrawl.tools.checkstyle.PackageObjectFactory;
import com.puppycrawl.tools.checkstyle.TreeWalker;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import org.apache.commons.io.output.NullOutputStream;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HapiErrorCodeCheckTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(HapiErrorCodeCheckTest.class);
	@Test
	public void testExpectedErrors() throws CheckstyleException {
		// setup
		Checker checker = buildChecker();

		List<File> files = new ArrayList<>();
		files.add(getFile("BadClass.java"));

		ByteArrayOutputStream errors = new ByteArrayOutputStream();
		DefaultLogger listener = new DefaultLogger(NullOutputStream.NULL_OUTPUT_STREAM, AutomaticBean.OutputStreamOptions.CLOSE,
			errors, AutomaticBean.OutputStreamOptions.CLOSE);
		checker.addListener(listener);

		// execute
		checker.process(files);

		// validate
		String[] errorLines = errors.toString().split("\n");
		Arrays.stream(errorLines).forEach(ourLog::info);
		assertEquals(2, errorLines.length);
		assertThat(errorLines[0], startsWith("[ERROR] "));
		assertThat(errorLines[0], endsWith("BadClass.java:7: Exception thrown that does not call Msg.code() [HapiErrorCode]"));
		assertThat(errorLines[1], startsWith("[ERROR] "));
		assertThat(errorLines[1], containsString("BadClass.java:11: Two different exception messages call Msg.code(2).  Each thrown exception throw call Msg.code() with a different code."));
		assertThat(errorLines[1], containsString("BadClass.java:9:9"));
	}

	private Checker buildChecker() throws CheckstyleException {
		Checker checker = new Checker();
		TreeWalker treeWalker = new TreeWalker();
		DefaultConfiguration childConf = new DefaultConfiguration("ca.uhn.fhir.checks.HapiErrorCodeCheck");
		DefaultConfiguration config = new DefaultConfiguration("TreeWalker");

		config.addChild(childConf);
		treeWalker.setModuleFactory(new PackageObjectFactory("test", this.getClass().getClassLoader()));
		treeWalker.configure(config);
		checker.addFileSetCheck(treeWalker);
		return checker;
	}

	private File getFile(String theFilename) {
		URL url = this.getClass()
			.getClassLoader()
			.getResource(theFilename);

		if(url == null) {
			throw new IllegalArgumentException(theFilename + " file not found");
		}

		return new File(url.getFile());
	}

}
