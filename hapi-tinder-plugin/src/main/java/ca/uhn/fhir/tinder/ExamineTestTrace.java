package ca.uhn.fhir.tinder;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class ExamineTestTrace {

	private static final Logger ourLog = LoggerFactory.getLogger(ExamineTestTrace.class);

	public static void main(String[] aaa) {
		String input = "Running ca.uhn.fhir.model.primitive.BaseResourceReferenceDtTest\n" +
			"Tests run: 8, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.896 sec - in ca.uhn.fhir.rest.server.OperationServerWithSearchParamTypesDstu2Test";

		Set<String> started = new HashSet<>();
		Set<String> finished = new HashSet<>();
		for (String next : input.split("\n")) {
			if (next.startsWith("Running ")) {
				started.add(next.substring("Running ".length()));
			} else if (next.startsWith("Tests run: ")) {
				finished.add(next.substring(next.indexOf(" - in ") + " - in ".length()));
			} else if (isBlank(next)) {
				continue;
			} else {
				throw new IllegalStateException();
			}
		}

		ourLog.info("Started {}", started.size());
		ourLog.info("Finished {}", finished.size());
		ourLog.info(CollectionUtils.disjunction(started, finished).toString());

	}


}
