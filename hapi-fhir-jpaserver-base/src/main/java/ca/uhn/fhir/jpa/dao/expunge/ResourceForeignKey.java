package ca.uhn.fhir.jpa.dao.expunge;

public class ResourceForeignKey {
	public final String table;
	public final String key;

	public ResourceForeignKey(String theTable, String theKey) {
		table = theTable;
		key = theKey;
	}
}
