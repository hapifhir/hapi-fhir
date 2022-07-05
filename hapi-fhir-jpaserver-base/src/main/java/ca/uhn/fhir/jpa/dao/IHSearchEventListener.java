package ca.uhn.fhir.jpa.dao;

public interface IHSearchEventListener {

	enum HSearchEventType {
		SEARCH
	}

	void hsearchEvent(HSearchEventType theEventType);
}
