package ca.uhn.fhir.cql.provider;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.rp.dstu3.LibraryResourceProvider;
import ca.uhn.fhir.jpa.rp.dstu3.MeasureResourceProvider;
import org.opencds.cqf.common.evaluation.EvaluationProviderFactory;
import org.opencds.cqf.dstu3.providers.HQMFProvider;
import org.opencds.cqf.dstu3.providers.LibraryOperationsProvider;
import org.opencds.cqf.dstu3.providers.MeasureOperationsProvider;
import org.opencds.cqf.tooling.library.stu3.NarrativeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CqlProviderFactory {
	@Autowired
	private EvaluationProviderFactory myEvaluationProviderFactory;
	@Autowired
	private NarrativeProvider myNarrativeProvider;
	@Autowired
	private HQMFProvider myHQMFProvider;
	@Autowired
	private LibraryOperationsProvider myLibraryOperationsProvider;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private MeasureResourceProvider myMeasureResourceProvider;
	@Autowired
	private LibraryResourceProvider myLibraryResourceProvider;

	public MeasureOperationsProvider getMeasureOperationsProviderDstu3() {
		// FIXME KBD Can we find a way to remove this?
		myMeasureResourceProvider.setDao(myDaoRegistry.getResourceDao("Measure"));
		// FIXME KBD Can we find a way to remove this?
		myLibraryResourceProvider.setDao(myDaoRegistry.getResourceDao("Library"));
		return new MeasureOperationsProvider(myDaoRegistry, myEvaluationProviderFactory, myNarrativeProvider, myHQMFProvider, myLibraryOperationsProvider, myMeasureResourceProvider);
	}

}
