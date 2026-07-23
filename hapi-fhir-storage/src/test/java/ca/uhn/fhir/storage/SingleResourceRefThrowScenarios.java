package ca.uhn.fhir.storage;

import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

// Created by claude-opus-4-7
class SingleResourceRefThrowScenarios implements ArgumentsProvider {

	@Override
	public Stream<? extends Arguments> provideArguments(ExtensionContext theContext) {
		return Stream.of(
			Arguments.of(
				"identifier without system | throws PreconditionFailed",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"subject" : { "reference": "Patient?identifier=value1" }
								},
								"request" : { "method" : "POST", "url" : "Observation" }
							}
						]
					}
					""",
				PreconditionFailedException.class,
				"HAPI-2995: Inline match URL identifier must have both a system and a value: Patient?identifier=value1"
			),
			Arguments.of(
				"identifier with blank value | throws PreconditionFailed",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"subject" : { "reference": "Patient?identifier=http://system|" }
								},
								"request" : { "method" : "POST", "url" : "Observation" }
							}
						]
					}
					""",
				PreconditionFailedException.class,
				"HAPI-2995: Inline match URL identifier must have both a system and a value: Patient?identifier=http://system|"
			),
			Arguments.of(
				"multiple search parameters | throws PreconditionFailed",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"subject" : { "reference": "Patient?identifier=http://system|value1&active=true" }
								},
								"request" : { "method" : "POST", "url" : "Observation" }
							}
						]
					}
					""",
				PreconditionFailedException.class,
				"HAPI-2996: Inline match URL matching only supports identifier search parameters: Patient?identifier=http://system|value1&active=true"
			),
			Arguments.of(
				"identifier without system, matching no-system identifier in bundle | still throws PreconditionFailed",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [{ "value" : "value1" }]
								},
								"request" : { "method" : "POST", "url" : "Patient" }
							},
							{
								"resource" : {
									"resourceType" : "Observation",
									"subject" : { "reference": "Patient?identifier=value1" }
								},
								"request" : { "method" : "POST", "url" : "Observation" }
							}
						]
					}
					""",
				PreconditionFailedException.class,
				"HAPI-2995: Inline match URL identifier must have both a system and a value: Patient?identifier=value1"
			),
			Arguments.of(
				"identifier with :not modifier, matching identifier in bundle | throws PreconditionFailed",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [{ "system" : "sys", "value" : "val" }]
								},
								"request" : { "method" : "POST", "url" : "Patient" }
							},
							{
								"resource" : {
									"resourceType" : "Observation",
									"subject" : { "reference": "Patient?identifier:not=sys|val" }
								},
								"request" : { "method" : "POST", "url" : "Observation" }
							}
						]
					}
					""",
				PreconditionFailedException.class,
				"HAPI-2997: Inline match URL identifier must not use a search modifier: Patient?identifier:not=sys|val"
			),
			Arguments.of(
				"identifier with blank value, matching value-less identifier in bundle | throws PreconditionFailed",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [{ "system" : "http://system" }]
								},
								"request" : { "method" : "POST", "url" : "Patient" }
							},
							{
								"resource" : {
									"resourceType" : "Observation",
									"subject" : { "reference": "Patient?identifier=http://system|" }
								},
								"request" : { "method" : "POST", "url" : "Observation" }
							}
						]
					}
					""",
				PreconditionFailedException.class,
				"HAPI-2995: Inline match URL identifier must have both a system and a value: Patient?identifier=http://system|"
			),
			Arguments.of(
				"multiple search parameters, matching identifier in bundle | throws PreconditionFailed",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Patient",
									"identifier" : [{ "system" : "http://system", "value" : "value1" }],
									"active" : true
								},
								"request" : { "method" : "POST", "url" : "Patient" }
							},
							{
								"resource" : {
									"resourceType" : "Observation",
									"subject" : { "reference": "Patient?identifier=http://system|value1&active=true" }
								},
								"request" : { "method" : "POST", "url" : "Observation" }
							}
						]
					}
					""",
				PreconditionFailedException.class,
				"HAPI-2996: Inline match URL matching only supports identifier search parameters: Patient?identifier=http://system|value1&active=true"
			),
			Arguments.of(
				"identifier with explicitly empty system | throws PreconditionFailed",
				"""
					{ "resourceType" : "Bundle", "type" : "transaction",
						"entry" : [
							{
								"resource" : {
									"resourceType" : "Observation",
									"subject" : { "reference": "Patient?identifier=|value1" }
								},
								"request" : { "method" : "POST", "url" : "Observation" }
							}
						]
					}
					""",
				PreconditionFailedException.class,
				"HAPI-2995: Inline match URL identifier must have both a system and a value: Patient?identifier=|value1"
			)
		);
	}
}
