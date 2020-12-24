package ca.uhn.fhir.cql.r4;

import ca.uhn.fhir.cql.BaseCqlR4Test;
import ca.uhn.fhir.cql.common.provider.CqlProviderTestBase;
import ca.uhn.fhir.cql.r4.provider.LibraryOperationsProvider;
import ca.uhn.fhir.cql.r4.provider.MeasureOperationsProvider;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Library;
import org.hl7.fhir.r4.model.Measure;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CqlProviderR4Test extends BaseCqlR4Test implements CqlProviderTestBase {
	private static final Logger ourLog = LoggerFactory.getLogger(CqlProviderR4Test.class);

	@Autowired
	IFhirResourceDao<Measure> myMeasureDao;
	@Autowired
	IFhirResourceDao<Library> myLibraryDao;
	@Autowired
	LibraryOperationsProvider myLibraryOperationsProvider;
	@Autowired
	MeasureOperationsProvider myMeasureOperationsProvider;

	@Test
	public void testEXM349RefreshGeneratedContent() throws IOException {
		IdType measureId = new IdType("Measure", "measure-EXM349-2.10.000");
		loadBundle("r4/EXM349/EXM349-2.10.000-bundle.json");

		DaoMethodOutcome methodOutcome = (DaoMethodOutcome)myMeasureOperationsProvider.refreshGeneratedContent(null, measureId);
		assertNotNull(methodOutcome, "NULL methodOutcome returned from call to myMeasureOperationsProvider.refreshGeneratedContent(null, '" + measureId +"')!");
		OperationOutcome operationOutcome = (OperationOutcome) methodOutcome.getOperationOutcome();
		assertTrue(operationOutcome.getIssue().get(0).getSeverity().equals(OperationOutcome.IssueSeverity.INFORMATION),
			"Did NOT get result of OperationOutcome.IssueSeverity.INFORMATION from call to myMeasureOperationsProvider.refreshGeneratedContent(null, measureId)!");

		Measure measure = myMeasureDao.read(measureId);
		assertNotNull(measure, "NULL measure returned from call to myMeasureDao.read('" + measureId +"')!");
	}

	@Test
	public void testEXM130RefreshGeneratedContent() throws IOException {
		IdType measureId = new IdType("Measure", "measure-EXM130-7.3.000");
		IdType libraryId = new IdType("Library", "library-EXM130-7.3.000");

		loadResource("r4/EXM130/library-MATGlobalCommonFunctions-FHIR4-4.0.000.json");
		loadBundle("r4/EXM130/EXM130-7.3.000-bundle.json.manuallyeditedtoremovecqlandelm");

		Measure measure = myMeasureDao.read(measureId);
		int originalMeasureRelatedArtifactsSize = measure.getRelatedArtifact().size();
		Library library = myLibraryDao.read(libraryId);

		DaoMethodOutcome methodOutcome = (DaoMethodOutcome)myMeasureOperationsProvider.refreshGeneratedContent(null, measureId);
		OperationOutcome operationOutcome = (OperationOutcome) methodOutcome.getOperationOutcome();
		assertTrue(operationOutcome.getIssue().get(0).getSeverity().equals(OperationOutcome.IssueSeverity.INFORMATION),
			"Did NOT get result of OperationOutcome.IssueSeverity.INFORMATION from call to myMeasureOperationsProvider.refreshGeneratedContent(null, measureId)!");

		measure = myMeasureDao.read(measureId);
		assertNotNull(measure, "NULL measure returned from call to myMeasureDao.read('" + measureId +"')!");
		library = myLibraryDao.read(libraryId);
		assertNotNull(library, "NULL library returned from call to myLibraryDao.read('" + libraryId +"')!");

		assertTrue(measure.getRelatedArtifact().size() > originalMeasureRelatedArtifactsSize,
			"No increase in size of measure.getRelatedArtifacts()!");
	}
}
