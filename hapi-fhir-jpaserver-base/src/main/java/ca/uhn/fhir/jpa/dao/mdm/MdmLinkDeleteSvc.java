package ca.uhn.fhir.jpa.dao.mdm;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.dao.data.IMdmLinkDao;
import ca.uhn.fhir.jpa.dao.index.IJpaIdHelperService;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MdmLinkDeleteSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(MdmLinkDeleteSvc.class);

	@Autowired
	private IMdmLinkDao myMdmLinkDao;
	@Autowired
	private IJpaIdHelperService myIdHelperService;

	/**
	 * Delete all {@link ca.uhn.fhir.jpa.entity.MdmLink} records with any reference to this resource.  (Used by Expunge.)
	 *
	 * @param theResource
	 * @return the number of records deleted
	 */
	public int deleteWithAnyReferenceTo(IBaseResource theResource) {
		Long pid = myIdHelperService.getPidOrThrowException(theResource.getIdElement());
		int removed = myMdmLinkDao.deleteWithAnyReferenceToPid(pid);
		if (removed > 0) {
			ourLog.info("Removed {} MDM links with references to {}", removed, theResource.getIdElement().toVersionless());
		}
		return removed;
	}

	public int deleteNonRedirectWithAnyReferenceTo(IBaseResource theResource) {
		Long pid = myIdHelperService.getPidOrThrowException(theResource.getIdElement());
		int removed = myMdmLinkDao.deleteWithAnyReferenceToPidAndMatchResultNot(pid, MdmMatchResultEnum.REDIRECT);
		if (removed > 0) {
			ourLog.info("Removed {} non-redirect MDM links with references to {}", removed, theResource.getIdElement().toVersionless());
		}
		return removed;
	}
}
