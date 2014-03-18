package ca.uhn.fhir.rest.server;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.qos.logback.core.joran.action.ParamAction;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.rest.annotation.Include;
import ca.uhn.fhir.rest.annotation.Optional;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Required;
import ca.uhn.fhir.rest.param.IParameter;
import ca.uhn.fhir.rest.param.IncludeParameter;
import ca.uhn.fhir.rest.param.SearchParameter;
import ca.uhn.fhir.util.ReflectionUtil;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class Util {
	public static Map<String, String> getQueryParams(String query) {
		try {

			Map<String, String> params = new HashMap<String, String>();
			for (String param : query.split("&")) {
				String[] pair = param.split("=");
				String key = URLDecoder.decode(pair[0], "UTF-8");
				String value = URLDecoder.decode(pair[1], "UTF-8");

				params.put(key, value);
			}
			return params;
		} catch (UnsupportedEncodingException ex) {
			throw new AssertionError(ex);
		}
	}

	@SuppressWarnings("unchecked")
	public static List<IParameter> getResourceParameters(Method method) {
		List<IParameter> parameters = new ArrayList<IParameter>();

		Class<?>[] parameterTypes = method.getParameterTypes();
		int paramIndex = 0;
		for (Annotation[] annotations : method.getParameterAnnotations()) {
			boolean haveHandledMethod = false;
			for (int i = 0; i < annotations.length; i++) {
				Annotation nextAnnotation = annotations[i];
				Class<?> parameterType = parameterTypes[paramIndex];
				
				Class<? extends java.util.Collection<?>> outerCollectionType = null;
				Class<? extends java.util.Collection<?>> innerCollectionType = null;
				
				if (Collection.class.isAssignableFrom(parameterType)) {
					innerCollectionType = (Class<? extends java.util.Collection<?>>) parameterType;
					parameterType = ReflectionUtil.getGenericCollectionTypeOfMethodParameter(method, paramIndex);
				}

				if (Collection.class.isAssignableFrom(parameterType)) {
					outerCollectionType = innerCollectionType;
					innerCollectionType = (Class<? extends java.util.Collection<?>>) parameterType;
					parameterType = ReflectionUtil.getGenericCollectionTypeOfMethodParameter(method, paramIndex);
				}

				IParameter param;
				if (nextAnnotation instanceof Required) {
					SearchParameter parameter = new SearchParameter();
					parameter.setName(((Required) nextAnnotation).name());
					parameter.setRequired(true);
					parameter.setType(parameterType, innerCollectionType, outerCollectionType);
					param = parameter;
				} else if (nextAnnotation instanceof Optional) {
					SearchParameter parameter = new SearchParameter();
					parameter.setName(((Optional) nextAnnotation).name());
					parameter.setRequired(false);
					parameter.setType(parameterType, innerCollectionType, innerCollectionType);
					param = parameter;
				} else if (nextAnnotation instanceof Include) {
					if (parameterType != String.class) {
						throw new ConfigurationException("Method '" + method.getName() + "' is annotated with @" + Include.class.getSimpleName() + " but has a type other than Collection<String>");
					}
//					if (innerCollectionType)
					
					param = new IncludeParameter();
				} else {
					continue;
				}
				
				haveHandledMethod= true;
				parameters.add(param);
			}
			
			if (!haveHandledMethod) {
				throw new ConfigurationException("Parameter # " + paramIndex + " of method '" + method.getName() + "' has no recognized FHIR interface parameter annotations. Don't know how to handle this parameter!");
			}
			
			paramIndex++;
		}
		return parameters;
	}

	public static Integer findReadIdParameterIndex(Method theMethod) {
		return findParamIndex(theMethod, Read.IdParam.class);
	}

	public static Integer findReadVersionIdParameterIndex(Method theMethod) {
		return findParamIndex(theMethod, Read.VersionIdParam.class);
	}

	private static Integer findParamIndex(Method theMethod, Class<?> toFind) {
		int paramIndex = 0;
		for (Annotation[] annotations : theMethod.getParameterAnnotations()) {
			for (int annotationIndex = 0; annotationIndex < annotations.length; annotationIndex++) {
				Annotation nextAnnotation = annotations[annotationIndex];
				Class<? extends Annotation> class1 = nextAnnotation.getClass();
				if (toFind.isAssignableFrom(class1)) {
					return paramIndex;
				}
			}
			paramIndex++;
		}
		return null;
	}
}
