package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemVersionDao;
import ca.uhn.fhir.jpa.dao.r5.BaseJpaR5Test;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.term.api.ITermWriteSvc;
import org.hl7.fhir.common.hapi.validation.util.TermConceptPropertyTypeEnum;
import org.hl7.fhir.r5.model.CodeSystem;
import org.hl7.fhir.r5.model.Coding;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;

import static ca.uhn.fhir.test.utilities.UuidUtils.UUID_PATTERN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;

class TermWriteSvcImplTest extends BaseJpaR5Test {

	@Autowired
	private TermWriteSvcImpl mySvc;
	@Autowired
	private ITermCodeSystemVersionDao myCodeSystemVersionDao;

	@Test
	void testStartStagingCodeSystemVersion_DoesntAlreadyExist() {
		createCodeSystem(withUrl("http://foo"));

		// Test
		ITermWriteSvc.StartStagingCodeSystemVersionResponse outcome = mySvc.startStagingCodeSystemVersion("http://foo", "123");

		// Verify
		runInTransaction(() -> {
			TermCodeSystemVersion existing = myCodeSystemVersionDao.findByCodeSystemUriAndVersion("http://foo", outcome.stagingVersionId());
			assertNotNull(existing);
			assertEquals("http://foo", existing.getCodeSystem().getCodeSystemUri());
			assertEquals(outcome.stagingVersionId(), existing.getCodeSystemVersionId());
			assertThat(existing.getCodeSystemVersionId()).matches(UUID_PATTERN);
			assertEquals("123", existing.getCodeSystemIntendedVersionId());

			// The new version shouldn't be activated
			assertNotSame(existing, existing.getCodeSystem().getCurrentVersion());
			assertNull(existing.getCodeSystem().getCurrentVersion().getCodeSystemVersionId());
		});
	}


	@Test
	void testWriteCodeSystemCodes_WithPropertyAndDesignation() {
		createCodeSystem(withUrl("http://foo"));
		String stagingVersion = mySvc.startStagingCodeSystemVersion("http://foo", "123").stagingVersionId();

		// Test
		CodeSystem input = new CodeSystem();
		input.setUrl("http://foo");
		input.setVersion(stagingVersion);
		input.addConcept()
			.setCode("A0")
			.setDisplay("A0-Display")
			.addDesignation(
				new CodeSystem.ConceptDefinitionDesignationComponent()
					.setValue("A0-Designation-Value")
					.setLanguage("en_CA")
					.setUse(new Coding("http://designations", "A0-desig", null)))
			.addProperty(
				new CodeSystem.ConceptPropertyComponent()
					.setCode("A0-Property")
					.setValue(new Coding("A0-Property-System", "A0-Property-Value", "A0-Property-Display"))
			);
		input.addConcept()
			.setCode("A1")
			.setDisplay("A1-Display");

		ITermWriteSvc.UploadCodeSystemConceptsResponse response = mySvc.uploadCodeSystemConcepts(input);

		// Verify
		assertEquals(2, response.conceptsAdded());
		assertEquals(1, response.propertiesAdded());
		assertEquals(1, response.designationsAdded());
		assertEquals(0, response.conceptLinksAdded());

		runInTransaction(()->{
			TermCodeSystemVersion existing = myCodeSystemVersionDao.findByCodeSystemUriAndVersion("http://foo", stagingVersion);
			assertNotNull(existing);
			List<TermConcept> concepts = getConceptsSortedByCode(existing);
			assertEquals(2, concepts.size());

			assertEquals("A0", concepts.get(0).getCode());
			assertEquals("A0-Display", concepts.get(0).getDisplay());
			assertEquals(1, concepts.get(0).getDesignations().size());
			assertEquals("en_CA", concepts.get(0).getDesignations().iterator().next().getLanguage());
			assertEquals("http://designations", concepts.get(0).getDesignations().iterator().next().getUseSystem());
			assertEquals("A0-desig", concepts.get(0).getDesignations().iterator().next().getUseCode());
			assertEquals("A0-Designation-Value", concepts.get(0).getDesignations().iterator().next().getValue());
			assertEquals(1, concepts.get(0).getProperties().size());
			assertEquals("A0-Property", concepts.get(0).getProperties().iterator().next().getKey());
			assertEquals(TermConceptPropertyTypeEnum.CODING, concepts.get(0).getProperties().iterator().next().getType());
			assertEquals("A0-Property-Value", concepts.get(0).getProperties().iterator().next().getValue());
			assertEquals("A0-Property-System", concepts.get(0).getProperties().iterator().next().getCodeSystem());
			assertEquals("A0-Property-Display", concepts.get(0).getProperties().iterator().next().getDisplay());

			assertEquals("A1", concepts.get(1).getCode());
			assertEquals("A1-Display", concepts.get(1).getDisplay());
			assertEquals(0, concepts.get(1).getDesignations().size());
			assertEquals(0, concepts.get(1).getProperties().size());
		});

		// Repeat a second time and ensure that nothing is added
		response = mySvc.uploadCodeSystemConcepts(input);

		// Verify
		assertEquals(0, response.conceptsAdded());
		assertEquals(0, response.propertiesAdded());
		assertEquals(0, response.designationsAdded());
		runInTransaction(()-> {
			TermCodeSystemVersion existing = myCodeSystemVersionDao.findByCodeSystemUriAndVersion("http://foo", stagingVersion);
			assertNotNull(existing);
			List<TermConcept> concepts = getConceptsSortedByCode(existing);
			assertEquals(2, concepts.size());
			assertEquals(1, concepts.get(0).getDesignations().size());
			assertEquals(1, concepts.get(0).getProperties().size());
		});

	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void testWriteCodeSystemCodes_WithHierarchy(boolean theParentAlreadyExists) {
		createCodeSystem(withUrl("http://foo"));

		String stagingVersion = mySvc.startStagingCodeSystemVersion("http://foo", "123").stagingVersionId();

		if (theParentAlreadyExists) {
			CodeSystem input = new CodeSystem();
			input.setUrl("http://foo");
			input.setVersion(stagingVersion);
			input.addConcept()
				.setCode("PARENT")
				.setDisplay("Parent");
			mySvc.uploadCodeSystemConcepts(input);
		}

		// Test

		CodeSystem input = new CodeSystem();
		input.setUrl("http://foo");
		input.setVersion(stagingVersion);
		CodeSystem.ConceptDefinitionComponent parent = input.addConcept()
			.setCode("PARENT")
			.setDisplay("Parent");
		parent.addConcept()
				.setCode("CHILD")
				.setDisplay("Child");

		ITermWriteSvc.UploadCodeSystemConceptsResponse response = mySvc.uploadCodeSystemConcepts(input);
		if (theParentAlreadyExists) {
			assertEquals(1, response.conceptsAdded());
		}else{
			assertEquals(2, response.conceptsAdded());
		}
		assertEquals(1, response.conceptLinksAdded());


		// Verify
		runInTransaction(()->{
			TermCodeSystemVersion existing = myCodeSystemVersionDao.findByCodeSystemUriAndVersion("http://foo", stagingVersion);
			assertNotNull(existing);
			assertEquals(2, existing.getConcepts().size());
			assertEquals("Parent", existing.getConcept("PARENT").orElseThrow().getDisplay());
			List<TermConcept> children = existing.getConcept("PARENT").orElseThrow().getChildCodes();
			assertEquals(1, children.size());
			assertEquals("CHILD", children.get(0).getCode());
			assertEquals("Child", children.get(0).getDisplay());
		});

		// Repeat a second time and ensure that nothing is added
		response = mySvc.uploadCodeSystemConcepts(input);
		assertEquals(0, response.conceptsAdded());
		assertEquals(0, response.conceptLinksAdded());

		// Verify
		runInTransaction(()-> {
			TermCodeSystemVersion existing = myCodeSystemVersionDao.findByCodeSystemUriAndVersion("http://foo", stagingVersion);
			assertNotNull(existing);
			List<TermConcept> concepts = getConceptsSortedByCode(existing);
			assertEquals(2, concepts.size());
		});

	}

	private static List<TermConcept> getConceptsSortedByCode(TermCodeSystemVersion theCodeSystemVersion) {
		return theCodeSystemVersion.getConcepts().stream().sorted(Comparator.comparing(TermConcept::getCode)).toList();
	}


}
