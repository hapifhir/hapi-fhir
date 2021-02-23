package ca.uhn.fhir.jpa.bulk.job;

public class GroupBulkExportJobParametersBuilder extends BulkExportJobParametersBuilder {
	public GroupBulkExportJobParametersBuilder setGroupId(String theGroupId) {
		this.addString("groupId", theGroupId);
		return this;
	}
}
