package ca.uhn.fhir.jpa.searchparam.nickname;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.i18n.Msg;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * A factory used to create NicknameSvc objects.
 *
 * Can be used to set custom name -> similar names map
 * If no custom map exists, a default one will be used.
 *
 * See names.csv
 */
public class NicknameServiceFactory {

	private NicknameSvc myNicknameSvc;

	private final NicknameMap myNicknameMap = new NicknameMap();

	private Map<String, Collection<String>> myNameToNickname;

	/**
	 * Returns this factory's NicknameSvc
	 */
	public NicknameSvc getNicknameSvc() {
		if (myNicknameSvc == null) {
			createNicknameSvc();
		}
		return myNicknameSvc;
	}

	/**
	 * Set a custom nickname -> list of alternative names map.
	 *
	 * This map (if populated) will be used instead of the defaults.
	 */
	public void setNicknameMap(Map<String, Collection<String>> theMap) {
		myNameToNickname = theMap;
	}

	private void createNicknameSvc() {
		if (myNicknameSvc == null) {
			if (myNameToNickname == null || myNameToNickname.isEmpty()) {
				// default
				try {
					Resource nicknameCsvResource = new ClassPathResource("/nickname/names.csv");
					try (InputStream inputStream = nicknameCsvResource.getInputStream()) {
						try (Reader reader = new InputStreamReader(inputStream)) {
							myNicknameMap.load(reader);
						}
					}
				} catch (IOException e) {
					throw new ConfigurationException(Msg.code(2234) + "Unable to load nicknames", e);
				}
			} else {
				for (Map.Entry<String, Collection<String>> entry : myNameToNickname.entrySet()) {
					myNicknameMap.add(entry.getKey(), new ArrayList<>(entry.getValue()));
				}
			}
			myNicknameSvc = new NicknameSvc(myNicknameMap);
		}
	}
}
