package ca.uhn.fhir.cql.provider;

import ca.uhn.fhir.cql.BaseCqlR4Test;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opencds.cqf.common.evaluation.EvaluationProviderFactory;
import org.opencds.cqf.dstu3.providers.MeasureOperationsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class CqlProviderR4Test extends BaseCqlR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(CqlProviderR4Test.class);

	@Autowired
	CqlProviderLoader myCqlProviderLoader;
	@Autowired
	IFhirResourceDao<Patient> myPatientDao;

	private EvaluationProviderFactory evaluationProviderFactory;
	MeasureOperationsProvider myProvider;

	@BeforeEach
	public void before() {
		myProvider = myCqlProviderLoader.buildDstu3Provider(evaluationProviderFactory);
	}

	@Test
	public void evaluateMeasure() {
		Patient patient = new Patient();
		// FIXME KBD add something to patient we want to measure
		IIdType patientId = myPatientDao.create(patient).getId().toVersionless();

	// FIXME KBD
//		MeasureReport measureReport = myProvider.evaluateMeasure(patientId, ...);
		// FIXME KBD assert on stuff in measureReport
	}
}
