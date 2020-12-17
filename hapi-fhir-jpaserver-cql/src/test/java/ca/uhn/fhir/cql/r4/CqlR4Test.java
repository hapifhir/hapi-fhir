package ca.uhn.fhir.cql.r4;

import ca.uhn.fhir.cql.BaseCqlR4Test;
import ca.uhn.fhir.cql.r4.provider.MeasureOperationsProvider;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.rest.api.MethodOutcome;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Library;
import org.hl7.fhir.r4.model.Measure;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CqlR4Test extends BaseCqlR4Test {
	public static final String HIV_INDICATORS_ID = "measure-EXM349-2.10.000"; // hiv-indicators
	public static final IdType hivMeasureId = new IdType("Measure", HIV_INDICATORS_ID);

	@Autowired
	IFhirResourceDao<Measure> myMeasureDao;
	@Autowired
	IFhirResourceDao<Library> myLibraryDao;
	@Autowired
	MeasureOperationsProvider myMeasureOperationsProvider;

	//@Test
	public void testGeneratedContentEXM349() throws IOException {
		//loadResource("r4/EXM349/library-EXM349_FHIR3-2.9.000.json");
		loadBundle("r4/EXM349/EXM349-2.10.000-bundle.json");

		myMeasureOperationsProvider.refreshGeneratedContent(null, hivMeasureId);
		Measure measure = myMeasureDao.read(hivMeasureId);
		// FIXME KBD Add asserts--e.g. on generated elm
	}

	@Test
	public void testGeneratedContentEXM130() throws IOException {
		IdType measureId = new IdType("Measure", "measure-EXM130-7.3.000");
		IdType libraryId = new IdType("Library", "library-EXM130-7.3.000");

		loadResource("r4/EXM130/library-MATGlobalCommonFunctions-FHIR4-4.0.000.json");
		loadBundle("r4/EXM130/EXM130-7.3.000-bundle.json");
		// FIXME KBD At this point, the loading of the library without CQL or ELM triggered the loading mechanism
		// to look for, find and parse the raw CQL on teh disk, and then fill in the missing parts in the Library
		// Therefore, we can Assert that it contains CQL and ELm now since we know the disk file did not.

		Measure measure = myMeasureDao.read(measureId);
		int originalMeasureRelatedArtifactsSize = measure.getRelatedArtifact().size();
		Library library = myLibraryDao.read(libraryId);

		MethodOutcome methodOutcome = myMeasureOperationsProvider.refreshGeneratedContent(null, measureId);
		Assert.notNull(methodOutcome, "NULL methodOutcome returned from call to myMeasureOperationsProvider.refreshGeneratedContent(null, '" + measureId +"')!");

		measure = myMeasureDao.read(measureId);
		assertNotNull(measure, "NULL measure returned from call to myMeasureDao.read('" + measureId +"')!");
		library = myLibraryDao.read(libraryId);
		assertNotNull(library, "NULL library returned from call to myLibraryDao.read('" + libraryId +"')!");

		assertTrue(measure.getRelatedArtifact().size() > originalMeasureRelatedArtifactsSize);

		// FIXME KBD Add asserts--e.g. on generated elm
	}
}
