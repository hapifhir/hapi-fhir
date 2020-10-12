package ca.uhn.fhir.jpa.dao.expunge;

import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@Service
public class ResourceTableFKProvider {
	@Nonnull
	protected List<ResourceForeignKey> getResourceForeignKeys() {
		// To find all the FKs that need to be included here, run the following SQL in the INFORMATION_SCHEMA:
		// SELECT FKTABLE_NAME, FKCOLUMN_NAME FROM CROSS_REFERENCES WHERE PKTABLE_NAME = 'HFJ_RESOURCE'
		List<ResourceForeignKey> resourceForeignKeys = new ArrayList<>();
		// FIXME KHS hook
//		resourcePidLinks.add(new ResourcePidLink("CDH_LB_REF", "LB_RES_ID"));
//		resourcePidLinks.add(new ResourcePidLink("CDH_LB_REF", "ROOT_RES_ID"));
//		resourcePidLinks.add(new ResourcePidLink("CDH_LB_REF", "SUBS_RES_ID"));
//		resourcePidLinks.add(new ResourcePidLink("CDH_LB_SUB_GROUP", "SUBS_RES_ID"));
//		resourcePidLinks.add(new ResourcePidLink("CDH_LB_WL_SUBS", "SUBS_RES_ID"));
		resourceForeignKeys.add(new ResourceForeignKey("HFJ_FORCED_ID", "RESOURCE_PID"));
		resourceForeignKeys.add(new ResourceForeignKey("HFJ_IDX_CMP_STRING_UNIQ", "RES_ID"));
		resourceForeignKeys.add(new ResourceForeignKey("HFJ_RES_LINK", "SRC_RESOURCE_ID"));
		resourceForeignKeys.add(new ResourceForeignKey("HFJ_RES_LINK", "TARGET_RESOURCE_ID"));
		resourceForeignKeys.add(new ResourceForeignKey("HFJ_RES_PARAM_PRESENT", "RES_ID"));
		resourceForeignKeys.add(new ResourceForeignKey("HFJ_RES_TAG", "RES_ID"));
		resourceForeignKeys.add(new ResourceForeignKey("HFJ_RES_VER", "RES_ID"));
		resourceForeignKeys.add(new ResourceForeignKey("HFJ_RES_VER_PROV", "RES_PID"));
		resourceForeignKeys.add(new ResourceForeignKey("HFJ_SPIDX_COORDS", "RES_ID"));
		resourceForeignKeys.add(new ResourceForeignKey("HFJ_SPIDX_DATE", "RES_ID"));
		resourceForeignKeys.add(new ResourceForeignKey("HFJ_SPIDX_NUMBER", "RES_ID"));
		resourceForeignKeys.add(new ResourceForeignKey("HFJ_SPIDX_QUANTITY", "RES_ID"));
		resourceForeignKeys.add(new ResourceForeignKey("HFJ_SPIDX_STRING", "RES_ID"));
		resourceForeignKeys.add(new ResourceForeignKey("HFJ_SPIDX_TOKEN", "RES_ID"));
		resourceForeignKeys.add(new ResourceForeignKey("HFJ_SPIDX_URI", "RES_ID"));
		resourceForeignKeys.add(new ResourceForeignKey("HFJ_SUBSCRIPTION_STATS", "RES_ID"));
		resourceForeignKeys.add(new ResourceForeignKey("MPI_LINK", "PERSON_PID"));
		resourceForeignKeys.add(new ResourceForeignKey("MPI_LINK", "TARGET_PID"));
		resourceForeignKeys.add(new ResourceForeignKey("NPM_PACKAGE_VER", "BINARY_RES_ID"));
		resourceForeignKeys.add(new ResourceForeignKey("NPM_PACKAGE_VER_RES", "BINARY_RES_ID"));
		resourceForeignKeys.add(new ResourceForeignKey("TRM_CODESYSTEM", "RES_ID"));
		resourceForeignKeys.add(new ResourceForeignKey("TRM_CODESYSTEM_VER", "RES_ID"));
		resourceForeignKeys.add(new ResourceForeignKey("TRM_CONCEPT_MAP", "RES_ID"));
		resourceForeignKeys.add(new ResourceForeignKey("TRM_VALUESET", "RES_ID"));

		return resourceForeignKeys;
	}
}
