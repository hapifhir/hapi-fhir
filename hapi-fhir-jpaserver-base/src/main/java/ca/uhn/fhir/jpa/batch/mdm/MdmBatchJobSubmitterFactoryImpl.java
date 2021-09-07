package ca.uhn.fhir.jpa.batch.mdm;

import ca.uhn.fhir.mdm.api.IMdmBatchJobSubmitterFactory;
import ca.uhn.fhir.mdm.api.IMdmClearJobSubmitter;
import org.springframework.beans.factory.annotation.Autowired;

public class MdmBatchJobSubmitterFactoryImpl implements IMdmBatchJobSubmitterFactory {
	@Autowired
	IMdmClearJobSubmitter myMdmClearJobSubmitter;

	@Override
	public IMdmClearJobSubmitter getClearJobSubmitter() {
		return myMdmClearJobSubmitter;
	}
}
