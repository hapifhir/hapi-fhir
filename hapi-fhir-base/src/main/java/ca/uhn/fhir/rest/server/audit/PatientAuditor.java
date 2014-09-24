package ca.uhn.fhir.rest.server.audit;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.SecurityEvent.ObjectDetail;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectSensitivityEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectTypeEnum;

public class PatientAuditor implements IResourceAuditor<Patient> {
	
	private Patient myPatient;

	@Override
	public boolean isAuditable() {
		return myPatient != null; //if we have a patient, we want to audit it
	}

	@Override
	public Patient getResource() {
		return myPatient;
	}

	@Override
	public void setResource(Patient thePatient) {
		myPatient = thePatient;
	}

	@Override
	public String getName() {
		if(myPatient != null){
			return myPatient.getNameFirstRep().getText().getValue();
		}
		return null;
	}

	@Override
	public IdentifierDt getIdentifier() {
		if(myPatient != null){
			return myPatient.getIdentifierFirstRep();
		}
		return null;
	}

	@Override
	public SecurityEventObjectTypeEnum getType() {	 
		return SecurityEventObjectTypeEnum.PERSON;
	}

	@Override
	public String getDescription() {
		return null; //name + identifier ought to suffice?
	}

	@Override
	public List<ObjectDetail> getDetail() {
		if(myPatient != null){
			List<IdentifierDt> ids = myPatient.getIdentifier();
			if(ids != null && !ids.isEmpty()){
				List<ObjectDetail> detailList = new ArrayList<ObjectDetail>(ids.size());
				for(IdentifierDt id: ids){
					ObjectDetail detail = new ObjectDetail();
					detail.setType(id.getSystem().getValueAsString());
					try {
						detail.setValue(id.getValue().getValueAsString().getBytes("UTF-8"));
					} catch (UnsupportedEncodingException e) {
						detail.setValue(id.getValue().getValueAsString().getBytes());
					}
					detailList.add(detail);
				}
				return detailList;
			}
			
		}
		return null;
	}

	@Override
	public SecurityEventObjectSensitivityEnum getSensitivity() {		
		return null; //override to include things like locked patient records
	}

}
