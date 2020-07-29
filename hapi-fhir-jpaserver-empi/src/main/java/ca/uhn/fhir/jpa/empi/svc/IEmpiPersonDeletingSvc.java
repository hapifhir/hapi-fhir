package ca.uhn.fhir.jpa.empi.svc;

import java.util.List;

public interface IEmpiPersonDeletingSvc {

    void deleteResourcesAndHandleConflicts(List<Long> theLongs);

    void expungeHistoricalAndCurrentVersionsOfIds(List<Long> theLongs);
}
