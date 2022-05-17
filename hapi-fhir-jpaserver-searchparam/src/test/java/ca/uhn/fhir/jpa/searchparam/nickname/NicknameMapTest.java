package ca.uhn.fhir.jpa.searchparam.nickname;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
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
		assertThat(map.getNicknamesFromFormalNameOrNull("kenneth"), containsInAnyOrder("ken", "kenny", "kendrick"));
		assertThat(map.getFormalNamesFromNicknameOrNull("ken"), containsInAnyOrder("kendall", "kendrick", "kendrik", "kenneth", "kenny", "kent"));
	}
}
