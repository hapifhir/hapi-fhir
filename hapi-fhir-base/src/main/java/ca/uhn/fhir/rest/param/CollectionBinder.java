package ca.uhn.fhir.rest.param;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ca.uhn.fhir.context.ConfigurationException;

public class CollectionBinder
// implements IParamBinder
{

	/**
	 * @param thePositionDescription Just used in exceptions if theCollectionType is invalid
	 */
	@SuppressWarnings("unchecked")
	public static Class<? extends Collection> getInstantiableCollectionType(Class<? extends Collection<?>> theCollectionType, String thePositionDescription) {
		if (theCollectionType.equals(List.class) || theCollectionType .equals(ArrayList.class)) {
			return (Class<? extends Collection>) ArrayList.class;
		} else if (theCollectionType .equals( Set.class )|| theCollectionType .equals( HashSet.class)) {
			return (Class<? extends Collection>) HashSet.class;
		} else if (theCollectionType.equals(Collection.class)) {
			return (Class<? extends Collection>) ArrayList.class;
		} else {
			throw new ConfigurationException("Unsupported binding collection type '" + theCollectionType.getCanonicalName() + "' for " + thePositionDescription);
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
	// throw new ConfigurationException("Unsupported binding collection type: " + theCollectionType.getCanonicalName());
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
	// throw new InternalErrorException("Failed to instantiate " + myCollectionType, e);
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