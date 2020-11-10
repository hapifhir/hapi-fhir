package ca.uhn.fhir.cql.provider;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import org.opencds.cqf.common.evaluation.EvaluationProviderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CqlProviderFactory {
	@Autowired
	private EvaluationProviderFactory myEvaluationProviderFactory;
	@Autowired
	private org.opencds.cqf.tooling.library.stu3.NarrativeProvider myNarrativeProviderDstu3;
	@Autowired
	private org.opencds.cqf.dstu3.providers.HQMFProvider myHQMFProviderDstu3;
	@Autowired
	private org.opencds.cqf.dstu3.providers.LibraryOperationsProvider myLibraryOperationsProviderDstu3;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private ca.uhn.fhir.jpa.rp.dstu3.MeasureResourceProvider myMeasureResourceProviderDstu3;

	public org.opencds.cqf.dstu3.providers.MeasureOperationsProvider getMeasureOperationsProviderDstu3() {
		return new org.opencds.cqf.dstu3.providers.MeasureOperationsProvider(myDaoRegistry, myEvaluationProviderFactory, myNarrativeProviderDstu3, myHQMFProviderDstu3, myLibraryOperationsProviderDstu3, myMeasureResourceProviderDstu3);
	}

	public org.opencds.cqf.r4.providers.MeasureOperationsProvider getMeasureOperationsProviderR4() {
		return null;
	}
}
