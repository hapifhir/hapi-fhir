/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.term.icd10cm;

import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.util.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.Reader;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class Icd10CmLoader {

	private final TermCodeSystemVersion myCodeSystemVersion;
	private int myConceptCount;
	private static final String SEVEN_CHR_DEF = "sevenChrDef";
	private static final String VERSION = "version";
	private static final String EXTENSION = "extension";
	private static final String CHAPTER = "chapter";
	private static final String SECTION = "section";
	private static final String DIAG = "diag";
	private static final String NAME = "name";
	private static final String DESC = "desc";

	/**
	 * Constructor
	 */
	public Icd10CmLoader(TermCodeSystemVersion theCodeSystemVersion) {
		myCodeSystemVersion = theCodeSystemVersion;
	}

	public void load(Reader theReader) throws IOException, SAXException {
		myConceptCount = 0;

		Document document = XmlUtil.parseDocument(theReader, false, false);
		Element documentElement = document.getDocumentElement();

		// Extract version: Should only be 1 tag
		for (Element nextVersion : XmlUtil.getChildrenByTagName(documentElement, "version")) {
			String versionId = nextVersion.getTextContent();
			if (isNotBlank(versionId)) {
				myCodeSystemVersion.setCodeSystemVersionId(versionId);
			}
		}

		// Extract Diags (codes)
		for (Element nextChapter : XmlUtil.getChildrenByTagName(documentElement, "chapter")) {
			for (Element nextSection : XmlUtil.getChildrenByTagName(nextChapter, "section")) {
				for (Element nextDiag : XmlUtil.getChildrenByTagName(nextSection, "diag")) {
					extractCode(nextDiag, null);
				}
			}
		}
	}

	private void extractCode(Element theDiagElement, TermConcept theParentConcept) {
		String code = theDiagElement.getElementsByTagName(NAME).item(0).getTextContent();
		String display = theDiagElement.getElementsByTagName(DESC).item(0).getTextContent();

		TermConcept concept;
		if (theParentConcept == null) {
			concept = myCodeSystemVersion.addConcept();
		} else {
			concept = theParentConcept.addChild(TermConceptParentChildLink.RelationshipTypeEnum.ISA);
		}

		concept.setCode(code);
		concept.setDisplay(display);

		for (Element nextChildDiag : XmlUtil.getChildrenByTagName(theDiagElement, DIAG)) {
			extractCode(nextChildDiag, concept);
			if (XmlUtil.getChildrenByTagName(theDiagElement, SEVEN_CHR_DEF).size() != 0) {
				extractExtension(theDiagElement, nextChildDiag, concept);
			}
		}

		myConceptCount++;
	}

	private void extractExtension(Element theDiagElement, Element theChildDiag, TermConcept theParentConcept) {
		for (Element nextChrNote : XmlUtil.getChildrenByTagName(theDiagElement, SEVEN_CHR_DEF)) {
			for (Element nextExtension : XmlUtil.getChildrenByTagName(nextChrNote, EXTENSION)) {
				String baseCode =
						theChildDiag.getElementsByTagName(NAME).item(0).getTextContent();
				String sevenChar = nextExtension.getAttributes().item(0).getNodeValue();
				String baseDef = theChildDiag.getElementsByTagName(DESC).item(0).getTextContent();
				String sevenCharDef = nextExtension.getTextContent();

				TermConcept concept = theParentConcept.addChild(TermConceptParentChildLink.RelationshipTypeEnum.ISA);

				concept.setCode(getExtendedCode(baseCode, sevenChar));
				concept.setDisplay(getExtendedDisplay(baseDef, sevenCharDef));
			}
		}
	}

	private String getExtendedDisplay(String theBaseDef, String theSevenCharDef) {
		return theBaseDef + ", " + theSevenCharDef;
	}

	/**
	 * The Seventh Character must be placed at the seventh position of the code
	 * If the base code only has five characters, "X" will be used as a placeholder
	 */
	private String getExtendedCode(String theBaseCode, String theSevenChar) {
		String placeholder = "X";
		String code = theBaseCode;
		for (int i = code.length(); i < 7; i++) {
			code += placeholder;
		}
		code += theSevenChar;
		return code;
	}

	public int getConceptCount() {
		return myConceptCount;
	}
}
