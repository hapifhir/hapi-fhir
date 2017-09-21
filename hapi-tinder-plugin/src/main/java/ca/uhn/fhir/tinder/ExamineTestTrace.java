package ca.uhn.fhir.tinder;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class ExamineTestTrace {

	private static final Logger ourLog = LoggerFactory.getLogger(ExamineTestTrace.class);

	public static void main(String[] aaa) {
		String input = "Running ca.uhn.fhir.jpa.config.IdentifierLengthTest\n" +
			"Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 6.913 sec - in ca.uhn.fhir.jpa.subscription.r4.RestHookTestWithInterceptorRegisteredToDaoConfigR4Test";

		Set<String> started = new HashSet<>();
		Set<String> finished = new HashSet<>();
		for (String next : input.split("\n")) {
			if (next.startsWith("Running ")) {
				started.add(next.substring("Running ".length()));
			} else if (next.startsWith("Tests run: ")) {
				finished.add(next.substring(next.indexOf(" - in ") + " - in ".length()));
			} else {
				throw new IllegalStateException();
			}
		}

		ourLog.info("Started {}", started.size());
		ourLog.info("Finished {}", finished.size());
		ourLog.info(CollectionUtils.disjunction(started, finished).toString());

	}


}
