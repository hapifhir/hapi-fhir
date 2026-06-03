package ca.uhn.fhir.jpa.batch2.jobs.term.icd.icd10cm;

import ca.uhn.fhir.batch2.api.AttachmentDetails;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseImportTerminologyFileCsvStep;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseImportTerminologyFileStep;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyJobParameters;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyMetadataAttachmentJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincJobAppCtx;
import ca.uhn.fhir.util.XmlUtil;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.CodeSystem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @see ImportLoincJobAppCtx#importLoincStep2Concepts()
 */
public class ImportIcd10CmStep2HandleConcepts
		extends BaseImportTerminologyFileStep<ImportTerminologyJobParameters, BaseImportTerminologyFileStep.MyBaseContext> {

	private static final String SEVEN_CHR_DEF = "sevenChrDef";
	private static final String EXTENSION = "extension";
	private static final String DIAG = "diag";
	private static final String NAME = "name";
	private static final String DESC = "desc";

	@Nonnull
	@Override
	public List<BaseImportTerminologyFileCsvStep.LoincFileNameSpecification> getFilesToProcess(
			StepExecutionDetails<ImportTerminologyJobParameters, ?> theStepExecutionDetails) {
		return List.of(new BaseImportTerminologyFileCsvStep.LoincFileNameSpecification(
				FileHandlingType.XML, t -> Pattern.compile("icd10.*.xml$", Pattern.CASE_INSENSITIVE)
						.matcher(t)
						.find()));
	}

	@Override
	protected void processAttachment(
			@Nonnull StepExecutionDetails<ImportTerminologyJobParameters, TerminologyFileSetJson> theStepExecutionDetails,
			ImportTerminologyMetadataAttachmentJson theJobMetadata,
			MyBaseContext theContext,
			AttachmentDetails theAttachment,
			ImportTerminologyJobParameters theJobParameters,
			CodeSystem theCodeSystemToPopulate,
			TerminologyFileSetJson theData,
			String theSourceFilename) {

		InputStreamReader reader = new InputStreamReader(theAttachment.getInputStream(), StandardCharsets.UTF_8);
		Document document;
		try {
			document = XmlUtil.parseDocument(reader, false, true);
		} catch (SAXException | IOException theE) {
			// FIXME: add code
			throw new RuntimeException(theE);
		}

		Element documentElement = document.getDocumentElement();

		// Extract version: Should only be 1 tag
		for (Element nextVersion : XmlUtil.getChildrenByTagName(documentElement, "version")) {
			String versionId = nextVersion.getTextContent();
			// FIXME: validate version
			//			if (isNotBlank(versionId)) {
			//				myCodeSystemVersion.setCodeSystemVersionId(versionId);
			//			}
		}

		// Extract Diags (codes)
		for (Element nextChapter : XmlUtil.getChildrenByTagName(documentElement, "chapter")) {
			for (Element nextSection : XmlUtil.getChildrenByTagName(nextChapter, "section")) {
				for (Element nextDiag : XmlUtil.getChildrenByTagName(nextSection, "diag")) {
					extractCode(nextDiag, null, null, theContext);
				}
			}
		}
	}

	private void extractCode(
			Element theDiagElement,
			CodeSystem.ConceptDefinitionComponent theParentConcept,
			List<Element> theParentSevenChrDef,
			MyBaseContext theContext) {
		String code = theDiagElement.getElementsByTagName(NAME).item(0).getTextContent();
		String display = theDiagElement.getElementsByTagName(DESC).item(0).getTextContent();
		List<Element> mySevenChrDef = null;
		CodeSystem.ConceptDefinitionComponent concept;
		if (theParentConcept == null) {
			concept = getOrAddConcept(theContext, code);
		} else {
			concept = theParentConcept.addConcept();
			concept.setCode(code);
		}

		concept.setDisplay(display);

		// Check for seventh character definitions. If none exist at this level,
		// use seventh character definitions inherited from parent level.
		if (!XmlUtil.getChildrenByTagName(theDiagElement, SEVEN_CHR_DEF).isEmpty()) {
			mySevenChrDef = XmlUtil.getChildrenByTagName(theDiagElement, SEVEN_CHR_DEF);
		} else if (theParentSevenChrDef != null) {
			mySevenChrDef = theParentSevenChrDef.stream().toList();
		}

		// If this concept has no children, apply the seventh character definitions.
		// Otherwise create the children.
		if (mySevenChrDef != null
				&& XmlUtil.getChildrenByTagName(theDiagElement, DIAG).isEmpty()) {
			if (theParentConcept == null) {
				// This is a root concept. Add the extensions as children of the current concept.
				extractExtension(mySevenChrDef, theDiagElement, concept, true);
			} else {
				// This is a child concept. Add the extensions as siblings of the current concept
				extractExtension(mySevenChrDef, theDiagElement, theParentConcept, false);
			}
		} else {
			for (Element nextChildDiag : XmlUtil.getChildrenByTagName(theDiagElement, DIAG)) {
				extractCode(nextChildDiag, concept, mySevenChrDef, theContext);
			}
		}
	}

	private void extractExtension(
			List<Element> theSevenChrDefElement,
			Element theChildDiag,
			CodeSystem.ConceptDefinitionComponent theParentConcept,
			boolean isRootCode) {
		for (Element nextChrNote : theSevenChrDefElement) {
			for (Element nextExtension : XmlUtil.getChildrenByTagName(nextChrNote, EXTENSION)) {
				String baseCode =
						theChildDiag.getElementsByTagName(NAME).item(0).getTextContent();
				if (isRootCode) {
					baseCode = baseCode + ".";
				}
				String sevenChar = nextExtension.getAttributes().item(0).getNodeValue();
				String baseDef = theChildDiag.getElementsByTagName(DESC).item(0).getTextContent();
				String sevenCharDef = nextExtension.getTextContent();

				CodeSystem.ConceptDefinitionComponent concept = theParentConcept.addConcept();

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
		StringBuilder code = new StringBuilder(theBaseCode);
		code.append(placeholder.repeat(Math.max(0, 7 - code.length())));
		code.append(theSevenChar);
		return code.toString();
	}

	@Override
	protected MyBaseContext newContextObject(
			StepExecutionDetails<ImportTerminologyJobParameters, TerminologyFileSetJson> theStepExecutionDetails) {
		return new BaseImportTerminologyFileStep.MyBaseContext();
	}
}
