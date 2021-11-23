package ca.uhn.fhir.jpa.mdm.broker;

import ca.uhn.fhir.jpa.subscription.api.ISubscriptionMessageKeySvc;
import ca.uhn.fhir.mdm.model.CanonicalEID;
import ca.uhn.fhir.mdm.util.EIDHelper;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.List;

@Service
public class MdmMessageKeySvc implements ISubscriptionMessageKeySvc {
	@Autowired
	private EIDHelper myEIDHelper;

	@Nullable
	@Override
	public String getMessageKeyOrNull(IBaseResource theTargetResource) {
		List<CanonicalEID> eidList = myEIDHelper.getExternalEid(theTargetResource);
		if (eidList.isEmpty()) {
			return null;
		}
		return eidList.get(0).getValue();
	}
}
