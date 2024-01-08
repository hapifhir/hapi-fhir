/*-
 * #%L
 * HAPI FHIR JPA Model
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.i18n.Msg;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Nickname service is used to load nicknames
 * via a file that contains rows of comma separated names that are
 * "similar" or nicknames of each other.
 * -
 * If no nickname resource is provided, nicknames/names.csv will be used instead.
 * -
 * If one is to be provided, it must be provided before nickname svc is invoked
 */
public class NicknameSvc implements INicknameSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(NicknameSvc.class);
	private NicknameMap myNicknameMap;

	private Resource myNicknameResource;

	public NicknameSvc() {}

	public void setNicknameResource(Resource theNicknameResource) {
		myNicknameResource = theNicknameResource;
	}

	public int size() {
		ensureMapInitialized();
		return myNicknameMap.size();
	}

	public List<String> getBadRows() {
		ensureMapInitialized();
		return myNicknameMap.getBadRows();
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
		ensureMapInitialized();
		return myNicknameMap.getNicknamesFromFormalName(theName);
	}

	@Nonnull
	List<String> getFormalNamesFromNickname(String theNickname) {
		ensureMapInitialized();
		return myNicknameMap.getFormalNamesFromNickname(theNickname);
	}

	private void ensureMapInitialized() {
		if (myNicknameResource == null) {
			ourLog.debug("Loading defaults");
			myNicknameResource = new ClassPathResource("/nickname/names.csv");
		}

		if (myNicknameMap == null) {
			myNicknameMap = new NicknameMap();
		}
		if (myNicknameMap.isEmpty()) {
			try {
				try (InputStream inputStream = myNicknameResource.getInputStream()) {
					try (Reader reader = new InputStreamReader(inputStream)) {
						myNicknameMap.load(reader);
					}
				}
			} catch (IOException e) {
				throw new ConfigurationException(Msg.code(2234) + "Unable to load nicknames", e);
			}
		}
	}
}
