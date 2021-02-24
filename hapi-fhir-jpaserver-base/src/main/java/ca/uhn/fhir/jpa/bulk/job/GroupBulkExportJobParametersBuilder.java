package ca.uhn.fhir.jpa.bulk.job;

public class GroupBulkExportJobParametersBuilder extends BulkExportJobParametersBuilder {
	public GroupBulkExportJobParametersBuilder setGroupId(String theGroupId) {
		this.addString(BulkExportJobConfig.GROUP_ID_PARAMETER, theGroupId);
		return this;
	}
}
