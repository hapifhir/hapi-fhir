package ca.uhn.fhir.jpa.nickname;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class NicknameMapTest {
	@Test
	public void testLoad() throws IOException {
		String testData = """
kendall,ken,kenny
kendra,kenj,kenji,kay,kenny
kendrick,ken,kenny
kendrik,ken,kenny
kenneth,ken,kenny,kendrick
kenny,ken,kenneth
kent,ken,kenny,kendrick
			""";
		NicknameMap map = new NicknameMap();
		map.load(new StringReader(testData));
		assertEquals(7, map.size());
		assertThat(map.getNicknamesFromFormalName("kenneth")).containsExactlyInAnyOrder("ken", "kenny", "kendrick");
		assertThat(map.getFormalNamesFromNickname("ken")).containsExactlyInAnyOrder("kendall", "kendrick", "kendrik", "kenneth", "kenny", "kent");
	}
}
