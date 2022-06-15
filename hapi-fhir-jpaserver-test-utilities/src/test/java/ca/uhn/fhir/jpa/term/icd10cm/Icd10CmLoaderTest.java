package ca.uhn.fhir.jpa.term.icd10cm;

import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.util.ClasspathUtil;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Icd10CmLoaderTest {
	StringReader reader = new StringReader(ClasspathUtil.loadResource("icd/icd10cm_tabular_2021.xml"));
	TermCodeSystemVersion codeSystemVersion = new TermCodeSystemVersion();
	Icd10CmLoader loader = new Icd10CmLoader(codeSystemVersion);
	@Test
	public void testLoadIcd10CmCheckVersion() throws IOException, SAXException {
		loader.load(reader);

		assertEquals("2021", codeSystemVersion.getCodeSystemVersionId());
	}

	@Test
	public void testLoadIcd10CmCheckRootConcepts() throws IOException, SAXException {
		loader.load(reader);

		List<TermConcept> rootConcepts = new ArrayList<>(codeSystemVersion.getConcepts());
		assertEquals(4, rootConcepts.size());
		assertEquals("A00", rootConcepts.get(0).getCode());
		assertEquals("Cholera", rootConcepts.get(0).getDisplay());
		List<String> conceptNames = rootConcepts.stream().map(t -> t.getCode()).collect(Collectors.toList());
		assertThat(conceptNames.toString(), conceptNames, Matchers.contains("A00", "A01","H40","R40"));
	}

	@Test
	public void testLoadIcd10CmCheckChildCode() throws IOException, SAXException {
		loader.load(reader);

		List<TermConcept> rootConcepts = new ArrayList<>(codeSystemVersion.getConcepts());
		assertEquals(3, rootConcepts.get(0).getChildCodes().size());
		TermConcept firstChildCode = rootConcepts.get(0).getChildCodes().get(0);
		assertEquals("A00.0", firstChildCode.getCode());
		assertEquals("Cholera due to Vibrio cholerae 01, biovar cholerae", firstChildCode.getDisplay());
		List<String> conceptNames = rootConcepts.get(0).getChildCodes().stream().map(t -> t.getCode()).collect(Collectors.toList());
		assertThat(conceptNames.toString(), conceptNames, Matchers.contains("A00.0", "A00.1", "A00.9"));
	}

	@Test
	public void testLoadIcd10CmCheckExtendedChildCode() throws IOException, SAXException {
		loader.load(reader);

		List<TermConcept> rootConcepts = new ArrayList<>(codeSystemVersion.getConcepts());
		TermConcept ExtendedChildCode = rootConcepts.get(2).getChildCodes().get(0).getChildCodes().get(1);
		assertEquals("H40.40X0", ExtendedChildCode.getCode());
		assertEquals("Glaucoma secondary to eye inflammation, unspecified eye, stage unspecified", ExtendedChildCode.getDisplay());

		List<String> conceptNames3 = rootConcepts.get(3).getChildCodes().get(0).getChildCodes().stream().map(t -> t.getCode()).collect(Collectors.toList());
		ExtendedChildCode = rootConcepts.get(3).getChildCodes().get(0).getChildCodes().get(0).getChildCodes().get(0).getChildCodes().get(0).getChildCodes().get(0).getChildCodes().get(3);
		assertEquals("R40.2112",ExtendedChildCode.getCode());
		assertEquals("Coma scale, eyes open, never, at arrival to emergency department", ExtendedChildCode.getDisplay());
	}
}
