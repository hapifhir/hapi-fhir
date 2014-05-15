package ca.uhn.fhir.tinder.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringUtils;

public class SearchParameter {

	private String myDescription;
	private String myName;
	private String myPath;
	private String myType;

	public SearchParameter() {
		
	}
	
	public String getDescription() {
		return StringUtils.defaultString(myDescription);
	}

	public String getConstantName() {
		return "SP_" + myName.toUpperCase().replace("_[X]", "_X").replace("-[X]", "_X").replace('-', '_').replace("!", "");
	}
	
	public List<Include> getPaths() {
		ArrayList<Include> retVal = new ArrayList<Include>();
		for (String next : getPath().split("\\s*\\|\\s*")) {
			retVal.add(new Include(next));
		}
		return retVal;
	}
	
	public static class Include
	{
		private String myPath;
		
		public Include(String thePath) {
			myPath=thePath;
		}

		public String getPath() {
//			String retVal = StringUtils.defaultString(myPath);
//			retVal = retVal.substring(retVal.indexOf('.')+1);
			return myPath;
		}

		public String getIncludeName() {
			String retVal = myPath;
			retVal = retVal.substring(retVal.indexOf('.')+1);
			retVal = retVal.toUpperCase().replace('.', '_').replace("[X]", "");
			return retVal;
		}

	}
	
	public String getFluentConstantName() {
//		if (myPath==null) {
//			return myName.toUpperCase();
//		}
//		return myPath.toUpperCase().replace("_[X]", "_X").replace("-[X]", "_X").replace('-', '_').replace("!", "");
		return myName.toUpperCase().replace("_[X]", "_X").replace("-[X]", "_X").replace('-', '_').replace("!", "");
	}
		
	public String getTypeCapitalized() {
		return WordUtils.capitalize(myType);
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

	public String getType() {
		return StringUtils.defaultString(myType);
	}

	public void setDescription(String theDescription) {
		myDescription = theDescription;
	}

	public void setName(String theName) {
		myName = theName;
	}

	public void setPath(String thePath) {
		myPath = thePath;
	}

	public void setType(String theType) {
		myType = theType;
	}

}
