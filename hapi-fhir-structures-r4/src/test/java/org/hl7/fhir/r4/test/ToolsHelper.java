/*
Copyright (c) 2011+, HL7, Inc
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, 
are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this 
   list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, 
   this list of conditions and the following disclaimer in the documentation 
   and/or other materials provided with the distribution.
 * Neither the name of HL7 nor the names of its contributors may be used to 
   endorse or promote products derived from this software without specific 
   prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
POSSIBILITY OF SUCH DAMAGE.

 */
package org.hl7.fhir.r4.test;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.hl7.fhir.r4.context.SimpleWorkerContext;
import org.hl7.fhir.r4.formats.IParser.OutputStyle;
import org.hl7.fhir.r4.formats.JsonParser;
import org.hl7.fhir.r4.formats.RdfParser;
import org.hl7.fhir.r4.formats.RdfParserBase;
import org.hl7.fhir.r4.formats.XmlParser;
import org.hl7.fhir.r4.model.Constants;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.FHIRFormatError;
import org.hl7.fhir.utilities.CSFile;
import org.hl7.fhir.utilities.CSFileInputStream;
import org.hl7.fhir.utilities.TextFile;
import org.hl7.fhir.utilities.Utilities;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class ToolsHelper {

	public static void main(String[] args) {   
		try {
			ToolsHelper self = new ToolsHelper();
			if (args.length == 0) 
				throw new FHIRException("Missing Command Parameter. Valid Commands: round, json, version, fragments, snapshot-maker");
			if (args[0].equals("round")) 
				self.executeRoundTrip(args);
			else if (args[0].equals("test")) 
				self.executeTest(args);
			else if (args[0].equals("examples")) 
				self.executeExamples(args);
			else if (args[0].equals("json")) 
				self.executeJson(args);
			else if (args[0].equals("cxml")) 
				self.executeCanonicalXml(args);
			else if (args[0].equals("version")) 
				self.executeVersion(args);
			else if (args[0].equals("fragments")) 
				self.executeFragments(args);
			else if (args[0].equals("snapshot-maker")) 
				self.generateSnapshots(args);
			else 
				throw new FHIRException("Unknown command '"+args[0]+"'. Valid Commands: round, test, examples, json, cxml, version, fragments, snapshot-maker");
		} catch (Throwable e) {
			try {
				e.printStackTrace();
				TextFile.stringToFile(e.toString(), (args.length == 0 ? "tools" : args[0])+".err");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	private void executeExamples(String[] args) throws IOException {
		try {
			@SuppressWarnings("unchecked")
			List<String> lines = FileUtils.readLines(new File(args[1]), "UTF-8");
			String srcDir = lines.get(0);
			lines.remove(0);
			processExamples(srcDir, lines);
			TextFile.stringToFile("ok", Utilities.changeFileExt(args[1], ".out"));
		} catch (Exception e) {
			TextFile.stringToFile(e.getMessage(), Utilities.changeFileExt(args[1], ".out"));			
		}
	}

	private void generateSnapshots(String[] args) throws IOException, FHIRException {
		if (args.length == 1) {
			System.out.println("tools.jar snapshot-maker [source] -defn [definitions]");
			System.out.println("");
			System.out.println("Generates a snapshot from a differential. The nominated profile must have a single struture that has a differential");
			System.out.println("");
			System.out.println("source - the profile to generate the snapshot for. Maybe a file name, or a URL reference to a server running FHIR RESTful API");
			System.out.println("definitions - filename for local copy of the validation.zip file");			
		}
		String address = args[1];
		String definitions = args[3];

		SimpleWorkerContext context = SimpleWorkerContext.fromDefinitions(getDefinitions(definitions));

		//    if (address.startsWith("http:") || address.startsWith("http:")) {
		//    	// this is on a restful interface
		//    	String[] parts = address.split("\\/Profile\\/");
		//    	if (parts.length != 2)
		//    		throw new FHIRException("Unable to understand address of profile");
		//    	StructureDefinition profile = context.fetchResource(StructureDefinition.class, parts[1]);
		//    	ProfileUtilities utils = new ProfileUtilities(context);
		//    	StructureDefinition base = utils.getProfile(profile, profile.getBase());
		//    	if (base == null)
		//    		throw new FHIRException("Unable to resolve profile "+profile.getBase());
		//    	utils.generateSnapshot(base, profile, address, profile.getName(), null, null);
		//    	// client.update(StructureDefinition.class, profile, parts[1]);
		//    } else {
		throw new NotImplementedException("generating snapshots not done yet (address = "+address+")");
		//    }
	}

	private Map<String, byte[]> getDefinitions(String definitions) throws IOException, FHIRException {
		Map<String, byte[]> results = new HashMap<String, byte[]>();
		readDefinitions(results, loadDefinitions(definitions));
		return results;
	}

	private void readDefinitions(Map<String, byte[]> map, byte[] defn) throws IOException {
		ZipInputStream zip = new ZipInputStream(new ByteArrayInputStream(defn));
		ZipEntry ze;
		while ((ze = zip.getNextEntry()) != null) {
			if (!ze.getName().endsWith(".zip") && !ze.getName().endsWith(".jar") ) { // skip saxon .zip
				String name = ze.getName();
				InputStream in = zip;
				ByteArrayOutputStream b = new ByteArrayOutputStream();
				int n;
				byte[] buf = new byte[1024];
				while ((n = in.read(buf, 0, 1024)) > -1) {
					b.write(buf, 0, n);
				}        
				map.put(name, b.toByteArray());
			}
			zip.closeEntry();
		}
		zip.close();    
	}

	private byte[] loadDefinitions(String definitions) throws FHIRException, IOException {
		byte[] defn;
		//    if (Utilities.noString(definitions)) {
		//      defn = loadFromUrl(MASTER_SOURCE);
		//    } else 
		if (definitions.startsWith("https:") || definitions.startsWith("http:")) {
			defn = loadFromUrl(definitions);
		} else if (new File(definitions).exists()) {
			defn = loadFromFile(definitions);      
		} else
			throw new FHIRException("Unable to find FHIR validation Pack (source = "+definitions+")");
		return defn;
	}

	private byte[] loadFromUrl(String src) throws IOException {
		URL url = new URL(src);
		byte[] str = IOUtils.toByteArray(url.openStream());
		return str;
	}

	private byte[] loadFromFile(String src) throws IOException {
		FileInputStream in = new FileInputStream(src);
		byte[] b = new byte[in.available()];
		in.read(b);
		in.close();
		return b;
	}


	protected XmlPullParser loadXml(InputStream stream) throws XmlPullParserException, IOException {
		BufferedInputStream input = new BufferedInputStream(stream);
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance(System.getProperty(XmlPullParserFactory.PROPERTY_NAME), null);
		factory.setNamespaceAware(true);
		XmlPullParser xpp = factory.newPullParser();
		xpp.setInput(input, "UTF-8");
		xpp.next();
		return xpp;
	}

	protected int nextNoWhitespace(XmlPullParser xpp) throws XmlPullParserException, IOException {
		int eventType = xpp.getEventType();
		while (eventType == XmlPullParser.TEXT && xpp.isWhitespace())
			eventType = xpp.next();
		return eventType;
	}

	public void executeFragments(String[] args) throws IOException {
		try {
			File source = new CSFile(args[1]);
			if (!source.exists())        
				throw new FHIRException("Source File \""+source.getAbsolutePath()+"\" not found");
			XmlPullParser xpp = loadXml(new FileInputStream(source));
			nextNoWhitespace(xpp);
			if (!xpp.getName().equals("tests"))
				throw new FHIRFormatError("Unable to parse file - starts with "+xpp.getName());
			xpp.next();
			nextNoWhitespace(xpp);
			StringBuilder s = new StringBuilder();
			s.append("<results>\r\n");
			int fail = 0;
			while (xpp.getEventType() == XmlPullParser.START_TAG && xpp.getName().equals("test")) {
				String id = xpp.getAttributeValue(null, "id");
				String type = xpp.getAttributeValue(null, "type");
				// test
				xpp.next();
				nextNoWhitespace(xpp);
				// pre
				xpp.next();
				nextNoWhitespace(xpp);
				XmlParser p = new XmlParser();
				try {
					p.parseFragment(xpp, type);
					s.append("<result id=\""+id+"\" outcome=\"ok\"/>\r\n");
					nextNoWhitespace(xpp);
				} catch (Exception e) {
					s.append("<result id=\""+id+"\" outcome=\"error\" msg=\""+Utilities.escapeXml(e.getMessage())+"\"/>\r\n");
					fail++;
				}
				while (xpp.getEventType() != XmlPullParser.END_TAG || !xpp.getName().equals("pre")) 
					xpp.next();
				xpp.next();
				nextNoWhitespace(xpp);
				xpp.next();
				nextNoWhitespace(xpp);
			}
			s.append("</results>\r\n");

			TextFile.stringToFile(s.toString(), args[2]);
		} catch (Exception e) {
			e.printStackTrace();
			TextFile.stringToFile(e.getMessage(), args[2]);
		}
	}

	public void executeRoundTrip(String[] args) throws IOException, FHIRException {
		FileInputStream in;
		File source = new CSFile(args[1]);
		File dest = new CSFile(args[2]);
		if (args.length >= 4) {
			Utilities.copyFile(args[1], args[3]);
		}

		if (!source.exists())        
			throw new FHIRException("Source File \""+source.getAbsolutePath()+"\" not found");
		in = new CSFileInputStream(source);
		XmlParser p = new XmlParser();
		JsonParser parser = new JsonParser();
		JsonParser pj = parser;
		Resource rf = p.parse(in);
		ByteArrayOutputStream json = new ByteArrayOutputStream();
		parser.setOutputStyle(OutputStyle.PRETTY);
		parser.compose(json, rf);
		json.close();
		TextFile.stringToFile(new String(json.toByteArray()), Utilities.changeFileExt(dest.getAbsolutePath(), ".json"));
		rf = pj.parse(new ByteArrayInputStream(json.toByteArray()));
		FileOutputStream s = new FileOutputStream(dest);
		new XmlParser().compose(s, rf, true);
		s.close();
	}

	public String executeJson(String[] args) throws IOException, FHIRException {
		FileInputStream in;
		File source = new CSFile(args[1]);
		File dest = new CSFile(args[2]);
		File destc = new CSFile(Utilities.changeFileExt(args[2], ".canonical.json"));
		File destt = new CSFile(args[2]+".tmp");
		File destr = new CSFile(Utilities.changeFileExt(args[2], ".ttl"));

		if (!source.exists())        
			throw new FHIRException("Source File \""+source.getAbsolutePath()+"\" not found");
		in = new CSFileInputStream(source);
		XmlParser p = new XmlParser();
		Resource rf = p.parse(in);
		JsonParser json = new JsonParser();
		json.setOutputStyle(OutputStyle.PRETTY);
		FileOutputStream s = new FileOutputStream(dest);
		json.compose(s, rf);
		s.close();
		json.setOutputStyle(OutputStyle.CANONICAL);
		s = new FileOutputStream(destc);
		json.compose(s, rf);
		s.close();
		json.setSuppressXhtml("Snipped for Brevity");
		json.setOutputStyle(OutputStyle.PRETTY);
		s = new FileOutputStream(destt);
		json.compose(s, rf);
		s.close();    

		RdfParserBase rdf = new RdfParser();
		s = new FileOutputStream(destr);
		rdf.compose(s, rf);
		s.close();
		
		return TextFile.fileToString(destt.getAbsolutePath());
	}

	public void executeCanonicalXml(String[] args) throws FHIRException, IOException {
		FileInputStream in;
		File source = new CSFile(args[1]);
		File dest = new CSFile(args[2]);

		if (!source.exists())        
			throw new FHIRException("Source File \""+source.getAbsolutePath()+"\" not found");
		in = new CSFileInputStream(source);
		XmlParser p = new XmlParser();
		Resource rf = p.parse(in);
		XmlParser cxml = new XmlParser();
		cxml.setOutputStyle(OutputStyle.NORMAL);
		cxml.compose(new FileOutputStream(dest), rf);
	}

	private void executeVersion(String[] args) throws IOException {
		TextFile.stringToFile(org.hl7.fhir.r4.utils.Version.VERSION+":"+Constants.VERSION, args[1]);
	}

	public void processExamples(String rootDir, Collection<String> list) throws FHIRException  {
		for (String n : list) {
			try {
				String filename = rootDir + n + ".xml";
				// 1. produce canonical XML
				CSFileInputStream source = new CSFileInputStream(filename);
				FileOutputStream dest = new FileOutputStream(Utilities.changeFileExt(filename, ".canonical.xml"));
				XmlParser p = new XmlParser();
				Resource r = p.parse(source);
				XmlParser cxml = new XmlParser();
				cxml.setOutputStyle(OutputStyle.CANONICAL);
				cxml.compose(dest, r);

				// 2. produce JSON
				source = new CSFileInputStream(filename);
				dest = new FileOutputStream(Utilities.changeFileExt(filename, ".json"));
				r = p.parse(source);
				JsonParser json = new JsonParser();
				json.setOutputStyle(OutputStyle.PRETTY);
				json.compose(dest, r);
				json = new JsonParser();
				json.setOutputStyle(OutputStyle.CANONICAL);
				dest = new FileOutputStream(Utilities.changeFileExt(filename, ".canonical.json"));
				json.compose(dest, r);
				
				// 2. produce JSON
				dest = new FileOutputStream(Utilities.changeFileExt(filename, ".ttl"));
				RdfParserBase rdf = new RdfParser();
				rdf.compose(dest, r);
			} catch (Exception e) {
				e.printStackTrace();
				throw new FHIRException("Error Processing "+n+".xml: "+e.getMessage(), e);
			}
		}
	}

	public void testRoundTrip(String rootDir, String tmpDir, Collection<String> names) throws Throwable {
		try {
			System.err.println("Round trip from "+rootDir+" to "+tmpDir+":"+Integer.toString(names.size())+" files");
			for (String n : names) {
				System.err.print("  "+n);
				String source = rootDir + n + ".xml";
				// String tmpJson = tmpDir + n + ".json";
				String tmp = tmpDir + n.replace(File.separator, "-") + ".tmp";
				String dest = tmpDir + n.replace(File.separator, "-") + ".java.xml";

				FileInputStream in = new FileInputStream(source);
				XmlParser xp = new XmlParser();
				Resource r = xp.parse(in);
				System.err.print(".");
				JsonParser jp = new JsonParser();
				FileOutputStream out = new FileOutputStream(tmp);
				jp.setOutputStyle(OutputStyle.PRETTY);
				jp.compose(out, r);
				out.close();
				r = null;
				System.err.print(".");

				in = new FileInputStream(tmp);
				System.err.print(",");
				r = jp.parse(in);
				System.err.print(".");
				out = new FileOutputStream(dest);
				new XmlParser().compose(out, r, true);
				System.err.println("!");
				out.close();
				r = null;
				System.gc();
			}
		} catch (Throwable e) {
			System.err.println("Error: "+e.getMessage());
			throw e;
		}
	}

	private void executeTest(String[] args) throws Throwable {
		try {
			@SuppressWarnings("unchecked")
			List<String> lines = FileUtils.readLines(new File(args[1]), "UTF-8");
			String srcDir = lines.get(0);
			lines.remove(0);
			String dstDir = lines.get(0).trim();
			lines.remove(0);
			testRoundTrip(srcDir, dstDir, lines);
			TextFile.stringToFile("ok", Utilities.changeFileExt(args[1], ".out"));
		} catch (Exception e) {
			TextFile.stringToFile(e.getMessage(), Utilities.changeFileExt(args[1], ".out"));			
		}
	}
}
