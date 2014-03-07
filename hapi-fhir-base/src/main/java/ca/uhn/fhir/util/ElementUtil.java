package ca.uhn.fhir.util;

import java.util.List;

import ca.uhn.fhir.model.api.IElement;

public class ElementUtil {

	@SuppressWarnings("unchecked")
	public static boolean isEmpty(Object... theElements) {
		if (theElements ==null) {
			return true;
		}
		for (int i = 0; i < theElements.length; i++) {
			Object next = theElements[i];
			if (next instanceof List) {
				if (!isEmpty((List<? extends IElement>)next)) {
					return false;
				}
			}
			if (next != null && !((IElement)next).isEmpty()) {
				return false;
			}
		}
		return true;
	}

	public static boolean isEmpty(IElement... theElements) {
		if (theElements ==null) {
			return true;
		}
		for (int i = 0; i < theElements.length; i++) {
			IElement next = theElements[i];
			if (next != null && !next.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	public static boolean isEmpty(List<? extends IElement> theElements) {
		if (theElements ==null) {
			return true;
		}
		for (int i = 0; i < theElements.size(); i++) {
			IElement next = theElements.get(i);
			if (next != null && !next.isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
}
