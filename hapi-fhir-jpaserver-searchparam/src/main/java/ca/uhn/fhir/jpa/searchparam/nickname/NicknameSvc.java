package ca.uhn.fhir.jpa.searchparam.nickname;

/*-
 * #%L
 * HAPI FHIR Search Parameters
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

	public Collection<String> getEquivalentNames(String theName) {
		Set<String> retval = new HashSet<>(getNicknamesFromFormalName(theName));

		if (retval.isEmpty()) {
			List<String> formalNames = getFormalNamesFromNickname(theName);
			retval.addAll(formalNames);
			for (String formalName : formalNames) {
				retval.addAll(getNicknamesFromFormalName(formalName));
			}
		}
		retval.add(theName);
		return retval;
	}

	@Nonnull
	List<String> getNicknamesFromFormalName(String theName) {
		return myNicknameMap.getNicknamesFromFormalName(theName);
	}

	@Nonnull
	List<String> getFormalNamesFromNickname(String theNickname) {
		return myNicknameMap.getFormalNamesFromNickname(theNickname);
	}
}
