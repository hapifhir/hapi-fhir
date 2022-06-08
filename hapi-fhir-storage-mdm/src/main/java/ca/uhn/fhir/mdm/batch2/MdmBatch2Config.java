package ca.uhn.fhir.mdm.batch2;

import ca.uhn.fhir.mdm.batch2.clear.MdmClearAppCtx;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({MdmClearAppCtx.class})
public class MdmBatch2Config {
}
