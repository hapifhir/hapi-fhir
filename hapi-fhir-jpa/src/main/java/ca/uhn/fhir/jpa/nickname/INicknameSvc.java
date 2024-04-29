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

import org.springframework.core.io.Resource;

import java.util.Collection;
import java.util.List;

public interface INicknameSvc {

	/**
	 * Set a custom nickname resource to use.
	 * If not used, a default will be used instead.
	 */
	void setNicknameResource(Resource theNicknameResource);

	/**
	 * The number of nicknames in the nickname svc
	 */
	int size();

	/**
	 * If using a custom nickname resource,
	 * processing will keep track of any badly
	 * formatted rows.
	 * These badly formatted rows can be accessed with this api.
	 */
	List<String> getBadRows();

	/**
	 * Gets a list of nicknames for the provided name
	 * @param theName - the provided name
	 * @return - a collection of similar/nicknames
	 */
	Collection<String> getEquivalentNames(String theName);
}
