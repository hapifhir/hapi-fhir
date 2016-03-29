package ca.uhn.fhir.rest.server.interceptor.auth;

enum RuleOpEnum {
	READ,
	WRITE, 
	ALLOW_ALL,
	DENY_ALL, 
	TRANSACTION
}
