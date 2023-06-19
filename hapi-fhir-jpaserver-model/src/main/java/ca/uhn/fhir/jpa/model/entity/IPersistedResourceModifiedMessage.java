package ca.uhn.fhir.jpa.model.entity;

public interface IPersistedResourceModifiedMessage {

	IPersistedResourceModifiedMessagePK getPersistedResourceModifiedMessagePk();

	String getResourceType();

}
