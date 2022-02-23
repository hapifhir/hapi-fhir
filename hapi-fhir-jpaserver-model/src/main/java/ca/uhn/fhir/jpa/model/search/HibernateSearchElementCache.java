package ca.uhn.fhir.jpa.model.search;

/*-
 * #%L
 * HAPI FHIR JPA Model
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

import org.hibernate.search.engine.backend.document.DocumentElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provide a lookup of created Hibernate Search DocumentElement entries.
 *
 * The Hibernate Search DocumentElement api only supports create - it does not support fetching an existing element.
 * This class demand-creates object elements for a given path.
 */
public class HibernateSearchElementCache {
	private static final Logger ourLog = LoggerFactory.getLogger(HibernateSearchElementCache.class);
	private final DocumentElement myRoot;
	private final Map<String, DocumentElement> myCache = new HashMap<>();

	/**
	 * Create the helper rooted on the given DocumentElement
	 * @param theRoot the document root
	 */
	public HibernateSearchElementCache(DocumentElement theRoot) {
		this.myRoot = theRoot;
	}

	/**
	 * Fetch or create an Object DocumentElement with thePath from the root element.
	 *
	 * @param thePath the property names of the object path.  E.g. "sp","code","token"
	 * @return the existing or created element
	 */
	public DocumentElement getObjectElement(@Nonnull String... thePath) {
		return getObjectElement(Arrays.asList(thePath));
	}

	/**
	 * Fetch or create an Object DocumentElement with thePath from the root element.
	 *
	 * @param thePath the property names of the object path.  E.g. "sp","code","token"
	 * @return the existing or created element
	 */
	public DocumentElement getObjectElement(@Nonnull List<String> thePath) {
		if (thePath.size() == 0) {
			return myRoot;
		}
		String key = String.join(".", thePath);
		// re-implement computeIfAbsent since we're recursive, and it isn't rentrant.
		DocumentElement result = myCache.get(key);
		if (result == null) {
			DocumentElement parent = getObjectElement(thePath.subList(0, thePath.size() - 1));
			String lastSegment = thePath.get(thePath.size() - 1);
			assert (lastSegment.indexOf('.') == -1);
			result = parent.addObject(lastSegment);
			myCache.put(key, result);
		}
		ourLog.trace("getNode {}: {}", key, result);
		return result;
	}
}
