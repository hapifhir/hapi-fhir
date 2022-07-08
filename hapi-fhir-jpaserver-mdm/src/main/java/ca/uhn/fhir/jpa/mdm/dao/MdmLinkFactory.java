package ca.uhn.fhir.jpa.mdm.dao;

/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
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

import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.jpa.entity.MdmLink;
import org.springframework.beans.factory.annotation.Autowired;

public class MdmLinkFactory {
	private final IMdmSettings myMdmSettings;

	@Autowired
	public MdmLinkFactory(IMdmSettings theMdmSettings) {
		myMdmSettings = theMdmSettings;
	}

	/**
	 * Create a new {@link MdmLink}, populating it with the version of the ruleset used to create it.
	 *
	 * @return the new {@link MdmLink}
	 */
	public MdmLink newMdmLink() {
		return new MdmLink(myMdmSettings.getRuleVersion());
	}
}
