package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.BinaryUtil;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Binary;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class BinaryUtilTest {

	@Test
	void testGetOrCreateDataWithExternalizedBinary() {
		FhirContext myCtx = FhirContext.forR4();
		String binaryString = "{\"resourceType\":\"Binary\",\n" +
			"\"contentType\":\"application/fhir+json\",\n" +
			"\"_data\":{\"extension\":[{\"url\":\"http://hapifhir.io/fhir/StructureDefinition/externalized-binary-id\",\"valueString\":\"IC1RlOxVshyNxKouxzCMeJXHrT7AFvkRmGLFNhozh9XIhhbwPuY9NcD96J3ZGam7fdCWydC8voS5SzJzsLUCeU5CgLDCJoAVXVOF\"}]}}\n";
		Binary iBaseBinary = myCtx.newJsonParser().parseResource(Binary.class, binaryString);
		byte[] orCreateData = BinaryUtil.getOrCreateData(myCtx, iBaseBinary).getValue();
		String reuslt = new String(orCreateData, StandardCharsets.UTF_8);
		assertNotNull(orCreateData);
	}
}
