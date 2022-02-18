package ca.uhn.fhir.util;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.model.primitive.XhtmlDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.util.jar.DependencyLogFactory;
import ca.uhn.fhir.util.jar.IDependencyLog;
import com.ctc.wstx.api.WstxInputProperties;
import com.ctc.wstx.stax.WstxOutputFactory;
import org.apache.commons.text.StringEscapeUtils;
import org.codehaus.stax2.XMLOutputFactory2;
import org.codehaus.stax2.io.EscapingWriterFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLResolver;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Utility methods for working with the StAX API.
 * <p>
 * This class contains code adapted from the Apache Axiom project.
 */
public class XmlUtil {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(XmlUtil.class);
	private static final Map<String, Integer> VALID_ENTITY_NAMES;
	private static final ExtendedEntityReplacingXmlResolver XML_RESOLVER = new ExtendedEntityReplacingXmlResolver();
	private static XMLOutputFactory ourFragmentOutputFactory;
	private static volatile boolean ourHaveLoggedStaxImplementation;
	private static volatile XMLInputFactory ourInputFactory;
	private static Throwable ourNextException;
	private static volatile XMLOutputFactory ourOutputFactory;

	static {
		HashMap<String, Integer> validEntityNames = new HashMap<>(1448);
		validEntityNames.put("AElig", 0x000C6);
		validEntityNames.put("Aacute", 0x000C1);
		validEntityNames.put("Abreve", 0x00102);
		validEntityNames.put("Acirc", 0x000C2);
		validEntityNames.put("Acy", 0x00410);
		validEntityNames.put("Afr", 0x1D504);
		validEntityNames.put("Agrave", 0x000C0);
		validEntityNames.put("Alpha", 0x00391);
		validEntityNames.put("Amacr", 0x00100);
		validEntityNames.put("And", 0x02A53);
		validEntityNames.put("Aogon", 0x00104);
		validEntityNames.put("Aopf", 0x1D538);
		validEntityNames.put("ApplyFunction", 0x02061);
		validEntityNames.put("Aring", 0x000C5);
		validEntityNames.put("Ascr", 0x1D49C);
		validEntityNames.put("Atilde", 0x000C3);
		validEntityNames.put("Auml", 0x000C4);
		validEntityNames.put("Barv", 0x02AE7);
		validEntityNames.put("Barwed", 0x02306);
		validEntityNames.put("Bcy", 0x00411);
		validEntityNames.put("Beta", 0x00392);
		validEntityNames.put("Bfr", 0x1D505);
		validEntityNames.put("Bopf", 0x1D539);
		validEntityNames.put("CHcy", 0x00427);
		validEntityNames.put("Cacute", 0x00106);
		validEntityNames.put("Cap", 0x022D2);
		validEntityNames.put("CapitalDifferentialD", 0x02145);
		validEntityNames.put("Ccaron", 0x0010C);
		validEntityNames.put("Ccedil", 0x000C7);
		validEntityNames.put("Ccirc", 0x00108);
		validEntityNames.put("Cconint", 0x02230);
		validEntityNames.put("Cdot", 0x0010A);
		validEntityNames.put("Cfr", 0x0212D);
		validEntityNames.put("Chi", 0x003A7);
		validEntityNames.put("Colon", 0x02237);
		validEntityNames.put("Colone", 0x02A74);
		validEntityNames.put("Conint", 0x0222F);
		validEntityNames.put("Copf", 0x02102);
		validEntityNames.put("Cross", 0x02A2F);
		validEntityNames.put("Cscr", 0x1D49E);
		validEntityNames.put("Cup", 0x022D3);
		validEntityNames.put("DDotrahd", 0x02911);
		validEntityNames.put("DJcy", 0x00402);
		validEntityNames.put("DScy", 0x00405);
		validEntityNames.put("DZcy", 0x0040F);
		validEntityNames.put("Dagger", 0x02021);
		validEntityNames.put("Darr", 0x021A1);
		validEntityNames.put("Dashv", 0x02AE4);
		validEntityNames.put("Dcaron", 0x0010E);
		validEntityNames.put("Dcy", 0x00414);
		validEntityNames.put("Delta", 0x00394);
		validEntityNames.put("Dfr", 0x1D507);
		validEntityNames.put("DifferentialD", 0x02146);
		validEntityNames.put("Dopf", 0x1D53B);
		validEntityNames.put("Dot", 0x000A8);
		validEntityNames.put("DotDot", 0x020DC);
		validEntityNames.put("DownArrowBar", 0x02913);
		validEntityNames.put("DownBreve", 0x00311);
		validEntityNames.put("DownLeftRightVector", 0x02950);
		validEntityNames.put("DownLeftTeeVector", 0x0295E);
		validEntityNames.put("DownLeftVectorBar", 0x02956);
		validEntityNames.put("DownRightTeeVector", 0x0295F);
		validEntityNames.put("DownRightVectorBar", 0x02957);
		validEntityNames.put("DownTeeArrow", 0x021A7);
		validEntityNames.put("Dscr", 0x1D49F);
		validEntityNames.put("Dstrok", 0x00110);
		validEntityNames.put("ENG", 0x0014A);
		validEntityNames.put("ETH", 0x000D0);
		validEntityNames.put("Eacute", 0x000C9);
		validEntityNames.put("Ecaron", 0x0011A);
		validEntityNames.put("Ecirc", 0x000CA);
		validEntityNames.put("Ecy", 0x0042D);
		validEntityNames.put("Edot", 0x00116);
		validEntityNames.put("Efr", 0x1D508);
		validEntityNames.put("Egrave", 0x000C8);
		validEntityNames.put("Emacr", 0x00112);
		validEntityNames.put("EmptySmallSquare", 0x025FB);
		validEntityNames.put("EmptyVerySmallSquare", 0x025AB);
		validEntityNames.put("Eogon", 0x00118);
		validEntityNames.put("Eopf", 0x1D53C);
		validEntityNames.put("Epsilon", 0x00395);
		validEntityNames.put("Equal", 0x02A75);
		validEntityNames.put("Escr", 0x02130);
		validEntityNames.put("Esim", 0x02A73);
		validEntityNames.put("Eta", 0x00397);
		validEntityNames.put("Euml", 0x000CB);
		validEntityNames.put("ExponentialE", 0x02147);
		validEntityNames.put("Fcy", 0x00424);
		validEntityNames.put("Ffr", 0x1D509);
		validEntityNames.put("FilledSmallSquare", 0x025FC);
		validEntityNames.put("Fopf", 0x1D53D);
		validEntityNames.put("Fscr", 0x02131);
		validEntityNames.put("GJcy", 0x00403);
		validEntityNames.put("Gamma", 0x00393);
		validEntityNames.put("Gammad", 0x003DC);
		validEntityNames.put("Gbreve", 0x0011E);
		validEntityNames.put("Gcedil", 0x00122);
		validEntityNames.put("Gcirc", 0x0011C);
		validEntityNames.put("Gcy", 0x00413);
		validEntityNames.put("Gdot", 0x00120);
		validEntityNames.put("Gfr", 0x1D50A);
		validEntityNames.put("Gg", 0x022D9);
		validEntityNames.put("Gopf", 0x1D53E);
		validEntityNames.put("GreaterGreater", 0x02AA2);
		validEntityNames.put("Gscr", 0x1D4A2);
		validEntityNames.put("Gt", 0x0226B);
		validEntityNames.put("HARDcy", 0x0042A);
		validEntityNames.put("Hat", 0x0005E);
		validEntityNames.put("Hcirc", 0x00124);
		validEntityNames.put("Hfr", 0x0210C);
		validEntityNames.put("Hstrok", 0x00126);
		validEntityNames.put("IEcy", 0x00415);
		validEntityNames.put("IJlig", 0x00132);
		validEntityNames.put("IOcy", 0x00401);
		validEntityNames.put("Iacute", 0x000CD);
		validEntityNames.put("Icirc", 0x000CE);
		validEntityNames.put("Icy", 0x00418);
		validEntityNames.put("Idot", 0x00130);
		validEntityNames.put("Igrave", 0x000CC);
		validEntityNames.put("Imacr", 0x0012A);
		validEntityNames.put("ImaginaryI", 0x02148);
		validEntityNames.put("Int", 0x0222C);
		validEntityNames.put("InvisibleComma", 0x02063);
		validEntityNames.put("InvisibleTimes", 0x02062);
		validEntityNames.put("Iogon", 0x0012E);
		validEntityNames.put("Iopf", 0x1D540);
		validEntityNames.put("Iota", 0x00399);
		validEntityNames.put("Iscr", 0x02110);
		validEntityNames.put("Itilde", 0x00128);
		validEntityNames.put("Iukcy", 0x00406);
		validEntityNames.put("Iuml", 0x000CF);
		validEntityNames.put("Jcirc", 0x00134);
		validEntityNames.put("Jcy", 0x00419);
		validEntityNames.put("Jfr", 0x1D50D);
		validEntityNames.put("Jopf", 0x1D541);
		validEntityNames.put("Jscr", 0x1D4A5);
		validEntityNames.put("Jsercy", 0x00408);
		validEntityNames.put("Jukcy", 0x00404);
		validEntityNames.put("KHcy", 0x00425);
		validEntityNames.put("KJcy", 0x0040C);
		validEntityNames.put("Kappa", 0x0039A);
		validEntityNames.put("Kcedil", 0x00136);
		validEntityNames.put("Kcy", 0x0041A);
		validEntityNames.put("Kfr", 0x1D50E);
		validEntityNames.put("Kopf", 0x1D542);
		validEntityNames.put("Kscr", 0x1D4A6);
		validEntityNames.put("LJcy", 0x00409);
		validEntityNames.put("Lacute", 0x00139);
		validEntityNames.put("Lambda", 0x0039B);
		validEntityNames.put("Lang", 0x027EA);
		validEntityNames.put("Larr", 0x0219E);
		validEntityNames.put("Lcaron", 0x0013D);
		validEntityNames.put("Lcedil", 0x0013B);
		validEntityNames.put("Lcy", 0x0041B);
		validEntityNames.put("LeftDownTeeVector", 0x02961);
		validEntityNames.put("LeftDownVectorBar", 0x02959);
		validEntityNames.put("LeftRightVector", 0x0294E);
		validEntityNames.put("LeftTeeArrow", 0x021A4);
		validEntityNames.put("LeftTeeVector", 0x0295A);
		validEntityNames.put("LeftTriangleBar", 0x029CF);
		validEntityNames.put("LeftUpDownVector", 0x02951);
		validEntityNames.put("LeftUpTeeVector", 0x02960);
		validEntityNames.put("LeftUpVectorBar", 0x02958);
		validEntityNames.put("LeftVectorBar", 0x02952);
		validEntityNames.put("LessLess", 0x02AA1);
		validEntityNames.put("Lfr", 0x1D50F);
		validEntityNames.put("Ll", 0x022D8);
		validEntityNames.put("Lmidot", 0x0013F);
		validEntityNames.put("Lopf", 0x1D543);
		validEntityNames.put("Lscr", 0x02112);
		validEntityNames.put("Lstrok", 0x00141);
		validEntityNames.put("Lt", 0x0226A);
		validEntityNames.put("Map", 0x02905);
		validEntityNames.put("Mcy", 0x0041C);
		validEntityNames.put("MediumSpace", 0x0205F);
		validEntityNames.put("Mfr", 0x1D510);
		validEntityNames.put("Mopf", 0x1D544);
		validEntityNames.put("Mu", 0x0039C);
		validEntityNames.put("NJcy", 0x0040A);
		validEntityNames.put("Nacute", 0x00143);
		validEntityNames.put("Ncaron", 0x00147);
		validEntityNames.put("Ncedil", 0x00145);
		validEntityNames.put("Ncy", 0x0041D);
		validEntityNames.put("NewLine", 0x0000A);
		validEntityNames.put("Nfr", 0x1D511);
		validEntityNames.put("NoBreak", 0x02060);
		validEntityNames.put("Nopf", 0x02115);
		validEntityNames.put("Not", 0x02AEC);
		validEntityNames.put("NotCupCap", 0x0226D);
		validEntityNames.put("Nscr", 0x1D4A9);
		validEntityNames.put("Ntilde", 0x000D1);
		validEntityNames.put("Nu", 0x0039D);
		validEntityNames.put("OElig", 0x00152);
		validEntityNames.put("Oacute", 0x000D3);
		validEntityNames.put("Ocirc", 0x000D4);
		validEntityNames.put("Ocy", 0x0041E);
		validEntityNames.put("Odblac", 0x00150);
		validEntityNames.put("Ofr", 0x1D512);
		validEntityNames.put("Ograve", 0x000D2);
		validEntityNames.put("Omacr", 0x0014C);
		validEntityNames.put("Omega", 0x003A9);
		validEntityNames.put("Omicron", 0x0039F);
		validEntityNames.put("Oopf", 0x1D546);
		validEntityNames.put("Or", 0x02A54);
		validEntityNames.put("Oscr", 0x1D4AA);
		validEntityNames.put("Oslash", 0x000D8);
		validEntityNames.put("Otilde", 0x000D5);
		validEntityNames.put("Otimes", 0x02A37);
		validEntityNames.put("Ouml", 0x000D6);
		validEntityNames.put("OverBrace", 0x023DE);
		validEntityNames.put("OverParenthesis", 0x023DC);
		validEntityNames.put("Pcy", 0x0041F);
		validEntityNames.put("Pfr", 0x1D513);
		validEntityNames.put("Phi", 0x003A6);
		validEntityNames.put("Pi", 0x003A0);
		validEntityNames.put("Popf", 0x02119);
		validEntityNames.put("Pr", 0x02ABB);
		validEntityNames.put("Prime", 0x02033);
		validEntityNames.put("Pscr", 0x1D4AB);
		validEntityNames.put("Psi", 0x003A8);
		validEntityNames.put("Qfr", 0x1D514);
		validEntityNames.put("Qscr", 0x1D4AC);
		validEntityNames.put("RBarr", 0x02910);
		validEntityNames.put("Racute", 0x00154);
		validEntityNames.put("Rang", 0x027EB);
		validEntityNames.put("Rarr", 0x021A0);
		validEntityNames.put("Rarrtl", 0x02916);
		validEntityNames.put("Rcaron", 0x00158);
		validEntityNames.put("Rcedil", 0x00156);
		validEntityNames.put("Rcy", 0x00420);
		validEntityNames.put("Rho", 0x003A1);
		validEntityNames.put("RightDownTeeVector", 0x0295D);
		validEntityNames.put("RightDownVectorBar", 0x02955);
		validEntityNames.put("RightTeeVector", 0x0295B);
		validEntityNames.put("RightTriangleBar", 0x029D0);
		validEntityNames.put("RightUpDownVector", 0x0294F);
		validEntityNames.put("RightUpTeeVector", 0x0295C);
		validEntityNames.put("RightUpVectorBar", 0x02954);
		validEntityNames.put("RightVectorBar", 0x02953);
		validEntityNames.put("RoundImplies", 0x02970);
		validEntityNames.put("Rscr", 0x0211B);
		validEntityNames.put("RuleDelayed", 0x029F4);
		validEntityNames.put("SHCHcy", 0x00429);
		validEntityNames.put("SHcy", 0x00428);
		validEntityNames.put("SOFTcy", 0x0042C);
		validEntityNames.put("Sacute", 0x0015A);
		validEntityNames.put("Sc", 0x02ABC);
		validEntityNames.put("Scaron", 0x00160);
		validEntityNames.put("Scedil", 0x0015E);
		validEntityNames.put("Scirc", 0x0015C);
		validEntityNames.put("Scy", 0x00421);
		validEntityNames.put("Sfr", 0x1D516);
		validEntityNames.put("Sigma", 0x003A3);
		validEntityNames.put("Sopf", 0x1D54A);
		validEntityNames.put("Sscr", 0x1D4AE);
		validEntityNames.put("Sub", 0x022D0);
		validEntityNames.put("Sup", 0x022D1);
		validEntityNames.put("THORN", 0x000DE);
		validEntityNames.put("TSHcy", 0x0040B);
		validEntityNames.put("TScy", 0x00426);
		validEntityNames.put("Tab", 0x00009);
		validEntityNames.put("Tau", 0x003A4);
		validEntityNames.put("Tcaron", 0x00164);
		validEntityNames.put("Tcedil", 0x00162);
		validEntityNames.put("Tcy", 0x00422);
		validEntityNames.put("Tfr", 0x1D517);
		validEntityNames.put("Theta", 0x00398);
		validEntityNames.put("Topf", 0x1D54B);
		validEntityNames.put("Tscr", 0x1D4AF);
		validEntityNames.put("Tstrok", 0x00166);
		validEntityNames.put("Uacute", 0x000DA);
		validEntityNames.put("Uarr", 0x0219F);
		validEntityNames.put("Uarrocir", 0x02949);
		validEntityNames.put("Ubrcy", 0x0040E);
		validEntityNames.put("Ubreve", 0x0016C);
		validEntityNames.put("Ucirc", 0x000DB);
		validEntityNames.put("Ucy", 0x00423);
		validEntityNames.put("Udblac", 0x00170);
		validEntityNames.put("Ufr", 0x1D518);
		validEntityNames.put("Ugrave", 0x000D9);
		validEntityNames.put("Umacr", 0x0016A);
		validEntityNames.put("UnderBar", 0x00332);
		validEntityNames.put("UnderBrace", 0x023DF);
		validEntityNames.put("UnderParenthesis", 0x023DD);
		validEntityNames.put("Uogon", 0x00172);
		validEntityNames.put("Uopf", 0x1D54C);
		validEntityNames.put("UpArrowBar", 0x02912);
		validEntityNames.put("UpTeeArrow", 0x021A5);
		validEntityNames.put("Upsi", 0x003D2);
		validEntityNames.put("Upsilon", 0x003A5);
		validEntityNames.put("Uring", 0x0016E);
		validEntityNames.put("Uscr", 0x1D4B0);
		validEntityNames.put("Utilde", 0x00168);
		validEntityNames.put("Uuml", 0x000DC);
		validEntityNames.put("VDash", 0x022AB);
		validEntityNames.put("Vbar", 0x02AEB);
		validEntityNames.put("Vcy", 0x00412);
		validEntityNames.put("Vdash", 0x022A9);
		validEntityNames.put("Vdashl", 0x02AE6);
		validEntityNames.put("Verbar", 0x02016);
		validEntityNames.put("VerticalSeparator", 0x02758);
		validEntityNames.put("Vfr", 0x1D519);
		validEntityNames.put("Vopf", 0x1D54D);
		validEntityNames.put("Vscr", 0x1D4B1);
		validEntityNames.put("Vvdash", 0x022AA);
		validEntityNames.put("Wcirc", 0x00174);
		validEntityNames.put("Wfr", 0x1D51A);
		validEntityNames.put("Wopf", 0x1D54E);
		validEntityNames.put("Wscr", 0x1D4B2);
		validEntityNames.put("Xfr", 0x1D51B);
		validEntityNames.put("Xi", 0x0039E);
		validEntityNames.put("Xopf", 0x1D54F);
		validEntityNames.put("Xscr", 0x1D4B3);
		validEntityNames.put("YAcy", 0x0042F);
		validEntityNames.put("YIcy", 0x00407);
		validEntityNames.put("YUcy", 0x0042E);
		validEntityNames.put("Yacute", 0x000DD);
		validEntityNames.put("Ycirc", 0x00176);
		validEntityNames.put("Ycy", 0x0042B);
		validEntityNames.put("Yfr", 0x1D51C);
		validEntityNames.put("Yopf", 0x1D550);
		validEntityNames.put("Yscr", 0x1D4B4);
		validEntityNames.put("Yuml", 0x00178);
		validEntityNames.put("ZHcy", 0x00416);
		validEntityNames.put("Zacute", 0x00179);
		validEntityNames.put("Zcaron", 0x0017D);
		validEntityNames.put("Zcy", 0x00417);
		validEntityNames.put("Zdot", 0x0017B);
		validEntityNames.put("ZeroWidthSpace", 0x0200B);
		validEntityNames.put("Zeta", 0x00396);
		validEntityNames.put("Zfr", 0x02128);
		validEntityNames.put("Zscr", 0x1D4B5);
		validEntityNames.put("aacute", 0x000E1);
		validEntityNames.put("abreve", 0x00103);
		validEntityNames.put("ac", 0x0223E);
		validEntityNames.put("acd", 0x0223F);
		validEntityNames.put("acirc", 0x000E2);
		validEntityNames.put("acute", 0x000B4);
		validEntityNames.put("acy", 0x00430);
		validEntityNames.put("aelig", 0x000E6);
		validEntityNames.put("afr", 0x1D51E);
		validEntityNames.put("agrave", 0x000E0);
		validEntityNames.put("alefsym", 0x02135);
		validEntityNames.put("alpha", 0x003B1);
		validEntityNames.put("amacr", 0x00101);
		validEntityNames.put("amalg", 0x02A3F);
		validEntityNames.put("amp", 0x00026);
		validEntityNames.put("and", 0x02227);
		validEntityNames.put("andand", 0x02A55);
		validEntityNames.put("andd", 0x02A5C);
		validEntityNames.put("andslope", 0x02A58);
		validEntityNames.put("andv", 0x02A5A);
		validEntityNames.put("ang", 0x02220);
		validEntityNames.put("ange", 0x029A4);
		validEntityNames.put("angmsd", 0x02221);
		validEntityNames.put("angmsdaa", 0x029A8);
		validEntityNames.put("angmsdab", 0x029A9);
		validEntityNames.put("angmsdac", 0x029AA);
		validEntityNames.put("angmsdad", 0x029AB);
		validEntityNames.put("angmsdae", 0x029AC);
		validEntityNames.put("angmsdaf", 0x029AD);
		validEntityNames.put("angmsdag", 0x029AE);
		validEntityNames.put("angmsdah", 0x029AF);
		validEntityNames.put("angrt", 0x0221F);
		validEntityNames.put("angrtvb", 0x022BE);
		validEntityNames.put("angrtvbd", 0x0299D);
		validEntityNames.put("angsph", 0x02222);
		validEntityNames.put("angst", 0x0212B);
		validEntityNames.put("angzarr", 0x0237C);
		validEntityNames.put("aogon", 0x00105);
		validEntityNames.put("aopf", 0x1D552);
		validEntityNames.put("apE", 0x02A70);
		validEntityNames.put("apacir", 0x02A6F);
		validEntityNames.put("ape", 0x0224A);
		validEntityNames.put("apid", 0x0224B);
		validEntityNames.put("apos", 0x00027);
		validEntityNames.put("aring", 0x000E5);
		validEntityNames.put("ascr", 0x1D4B6);
		validEntityNames.put("ast", 0x0002A);
		validEntityNames.put("asymp", 0x02248);
		validEntityNames.put("asympeq", 0x0224D);
		validEntityNames.put("atilde", 0x000E3);
		validEntityNames.put("auml", 0x000E4);
		validEntityNames.put("awconint", 0x02233);
		validEntityNames.put("awint", 0x02A11);
		validEntityNames.put("bNot", 0x02AED);
		validEntityNames.put("barvee", 0x022BD);
		validEntityNames.put("barwed", 0x02305);
		validEntityNames.put("bbrk", 0x023B5);
		validEntityNames.put("bbrktbrk", 0x023B6);
		validEntityNames.put("bcong", 0x0224C);
		validEntityNames.put("bcy", 0x00431);
		validEntityNames.put("becaus", 0x02235);
		validEntityNames.put("bemptyv", 0x029B0);
		validEntityNames.put("bepsi", 0x003F6);
		validEntityNames.put("bernou", 0x0212C);
		validEntityNames.put("beta", 0x003B2);
		validEntityNames.put("beth", 0x02136);
		validEntityNames.put("bfr", 0x1D51F);
		validEntityNames.put("blank", 0x02423);
		validEntityNames.put("blk12", 0x02592);
		validEntityNames.put("blk14", 0x02591);
		validEntityNames.put("blk34", 0x02593);
		validEntityNames.put("block", 0x02588);
		validEntityNames.put("bnot", 0x02310);
		validEntityNames.put("bopf", 0x1D553);
		validEntityNames.put("bottom", 0x022A5);
		validEntityNames.put("bowtie", 0x022C8);
		validEntityNames.put("boxDL", 0x02557);
		validEntityNames.put("boxDR", 0x02554);
		validEntityNames.put("boxDl", 0x02556);
		validEntityNames.put("boxDr", 0x02553);
		validEntityNames.put("boxH", 0x02550);
		validEntityNames.put("boxHD", 0x02566);
		validEntityNames.put("boxHU", 0x02569);
		validEntityNames.put("boxHd", 0x02564);
		validEntityNames.put("boxHu", 0x02567);
		validEntityNames.put("boxUL", 0x0255D);
		validEntityNames.put("boxUR", 0x0255A);
		validEntityNames.put("boxUl", 0x0255C);
		validEntityNames.put("boxUr", 0x02559);
		validEntityNames.put("boxV", 0x02551);
		validEntityNames.put("boxVH", 0x0256C);
		validEntityNames.put("boxVL", 0x02563);
		validEntityNames.put("boxVR", 0x02560);
		validEntityNames.put("boxVh", 0x0256B);
		validEntityNames.put("boxVl", 0x02562);
		validEntityNames.put("boxVr", 0x0255F);
		validEntityNames.put("boxbox", 0x029C9);
		validEntityNames.put("boxdL", 0x02555);
		validEntityNames.put("boxdR", 0x02552);
		validEntityNames.put("boxdl", 0x02510);
		validEntityNames.put("boxdr", 0x0250C);
		validEntityNames.put("boxh", 0x02500);
		validEntityNames.put("boxhD", 0x02565);
		validEntityNames.put("boxhU", 0x02568);
		validEntityNames.put("boxhd", 0x0252C);
		validEntityNames.put("boxhu", 0x02534);
		validEntityNames.put("boxuL", 0x0255B);
		validEntityNames.put("boxuR", 0x02558);
		validEntityNames.put("boxul", 0x02518);
		validEntityNames.put("boxur", 0x02514);
		validEntityNames.put("boxv", 0x02502);
		validEntityNames.put("boxvH", 0x0256A);
		validEntityNames.put("boxvL", 0x02561);
		validEntityNames.put("boxvR", 0x0255E);
		validEntityNames.put("boxvh", 0x0253C);
		validEntityNames.put("boxvl", 0x02524);
		validEntityNames.put("boxvr", 0x0251C);
		validEntityNames.put("bprime", 0x02035);
		validEntityNames.put("breve", 0x002D8);
		validEntityNames.put("brvbar", 0x000A6);
		validEntityNames.put("bscr", 0x1D4B7);
		validEntityNames.put("bsemi", 0x0204F);
		validEntityNames.put("bsim", 0x0223D);
		validEntityNames.put("bsime", 0x022CD);
		validEntityNames.put("bsol", 0x0005C);
		validEntityNames.put("bsolb", 0x029C5);
		validEntityNames.put("bull", 0x02022);
		validEntityNames.put("bump", 0x0224E);
		validEntityNames.put("bumpE", 0x02AAE);
		validEntityNames.put("bumpe", 0x0224F);
		validEntityNames.put("cacute", 0x00107);
		validEntityNames.put("cap", 0x02229);
		validEntityNames.put("capand", 0x02A44);
		validEntityNames.put("capbrcup", 0x02A49);
		validEntityNames.put("capcap", 0x02A4B);
		validEntityNames.put("capcup", 0x02A47);
		validEntityNames.put("capdot", 0x02A40);
		validEntityNames.put("caret", 0x02041);
		validEntityNames.put("caron", 0x002C7);
		validEntityNames.put("ccaps", 0x02A4D);
		validEntityNames.put("ccaron", 0x0010D);
		validEntityNames.put("ccedil", 0x000E7);
		validEntityNames.put("ccirc", 0x00109);
		validEntityNames.put("ccups", 0x02A4C);
		validEntityNames.put("ccupssm", 0x02A50);
		validEntityNames.put("cdot", 0x0010B);
		validEntityNames.put("cedil", 0x000B8);
		validEntityNames.put("cemptyv", 0x029B2);
		validEntityNames.put("cent", 0x000A2);
		validEntityNames.put("cfr", 0x1D520);
		validEntityNames.put("chcy", 0x00447);
		validEntityNames.put("check", 0x02713);
		validEntityNames.put("chi", 0x003C7);
		validEntityNames.put("cir", 0x025CB);
		validEntityNames.put("cirE", 0x029C3);
		validEntityNames.put("circ", 0x002C6);
		validEntityNames.put("cire", 0x02257);
		validEntityNames.put("cirfnint", 0x02A10);
		validEntityNames.put("cirmid", 0x02AEF);
		validEntityNames.put("cirscir", 0x029C2);
		validEntityNames.put("clubs", 0x02663);
		validEntityNames.put("colon", 0x0003A);
		validEntityNames.put("colone", 0x02254);
		validEntityNames.put("comma", 0x0002C);
		validEntityNames.put("commat", 0x00040);
		validEntityNames.put("comp", 0x02201);
		validEntityNames.put("compfn", 0x02218);
		validEntityNames.put("cong", 0x02245);
		validEntityNames.put("congdot", 0x02A6D);
		validEntityNames.put("conint", 0x0222E);
		validEntityNames.put("copf", 0x1D554);
		validEntityNames.put("coprod", 0x02210);
		validEntityNames.put("copy", 0x000A9);
		validEntityNames.put("copysr", 0x02117);
		validEntityNames.put("crarr", 0x021B5);
		validEntityNames.put("cross", 0x02717);
		validEntityNames.put("cscr", 0x1D4B8);
		validEntityNames.put("csub", 0x02ACF);
		validEntityNames.put("csube", 0x02AD1);
		validEntityNames.put("csup", 0x02AD0);
		validEntityNames.put("csupe", 0x02AD2);
		validEntityNames.put("ctdot", 0x022EF);
		validEntityNames.put("cudarrl", 0x02938);
		validEntityNames.put("cudarrr", 0x02935);
		validEntityNames.put("cuepr", 0x022DE);
		validEntityNames.put("cuesc", 0x022DF);
		validEntityNames.put("cularr", 0x021B6);
		validEntityNames.put("cularrp", 0x0293D);
		validEntityNames.put("cup", 0x0222A);
		validEntityNames.put("cupbrcap", 0x02A48);
		validEntityNames.put("cupcap", 0x02A46);
		validEntityNames.put("cupcup", 0x02A4A);
		validEntityNames.put("cupdot", 0x0228D);
		validEntityNames.put("cupor", 0x02A45);
		validEntityNames.put("curarr", 0x021B7);
		validEntityNames.put("curarrm", 0x0293C);
		validEntityNames.put("curren", 0x000A4);
		validEntityNames.put("cuvee", 0x022CE);
		validEntityNames.put("cuwed", 0x022CF);
		validEntityNames.put("cwconint", 0x02232);
		validEntityNames.put("cwint", 0x02231);
		validEntityNames.put("cylcty", 0x0232D);
		validEntityNames.put("dArr", 0x021D3);
		validEntityNames.put("dHar", 0x02965);
		validEntityNames.put("dagger", 0x02020);
		validEntityNames.put("daleth", 0x02138);
		validEntityNames.put("darr", 0x02193);
		validEntityNames.put("dashv", 0x022A3);
		validEntityNames.put("dblac", 0x002DD);
		validEntityNames.put("dcaron", 0x0010F);
		validEntityNames.put("dcy", 0x00434);
		validEntityNames.put("ddarr", 0x021CA);
		validEntityNames.put("deg", 0x000B0);
		validEntityNames.put("delta", 0x003B4);
		validEntityNames.put("demptyv", 0x029B1);
		validEntityNames.put("dfisht", 0x0297F);
		validEntityNames.put("dfr", 0x1D521);
		validEntityNames.put("dharl", 0x021C3);
		validEntityNames.put("dharr", 0x021C2);
		validEntityNames.put("diam", 0x022C4);
		validEntityNames.put("diams", 0x02666);
		validEntityNames.put("disin", 0x022F2);
		validEntityNames.put("divide", 0x000F7);
		validEntityNames.put("divonx", 0x022C7);
		validEntityNames.put("djcy", 0x00452);
		validEntityNames.put("dlcorn", 0x0231E);
		validEntityNames.put("dlcrop", 0x0230D);
		validEntityNames.put("dollar", 0x00024);
		validEntityNames.put("dopf", 0x1D555);
		validEntityNames.put("dot", 0x002D9);
		validEntityNames.put("drcorn", 0x0231F);
		validEntityNames.put("drcrop", 0x0230C);
		validEntityNames.put("dscr", 0x1D4B9);
		validEntityNames.put("dscy", 0x00455);
		validEntityNames.put("dsol", 0x029F6);
		validEntityNames.put("dstrok", 0x00111);
		validEntityNames.put("dtdot", 0x022F1);
		validEntityNames.put("dtri", 0x025BF);
		validEntityNames.put("dtrif", 0x025BE);
		validEntityNames.put("duarr", 0x021F5);
		validEntityNames.put("duhar", 0x0296F);
		validEntityNames.put("dwangle", 0x029A6);
		validEntityNames.put("dzcy", 0x0045F);
		validEntityNames.put("dzigrarr", 0x027FF);
		validEntityNames.put("eDDot", 0x02A77);
		validEntityNames.put("eDot", 0x02251);
		validEntityNames.put("eacute", 0x000E9);
		validEntityNames.put("easter", 0x02A6E);
		validEntityNames.put("ecaron", 0x0011B);
		validEntityNames.put("ecir", 0x02256);
		validEntityNames.put("ecirc", 0x000EA);
		validEntityNames.put("ecolon", 0x02255);
		validEntityNames.put("ecy", 0x0044D);
		validEntityNames.put("edot", 0x00117);
		validEntityNames.put("efDot", 0x02252);
		validEntityNames.put("efr", 0x1D522);
		validEntityNames.put("eg", 0x02A9A);
		validEntityNames.put("egrave", 0x000E8);
		validEntityNames.put("egs", 0x02A96);
		validEntityNames.put("egsdot", 0x02A98);
		validEntityNames.put("el", 0x02A99);
		validEntityNames.put("elinters", 0x023E7);
		validEntityNames.put("ell", 0x02113);
		validEntityNames.put("els", 0x02A95);
		validEntityNames.put("elsdot", 0x02A97);
		validEntityNames.put("emacr", 0x00113);
		validEntityNames.put("empty", 0x02205);
		validEntityNames.put("emsp", 0x02003);
		validEntityNames.put("emsp13", 0x02004);
		validEntityNames.put("emsp14", 0x02005);
		validEntityNames.put("eng", 0x0014B);
		validEntityNames.put("ensp", 0x02002);
		validEntityNames.put("eogon", 0x00119);
		validEntityNames.put("eopf", 0x1D556);
		validEntityNames.put("epar", 0x022D5);
		validEntityNames.put("eparsl", 0x029E3);
		validEntityNames.put("eplus", 0x02A71);
		validEntityNames.put("epsi", 0x003F5);
		validEntityNames.put("epsiv", 0x003B5);
		validEntityNames.put("equals", 0x0003D);
		validEntityNames.put("equest", 0x0225F);
		validEntityNames.put("equiv", 0x02261);
		validEntityNames.put("equivDD", 0x02A78);
		validEntityNames.put("eqvparsl", 0x029E5);
		validEntityNames.put("erDot", 0x02253);
		validEntityNames.put("erarr", 0x02971);
		validEntityNames.put("escr", 0x0212F);
		validEntityNames.put("esdot", 0x02250);
		validEntityNames.put("esim", 0x02242);
		validEntityNames.put("eta", 0x003B7);
		validEntityNames.put("eth", 0x000F0);
		validEntityNames.put("euml", 0x000EB);
		validEntityNames.put("euro", 0x020AC);
		validEntityNames.put("excl", 0x00021);
		validEntityNames.put("exist", 0x02203);
		validEntityNames.put("fcy", 0x00444);
		validEntityNames.put("female", 0x02640);
		validEntityNames.put("ffilig", 0x0FB03);
		validEntityNames.put("fflig", 0x0FB00);
		validEntityNames.put("ffllig", 0x0FB04);
		validEntityNames.put("ffr", 0x1D523);
		validEntityNames.put("filig", 0x0FB01);
		validEntityNames.put("flat", 0x0266D);
		validEntityNames.put("fllig", 0x0FB02);
		validEntityNames.put("fltns", 0x025B1);
		validEntityNames.put("fnof", 0x00192);
		validEntityNames.put("fopf", 0x1D557);
		validEntityNames.put("forall", 0x02200);
		validEntityNames.put("fork", 0x022D4);
		validEntityNames.put("forkv", 0x02AD9);
		validEntityNames.put("fpartint", 0x02A0D);
		validEntityNames.put("frac12", 0x000BD);
		validEntityNames.put("frac13", 0x02153);
		validEntityNames.put("frac14", 0x000BC);
		validEntityNames.put("frac15", 0x02155);
		validEntityNames.put("frac16", 0x02159);
		validEntityNames.put("frac18", 0x0215B);
		validEntityNames.put("frac23", 0x02154);
		validEntityNames.put("frac25", 0x02156);
		validEntityNames.put("frac34", 0x000BE);
		validEntityNames.put("frac35", 0x02157);
		validEntityNames.put("frac38", 0x0215C);
		validEntityNames.put("frac45", 0x02158);
		validEntityNames.put("frac56", 0x0215A);
		validEntityNames.put("frac58", 0x0215D);
		validEntityNames.put("frac78", 0x0215E);
		validEntityNames.put("frasl", 0x02044);
		validEntityNames.put("frown", 0x02322);
		validEntityNames.put("fscr", 0x1D4BB);
		validEntityNames.put("gE", 0x02267);
		validEntityNames.put("gEl", 0x02A8C);
		validEntityNames.put("gacute", 0x001F5);
		validEntityNames.put("gamma", 0x003B3);
		validEntityNames.put("gammad", 0x003DD);
		validEntityNames.put("gap", 0x02A86);
		validEntityNames.put("gbreve", 0x0011F);
		validEntityNames.put("gcirc", 0x0011D);
		validEntityNames.put("gcy", 0x00433);
		validEntityNames.put("gdot", 0x00121);
		validEntityNames.put("ge", 0x02265);
		validEntityNames.put("gel", 0x022DB);
		validEntityNames.put("ges", 0x02A7E);
		validEntityNames.put("gescc", 0x02AA9);
		validEntityNames.put("gesdot", 0x02A80);
		validEntityNames.put("gesdoto", 0x02A82);
		validEntityNames.put("gesdotol", 0x02A84);
		validEntityNames.put("gesles", 0x02A94);
		validEntityNames.put("gfr", 0x1D524);
		validEntityNames.put("gimel", 0x02137);
		validEntityNames.put("gjcy", 0x00453);
		validEntityNames.put("gl", 0x02277);
		validEntityNames.put("glE", 0x02A92);
		validEntityNames.put("gla", 0x02AA5);
		validEntityNames.put("glj", 0x02AA4);
		validEntityNames.put("gnE", 0x02269);
		validEntityNames.put("gnap", 0x02A8A);
		validEntityNames.put("gne", 0x02A88);
		validEntityNames.put("gnsim", 0x022E7);
		validEntityNames.put("gopf", 0x1D558);
		validEntityNames.put("grave", 0x00060);
		validEntityNames.put("gscr", 0x0210A);
		validEntityNames.put("gsim", 0x02273);
		validEntityNames.put("gsime", 0x02A8E);
		validEntityNames.put("gsiml", 0x02A90);
		validEntityNames.put("gt", 0x0003E);
		validEntityNames.put("gtcc", 0x02AA7);
		validEntityNames.put("gtcir", 0x02A7A);
		validEntityNames.put("gtdot", 0x022D7);
		validEntityNames.put("gtlPar", 0x02995);
		validEntityNames.put("gtquest", 0x02A7C);
		validEntityNames.put("gtrarr", 0x02978);
		validEntityNames.put("hArr", 0x021D4);
		validEntityNames.put("hairsp", 0x0200A);
		validEntityNames.put("hamilt", 0x0210B);
		validEntityNames.put("hardcy", 0x0044A);
		validEntityNames.put("harr", 0x02194);
		validEntityNames.put("harrcir", 0x02948);
		validEntityNames.put("harrw", 0x021AD);
		validEntityNames.put("hcirc", 0x00125);
		validEntityNames.put("hearts", 0x02665);
		validEntityNames.put("hellip", 0x02026);
		validEntityNames.put("hercon", 0x022B9);
		validEntityNames.put("hfr", 0x1D525);
		validEntityNames.put("hoarr", 0x021FF);
		validEntityNames.put("homtht", 0x0223B);
		validEntityNames.put("hopf", 0x1D559);
		validEntityNames.put("horbar", 0x02015);
		validEntityNames.put("hscr", 0x1D4BD);
		validEntityNames.put("hstrok", 0x00127);
		validEntityNames.put("hybull", 0x02043);
		validEntityNames.put("hyphen", 0x02010);
		validEntityNames.put("iacute", 0x000ED);
		validEntityNames.put("icirc", 0x000EE);
		validEntityNames.put("icy", 0x00438);
		validEntityNames.put("iecy", 0x00435);
		validEntityNames.put("iexcl", 0x000A1);
		validEntityNames.put("ifr", 0x1D526);
		validEntityNames.put("igrave", 0x000EC);
		validEntityNames.put("iinfin", 0x029DC);
		validEntityNames.put("iiota", 0x02129);
		validEntityNames.put("ijlig", 0x00133);
		validEntityNames.put("imacr", 0x0012B);
		validEntityNames.put("image", 0x02111);
		validEntityNames.put("imath", 0x00131);
		validEntityNames.put("imof", 0x022B7);
		validEntityNames.put("imped", 0x001B5);
		validEntityNames.put("incare", 0x02105);
		validEntityNames.put("infin", 0x0221E);
		validEntityNames.put("infintie", 0x029DD);
		validEntityNames.put("int", 0x0222B);
		validEntityNames.put("intcal", 0x022BA);
		validEntityNames.put("integers", 0x02124);
		validEntityNames.put("intlarhk", 0x02A17);
		validEntityNames.put("iocy", 0x00451);
		validEntityNames.put("iogon", 0x0012F);
		validEntityNames.put("iopf", 0x1D55A);
		validEntityNames.put("iota", 0x003B9);
		validEntityNames.put("iprod", 0x02A3C);
		validEntityNames.put("iquest", 0x000BF);
		validEntityNames.put("iscr", 0x1D4BE);
		validEntityNames.put("isin", 0x02208);
		validEntityNames.put("isinE", 0x022F9);
		validEntityNames.put("isindot", 0x022F5);
		validEntityNames.put("isins", 0x022F4);
		validEntityNames.put("isinsv", 0x022F3);
		validEntityNames.put("itilde", 0x00129);
		validEntityNames.put("iukcy", 0x00456);
		validEntityNames.put("iuml", 0x000EF);
		validEntityNames.put("jcirc", 0x00135);
		validEntityNames.put("jcy", 0x00439);
		validEntityNames.put("jfr", 0x1D527);
		validEntityNames.put("jmath", 0x00237);
		validEntityNames.put("jopf", 0x1D55B);
		validEntityNames.put("jscr", 0x1D4BF);
		validEntityNames.put("jsercy", 0x00458);
		validEntityNames.put("jukcy", 0x00454);
		validEntityNames.put("kappa", 0x003BA);
		validEntityNames.put("kappav", 0x003F0);
		validEntityNames.put("kcedil", 0x00137);
		validEntityNames.put("kcy", 0x0043A);
		validEntityNames.put("kfr", 0x1D528);
		validEntityNames.put("kgreen", 0x00138);
		validEntityNames.put("khcy", 0x00445);
		validEntityNames.put("kjcy", 0x0045C);
		validEntityNames.put("kopf", 0x1D55C);
		validEntityNames.put("kscr", 0x1D4C0);
		validEntityNames.put("lAarr", 0x021DA);
		validEntityNames.put("lArr", 0x021D0);
		validEntityNames.put("lAtail", 0x0291B);
		validEntityNames.put("lBarr", 0x0290E);
		validEntityNames.put("lE", 0x02266);
		validEntityNames.put("lEg", 0x02A8B);
		validEntityNames.put("lHar", 0x02962);
		validEntityNames.put("lacute", 0x0013A);
		validEntityNames.put("laemptyv", 0x029B4);
		validEntityNames.put("lambda", 0x003BB);
		validEntityNames.put("lang", 0x027E8);
		validEntityNames.put("langd", 0x02991);
		validEntityNames.put("lap", 0x02A85);
		validEntityNames.put("laquo", 0x000AB);
		validEntityNames.put("larr", 0x02190);
		validEntityNames.put("larrb", 0x021E4);
		validEntityNames.put("larrbfs", 0x0291F);
		validEntityNames.put("larrfs", 0x0291D);
		validEntityNames.put("larrhk", 0x021A9);
		validEntityNames.put("larrlp", 0x021AB);
		validEntityNames.put("larrpl", 0x02939);
		validEntityNames.put("larrsim", 0x02973);
		validEntityNames.put("larrtl", 0x021A2);
		validEntityNames.put("lat", 0x02AAB);
		validEntityNames.put("latail", 0x02919);
		validEntityNames.put("late", 0x02AAD);
		validEntityNames.put("lbarr", 0x0290C);
		validEntityNames.put("lbbrk", 0x02772);
		validEntityNames.put("lbrke", 0x0298B);
		validEntityNames.put("lbrksld", 0x0298F);
		validEntityNames.put("lbrkslu", 0x0298D);
		validEntityNames.put("lcaron", 0x0013E);
		validEntityNames.put("lcedil", 0x0013C);
		validEntityNames.put("lceil", 0x02308);
		validEntityNames.put("lcub", 0x0007B);
		validEntityNames.put("lcy", 0x0043B);
		validEntityNames.put("ldca", 0x02936);
		validEntityNames.put("ldquo", 0x0201C);
		validEntityNames.put("ldquor", 0x0201E);
		validEntityNames.put("ldrdhar", 0x02967);
		validEntityNames.put("ldrushar", 0x0294B);
		validEntityNames.put("ldsh", 0x021B2);
		validEntityNames.put("le", 0x02264);
		validEntityNames.put("leg", 0x022DA);
		validEntityNames.put("les", 0x02A7D);
		validEntityNames.put("lescc", 0x02AA8);
		validEntityNames.put("lesdot", 0x02A7F);
		validEntityNames.put("lesdoto", 0x02A81);
		validEntityNames.put("lesdotor", 0x02A83);
		validEntityNames.put("lesges", 0x02A93);
		validEntityNames.put("lfisht", 0x0297C);
		validEntityNames.put("lfloor", 0x0230A);
		validEntityNames.put("lfr", 0x1D529);
		validEntityNames.put("lg", 0x02276);
		validEntityNames.put("lgE", 0x02A91);
		validEntityNames.put("lhard", 0x021BD);
		validEntityNames.put("lharu", 0x021BC);
		validEntityNames.put("lharul", 0x0296A);
		validEntityNames.put("lhblk", 0x02584);
		validEntityNames.put("ljcy", 0x00459);
		validEntityNames.put("llarr", 0x021C7);
		validEntityNames.put("llhard", 0x0296B);
		validEntityNames.put("lltri", 0x025FA);
		validEntityNames.put("lmidot", 0x00140);
		validEntityNames.put("lmoust", 0x023B0);
		validEntityNames.put("lnE", 0x02268);
		validEntityNames.put("lnap", 0x02A89);
		validEntityNames.put("lne", 0x02A87);
		validEntityNames.put("lnsim", 0x022E6);
		validEntityNames.put("loang", 0x027EC);
		validEntityNames.put("loarr", 0x021FD);
		validEntityNames.put("lobrk", 0x027E6);
		validEntityNames.put("lopar", 0x02985);
		validEntityNames.put("lopf", 0x1D55D);
		validEntityNames.put("loplus", 0x02A2D);
		validEntityNames.put("lotimes", 0x02A34);
		validEntityNames.put("lowast", 0x02217);
		validEntityNames.put("lowbar", 0x0005F);
		validEntityNames.put("loz", 0x025CA);
		validEntityNames.put("lozf", 0x029EB);
		validEntityNames.put("lpar", 0x00028);
		validEntityNames.put("lparlt", 0x02993);
		validEntityNames.put("lrarr", 0x021C6);
		validEntityNames.put("lrhar", 0x021CB);
		validEntityNames.put("lrhard", 0x0296D);
		validEntityNames.put("lrm", 0x0200E);
		validEntityNames.put("lrtri", 0x022BF);
		validEntityNames.put("lsaquo", 0x02039);
		validEntityNames.put("lscr", 0x1D4C1);
		validEntityNames.put("lsh", 0x021B0);
		validEntityNames.put("lsim", 0x02272);
		validEntityNames.put("lsime", 0x02A8D);
		validEntityNames.put("lsimg", 0x02A8F);
		validEntityNames.put("lsqb", 0x0005B);
		validEntityNames.put("lsquo", 0x02018);
		validEntityNames.put("lsquor", 0x0201A);
		validEntityNames.put("lstrok", 0x00142);
		validEntityNames.put("lt", 0x0003C);
		validEntityNames.put("ltcc", 0x02AA6);
		validEntityNames.put("ltcir", 0x02A79);
		validEntityNames.put("ltdot", 0x022D6);
		validEntityNames.put("lthree", 0x022CB);
		validEntityNames.put("ltimes", 0x022C9);
		validEntityNames.put("ltlarr", 0x02976);
		validEntityNames.put("ltquest", 0x02A7B);
		validEntityNames.put("ltrPar", 0x02996);
		validEntityNames.put("ltri", 0x025C3);
		validEntityNames.put("ltrie", 0x022B4);
		validEntityNames.put("ltrif", 0x025C2);
		validEntityNames.put("lurdshar", 0x0294A);
		validEntityNames.put("luruhar", 0x02966);
		validEntityNames.put("mDDot", 0x0223A);
		validEntityNames.put("macr", 0x000AF);
		validEntityNames.put("male", 0x02642);
		validEntityNames.put("malt", 0x02720);
		validEntityNames.put("map", 0x021A6);
		validEntityNames.put("marker", 0x025AE);
		validEntityNames.put("mcomma", 0x02A29);
		validEntityNames.put("mcy", 0x0043C);
		validEntityNames.put("mdash", 0x02014);
		validEntityNames.put("mfr", 0x1D52A);
		validEntityNames.put("mho", 0x02127);
		validEntityNames.put("micro", 0x000B5);
		validEntityNames.put("mid", 0x02223);
		validEntityNames.put("midcir", 0x02AF0);
		validEntityNames.put("middot", 0x000B7);
		validEntityNames.put("minus", 0x02212);
		validEntityNames.put("minusb", 0x0229F);
		validEntityNames.put("minusd", 0x02238);
		validEntityNames.put("minusdu", 0x02A2A);
		validEntityNames.put("mlcp", 0x02ADB);
		validEntityNames.put("mnplus", 0x02213);
		validEntityNames.put("models", 0x022A7);
		validEntityNames.put("mopf", 0x1D55E);
		validEntityNames.put("mscr", 0x1D4C2);
		validEntityNames.put("mu", 0x003BC);
		validEntityNames.put("mumap", 0x022B8);
		validEntityNames.put("nVDash", 0x022AF);
		validEntityNames.put("nVdash", 0x022AE);
		validEntityNames.put("nabla", 0x02207);
		validEntityNames.put("nacute", 0x00144);
		validEntityNames.put("nap", 0x02249);
		validEntityNames.put("napos", 0x00149);
		validEntityNames.put("natur", 0x0266E);
		validEntityNames.put("nbsp", 0x000A0);
		validEntityNames.put("ncap", 0x02A43);
		validEntityNames.put("ncaron", 0x00148);
		validEntityNames.put("ncedil", 0x00146);
		validEntityNames.put("ncong", 0x02247);
		validEntityNames.put("ncup", 0x02A42);
		validEntityNames.put("ncy", 0x0043D);
		validEntityNames.put("ndash", 0x02013);
		validEntityNames.put("ne", 0x02260);
		validEntityNames.put("neArr", 0x021D7);
		validEntityNames.put("nearhk", 0x02924);
		validEntityNames.put("nearr", 0x02197);
		validEntityNames.put("nequiv", 0x02262);
		validEntityNames.put("nesear", 0x02928);
		validEntityNames.put("nexist", 0x02204);
		validEntityNames.put("nfr", 0x1D52B);
		validEntityNames.put("nge", 0x02271);
		validEntityNames.put("ngsim", 0x02275);
		validEntityNames.put("ngt", 0x0226F);
		validEntityNames.put("nhArr", 0x021CE);
		validEntityNames.put("nharr", 0x021AE);
		validEntityNames.put("nhpar", 0x02AF2);
		validEntityNames.put("nis", 0x022FC);
		validEntityNames.put("nisd", 0x022FA);
		validEntityNames.put("niv", 0x0220B);
		validEntityNames.put("njcy", 0x0045A);
		validEntityNames.put("nlArr", 0x021CD);
		validEntityNames.put("nlarr", 0x0219A);
		validEntityNames.put("nldr", 0x02025);
		validEntityNames.put("nle", 0x02270);
		validEntityNames.put("nlsim", 0x02274);
		validEntityNames.put("nlt", 0x0226E);
		validEntityNames.put("nltri", 0x022EA);
		validEntityNames.put("nltrie", 0x022EC);
		validEntityNames.put("nmid", 0x02224);
		validEntityNames.put("nopf", 0x1D55F);
		validEntityNames.put("not", 0x000AC);
		validEntityNames.put("notin", 0x02209);
		validEntityNames.put("notinvb", 0x022F7);
		validEntityNames.put("notinvc", 0x022F6);
		validEntityNames.put("notni", 0x0220C);
		validEntityNames.put("notnivb", 0x022FE);
		validEntityNames.put("notnivc", 0x022FD);
		validEntityNames.put("npar", 0x02226);
		validEntityNames.put("npolint", 0x02A14);
		validEntityNames.put("npr", 0x02280);
		validEntityNames.put("nprcue", 0x022E0);
		validEntityNames.put("nrArr", 0x021CF);
		validEntityNames.put("nrarr", 0x0219B);
		validEntityNames.put("nrtri", 0x022EB);
		validEntityNames.put("nrtrie", 0x022ED);
		validEntityNames.put("nsc", 0x02281);
		validEntityNames.put("nsccue", 0x022E1);
		validEntityNames.put("nscr", 0x1D4C3);
		validEntityNames.put("nsim", 0x02241);
		validEntityNames.put("nsime", 0x02244);
		validEntityNames.put("nsqsube", 0x022E2);
		validEntityNames.put("nsqsupe", 0x022E3);
		validEntityNames.put("nsub", 0x02284);
		validEntityNames.put("nsube", 0x02288);
		validEntityNames.put("nsup", 0x02285);
		validEntityNames.put("nsupe", 0x02289);
		validEntityNames.put("ntgl", 0x02279);
		validEntityNames.put("ntilde", 0x000F1);
		validEntityNames.put("ntlg", 0x02278);
		validEntityNames.put("nu", 0x003BD);
		validEntityNames.put("num", 0x00023);
		validEntityNames.put("numero", 0x02116);
		validEntityNames.put("numsp", 0x02007);
		validEntityNames.put("nvDash", 0x022AD);
		validEntityNames.put("nvHarr", 0x02904);
		validEntityNames.put("nvdash", 0x022AC);
		validEntityNames.put("nvinfin", 0x029DE);
		validEntityNames.put("nvlArr", 0x02902);
		validEntityNames.put("nvrArr", 0x02903);
		validEntityNames.put("nwArr", 0x021D6);
		validEntityNames.put("nwarhk", 0x02923);
		validEntityNames.put("nwarr", 0x02196);
		validEntityNames.put("nwnear", 0x02927);
		validEntityNames.put("oS", 0x024C8);
		validEntityNames.put("oacute", 0x000F3);
		validEntityNames.put("oast", 0x0229B);
		validEntityNames.put("ocir", 0x0229A);
		validEntityNames.put("ocirc", 0x000F4);
		validEntityNames.put("ocy", 0x0043E);
		validEntityNames.put("odash", 0x0229D);
		validEntityNames.put("odblac", 0x00151);
		validEntityNames.put("odiv", 0x02A38);
		validEntityNames.put("odot", 0x02299);
		validEntityNames.put("odsold", 0x029BC);
		validEntityNames.put("oelig", 0x00153);
		validEntityNames.put("ofcir", 0x029BF);
		validEntityNames.put("ofr", 0x1D52C);
		validEntityNames.put("ogon", 0x002DB);
		validEntityNames.put("ograve", 0x000F2);
		validEntityNames.put("ogt", 0x029C1);
		validEntityNames.put("ohbar", 0x029B5);
		validEntityNames.put("ohm", 0x02126);
		validEntityNames.put("olarr", 0x021BA);
		validEntityNames.put("olcir", 0x029BE);
		validEntityNames.put("olcross", 0x029BB);
		validEntityNames.put("oline", 0x0203E);
		validEntityNames.put("olt", 0x029C0);
		validEntityNames.put("omacr", 0x0014D);
		validEntityNames.put("omega", 0x003C9);
		validEntityNames.put("omicron", 0x003BF);
		validEntityNames.put("omid", 0x029B6);
		validEntityNames.put("ominus", 0x02296);
		validEntityNames.put("oopf", 0x1D560);
		validEntityNames.put("opar", 0x029B7);
		validEntityNames.put("operp", 0x029B9);
		validEntityNames.put("oplus", 0x02295);
		validEntityNames.put("or", 0x02228);
		validEntityNames.put("orarr", 0x021BB);
		validEntityNames.put("ord", 0x02A5D);
		validEntityNames.put("order", 0x02134);
		validEntityNames.put("ordf", 0x000AA);
		validEntityNames.put("ordm", 0x000BA);
		validEntityNames.put("origof", 0x022B6);
		validEntityNames.put("oror", 0x02A56);
		validEntityNames.put("orslope", 0x02A57);
		validEntityNames.put("orv", 0x02A5B);
		validEntityNames.put("oslash", 0x000F8);
		validEntityNames.put("osol", 0x02298);
		validEntityNames.put("otilde", 0x000F5);
		validEntityNames.put("otimes", 0x02297);
		validEntityNames.put("otimesas", 0x02A36);
		validEntityNames.put("ouml", 0x000F6);
		validEntityNames.put("ovbar", 0x0233D);
		validEntityNames.put("par", 0x02225);
		validEntityNames.put("para", 0x000B6);
		validEntityNames.put("parsim", 0x02AF3);
		validEntityNames.put("parsl", 0x02AFD);
		validEntityNames.put("part", 0x02202);
		validEntityNames.put("pcy", 0x0043F);
		validEntityNames.put("percnt", 0x00025);
		validEntityNames.put("period", 0x0002E);
		validEntityNames.put("permil", 0x02030);
		validEntityNames.put("pertenk", 0x02031);
		validEntityNames.put("pfr", 0x1D52D);
		validEntityNames.put("phi", 0x003C6);
		validEntityNames.put("phmmat", 0x02133);
		validEntityNames.put("phone", 0x0260E);
		validEntityNames.put("pi", 0x003C0);
		validEntityNames.put("piv", 0x003D6);
		validEntityNames.put("planck", 0x0210F);
		validEntityNames.put("planckh", 0x0210E);
		validEntityNames.put("plus", 0x0002B);
		validEntityNames.put("plusacir", 0x02A23);
		validEntityNames.put("plusb", 0x0229E);
		validEntityNames.put("pluscir", 0x02A22);
		validEntityNames.put("plusdo", 0x02214);
		validEntityNames.put("plusdu", 0x02A25);
		validEntityNames.put("pluse", 0x02A72);
		validEntityNames.put("plusmn", 0x000B1);
		validEntityNames.put("plussim", 0x02A26);
		validEntityNames.put("plustwo", 0x02A27);
		validEntityNames.put("pointint", 0x02A15);
		validEntityNames.put("popf", 0x1D561);
		validEntityNames.put("pound", 0x000A3);
		validEntityNames.put("pr", 0x0227A);
		validEntityNames.put("prE", 0x02AB3);
		validEntityNames.put("prap", 0x02AB7);
		validEntityNames.put("prcue", 0x0227C);
		validEntityNames.put("pre", 0x02AAF);
		validEntityNames.put("prime", 0x02032);
		validEntityNames.put("prnE", 0x02AB5);
		validEntityNames.put("prnap", 0x02AB9);
		validEntityNames.put("prnsim", 0x022E8);
		validEntityNames.put("prod", 0x0220F);
		validEntityNames.put("profalar", 0x0232E);
		validEntityNames.put("profline", 0x02312);
		validEntityNames.put("profsurf", 0x02313);
		validEntityNames.put("prop", 0x0221D);
		validEntityNames.put("prsim", 0x0227E);
		validEntityNames.put("prurel", 0x022B0);
		validEntityNames.put("pscr", 0x1D4C5);
		validEntityNames.put("psi", 0x003C8);
		validEntityNames.put("puncsp", 0x02008);
		validEntityNames.put("qfr", 0x1D52E);
		validEntityNames.put("qint", 0x02A0C);
		validEntityNames.put("qopf", 0x1D562);
		validEntityNames.put("qprime", 0x02057);
		validEntityNames.put("qscr", 0x1D4C6);
		validEntityNames.put("quaternions", 0x0210D);
		validEntityNames.put("quatint", 0x02A16);
		validEntityNames.put("quest", 0x0003F);
		validEntityNames.put("quot", 0x00022);
		validEntityNames.put("rAarr", 0x021DB);
		validEntityNames.put("rArr", 0x021D2);
		validEntityNames.put("rAtail", 0x0291C);
		validEntityNames.put("rBarr", 0x0290F);
		validEntityNames.put("rHar", 0x02964);
		validEntityNames.put("race", 0x029DA);
		validEntityNames.put("racute", 0x00155);
		validEntityNames.put("radic", 0x0221A);
		validEntityNames.put("raemptyv", 0x029B3);
		validEntityNames.put("rang", 0x027E9);
		validEntityNames.put("rangd", 0x02992);
		validEntityNames.put("range", 0x029A5);
		validEntityNames.put("raquo", 0x000BB);
		validEntityNames.put("rarr", 0x02192);
		validEntityNames.put("rarrap", 0x02975);
		validEntityNames.put("rarrb", 0x021E5);
		validEntityNames.put("rarrbfs", 0x02920);
		validEntityNames.put("rarrc", 0x02933);
		validEntityNames.put("rarrfs", 0x0291E);
		validEntityNames.put("rarrhk", 0x021AA);
		validEntityNames.put("rarrlp", 0x021AC);
		validEntityNames.put("rarrpl", 0x02945);
		validEntityNames.put("rarrsim", 0x02974);
		validEntityNames.put("rarrtl", 0x021A3);
		validEntityNames.put("rarrw", 0x0219D);
		validEntityNames.put("ratail", 0x0291A);
		validEntityNames.put("ratio", 0x02236);
		validEntityNames.put("rationals", 0x0211A);
		validEntityNames.put("rbarr", 0x0290D);
		validEntityNames.put("rbbrk", 0x02773);
		validEntityNames.put("rbrke", 0x0298C);
		validEntityNames.put("rbrksld", 0x0298E);
		validEntityNames.put("rbrkslu", 0x02990);
		validEntityNames.put("rcaron", 0x00159);
		validEntityNames.put("rcedil", 0x00157);
		validEntityNames.put("rceil", 0x02309);
		validEntityNames.put("rcub", 0x0007D);
		validEntityNames.put("rcy", 0x00440);
		validEntityNames.put("rdca", 0x02937);
		validEntityNames.put("rdldhar", 0x02969);
		validEntityNames.put("rdquo", 0x0201D);
		validEntityNames.put("rdsh", 0x021B3);
		validEntityNames.put("real", 0x0211C);
		validEntityNames.put("reals", 0x0211D);
		validEntityNames.put("rect", 0x025AD);
		validEntityNames.put("reg", 0x000AE);
		validEntityNames.put("rfisht", 0x0297D);
		validEntityNames.put("rfloor", 0x0230B);
		validEntityNames.put("rfr", 0x1D52F);
		validEntityNames.put("rhard", 0x021C1);
		validEntityNames.put("rharu", 0x021C0);
		validEntityNames.put("rharul", 0x0296C);
		validEntityNames.put("rho", 0x003C1);
		validEntityNames.put("rhov", 0x003F1);
		validEntityNames.put("ring", 0x002DA);
		validEntityNames.put("rlarr", 0x021C4);
		validEntityNames.put("rlhar", 0x021CC);
		validEntityNames.put("rlm", 0x0200F);
		validEntityNames.put("rmoust", 0x023B1);
		validEntityNames.put("rnmid", 0x02AEE);
		validEntityNames.put("roang", 0x027ED);
		validEntityNames.put("roarr", 0x021FE);
		validEntityNames.put("robrk", 0x027E7);
		validEntityNames.put("ropar", 0x02986);
		validEntityNames.put("ropf", 0x1D563);
		validEntityNames.put("roplus", 0x02A2E);
		validEntityNames.put("rotimes", 0x02A35);
		validEntityNames.put("rpar", 0x00029);
		validEntityNames.put("rpargt", 0x02994);
		validEntityNames.put("rppolint", 0x02A12);
		validEntityNames.put("rrarr", 0x021C9);
		validEntityNames.put("rsaquo", 0x0203A);
		validEntityNames.put("rscr", 0x1D4C7);
		validEntityNames.put("rsh", 0x021B1);
		validEntityNames.put("rsqb", 0x0005D);
		validEntityNames.put("rsquo", 0x02019);
		validEntityNames.put("rthree", 0x022CC);
		validEntityNames.put("rtimes", 0x022CA);
		validEntityNames.put("rtri", 0x025B9);
		validEntityNames.put("rtrie", 0x022B5);
		validEntityNames.put("rtrif", 0x025B8);
		validEntityNames.put("rtriltri", 0x029CE);
		validEntityNames.put("ruluhar", 0x02968);
		validEntityNames.put("rx", 0x0211E);
		validEntityNames.put("sacute", 0x0015B);
		validEntityNames.put("sc", 0x0227B);
		validEntityNames.put("scE", 0x02AB4);
		validEntityNames.put("scap", 0x02AB8);
		validEntityNames.put("scaron", 0x00161);
		validEntityNames.put("sccue", 0x0227D);
		validEntityNames.put("sce", 0x02AB0);
		validEntityNames.put("scedil", 0x0015F);
		validEntityNames.put("scirc", 0x0015D);
		validEntityNames.put("scnE", 0x02AB6);
		validEntityNames.put("scnap", 0x02ABA);
		validEntityNames.put("scnsim", 0x022E9);
		validEntityNames.put("scpolint", 0x02A13);
		validEntityNames.put("scsim", 0x0227F);
		validEntityNames.put("scy", 0x00441);
		validEntityNames.put("sdot", 0x022C5);
		validEntityNames.put("sdotb", 0x022A1);
		validEntityNames.put("sdote", 0x02A66);
		validEntityNames.put("seArr", 0x021D8);
		validEntityNames.put("searhk", 0x02925);
		validEntityNames.put("searr", 0x02198);
		validEntityNames.put("sect", 0x000A7);
		validEntityNames.put("semi", 0x0003B);
		validEntityNames.put("seswar", 0x02929);
		validEntityNames.put("setmn", 0x02216);
		validEntityNames.put("sext", 0x02736);
		validEntityNames.put("sfr", 0x1D530);
		validEntityNames.put("sharp", 0x0266F);
		validEntityNames.put("shchcy", 0x00449);
		validEntityNames.put("shcy", 0x00448);
		validEntityNames.put("shy", 0x000AD);
		validEntityNames.put("sigma", 0x003C3);
		validEntityNames.put("sigmav", 0x003C2);
		validEntityNames.put("sim", 0x0223C);
		validEntityNames.put("simdot", 0x02A6A);
		validEntityNames.put("sime", 0x02243);
		validEntityNames.put("simg", 0x02A9E);
		validEntityNames.put("simgE", 0x02AA0);
		validEntityNames.put("siml", 0x02A9D);
		validEntityNames.put("simlE", 0x02A9F);
		validEntityNames.put("simne", 0x02246);
		validEntityNames.put("simplus", 0x02A24);
		validEntityNames.put("simrarr", 0x02972);
		validEntityNames.put("smashp", 0x02A33);
		validEntityNames.put("smeparsl", 0x029E4);
		validEntityNames.put("smile", 0x02323);
		validEntityNames.put("smt", 0x02AAA);
		validEntityNames.put("smte", 0x02AAC);
		validEntityNames.put("softcy", 0x0044C);
		validEntityNames.put("sol", 0x0002F);
		validEntityNames.put("solb", 0x029C4);
		validEntityNames.put("solbar", 0x0233F);
		validEntityNames.put("sopf", 0x1D564);
		validEntityNames.put("spades", 0x02660);
		validEntityNames.put("sqcap", 0x02293);
		validEntityNames.put("sqcup", 0x02294);
		validEntityNames.put("sqsub", 0x0228F);
		validEntityNames.put("sqsube", 0x02291);
		validEntityNames.put("sqsup", 0x02290);
		validEntityNames.put("sqsupe", 0x02292);
		validEntityNames.put("squ", 0x025A1);
		validEntityNames.put("squf", 0x025AA);
		validEntityNames.put("sscr", 0x1D4C8);
		validEntityNames.put("sstarf", 0x022C6);
		validEntityNames.put("star", 0x02606);
		validEntityNames.put("starf", 0x02605);
		validEntityNames.put("straightphi", 0x003D5);
		validEntityNames.put("sub", 0x02282);
		validEntityNames.put("subE", 0x02AC5);
		validEntityNames.put("subdot", 0x02ABD);
		validEntityNames.put("sube", 0x02286);
		validEntityNames.put("subedot", 0x02AC3);
		validEntityNames.put("submult", 0x02AC1);
		validEntityNames.put("subnE", 0x02ACB);
		validEntityNames.put("subne", 0x0228A);
		validEntityNames.put("subplus", 0x02ABF);
		validEntityNames.put("subrarr", 0x02979);
		validEntityNames.put("subsim", 0x02AC7);
		validEntityNames.put("subsub", 0x02AD5);
		validEntityNames.put("subsup", 0x02AD3);
		validEntityNames.put("sum", 0x02211);
		validEntityNames.put("sung", 0x0266A);
		validEntityNames.put("sup", 0x02283);
		validEntityNames.put("sup1", 0x000B9);
		validEntityNames.put("sup2", 0x000B2);
		validEntityNames.put("sup3", 0x000B3);
		validEntityNames.put("supE", 0x02AC6);
		validEntityNames.put("supdot", 0x02ABE);
		validEntityNames.put("supdsub", 0x02AD8);
		validEntityNames.put("supe", 0x02287);
		validEntityNames.put("supedot", 0x02AC4);
		validEntityNames.put("suphsub", 0x02AD7);
		validEntityNames.put("suplarr", 0x0297B);
		validEntityNames.put("supmult", 0x02AC2);
		validEntityNames.put("supnE", 0x02ACC);
		validEntityNames.put("supne", 0x0228B);
		validEntityNames.put("supplus", 0x02AC0);
		validEntityNames.put("supsim", 0x02AC8);
		validEntityNames.put("supsub", 0x02AD4);
		validEntityNames.put("supsup", 0x02AD6);
		validEntityNames.put("swArr", 0x021D9);
		validEntityNames.put("swarhk", 0x02926);
		validEntityNames.put("swarr", 0x02199);
		validEntityNames.put("swnwar", 0x0292A);
		validEntityNames.put("szlig", 0x000DF);
		validEntityNames.put("target", 0x02316);
		validEntityNames.put("tau", 0x003C4);
		validEntityNames.put("tbrk", 0x023B4);
		validEntityNames.put("tcaron", 0x00165);
		validEntityNames.put("tcedil", 0x00163);
		validEntityNames.put("tcy", 0x00442);
		validEntityNames.put("tdot", 0x020DB);
		validEntityNames.put("telrec", 0x02315);
		validEntityNames.put("tfr", 0x1D531);
		validEntityNames.put("there4", 0x02234);
		validEntityNames.put("theta", 0x003B8);
		validEntityNames.put("thetav", 0x003D1);
		validEntityNames.put("thinsp", 0x02009);
		validEntityNames.put("thorn", 0x000FE);
		validEntityNames.put("tilde", 0x002DC);
		validEntityNames.put("times", 0x000D7);
		validEntityNames.put("timesb", 0x022A0);
		validEntityNames.put("timesbar", 0x02A31);
		validEntityNames.put("timesd", 0x02A30);
		validEntityNames.put("tint", 0x0222D);
		validEntityNames.put("top", 0x022A4);
		validEntityNames.put("topbot", 0x02336);
		validEntityNames.put("topcir", 0x02AF1);
		validEntityNames.put("topf", 0x1D565);
		validEntityNames.put("topfork", 0x02ADA);
		validEntityNames.put("tprime", 0x02034);
		validEntityNames.put("trade", 0x02122);
		validEntityNames.put("tridot", 0x025EC);
		validEntityNames.put("trie", 0x0225C);
		validEntityNames.put("triminus", 0x02A3A);
		validEntityNames.put("triplus", 0x02A39);
		validEntityNames.put("trisb", 0x029CD);
		validEntityNames.put("tritime", 0x02A3B);
		validEntityNames.put("trpezium", 0x023E2);
		validEntityNames.put("tscr", 0x1D4C9);
		validEntityNames.put("tscy", 0x00446);
		validEntityNames.put("tshcy", 0x0045B);
		validEntityNames.put("tstrok", 0x00167);
		validEntityNames.put("twixt", 0x0226C);
		validEntityNames.put("uArr", 0x021D1);
		validEntityNames.put("uHar", 0x02963);
		validEntityNames.put("uacute", 0x000FA);
		validEntityNames.put("uarr", 0x02191);
		validEntityNames.put("ubrcy", 0x0045E);
		validEntityNames.put("ubreve", 0x0016D);
		validEntityNames.put("ucirc", 0x000FB);
		validEntityNames.put("ucy", 0x00443);
		validEntityNames.put("udarr", 0x021C5);
		validEntityNames.put("udblac", 0x00171);
		validEntityNames.put("udhar", 0x0296E);
		validEntityNames.put("ufisht", 0x0297E);
		validEntityNames.put("ufr", 0x1D532);
		validEntityNames.put("ugrave", 0x000F9);
		validEntityNames.put("uharl", 0x021BF);
		validEntityNames.put("uharr", 0x021BE);
		validEntityNames.put("uhblk", 0x02580);
		validEntityNames.put("ulcorn", 0x0231C);
		validEntityNames.put("ulcrop", 0x0230F);
		validEntityNames.put("ultri", 0x025F8);
		validEntityNames.put("umacr", 0x0016B);
		validEntityNames.put("uogon", 0x00173);
		validEntityNames.put("uopf", 0x1D566);
		validEntityNames.put("uplus", 0x0228E);
		validEntityNames.put("upsi", 0x003C5);
		validEntityNames.put("urcorn", 0x0231D);
		validEntityNames.put("urcrop", 0x0230E);
		validEntityNames.put("uring", 0x0016F);
		validEntityNames.put("urtri", 0x025F9);
		validEntityNames.put("uscr", 0x1D4CA);
		validEntityNames.put("utdot", 0x022F0);
		validEntityNames.put("utilde", 0x00169);
		validEntityNames.put("utri", 0x025B5);
		validEntityNames.put("utrif", 0x025B4);
		validEntityNames.put("uuarr", 0x021C8);
		validEntityNames.put("uuml", 0x000FC);
		validEntityNames.put("uwangle", 0x029A7);
		validEntityNames.put("vArr", 0x021D5);
		validEntityNames.put("vBar", 0x02AE8);
		validEntityNames.put("vBarv", 0x02AE9);
		validEntityNames.put("vDash", 0x022A8);
		validEntityNames.put("vangrt", 0x0299C);
		validEntityNames.put("varr", 0x02195);
		validEntityNames.put("vcy", 0x00432);
		validEntityNames.put("vdash", 0x022A2);
		validEntityNames.put("veebar", 0x022BB);
		validEntityNames.put("veeeq", 0x0225A);
		validEntityNames.put("vellip", 0x022EE);
		validEntityNames.put("verbar", 0x0007C);
		validEntityNames.put("vfr", 0x1D533);
		validEntityNames.put("vltri", 0x022B2);
		validEntityNames.put("vopf", 0x1D567);
		validEntityNames.put("vrtri", 0x022B3);
		validEntityNames.put("vscr", 0x1D4CB);
		validEntityNames.put("vzigzag", 0x0299A);
		validEntityNames.put("wcirc", 0x00175);
		validEntityNames.put("wedbar", 0x02A5F);
		validEntityNames.put("wedgeq", 0x02259);
		validEntityNames.put("weierp", 0x02118);
		validEntityNames.put("wfr", 0x1D534);
		validEntityNames.put("wopf", 0x1D568);
		validEntityNames.put("wreath", 0x02240);
		validEntityNames.put("wscr", 0x1D4CC);
		validEntityNames.put("xcap", 0x022C2);
		validEntityNames.put("xcirc", 0x025EF);
		validEntityNames.put("xcup", 0x022C3);
		validEntityNames.put("xdtri", 0x025BD);
		validEntityNames.put("xfr", 0x1D535);
		validEntityNames.put("xhArr", 0x027FA);
		validEntityNames.put("xharr", 0x027F7);
		validEntityNames.put("xi", 0x003BE);
		validEntityNames.put("xlArr", 0x027F8);
		validEntityNames.put("xlarr", 0x027F5);
		validEntityNames.put("xmap", 0x027FC);
		validEntityNames.put("xnis", 0x022FB);
		validEntityNames.put("xodot", 0x02A00);
		validEntityNames.put("xopf", 0x1D569);
		validEntityNames.put("xoplus", 0x02A01);
		validEntityNames.put("xotime", 0x02A02);
		validEntityNames.put("xrArr", 0x027F9);
		validEntityNames.put("xrarr", 0x027F6);
		validEntityNames.put("xscr", 0x1D4CD);
		validEntityNames.put("xsqcup", 0x02A06);
		validEntityNames.put("xuplus", 0x02A04);
		validEntityNames.put("xutri", 0x025B3);
		validEntityNames.put("xvee", 0x022C1);
		validEntityNames.put("xwedge", 0x022C0);
		validEntityNames.put("yacute", 0x000FD);
		validEntityNames.put("yacy", 0x0044F);
		validEntityNames.put("ycirc", 0x00177);
		validEntityNames.put("ycy", 0x0044B);
		validEntityNames.put("yen", 0x000A5);
		validEntityNames.put("yfr", 0x1D536);
		validEntityNames.put("yicy", 0x00457);
		validEntityNames.put("yopf", 0x1D56A);
		validEntityNames.put("yscr", 0x1D4CE);
		validEntityNames.put("yucy", 0x0044E);
		validEntityNames.put("yuml", 0x000FF);
		validEntityNames.put("zacute", 0x0017A);
		validEntityNames.put("zcaron", 0x0017E);
		validEntityNames.put("zcy", 0x00437);
		validEntityNames.put("zdot", 0x0017C);
		validEntityNames.put("zeta", 0x003B6);
		validEntityNames.put("zfr", 0x1D537);
		validEntityNames.put("zhcy", 0x00436);
		validEntityNames.put("zigrarr", 0x021DD);
		validEntityNames.put("zopf", 0x1D56B);
		validEntityNames.put("zscr", 0x1D4CF);
		validEntityNames.put("zwj", 0x0200D);
		validEntityNames.put("zwnj", 0x0200C);

		VALID_ENTITY_NAMES = Collections.unmodifiableMap(validEntityNames);
	}

	/**
	 * Non-instantiable
	 */
	private XmlUtil() {
	}

	private static final class ExtendedEntityReplacingXmlResolver implements XMLResolver {
		@Override
		public Object resolveEntity(String thePublicID, String theSystemID, String theBaseURI, String theNamespace) {
			if (thePublicID == null && theSystemID == null) {
				if (theNamespace != null && VALID_ENTITY_NAMES.containsKey(theNamespace)) {
					return new String(Character.toChars(VALID_ENTITY_NAMES.get(theNamespace)));
				}
			}

			return null;
		}
	}

	public static class MyEscaper implements EscapingWriterFactory {

		@Override
		public Writer createEscapingWriterFor(OutputStream theOut, String theEnc) throws UnsupportedEncodingException {
			return createEscapingWriterFor(new OutputStreamWriter(theOut, theEnc), theEnc);
		}

		@Override
		public Writer createEscapingWriterFor(final Writer theW, String theEnc) {
			return new Writer() {

				@Override
				public void close() throws IOException {
					theW.close();
				}

				@Override
				public void flush() throws IOException {
					theW.flush();
				}

				@Override
				public void write(char[] theCbuf, int theOff, int theLen) throws IOException {
					boolean hasEscapable = false;
					for (int i = 0; i < theLen && !hasEscapable; i++) {
						char nextChar = theCbuf[i + theOff];
						switch (nextChar) {
							case '<':
							case '>':
							case '"':
							case '&':
								hasEscapable = true;
								break;
							default:
								break;
						}
					}

					if (!hasEscapable) {
						theW.write(theCbuf, theOff, theLen);
						return;
					}

					String escaped = StringEscapeUtils.escapeXml10(new String(theCbuf, theOff, theLen));
					theW.write(escaped.toCharArray());
				}
			};
		}

	}

	private static XMLOutputFactory createOutputFactory() throws FactoryConfigurationError {
		try {
			// Detect if we're running with the Android lib, and force repackaged Woodstox to be used
			Class.forName("ca.uhn.fhir.repackage.javax.xml.stream.XMLOutputFactory");
			System.setProperty("javax.xml.stream.XMLOutputFactory", "com.ctc.wstx.stax.WstxOutputFactory");
		} catch (ClassNotFoundException e) {
			// ok
		}

		XMLOutputFactory outputFactory = newOutputFactory();

		if (!ourHaveLoggedStaxImplementation) {
			logStaxImplementation(outputFactory.getClass());
		}

		/*
		 * Note that these properties are Woodstox specific and they cause a crash in environments where SJSXP is
		 * being used (e.g. glassfish) so we don't set them there.
		 */
		try {
			Class.forName("com.ctc.wstx.stax.WstxOutputFactory");
			if (outputFactory instanceof WstxOutputFactory) {
//				((WstxOutputFactory)outputFactory).getConfig().setAttrValueEscaperFactory(new MyEscaper());
				outputFactory.setProperty(XMLOutputFactory2.P_TEXT_ESCAPER, new MyEscaper());
			}
		} catch (ClassNotFoundException e) {
			ourLog.debug("WstxOutputFactory (Woodstox) not found on classpath");
		}
		return outputFactory;
	}

	private static XMLEventWriter createXmlFragmentWriter(Writer theWriter) throws FactoryConfigurationError, XMLStreamException {
		XMLOutputFactory outputFactory = getOrCreateFragmentOutputFactory();
		return outputFactory.createXMLEventWriter(theWriter);
	}

	public static XMLEventReader createXmlReader(Reader reader) throws FactoryConfigurationError, XMLStreamException {
		throwUnitTestExceptionIfConfiguredToDoSo();

		XMLInputFactory inputFactory = getOrCreateInputFactory();

		// Now.. create the reader and return it
		return inputFactory.createXMLEventReader(reader);
	}

	public static XMLStreamWriter createXmlStreamWriter(Writer theWriter) throws FactoryConfigurationError, XMLStreamException {
		throwUnitTestExceptionIfConfiguredToDoSo();

		XMLOutputFactory outputFactory = getOrCreateOutputFactory();
		return outputFactory.createXMLStreamWriter(theWriter);
	}

	public static XMLEventWriter createXmlWriter(Writer theWriter) throws FactoryConfigurationError, XMLStreamException {
		XMLOutputFactory outputFactory = getOrCreateOutputFactory();
		return outputFactory.createXMLEventWriter(theWriter);
	}

	/**
	 * Encode a set of StAX events into a String
	 */
	public static String encode(List<XMLEvent> theEvents) {
		try {
			StringWriter w = new StringWriter();
			XMLEventWriter ew = XmlUtil.createXmlFragmentWriter(w);

			for (XMLEvent next : theEvents) {
				if (next.isCharacters()) {
					ew.add(next);
				} else {
					ew.add(next);
				}
			}
			ew.close();
			return w.toString();
		} catch (XMLStreamException e) {
			throw new DataFormatException(Msg.code(1751) + "Problem with the contained XML events", e);
		} catch (FactoryConfigurationError e) {
			throw new ConfigurationException(Msg.code(1752) + e);
		}
	}

	private static XMLOutputFactory getOrCreateFragmentOutputFactory() throws FactoryConfigurationError {
		XMLOutputFactory retVal = ourFragmentOutputFactory;
		if (retVal == null) {
			retVal = createOutputFactory();
			retVal.setProperty(XMLOutputFactory.IS_REPAIRING_NAMESPACES, Boolean.TRUE);
			ourFragmentOutputFactory = retVal;
			return retVal;
		}
		return retVal;
	}

	private static XMLInputFactory getOrCreateInputFactory() throws FactoryConfigurationError {
		if (ourInputFactory == null) {

			try {
				// Detect if we're running with the Android lib, and force repackaged Woodstox to be used
				Class.forName("ca.uhn.fhir.repackage.javax.xml.stream.XMLInputFactory");
				System.setProperty("javax.xml.stream.XMLInputFactory", "com.ctc.wstx.stax.WstxInputFactory");
			} catch (ClassNotFoundException e) {
				// ok
			}

			XMLInputFactory inputFactory = newInputFactory();

			if (!ourHaveLoggedStaxImplementation) {
				logStaxImplementation(inputFactory.getClass());
			}

			/*
			 * These two properties disable external entity processing, which can
			 * be a security vulnerability.
			 *
			 * See https://github.com/hapifhir/hapi-fhir/issues/339
			 * https://www.owasp.org/index.php/XML_External_Entity_%28XXE%29_Processing
			 */
			inputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false); // This disables DTDs entirely for that factory
			inputFactory.setProperty("javax.xml.stream.isSupportingExternalEntities", false); // disable external entities


			/*
			 * In the following few lines, you can uncomment the first and comment the second to disable automatic
			 * parsing of extended entities, e.g. &sect;
			 *
			 * Note that these properties are Woodstox specific and they cause a crash in environments where SJSXP is
			 * being used (e.g. glassfish) so we don't set them there.
			 */
			try {
				Class.forName("com.ctc.wstx.stax.WstxInputFactory");
				boolean isWoodstox = inputFactory instanceof com.ctc.wstx.stax.WstxInputFactory;
				if (!isWoodstox) {
					// Check if implementation is woodstox by property since instanceof check does not work if running in JBoss
					try {
						isWoodstox = inputFactory.getProperty("org.codehaus.stax2.implVersion") != null;
					} catch (Exception e) {
						// ignore
					}
				}
				if (isWoodstox) {
					// inputFactory.setProperty(WstxInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
					inputFactory.setProperty(WstxInputProperties.P_UNDECLARED_ENTITY_RESOLVER, XML_RESOLVER);
					try {
						inputFactory.setProperty(WstxInputProperties.P_MAX_ATTRIBUTE_SIZE, "100000000");
					} catch (IllegalArgumentException e) {
						// ignore
					}
				}
			} catch (ClassNotFoundException e) {
				ourLog.debug("WstxOutputFactory (Woodstox) not found on classpath");
			}
			ourInputFactory = inputFactory;
		}
		return ourInputFactory;
	}

	private static XMLOutputFactory getOrCreateOutputFactory() throws FactoryConfigurationError {
		if (ourOutputFactory == null) {
			ourOutputFactory = createOutputFactory();
		}
		return ourOutputFactory;
	}

	private static void logStaxImplementation(Class<?> theClass) {
		IDependencyLog logger = DependencyLogFactory.createJarLogger();
		if (logger != null) {
			logger.logStaxImplementation(theClass);
		}
		ourHaveLoggedStaxImplementation = true;
	}

	static XMLInputFactory newInputFactory() throws FactoryConfigurationError {
		XMLInputFactory inputFactory;
		try {
			inputFactory = XMLInputFactory.newInstance();
			if (inputFactory.isPropertySupported(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES)) {
				inputFactory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, true);
			}
			throwUnitTestExceptionIfConfiguredToDoSo();
		} catch (Throwable e) {
			throw new ConfigurationException(Msg.code(1753) + "Unable to initialize StAX - XML processing is disabled", e);
		}
		return inputFactory;
	}

	static XMLOutputFactory newOutputFactory() throws FactoryConfigurationError {
		XMLOutputFactory outputFactory;
		try {
			outputFactory = XMLOutputFactory.newInstance();
			throwUnitTestExceptionIfConfiguredToDoSo();
		} catch (Throwable e) {
			throw new ConfigurationException(Msg.code(1754) + "Unable to initialize StAX - XML processing is disabled", e);
		}
		return outputFactory;
	}

	/**
	 * Parses an XML string into a set of StAX events
	 */
	public static List<XMLEvent> parse(String theValue) {
		if (isBlank(theValue)) {
			return Collections.emptyList();
		}

		String val = theValue.trim();
		if (!val.startsWith("<")) {
			val = XhtmlDt.DIV_OPEN_FIRST + val + "</div>";
		}
		boolean hasProcessingInstruction = val.startsWith("<?");
		if (hasProcessingInstruction && val.endsWith("?>")) {
			return null;
		}


		try {
			ArrayList<XMLEvent> value = new ArrayList<>();
			StringReader reader = new StringReader(val);
			XMLEventReader er = XmlUtil.createXmlReader(reader);
			boolean first = true;
			while (er.hasNext()) {
				XMLEvent next = er.nextEvent();
				if (first) {
					first = false;
					continue;
				}
				if (er.hasNext()) {
					// don't add the last event
					value.add(next);
				}
			}
			return value;

		} catch (XMLStreamException e) {
			throw new DataFormatException(Msg.code(1755) + "String does not appear to be valid XML/XHTML (error is \"" + e.getMessage() + "\"): " + theValue, e);
		} catch (FactoryConfigurationError e) {
			throw new ConfigurationException(Msg.code(1756) + e);
		}
	}

	/**
	 * FOR UNIT TESTS ONLY - Throw this exception for the next operation
	 */
	static void setThrowExceptionForUnitTest(Throwable theException) {
		ourNextException = theException;
	}

	private static void throwUnitTestExceptionIfConfiguredToDoSo() throws FactoryConfigurationError, XMLStreamException {
		if (ourNextException != null) {
			if (ourNextException instanceof javax.xml.stream.FactoryConfigurationError) {
				throw ((javax.xml.stream.FactoryConfigurationError) ourNextException);
			}
			throw (XMLStreamException) ourNextException;
		}
	}

	public static Document parseDocument(String theInput) throws IOException, SAXException {
		StringReader reader = new StringReader(theInput);
		return parseDocument(reader);
	}

	public static Document parseDocument(Reader reader) throws SAXException, IOException {
		return parseDocument(reader, true);
	}

	public static Document parseDocument(Reader theReader, boolean theNamespaceAware) throws SAXException, IOException {
		DocumentBuilder builder;
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			docBuilderFactory.setNamespaceAware(theNamespaceAware);
			docBuilderFactory.setXIncludeAware(false);
			docBuilderFactory.setExpandEntityReferences(false);
			try {
				docBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
				docBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
				docBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
				docBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
				docBuilderFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
				throwUnitTestExceptionIfConfiguredToDoSo();
			} catch (Exception e) {
				ourLog.warn("Failed to set feature on XML parser: " + e.toString());
			}

			builder = docBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new ConfigurationException(Msg.code(1757) + e);
		}

		InputSource src = new InputSource(theReader);
		return builder.parse(src);
	}


	public static List<Element> getChildrenByTagName(Element theParent, String theName) {
		List<Element> nodeList = new ArrayList<Element>();
		for (Node child = theParent.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (child.getNodeType() == Node.ELEMENT_NODE && theName.equals(child.getNodeName())) {
				nodeList.add((Element) child);
			}
		}

		return nodeList;
	}


	public static String encodeDocument(Node theElement) throws TransformerException {
		return encodeDocument(theElement, false);
	}

	public static String encodeDocument(Node theElement, boolean theIndent) throws TransformerException {
		TransformerFactory transFactory = TransformerFactory.newInstance();
		Transformer transformer = transFactory.newTransformer();
		StringWriter buffer = new StringWriter();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		if (theIndent) {
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		}
		transformer.transform(new DOMSource(theElement), new StreamResult(buffer));
		return buffer.toString();
	}
}
