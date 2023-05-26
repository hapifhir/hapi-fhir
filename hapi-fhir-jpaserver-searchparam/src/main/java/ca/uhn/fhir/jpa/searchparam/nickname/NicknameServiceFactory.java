package ca.uhn.fhir.jpa.searchparam.nickname;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.i18n.Msg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static final Logger ourLog = LoggerFactory.getLogger(NicknameServiceFactory.class);

	private NicknameSvc myNicknameSvc;

	private final NicknameMap myNicknameMap = new NicknameMap();

	private Map<String, Collection<String>> myNameToNickname;

	/**
	 * Returns this factory's NicknameSvc
	 */
	public synchronized NicknameSvc getNicknameSvc() {
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
	public void setNicknameMap(Map<String, Collection<String>> theNameToNicknameListMap) {
		myNameToNickname = theNameToNicknameListMap;

		// we ideally never see this
		// but in case someone wants to redefine the map after construction, we'll allow it
		if (myNicknameSvc != null) {
			ourLog.warn("Resetting Nickname map. Future calls to nickname service will use this new map");
			myNicknameMap.clear();
			populateNicknameMap();
			myNicknameSvc.setNicknameMap(myNicknameMap);
		}
	}

	private void createNicknameSvc() {
		if (myNicknameSvc == null) {
			populateNicknameMap();
			myNicknameSvc = new NicknameSvc(myNicknameMap);
		}
	}

	private void populateNicknameMap() {
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
	}
}
