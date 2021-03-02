package ca.uhn.fhir.jpa.bulk.job;

public class GroupBulkExportJobParametersBuilder extends BulkExportJobParametersBuilder {
	public GroupBulkExportJobParametersBuilder setGroupId(String theGroupId) {
		this.addString(BulkExportJobConfig.GROUP_ID_PARAMETER, theGroupId);
		return this;
	}

	public GroupBulkExportJobParametersBuilder setMdm(boolean theMdm) {
		this.addString(BulkExportJobConfig.EXPAND_MDM_PARAMETER, String.valueOf(theMdm));
		return this;
	}
}
