package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetConceptDao;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetConceptDesignationDao;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetDao;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import org.springframework.beans.factory.annotation.Autowired;

public class ValueSetConceptAccumulatorFactory {

	@Autowired
	private ITermValueSetDao myValueSetDao;

	@Autowired
	private ITermValueSetConceptDao myValueSetConceptDao;

	@Autowired
	private ITermValueSetConceptDesignationDao myValueSetConceptDesignationDao;

	@Autowired
	private JpaStorageSettings myStorageSettings;

	public ValueSetConceptAccumulator create(TermValueSet theTermValueSet) {
		ValueSetConceptAccumulator valueSetConceptAccumulator = new ValueSetConceptAccumulator(
				theTermValueSet, myValueSetDao, myValueSetConceptDao, myValueSetConceptDesignationDao);

		valueSetConceptAccumulator.setSupportLegacyLob(myStorageSettings.isWriteToLegacyLobColumns());

		return valueSetConceptAccumulator;
	}
}
