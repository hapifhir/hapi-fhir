/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.batch2.jobs.term.icd.icd10;

import ca.uhn.fhir.batch2.api.AttachmentDetails;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseImportTerminologyFileCsvStep;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseImportTerminologyFileStep;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyJobParameters;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyMetadataAttachmentJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyXmlUtil;
import ca.uhn.fhir.jpa.batch2.jobs.term.icd.ImportIcdJobAppCtx;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

import static ca.uhn.fhir.util.XmlUtil.getChildrenByTagName;

/**
 * @see ImportIcdJobAppCtx#importIcd10Step2Concepts()
 */
public class ImportIcd10Step2HandleConcepts
	extends BaseImportTerminologyFileStep<
	ImportTerminologyJobParameters, BaseImportTerminologyFileStep.MyBaseContext> {
	public static final Pattern ICD10_XML_FILE_PATTERN = Pattern.compile("icd10.*.xml$", Pattern.CASE_INSENSITIVE);
	public static final String ICD10_XML_FILENAME = "icd10.xml";
	private static final Logger ourLog = LoggerFactory.getLogger(ImportIcd10Step2HandleConcepts.class);
	private static final String EXPECTED_ROOT_NODE = "ClaML";

	@Nonnull
	@Override
	public List<BaseImportTerminologyFileCsvStep.LoincFileNameSpecification> getFilesToProcess(
		StepExecutionDetails<ImportTerminologyJobParameters, ?> theStepExecutionDetails) {
		return List.of(new BaseImportTerminologyFileCsvStep.LoincFileNameSpecification(
			FileHandlingType.XML, t -> ICD10_XML_FILE_PATTERN.matcher(t).find()));
	}

	@Override
	protected void processAttachment(
		@Nonnull
		StepExecutionDetails<ImportTerminologyJobParameters, TerminologyFileSetJson>
			theStepExecutionDetails,
		ImportTerminologyMetadataAttachmentJson theJobMetadata,
		MyBaseContext theContext,
		AttachmentDetails theAttachment,
		ImportTerminologyJobParameters theJobParameters,
		CodeSystem theCodeSystemToPopulate,
		TerminologyFileSetJson theData,
		String theSourceFilename) {

		Element documentElement = TerminologyXmlUtil.parseXmlDocument(theAttachment, theSourceFilename);
		Set<String> rootConcepts = new HashSet<>();

		String rootNodeName = documentElement.getTagName();
		if (!EXPECTED_ROOT_NODE.equals(rootNodeName)) {
			// FIXME: add test
			throw new IllegalStateException(Msg.code(2969) + "Unexpected root node in ICD-10 document: " + rootNodeName);
		}

		for (Element title : getChildrenByTagName(documentElement, "Title")) {
			String name = title.getAttribute("name");
			if (!name.isEmpty()) {
				theCodeSystemToPopulate.setName(name);
				theCodeSystemToPopulate.setTitle(name);
			}

			theCodeSystemToPopulate.setDescription(title.getTextContent());
		}

		for (Element aClass : getChildrenByTagName(documentElement, "Class")) {
			String code = aClass.getAttribute("code");
			if (code.isEmpty()) {
				continue;
			}

			CodeSystem.ConceptDefinitionComponent termConcept = getOrAddConcept(theContext, code);

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
							termConcept.addProperty().setCode(kind).setValue(new StringType(textContent));
						}
					}
				}
			}

			List<Element> superClassElements = getChildrenByTagName(aClass, "SuperClass");
			for (Element superClass : superClassElements) {
				String parentCode = superClass.getAttribute("code");
				CodeSystem.ConceptDefinitionComponent parent = getOrAddConcept(theContext, parentCode);
				parent.addConcept(termConcept);

				ourLog.atInfo()
					.setMessage("ICD-10 code[{}] has parent[{}]")
					.addArgument(code)
					.addArgument(parentCode)
					.log();

			}

			if (superClassElements.isEmpty()) {
				theCodeSystemToPopulate.addConcept(termConcept);
				rootConcepts.add(code);
			} else {
				if (rootConcepts.contains(code)) {
					if (theCodeSystemToPopulate.getConcept().removeIf(t -> t.getCode().equals(code))) {
						rootConcepts.remove(code);
					}
				}
			}

		}
	}

	@Override
	protected MyBaseContext newContextObject(
		StepExecutionDetails<ImportTerminologyJobParameters, TerminologyFileSetJson> theStepExecutionDetails) {
		return new MyBaseContext();
	}
}
