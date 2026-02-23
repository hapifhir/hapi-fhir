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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Icd10CmLoaderTest {

	private static TermCodeSystemVersion codeSystemVersion;

	@BeforeAll
	static void beforeClass() throws IOException, SAXException {
		StringReader reader = new StringReader(ClasspathUtil.loadResource("icd/icd10cm_tabular_2021.xml"));
		codeSystemVersion = new TermCodeSystemVersion();
		Icd10CmLoader loader = new Icd10CmLoader(codeSystemVersion);
		loader.load(reader);
	}

	@Test
	void testLoadIcd10CmCheckVersion() {
		assertEquals("2021", codeSystemVersion.getCodeSystemVersionId());
	}

	@Test
	void testLoadIcd10CmCheckRootConcepts() {
		List<TermConcept> rootConcepts = new ArrayList<>(codeSystemVersion.getConcepts());
		assertThat(rootConcepts).hasSize(12);
		assertEquals("A00", rootConcepts.get(0).getCode());
		assertEquals("Cholera", rootConcepts.get(0).getDisplay());
		List<String> conceptNames = rootConcepts.stream().map(TermConcept::getCode).toList();
		assertThat(conceptNames).as(conceptNames.toString()).containsExactly("A00", "A01", "H40", "R40", "E08", "M48", "O33", "S12", "T07", "W34", "W49", "Y02");
	}

	@Test
	void testLoadIcd10CmCheckChildCode() {
		List<TermConcept> rootConcepts = new ArrayList<>(codeSystemVersion.getConcepts());
		assertThat(rootConcepts.get(0).getChildCodes()).hasSize(3);
		TermConcept firstChildCode = rootConcepts.get(0).getChildCodes().get(0);
		assertEquals("A00.0", firstChildCode.getCode());
		assertEquals("Cholera due to Vibrio cholerae 01, biovar cholerae", firstChildCode.getDisplay());
		List<String> conceptNames = rootConcepts.get(0).getChildCodes().stream().map(TermConcept::getCode).toList();
		assertThat(conceptNames).as(conceptNames.toString()).containsExactly("A00.0", "A00.1", "A00.9");
	}

	@Test
	void testLoadIcd10CmCheckExtendedChildCode() {
		List<TermConcept> rootConcepts = new ArrayList<>(codeSystemVersion.getConcepts());

		// Category with 7th characters defined and codes defined at the 2nd level subcategory.
		// e.g. Category H40 has 7th character extensions "0", "1", "2", "3", and "4" and a subcategory "4", which in turn has subcategories of "1" and "2".
		// Valid extensions include "H40.40X0", "H40.40X1", "H40.40X2", "H40.40X3", "H40.40X4", "H40.41X0", "H40.41X1", "H40.41X2", "H40.41X3", and
		// "H40.41X4" respectively which would appear as siblings of "H40.40" and "H40.41".
		List<String> conceptNames = rootConcepts.get(2).getChildCodes().get(0).getChildCodes().stream().map(TermConcept::getCode).toList();
		assertThat(conceptNames).as(conceptNames.toString()).containsExactly("H40.40", "H40.40X0", "H40.40X1", "H40.40X2", "H40.40X3", "H40.40X4", "H40.41", "H40.41X0", "H40.41X1", "H40.41X2", "H40.41X3", "H40.41X4");

		TermConcept extendedChildCode = rootConcepts.get(2).getChildCodes().get(0).getChildCodes().get(1);
		assertEquals("H40.40X0", extendedChildCode.getCode());
		assertEquals("Glaucoma secondary to eye inflammation, unspecified eye, stage unspecified", extendedChildCode.getDisplay());


		extendedChildCode = rootConcepts.get(3).getChildCodes().get(0).getChildCodes().get(0).getChildCodes().get(0).getChildCodes().get(0).getChildCodes().get(0).getChildCodes().get(3);
		assertEquals("R40.2112", extendedChildCode.getCode());
		assertEquals("Coma scale, eyes open, never, at arrival to emergency department", extendedChildCode.getDisplay());
	}

	@Test
	void testLoadIcd10CmCheckExtendedChildCode_additionalcases() {
		List<TermConcept> rootConcepts = new ArrayList<>(codeSystemVersion.getConcepts());

		// Case 1: Category with 7th characters defined and codes defined at the 1st level subcategory.
		// e.g. Category Y02 has 7th character extensions "A", "D", and "S" and a subcategory "0" which has no subcategories.
		// Valid extensions include "Y02.0XXA", "Y02.0XXD", and "Y02.0XXS" which would appear as siblings of "Y02.0".
		List<String> conceptNames = rootConcepts.get(11).getChildCodes().stream().map(TermConcept::getCode).toList();
		assertThat(conceptNames).as(conceptNames.toString()).containsExactly("Y02.0", "Y02.0XXA", "Y02.0XXD", "Y02.0XXS");
		// 5 new codes

		// Case 2: Category has a subcategory with 7th characters defined and subcategories beneath it.
		// e.g. M48 has a subcategory "4" which has 7th character extensions "A", "D", "G" and "S" and a subcategory "0".
		// Valid extensions include "M48.40XA", "M48.40XD", "M48.40XG" and "M48.40XS" which would appear as siblings of "M48.40".
		conceptNames = rootConcepts.get(5).getChildCodes().get(0).getChildCodes().stream().map(TermConcept::getCode).toList();
		assertThat(conceptNames).as(conceptNames.toString()).containsExactly("M48.40", "M48.40XA", "M48.40XD", "M48.40XG", "M48.40XS");
		// 7 new codes

		// Case 3: A second level subcategory (a subcategory of a subcategory) with 7th characters defined and subcategories beneath it.
		// e.g. EO8 has a subcategory "3" which has a subcategory of "2" that has 7th character extensions "1", "2", "3" and "9" and subcategory "1".
		// Valid codes include "E08.3211", "E08.3212", "E08.3213" and "E08.3219".
		conceptNames = rootConcepts.get(4).getChildCodes().get(0).getChildCodes().get(0).getChildCodes().stream().map(TermConcept::getCode).toList();
		assertThat(conceptNames).as(conceptNames.toString()).containsExactly("E08.321", "E08.3211", "E08.3212", "E08.3213", "E08.3219");
		// 8 new codes

		// Case 4: Category with 7th characters defined, but no subcategories or lower-level codes.
		// e.g. T07 7th character extensions "A", "D", and "S" and no subcategories.
		// Valid codes include "T07.XXXA", "T07.XXXD", "T07.XXXS".
		conceptNames = rootConcepts.get(8).getChildCodes().stream().map(TermConcept::getCode).toList();
		assertThat(conceptNames).as(conceptNames.toString()).containsExactly("T07.XXXA", "T07.XXXD", "T07.XXXS");
		// 4 new codes

		// Case 5: Category with 7th characters defined and two levels of subcategories.
		// e.g. W49 has 7th character extensions "A", "D", and "S" and a sub-category "0", which in turn has a subcategory of "1".
		// Valid codes include "W49.01XA", "W49.01XD", "W49.01XS".
		conceptNames = rootConcepts.get(10).getChildCodes().get(0).getChildCodes().stream().map(TermConcept::getCode).toList();
		assertThat(conceptNames).as(conceptNames.toString()).containsExactly("W49.01", "W49.01XA", "W49.01XD", "W49.01XS");
		// 6 new codes

		// Case 6: Category with 7th characters defined and three levels of subcategories.
		// e.g. W34 has 7th character extensions "A", "D", and "S" and a sub-category "0", which in turn has a subcategory of "1", which in turn is
		// has its subcategory of "0".
		// Valid codes include "W34.010A", "W34.010D", "W34.010S".
		conceptNames = rootConcepts.get(9).getChildCodes().get(0).getChildCodes().get(0).getChildCodes().stream().map(TermConcept::getCode).toList();
		assertThat(conceptNames).as(conceptNames.toString()).containsExactly("W34.010", "W34.010A", "W34.010D", "W34.010S");
		// 7 new codes

		// Case 7: Category has a subcategory with 7th characters defined and no further subcategories.
		// e.g. O33 has a subcategory "3" which has 7th character extensions "0", "1", and "2".
		// Valid codes include "O33.3XX0", "O33.3XX1", "O33.3XX2".
		conceptNames = rootConcepts.get(6).getChildCodes().stream().map(TermConcept::getCode).toList();
		assertThat(conceptNames).as(conceptNames.toString()).containsExactly("O33.3", "O33.3XX0", "O33.3XX1", "O33.3XX2");
		// 5 new codes

		// Case 8: A second level subcategory (a subcategory of a subcategory) with 7th characters defined and no subcategories beneath it.
		// e.g. EO8 has a subcategory "3" which has a subcategory of "7" that has 7th character extensions "1", "2", "3" and "9" and no subcategories.
		// Valid codes include "E08.37X1", "E08.37X2", "E08.37X3" and "E08.37X9".
		conceptNames = rootConcepts.get(4).getChildCodes().get(0).getChildCodes().stream().map(TermConcept::getCode).toList();
		assertThat(conceptNames).as(conceptNames.toString()).containsExactly("E08.32", "E08.37", "E08.37X1", "E08.37X2", "E08.37X3", "E08.37X9");
		// 5 additional new codes

		// Case 9: Category has 7th characters defined and a subcategory with its own 7th characters defined.
		// e.g. S12 has 7th character extensions "A", "B", "D", "G", "K", "S" and a subcategory "8" which its own 7th character extensions "A", "D", and "S".
		// Valid codes include "S12.8XXA", "S12.8XXD", and "S12.8XXS".
		conceptNames = rootConcepts.get(7).getChildCodes().stream().map(TermConcept::getCode).toList();
		assertThat(conceptNames).as(conceptNames.toString()).containsExactly("S12.8", "S12.8XXA", "S12.8XXD", "S12.8XXS");

	}
 }
