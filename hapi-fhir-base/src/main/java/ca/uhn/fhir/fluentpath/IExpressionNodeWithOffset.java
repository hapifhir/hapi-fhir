package ca.uhn.fhir.fluentpath;

public interface IExpressionNodeWithOffset {
	int getOffset();
	IExpressionNode getNode();
}
