package ca.uhn.fhir.jpa.dao.r5.bulkpatch;

import ca.uhn.fhir.batch2.jobs.bulkmodify.patch.BulkPatchJobAppCtx;
import ca.uhn.fhir.batch2.jobs.bulkmodify.patchrewrite.BulkPatchRewriteJobAppCtx;
import ca.uhn.fhir.jpa.dao.r5.BaseJpaR5Test;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {
	BulkPatchJobAppCtx.class,
	BulkPatchRewriteJobAppCtx.class
})
public class BaseBulkPatchR5Test extends BaseJpaR5Test {
}
