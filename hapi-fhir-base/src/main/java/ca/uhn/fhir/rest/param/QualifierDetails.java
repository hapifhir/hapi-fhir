package ca.uhn.fhir.rest.param;

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

import java.util.Set;

public class QualifierDetails {

	private String myColonQualifier;
	private String myDotQualifier;
	private String myParamName;
	private String myWholeQualifier;

	public boolean passes(Set<String> theQualifierWhitelist, Set<String> theQualifierBlacklist) {
		if (theQualifierWhitelist != null) {
			if (!theQualifierWhitelist.contains(".*")) {
				if (myDotQualifier != null) {
					if (!theQualifierWhitelist.contains(myDotQualifier)) {
						return false;
					}
				} else {
					if (!theQualifierWhitelist.contains(".")) {
						return false;
					}
				}
			}
		}
		if (theQualifierBlacklist != null) {
			if (myDotQualifier != null) {
				if (theQualifierBlacklist.contains(myDotQualifier)) {
					return false;
				}
			}
			if (myColonQualifier != null) {
				if (theQualifierBlacklist.contains(myColonQualifier)) {
					return false;
				}
			}
		}

		return true;
	}

	public void setParamName(String theParamName) {
		myParamName = theParamName;
	}

	public String getParamName() {
		return myParamName;
	}

	public void setColonQualifier(String theColonQualifier) {
		myColonQualifier = theColonQualifier;
	}

	public void setDotQualifier(String theDotQualifier) {
		myDotQualifier = theDotQualifier;
	}

	public String getWholeQualifier() {
		return myWholeQualifier;
	}

	public void setWholeQualifier(String theWholeQualifier) {
		myWholeQualifier = theWholeQualifier;
	}

	
	public static QualifierDetails extractQualifiersFromParameterName(String theParamName) {
		QualifierDetails retVal = new QualifierDetails();
		if (theParamName == null || theParamName.length() == 0) {
			return retVal;
		}

		int dotIdx = -1;
		int colonIdx = -1;
		for (int idx = 0; idx < theParamName.length(); idx++) {
			char nextChar = theParamName.charAt(idx);
			if (nextChar == '.' && dotIdx == -1) {
				dotIdx = idx;
			} else if (nextChar == ':' && colonIdx == -1) {
				colonIdx = idx;
			}
		}

		if (dotIdx != -1 && colonIdx != -1) {
			if (dotIdx < colonIdx) {
				retVal.setDotQualifier(theParamName.substring(dotIdx, colonIdx));
				retVal.setColonQualifier(theParamName.substring(colonIdx));
				retVal.setParamName(theParamName.substring(0, dotIdx));
				retVal.setWholeQualifier(theParamName.substring(dotIdx));
			} else {
				retVal.setColonQualifier(theParamName.substring(colonIdx, dotIdx));
				retVal.setDotQualifier(theParamName.substring(dotIdx));
				retVal.setParamName(theParamName.substring(0, colonIdx));
				retVal.setWholeQualifier(theParamName.substring(colonIdx));
			}
		} else if (dotIdx != -1) {
			retVal.setDotQualifier(theParamName.substring(dotIdx));
			retVal.setParamName(theParamName.substring(0, dotIdx));
			retVal.setWholeQualifier(theParamName.substring(dotIdx));
		} else if (colonIdx != -1) {
			retVal.setColonQualifier(theParamName.substring(colonIdx));
			retVal.setParamName(theParamName.substring(0, colonIdx));
			retVal.setWholeQualifier(theParamName.substring(colonIdx));
		} else {
			retVal.setParamName(theParamName);
			retVal.setColonQualifier(null);
			retVal.setDotQualifier(null);
			retVal.setWholeQualifier(null);
		}

		return retVal;
	}


	
}
