package org.hl7.fhir.dstu2016may.utils;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hl7.fhir.dstu2016may.formats.IParser.OutputStyle;
import org.hl7.fhir.dstu2016may.formats.XmlParser;
import org.hl7.fhir.dstu2016may.metamodel.Element;
import org.hl7.fhir.dstu2016may.metamodel.Manager;
import org.hl7.fhir.dstu2016may.metamodel.Manager.FhirFormat;
import org.hl7.fhir.dstu2016may.model.Bundle;
import org.hl7.fhir.dstu2016may.model.StructureMap;
import org.hl7.fhir.utilities.TextFile;
import org.hl7.fhir.utilities.Utilities;

public class Transformer {

	private String txServer;
	private String definitions;
	private List<String> folders = new ArrayList<String>();
	private String source;
	private String mapUri;
	private String output;
	private String message;
	private StructureMapUtilities scu;

	public String getTxServer() {
		return txServer;
	}
	public void setTxServer(String txServer) {
		this.txServer = txServer;
	}
	public String getDefinitions() {
		return definitions;
	}
	public void setDefinitions(String definitions) {
		this.definitions = definitions;
	}
	public List<String> getFolders() {
		return folders;
	}

	public void addFolder(String value) {
		folders.add(value);
	}

	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		this.output = output;
	}





	public String getMapUri() {
		return mapUri;
	}
	public void setMapUri(String mapUri) {
		this.mapUri = mapUri;
	}
	public boolean process() {
		try {
	    System.out.println("  .. load definitions from "+definitions);
			IWorkerContext context = SimpleWorkerContext.fromPack(definitions);
			scu = new StructureMapUtilities(context, new HashMap<String, StructureMap>(), null);

			for (String folder : folders) {
		    System.out.println("  .. load additional definitions from "+folder);
				((SimpleWorkerContext) context).loadFromFolder(folder);
				loadMaps(folder);
			}
	    System.out.println("  .. load source from "+source);
			Element e = Manager.parse(context, new FileInputStream(source), FhirFormat.XML);

			Bundle bundle = new Bundle();
			StructureMap map = scu.getLibrary().get(mapUri);
			if (map == null)
				throw new Error("Unable to find map "+mapUri);
			scu.transform(null, e, map, bundle);
			new XmlParser().setOutputStyle(OutputStyle.PRETTY).compose(new FileOutputStream(output), bundle);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			message = e.getMessage();
			return false;
		} 
	}
	
	private void loadMaps(String folder) {
		for (String f : new File(folder).list()) {
			try {
				StructureMap map = scu.parse(TextFile.fileToString(Utilities.path(folder, f)));
				scu.getLibrary().put(map.getUrl(), map);
			} catch (Exception e) {
			}
		}

	}
	public String getMessage() {
		return message;
	}


}
