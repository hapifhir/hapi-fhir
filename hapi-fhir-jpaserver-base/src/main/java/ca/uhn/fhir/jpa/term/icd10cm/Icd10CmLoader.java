package ca.uhn.fhir.jpa.term.icd10cm;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

	/**
	 * Constructor
	 */
	public Icd10CmLoader(TermCodeSystemVersion theCodeSystemVersion) {
		myCodeSystemVersion = theCodeSystemVersion;
	}


	public void load(Reader theReader) throws IOException, SAXException {
		myConceptCount = 0;

		Document document = XmlUtil.parseDocument(theReader, false);
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
		String code = theDiagElement.getElementsByTagName("name").item(0).getTextContent();
		String display = theDiagElement.getElementsByTagName("desc").item(0).getTextContent();

		TermConcept concept;
		if (theParentConcept == null) {
			concept = myCodeSystemVersion.addConcept();
		} else {
			concept = theParentConcept.addChild(TermConceptParentChildLink.RelationshipTypeEnum.ISA);
		}

		concept.setCode(code);
		concept.setDisplay(display);

		for (Element nextChildDiag : XmlUtil.getChildrenByTagName(theDiagElement, "diag")) {
			extractCode(nextChildDiag, concept);
		}

		myConceptCount++;
	}


	public int getConceptCount() {
		return myConceptCount;
	}

}
