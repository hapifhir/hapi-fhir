package ca.uhn.fhir.rest.client.method;

class SinceOrAtParameter extends SearchParameter {

//	private Class<?> myType;
//	private String myParamName;
//	private Class<?> myAnnotationType;

	public SinceOrAtParameter(String theParamName, Class<?> theAnnotationType) {
		super(theParamName, false);
//		myParamName = theParamName;
//		myAnnotationType = theAnnotationType;
	}

//	@Override
//	public void translateClientArgumentIntoQueryArgument(FhirContext theContext, Object theSourceClientArgument, Map<String, List<String>> theTargetQueryArguments, IBaseResource theTargetResource) throws InternalErrorException {
//		if (theSourceClientArgument != null) {
//			InstantDt since = ParameterUtil.toInstant(theSourceClientArgument);
//			if (since.isEmpty() == false) {
//				theTargetQueryArguments.put(myParamName, Collections.singletonList(since.getValueAsString()));
//			}
//		}
//	}
//
//	@Override
//	public Object translateQueryParametersIntoServerArgument(RequestDetails theRequest, BaseMethodBinding<?> theMethodBinding) throws InternalErrorException, InvalidRequestException {
//		String[] sinceParams = theRequest.getParameters().remove(myParamName);
//		if (sinceParams != null) {
//			if (sinceParams.length > 0) {
//				if (StringUtils.isNotBlank(sinceParams[0])) {
//					try {
//						return ParameterUtil.fromInstant(myType, sinceParams);
//					} catch (DataFormatException e) {
//						throw new InvalidRequestException("Invalid " + Constants.PARAM_SINCE + " value: " + sinceParams[0]);
//					}
//				}
//			}
//		}
//		return ParameterUtil.fromInstant(myType, null);
//	}
//
//	@Override
//	public void initializeTypes(Method theMethod, Class<? extends Collection<?>> theOuterCollectionType, Class<? extends Collection<?>> theInnerCollectionType, Class<?> theParameterType) {
//		if (theOuterCollectionType != null) {
//			throw new ConfigurationException("Method '" + theMethod.getName() + "' in type '" + "' is annotated with @" + myAnnotationType.getName() + " but can not be of collection type");
//		}
//		if (ParameterUtil.getBindableInstantTypes().contains(theParameterType)) {
//			myType = theParameterType;
//		} else { 
//			throw new ConfigurationException("Method '" + theMethod.getName() + "' in type '" + "' is annotated with @" + myAnnotationType.getName() + " but is an invalid type, must be one of: " + ParameterUtil.getBindableInstantTypes());
//		}
//	}

}
