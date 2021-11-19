package ca.uhn.fhir.jpa.dao;

import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import static ca.uhn.fhir.jpa.dao.IdSubstitutionMap.toVersionlessValue;

public class EntriesToProcessMap {

	private final IdentityHashMap<IBase, IIdType> myEntriesToProcess = new IdentityHashMap<>();
	private final Map<String, IIdType> myVersionlessIdToVersionedId = new HashMap<>();

	public void put(IBase theBundleEntry, IIdType theId) {
		myEntriesToProcess.put(theBundleEntry, theId);
		myVersionlessIdToVersionedId.put(toVersionlessValue(theId), theId);
	}

	public IIdType getIdWithVersionlessComparison(IIdType theId) {
		return myVersionlessIdToVersionedId.get(toVersionlessValue(theId));
	}

	public Set<Map.Entry<IBase, IIdType>> entrySet() {
		return myEntriesToProcess.entrySet();
	}
}
