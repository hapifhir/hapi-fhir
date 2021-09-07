package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.test.utilities.docker.RequiresDocker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@RequiresDocker
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DummyConfig.class})
public class HsTest {

	@Test
	public void test() {
		System.out.println("zoop");
	}

}
