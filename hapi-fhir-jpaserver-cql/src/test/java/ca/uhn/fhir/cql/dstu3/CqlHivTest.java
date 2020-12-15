package ca.uhn.fhir.cql.dstu3;

import ca.uhn.fhir.cql.BaseCqlDstu3Test;
import ca.uhn.fhir.cql.dstu3.provider.MeasureOperationsProvider;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Library;
import org.hl7.fhir.dstu3.model.Measure;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class CqlHivTest extends BaseCqlDstu3Test {
	public static final String HIV_INDICATORS_ID = "measure-EXM349-FHIR3-2.9.000";
	public static final IdType hivMeasureId = new IdType("Measure", HIV_INDICATORS_ID);

	@Autowired
	IFhirResourceDao<Measure> myMeasureDao;
	@Autowired
	IFhirResourceDao<Library> myLibraryDao;
	@Autowired
	MeasureOperationsProvider myMeasureOperationsProvider;

	@Test
	public void testGeneratedContent() throws IOException {
		loadBundle("dstu3/EXM349/EXM349_FHIR3-2.9.000-bundle.json");

		myMeasureOperationsProvider.refreshGeneratedContent(null, hivMeasureId);
		Measure measure = myMeasureDao.read(hivMeasureId);
		// FIXME KBD Add asserts--e.g. on generated elm
	}
}
