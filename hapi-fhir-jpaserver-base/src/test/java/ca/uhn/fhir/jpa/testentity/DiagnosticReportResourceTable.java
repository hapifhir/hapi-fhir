package ca.uhn.fhir.jpa.testentity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import ca.uhn.fhir.jpa.entity.BaseResourceTable;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;

@Entity
@DiscriminatorValue("DIAGNOSTICREPORT")
public class DiagnosticReportResourceTable extends BaseResourceTable<DiagnosticReport> {

	@Override
	public Class<DiagnosticReport> getResourceType() {
		return DiagnosticReport.class;
	}

}
