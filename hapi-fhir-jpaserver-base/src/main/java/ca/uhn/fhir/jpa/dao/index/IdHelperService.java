package ca.uhn.fhir.jpa.dao.index;

import ca.uhn.fhir.jpa.dao.DaoConfig;
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
	@Autowired(required = true)
	private DaoConfig myDaoConfig;

	public void delete(ForcedId forcedId) {
		myForcedIdDao.delete(forcedId);
	}

	public Long translateForcedIdToPid(String theResourceName, String theResourceId) {
		return translateForcedIdToPids(myDaoConfig, new IdDt(theResourceName, theResourceId), myForcedIdDao).get(0);
	}

	public List<Long> translateForcedIdToPids(IIdType theId) {
		return IdHelperService.translateForcedIdToPids(myDaoConfig, theId, myForcedIdDao);
	}

	static List<Long> translateForcedIdToPids(DaoConfig theDaoConfig, IIdType theId, IForcedIdDao theForcedIdDao) {
		Validate.isTrue(theId.hasIdPart());

		if (theDaoConfig.getResourceClientIdStrategy() != DaoConfig.ClientIdStrategyEnum.ANY && isValidPid(theId)) {
			return Collections.singletonList(theId.getIdPartAsLong());
		} else {
			List<ForcedId> forcedId;
			if (theId.hasResourceType()) {
				forcedId = theForcedIdDao.findByTypeAndForcedId(theId.getResourceType(), theId.getIdPart());
			} else {
				forcedId = theForcedIdDao.findByForcedId(theId.getIdPart());
			}

			if (!forcedId.isEmpty()) {
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
