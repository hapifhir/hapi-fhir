package ca.uhn.fhir.jpa.dao.expunge;

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

import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.rules.config.MdmSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ResourceTableFKProvider {
	@Autowired(required = false)
	IMdmSettings myMdmSettings;

	@Nonnull
	public List<ResourceForeignKey> getResourceForeignKeys() {
		List<ResourceForeignKey> retval = new ArrayList<>();

		// To find all the FKs that need to be included here, run the following SQL in the INFORMATION_SCHEMA:
		// SELECT FKTABLE_NAME, FKCOLUMN_NAME FROM CROSS_REFERENCES WHERE PKTABLE_NAME = 'HFJ_RESOURCE'

		// Add some secondary related records that don't have foreign keys
		retval.add(new ResourceForeignKey("HFJ_HISTORY_TAG", "RES_ID"));//NOT covered by index.
		retval.add(new ResourceForeignKey("HFJ_RES_VER_PROV", "RES_PID"));

		//These have the possibility of touching all resource types.
		retval.add(new ResourceForeignKey("HFJ_FORCED_ID", "RESOURCE_PID"));
		retval.add(new ResourceForeignKey("HFJ_IDX_CMP_STRING_UNIQ", "RES_ID"));
		retval.add(new ResourceForeignKey("HFJ_IDX_CMB_TOK_NU", "RES_ID"));
		retval.add(new ResourceForeignKey("HFJ_RES_LINK", "SRC_RESOURCE_ID"));
		retval.add(new ResourceForeignKey("HFJ_RES_LINK", "TARGET_RESOURCE_ID"));
		retval.add(new ResourceForeignKey("HFJ_RES_PARAM_PRESENT", "RES_ID"));
		retval.add(new ResourceForeignKey("HFJ_RES_TAG", "RES_ID"));
		retval.add(new ResourceForeignKey("HFJ_RES_VER", "RES_ID"));
		retval.add(new ResourceForeignKey("HFJ_SPIDX_COORDS", "RES_ID"));
		retval.add(new ResourceForeignKey("HFJ_SPIDX_DATE", "RES_ID"));
		retval.add(new ResourceForeignKey("HFJ_SPIDX_NUMBER", "RES_ID"));
		retval.add(new ResourceForeignKey("HFJ_SPIDX_QUANTITY", "RES_ID"));
		retval.add(new ResourceForeignKey("HFJ_SPIDX_QUANTITY_NRML", "RES_ID"));
		retval.add(new ResourceForeignKey("HFJ_SPIDX_STRING", "RES_ID"));
		retval.add(new ResourceForeignKey("HFJ_SPIDX_TOKEN", "RES_ID"));
		retval.add(new ResourceForeignKey("HFJ_SPIDX_URI", "RES_ID"));
		retval.add(new ResourceForeignKey("MPI_LINK", "GOLDEN_RESOURCE_PID"));
		retval.add(new ResourceForeignKey("MPI_LINK", "TARGET_PID"));
		retval.add(new ResourceForeignKey("MPI_LINK", "PERSON_PID"));

		//These only touch certain resource types.
		retval.add(new ResourceForeignKey("TRM_CODESYSTEM_VER", "RES_ID"));
		retval.add(new ResourceForeignKey("TRM_CODESYSTEM", "RES_ID"));
		retval.add(new ResourceForeignKey("TRM_VALUESET", "RES_ID"));
		retval.add(new ResourceForeignKey("TRM_CONCEPT_MAP", "RES_ID"));
		retval.add(new ResourceForeignKey("NPM_PACKAGE_VER", "BINARY_RES_ID"));
		retval.add(new ResourceForeignKey("NPM_PACKAGE_VER_RES", "BINARY_RES_ID"));

		retval.add(new ResourceForeignKey("HFJ_SUBSCRIPTION_STATS", "RES_ID"));

		return retval;
	}
	@Nonnull
	public List<ResourceForeignKey> getResourceForeignKeysByResourceType(String theResourceType) {
		List<ResourceForeignKey> retval = new ArrayList<>();
		//These have the possibility of touching all resource types.
		retval.add(new ResourceForeignKey("HFJ_HISTORY_TAG", "RES_ID"));
		retval.add(new ResourceForeignKey("HFJ_RES_VER_PROV", "RES_PID"));
		retval.add(new ResourceForeignKey("HFJ_FORCED_ID", "RESOURCE_PID"));
		retval.add(new ResourceForeignKey("HFJ_IDX_CMP_STRING_UNIQ", "RES_ID"));
		retval.add(new ResourceForeignKey("HFJ_IDX_CMB_TOK_NU", "RES_ID"));
		retval.add(new ResourceForeignKey("HFJ_RES_LINK", "SRC_RESOURCE_ID"));
		retval.add(new ResourceForeignKey("HFJ_RES_LINK", "TARGET_RESOURCE_ID"));
		retval.add(new ResourceForeignKey("HFJ_RES_PARAM_PRESENT", "RES_ID"));
		retval.add(new ResourceForeignKey("HFJ_RES_TAG", "RES_ID"));//TODO GGG: Res_ID + TAG_ID? is that enough?
		retval.add(new ResourceForeignKey("HFJ_RES_VER", "RES_ID"));//TODO GGG: RES_ID + updated? is that enough?
		retval.add(new ResourceForeignKey("HFJ_SPIDX_COORDS", "RES_ID"));
		retval.add(new ResourceForeignKey("HFJ_SPIDX_DATE", "RES_ID"));
		retval.add(new ResourceForeignKey("HFJ_SPIDX_NUMBER", "RES_ID"));
		retval.add(new ResourceForeignKey("HFJ_SPIDX_QUANTITY", "RES_ID"));
		retval.add(new ResourceForeignKey("HFJ_SPIDX_QUANTITY_NRML", "RES_ID"));
		retval.add(new ResourceForeignKey("HFJ_SPIDX_STRING", "RES_ID"));
		retval.add(new ResourceForeignKey("HFJ_SPIDX_TOKEN", "RES_ID"));
		retval.add(new ResourceForeignKey("HFJ_SPIDX_URI", "RES_ID"));

		if (myMdmSettings != null &&  myMdmSettings.isEnabled()) {
			retval.add(new ResourceForeignKey("MPI_LINK", "GOLDEN_RESOURCE_PID"));//NOT covered by index.
			retval.add(new ResourceForeignKey("MPI_LINK", "TARGET_PID"));//Possibly covered, partial index
			retval.add(new ResourceForeignKey("MPI_LINK", "PERSON_PID"));//TODO GGG: I don't even think we need this... this field is deprecated, and the deletion is covered by GOLDEN_RESOURCE_PID
		}

		switch (theResourceType.toLowerCase()) {
			case "binary":
				retval.add(new ResourceForeignKey("NPM_PACKAGE_VER", "BINARY_RES_ID"));//Not covered
				retval.add(new ResourceForeignKey("NPM_PACKAGE_VER_RES", "BINARY_RES_ID"));//Not covered
				break;
			case "subscription":
				retval.add(new ResourceForeignKey("HFJ_SUBSCRIPTION_STATS", "RES_ID"));//Covered by index.
				break;
			case "codesystem":
				retval.add(new ResourceForeignKey("TRM_CODESYSTEM_VER", "RES_ID"));//Not covered
				retval.add(new ResourceForeignKey("TRM_CODESYSTEM", "RES_ID"));//Not covered
				break;
			case "valueset":
				retval.add(new ResourceForeignKey("TRM_VALUESET", "RES_ID"));//Not covered
				break;
			case "conceptmap":
				retval.add(new ResourceForeignKey("TRM_CONCEPT_MAP", "RES_ID"));//Not covered
				break;
			default:
		}
		return retval;
	}

}
