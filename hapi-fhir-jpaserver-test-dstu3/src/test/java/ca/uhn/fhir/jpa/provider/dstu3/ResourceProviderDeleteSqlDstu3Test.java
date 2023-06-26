package ca.uhn.fhir.jpa.provider.dstu3;

import ca.uhn.fhir.jpa.util.CircularQueueCaptureQueriesListener;
import ca.uhn.fhir.jpa.util.SqlQuery;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResourceProviderDeleteSqlDstu3Test extends BaseResourceProviderDstu3Test {

	private static final Logger ourLog = LoggerFactory.getLogger(ResourceProviderDeleteSqlDstu3Test.class);

	@Autowired
	protected CircularQueueCaptureQueriesListener myCaptureQueriesListener;

	@Test
	public void testDeleteFortyTokensWithOneCommand() {
		Observation o = new Observation();
		o.setStatus(Observation.ObservationStatus.FINAL);
		for (int i = 0; i < 40; ++i) {
			CodeableConcept code = new CodeableConcept();
			code.addCoding().setSystem("foo").setCode("Code" + i);
			o.getCategory().add(code);
		}
		IIdType observationId = myObservationDao.create(o).getId();

		myCaptureQueriesListener.clear();
		myObservationDao.delete(observationId);
		myCaptureQueriesListener.logDeleteQueries();
		long deleteCount = myCaptureQueriesListener.getDeleteQueries()
			.stream()
			.filter(query -> query.getSql(false, false).contains("HFJ_SPIDX_TOKEN"))
			.collect(Collectors.summarizingInt(SqlQuery::getSize))
			.getSum();
		assertEquals(1, deleteCount);
	}
}
