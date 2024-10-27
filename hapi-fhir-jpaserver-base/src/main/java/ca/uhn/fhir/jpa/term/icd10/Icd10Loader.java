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
package ca.uhn.fhir.jpa.term.icd10;

import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import org.hl7.fhir.r4.model.CodeSystem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static ca.uhn.fhir.util.XmlUtil.getChildrenByTagName;
import static ca.uhn.fhir.util.XmlUtil.parseDocument;

public class Icd10Loader {

	public static final String EXPECTED_ROOT_NODE = "ClaML";
	private final CodeSystem codeSystem;
	private final TermCodeSystemVersion codeSystemVersion;
	private int conceptCount = 0;

	public Icd10Loader(CodeSystem codeSystem, TermCodeSystemVersion codeSystemVersion) {
		this.codeSystem = codeSystem;
		this.codeSystemVersion = codeSystemVersion;
	}

	public void load(Reader reader) throws IOException, SAXException {
		Document document = parseDocument(reader, false, true);
		Element documentElement = document.getDocumentElement();

		String rootNodeName = documentElement.getTagName();
		if (!EXPECTED_ROOT_NODE.equals(rootNodeName)) {
			return;
		}

		for (Element title : getChildrenByTagName(documentElement, "Title")) {
			String name = title.getAttribute("name");
			if (!name.isEmpty()) {
				codeSystem.setName(name);
				codeSystem.setTitle(name);
			}
			String version = title.getAttribute("version");
			if (!version.isEmpty()) {
				codeSystemVersion.setCodeSystemVersionId(version);
			}
			codeSystem.setDescription(title.getTextContent());
		}

		Map<String, TermConcept> conceptMap = new HashMap<>();
		for (Element aClass : getChildrenByTagName(documentElement, "Class")) {
			String code = aClass.getAttribute("code");
			if (code.isEmpty()) {
				continue;
			}

			boolean rootConcept = getChildrenByTagName(aClass, "SuperClass").isEmpty();
			TermConcept termConcept = rootConcept ? codeSystemVersion.addConcept() : new TermConcept();
			termConcept.setCode(code);

			// Preferred label and other properties
			for (Element rubric : getChildrenByTagName(aClass, "Rubric")) {
				String kind = rubric.getAttribute("kind");
				Optional<Element> firstLabel =
						getChildrenByTagName(rubric, "Label").stream().findFirst();
				if (firstLabel.isPresent()) {
					String textContent = firstLabel.get().getTextContent();
					if (textContent != null && !textContent.isEmpty()) {
						textContent =
								textContent.replace("\n", "").replace("\r", "").replace("\t", "");
						if (kind.equals("preferred")) {
							termConcept.setDisplay(textContent);
						} else {
							termConcept.addPropertyString(kind, textContent);
						}
					}
				}
			}

			for (Element superClass : getChildrenByTagName(aClass, "SuperClass")) {
				TermConcept parent = conceptMap.get(superClass.getAttribute("code"));
				if (parent != null) {
					parent.addChild(termConcept, TermConceptParentChildLink.RelationshipTypeEnum.ISA);
				}
			}

			conceptMap.put(code, termConcept);
		}

		conceptCount = conceptMap.size();
	}

	public int getConceptCount() {
		return conceptCount;
	}
}
