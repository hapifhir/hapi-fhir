package org.hl7.fhir.convertors.misc;

/*-
 * #%L
 * org.hl7.fhir.convertors
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
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.hl7.fhir.utilities.TextFile;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.xml.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CKMImporter {

	private String ckm;
	private String dest;
	private String config;
	private String info;

	public static void main(String[] args) throws Exception {
		CKMImporter self = new CKMImporter();
  	self.ckm = getParam(args, "ckm");
  	self.dest = getParam(args, "dest");
  	self.config = getParam(args, "config");
  	self.info = getParam(args, "info");
	  if (self.ckm == null || self.dest == null || self.config == null) {
	  	System.out.println("ADL to FHIR StructureDefinition Converter");
	  	System.out.println("This tool takes 4 parameters:");
	  	System.out.println("-ckm: Baase URL of CKM");
	  	System.out.println("-dest: folder for output");
	  	System.out.println("-config: filename of OpenEHR/FHIR knowlege base (required)");
	  	System.out.println("-info: folder for additional knowlege of archetypes");
	  } else {
	  	self.execute();
	  }
	}

	private static String getParam(String[] args, String name) {
	  for (int i = 0; i < args.length - 1; i++) {
	  	if (args[i].equals("-"+name)) {
	  		return args[i+1];
	  	}
	  }
	  return null;
	}


	private void execute() throws Exception {
		List<String> ids = new ArrayList<String>();
		Document xml = loadXml(ckm + "/services/ArchetypeFinderBean/getAllArchetypeIds");
		Element e = XMLUtil.getFirstChild(xml.getDocumentElement());
		while (e != null) {
			ids.add(e.getTextContent());
			e = XMLUtil.getNextSibling(e);
		}
//		for (String id : ids) {
//			downloadArchetype(id);
//		}
		for (String id : ids) {
			processArchetype(id);
		}
	}

	private void downloadArchetype(String id) throws Exception {
		System.out.println("Fetch "+id);
		Document sxml = loadXml(ckm+"/services/ArchetypeFinderBean/getArchetypeInXML?archetypeId="+id);
		Element e = XMLUtil.getFirstChild(sxml.getDocumentElement());
		
		String src = Utilities.path("c:\\temp", id+".xml");
		TextFile.stringToFile(e.getTextContent(), src);
	}

	private void processArchetype(String id) throws Exception {
		System.out.println("Process "+id);
		
		String cfg = info == null ? null : Utilities.path(info, id+".config");
		String src = Utilities.path("c:\\temp", id+".xml");
		String dst = Utilities.path(dest, id+".xml");

		if (!new File(src).exists())
			downloadArchetype(id);
		if (cfg != null && new File(cfg).exists())
			ADLImporter.main(new String[] {"-source", src, "-dest", dst, "-config", config, "-info", cfg});
		else
			ADLImporter.main(new String[] {"-source", src, "-dest", dst, "-config", config});	
	}

	private Document loadXml(String address) throws Exception {
		URL url = new URL(address);
		HttpURLConnection connection =
		    (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Accept", "application/xml");

		InputStream xml = connection.getInputStream();

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		return db.parse(xml);
	}
}
