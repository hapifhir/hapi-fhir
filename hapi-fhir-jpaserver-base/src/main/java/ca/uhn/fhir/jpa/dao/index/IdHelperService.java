package ca.uhn.fhir.jpa.dao.index;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.data.IForcedIdDao;
import ca.uhn.fhir.jpa.model.entity.ForcedId;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.model.search.StorageProcessingMessage;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
public class IdHelperService {
	@Autowired
	protected IForcedIdDao myForcedIdDao;
	@Autowired(required = true)
	private DaoConfig myDaoConfig;
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	public void delete(ForcedId forcedId) {
		myForcedIdDao.delete(forcedId);
	}

	/**
	 * @throws ResourceNotFoundException If the ID can not be found
	 */
	@Nonnull
	public Long translateForcedIdToPid(String theResourceName, String theResourceId) throws ResourceNotFoundException {
		// We only pass 1 input in so only 0..1 will come back
		IdDt id = new IdDt(theResourceName, theResourceId);
		List<Long> matches = translateForcedIdToPids(myDaoConfig, myInterceptorBroadcaster, myForcedIdDao, Collections.singletonList(id));
		assert matches.size() <= 1;
		if (matches.isEmpty()) {
			throw new ResourceNotFoundException(id);
		}
		return matches.get(0);
	}

	public List<Long> translateForcedIdToPids(Collection<IIdType> theId) {
		return IdHelperService.translateForcedIdToPids(myDaoConfig, myInterceptorBroadcaster, myForcedIdDao, theId);
	}

	static List<Long> translateForcedIdToPids(DaoConfig theDaoConfig, IInterceptorBroadcaster theInterceptorBroadcaster, IForcedIdDao theForcedIdDao, Collection<IIdType> theId) {
		theId.forEach(id -> Validate.isTrue(id.hasIdPart()));

		if (theId.isEmpty()) {
			return Collections.emptyList();
		}

		List<Long> retVal = new ArrayList<>();

		ListMultimap<String, String> typeToIds = MultimapBuilder.hashKeys().arrayListValues().build();
		for (IIdType nextId : theId) {
			if (theDaoConfig.getResourceClientIdStrategy() != DaoConfig.ClientIdStrategyEnum.ANY && isValidPid(nextId)) {
				retVal.add(nextId.getIdPartAsLong());
			} else {
				if (nextId.hasResourceType()) {
					typeToIds.put(nextId.getResourceType(), nextId.getIdPart());
				} else {
					typeToIds.put("", nextId.getIdPart());
				}
			}
		}

		for (Map.Entry<String, Collection<String>> nextEntry : typeToIds.asMap().entrySet()) {
			String nextResourceType = nextEntry.getKey();
			Collection<String> nextIds = nextEntry.getValue();
			if (isBlank(nextResourceType)) {

				StorageProcessingMessage msg = new StorageProcessingMessage()
					.setMessage("This search uses unqualified resource IDs (an ID without a resource type). This is less efficient than using a qualified type.");
				HookParams params = new HookParams()
					.add(StorageProcessingMessage.class, msg);
				theInterceptorBroadcaster.callHooks(Pointcut.STORAGE_PROCESSING_MESSAGE, params);

				retVal.addAll(theForcedIdDao.findByForcedId(nextIds));

			} else {
				retVal.addAll(theForcedIdDao.findByTypeAndForcedId(nextResourceType, nextIds));
			}
		}

			return retVal;
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
