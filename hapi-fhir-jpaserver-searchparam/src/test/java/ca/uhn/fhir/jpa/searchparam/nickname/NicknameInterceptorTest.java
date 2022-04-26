package ca.uhn.fhir.jpa.searchparam.nickname;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.param.StringParam;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NicknameInterceptorTest {
	@Test
	public void testExpandForward() throws IOException {
		// setup
		String formalName = "kenneth";
		SearchParameterMap sp = new SearchParameterMap();
		sp.add("name", new StringParam(formalName).setNicknameExpand(true));
		NicknameInterceptor svc = new NicknameInterceptor();

		// execute
		svc.expandNicknames(sp);

		// verify
		String newSearch = sp.toNormalizedQueryString(null);
		assertEquals("?name=ken,kendrick,kenneth,kenny", newSearch);
	}

	@Test
	public void testExpandBackward() throws IOException {
		// setup
		String nickname = "ken";
		SearchParameterMap sp = new SearchParameterMap();
		sp.add("name", new StringParam(nickname).setNicknameExpand(true));
		NicknameInterceptor svc = new NicknameInterceptor();

		// execute
		svc.expandNicknames(sp);

		// verify
		String newSearch = sp.toNormalizedQueryString(null);
		assertEquals("?name=ken,kendall,kendrick,kendrik,kenneth,kenny,kent,mckenna", newSearch);
	}

	@Test
	public void testNothingToExpand() throws IOException {
		// setup
		String unusualName = "X Æ A-12";
		SearchParameterMap sp = new SearchParameterMap();
		sp.add("name", new StringParam(unusualName).setNicknameExpand(true));
		NicknameInterceptor svc = new NicknameInterceptor();

		// execute
		svc.expandNicknames(sp);

		// verify
		String newSearch = sp.toNormalizedQueryString(null);
		assertEquals("?name=x%20%C3%A6%20a-12", newSearch);
	}
}
