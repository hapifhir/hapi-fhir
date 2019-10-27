package org.hl7.fhir.r4.test.support;

import ca.uhn.fhir.util.XmlUtil;
import com.google.common.base.Charsets;
import com.google.gson.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.r4.context.IWorkerContext;
import org.hl7.fhir.utilities.CSFile;
import org.hl7.fhir.utilities.Utilities;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestingUtilities {
	private static final boolean SHOW_DIFF = true;

	static public IWorkerContext context;
	static public boolean silent;

	static public String fixedpath;

	public static String home() {
		if (fixedpath != null)
			return fixedpath;
		String s = System.getenv("FHIR_HOME");
		if (!Utilities.noString(s))
			return s;
		s = "C:\\work\\org.hl7.fhir\\build";
		if (new File(s).exists())
			return s;
		throw new Error("FHIR Home directory not configured");
	}

	// diretory that contains all the US implementation guides
	public static String us() {
		if (fixedpath != null)
			return fixedpath;
		String s = System.getenv("FHIR_HOME");
		if (!Utilities.noString(s))
			return s;
		s = "C:\\work\\org.hl7.fhir.us";
		if (new File(s).exists())
			return s;
		throw new Error("FHIR US directory not configured");
	}

	public static String checkXMLIsSame(InputStream f1, InputStream f2) throws Exception {
		String result = compareXml(f1, f2);
		return result;
	}

	public static String checkXMLIsSame(String f1, String f2) throws Exception {
		String result = compareXml(f1, f2);
		if (result != null && SHOW_DIFF) {
			String diff = Utilities.path(System.getenv("ProgramFiles(X86)"), "WinMerge", "WinMergeU.exe");
			List<String> command = new ArrayList<String>();
			command.add("\"" + diff + "\" \"" + f1 + "\" \"" + f2 + "\"");

			ProcessBuilder builder = new ProcessBuilder(command);
			builder.directory(new CSFile("c:\\temp"));
			builder.start();

		}
		return result;
	}

	private static String compareXml(InputStream f1, InputStream f2) throws Exception {
		return compareElements("", loadXml(f1).getDocumentElement(), loadXml(f2).getDocumentElement());
	}

	private static String compareXml(String f1, String f2) throws Exception {
		return compareElements("", loadXml(f1).getDocumentElement(), loadXml(f2).getDocumentElement());
	}

	private static String compareElements(String path, Element e1, Element e2) {
		if (!e1.getNamespaceURI().equals(e2.getNamespaceURI()))
			return "Namespaces differ at " + path + ": " + e1.getNamespaceURI() + "/" + e2.getNamespaceURI();
		if (!e1.getLocalName().equals(e2.getLocalName()))
			return "Names differ at " + path + ": " + e1.getLocalName() + "/" + e2.getLocalName();
		path = path + "/" + e1.getLocalName();
		String s = compareAttributes(path, e1.getAttributes(), e2.getAttributes());
		if (!Utilities.noString(s))
			return s;
		s = compareAttributes(path, e2.getAttributes(), e1.getAttributes());
		if (!Utilities.noString(s))
			return s;

		Node c1 = e1.getFirstChild();
		Node c2 = e2.getFirstChild();
		c1 = skipBlankText(c1);
		c2 = skipBlankText(c2);
		while (c1 != null && c2 != null) {
			if (c1.getNodeType() != c2.getNodeType())
				return "node type mismatch in children of " + path + ": " + Integer.toString(e1.getNodeType()) + "/" + Integer.toString(e2.getNodeType());
			if (c1.getNodeType() == Node.TEXT_NODE) {
				if (!normalise(c1.getTextContent()).equals(normalise(c2.getTextContent())))
					return "Text differs at " + path + ": " + normalise(c1.getTextContent()) + "/" + normalise(c2.getTextContent());
			} else if (c1.getNodeType() == Node.ELEMENT_NODE) {
				s = compareElements(path, (Element) c1, (Element) c2);
				if (!Utilities.noString(s))
					return s;
			}

			c1 = skipBlankText(c1.getNextSibling());
			c2 = skipBlankText(c2.getNextSibling());
		}
		if (c1 != null)
			return "node mismatch - more nodes in source in children of " + path;
		if (c2 != null)
			return "node mismatch - more nodes in target in children of " + path;
		return null;
	}

	private static Object normalise(String text) {
		String result = text.trim().replace('\r', ' ').replace('\n', ' ').replace('\t', ' ');
		while (result.contains("  "))
			result = result.replace("  ", " ");
		return result;
	}

	private static String compareAttributes(String path, NamedNodeMap src, NamedNodeMap tgt) {
		for (int i = 0; i < src.getLength(); i++) {

			Node sa = src.item(i);
			String sn = sa.getNodeName();
			if (!(sn.equals("xmlns") || sn.startsWith("xmlns:"))) {
				Node ta = tgt.getNamedItem(sn);
				if (ta == null)
					return "Attributes differ at " + path + ": missing attribute " + sn;
				if (!normalise(sa.getTextContent()).equals(normalise(ta.getTextContent()))) {
					byte[] b1 = unBase64(sa.getTextContent());
					byte[] b2 = unBase64(ta.getTextContent());
					if (!sameBytes(b1, b2))
						return "Attributes differ at " + path + ": value " + normalise(sa.getTextContent()) + "/" + normalise(ta.getTextContent());
				}
			}
		}
		return null;
	}

	private static boolean sameBytes(byte[] b1, byte[] b2) {
		if (b1.length == 0 || b2.length == 0)
			return false;
		if (b1.length != b2.length)
			return false;
		for (int i = 0; i < b1.length; i++)
			if (b1[i] != b2[i])
				return false;
		return true;
	}

	private static byte[] unBase64(String text) {
		return Base64.decodeBase64(text);
	}

	private static Node skipBlankText(Node node) {
		while (node != null && (((node.getNodeType() == Node.TEXT_NODE) && Utilities.isWhitespace(node.getTextContent())) || (node.getNodeType() == Node.COMMENT_NODE)))
			node = node.getNextSibling();
		return node;
	}

	private static Document loadXml(String fn) throws Exception {
		return loadXml(new FileInputStream(fn));
	}

	private static Document loadXml(InputStream fn) throws Exception {
		String input = IOUtils.toString(fn, Charsets.UTF_8);
		return XmlUtil.parseDocument(input);
	}

	private static String compareObjects(String path, JsonObject o1, JsonObject o2) {
		for (Map.Entry<String, JsonElement> en : o1.entrySet()) {
			String n = en.getKey();
			if (!n.equals("fhir_comments")) {
				if (o2.has(n)) {
					String s = compareNodes(path + '.' + n, en.getValue(), o2.get(n));
					if (!Utilities.noString(s))
						return s;
				} else
					return "properties differ at " + path + ": missing property " + n;
			}
		}
		for (Map.Entry<String, JsonElement> en : o2.entrySet()) {
			String n = en.getKey();
			if (!n.equals("fhir_comments")) {
				if (!o1.has(n))
					return "properties differ at " + path + ": missing property " + n;
			}
		}
		return null;
	}

	private static String compareNodes(String path, JsonElement n1, JsonElement n2) {
		if (n1.getClass() != n2.getClass())
			return "properties differ at " + path + ": type " + n1.getClass().getName() + "/" + n2.getClass().getName();
		else if (n1 instanceof JsonPrimitive) {
			JsonPrimitive p1 = (JsonPrimitive) n1;
			JsonPrimitive p2 = (JsonPrimitive) n2;
			if (p1.isBoolean() && p2.isBoolean()) {
				if (p1.getAsBoolean() != p2.getAsBoolean())
					return "boolean property values differ at " + path + ": type " + p1.getAsString() + "/" + p2.getAsString();
			} else if (p1.isString() && p2.isString()) {
				String s1 = p1.getAsString();
				String s2 = p2.getAsString();
				if (!(s1.contains("<div") && s2.contains("<div")))
					if (!s1.equals(s2))
						if (!sameBytes(unBase64(s1), unBase64(s2)))
							return "string property values differ at " + path + ": type " + s1 + "/" + s2;
			} else if (p1.isNumber() && p2.isNumber()) {
				if (!p1.getAsString().equals(p2.getAsString()))
					return "number property values differ at " + path + ": type " + p1.getAsString() + "/" + p2.getAsString();
			} else
				return "property types differ at " + path + ": type " + p1.getAsString() + "/" + p2.getAsString();
		} else if (n1 instanceof JsonObject) {
			String s = compareObjects(path, (JsonObject) n1, (JsonObject) n2);
			if (!Utilities.noString(s))
				return s;
		} else if (n1 instanceof JsonArray) {
			JsonArray a1 = (JsonArray) n1;
			JsonArray a2 = (JsonArray) n2;

			if (a1.size() != a2.size())
				return "array properties differ at " + path + ": count " + a1.size() + "/" + a2.size();
			for (int i = 0; i < a1.size(); i++) {
				String s = compareNodes(path + "[" + i + "]", a1.get(i), a2.get(i));
				if (!Utilities.noString(s))
					return s;
			}
		} else if (n1 instanceof JsonNull) {

		} else
			return "unhandled property " + n1.getClass().getName();
		return null;
	}

	public static String temp() {
		if (new File("c:\\temp").exists())
			return "c:\\temp";
		return System.getProperty("java.io.tmpdir");
	}


}
