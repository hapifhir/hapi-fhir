package ca.uhn.fhir.rest.api.server.storage;

public class NotFoundPid extends BaseResourcePersistentId<Long> {
	 public NotFoundPid() {
		  super(null);
	 }

	@Override
	public Long getId() {
		return -1L;
	}
}
