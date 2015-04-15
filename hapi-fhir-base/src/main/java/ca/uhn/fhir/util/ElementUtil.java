package ca.uhn.fhir.util;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.instance.model.IBase;

import ca.uhn.fhir.model.api.ICompositeElement;

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

	public static boolean isEmpty(List<? extends IBase> theElements) {
		if (theElements == null) {
			return true;
		}
		for (int i = 0; i < theElements.size(); i++) {
			IBase next = theElements.get(i);
			if (next != null && !next.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @param theType Can be null
	 */
	@SuppressWarnings("unused")
	public static <T extends IBase> List<T> allPopulatedChildElements(Class<T> theType, Object... theElements) {
		throw new UnsupportedOperationException();
//		ArrayList<T> retVal = new ArrayList<T>();
//		for (Object next : theElements) {
//			if (next == null) {
//				continue;
//			}else if (next instanceof IBase) {
//				addElement(retVal, (IBase) next, theType);
//			} else if (next instanceof List) {
//				for (Object nextElement : ((List<?>)next)) {
//					if (!(nextElement instanceof IBase)) {
//						throw new IllegalArgumentException("Found element of "+nextElement.getClass());
//					}
//					addElement(retVal, (IBase) nextElement, theType);
//				}
//			} else {
//				throw new IllegalArgumentException("Found element of "+next.getClass());
//			}
//			
//		}
//		return retVal;
	}

//	@SuppressWarnings("unchecked")
//	private static <T extends IBase> void addElement(ArrayList<T> retVal, IBase next, Class<T> theType) {
//		if (theType == null|| theType.isAssignableFrom(next.getClass())) {
//			retVal.add((T) next);
//		}
//		if (next instanceof ICompositeElement) {
//			retVal.addAll(((ICompositeElement) next).getAllPopulatedChildElementsOfType(theType));
//		}
//	}
	
}
