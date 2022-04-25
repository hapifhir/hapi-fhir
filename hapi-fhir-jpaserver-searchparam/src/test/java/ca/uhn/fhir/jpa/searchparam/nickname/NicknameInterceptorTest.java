package ca.uhn.fhir.jpa.searchparam.nickname;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.param.StringParam;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NicknameInterceptorTest {
	@Mock
	NicknameSvc myNicknameSvc;

	@Test
	public void testExpandForward() {
		// setup
		String formalName = "kenneth";
		when(myNicknameSvc.getNicknamesFromFormalNameOrNull(formalName)).thenReturn(List.of("ken","kenny","kendrick"));
		SearchParameterMap sp = new SearchParameterMap();
		sp.add("name", new StringParam(formalName).setNicknameExpand(true));
		NicknameInterceptor svc = new NicknameInterceptor(myNicknameSvc);

		// execute
		svc.expandNicknames(sp);

		// verify
		String newSearch = sp.toNormalizedQueryString(null);
		assertEquals("?name=ken,kendrick,kenneth,kenny", newSearch);
	}

	@Test
	public void testExpandBackward() {
		// setup
		String nickname = "ken";
		when(myNicknameSvc.getNicknamesFromFormalNameOrNull(nickname)).thenReturn(null);
		when(myNicknameSvc.getFormalNamesFromNicknameOrNull(nickname)).thenReturn(List.of("kendall", "kendrick", "kendrik", "kenneth", "kenny", "kent"));
		SearchParameterMap sp = new SearchParameterMap();
		sp.add("name", new StringParam(nickname).setNicknameExpand(true));
		NicknameInterceptor svc = new NicknameInterceptor(myNicknameSvc);

		// execute
		svc.expandNicknames(sp);

		// verify
		String newSearch = sp.toNormalizedQueryString(null);
		assertEquals("?name=ken,kendall,kendrick,kendrik,kenneth,kenny,kent", newSearch);
	}

	@Test
	public void testNothingToExpand() {
		// setup
		String unusualName = "X Æ A-12";
		when(myNicknameSvc.getNicknamesFromFormalNameOrNull(unusualName)).thenReturn(null);
		when(myNicknameSvc.getFormalNamesFromNicknameOrNull(unusualName)).thenReturn(null);
		SearchParameterMap sp = new SearchParameterMap();
		sp.add("name", new StringParam(unusualName).setNicknameExpand(true));
		NicknameInterceptor svc = new NicknameInterceptor(myNicknameSvc);

		// execute
		svc.expandNicknames(sp);

		// verify
		String newSearch = sp.toNormalizedQueryString(null);
		assertEquals("?name=X%20%C3%86%20A-12", newSearch);
	}
}
