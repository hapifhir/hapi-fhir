package ca.uhn.fhir.rest.param.binder;

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

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.i18n.Msg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CollectionBinder
// implements IParamBinder
{

	/**
	 * @param thePositionDescription Just used in exceptions if theCollectionType is invalid
	 */
	@SuppressWarnings({ "rawtypes", "cast" })
	public static Class<? extends Collection> getInstantiableCollectionType(Class<? extends Collection<?>> theCollectionType, String thePositionDescription) {
		if (theCollectionType.equals(List.class) || theCollectionType .equals(ArrayList.class)) {
			return (Class<? extends Collection>) ArrayList.class;
		} else if (theCollectionType .equals( Set.class )|| theCollectionType .equals( HashSet.class)) {
			return (Class<? extends Collection>) HashSet.class;
		} else if (theCollectionType.equals(Collection.class)) {
			return (Class<? extends Collection>) ArrayList.class;
		} else {
			throw new ConfigurationException(Msg.code(1956) + "Unsupported binding collection type '" + theCollectionType.getCanonicalName() + "' for " + thePositionDescription);
		}
	}

	// private Class<?> myCollectionType;
	// private IParamBinder myWrap;
	//
	// public CollectionBinder(IParamBinder theWrap, Class<? extends java.util.Collection<?>> theCollectionType) {
	// myWrap = theWrap;
	// if (theCollectionType == List.class || theCollectionType == ArrayList.class) {
	// myCollectionType = ArrayList.class;
	// } else if (theCollectionType == Set.class || theCollectionType == HashSet.class) {
	// myCollectionType = HashSet.class;
	// } else if (theCollectionType == Collection.class) {
	// myCollectionType = ArrayList.class;
	// } else {
	// throw new ConfigurationException(Msg.code(1957) + "Unsupported binding collection type: " + theCollectionType.getCanonicalName());
	// }
	// }

	// @Override
	// public String encode(Object theString) throws InternalErrorException {
	// Collection<?> obj = (Collection<?>) theString;
	// StringBuilder b = new StringBuilder();
	// for (Object object : obj) {
	// String next = myWrap.encode(object);
	// if (b.length() > 0) {
	// b.append(",");
	// }
	// b.append(next.replace(",", "\\,"));
	// }
	// return b.toString();
	// }
	//
	// @SuppressWarnings("unchecked")
	// @Override
	// public Object parse(String theString) throws InternalErrorException {
	// Collection<Object> retVal;
	// try {
	// retVal = (Collection<Object>) myCollectionType.newInstance();
	// } catch (Exception e) {
	// throw new InternalErrorException(Msg.code(1958) + "Failed to instantiate " + myCollectionType, e);
	// }
	//
	// List<String> params = QueryUtil.splitQueryStringByCommasIgnoreEscape(theString);
	// for (String string : params) {
	// Object nextParsed = myWrap.parse(string);
	// retVal.add(nextParsed);
	// }
	//
	// return retVal;
	// }

}
