package ca.uhn.fhir.jpa.searchparam.nickname;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class NicknameSvc {
	private final NicknameMap myNicknameMap = new NicknameMap();

	public NicknameSvc() throws IOException {
		Resource nicknameCsvResource = new ClassPathResource("/nickname/names.csv");
		try (InputStream inputStream = nicknameCsvResource.getInputStream()) {
			try (Reader reader = new InputStreamReader(inputStream)) {
				myNicknameMap.load(reader);
			}
		}
	}

	public int size() {
		return myNicknameMap.size();
	}

	public List<String> getEquivalentNames(String theName) {
		List<String> retval = new ArrayList<>();
		retval.add(theName);

		List<String> expansions;
		expansions = getNicknamesFromFormalNameOrNull(theName);
		if (expansions != null) {
			retval.addAll(expansions);
		} else {
			expansions = getFormalNamesFromNicknameOrNull(theName);
			if (expansions != null) {
				retval.addAll(expansions);
			}
		}
		return retval;
	}

	List<String> getNicknamesFromFormalNameOrNull(String theName) {
		return myNicknameMap.getNicknamesFromFormalNameOrNull(theName);
	}

	List<String> getFormalNamesFromNicknameOrNull(String theNickname) {
		return myNicknameMap.getFormalNamesFromNicknameOrNull(theNickname);
	}
}
