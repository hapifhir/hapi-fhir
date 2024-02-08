package ca.uhn.fhir.jpa.searchparam.nickname;

import ca.uhn.fhir.jpa.nickname.NicknameSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.param.StringParam;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class NicknameInterceptorTest {

	private NicknameInterceptor createNicknameInterceptor() {
		return new NicknameInterceptor(new NicknameSvc());
	}

	@Test
	public void testExpandForward() throws IOException {
		// setup
		String formalName = "kenneth";
		SearchParameterMap sp = new SearchParameterMap();
		sp.add("name", new StringParam(formalName).setNicknameExpand(true));
		NicknameInterceptor svc = createNicknameInterceptor();

		// execute
		svc.expandNicknames(sp);

		// verify
		String newSearch = sp.toNormalizedQueryString(null);
		assertThat(newSearch).isEqualTo("?name=ken,kendrick,kenneth,kenny");
	}

	@Test
	public void testExpandBackward() throws IOException {
		// setup
		String nickname = "ken";
		SearchParameterMap sp = new SearchParameterMap();
		sp.add("name", new StringParam(nickname).setNicknameExpand(true));
		NicknameInterceptor svc = createNicknameInterceptor();

		// execute
		svc.expandNicknames(sp);

		// verify
		String newSearch = sp.toNormalizedQueryString(null);
		assertThat(newSearch).isEqualTo("?name=ken,kendall,kendrick,kendrik,kenna,kenneth,kenny,kent,mckenna,meaka");
	}

	@Test
	public void testNothingToExpand() throws IOException {
		// setup
		String unusualName = "X Æ A-12";
		SearchParameterMap sp = new SearchParameterMap();
		sp.add("name", new StringParam(unusualName).setNicknameExpand(true));
		NicknameInterceptor svc = createNicknameInterceptor();

		// execute
		svc.expandNicknames(sp);

		// verify
		String newSearch = sp.toNormalizedQueryString(null);
		assertThat(newSearch).isEqualTo("?name=x%20%C3%A6%20a-12");
	}
}
