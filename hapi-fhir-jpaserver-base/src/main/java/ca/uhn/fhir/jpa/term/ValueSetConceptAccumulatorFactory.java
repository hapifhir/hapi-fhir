package ca.uhn.fhir.jpa.term;

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

	public ValueSetConceptAccumulator create(TermValueSet theTermValueSet) {
		return new ValueSetConceptAccumulator(
				theTermValueSet, myValueSetDao, myValueSetConceptDao, myValueSetConceptDesignationDao);
	}
}
