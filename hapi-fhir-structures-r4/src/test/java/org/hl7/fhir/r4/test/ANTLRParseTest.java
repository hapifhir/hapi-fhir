package org.hl7.fhir.r4.test;

import ca.uhn.fhir.context.FhirContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.hl7.fhir.r4.context.IWorkerContext;
import org.hl7.fhir.r4.hapi.ctx.DefaultProfileValidationSupport;
import org.hl7.fhir.r4.hapi.ctx.HapiWorkerContext;
import org.hl7.fhir.r4.hapi.ctx.PrePopulatedValidationSupport;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.StructureMap;
import org.hl7.fhir.r4.utils.StructureMapUtilities;
import org.hl7.fhir.r4.utils.transform.ParseImpl;
import org.hl7.fhir.r4.utils.transform.deserializer.FhirMapProcessor;
import org.hl7.fhir.r4.utils.transform.deserializer.FhirMapVisitor;
import org.hl7.fhir.r4.utils.transform.deserializer.UrlProcessor;
import org.hl7.fhir.r4.utils.transform.deserializer.grammar.antlr.javaAntlr.FhirMapJavaParser;
import org.hl7.fhir.utilities.TextFile;
import org.junit.Test;

import javax.xml.bind.helpers.DefaultValidationEventHandler;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

	@Test
	public void testTransform() throws Exception {
		StructureMapUtilities scu = null;
		StructureDefinition sd1 = null;
		PrePopulatedValidationSupport validation = new PrePopulatedValidationSupport();
		Map<String, StructureMap> maps = new HashMap<String, StructureMap>();

		FhirContext context = FhirContext.forR4();
		context.setValidationSupport(validation);
		HapiWorkerContext hapiContext = new HapiWorkerContext(context, validation);
		sd1 = context.newXmlParser().parseResource(StructureDefinition.class, new FileReader(new File("C:\\JCimiProject\\hapi-fhir-resource-profile-generator\\target\\classes\\mapping\\logical\\structuredefinition-colorectal.xml")));
		if (sd1.getId().contains("/"))
			sd1.setId(sd1.getId().split("/")[sd1.getId().split("/").length - 1]);
		validation.addStructureDefinition(sd1);
		for (StructureDefinition sd : new DefaultProfileValidationSupport().fetchAllStructureDefinitions(FhirContext.forR4())){
			validation.addStructureDefinition(sd);
		}
		StructureMap map = null;
		scu = new StructureMapUtilities(hapiContext, maps, null, null);
		map = scu.parse(TextFile.fileToString("colorectal3.map"));
		maps.put(map.getUrl(), map);
		List<StructureDefinition> result = scu.analyse(null, map).getProfiles();
		for (StructureDefinition sd : result)
			System.out.println(sd);

	}


}
