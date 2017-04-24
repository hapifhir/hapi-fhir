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
package org.hl7.fhir.utilities.xml;

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
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import java.io.*;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.utilities.Utilities;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;

public class XMLUtil {

	public static final String SPACE_CHAR = "\u00A0";

  public static boolean isNMToken(String name) {
		if (name == null)
			return false;
		for (int i = 0; i < name.length(); i++) 
			if (!isNMTokenChar(name.charAt(i)))
				return false;	
		return name.length() > 0;
	}

	public static boolean isNMTokenChar(char c) {
		return isLetter(c) || isDigit(c) || c == '.' || c == '-' || c == '_' || c == ':' || isCombiningChar(c) || isExtender(c);
	}

	private static boolean isDigit(char c) {
		return (c >= '\u0030' && c <= '\u0039') || (c >= '\u0660' && c <= '\u0669') || (c >= '\u06F0' && c <= '\u06F9') || 
			(c >= '\u0966' && c <= '\u096F') || (c >= '\u09E6' && c <= '\u09EF') || (c >= '\u0A66' && c <= '\u0A6F') || 
			(c >= '\u0AE6' && c <= '\u0AEF') || (c >= '\u0B66' && c <= '\u0B6F') || (c >= '\u0BE7' && c <= '\u0BEF') || 
			(c >= '\u0C66' && c <= '\u0C6F') || (c >= '\u0CE6' && c <= '\u0CEF') || (c >= '\u0D66' && c <= '\u0D6F') || 
			(c >= '\u0E50' && c <= '\u0E59') || (c >= '\u0ED0' && c <= '\u0ED9') || (c >= '\u0F20' && c <= '\u0F29');
	}

	private static boolean isCombiningChar(char c) {
		return (c >= '\u0300' && c <= '\u0345') || (c >= '\u0360' && c <= '\u0361') || (c >= '\u0483' && c <= '\u0486') || 
			(c >= '\u0591' && c <= '\u05A1') || (c >= '\u05A3' && c <= '\u05B9') || (c >= '\u05BB' && c <= '\u05BD') || 
			c == '\u05BF' || (c >= '\u05C1' && c <= '\u05C2') || c == '\u05C4' || (c >= '\u064B' && c <= '\u0652') || 
			c == '\u0670' || (c >= '\u06D6' && c <= '\u06DC') || (c >= '\u06DD' && c <= '\u06DF') || (c >= '\u06E0' && c <= '\u06E4') || 
			(c >= '\u06E7' && c <= '\u06E8') || (c >= '\u06EA' && c <= '\u06ED') || (c >= '\u0901' && c <= '\u0903') || c == '\u093C' || 
			(c >= '\u093E' && c <= '\u094C') || c == '\u094D' || (c >= '\u0951' && c <= '\u0954') || (c >= '\u0962' && c <= '\u0963') || 
			(c >= '\u0981' && c <= '\u0983') || c == '\u09BC' || c == '\u09BE' || c == '\u09BF' || (c >= '\u09C0' && c <= '\u09C4') || 
			(c >= '\u09C7' && c <= '\u09C8') || (c >= '\u09CB' && c <= '\u09CD') || c == '\u09D7' || (c >= '\u09E2' && c <= '\u09E3') || 
			c == '\u0A02' || c == '\u0A3C' || c == '\u0A3E' || c == '\u0A3F' || (c >= '\u0A40' && c <= '\u0A42') || 
			(c >= '\u0A47' && c <= '\u0A48') || (c >= '\u0A4B' && c <= '\u0A4D') || (c >= '\u0A70' && c <= '\u0A71') || 
			(c >= '\u0A81' && c <= '\u0A83') || c == '\u0ABC' || (c >= '\u0ABE' && c <= '\u0AC5') || (c >= '\u0AC7' && c <= '\u0AC9') || 
			(c >= '\u0ACB' && c <= '\u0ACD') || (c >= '\u0B01' && c <= '\u0B03') || c == '\u0B3C' || (c >= '\u0B3E' && c <= '\u0B43') || 
			(c >= '\u0B47' && c <= '\u0B48') || (c >= '\u0B4B' && c <= '\u0B4D') || (c >= '\u0B56' && c <= '\u0B57') || 
			(c >= '\u0B82' && c <= '\u0B83') || (c >= '\u0BBE' && c <= '\u0BC2') || (c >= '\u0BC6' && c <= '\u0BC8') || 
			(c >= '\u0BCA' && c <= '\u0BCD') || c == '\u0BD7' || (c >= '\u0C01' && c <= '\u0C03') || (c >= '\u0C3E' && c <= '\u0C44') || 
			(c >= '\u0C46' && c <= '\u0C48') || (c >= '\u0C4A' && c <= '\u0C4D') || (c >= '\u0C55' && c <= '\u0C56') || 
			(c >= '\u0C82' && c <= '\u0C83') || (c >= '\u0CBE' && c <= '\u0CC4') || (c >= '\u0CC6' && c <= '\u0CC8') || 
			(c >= '\u0CCA' && c <= '\u0CCD') || (c >= '\u0CD5' && c <= '\u0CD6') || (c >= '\u0D02' && c <= '\u0D03') || 
			(c >= '\u0D3E' && c <= '\u0D43') || (c >= '\u0D46' && c <= '\u0D48') || (c >= '\u0D4A' && c <= '\u0D4D') || c == '\u0D57' || 
			c == '\u0E31' || (c >= '\u0E34' && c <= '\u0E3A') || (c >= '\u0E47' && c <= '\u0E4E') || c == '\u0EB1' || 
			(c >= '\u0EB4' && c <= '\u0EB9') || (c >= '\u0EBB' && c <= '\u0EBC') || (c >= '\u0EC8' && c <= '\u0ECD') || 
			(c >= '\u0F18' && c <= '\u0F19') || c == '\u0F35' || c == '\u0F37' || c == '\u0F39' || c == '\u0F3E' || c == '\u0F3F' || 
			(c >= '\u0F71' && c <= '\u0F84') || (c >= '\u0F86' && c <= '\u0F8B') || (c >= '\u0F90' && c <= '\u0F95') || c == '\u0F97' || 
			(c >= '\u0F99' && c <= '\u0FAD') || (c >= '\u0FB1' && c <= '\u0FB7') || c == '\u0FB9' || (c >= '\u20D0' && c <= '\u20DC') ||
			c == '\u20E1' || (c >= '\u302A' && c <= '\u302F') || c == '\u3099' || c == '\u309A';
	}

	private static boolean isExtender(char c) {
		return c == '\u00B7' || c == '\u02D0' || c == '\u02D1' || c == '\u0387' || c == '\u0640' || c == '\u0E46' || 
			c == '\u0EC6' || c == '\u3005' || (c >= '\u3031' && c <= '\u3035') || (c >= '\u309D' && c <= '\u309E') || 
			(c >= '\u30FC' && c <= '\u30FE');
	}

	private static boolean isLetter(char c) {
		return isBaseChar(c) || isIdeographic(c);
	}

	private static boolean isBaseChar(char c) {
		return (c >= '\u0041' && c <= '\u005A') || (c >= '\u0061' && c <= '\u007A') || (c >= '\u00C0' && c <= '\u00D6') || 
			(c >= '\u00D8' && c <= '\u00F6') || (c >= '\u00F8' && c <= '\u00FF') || (c >= '\u0100' && c <= '\u0131') || 
			(c >= '\u0134' && c <= '\u013E') || (c >= '\u0141' && c <= '\u0148') || (c >= '\u014A' && c <= '\u017E') || 
			(c >= '\u0180' && c <= '\u01C3') || (c >= '\u01CD' && c <= '\u01F0') || (c >= '\u01F4' && c <= '\u01F5') || 
			(c >= '\u01FA' && c <= '\u0217') || (c >= '\u0250' && c <= '\u02A8') || (c >= '\u02BB' && c <= '\u02C1') || 
			c == '\u0386' || (c >= '\u0388' && c <= '\u038A') || c == '\u038C' || (c >= '\u038E' && c <= '\u03A1') || 
			(c >= '\u03A3' && c <= '\u03CE') || (c >= '\u03D0' && c <= '\u03D6') || c == '\u03DA' || c == '\u03DC' || c == '\u03DE' || 
			c == '\u03E0' || (c >= '\u03E2' && c <= '\u03F3') || (c >= '\u0401' && c <= '\u040C') || (c >= '\u040E' && c <= '\u044F') || 
			(c >= '\u0451' && c <= '\u045C') || (c >= '\u045E' && c <= '\u0481') || (c >= '\u0490' && c <= '\u04C4') || 
			(c >= '\u04C7' && c <= '\u04C8') || (c >= '\u04CB' && c <= '\u04CC') || (c >= '\u04D0' && c <= '\u04EB') || 
			(c >= '\u04EE' && c <= '\u04F5') || (c >= '\u04F8' && c <= '\u04F9') || (c >= '\u0531' && c <= '\u0556') || 
			c == '\u0559' || (c >= '\u0561' && c <= '\u0586') || (c >= '\u05D0' && c <= '\u05EA') || (c >= '\u05F0' && c <= '\u05F2') || 
			(c >= '\u0621' && c <= '\u063A') || (c >= '\u0641' && c <= '\u064A') || (c >= '\u0671' && c <= '\u06B7') || 
			(c >= '\u06BA' && c <= '\u06BE') || (c >= '\u06C0' && c <= '\u06CE') || (c >= '\u06D0' && c <= '\u06D3') || 
			c == '\u06D5' || (c >= '\u06E5' && c <= '\u06E6') || (c >= '\u0905' && c <= '\u0939') || c == '\u093D' || 
			(c >= '\u0958' && c <= '\u0961') || (c >= '\u0985' && c <= '\u098C') || (c >= '\u098F' && c <= '\u0990') || 
			(c >= '\u0993' && c <= '\u09A8') || (c >= '\u09AA' && c <= '\u09B0') || c == '\u09B2' || 
			(c >= '\u09B6' && c <= '\u09B9') || (c >= '\u09DC' && c <= '\u09DD') || (c >= '\u09DF' && c <= '\u09E1') || 
			(c >= '\u09F0' && c <= '\u09F1') || (c >= '\u0A05' && c <= '\u0A0A') || (c >= '\u0A0F' && c <= '\u0A10') || 
			(c >= '\u0A13' && c <= '\u0A28') || (c >= '\u0A2A' && c <= '\u0A30') || (c >= '\u0A32' && c <= '\u0A33') || 
			(c >= '\u0A35' && c <= '\u0A36') || (c >= '\u0A38' && c <= '\u0A39') || (c >= '\u0A59' && c <= '\u0A5C') || 
			c == '\u0A5E' || (c >= '\u0A72' && c <= '\u0A74') || (c >= '\u0A85' && c <= '\u0A8B') || c == '\u0A8D' || 
			(c >= '\u0A8F' && c <= '\u0A91') || (c >= '\u0A93' && c <= '\u0AA8') || (c >= '\u0AAA' && c <= '\u0AB0') || 
			(c >= '\u0AB2' && c <= '\u0AB3') || (c >= '\u0AB5' && c <= '\u0AB9') || c == '\u0ABD' || c == '\u0AE0' || 
			(c >= '\u0B05' && c <= '\u0B0C') || (c >= '\u0B0F' && c <= '\u0B10') || (c >= '\u0B13' && c <= '\u0B28') || 
			(c >= '\u0B2A' && c <= '\u0B30') || (c >= '\u0B32' && c <= '\u0B33') || (c >= '\u0B36' && c <= '\u0B39') || 
			c == '\u0B3D' || (c >= '\u0B5C' && c <= '\u0B5D') || (c >= '\u0B5F' && c <= '\u0B61') || 
			(c >= '\u0B85' && c <= '\u0B8A') || (c >= '\u0B8E' && c <= '\u0B90') || (c >= '\u0B92' && c <= '\u0B95') || 
			(c >= '\u0B99' && c <= '\u0B9A') || c == '\u0B9C' || (c >= '\u0B9E' && c <= '\u0B9F') || 
			(c >= '\u0BA3' && c <= '\u0BA4') || (c >= '\u0BA8' && c <= '\u0BAA') || (c >= '\u0BAE' && c <= '\u0BB5') || 
			(c >= '\u0BB7' && c <= '\u0BB9') || (c >= '\u0C05' && c <= '\u0C0C') || (c >= '\u0C0E' && c <= '\u0C10') || 
			(c >= '\u0C12' && c <= '\u0C28') || (c >= '\u0C2A' && c <= '\u0C33') || (c >= '\u0C35' && c <= '\u0C39') || 
			(c >= '\u0C60' && c <= '\u0C61') || (c >= '\u0C85' && c <= '\u0C8C') || (c >= '\u0C8E' && c <= '\u0C90') || 
			(c >= '\u0C92' && c <= '\u0CA8') || (c >= '\u0CAA' && c <= '\u0CB3') || (c >= '\u0CB5' && c <= '\u0CB9') || 
			c == '\u0CDE' || (c >= '\u0CE0' && c <= '\u0CE1') || (c >= '\u0D05' && c <= '\u0D0C') || 
			(c >= '\u0D0E' && c <= '\u0D10') || (c >= '\u0D12' && c <= '\u0D28') || (c >= '\u0D2A' && c <= '\u0D39') || 
			(c >= '\u0D60' && c <= '\u0D61') || (c >= '\u0E01' && c <= '\u0E2E') || c == '\u0E30' || 
			(c >= '\u0E32' && c <= '\u0E33') || (c >= '\u0E40' && c <= '\u0E45') || (c >= '\u0E81' && c <= '\u0E82') || 
			c == '\u0E84' || (c >= '\u0E87' && c <= '\u0E88') || c == '\u0E8A' || c == '\u0E8D' || (c >= '\u0E94' && c <= '\u0E97') || 
			(c >= '\u0E99' && c <= '\u0E9F') || (c >= '\u0EA1' && c <= '\u0EA3') || c == '\u0EA5' || c == '\u0EA7' || 
			(c >= '\u0EAA' && c <= '\u0EAB') || (c >= '\u0EAD' && c <= '\u0EAE') || c == '\u0EB0' || 
			(c >= '\u0EB2' && c <= '\u0EB3') || c == '\u0EBD' || (c >= '\u0EC0' && c <= '\u0EC4') || 
			(c >= '\u0F40' && c <= '\u0F47') || (c >= '\u0F49' && c <= '\u0F69') || (c >= '\u10A0' && c <= '\u10C5') || 
			(c >= '\u10D0' && c <= '\u10F6') || c == '\u1100' || (c >= '\u1102' && c <= '\u1103') || 
			(c >= '\u1105' && c <= '\u1107') || c == '\u1109' || (c >= '\u110B' && c <= '\u110C') || 
			(c >= '\u110E' && c <= '\u1112') || c == '\u113C' || c == '\u113E' || c == '\u1140' || c == '\u114C' || 
			c == '\u114E' || c == '\u1150' || (c >= '\u1154' && c <= '\u1155') || c == '\u1159' || 
			(c >= '\u115F' && c <= '\u1161') || c == '\u1163' || c == '\u1165' || c == '\u1167' || c == '\u1169' || 
			(c >= '\u116D' && c <= '\u116E') || (c >= '\u1172' && c <= '\u1173') || c == '\u1175' || 
			c == '\u119E' || c == '\u11A8' || c == '\u11AB' || (c >= '\u11AE' && c <= '\u11AF') || 
			(c >= '\u11B7' && c <= '\u11B8') || c == '\u11BA' || (c >= '\u11BC' && c <= '\u11C2') || 
			c == '\u11EB' || c == '\u11F0' || c == '\u11F9' || (c >= '\u1E00' && c <= '\u1E9B') || (c >= '\u1EA0' && c <= '\u1EF9') || 
			(c >= '\u1F00' && c <= '\u1F15') || (c >= '\u1F18' && c <= '\u1F1D') || (c >= '\u1F20' && c <= '\u1F45') || 
			(c >= '\u1F48' && c <= '\u1F4D') || (c >= '\u1F50' && c <= '\u1F57') || c == '\u1F59' || c == '\u1F5B' || c == '\u1F5D' || 
			(c >= '\u1F5F' && c <= '\u1F7D') || (c >= '\u1F80' && c <= '\u1FB4') || (c >= '\u1FB6' && c <= '\u1FBC') || 
			c == '\u1FBE' || (c >= '\u1FC2' && c <= '\u1FC4') || (c >= '\u1FC6' && c <= '\u1FCC') || 
			(c >= '\u1FD0' && c <= '\u1FD3') || (c >= '\u1FD6' && c <= '\u1FDB') || (c >= '\u1FE0' && c <= '\u1FEC') || 
			(c >= '\u1FF2' && c <= '\u1FF4') || (c >= '\u1FF6' && c <= '\u1FFC') || c == '\u2126' || 
			(c >= '\u212A' && c <= '\u212B') || c == '\u212E' || (c >= '\u2180' && c <= '\u2182') || 
			(c >= '\u3041' && c <= '\u3094') || (c >= '\u30A1' && c <= '\u30FA') || (c >= '\u3105' && c <= '\u312C') || 
			(c >= '\uAC00' && c <= '\uD7A3');
	}

	private static boolean isIdeographic(char c) {
		return (c >= '\u4E00' && c <= '\u9FA5') || c == '\u3007' || (c >= '\u3021' && c <= '\u3029');
	}

	public static String determineEncoding(InputStream stream) throws IOException {
		stream.mark(20000);
		try {
			int b0 = stream.read();
			int b1 = stream.read();
			int b2 = stream.read();
			int b3 = stream.read();

			if (b0 == 0xFE && b1 == 0xFF)
				return "UTF-16BE";
			else if (b0 == 0xFF && b1 == 0xFE)
				return "UTF-16LE";
			else if (b0 == 0xEF && b1 == 0xBB && b2 == 0xBF )
				return "UTF-8";
			else if (b0 == 0x00 && b1 == 0x3C && b2 == 0x00 && b3 == 0x3F)
				return "UTF-16BE";
			else if (b0 == 0x3C && b1 == 0x00 && b2 == 0x3F && b3 == 0x00)
				return "UTF-16LE";
			else if (b0 == 0x3C && b1 == 0x3F && b2 == 0x78 && b3 == 0x6D) {
//				UTF-8, ISO 646, ASCII, some part of ISO 8859, Shift-JIS, EUC, or any other 7-bit, 8-bit, or mixed-width encoding 
//				which ensures that the characters of ASCII have their normal positions, width, and values; the actual encoding 
//				declaration must be read to detect which of these applies, but since all of these encodings use the same bit patterns 
//				for the relevant ASCII characters, the encoding declaration itself may be read reliably
				InputStreamReader rdr = new InputStreamReader(stream, "US-ASCII");
				String hdr = readFirstLine(rdr);
				return extractEncoding(hdr); 
			} else
				return null;
		} finally {
			stream.reset();
		}
	}

	private static String extractEncoding(String hdr) {
		int i = hdr.indexOf("encoding=");
		if (i == -1)
			return null;
		hdr = hdr.substring(i+9);
		char sep = hdr.charAt(0);
		hdr = hdr.substring(1);
		i = hdr.indexOf(sep);
		if (i == -1)
			return null;
		return hdr.substring(0, i);
	}

	private static String readFirstLine(InputStreamReader rdr) throws IOException {
		char[] buf = new char[1];
		StringBuffer bldr = new StringBuffer();
		rdr.read(buf);
		while (buf[0] != '>') {
			bldr.append(buf[0]);
			rdr.read(buf);
		}
		return bldr.toString();
	}

	
    public static boolean charSetImpliesAscii(String charset) {
		return charset.equals("ISO-8859-1") || charset.equals("US-ASCII");
	}

	
	/**
	 * Converts the raw characters to XML escape characters.
	 * 
	 * @param rawContent
	 * @param charset Null when charset is not known, so we assume it's unicode
	 * @param isNoLines
	 * @return escape string
	 */
	public static String escapeXML(String rawContent, String charset, boolean isNoLines) {
		if (rawContent == null){
			return "";
		}
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < rawContent.length(); i++) {
			char ch = rawContent.charAt(i);
			if (ch == '\'')
				sb.append("&#39;");
			else if (ch == '&')
				sb.append("&amp;");
			else if (ch == '"')
				sb.append("&quot;");
			else if (ch == '<')
				sb.append("&lt;");
			else if (ch == '>')
				sb.append("&gt;");
			else if (ch > '~' && charset != null && charSetImpliesAscii(charset)) 
				// TODO - why is hashcode the only way to get the unicode number for the character
				// in jre 5.0?
				sb.append("&#x"+Integer.toHexString(new Character(ch).hashCode()).toUpperCase()+";");
			else if (isNoLines) {
				if (ch == '\r')
					sb.append("&#xA;");
				else if (ch != '\n')
					sb.append(ch);
			}
			else
				sb.append(ch);
		}
		return sb.toString();
	}

  public static Element getFirstChild(Element e) {
    if (e == null)
      return null;
    Node n = e.getFirstChild();
    while (n != null && n.getNodeType() != Node.ELEMENT_NODE)
      n = n.getNextSibling();
    return (Element) n;
  }

  public static Element getNamedChild(Element e, String name) {
    Element c = getFirstChild(e);
    while (c != null && !name.equals(c.getLocalName()) && !name.equals(c.getNodeName()))
      c = getNextSibling(c);
    return c;
  }

  public static Element getNextSibling(Element e) {
    Node n = e.getNextSibling();
    while (n != null && n.getNodeType() != Node.ELEMENT_NODE)
      n = n.getNextSibling();
    return (Element) n;
  }

  public static void getNamedChildren(Element e, String name, List<Element> set) {
    Element c = getFirstChild(e);
    while (c != null) {
      if (name.equals(c.getLocalName()) || name.equals(c.getNodeName()) )
        set.add(c);
      c = getNextSibling(c);
    }
  }

  public static String htmlToXmlEscapedPlainText(Element r) {
    StringBuilder s = new StringBuilder();
    Node n = r.getFirstChild();
    boolean ws = false;
    while (n != null) {
      if (n.getNodeType() == Node.TEXT_NODE) {
        String t = n.getTextContent().trim();
        if (Utilities.noString(t))
          ws = true;
        else {
          if (ws)
            s.append(" ");
          ws = false;
          s.append(t);
        }
      }
      if (n.getNodeType() == Node.ELEMENT_NODE) {
        if (ws)
          s.append(" ");
        ws = false;
        s.append(htmlToXmlEscapedPlainText((Element) n));
        if (r.getNodeName().equals("br") || r.getNodeName().equals("p"))
          s.append("\r\n");
      }
      n = n.getNextSibling();      
    }
    return s.toString();
  }

  public static String htmlToXmlEscapedPlainText(String definition) throws ParserConfigurationException, SAXException, IOException  {
    return htmlToXmlEscapedPlainText(parseToDom("<div>"+definition+"</div>").getDocumentElement());
  }

  public static String elementToString(Element el) {
    if (el == null)
      return "";
    Document document = el.getOwnerDocument();
    DOMImplementationLS domImplLS = (DOMImplementationLS) document
        .getImplementation();
    LSSerializer serializer = domImplLS.createLSSerializer();
    return serializer.writeToString(el);
  }

  public static String getNamedChildValue(Element element, String name) {
    Element e = getNamedChild(element, name);
    return e == null ? null : e.getAttribute("value");
  }

  public static void setNamedChildValue(Element element, String name, String value) throws FHIRException  {
    Element e = getNamedChild(element, name);
    if (e == null)
      throw new FHIRException("unable to find element "+name);
    e.setAttribute("value", value);
  }


	public static void getNamedChildrenWithWildcard(Element focus, String name, List<Element> children) {
    Element c = getFirstChild(focus);
    while (c != null) {
    	String n = c.getLocalName() != null ? c.getLocalName() : c.getNodeName(); 
      if (name.equals(n) || (name.endsWith("[x]") && n.startsWith(name.substring(0, name.length()-3))))
        children.add(c);
      c = getNextSibling(c);
    }
  }

	public static void getNamedChildrenWithTails(Element focus, String name, List<Element> children, Set<String> typeTails) {
    Element c = getFirstChild(focus);
    while (c != null) {
      String n = c.getLocalName() != null ? c.getLocalName() : c.getNodeName(); 
      if (n.equals(name) || (!n.equals("responseCode") && (n.startsWith(name) && typeTails.contains(n.substring(name.length())))))
        children.add(c);
      c = getNextSibling(c);
    }
  }
	
  public static boolean hasNamedChild(Element e, String name) {
    Element c = getFirstChild(e);
    while (c != null && !name.equals(c.getLocalName()) && !name.equals(c.getNodeName()))
      c = getNextSibling(c);
    return c != null;
  }

  public static Document parseToDom(String content) throws ParserConfigurationException, SAXException, IOException  {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(false);
    DocumentBuilder builder = factory.newDocumentBuilder();
    return builder.parse(new ByteArrayInputStream(content.getBytes()));
  }

  public static Document parseFileToDom(String filename) throws ParserConfigurationException, SAXException, IOException  {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(false);
    DocumentBuilder builder = factory.newDocumentBuilder();
    //FIXME resource leak
    return builder.parse(new FileInputStream(filename));
  }

  public static Element getLastChild(Element e) {
    if (e == null)
      return null;
    Node n = e.getLastChild();
    while (n != null && n.getNodeType() != Node.ELEMENT_NODE)
      n = n.getPreviousSibling();
    return (Element) n;
  }

  public static Element getPrevSibling(Element e) {
    Node n = e.getPreviousSibling();
    while (n != null && n.getNodeType() != Node.ELEMENT_NODE)
      n = n.getPreviousSibling();
    return (Element) n;
  }

  public static String getNamedChildAttribute(Element element, String name, String aname) {
    Element e = getNamedChild(element, name);
    return e == null ? null : e.getAttribute(aname);
  }

  public static void writeDomToFile(Document doc, String filename) throws TransformerException {
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    DOMSource source = new DOMSource(doc);
    StreamResult streamResult =  new StreamResult(new File(filename));
    transformer.transform(source, streamResult);    
  }

  public static String getXsiType(org.w3c.dom.Element element) {
    Attr a = element.getAttributeNodeNS("http://www.w3.org/2001/XMLSchema-instance", "type");
    return (a == null ? null : a.getTextContent());
    
  }

	public static String getDirectText(org.w3c.dom.Element node) {
    Node n = node.getFirstChild();
    StringBuilder b = new StringBuilder();
    while (n != null) {
    	if (n.getNodeType() == Node.TEXT_NODE) 
    		b.append(n.getTextContent());
    	n = n.getNextSibling();
    }
	  return b.toString().trim();
	}

 	
}
