package ca.uhn.fhir.cql.provider;

import ca.uhn.fhir.cql.BaseCqlDstu3Test;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.MeasureReport;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.dstu3.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.opencds.cqf.common.evaluation.EvaluationProviderFactory;
import org.opencds.cqf.dstu3.providers.MeasureOperationsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CqlProviderDstu3Test extends BaseCqlDstu3Test {
	private static final Logger ourLog = LoggerFactory.getLogger(CqlProviderDstu3Test.class);

	@Autowired
	CqlProviderLoader myCqlProviderLoader;
	@Autowired
	DaoRegistry myDaoRegistry;
	@Autowired
	IFhirResourceDao<Patient> myPatientDao;

	private EvaluationProviderFactory evaluationProviderFactory;
	MeasureOperationsProvider myProvider;

	@BeforeEach
	public void before() {
		myProvider = myCqlProviderLoader.buildDstu3Provider();
	}

	@Test
	public void evaluateMeasure() {
		Patient patient = new Patient();
		// FIXME KBD add something to patient we want to measure
		IIdType patientId = myPatientDao.create(patient).getId().toVersionless();

		Patient patientInstance = myDaoRegistry.getResourceDao(Patient.class).read(patientId);

		// FIXME KBD
		String periodStart = "0";
		String periodEnd = StringUtils.defaultToString(System.currentTimeMillis());
		String subject = "Patient";
		MeasureReport measureReport = myProvider.evaluateMeasure((IdType) patientId.toVersionless(), periodStart,
			periodEnd, null, "patient", subject, null, null,
			null, null, null, null);
		assertNotNull(measureReport);
		// FIXME KBD assert on stuff in measureReport
	}
}
