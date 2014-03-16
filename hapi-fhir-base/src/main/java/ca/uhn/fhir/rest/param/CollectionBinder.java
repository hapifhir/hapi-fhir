package ca.uhn.fhir.rest.param;


public class CollectionBinder 
//implements IParamBinder 
{

//	private Class<?> myCollectionType;
//	private IParamBinder myWrap;
//
//	public CollectionBinder(IParamBinder theWrap, Class<? extends java.util.Collection<?>> theCollectionType) {
//		myWrap = theWrap;
//		if (theCollectionType == List.class || theCollectionType == ArrayList.class) {
//			myCollectionType = ArrayList.class;
//		} else if (theCollectionType == Set.class || theCollectionType == HashSet.class) {
//			myCollectionType = HashSet.class;
//		} else if (theCollectionType == Collection.class) {
//			myCollectionType = ArrayList.class;
//		} else {
//			throw new ConfigurationException("Unsupported binding collection type: " + theCollectionType.getCanonicalName());
//		}
//	}

//	@Override
//	public String encode(Object theString) throws InternalErrorException {
//		Collection<?> obj = (Collection<?>) theString;
//		StringBuilder b = new StringBuilder();
//		for (Object object : obj) {
//			String next = myWrap.encode(object);
//			if (b.length() > 0) {
//				b.append(",");
//			}
//			b.append(next.replace(",", "\\,"));
//		}
//		return b.toString();
//	}
//
//	@SuppressWarnings("unchecked")
//	@Override
//	public Object parse(String theString) throws InternalErrorException {
//		Collection<Object> retVal;
//		try {
//			retVal = (Collection<Object>) myCollectionType.newInstance();
//		} catch (Exception e) {
//			throw new InternalErrorException("Failed to instantiate " + myCollectionType, e);
//		}
//
//		List<String> params = QueryUtil.splitQueryStringByCommasIgnoreEscape(theString);
//		for (String string : params) {
//			Object nextParsed = myWrap.parse(string);
//			retVal.add(nextParsed);
//		}
//
//		return retVal;
//	}

}