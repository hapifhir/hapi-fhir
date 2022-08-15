package ca.uhn.fhir.mdm.api;

/*-
 * #%L
 * HAPI FHIR - Master Data Management
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

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MdmLinkEvent implements IModelJson {

	private List<MdmLinkJson> myMdmLinks = new ArrayList<>();

	public List<MdmLinkJson> getMdmLinks() {
		return myMdmLinks;
	}

	public void setMdmLinks(List<MdmLinkJson> theMdmLinks) {
		myMdmLinks = theMdmLinks;
	}

	public MdmLinkEvent addMdmLink(MdmLinkJson theMdmLink) {
		getMdmLinks().add(theMdmLink);
		return this;
	}

	@Override
	public String toString() {
		return "MdmLinkEvent{" +
			"myMdmLinks=" + myMdmLinks +
			'}';
	}
}
