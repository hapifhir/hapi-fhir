package org.hl7.fhir.r4.test;

import org.antlr.v4.runtime.BufferedTokenStream;
import org.hl7.fhir.r4.utils.transform.TestImplementor;
import org.hl7.fhir.r4.utils.transform.deserializer.FhirMapProcessor;
import org.hl7.fhir.r4.utils.transform.deserializer.FhirMapVisitor;
import org.hl7.fhir.r4.utils.transform.deserializer.grammar.antlr.javaAntlr.FhirMapJavaParser;
import org.junit.Test;

import java.io.File;
import java.util.Scanner;

public class ANTLRParseTest {

	@Test
	public void FilePathing(){
		File file = new File("");
		System.out.println(file.getAbsolutePath());
	}

	@Test
	public void TestFHIRAntlr() throws Exception {
		TestImplementor testI = new TestImplementor();

		Scanner reader = new Scanner(new File(""));
		FhirMapVisitor visitor = new FhirMapVisitor(testI);

		FhirMapProcessor p = new FhirMapProcessor();
		{
			FhirMapJavaParser grammar = p.loadGrammar("fsdfs");
		}
	}
}
