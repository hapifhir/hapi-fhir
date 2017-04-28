/*
 * Copyright (c) 2011+, HL7, Inc
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of HL7 nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific
 * prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 */
package org.hl7.fhir.utilities.xhtml;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.*;
import java.util.*;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.Comment;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.FHIRFormatError;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XhtmlParser {
	public static final String XHTML_NS = "http://www.w3.org/1999/xhtml";

	private Set<String> attributes = new HashSet<String>();

	private String cache = "";

	private int col = 0;
	private Set<String> elements = new HashSet<String>();

	private char lastChar;

	private String lastText = "";

	private int line = 1;

	private boolean mustBeWellFormed = true;
	private ParserSecurityPolicy policy;
	private Reader rdr;

	private boolean trimWhitespace;

	private XhtmlNode unwindPoint;

	private boolean validatorMode;

	public XhtmlParser() {
		super();
		policy = ParserSecurityPolicy.Accept; // for general parsing

		// set up sets
		elements.add("p");
		elements.add("br");
		elements.add("div");
		elements.add("h1");
		elements.add("h2");
		elements.add("h3");
		elements.add("h4");
		elements.add("h5");
		elements.add("h6");
		elements.add("a");
		elements.add("span");
		elements.add("b");
		elements.add("em");
		elements.add("i");
		elements.add("strong");
		elements.add("small");
		elements.add("big");
		elements.add("tt");
		elements.add("small");
		elements.add("dfn");
		elements.add("q");
		elements.add("var");
		elements.add("abbr");
		elements.add("acronym");
		elements.add("cite");
		elements.add("blockquote");
		elements.add("hr");
		elements.add("address");
		elements.add("bdo");
		elements.add("kbd");
		elements.add("q");
		elements.add("sub");
		elements.add("sup");
		elements.add("ul");
		elements.add("ol");
		elements.add("li");
		elements.add("dl");
		elements.add("dt");
		elements.add("dd");
		elements.add("pre");
		elements.add("table");
		elements.add("caption");
		elements.add("colgroup");
		elements.add("col");
		elements.add("thead");
		elements.add("tr");
		elements.add("tfoot");
		elements.add("tbody");
		elements.add("th");
		elements.add("td");
		elements.add("code");
		elements.add("samp");
		elements.add("img");
		elements.add("map");
		elements.add("area");

		attributes.add("title");
		attributes.add("style");
		attributes.add("class");
		attributes.add("id");
		attributes.add("lang");
		attributes.add("xml:lang");
		attributes.add("dir");
		attributes.add("accesskey");
		attributes.add("tabindex");
		// tables:
		attributes.add("span");
		attributes.add("width");
		attributes.add("align");
		attributes.add("valign");
		attributes.add("char");
		attributes.add("charoff");
		attributes.add("abbr");
		attributes.add("axis");
		attributes.add("headers");
		attributes.add("scope");
		attributes.add("rowspan");
		attributes.add("colspan");

		attributes.add("a.href");
		attributes.add("a.name");
		attributes.add("img.src");
		attributes.add("img.border");
		attributes.add("div.xmlns");
		attributes.add("blockquote.cite");
		attributes.add("q.cite");
		attributes.add("a.charset");
		attributes.add("a.type");
		attributes.add("a.name");
		attributes.add("a.href");
		attributes.add("a.hreflang");
		attributes.add("a.rel");
		attributes.add("a.rev");
		attributes.add("a.shape");
		attributes.add("a.coords");
		attributes.add("img.src");
		attributes.add("img.alt");
		attributes.add("img.longdesc");
		attributes.add("img.height");
		attributes.add("img.width");
		attributes.add("img.usemap");
		attributes.add("img.ismap");
		attributes.add("map.name");
		attributes.add("area.shape");
		attributes.add("area.coords");
		attributes.add("area.href");
		attributes.add("area.nohref");
		attributes.add("area.alt");
		attributes.add("table.summary");
		attributes.add("table.width");
		attributes.add("table.border");
		attributes.add("table.frame");
		attributes.add("table.rules");
		attributes.add("table.cellspacing");
		attributes.add("table.cellpadding");
	}

	private void addTextNode(XhtmlNode node, StringBuilder s) {
		String t = isTrimWhitespace() ? s.toString().trim() : s.toString();
		if (t.length() > 0) {
			lastText = t;
			// System.out.println(t);
			node.addText(t);
			s.setLength(0);
		}
	}

	private boolean attributeIsOk(String elem, String attr, String value) throws FHIRFormatError {
		if (validatorMode)
			return true;
		boolean ok = attributes.contains(attr) || attributes.contains(elem + "." + attr);
		if (ok) {
			return true;
		}
		switch (policy) {
		case Accept:
			return true;
		case Drop:
			return false;
		case Reject:
			throw new FHIRFormatError("Illegal HTML attribute " + elem + "." + attr);
		}

		if ((elem + "." + attr).equals("img.src") && !(value.startsWith("#") || value.startsWith("http:") || value.startsWith("https:"))) {
			switch (policy) {
			case Accept:
				return true;
			case Drop:
				return false;
			case Reject:
				throw new FHIRFormatError("Illegal Image Reference " + value);
			}
		}
		return false;
	}

	private NSMap checkNamespaces(QName n, XhtmlNode node, NSMap nsm, boolean root) {
		// what we do here is strip out any stated namespace attributes, putting them in the namesapce map
		// then we figure out what the namespace of this element is, and state it explicitly if it's not the default

		// but we don't bother with any of this if we're not validating
		if (!validatorMode)
			return null;
		NSMap result = new NSMap(nsm);
		List<String> nsattrs = new ArrayList<String>();
		for (String an : node.getAttributes().keySet()) {
			if (an.equals("xmlns")) {
				result.def(node.getAttribute(an));
				nsattrs.add(an);
			}
			if (an.startsWith("xmlns:")) {
				result.ns(an.substring(6), node.getAttribute(an));
				nsattrs.add(an);
			}
		}
		for (String s : nsattrs)
			node.getAttributes().remove(s);
		if (n.hasNs()) {
			String nns = result.get(n.getNs());
			if (!nns.equals(result.def())) {
				node.getAttributes().put("xmlns", nns);
				result.def(nns);
			}
		} else if (root && result.hasDef()) {
			node.getAttributes().put("xmlns", result.def());
		}
		return result;
	}

	private String checkNS(XhtmlNode res, Element node, String defaultNS) {
		if (!validatorMode)
			return null;
		String ns = node.getNamespaceURI();
		if (ns == null)
			return null;
		if (!ns.equals(defaultNS)) {
			res.getAttributes().put("xmlns", ns);
			return ns;
		}
		return defaultNS;
	}

	private String descLoc() {
		return " at line " + Integer.toString(line) + " column " + Integer.toString(col);
	}

	private boolean elementIsOk(String name) throws FHIRFormatError {
		if (validatorMode)
			return true;
		boolean ok = elements.contains(name);
		if (ok){
			return true;
		}
		switch (policy) {
		case Accept:
			return true;
		case Drop:
			return false;
		case Reject:
			throw new FHIRFormatError("Illegal HTML element " + name);
		}
		return false;
	}

	public ParserSecurityPolicy getPolicy() {
		return policy;
	}

	private boolean isInteger(String s, int base) {
		try {
			Integer.parseInt(s, base);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isMustBeWellFormed() {
		return mustBeWellFormed;
	}

	private boolean isNameChar(char ch) {
		return Character.isLetterOrDigit(ch) || ch == '_' || ch == '-' || ch == ':';
	}

	public boolean isTrimWhitespace() {
		return trimWhitespace;
	}

	public boolean isValidatorMode() {
		return validatorMode;
	}

	public XhtmlDocument parse(InputStream input, String entryName) throws FHIRFormatError, IOException {
		rdr = new InputStreamReader(input, "UTF-8");
		return parse(entryName);
	}

	private XhtmlDocument parse(String entryName) throws FHIRFormatError, IOException {
		XhtmlDocument result = new XhtmlDocument();
		skipWhiteSpaceAndComments(result);
		if (peekChar() != '<')
			throw new FHIRFormatError("Unable to Parse HTML - does not start with tag. Found " + peekChar() + descLoc());
		readChar();
		QName n = new QName(readName().toLowerCase());
		if ((entryName != null) && !n.getName().equals(entryName))
			throw new FHIRFormatError("Unable to Parse HTML - starts with '" + n + "' not '" + entryName + "'" + descLoc());
		XhtmlNode root = result.addTag(n.getName());
		parseAttributes(root);
		NSMap nsm = checkNamespaces(n, root, null, true);
		if (readChar() == '/') {
			if (peekChar() != '>')
				throw new FHIRFormatError("unexpected non-end of element " + n + " " + descLoc());
			readChar();
		} else {
			unwindPoint = null;
			List<XhtmlNode> p = new ArrayList<XhtmlNode>();
			parseElementInner(root, p, nsm);
		}
		return result;
	}

	public XhtmlDocument parse(String source, String entryName) throws FHIRFormatError, IOException {
		rdr = new StringReader(source);
		return parse(entryName);
	}

	private void parseAttributes(XhtmlNode node) throws FHIRFormatError, IOException {
		while (Character.isWhitespace(peekChar()))
			readChar();
		while (peekChar() != '>' && peekChar() != '/' && peekChar() != '\0') {
			String name = readName();
			if (name.length() == 0) {
				throw new FHIRFormatError("Unable to read attribute on <" + node.getName() + ">" + descLoc());
			}
			while (Character.isWhitespace(peekChar()))
				readChar();

			if (isNameChar(peekChar()) || peekChar() == '>' || peekChar() == '/')
				node.getAttributes().put(name, null);
			else if (peekChar() != '=') {
				throw new FHIRFormatError("Unable to read attribute '" + name + "' value on <" + node.getName() + ">" + descLoc());
			} else {
				readChar();
				while (Character.isWhitespace(peekChar()))
					readChar();
				if (peekChar() == '"' || peekChar() == '\'')
					node.getAttributes().put(name, parseAttributeValue(readChar()));
				else
					node.getAttributes().put(name, parseAttributeValue('\0'));
			}
			while (Character.isWhitespace(peekChar()))
				readChar();
		}
	}

	private String parseAttributeValue(char term) throws IOException, FHIRFormatError {
		StringBuilder b = new StringBuilder();
		while (peekChar() != '\0' && peekChar() != '>' && (term != '\0' || peekChar() != '/') && peekChar() != term) {
			if (peekChar() == '&') {
				parseLiteral(b);
			} else
				b.append(readChar());
		}
		if (peekChar() == term)
			readChar();
		return b.toString();
	}

	private void parseElement(XhtmlNode parent, List<XhtmlNode> parents, NSMap nsm) throws IOException, FHIRFormatError {
		QName name = new QName(readName());
		XhtmlNode node = parent.addTag(name.getName());
		List<XhtmlNode> newParents = new ArrayList<XhtmlNode>();
		newParents.addAll(parents);
		newParents.add(parent);
		parseAttributes(node);
		nsm = checkNamespaces(name, node, nsm, false);
		if (readChar() == '/') {
			if (peekChar() != '>')
				throw new FHIRFormatError("unexpected non-end of element " + name + " " + descLoc());
			readChar();
		} else {
			parseElementInner(node, newParents, nsm);
		}
	}

	private void parseElementInner(XhtmlNode node, List<XhtmlNode> parents, NSMap nsm) throws FHIRFormatError, IOException {
		StringBuilder s = new StringBuilder();
		while (peekChar() != '\0' && !parents.contains(unwindPoint) && !(node == unwindPoint)) {
			if (peekChar() == '<') {
				addTextNode(node, s);
				readChar();
				if (peekChar() == '!') {
					String sc = readToCommentEnd();
					if (sc.startsWith("DOCTYPE"))
						throw new FHIRFormatError("Malformed XHTML: Found a DocType declaration, and these are not allowed (XXE security vulnerability protection)");
					node.addComment(sc);
				} else if (peekChar() == '?')
					node.addComment(readToTagEnd());
				else if (peekChar() == '/') {
					readChar();
					QName n = new QName(readToTagEnd());
					if (node.getName().equals(n.getName())){
						return;
					}
					if (mustBeWellFormed)
						throw new FHIRFormatError("Malformed XHTML: Found \"</" + n.getName() + ">\" expecting \"</" + node.getName() + ">\"" + descLoc());
					for (int i = parents.size() - 1; i >= 0; i--) {
						if (parents.get(i).getName().equals(n))
							unwindPoint = parents.get(i);
					}
					if (unwindPoint != null) {
						for (int i = parents.size(); i > 0; i--) {
							if (i < parents.size() && parents.get(i) == unwindPoint)
								return;
							if (i == parents.size()) {
								parents.get(i - 1).getChildNodes().addAll(node.getChildNodes());
								node.getChildNodes().clear();
							} else {
								parents.get(i - 1).getChildNodes().addAll(parents.get(i).getChildNodes());
								parents.get(i).getChildNodes().clear();
							}
						}
					}
				} else if (Character.isLetterOrDigit(peekChar())) {
					parseElement(node, parents, nsm);
				} else
					throw new FHIRFormatError("Unable to Parse HTML - node '" + node.getName() + "' has unexpected content '" + peekChar() + "' (last text = '" + lastText + "'" + descLoc());
			} else if (peekChar() == '&') {
				parseLiteral(s);
			} else
				s.append(readChar());
		}
		addTextNode(node, s);
	}

	private XhtmlNode parseFragment() throws IOException, FHIRException {
		skipWhiteSpace();
		if (peekChar() != '<')
			throw new FHIRException("Unable to Parse HTML - does not start with tag. Found " + peekChar() + descLoc());
		readChar();
		if (peekChar() == '?') {
			readToTagEnd();
			skipWhiteSpace();
			if (peekChar() != '<')
				throw new FHIRException("Unable to Parse HTML - does not start with tag after processing instruction. Found " + peekChar() + descLoc());
			readChar();
		}
		String n = readName().toLowerCase();
		readToTagEnd();
		XhtmlNode result = new XhtmlNode(NodeType.Element);

		int colonIndex = n.indexOf(':');
		if (colonIndex != -1) {
			n = n.substring(colonIndex + 1);
		}

		result.setName(n);
		unwindPoint = null;
		List<XhtmlNode> p = new ArrayList<XhtmlNode>();
		parseElementInner(result, p, null);

		return result;
	}

	public XhtmlNode parseFragment(InputStream input) throws IOException, FHIRException {
		rdr = new InputStreamReader(input);
		return parseFragment();
	}

	public XhtmlNode parseFragment(String source) throws IOException, FHIRException {
		rdr = new StringReader(source);
		return parseFragment();
	}

	public XhtmlNode parseHtmlNode(Element node) throws FHIRFormatError {
		return parseHtmlNode(node, null);
	}

	public XhtmlNode parseHtmlNode(Element node, String defaultNS) throws FHIRFormatError {
		XhtmlNode res = parseNode(node, defaultNS);
		if (res.getNsDecl() == null)
			res.getAttributes().put("xmlns", XHTML_NS);
		return res;
	}

	public XhtmlNode parseHtmlNode(XMLEventReader xpp) throws IOException, FHIRFormatError, XMLStreamException {
		XhtmlNode res = parseNode(xpp);
		if (res.getNsDecl() == null)
			res.getAttributes().put("xmlns", XHTML_NS);
		return res;

	}

	private void parseLiteral(StringBuilder s) throws IOException, FHIRFormatError {
		// UInt16 w;
		readChar();
		String c = readUntil(';');
		if (c.equals("apos"))
			s.append('\'');
		else if (c.equals("quot"))
			s.append('"');
		else if (c.equals("nbsp"))
			s.append(XhtmlNode.NBSP);
		else if (c.equals("amp"))
			s.append('&');
		else if (c.equals("rsquo"))
			s.append('’');
		else if (c.equals("gt"))
			s.append('>');
		else if (c.equals("lt"))
			s.append('<');
		else if (c.equals("copy"))
			s.append((char) 169);
		else if (c.equals("reg"))
			s.append((char) 174);
		else if (c.equals("sect"))
			s.append((char) 0xA7);
		else if (c.charAt(0) == '#') {
			if (isInteger(c.substring(1), 10))
				s.append((char) Integer.parseInt(c.substring(1)));
			else if (c.charAt(1) == 'x' && isInteger(c.substring(2), 16))
				s.append((char) Integer.parseInt(c.substring(2), 16));
		} else if (c.equals("fnof"))
			s.append((char) 402); // latin small f with hook = function = florin, U+0192 ISOtech -->
		else if (c.equals("Alpha"))
			s.append((char) 913); // greek capital letter alpha, U+0391
		else if (c.equals("Beta"))
			s.append((char) 914); // greek capital letter beta, U+0392
		else if (c.equals("Gamma"))
			s.append((char) 915); // greek capital letter gamma, U+0393 ISOgrk3
		else if (c.equals("Delta"))
			s.append((char) 916); // greek capital letter delta, U+0394 ISOgrk3
		else if (c.equals("Epsilon"))
			s.append((char) 917); // greek capital letter epsilon, U+0395
		else if (c.equals("Zeta"))
			s.append((char) 918); // greek capital letter zeta, U+0396
		else if (c.equals("Eta"))
			s.append((char) 919); // greek capital letter eta, U+0397
		else if (c.equals("Theta"))
			s.append((char) 920); // greek capital letter theta, U+0398 ISOgrk3
		else if (c.equals("Iota"))
			s.append((char) 921); // greek capital letter iota, U+0399
		else if (c.equals("Kappa"))
			s.append((char) 922); // greek capital letter kappa, U+039A
		else if (c.equals("Lambda"))
			s.append((char) 923); // greek capital letter lambda, U+039B ISOgrk3
		else if (c.equals("Mu"))
			s.append((char) 924); // greek capital letter mu, U+039C
		else if (c.equals("Nu"))
			s.append((char) 925); // greek capital letter nu, U+039D
		else if (c.equals("Xi"))
			s.append((char) 926); // greek capital letter xi, U+039E ISOgrk3
		else if (c.equals("Omicron"))
			s.append((char) 927); // greek capital letter omicron, U+039F
		else if (c.equals("Pi"))
			s.append((char) 928); // greek capital letter pi, U+03A0 ISOgrk3
		else if (c.equals("Rho"))
			s.append((char) 929); // greek capital letter rho, U+03A1
		else if (c.equals("Sigma"))
			s.append((char) 931); // greek capital letter sigma, U+03A3 ISOgrk3
		else if (c.equals("Tau"))
			s.append((char) 932); // greek capital letter tau, U+03A4
		else if (c.equals("Upsilon"))
			s.append((char) 933); // greek capital letter upsilon, U+03A5 ISOgrk3
		else if (c.equals("Phi"))
			s.append((char) 934); // greek capital letter phi, U+03A6 ISOgrk3
		else if (c.equals("Chi"))
			s.append((char) 935); // greek capital letter chi, U+03A7
		else if (c.equals("Psi"))
			s.append((char) 936); // greek capital letter psi, U+03A8 ISOgrk3
		else if (c.equals("Omega"))
			s.append((char) 937); // greek capital letter omega, U+03A9 ISOgrk3
		else if (c.equals("alpha"))
			s.append((char) 945); // greek small letter alpha, U+03B1 ISOgrk3
		else if (c.equals("beta"))
			s.append((char) 946); // greek small letter beta, U+03B2 ISOgrk3
		else if (c.equals("gamma"))
			s.append((char) 947); // greek small letter gamma, U+03B3 ISOgrk3
		else if (c.equals("delta"))
			s.append((char) 948); // greek small letter delta, U+03B4 ISOgrk3
		else if (c.equals("epsilon"))
			s.append((char) 949); // greek small letter epsilon, U+03B5 ISOgrk3
		else if (c.equals("zeta"))
			s.append((char) 950); // greek small letter zeta, U+03B6 ISOgrk3
		else if (c.equals("eta"))
			s.append((char) 951); // greek small letter eta, U+03B7 ISOgrk3
		else if (c.equals("theta"))
			s.append((char) 952); // greek small letter theta, U+03B8 ISOgrk3
		else if (c.equals("iota"))
			s.append((char) 953); // greek small letter iota, U+03B9 ISOgrk3
		else if (c.equals("kappa"))
			s.append((char) 954); // greek small letter kappa, U+03BA ISOgrk3
		else if (c.equals("lambda"))
			s.append((char) 955); // greek small letter lambda, U+03BB ISOgrk3
		else if (c.equals("mu"))
			s.append((char) 956); // greek small letter mu, U+03BC ISOgrk3
		else if (c.equals("nu"))
			s.append((char) 957); // greek small letter nu, U+03BD ISOgrk3
		else if (c.equals("xi"))
			s.append((char) 958); // greek small letter xi, U+03BE ISOgrk3
		else if (c.equals("omicron"))
			s.append((char) 959); // greek small letter omicron, U+03BF NEW
		else if (c.equals("pi"))
			s.append((char) 960); // greek small letter pi, U+03C0 ISOgrk3
		else if (c.equals("rho"))
			s.append((char) 961); // greek small letter rho, U+03C1 ISOgrk3
		else if (c.equals("sigmaf"))
			s.append((char) 962); // greek small letter final sigma, U+03C2 ISOgrk3
		else if (c.equals("sigma"))
			s.append((char) 963); // greek small letter sigma, U+03C3 ISOgrk3
		else if (c.equals("tau"))
			s.append((char) 964); // greek small letter tau, U+03C4 ISOgrk3
		else if (c.equals("upsilon"))
			s.append((char) 965); // greek small letter upsilon, U+03C5 ISOgrk3
		else if (c.equals("phi"))
			s.append((char) 966); // greek small letter phi, U+03C6 ISOgrk3
		else if (c.equals("chi"))
			s.append((char) 967); // greek small letter chi, U+03C7 ISOgrk3
		else if (c.equals("psi"))
			s.append((char) 968); // greek small letter psi, U+03C8 ISOgrk3
		else if (c.equals("omega"))
			s.append((char) 969); // greek small letter omega, U+03C9 ISOgrk3
		else if (c.equals("thetasym"))
			s.append((char) 977); // greek small letter theta symbol, U+03D1 NEW
		else if (c.equals("upsih"))
			s.append((char) 978); // greek upsilon with hook symbol, U+03D2 NEW
		else if (c.equals("piv"))
			s.append((char) 982); // greek pi symbol, U+03D6 ISOgrk3
		else if (c.equals("bull"))
			s.append((char) 8226); // bullet = black small circle, U+2022 ISOpub
		else if (c.equals("hellip"))
			s.append((char) 8230); // horizontal ellipsis = three dot leader, U+2026 ISOpub
		else if (c.equals("prime"))
			s.append((char) 8242); // prime = minutes = feet, U+2032 ISOtech
		else if (c.equals("Prime"))
			s.append((char) 8243); // double prime = seconds = inches, U+2033 ISOtech
		else if (c.equals("oline"))
			s.append((char) 8254); // overline = spacing overscore, U+203E NEW
		else if (c.equals("frasl"))
			s.append((char) 8260); // fraction slash, U+2044 NEW
		else if (c.equals("weierp"))
			s.append((char) 8472); // script capital P = power set = Weierstrass p, U+2118 ISOamso
		else if (c.equals("image"))
			s.append((char) 8465); // blackletter capital I = imaginary part, U+2111 ISOamso
		else if (c.equals("real"))
			s.append((char) 8476); // blackletter capital R = real part symbol, U+211C ISOamso
		else if (c.equals("trade"))
			s.append((char) 8482); // trade mark sign, U+2122 ISOnum
		else if (c.equals("alefsym"))
			s.append((char) 8501); // alef symbol = first transfinite cardinal, U+2135 NEW
		else if (c.equals("larr"))
			s.append((char) 8592); // leftwards arrow, U+2190 ISOnum
		else if (c.equals("uarr"))
			s.append((char) 8593); // upwards arrow, U+2191 ISOnum
		else if (c.equals("rarr"))
			s.append((char) 8594); // rightwards arrow, U+2192 ISOnum
		else if (c.equals("darr"))
			s.append((char) 8595); // downwards arrow, U+2193 ISOnum
		else if (c.equals("harr"))
			s.append((char) 8596); // left right arrow, U+2194 ISOamsa
		else if (c.equals("crarr"))
			s.append((char) 8629); // downwards arrow with corner leftwards = carriage return, U+21B5 NEW
		else if (c.equals("lArr"))
			s.append((char) 8656); // leftwards double arrow, U+21D0 ISOtech
		else if (c.equals("uArr"))
			s.append((char) 8657); // upwards double arrow, U+21D1 ISOamsa
		else if (c.equals("rArr"))
			s.append((char) 8658); // rightwards double arrow, U+21D2 ISOtech
		else if (c.equals("dArr"))
			s.append((char) 8659); // downwards double arrow, U+21D3 ISOamsa
		else if (c.equals("hArr"))
			s.append((char) 8660); // left right double arrow, U+21D4 ISOamsa
		else if (c.equals("forall"))
			s.append((char) 8704); // for all, U+2200 ISOtech
		else if (c.equals("part"))
			s.append((char) 8706); // partial differential, U+2202 ISOtech
		else if (c.equals("exist"))
			s.append((char) 8707); // there exists, U+2203 ISOtech
		else if (c.equals("empty"))
			s.append((char) 8709); // empty set = null set = diameter, U+2205 ISOamso
		else if (c.equals("nabla"))
			s.append((char) 8711); // nabla = backward difference, U+2207 ISOtech
		else if (c.equals("isin"))
			s.append((char) 8712); // element of, U+2208 ISOtech
		else if (c.equals("notin"))
			s.append((char) 8713); // not an element of, U+2209 ISOtech
		else if (c.equals("ni"))
			s.append((char) 8715); // contains as member, U+220B ISOtech
		else if (c.equals("prod"))
			s.append((char) 8719); // n-ary product = product sign, U+220F ISOamsb
		else if (c.equals("sum"))
			s.append((char) 8721); // n-ary sumation, U+2211 ISOamsb
		else if (c.equals("minus"))
			s.append((char) 8722); // minus sign, U+2212 ISOtech
		else if (c.equals("lowast"))
			s.append((char) 8727); // asterisk operator, U+2217 ISOtech
		else if (c.equals("radic"))
			s.append((char) 8730); // square root = radical sign, U+221A ISOtech
		else if (c.equals("prop"))
			s.append((char) 8733); // proportional to, U+221D ISOtech
		else if (c.equals("infin"))
			s.append((char) 8734); // infinity, U+221E ISOtech -->
		else if (c.equals("ang"))
			s.append((char) 8736); // angle, U+2220 ISOamso
		else if (c.equals("and"))
			s.append((char) 8743); // logical and = wedge, U+2227 ISOtech
		else if (c.equals("or"))
			s.append((char) 8744); // logical or = vee, U+2228 ISOtech
		else if (c.equals("cap"))
			s.append((char) 8745); // intersection = cap, U+2229 ISOtech
		else if (c.equals("cup"))
			s.append((char) 8746); // union = cup, U+222A ISOtech
		else if (c.equals("int"))
			s.append((char) 8747); // integral, U+222B ISOtech
		else if (c.equals("there4"))
			s.append((char) 8756); // therefore, U+2234 ISOtech
		else if (c.equals("sim"))
			s.append((char) 8764); // tilde operator = varies with = similar t U+223C ISOtech
		else if (c.equals("cong"))
			s.append((char) 8773); // approximately equal to, U+2245 ISOtec
		else if (c.equals("asymp"))
			s.append((char) 8776); // almost equal to = asymptotic to, U+2248 ISOamsr
		else if (c.equals("ne"))
			s.append((char) 8800); // not equal to, U+2260 ISOtech
		else if (c.equals("equiv"))
			s.append((char) 8801); // identical to, U+2261 ISOtech
		else if (c.equals("le"))
			s.append((char) 8804); // less-than or equal to, U+2264 ISOtech
		else if (c.equals("ge"))
			s.append((char) 8805); // greater-than or equal to, U+2265 ISOtech
		else if (c.equals("sub"))
			s.append((char) 8834); // subset of, U+2282 ISOtech
		else if (c.equals("sup"))
			s.append((char) 8835); // superset of, U+2283 ISOtech
		else if (c.equals("nsub"))
			s.append((char) 8836); // not a subset of, U+2284 ISOamsn
		else if (c.equals("sube"))
			s.append((char) 8838); // subset of or equal to, U+2286 ISOtech
		else if (c.equals("supe"))
			s.append((char) 8839); // superset of or equal to, U+2287 ISOtech
		else if (c.equals("oplus"))
			s.append((char) 8853); // circled plus = direct sum, U+2295 ISOamsb
		else if (c.equals("otimes"))
			s.append((char) 8855); // circled times = vector product, U+2297 ISOamsb -->
		else if (c.equals("perp"))
			s.append((char) 8869); // up tack = orthogonal to = perpendicular, U+22A5 ISOtech
		else if (c.equals("sdot"))
			s.append((char) 8901); // dot operator, U+22C5 ISOamsb
		else if (c.equals("lceil"))
			s.append((char) 8968); // left ceiling = apl upstile, U+2308 ISOamsc
		else if (c.equals("rceil"))
			s.append((char) 8969); // right ceiling, U+2309 ISOamsc
		else if (c.equals("lfloor"))
			s.append((char) 8970); // left floor = apl downstile, U+230A ISOamsc
		else if (c.equals("rfloor"))
			s.append((char) 8971); // right floor, U+230B ISOamsc
		else if (c.equals("lang"))
			s.append((char) 9001); // left-pointing angle bracket = bra, U+2329 ISOtech
		else if (c.equals("rang"))
			s.append((char) 9002); // right-pointing angle bracket = ket, U+232A ISOtech
		else if (c.equals("loz"))
			s.append((char) 9674); // lozenge, U+25CA ISOpub
		else if (c.equals("spades"))
			s.append((char) 9824); // black spade suit, U+2660 ISOpub
		else if (c.equals("clubs"))
			s.append((char) 9827); // black club suit = shamrock, U+2663 ISOpub
		else if (c.equals("hearts"))
			s.append((char) 9829); // black heart suit = valentine, U+2665 ISOpub
		else if (c.equals("diams"))
			s.append((char) 9830); // black diamond suit, U+2666 ISOpub --
		else
			throw new FHIRFormatError("unable to parse character reference '" + c + "'' (last text = '" + lastText + "'" + descLoc());
	}

	private XhtmlNode parseNode(Element node, String defaultNS) throws FHIRFormatError {
		XhtmlNode res = new XhtmlNode(NodeType.Element);
		res.setName(node.getLocalName());
		defaultNS = checkNS(res, node, defaultNS);
		for (int i = 0; i < node.getAttributes().getLength(); i++) {
			Attr attr = (Attr) node.getAttributes().item(i);
			if (attributeIsOk(res.getName(), attr.getName(), attr.getValue()) && !attr.getLocalName().startsWith("xmlns"))
				res.getAttributes().put(attr.getName(), attr.getValue());
		}
		Node child = node.getFirstChild();
		while (child != null) {
			if (child.getNodeType() == Node.TEXT_NODE) {
				res.addText(child.getTextContent());
			} else if (child.getNodeType() == Node.COMMENT_NODE) {
				res.addComment(child.getTextContent());
			} else if (child.getNodeType() == Node.ELEMENT_NODE) {
				if (elementIsOk(child.getLocalName()))
					res.getChildNodes().add(parseNode((Element) child, defaultNS));
			} else
				throw new FHIRFormatError("Unhandled XHTML feature: " + Integer.toString(child.getNodeType()) + descLoc());
			child = child.getNextSibling();
		}
		return res;
	}

	private XhtmlNode parseNode(XMLEventReader xpp) throws IOException, FHIRFormatError, XMLStreamException {
		XhtmlNode res = new XhtmlNode(NodeType.Element);

		if (!xpp.hasNext()) {
			return res;
		}

		StartElement firstEvent = (StartElement) xpp.nextEvent();
		res.setName(firstEvent.getSchemaType().getLocalPart());

		for (Iterator<?> attrIter = firstEvent.getAttributes(); attrIter.hasNext();) {
			Attribute nextAttr = (Attribute) attrIter.next();
			if (attributeIsOk(firstEvent.getName().getLocalPart(), nextAttr.getName().getLocalPart(), nextAttr.getValue()))
				res.getAttributes().put(nextAttr.getName().getLocalPart(), nextAttr.getValue());
		}

		while (xpp.hasNext()) {
			XMLEvent nextEvent = xpp.nextEvent();
			int eventType = nextEvent.getEventType();
			if (eventType != XMLEvent.END_ELEMENT) {
				break;
			}
			if (eventType == XMLEvent.CHARACTERS) {
				res.addText(((Characters) xpp).getData());
			} else if (eventType == XMLEvent.COMMENT) {
				res.addComment(((Comment) xpp).getText());
			} else if (eventType == XMLEvent.START_ELEMENT) {
				StartElement nextStart = (StartElement) nextEvent;
				if (elementIsOk(nextStart.getName().getLocalPart())) {
					res.getChildNodes().add(parseNode(xpp));
				}
			} else {
				throw new FHIRFormatError("Unhandled XHTML feature: " + Integer.toString(eventType) + descLoc());
			}
		}
		xpp.next();
		return res;
	}

	private char peekChar() throws IOException {
		if (cache.length() > 0)
			return cache.charAt(0);
		else if (!rdr.ready())
			return '\0';
		else {
			char c = (char) rdr.read();
			if (c == (char) -1) {
				cache = "";
				return '\0';
			}
			cache = Character.toString(c);
			return c;
		}
	}

	private void pushChar(char ch) {
		cache = Character.toString(ch) + cache;
	}

	private char readChar() throws IOException {
		char c;
		if (cache.length() > 0) {
			c = cache.charAt(0);
			cache = cache.length() == 1 ? "" : cache.substring(1);
		} else if (!rdr.ready())
			c = '\0';
		else
			c = (char) rdr.read();
		if (c == '\r' || c == '\n') {
			if (c == '\r' || lastChar != '\r') {
				line++;
				col = 0;
			}
			lastChar = c;
		}
		col++;
		return c;
	}

	private String readName() throws IOException {
		StringBuilder s = new StringBuilder();
		while (isNameChar(peekChar()))
			s.append(readChar());
		return s.toString();
	}

	private String readToCommentEnd() throws IOException, FHIRFormatError {
		if (peekChar() == '!')
			readChar();
		StringBuilder s = new StringBuilder();

		boolean simple = true;
		if (peekChar() == '-') {
			readChar();
			simple = peekChar() != '-';
			if (simple)
				s.append('-');
			else
				readChar();
		}

		boolean done = false;
		while (!done) {
			char c = peekChar();
			if (c == '-') {
				readChar();
				if (peekChar() == '-') {
					readChar();
					if (peekChar() == '>') {
						done = true;
					} else
						s.append("--");
				} else
					s.append('-');
			} else if (simple && peekChar() == '>') {
				done = true;
			} else if (c != '\0')
				s.append(readChar());
			else if (mustBeWellFormed)
				throw new FHIRFormatError("Unexpected termination of html source" + descLoc());
		}
		if (peekChar() != '\0') {
			readChar();
			skipWhiteSpace();
		}
		return s.toString();
	}

	private String readToTagEnd() throws IOException, FHIRFormatError {
		StringBuilder s = new StringBuilder();
		while (peekChar() != '>' && peekChar() != '\0')
			s.append(readChar());
		if (peekChar() != '\0') {
			readChar();
			skipWhiteSpace();
		} else if (mustBeWellFormed)
			throw new FHIRFormatError("Unexpected termination of html source" + descLoc());
		return s.toString();
	}

	private String readUntil(char ch) throws IOException {
		StringBuilder s = new StringBuilder();
		while (peekChar() != 0 && peekChar() != ch)
			s.append(readChar());
		readChar();
		return s.toString();
	}

	public void setMustBeWellFormed(boolean mustBeWellFormed) {
		this.mustBeWellFormed = mustBeWellFormed;
	}

	public void setPolicy(ParserSecurityPolicy policy) {
		this.policy = policy;
	}

	public void setTrimWhitespace(boolean trimWhitespace) {
		this.trimWhitespace = trimWhitespace;
	}

	public XhtmlParser setValidatorMode(boolean validatorMode) {
		this.validatorMode = validatorMode;
		return this;
	}

	private void skipWhiteSpace() throws IOException {
		if (trimWhitespace)
			while (Character.isWhitespace(peekChar()) || (peekChar() == 0xfeff))
				readChar();
	}

	private void skipWhiteSpaceAndComments(XhtmlNode focus) throws IOException, FHIRFormatError {
		while (Character.isWhitespace(peekChar()) || (peekChar() == 0xfeff))
			readChar();
		if (peekChar() == '<') {
			char ch = readChar();
			if (peekChar() == '!') {
				readChar();
				if (peekChar() == '-') {
					readChar();
					if (peekChar() == '-') {
						readChar();
						if (peekChar() == ' ')
							readChar();
						focus.addComment(readToCommentEnd());
					} else
						throw new FHIRFormatError("unrecognised element type <!" + peekChar() + descLoc());
				} else
					focus.addDocType(readToCommentEnd());
				skipWhiteSpaceAndComments(focus);
			} else if (peekChar() == '?') {
				String r = readToTagEnd();
				focus.addInstruction(r.substring(1, r.length() - 1));
				skipWhiteSpaceAndComments(focus);
			} else
				pushChar(ch);
		}
	}

	public class NSMap {
		private Map<String, String> nslist = new HashMap<String, String>();

		public NSMap(NSMap nsm) {
			if (nsm != null)
				nslist.putAll(nsm.nslist);
		}

		public String def() {
			return nslist.get("");
		}

		public void def(String ns) {
			nslist.put("", ns);
		}

		public String get(String abbrev) {
			return nslist.containsKey(abbrev) ? nslist.get(abbrev) : "http://error/undefined-namespace";
		}

		public boolean hasDef() {
			return nslist.containsKey("");
		}

		public void ns(String abbrev, String ns) {
			nslist.put(abbrev, ns);
		}
	}

	public enum ParserSecurityPolicy {
		Accept, Drop, Reject
	}

	public class QName {
		private String name;
		private String ns;

		public QName(String src) {
			if (src.contains(":")) {
				ns = src.substring(0, src.indexOf(":"));
				name = src.substring(src.indexOf(":") + 1);
			} else {
				ns = null;
				name = src;
			}
		}

		public String getName() {
			return name;
		}

		public String getNs() {
			return ns;
		}

		public boolean hasNs() {
			return ns != null;
		}

	}

}
