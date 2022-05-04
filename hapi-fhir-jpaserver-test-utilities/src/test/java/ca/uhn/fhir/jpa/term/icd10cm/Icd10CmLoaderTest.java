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

	@Test
	public void testLoadIcd10Cm() throws IOException, SAXException {
		StringReader reader = new StringReader(ClasspathUtil.loadResource("icd/icd10cm_tabular_2021.xml"));
		TermCodeSystemVersion codeSystemVersion = new TermCodeSystemVersion();
		Icd10CmLoader loader = new Icd10CmLoader(codeSystemVersion);
		loader.load(reader);

		assertEquals("2021", codeSystemVersion.getCodeSystemVersionId());

		List<TermConcept> rootConcepts = new ArrayList<>(codeSystemVersion.getConcepts());
		assertEquals(2, rootConcepts.size());
		assertEquals("A00", rootConcepts.get(0).getCode());
		assertEquals("Cholera", rootConcepts.get(0).getDisplay());
		List<String> conceptNames = rootConcepts.stream().map(t -> t.getCode()).collect(Collectors.toList());
		assertThat(conceptNames.toString(), conceptNames, Matchers.contains("A00", "A01"));

		assertEquals(3, rootConcepts.get(0).getChildCodes().size());
		TermConcept firstChildCode = rootConcepts.get(0).getChildCodes().get(0);
		assertEquals("A00.0", firstChildCode.getCode());
		assertEquals("Cholera due to Vibrio cholerae 01, biovar cholerae", firstChildCode.getDisplay());
		conceptNames = rootConcepts.get(0).getChildCodes().stream().map(t -> t.getCode()).collect(Collectors.toList());
		assertThat(conceptNames.toString(), conceptNames, Matchers.contains("A00.0", "A00.1", "A00.9"));

	}

}
