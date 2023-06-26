package ca.uhn.fhir.narrative;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.util.ElementUtil;
import org.hl7.fhir.r4.model.BackboneElement;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.StringType;

@Block
public class FavouritePizzaExtension extends BackboneElement {

	@Child(name = "childBazExtension")
	@ca.uhn.fhir.model.api.annotation.Extension(url = "toppings")
	private StringType myToppings;
	@Child(name = "childBarExtension")
	@ca.uhn.fhir.model.api.annotation.Extension(url = "size")
	private Quantity mySize;

	@Override
	public BackboneElement copy() {
		return null;
	}

	@Override
	public boolean isEmpty() {
		return super.isEmpty() && ElementUtil.isEmpty(myToppings, mySize);
	}

	public StringType getToppings() {
		return myToppings;
	}

	public void setToppings(StringType theToppings) {
		myToppings = theToppings;
	}

	public Quantity getSize() {
		return mySize;
	}

	public void setSize(Quantity theSize) {
		mySize = theSize;
	}
}
