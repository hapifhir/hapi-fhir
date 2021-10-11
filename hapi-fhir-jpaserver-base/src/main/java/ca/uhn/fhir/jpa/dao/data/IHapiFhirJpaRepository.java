package ca.uhn.fhir.jpa.dao.data;

import javax.transaction.Transactional;

@Transactional(Transactional.TxType.MANDATORY)
public interface IHapiFhirJpaRepository {
}
