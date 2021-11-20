package ca.uhn.fhir.jpa.mdm.broker;

import ca.uhn.fhir.jpa.subscription.api.ISubscriptionMessageHasher;
import ca.uhn.fhir.mdm.model.CanonicalEID;
import ca.uhn.fhir.mdm.util.EIDHelper;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MdmMessageHasher implements ISubscriptionMessageHasher {
	@Autowired
	private EIDHelper myEIDHelper;

	@Override
	public Integer getMessageHashOrNull(IBaseResource theTargetResource) {
		List<CanonicalEID> eidList = myEIDHelper.getExternalEid(theTargetResource);
		if (eidList.isEmpty()) {
			return null;
		}
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		// FIXME document if there is more than one eid, we hash all of them (should we just hash the first one?
		//  should we sort them first?  Should more than one be an error?  Does it really matter?)
		eidList.forEach(hashCodeBuilder::append);
		return hashCodeBuilder.toHashCode();
	}
}
