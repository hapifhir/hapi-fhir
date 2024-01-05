/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.mdm.dao;

import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Creates a new {@link IMdmLink} either with the current {@link IMdmSettings#getRuleVersion()} or with a null version.
 * <br>
 * **Use extreme caution**.  The recommended practice is to only use (@link #newMdmLink()} when WRITING an MDM record.
 * <br>
 * Otherwise, there is the risk that code that searches for an MDM record fill fail to locate it due to a version mismatch.
 * <br>
 * Database code makes use of SpringData {@link org.springframework.data.domain.Example} queries.
 */
public class MdmLinkFactory<M extends IMdmLink> {
	private final IMdmSettings myMdmSettings;
	private final IMdmLinkImplFactory<M> myMdmLinkImplFactory;

	@Autowired
	public MdmLinkFactory(IMdmSettings theMdmSettings, IMdmLinkImplFactory<M> theMdmLinkImplFactory) {
		myMdmSettings = theMdmSettings;
		myMdmLinkImplFactory = theMdmLinkImplFactory;
	}

	/**
	 * Create a new {@link IMdmLink}, populating it with the version of the ruleset used to create it.
	 *
	 * Use this method **only** when writing a new MDM record.
	 *
	 * @return the new {@link IMdmLink}
	 */
	public M newMdmLink() {
		M retval = myMdmLinkImplFactory.newMdmLinkImpl();
		retval.setVersion(myMdmSettings.getRuleVersion());
		return retval;
	}

	/**
	 * Creating a new {@link IMdmLink} with the version deliberately omitted.  It will return as null.
	 *
	 * This is the recommended use when querying for any MDM records
	 */
	public M newMdmLinkVersionless() {
		return myMdmLinkImplFactory.newMdmLinkImpl();
	}
}
