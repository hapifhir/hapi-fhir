package ca.uhn.fhir.jpa.searchparam.nickname;

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

	public void load(Reader theReader) throws IOException {
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

	private void add(String theKey, List<String> theValues) {
		myFormalToNick.put(theKey, theValues);
		for (String value : theValues) {
			myNicknameToFormal.putIfAbsent(value, new ArrayList<>());
			myNicknameToFormal.get(value).add(theKey);
		}
	}

	public int size() {
		return myFormalToNick.size();
	}

	public List<String> getNicknamesFromFormalName(String theName) {
		return myFormalToNick.get(theName);
	}

	public List<String> getFormalNamesFromNickname(String theNickname) {
		return myNicknameToFormal.get(theNickname);
	}
}
