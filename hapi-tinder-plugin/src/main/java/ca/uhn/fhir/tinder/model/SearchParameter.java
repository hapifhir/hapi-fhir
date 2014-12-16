package ca.uhn.fhir.tinder.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringUtils;

public class SearchParameter {

	private String myDescription;
	private String myName;
	private String myPath;
	private List<String> myTargetTypes;
	private String myType;
	private List<String> myCompositeOf;
	private List<String> myCompositeTypes;

	public SearchParameter() {

	}

	public List<String> getCompositeOf() {
		if (myCompositeOf == null) {
			myCompositeOf = new ArrayList<String>();
		}
		return myCompositeOf;
	}

	public List<String> getCompositeTypes() {
		if (myCompositeTypes == null) {
			myCompositeTypes = new ArrayList<String>();
		}
		return myCompositeTypes;
	}

	public String getConstantName() {
		return "SP_" + myName.toUpperCase().replace("_[X]", "_X").replace("-[X]", "_X").replace('-', '_').replace("!", "");
	}

	public String getDescription() {
		return StringUtils.defaultString(myDescription);
	}

	public String getFluentConstantName() {
		// if (myPath==null) {
		// return myName.toUpperCase();
		// }
		// return myPath.toUpperCase().replace("_[X]", "_X").replace("-[X]", "_X").replace('-', '_').replace("!", "");
		return myName.toUpperCase().replace("_[X]", "_X").replace("-[X]", "_X").replace('-', '_').replace("!", "");
	}

	public String getName() {
		return myName;
	}

	public String getNameCapitalized() {
		return WordUtils.capitalize(myName).replace("_[x]", "").replace("-[x]", "").replace("_[X]", "").replace("-[X]", "").replace('-', '_').replace("!", "");
	}

	public String getPath() {
		return StringUtils.defaultString(myPath);
	}

	public List<Include> getPaths() {
		ArrayList<Include> retVal = new ArrayList<Include>();
		for (String next : getPath().split("\\s*\\|\\s*")) {
			retVal.add(new Include(next));
		}
		return retVal;
	}

	public List<String> getTargetTypes() {
		if (myTargetTypes == null) {
			return Collections.emptyList();
		}
		return myTargetTypes;
	}

	public String getType() {
		return StringUtils.defaultString(myType);
	}

	public String getTypeCapitalized() {
		return WordUtils.capitalize(myType);
	}

	public void setCompositeOf(List<String> theCompositeOf) {
		myCompositeOf = theCompositeOf;
	}

	public void setCompositeTypes(List<String> theCompositeTypes) {
		myCompositeTypes = theCompositeTypes;
	}

	public void setDescription(String theDescription) {
		myDescription = theDescription;
	}

	public void setName(String theName) {
		if (theName != null && Character.isUpperCase(theName.charAt(0))) {
			myName = theName.substring(theName.indexOf('.')+1);
		}else {
			myName = theName;
		}
	}

	public void setPath(String thePath) {
		myPath = thePath;
	}

	public void setTargetTypes(List<String> theTargetTypes) {
		myTargetTypes = theTargetTypes;
	}

	public void setType(String theType) {
		myType = theType;
	}

	public static class Include implements Comparable<Include>{
		private String myPath;

		public Include(String thePath) {
			myPath = thePath;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((myPath == null) ? 0 : myPath.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Include other = (Include) obj;
			if (myPath == null) {
				if (other.myPath != null)
					return false;
			} else if (!myPath.equals(other.myPath))
				return false;
			return true;
		}

		public String getIncludeName() {
			String retVal = myPath;
			retVal = retVal.substring(retVal.indexOf('.') + 1);
			retVal = retVal.toUpperCase().replace('.', '_').replace("[X]", "");
			return retVal;
		}

		public String getPath() {
			// String retVal = StringUtils.defaultString(myPath);
			// retVal = retVal.substring(retVal.indexOf('.')+1);
			return myPath;
		}

		@Override
		public int compareTo(Include theO) {
			return myPath.compareTo(theO.myPath);
		}

	}

}
