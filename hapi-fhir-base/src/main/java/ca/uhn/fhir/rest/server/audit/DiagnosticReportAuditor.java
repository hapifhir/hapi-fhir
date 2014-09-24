package ca.uhn.fhir.rest.server.audit;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.resource.SecurityEvent.ObjectDetail;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectSensitivityEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectTypeEnum;

public class DiagnosticReportAuditor implements IResourceAuditor<DiagnosticReport> {

	DiagnosticReport myDiagnosticReport;
	
	@Override
	public boolean isAuditable() {
		return myDiagnosticReport != null; 
	}

	@Override
	public String getName() {
		if(myDiagnosticReport != null){
			return myDiagnosticReport.getName().getText().getValue();
		}
		return null;
	}

	@Override
	public IdentifierDt getIdentifier() {
		if(myDiagnosticReport != null){
			return myDiagnosticReport.getIdentifier();
		}
		return null;
	}

	@Override
	public SecurityEventObjectTypeEnum getType() {
		return SecurityEventObjectTypeEnum.OTHER;
	}

	@Override
	public DiagnosticReport getResource() {
		return myDiagnosticReport;
	}

	@Override
	public void setResource(DiagnosticReport theDiagnosticReport) {
		myDiagnosticReport = theDiagnosticReport;
	}

	@Override
	public String getDescription() {
		return null; //name and ID should suffice for audit purposes
	}

	@Override
	public List<ObjectDetail> getDetail() {
		List<ObjectDetail> details = new ArrayList<ObjectDetail>();
		details.add(makeObjectDetail("dateIssued", myDiagnosticReport.getIssued().getValueAsString()));
		details.add(makeObjectDetail("version", myDiagnosticReport.getId().getVersionIdPart()));
		return details;
	}

	@Override
	public SecurityEventObjectSensitivityEnum getSensitivity() {		
		return null; //no sensitivity indicated 
	}
	
	private ObjectDetail makeObjectDetail(String type, String value) {	
		ObjectDetail detail = new ObjectDetail();
		if(type != null)
			detail.setType(type);
		if(value != null)
			detail.setValue(value.getBytes());
		return detail;
	}

}
