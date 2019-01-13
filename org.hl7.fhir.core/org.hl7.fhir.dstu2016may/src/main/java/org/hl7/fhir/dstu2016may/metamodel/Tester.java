package org.hl7.fhir.dstu2016may.metamodel;

/*-
 * #%L
 * org.hl7.fhir.dstu2016may
 * %%
 * Copyright (C) 2014 - 2019 Health Level 7
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map.Entry;

import org.hl7.fhir.dstu2016may.formats.IParser.OutputStyle;
import org.hl7.fhir.dstu2016may.metamodel.Manager.FhirFormat;
import org.hl7.fhir.dstu2016may.utils.IWorkerContext;
import org.hl7.fhir.dstu2016may.utils.SimpleWorkerContext;
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
