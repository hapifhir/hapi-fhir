package ca.uhn.fhir.jpa.search.builder.models;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.model.api.IQueryParameterType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LeafNodeDefinition {
	private final RuntimeSearchParam myParamDefinition;
	private final ArrayList<IQueryParameterType> myOrValues;
	private final String myLeafTarget;
	private final String myLeafParamName;
	private final String myLeafPathPrefix;
	private final List<String> myQualifiers;

	public LeafNodeDefinition(RuntimeSearchParam theParamDefinition, ArrayList<IQueryParameterType> theOrValues, String theLeafTarget, String theLeafParamName, String theLeafPathPrefix, List<String> theQualifiers) {
		myParamDefinition = theParamDefinition;
		myOrValues = theOrValues;
		myLeafTarget = theLeafTarget;
		myLeafParamName = theLeafParamName;
		myLeafPathPrefix = theLeafPathPrefix;
		myQualifiers = theQualifiers;
	}

	public RuntimeSearchParam getParamDefinition() {
		return myParamDefinition;
	}

	public ArrayList<IQueryParameterType> getOrValues() {
		return myOrValues;
	}

	public String getLeafTarget() {
		return myLeafTarget;
	}

	public String getLeafParamName() {
		return myLeafParamName;
	}

	public String getLeafPathPrefix() {
		return myLeafPathPrefix;
	}

	public List<String> getQualifiers() {
		return myQualifiers;
	}

	public LeafNodeDefinition withPathPrefix(String theResourceType, String theName) {
		return new LeafNodeDefinition(myParamDefinition, myOrValues, theResourceType, myLeafParamName, theName, myQualifiers);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		LeafNodeDefinition that = (LeafNodeDefinition) o;
		return Objects.equals(myParamDefinition, that.myParamDefinition) && Objects.equals(myOrValues, that.myOrValues) && Objects.equals(myLeafTarget, that.myLeafTarget) && Objects.equals(myLeafParamName, that.myLeafParamName) && Objects.equals(myLeafPathPrefix, that.myLeafPathPrefix) && Objects.equals(myQualifiers, that.myQualifiers);
	}

	@Override
	public int hashCode() {
		return Objects.hash(myParamDefinition, myOrValues, myLeafTarget, myLeafParamName, myLeafPathPrefix, myQualifiers);
	}
}
