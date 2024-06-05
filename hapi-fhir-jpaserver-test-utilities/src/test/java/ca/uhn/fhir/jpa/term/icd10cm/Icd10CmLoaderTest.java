package ca.uhn.fhir.jpa.term.icd10cm;

import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.util.ClasspathUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Icd10CmLoaderTest {

	private static TermCodeSystemVersion codeSystemVersion;

	@BeforeAll
	public static void beforeClass() throws IOException, SAXException {
		StringReader reader = new StringReader(ClasspathUtil.loadResource("icd/icd10cm_tabular_2021.xml"));
		codeSystemVersion = new TermCodeSystemVersion();
		Icd10CmLoader loader = new Icd10CmLoader(codeSystemVersion);
		loader.load(reader);
	}

	@Test
	public void testLoadIcd10CmCheckVersion() {
		assertEquals("2021", codeSystemVersion.getCodeSystemVersionId());
	}

	@Test
	public void testLoadIcd10CmCheckRootConcepts() {
		List<TermConcept> rootConcepts = new ArrayList<>(codeSystemVersion.getConcepts());
		assertThat(rootConcepts).hasSize(4);
		assertEquals("A00", rootConcepts.get(0).getCode());
		assertEquals("Cholera", rootConcepts.get(0).getDisplay());
		List<String> conceptNames = rootConcepts.stream().map(t -> t.getCode()).collect(Collectors.toList());
		assertThat(conceptNames).as(conceptNames.toString()).containsExactly("A00", "A01", "H40", "R40");
	}

	@Test
	public void testLoadIcd10CmCheckChildCode() {
		List<TermConcept> rootConcepts = new ArrayList<>(codeSystemVersion.getConcepts());
		assertThat(rootConcepts.get(0).getChildCodes()).hasSize(3);
		TermConcept firstChildCode = rootConcepts.get(0).getChildCodes().get(0);
		assertEquals("A00.0", firstChildCode.getCode());
		assertEquals("Cholera due to Vibrio cholerae 01, biovar cholerae", firstChildCode.getDisplay());
		List<String> conceptNames = rootConcepts.get(0).getChildCodes().stream().map(t -> t.getCode()).collect(Collectors.toList());
		assertThat(conceptNames).as(conceptNames.toString()).containsExactly("A00.0", "A00.1", "A00.9");
	}

	@Test
	public void testLoadIcd10CmCheckExtendedChildCode() {
		List<TermConcept> rootConcepts = new ArrayList<>(codeSystemVersion.getConcepts());

		List<String> conceptNames = rootConcepts.get(2).getChildCodes().get(0).getChildCodes().stream().map(t -> t.getCode()).collect(Collectors.toList());
		assertThat(conceptNames).as(conceptNames.toString()).containsExactly("H40.40", "H40.40X0", "H40.40X1", "H40.40X2", "H40.40X3", "H40.40X4", "H40.41", "H40.41X0", "H40.41X1", "H40.41X2", "H40.41X3", "H40.41X4");

		TermConcept ExtendedChildCode = rootConcepts.get(2).getChildCodes().get(0).getChildCodes().get(1);
		assertEquals("H40.40X0", ExtendedChildCode.getCode());
		assertEquals("Glaucoma secondary to eye inflammation, unspecified eye, stage unspecified", ExtendedChildCode.getDisplay());


		ExtendedChildCode = rootConcepts.get(3).getChildCodes().get(0).getChildCodes().get(0).getChildCodes().get(0).getChildCodes().get(0).getChildCodes().get(0).getChildCodes().get(3);
		assertEquals("R40.2112", ExtendedChildCode.getCode());
		assertEquals("Coma scale, eyes open, never, at arrival to emergency department", ExtendedChildCode.getDisplay());
	}
}
