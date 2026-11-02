package ca.uhn.fhir.batch;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.fail;

@AnalyzeClasses(
	packages = { "ca.uhn.fhir" },
	importOptions = {ImportOption.DoNotIncludeTests.class, ImportOption.DoNotIncludeArchives.class}
)
public class BatchJobArchitectureCheckTest {
	private static final Logger ourLog = LoggerFactory.getLogger(BatchJobArchitectureCheckTest.class);

	@ArchTest
	public void noUnreviewedSystemRequestDetails(JavaClasses theJavaClasses) {
		List<BatchClassWalker.CallChain> violations = new BatchClassWalker(theJavaClasses)
			.rootsIn("ca.uhn.fhir")
			.stopAt(BatchClassWalker.DEFAULT_CLASSES_TO_OMIT)
			.findSystemRequestDetailsCreation();

		if (!violations.isEmpty()) {
			ourLog.error("Found {} errors", violations.size());

			StringBuilder vsb = new StringBuilder();
			violations.forEach(c -> {
				vsb.append("\n");
				vsb.append(c.CreationSite())
						.append(" Call Stack : ")
							.append("(")
								.append(String.join(", ", c.Methods()))
									.append(")");
			});

			String errorMsg = vsb.toString();
			fail(errorMsg);
		}
	}
}
