/*-
 * #%L
 * HAPI FHIR Search Parameters
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.nickname;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NicknameMap {
	private final Map<String, List<String>> myFormalToNick = new HashMap<>();
	private final Map<String, List<String>> myNicknameToFormal = new HashMap<>();

	void load(Reader theReader) throws IOException {
		try (BufferedReader reader = new BufferedReader(theReader)) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				String key = parts[0];
				List<String> values = new ArrayList<>(Arrays.asList(parts).subList(1, parts.length));
				add(key, values);
			}
		}
	}

	public void clear() {
		myFormalToNick.clear();
		myNicknameToFormal.clear();
	}

	public void add(String theKey, List<String> theValues) {
		myFormalToNick.put(theKey, theValues);
		for (String value : theValues) {
			myNicknameToFormal.putIfAbsent(value, new ArrayList<>());
			myNicknameToFormal.get(value).add(theKey);
		}
	}

	public int size() {
		return myFormalToNick.size();
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	@Nonnull
	public List<String> getNicknamesFromFormalName(String theName) {
		List<String> result = myFormalToNick.get(theName);
		return result == null ? new ArrayList<>() : result;
	}

	@Nonnull
	public List<String> getFormalNamesFromNickname(String theNickname) {
		List<String> result = myNicknameToFormal.get(theNickname);
		return result == null ? new ArrayList<>() : result;
	}
}
