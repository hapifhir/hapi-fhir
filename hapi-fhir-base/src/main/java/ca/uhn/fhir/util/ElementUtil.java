package ca.uhn.fhir.util;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.ICompositeElement;
import ca.uhn.fhir.model.api.IElement;
import org.hl7.fhir.instance.model.api.IBase;

import java.util.ArrayList;
import java.util.List;

public class ElementUtil {

	@SuppressWarnings("unchecked")
	public static boolean isEmpty(Object... theElements) {
		if (theElements == null) {
			return true;
		}
		for (int i = 0; i < theElements.length; i++) {
			Object next = theElements[i];
			if (next instanceof List) {
				if (!isEmpty((List<? extends IBase>) next)) {
					return false;
				}
			} else if (next instanceof String && (!((String)next).isEmpty())) {
				return false;
			} else if (next != null && !((IBase) next).isEmpty()) {
				return false;
			}
		}
		return true;
	}

	public static boolean isEmpty(IBase... theElements) {
		if (theElements == null) {
			return true;
		}
		for (int i = 0; i < theElements.length; i++) {
			IBase next = theElements[i];
			if (next != null && !next.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	public static boolean isEmpty(IElement... theElements) {
		if (theElements == null) {
			return true;
		}
		for (int i = 0; i < theElements.length; i++) {
			IBase next = theElements[i];
			if (next != null && !next.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	public static boolean isEmpty(List<? extends IBase> theElements) {
		if (theElements == null) {
			return true;
		}
		for (int i = 0; i < theElements.size(); i++) {
			IBase next;
			try {
				next = theElements.get(i);
			} catch (ClassCastException e) {
				List<?> elements = theElements;
				String s = "Found instance of " + elements.get(i).getClass() + " - Did you set a field value to the incorrect type? Expected " + IBase.class.getName();
				throw new ClassCastException(Msg.code(1748) + s);
			}
			if (next != null && !next.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Note that this method does not work on HL7.org structures
	 */
	public static <T extends IElement> List<T> allPopulatedChildElements(Class<T> theType, Object... theElements) {
		ArrayList<T> retVal = new ArrayList<T>();
		for (Object next : theElements) {
			if (next == null) {
				continue;
			}else if (next instanceof IElement) {
				addElement(retVal, (IElement) next, theType);
			} else if (next instanceof List) {
				for (Object nextElement : ((List<?>)next)) {
					if (!(nextElement instanceof IBase)) {
						throw new IllegalArgumentException(Msg.code(1749) + "Found element of "+nextElement.getClass());
					}
					addElement(retVal, (IElement) nextElement, theType);
				}
			} else {
				throw new IllegalArgumentException(Msg.code(1750) + "Found element of "+next.getClass());
			}
			
		}
		return retVal;
	}

	//@SuppressWarnings("unchecked")
	private static <T extends IElement> void addElement(ArrayList<T> retVal, IElement next, Class<T> theType) {
		if (theType != null && theType.isAssignableFrom(next.getClass())) {
			retVal.add(theType.cast(next));
		}
		if (next instanceof ICompositeElement) {
			ICompositeElement iCompositeElement = (ICompositeElement) next;
			//TODO: Use of a deprecated method should be resolved.
			retVal.addAll(iCompositeElement.getAllPopulatedChildElementsOfType(theType));
		}
	}
	
}
