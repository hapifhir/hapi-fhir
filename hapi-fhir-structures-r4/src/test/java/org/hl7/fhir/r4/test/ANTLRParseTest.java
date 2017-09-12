package org.hl7.fhir.r4.test;

import org.antlr.v4.runtime.tree.ParseTree;
import org.hl7.fhir.r4.model.StructureMap;
import org.hl7.fhir.r4.utils.transform.ParseImpl;
import org.hl7.fhir.r4.utils.transform.deserializer.FhirMapProcessor;
import org.hl7.fhir.r4.utils.transform.deserializer.FhirMapVisitor;
import org.hl7.fhir.r4.utils.transform.deserializer.UrlProcessor;
import org.hl7.fhir.r4.utils.transform.deserializer.grammar.antlr.javaAntlr.FhirMapJavaParser;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ANTLRParseTest {

	@Test
	public void FilePathing(){
		File file = new File("");
		System.out.println(file.getAbsolutePath());
	}

	@Test
	public void TestFHIRAntlr() throws Exception {
		//Init a implementor of the code
		ParseImpl parse = new ParseImpl();

		//Read the Map into a string
		String map = "";
		File file = new File("colorectal3.map");
		System.out.println(file.getAbsolutePath());
		Scanner reader = new Scanner(file);
		while (reader.hasNext()){
			map += reader.nextLine()+"\n";
		}

		//Init a new visitor
		FhirMapVisitor visitor = new FhirMapVisitor(parse);

		//Make a processsor
		FhirMapProcessor p = new FhirMapProcessor(); //Is this part correct in the implementation?
		{
			FhirMapJavaParser grammar = p.loadGrammar(map);
			ParseTree tree = grammar.structureMap();
			visitor.visit(tree);
		}
		parse = (ParseImpl) visitor.getExecutor();//And this? I need to be able to pull a structure map from this object
		StructureMap structureMap = parse.structureMap;
	}

	@Test
	public void testparse() throws Exception {
		ParseImpl parse = new ParseImpl();

		//Read the Map into a string
		String map = " ";
		File file = new File("colorectal3.map");
		//System.out.println(file.getAbsolutePath());
		Scanner reader = new Scanner(file);
		while (reader.hasNext()){
			map += reader.nextLine()+"\r";
		}

		FhirMapProcessor processor = new FhirMapProcessor();

		processor.parseFhirMap(map, parse);

		System.out.println(parse.structureMap);

	}

	@Test
	public void testUrlPrse() throws Exception{
		UrlProcessor processor = new UrlProcessor();
		processor.parseUrl("\"http://fhir.hl7.org.au/fhir/rcpa/StructureMap/ColorectalMap\"");
	}
}
