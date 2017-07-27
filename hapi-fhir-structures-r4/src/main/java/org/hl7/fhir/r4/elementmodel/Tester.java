package org.hl7.fhir.r4.elementmodel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map.Entry;

import org.hl7.fhir.r4.context.IWorkerContext;
import org.hl7.fhir.r4.context.SimpleWorkerContext;
import org.hl7.fhir.r4.elementmodel.Manager.FhirFormat;
import org.hl7.fhir.r4.formats.IParser.OutputStyle;
import org.hl7.fhir.utilities.TextFile;
import org.hl7.fhir.utilities.Utilities;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Tester {

	public static void main(String[] args) throws Exception {
		IWorkerContext context = SimpleWorkerContext.fromPack(Utilities.path("C:\\work\\org.hl7.fhir\\build\\publish", "validation-min.xml.zip"));
		int t = 0;
		int ok = 0;
		for (String f : new File("C:\\work\\org.hl7.fhir\\build\\publish").list()) {
			if (f.endsWith(".xml") && !f.endsWith(".canonical.xml") && !f.contains("profile") && !f.contains("questionnaire") && new File("C:\\work\\org.hl7.fhir\\build\\publish\\"+Utilities.changeFileExt(f, ".ttl")).exists()) {
//				if (f.equals("account-questionnaire.xml")) {
				System.out.print("convert "+f);
//				Manager.convert(context, new FileInputStream("C:\\work\\org.hl7.fhir\\build\\publish\\"+f), FhirFormat.XML, 
//						new FileOutputStream("C:\\work\\org.hl7.fhir\\build\\publish\\"+Utilities.changeFileExt(f, ".mm.json")), FhirFormat.JSON, OutputStyle.PRETTY);
//				String src = normalise(TextFile.fileToString("C:\\work\\org.hl7.fhir\\build\\publish\\"+Utilities.changeFileExt(f, ".mm.json")));
//				String tgt = normalise(TextFile.fileToString("C:\\work\\org.hl7.fhir\\build\\publish\\"+Utilities.changeFileExt(f, ".json")));
				Element e = Manager.parse(context, new FileInputStream("C:\\work\\org.hl7.fhir\\build\\publish\\"+f), FhirFormat.XML);
				Manager.compose(context, e, new FileOutputStream("C:\\work\\org.hl7.fhir\\build\\publish\\"+Utilities.changeFileExt(f, ".mm.ttl")), FhirFormat.TURTLE, OutputStyle.PRETTY, null);
        Manager.compose(context, e, new FileOutputStream("C:\\temp\\resource.xml"), FhirFormat.XML, OutputStyle.PRETTY, null);
				String src = TextFile.fileToString("C:\\work\\org.hl7.fhir\\build\\publish\\"+Utilities.changeFileExt(f, ".mm.ttl"));
				String tgt = TextFile.fileToString("C:\\work\\org.hl7.fhir\\build\\publish\\"+Utilities.changeFileExt(f, ".ttl"));
				t++;
				if (src.equals(tgt)) {
					System.out.println(".. ok");
					ok++;
				} else
					System.out.println(".. fail");
				}
//			}
		}
		System.out.println("done - "+Integer.toString(t)+" files, "+Integer.toString(ok)+" ok");
	}

	private static com.google.gson.JsonParser  parser = new com.google.gson.JsonParser();
	
	private static String normalise(String s) {
		JsonObject json = parser.parse(s).getAsJsonObject();
		JsonElement txt = json.get("text");
		if (txt != null) {
			if (((JsonObject) txt).has("div"))
			((JsonObject) txt).remove("div");
		}
		removeComments(json);
	  return json.toString();
	}

	private static void removeComments(JsonArray arr) {
	  for (JsonElement i : arr) {
	  	if (i instanceof JsonObject)
	  		removeComments((JsonObject) i);
	  	if (i instanceof JsonArray) 
	  		removeComments((JsonArray) i);
	  }
	}
	
	private static void removeComments(JsonObject json) {
		if (json.has("fhir_comments"))
			json.remove("fhir_comments");
	  for (Entry<String, JsonElement> p : json.entrySet()) {
	  	if (p.getValue() instanceof JsonObject) 
	  		removeComments((JsonObject) p.getValue());
	  	if (p.getValue() instanceof JsonArray) 
	  		removeComments((JsonArray) p.getValue());
	  }
	
	}

}
