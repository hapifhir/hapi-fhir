package ca.uhn.fhir.tinder.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;
import org.codehaus.plexus.util.StringUtils;

public class ValueSetTm {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ValueSetTm.class);

	private String myClassName;
	private List<Code> myCodes = new ArrayList<Code>();
	private String myDescription;
	private String myId;
	private String myName;
	private Set<String> myCodeValues = new HashSet<String>();

	public void addConcept(String theSystem, String theCode, String theText, String theDefinition) {
		String key = theSystem + "|" + theCode;
		if (myCodeValues.contains(key)) {
			return;
		}
		myCodeValues.add(key);

		myCodes.add(new Code(theSystem, theCode, theText, theDefinition));
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ValueSetTm other = (ValueSetTm) obj;

		String id1 = myId != null && myId.isEmpty() == false ? myId : myName;
		String id2 = other.myId != null && other.myId.isEmpty() == false ? other.myId : other.myName;
		id1 = StringUtils.defaultString(id1);
		id2 = StringUtils.defaultString(id2);
		return id1.equals(id2);
	}

	public String getClassName() {
		return myClassName;
	}

	public List<Code> getCodes() {
		return Collections.unmodifiableList(myCodes);
	}

	public String getDescription() {
		return StringUtils.defaultString(myDescription);
	}

	public String getId() {
		return myId;
	}

	public String getName() {
		return StringUtils.defaultString(myName);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((myId == null) ? 0 : myId.hashCode());
		return result;
	}

	public void setClassName(String theClassName) {
		myClassName = theClassName;
	}

	public void setCodes(List<Code> theCodes) {
		myCodes = theCodes;
	}

	public void setDescription(String theDescription) {
		myDescription = theDescription;
	}

	public void setId(String theId) {
		myId = theId;
	}

	public void setName(String theName) {
		myName = theName;
	}

	public class Code {

		private String myCode;
		private String myDefinition;
		private String myDisplay;
		private String mySystem;

		private Code(String theSystem, String theCode, String theDisplay, String theDefinition) {
			mySystem = theSystem;
			myCode = theCode.trim();
			myDisplay = theDisplay;
			myDefinition = theDefinition;
		}

		public String getSystem() {
			return mySystem;
		}

		public String getCode() {
			return myCode;
		}

		public String getCodeEnumValue() {
			String retVal = myDisplay;
			if (StringUtils.isBlank(retVal)) {
				retVal = myCode;
				if (Character.isDigit(myCode.charAt(0))) {
					if (StringUtils.isNotBlank(myDefinition)) {
						if (myDefinition.length() < 100) {
							String newValue = myDefinition;
							if (newValue.contains(",")) {
								newValue = newValue.substring(0, newValue.indexOf(','));
							}
							if (newValue.contains("(")) {
								newValue = newValue.substring(0, newValue.indexOf('('));
							}
							newValue = newValue.replace(" / ", " OR ");
							while (!Character.isLetterOrDigit(newValue.charAt(newValue.length() - 1))) {
								newValue = newValue.substring(0, newValue.length() - 1);
							}
							ourLog.info("[{}] Replacing numeric code {} with description: {}", new Object[] { myName, retVal, newValue });
							retVal = newValue;
						}
					}
				}
			}

			if ("=".equals(retVal)) {
				retVal = "EQUALS";
			}
			if ("<=".equals(retVal)) {
				retVal = "LESSTHAN_OR_EQUALS";
			}
			if ("<".equals(retVal)) {
				retVal = "LESSTHAN";
			}
			if (">=".equals(retVal)) {
				retVal = "GREATERTHAN_OR_EQUALS";
			}
			if (">".equals(retVal)) {
				retVal = "GREATERTHAN";
			}

			StringBuilder b = new StringBuilder();
			for (char next : retVal.toUpperCase().replace("'", "").replace("(", "").replace(")", "").toCharArray()) {
				if (Character.isJavaIdentifierPart(next)) {
					b.append(next);
				} else {
					b.append("_");
				}
			}
			retVal = b.toString();

			if (!Character.isJavaIdentifierStart(retVal.charAt(0))) {
				retVal = '_' + retVal;
			}

			return retVal;
		}

		public String getDefinition() {
			return myDefinition;
		}

		public String getDisplay() {
			return myDisplay;
		}

		public boolean isHasDefinition() {
			return StringUtils.isNotBlank(myDefinition);
		}

		public boolean isHasDisplay() {
			return StringUtils.isNotBlank(myDisplay);
		}

	}

}
