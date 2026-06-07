package ca.uhn.fhir.jpa.batch2.jobs.term.icd.icd10cm;

import ca.uhn.fhir.jpa.batch2.jobs.term.icd.BaseIcdTest;
import ca.uhn.fhir.util.ClasspathUtil;
import org.hl7.fhir.r4.model.CodeSystem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImportIcd10CmStep2HandleConceptsTest extends BaseIcdTest {

	@InjectMocks
	private ImportIcd10CmStep2HandleConcepts mySvc;

	@Test
	void testLoadIcd10CmCheckVersion() {
		// Setup
		when(myAttachment.getInputStream()).thenReturn(ClasspathUtil.loadResourceAsStream("icd/icd10cm_tabular_2021.xml"));

		// Test
		mySvc.processAttachment(myStepExecutionDetails, myJobMetadata, myContext, myAttachment, myJobParameters, myCodeSystemToPopulate, myData, null);

		// Verify
		assertEquals("2021", myCodeSystemToPopulate.getVersion());
	}

	@Test
	void testLoadIcd10CmCheckRootConcepts() {
		// Setup
		when(myAttachment.getInputStream()).thenReturn(ClasspathUtil.loadResourceAsStream("icd/icd10cm_tabular_2021.xml"));
		
		// Test
		mySvc.processAttachment(myStepExecutionDetails, myJobMetadata, myContext, myAttachment, myJobParameters, myCodeSystemToPopulate, myData, null);
		
		// Verify
		String tree = toTree(myContext.getCodeToConcept().values());
		String expected = """
			A00 "Cholera"
			-A00.0 "Cholera due to Vibrio cholerae 01, biovar cholerae"
			-A00.1 "Cholera due to Vibrio cholerae 01, biovar eltor"
			-A00.9 "Cholera, unspecified"
			A01 "Typhoid and paratyphoid fevers"
			-A01.0 "Typhoid fever"
			--A01.00 "Typhoid fever, unspecified"
			--A01.01 "Typhoid meningitis"
			--A01.02 "Typhoid fever with heart involvement"
			--A01.03 "Typhoid pneumonia"
			--A01.04 "Typhoid arthritis"
			--A01.05 "Typhoid osteomyelitis"
			--A01.09 "Typhoid fever with other complications"
			-A01.1 "Paratyphoid fever A"
			-A01.2 "Paratyphoid fever B"
			-A01.3 "Paratyphoid fever C"
			-A01.4 "Paratyphoid fever, unspecified"
			H40 "Glaucoma"
			-H40.4 "Glaucoma secondary to eye inflammation"
			--H40.40 "Glaucoma secondary to eye inflammation, unspecified eye"
			--H40.40X0 "Glaucoma secondary to eye inflammation, unspecified eye, stage unspecified"
			--H40.40X1 "Glaucoma secondary to eye inflammation, unspecified eye, mild stage"
			--H40.40X2 "Glaucoma secondary to eye inflammation, unspecified eye, moderate stage"
			--H40.40X3 "Glaucoma secondary to eye inflammation, unspecified eye, severe stage"
			--H40.40X4 "Glaucoma secondary to eye inflammation, unspecified eye, indeterminate stage"
			--H40.41 "Glaucoma secondary to eye inflammation, right eye"
			--H40.41X0 "Glaucoma secondary to eye inflammation, right eye, stage unspecified"
			--H40.41X1 "Glaucoma secondary to eye inflammation, right eye, mild stage"
			--H40.41X2 "Glaucoma secondary to eye inflammation, right eye, moderate stage"
			--H40.41X3 "Glaucoma secondary to eye inflammation, right eye, severe stage"
			--H40.41X4 "Glaucoma secondary to eye inflammation, right eye, indeterminate stage"
			R40 "Somnolence, stupor and coma"
			-R40.0 "Somnolence"
			--R40.1 "Stupor"
			---R40.2 "Coma"
			----R40.20 "Unspecified coma"
			-----R40.21 "Coma scale, eyes open"
			------R40.211 "Coma scale, eyes open, never"
			------R40.2110 "Coma scale, eyes open, never, unspecified time"
			------R40.2111 "Coma scale, eyes open, never, in the field [EMT or ambulance]"
			------R40.2112 "Coma scale, eyes open, never, at arrival to emergency department"
			------R40.2113 "Coma scale, eyes open, never, at hospital admission"
			------R40.2114 "Coma scale, eyes open, never, 24 hours or more after hospital admission"
			E08 "Diabetes mellitus due to underlying condition"
			-E08.3 "Diabetes mellitus due to underlying condition with ophthalmic complications"
			--E08.32 "Diabetes mellitus due to underlying condition with mild nonproliferative diabetic retinopathy"
			---E08.321 "Diabetes mellitus due to underlying condition with mild nonproliferative diabetic retinopathy with macular edema"
			---E08.3211 "Diabetes mellitus due to underlying condition with mild nonproliferative diabetic retinopathy with macular edema, right eye"
			---E08.3212 "Diabetes mellitus due to underlying condition with mild nonproliferative diabetic retinopathy with macular edema, left eye"
			---E08.3213 "Diabetes mellitus due to underlying condition with mild nonproliferative diabetic retinopathy with macular edema, bilateral"
			---E08.3219 "Diabetes mellitus due to underlying condition with mild nonproliferative diabetic retinopathy with macular edema, unspecified eye"
			--E08.37 "Diabetes mellitus due to underlying condition with diabetic macular edema, resolved following treatment"
			--E08.37X1 "Diabetes mellitus due to underlying condition with diabetic macular edema, resolved following treatment, right eye"
			--E08.37X2 "Diabetes mellitus due to underlying condition with diabetic macular edema, resolved following treatment, left eye"
			--E08.37X3 "Diabetes mellitus due to underlying condition with diabetic macular edema, resolved following treatment, bilateral"
			--E08.37X9 "Diabetes mellitus due to underlying condition with diabetic macular edema, resolved following treatment, unspecified eye"
			M48 "Other spondylopathies"
			-M48.4 "Fatigue fracture of vertebra"
			--M48.40 "Fatigue fracture of vertebra, site unspecified"
			--M48.40XA "Fatigue fracture of vertebra, site unspecified, initial encounter for fracture"
			--M48.40XD "Fatigue fracture of vertebra, site unspecified, subsequent encounter for fracture with routine healing"
			--M48.40XG "Fatigue fracture of vertebra, site unspecified, subsequent encounter for fracture with delayed healing"
			--M48.40XS "Fatigue fracture of vertebra, site unspecified, sequela of fracture"
			O33 "Maternal care for disproportion"
			-O33.3 "Maternal care for disproportion due to outlet contraction of pelvis"
			-O33.3XX0 "Maternal care for disproportion due to outlet contraction of pelvis, not applicable or unspecified"
			-O33.3XX1 "Maternal care for disproportion due to outlet contraction of pelvis, fetus 1"
			-O33.3XX2 "Maternal care for disproportion due to outlet contraction of pelvis, fetus 2"
			S12 "Fracture of cervical vertebra and other parts of neck"
			-S12.8 "Fracture of other parts of neck"
			-S12.8XXA "Fracture of other parts of neck, initial encounter"
			-S12.8XXD "Fracture of other parts of neck, subsequent encounter"
			-S12.8XXS "Fracture of other parts of neck, sequela"
			T07 "Unspecified multiple injuries"
			-T07.XXXA "Unspecified multiple injuries, initial encounter"
			-T07.XXXD "Unspecified multiple injuries, subsequent encounter"
			-T07.XXXS "Unspecified multiple injuries, sequela"
			W34 "Accidental discharge and malfunction from other and unspecified firearms and guns"
			-W34.0 "Accidental discharge from other and unspecified firearms and guns"
			--W34.01 "Accidental discharge of gas, air or spring-operated guns"
			---W34.010 "Accidental discharge of airgun"
			---W34.010A "Accidental discharge of airgun, initial encounter"
			---W34.010D "Accidental discharge of airgun, subsequent encounter"
			---W34.010S "Accidental discharge of airgun, sequela"
			W49 "Exposure to other inanimate mechanical forces"
			-W49.0 "Item causing external constriction"
			--W49.01 "Hair causing external constriction"
			--W49.01XA "Hair causing external constriction, initial encounter"
			--W49.01XD "Hair causing external constriction, subsequent encounter"
			--W49.01XS "Hair causing external constriction, sequela"
			Y02 "Assault by pushing or placing victim in front of moving object"
			-Y02.0 "Assault by pushing or placing victim in front of motor vehicle"
			-Y02.0XXA "Assault by pushing or placing victim in front of motor vehicle, initial encounter"
			-Y02.0XXD "Assault by pushing or placing victim in front of motor vehicle, subsequent encounter"
			-Y02.0XXS "Assault by pushing or placing victim in front of motor vehicle, sequela"
			""";
		assertEquals(expected, tree);

		List<CodeSystem.ConceptDefinitionComponent> rootConcepts = super.getRootConcepts(myContext.getCodeToConcept().values());
		assertThat(rootConcepts).hasSize(12);
		assertEquals("A00", rootConcepts.get(0).getCode());
		assertEquals("Cholera", rootConcepts.get(0).getDisplay());
		List<String> conceptNames = rootConcepts.stream().map(t->t.getCode()).toList();
		assertThat(conceptNames).as(conceptNames.toString()).containsExactly("A00", "A01", "H40", "R40", "E08", "M48", "O33", "S12", "T07", "W34", "W49", "Y02");
	}

	@Test
	void testLoadIcd10CmCheckChildCode() {
		// Setup
		when(myAttachment.getInputStream()).thenReturn(ClasspathUtil.loadResourceAsStream("icd/icd10cm_tabular_2021.xml"));

		// Test
		mySvc.processAttachment(myStepExecutionDetails, myJobMetadata, myContext, myAttachment, myJobParameters, myCodeSystemToPopulate, myData, null);

		// Verify
		List<CodeSystem.ConceptDefinitionComponent> rootConcepts = getRootConcepts(myContext.getCodeToConcept().values());
		assertThat(rootConcepts.get(0).getConcept()).hasSize(3);
		CodeSystem.ConceptDefinitionComponent firstChildCode = rootConcepts.get(0).getConcept().get(0);
		assertEquals("A00.0", firstChildCode.getCode());
		assertEquals("Cholera due to Vibrio cholerae 01, biovar cholerae", firstChildCode.getDisplay());
		List<String> conceptNames = rootConcepts.get(0).getConcept().stream().map(t->t.getCode()).toList();
		assertThat(conceptNames).as(conceptNames.toString()).containsExactly("A00.0", "A00.1", "A00.9");
	}

	@Test
	void testLoadIcd10CmCheckExtendedChildCode() {
		// Setup
		when(myAttachment.getInputStream()).thenReturn(ClasspathUtil.loadResourceAsStream("icd/icd10cm_tabular_2021.xml"));

		// Test
		mySvc.processAttachment(myStepExecutionDetails, myJobMetadata, myContext, myAttachment, myJobParameters, myCodeSystemToPopulate, myData, null);

		// Verify
		List<CodeSystem.ConceptDefinitionComponent> rootConcepts = getRootConcepts(myContext.getCodeToConcept().values());

		// Category with 7th characters defined and codes defined at the 2nd level subcategory.
		// e.g. Category H40 has 7th character extensions "0", "1", "2", "3", and "4" and a subcategory "4", which in turn has subcategories of "1" and "2".
		// Valid extensions include "H40.40X0", "H40.40X1", "H40.40X2", "H40.40X3", "H40.40X4", "H40.41X0", "H40.41X1", "H40.41X2", "H40.41X3", and
		// "H40.41X4" respectively which would appear as siblings of "H40.40" and "H40.41".
		List<String> conceptNames = rootConcepts.get(2).getConcept().get(0).getConcept().stream().map(t->t.getCode()).toList();
		assertThat(conceptNames).as(conceptNames.toString()).containsExactly("H40.40", "H40.40X0", "H40.40X1", "H40.40X2", "H40.40X3", "H40.40X4", "H40.41", "H40.41X0", "H40.41X1", "H40.41X2", "H40.41X3", "H40.41X4");

		CodeSystem.ConceptDefinitionComponent extendedChildCode = rootConcepts.get(2).getConcept().get(0).getConcept().get(1);
		assertEquals("H40.40X0", extendedChildCode.getCode());
		assertEquals("Glaucoma secondary to eye inflammation, unspecified eye, stage unspecified", extendedChildCode.getDisplay());


		extendedChildCode = rootConcepts.get(3).getConcept().get(0).getConcept().get(0).getConcept().get(0).getConcept().get(0).getConcept().get(0).getConcept().get(3);
		assertEquals("R40.2112", extendedChildCode.getCode());
		assertEquals("Coma scale, eyes open, never, at arrival to emergency department", extendedChildCode.getDisplay());
	}

	@Test
	void testLoadIcd10CmCheckExtendedChildCode_additionalcases() {
		// Setup
		when(myAttachment.getInputStream()).thenReturn(ClasspathUtil.loadResourceAsStream("icd/icd10cm_tabular_2021.xml"));

		// Test
		mySvc.processAttachment(myStepExecutionDetails, myJobMetadata, myContext, myAttachment, myJobParameters, myCodeSystemToPopulate, myData, null);

		// Verify
		List<CodeSystem.ConceptDefinitionComponent> rootConcepts = getRootConcepts(myContext.getCodeToConcept().values());

		// Case 1: Category with 7th characters defined and codes defined at the 1st level subcategory.
		// e.g. Category Y02 has 7th character extensions "A", "D", and "S" and a subcategory "0" which has no subcategories.
		// Valid extensions include "Y02.0XXA", "Y02.0XXD", and "Y02.0XXS" which would appear as siblings of "Y02.0".
		List<String> conceptNames = rootConcepts.get(11).getConcept().stream().map(t->t.getCode()).toList();
		assertThat(conceptNames).as(conceptNames.toString()).containsExactly("Y02.0", "Y02.0XXA", "Y02.0XXD", "Y02.0XXS");
		// 5 new codes

		// Case 2: Category has a subcategory with 7th characters defined and subcategories beneath it.
		// e.g. M48 has a subcategory "4" which has 7th character extensions "A", "D", "G" and "S" and a subcategory "0".
		// Valid extensions include "M48.40XA", "M48.40XD", "M48.40XG" and "M48.40XS" which would appear as siblings of "M48.40".
		conceptNames = rootConcepts.get(5).getConcept().get(0).getConcept().stream().map(t->t.getCode()).toList();
		assertThat(conceptNames).as(conceptNames.toString()).containsExactly("M48.40", "M48.40XA", "M48.40XD", "M48.40XG", "M48.40XS");
		// 7 new codes

		// Case 3: A second level subcategory (a subcategory of a subcategory) with 7th characters defined and subcategories beneath it.
		// e.g. EO8 has a subcategory "3" which has a subcategory of "2" that has 7th character extensions "1", "2", "3" and "9" and subcategory "1".
		// Valid codes include "E08.3211", "E08.3212", "E08.3213" and "E08.3219".
		conceptNames = rootConcepts.get(4).getConcept().get(0).getConcept().get(0).getConcept().stream().map(t->t.getCode()).toList();
		assertThat(conceptNames).as(conceptNames.toString()).containsExactly("E08.321", "E08.3211", "E08.3212", "E08.3213", "E08.3219");
		// 8 new codes

		// Case 4: Category with 7th characters defined, but no subcategories or lower-level codes.
		// e.g. T07 7th character extensions "A", "D", and "S" and no subcategories.
		// Valid codes include "T07.XXXA", "T07.XXXD", "T07.XXXS".
		conceptNames = rootConcepts.get(8).getConcept().stream().map(t->t.getCode()).toList();
		assertThat(conceptNames).as(conceptNames.toString()).containsExactly("T07.XXXA", "T07.XXXD", "T07.XXXS");
		// 4 new codes

		// Case 5: Category with 7th characters defined and two levels of subcategories.
		// e.g. W49 has 7th character extensions "A", "D", and "S" and a sub-category "0", which in turn has a subcategory of "1".
		// Valid codes include "W49.01XA", "W49.01XD", "W49.01XS".
		conceptNames = rootConcepts.get(10).getConcept().get(0).getConcept().stream().map(t->t.getCode()).toList();
		assertThat(conceptNames).as(conceptNames.toString()).containsExactly("W49.01", "W49.01XA", "W49.01XD", "W49.01XS");
		// 6 new codes

		// Case 6: Category with 7th characters defined and three levels of subcategories.
		// e.g. W34 has 7th character extensions "A", "D", and "S" and a sub-category "0", which in turn has a subcategory of "1", which in turn is
		// has its subcategory of "0".
		// Valid codes include "W34.010A", "W34.010D", "W34.010S".
		conceptNames = rootConcepts.get(9).getConcept().get(0).getConcept().get(0).getConcept().stream().map(t->t.getCode()).toList();
		assertThat(conceptNames).as(conceptNames.toString()).containsExactly("W34.010", "W34.010A", "W34.010D", "W34.010S");
		// 7 new codes

		// Case 7: Category has a subcategory with 7th characters defined and no further subcategories.
		// e.g. O33 has a subcategory "3" which has 7th character extensions "0", "1", and "2".
		// Valid codes include "O33.3XX0", "O33.3XX1", "O33.3XX2".
		conceptNames = rootConcepts.get(6).getConcept().stream().map(t->t.getCode()).toList();
		assertThat(conceptNames).as(conceptNames.toString()).containsExactly("O33.3", "O33.3XX0", "O33.3XX1", "O33.3XX2");
		// 5 new codes

		// Case 8: A second level subcategory (a subcategory of a subcategory) with 7th characters defined and no subcategories beneath it.
		// e.g. EO8 has a subcategory "3" which has a subcategory of "7" that has 7th character extensions "1", "2", "3" and "9" and no subcategories.
		// Valid codes include "E08.37X1", "E08.37X2", "E08.37X3" and "E08.37X9".
		conceptNames = rootConcepts.get(4).getConcept().get(0).getConcept().stream().map(t->t.getCode()).toList();
		assertThat(conceptNames).as(conceptNames.toString()).containsExactly("E08.32", "E08.37", "E08.37X1", "E08.37X2", "E08.37X3", "E08.37X9");
		// 5 additional new codes

		// Case 9: Category has 7th characters defined and a subcategory with its own 7th characters defined.
		// e.g. S12 has 7th character extensions "A", "B", "D", "G", "K", "S" and a subcategory "8" which its own 7th character extensions "A", "D", and "S".
		// Valid codes include "S12.8XXA", "S12.8XXD", and "S12.8XXS".
		conceptNames = rootConcepts.get(7).getConcept().stream().map(t->t.getCode()).toList();
		assertThat(conceptNames).as(conceptNames.toString()).containsExactly("S12.8", "S12.8XXA", "S12.8XXD", "S12.8XXS");

	}
 }
