package ca.uhn.fhir.ws;

import ca.uhn.fhir.ws.parameters.Optional;
import ca.uhn.fhir.ws.parameters.Required;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class Util {
    public static Map<String, String> getQueryParams(String query) throws UnsupportedEncodingException {
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


    public static List<Parameter> getResourceParameters(Method method) {
        List<Parameter> parameters = new ArrayList<Parameter>();

        Class<?>[] parameterTypes = method.getParameterTypes();
        for (Annotation[] annotations : method.getParameterAnnotations()) {
            for (int i = 0; i < annotations.length; i++) {
                Annotation a = annotations[i];
                Parameter parameter = new Parameter();
                if (a instanceof Required) {
                    parameter.setName(((Required) a).name());
                    parameter.setRequired(true);
                    parameter.setType(parameterTypes[i]);

                } else if (a instanceof Optional) {
                    parameter.setName(((Optional) a).name());
                    parameter.setRequired(false);
                    parameter.setType(parameterTypes[i]);
                }
                parameters.add(parameter);
            }
        }
        return parameters;
    }
}
