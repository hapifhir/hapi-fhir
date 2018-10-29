package ca.uhn.fhir.jpa.service;

import ca.uhn.fhir.jpa.dao.data.IForcedIdDao;
import ca.uhn.fhir.jpa.entity.ForcedId;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class IdHelperService {
	@Autowired
	protected IForcedIdDao myForcedIdDao;

	public void delete(ForcedId forcedId) {
		myForcedIdDao.delete(forcedId);
	}

	public Long translateForcedIdToPid(String theResourceName, String theResourceId) {
		return translateForcedIdToPids(new IdDt(theResourceName, theResourceId)).get(0);
	}

	public List<Long> translateForcedIdToPids(IIdType theId) {
		return IdHelperService.translateForcedIdToPids(theId, myForcedIdDao);
	}

	// TODO KHS why static?
	public static Long translateForcedIdToPid(String theResourceName, String theResourceId, IForcedIdDao
		theForcedIdDao) {
		return IdHelperService.translateForcedIdToPids(new IdDt(theResourceName, theResourceId), theForcedIdDao).get(0);
	}

	static List<Long> translateForcedIdToPids(IIdType theId, IForcedIdDao theForcedIdDao) {
		Validate.isTrue(theId.hasIdPart());

		if (isValidPid(theId)) {
			return Collections.singletonList(theId.getIdPartAsLong());
		} else {
			List<ForcedId> forcedId;
			if (theId.hasResourceType()) {
				forcedId = theForcedIdDao.findByTypeAndForcedId(theId.getResourceType(), theId.getIdPart());
			} else {
				forcedId = theForcedIdDao.findByForcedId(theId.getIdPart());
			}

			if (forcedId.isEmpty() == false) {
				List<Long> retVal = new ArrayList<>(forcedId.size());
				for (ForcedId next : forcedId) {
					retVal.add(next.getResourcePid());
				}
				return retVal;
			} else {
				throw new ResourceNotFoundException(theId);
			}
		}
	}

	public String translatePidIdToForcedId(String theResourceType, Long theId) {
		ForcedId forcedId = myForcedIdDao.findByResourcePid(theId);
		if (forcedId != null) {
			return forcedId.getResourceType() + '/' + forcedId.getForcedId();
		} else {
			return theResourceType + '/' + theId.toString();
		}
	}

	public static boolean isValidPid(IIdType theId) {
		if (theId == null || theId.getIdPart() == null) {
			return false;
		}
		String idPart = theId.getIdPart();
		for (int i = 0; i < idPart.length(); i++) {
			char nextChar = idPart.charAt(i);
			if (nextChar < '0' || nextChar > '9') {
				return false;
			}
		}
		return true;
	}
}
