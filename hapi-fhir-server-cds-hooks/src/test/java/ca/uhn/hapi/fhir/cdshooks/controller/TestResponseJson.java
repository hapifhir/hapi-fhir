package ca.uhn.hapi.fhir.cdshooks.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class TestResponseJson {
	@JsonProperty("cards")
	List<TestCardJson> myCards;

	public void addCard(TestCardJson theCdsServiceResponseCardJson) {
		if (myCards == null) {
			myCards = new ArrayList<>();
		}
		myCards.add(theCdsServiceResponseCardJson);
	}
}
