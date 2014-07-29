package ca.uhn.fhir.rest.gclient;

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.MethodOutcome;

public interface IUpdateTyped extends IClientExecutable<IUpdateTyped, MethodOutcome> {

	IUpdateTyped withId(IdDt theId);

	IUpdateTyped withId(String theId);
}
