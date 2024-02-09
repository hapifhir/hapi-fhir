package ca.uhn.fhir.rest.api;

import ca.uhn.fhir.util.TestUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class QualifiedParamListTest {

	@Test
	public void testSplit1() {
		List<String> actual = QualifiedParamList.splitQueryStringByCommasIgnoreEscape(null,"aaa");
		assertThat(actual).hasSize(1);
		assertThat(actual.get(0)).isEqualTo("aaa");
	}
	
	@Test
	public void testSplit2() {
		List<String> actual = QualifiedParamList.splitQueryStringByCommasIgnoreEscape(null,"aaa,bbb");
		assertThat(actual).hasSize(2);
		assertThat(actual.get(0)).isEqualTo("aaa");
		assertThat(actual.get(1)).isEqualTo("bbb");
	}
	
	@Test
	public void testSplit3() {
		List<String> actual = QualifiedParamList.splitQueryStringByCommasIgnoreEscape(null,"aaa,b\\,bb");
		System.out.println(actual);
		assertThat(actual).hasSize(2);
		assertThat(actual.get(0)).isEqualTo("aaa");
		assertThat(actual.get(1)).isEqualTo("b,bb");
	}


	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

}
