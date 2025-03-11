package ca.uhn.fhir.jpa.fql.jdbc;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

@AnalyzeClasses(
	packages = "ca.uhn.fhir.jpa.fql..",
	importOptions = {
		ImportOption.DoNotIncludeTests.class
	}
)
public class ArchitectureTest {
	/**
	 * This project has a "provided" dependency on javax.servlet, but the packaged jdbc driver doesn't bundle it.
	 */
	@ArchTest
	void verifyNoDepsOnProvidedServlet(JavaClasses theJavaClasses) {

		ArchRuleDefinition.noClasses().that().resideInAPackage("ca.uhn.fhir.jpa.fql.jdbc")
			.should().transitivelyDependOnClassesThat().resideInAPackage("javax.servlet")
			.check(theJavaClasses);
	}

}
