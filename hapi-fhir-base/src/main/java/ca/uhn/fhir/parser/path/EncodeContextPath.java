package ca.uhn.fhir.parser.path;

/*-
 * #%L
 * HAPI FHIR - Core Library
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

import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class EncodeContextPath {
	private final List<EncodeContextPathElement> myPath;
	private final ArrayList<EncodeContextPathElement> myResourcePath = new ArrayList<>(10);

	public EncodeContextPath() {
		this(new ArrayList<>(10));
	}

	public EncodeContextPath(String thePath) {
		this();

		StringTokenizer tok = new StringTokenizer(thePath, ".");
		boolean first = true;
		while (tok.hasMoreTokens()) {
			String next = tok.nextToken();
			if (first && next.equals("*")) {
				getPath().add(new EncodeContextPathElement("*", true));
			} else if (isNotBlank(next)) {
				getPath().add(new EncodeContextPathElement(next, Character.isUpperCase(next.charAt(0))));
			}
			first = false;
		}
	}

	public EncodeContextPath(List<EncodeContextPathElement> thePath) {
		myPath = thePath;
	}

	@Override
	public String toString() {
		return myPath.stream().map(t -> t.toString()).collect(Collectors.joining("."));
	}

	public List<EncodeContextPathElement> getPath() {
		return myPath;
	}

	public EncodeContextPath getCurrentResourcePath() {
		EncodeContextPath retVal = null;
		for (int i = myPath.size() - 1; i >= 0; i--) {
			if (myPath.get(i).isResource()) {
				retVal = new EncodeContextPath(myPath.subList(i, myPath.size()));
				break;
			}
		}
		Validate.isTrue(retVal != null);
		return retVal;
	}

	/**
	 * Add an element at the end of the path
	 */
	public void pushPath(String thePathElement, boolean theResource) {
		assert isNotBlank(thePathElement);
		assert !thePathElement.contains(".");
		assert theResource ^ Character.isLowerCase(thePathElement.charAt(0));

		EncodeContextPathElement element = new EncodeContextPathElement(thePathElement, theResource);
		getPath().add(element);
		if (theResource) {
			myResourcePath.add(element);
		}
	}

	/**
	 * Remove the element at the end of the path
	 */
	public void popPath() {
		EncodeContextPathElement removed = getPath().remove(getPath().size() - 1);
		if (removed.isResource()) {
			myResourcePath.remove(myResourcePath.size() - 1);
		}
	}

	public ArrayList<EncodeContextPathElement> getResourcePath() {
		return myResourcePath;
	}

	public String getLeafElementName() {
		return getPath().get(getPath().size() - 1).getName();
	}

	public String getLeafResourceName() {
		return myResourcePath.get(myResourcePath.size() - 1).getName();
	}

	public String getLeafResourcePathFirstField() {
		String retVal = null;
		for (int i = getPath().size() - 1; i >= 0; i--) {
			if (getPath().get(i).isResource()) {
				break;
			} else {
				retVal = getPath().get(i).getName();
			}
		}
		return retVal;
	}

	/**
	 * Tests and returns whether this path starts with {@literal theCurrentResourcePath}
	 *
	 * @param theCurrentResourcePath The path to test
	 * @param theAllowSymmmetrical   If <code>true</code>, this method will return true if {@literal theCurrentResourcePath} starts with this path as well as testing whether this path starts with {@literal theCurrentResourcePath}
	 */
	public boolean startsWith(EncodeContextPath theCurrentResourcePath, boolean theAllowSymmmetrical) {
		for (int i = 0; i < getPath().size(); i++) {
			if (theCurrentResourcePath.getPath().size() == i) {
				return true;
			}
			EncodeContextPathElement expected = getPath().get(i);
			EncodeContextPathElement actual = theCurrentResourcePath.getPath().get(i);
			if (!expected.matches(actual)) {
				return false;
			}
		}

		if (theAllowSymmmetrical) {
			return true;
		}

		return getPath().size() == theCurrentResourcePath.getPath().size();
	}

	public boolean equalsPath(String thePath) {
		EncodeContextPath parsedPath = new EncodeContextPath(thePath);
		return getPath().equals(parsedPath.getPath());
	}


}
