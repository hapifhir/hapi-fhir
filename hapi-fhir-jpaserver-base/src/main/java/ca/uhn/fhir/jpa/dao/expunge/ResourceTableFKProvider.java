package ca.uhn.fhir.jpa.dao.expunge;

import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@Service
public class ResourceTableFKProvider {
	@Nonnull
	public List<ResourceForeignKey> getResourceForeignKeys() {
		List<ResourceForeignKey> retval = new ArrayList<>();
		// Add some secondary related records that don't have foreign keys
		retval.add(new ResourceForeignKey("HFJ_HISTORY_TAG", "RES_ID"));
		retval.add(new ResourceForeignKey("TRM_CODESYSTEM_VER", "RES_ID"));

		// To find all the FKs that need to be included here, run the following SQL in the INFORMATION_SCHEMA:
		// SELECT FKTABLE_NAME, FKCOLUMN_NAME FROM CROSS_REFERENCES WHERE PKTABLE_NAME = 'HFJ_RESOURCE'
		retval.add(new ResourceForeignKey("HFJ_FORCED_ID", "RESOURCE_PID"));
		retval.add(new ResourceForeignKey("HFJ_IDX_CMP_STRING_UNIQ", "RES_ID"));
		retval.add(new ResourceForeignKey("HFJ_RES_LINK", "SRC_RESOURCE_ID"));
		retval.add(new ResourceForeignKey("HFJ_RES_LINK", "TARGET_RESOURCE_ID"));
		retval.add(new ResourceForeignKey("HFJ_RES_PARAM_PRESENT", "RES_ID"));
		retval.add(new ResourceForeignKey("HFJ_RES_TAG", "RES_ID"));
		retval.add(new ResourceForeignKey("HFJ_RES_VER", "RES_ID"));
		retval.add(new ResourceForeignKey("HFJ_RES_VER_PROV", "RES_PID"));
		retval.add(new ResourceForeignKey("HFJ_SPIDX_COORDS", "RES_ID"));
		retval.add(new ResourceForeignKey("HFJ_SPIDX_DATE", "RES_ID"));
		retval.add(new ResourceForeignKey("HFJ_SPIDX_NUMBER", "RES_ID"));
		retval.add(new ResourceForeignKey("HFJ_SPIDX_QUANTITY", "RES_ID"));
		retval.add(new ResourceForeignKey("HFJ_SPIDX_STRING", "RES_ID"));
		retval.add(new ResourceForeignKey("HFJ_SPIDX_TOKEN", "RES_ID"));
		retval.add(new ResourceForeignKey("HFJ_SPIDX_URI", "RES_ID"));
		retval.add(new ResourceForeignKey("HFJ_SUBSCRIPTION_STATS", "RES_ID"));
		retval.add(new ResourceForeignKey("MPI_LINK", "PERSON_PID"));
		retval.add(new ResourceForeignKey("MPI_LINK", "TARGET_PID"));
		retval.add(new ResourceForeignKey("NPM_PACKAGE_VER", "BINARY_RES_ID"));
		retval.add(new ResourceForeignKey("NPM_PACKAGE_VER_RES", "BINARY_RES_ID"));
		retval.add(new ResourceForeignKey("TRM_CODESYSTEM", "RES_ID"));
		retval.add(new ResourceForeignKey("TRM_CODESYSTEM_VER", "RES_ID"));
		retval.add(new ResourceForeignKey("TRM_CONCEPT_MAP", "RES_ID"));
		retval.add(new ResourceForeignKey("TRM_VALUESET", "RES_ID"));

		return retval;
	}
}
