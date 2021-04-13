package ca.uhn.fhir.narrative;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.util.ElementUtil;
import org.hl7.fhir.r4.model.Patient;

@ResourceDef(profile = "http://custom_patient")
public class CustomPatient extends Patient {

	@Child(name = "favouritePizzaExtension")
	@Extension(url = "http://example.com/favourite_pizza")
	private FavouritePizzaExtension myFavouritePizza;

	public FavouritePizzaExtension getFavouritePizza() {
		return myFavouritePizza;
	}

	public void setFavouritePizza(FavouritePizzaExtension theFavouritePizza) {
		myFavouritePizza = theFavouritePizza;
	}

	@Override
	public boolean isEmpty() {
		return super.isEmpty() && ElementUtil.isEmpty(myFavouritePizza);
	}

}
