package ca.uhn.fhir.tinder;

import ca.uhn.fhir.i18n.Msg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class ExamineTestTrace {

	private static final Logger ourLog = LoggerFactory.getLogger(ExamineTestTrace.class);

	public static void main(String[] aaa) {
		String input = "[INFO] Running ca.uhn.fhir.rest.client.RestfulClientFactoryDstu2Test\n" +
			"[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.982 s - in ca.uhn.fhir.validation.ResourceValidatorDstu2Test";

		Set<String> started = new HashSet<>();
		Set<String> finished = new HashSet<>();
		for (String next : input.split("\n")) {
			if (next.startsWith("[INFO] ")) {
				next = next.substring("[INFO] ".length());
			}
			if (next.startsWith("[WARNING] ")) {
				next = next.substring("[WARNING] ".length());
			}
			if (next.startsWith("Running ")) {
				started.add(next.substring("Running ".length()));
			} else if (next.startsWith("Tests run: ")) {
				finished.add(next.substring(next.indexOf(" - in ") + " - in ".length()));
			} else if (isBlank(next)) {
				continue;
			} else {
				throw new IllegalStateException(Msg.code(107) + "Unknown line: " + next);
			}
		}

		ourLog.info("Started {}", started.size());
		ourLog.info("Finished {}", finished.size());

	}


}
