package ca.uhn.fhir.jpa.search;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * The whole purpose of his class is to ease construction of a non-trivial gson.JsonObject,
 * which can't be done the easy way in this case (using a JSON string), because there are
 * valid regex strings which break gson, as this: ".*\\^Donor$"
 */
public class ElasticsearchNestedQueryBuilderUtil {

	private final String myNestedObjName;
	private final String myNestedKeyPropName;
	private final String myNestedKeyPropValue;
	private final String myNestedValuePropName;
	private final String myNestedValuePropValue;

	private final String myNestedPropertyKeyPath;
	private final String myNestedPropertyValuePath;

	private JsonObject builtJsonObj = new JsonObject();

	public ElasticsearchNestedQueryBuilderUtil(String theNestedObjName, String theNestedKeyPropName,
			String theNestedKeyPropValue, String theNestedValuePropName, String theNestedValuePropValue) {

		myNestedObjName = theNestedObjName ;
		myNestedKeyPropName = theNestedKeyPropName;
		myNestedKeyPropValue = theNestedKeyPropValue;
		myNestedValuePropName = theNestedValuePropName;
		myNestedValuePropValue = theNestedValuePropValue;

		myNestedPropertyKeyPath = myNestedObjName + "." + myNestedKeyPropName;
		myNestedPropertyValuePath = myNestedObjName + "." + myNestedValuePropName;

		buildJsonObj();
	}


	private void buildJsonObj() {
		JsonObject matchPropJO = new JsonObject();
		matchPropJO.addProperty(myNestedObjName + "." + myNestedKeyPropName, myNestedKeyPropValue);
		JsonObject matchJO = new JsonObject();
		matchJO.add("match", matchPropJO);

		JsonObject regexpPropJO = new JsonObject();
		regexpPropJO.addProperty(myNestedObjName + "." + myNestedValuePropName, myNestedValuePropValue);
		JsonObject regexpJO = new JsonObject();
		regexpJO.add("regexp", regexpPropJO);

		JsonArray mustPropJA = new JsonArray();
		mustPropJA.add(matchJO);
		mustPropJA.add(regexpJO);

		JsonObject mustPropJO = new JsonObject();
		mustPropJO.add("must", mustPropJA);

		JsonObject boolJO = new JsonObject();
		boolJO.add("bool", mustPropJO);

		JsonObject nestedJO = new JsonObject();
		nestedJO.addProperty("path", myNestedObjName);
		nestedJO.add("query", boolJO);

		builtJsonObj.add("nested", nestedJO);
	}

	public JsonObject toGson() { return builtJsonObj; }

	public String getNestedPropertyKeyPath() { return myNestedPropertyKeyPath; }

	public String getNestedPropertyValuePath() { return myNestedPropertyValuePath; }

}
