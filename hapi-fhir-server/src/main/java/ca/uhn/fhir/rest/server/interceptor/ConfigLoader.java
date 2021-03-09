package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.util.ClasspathUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.Properties;

public class ConfigLoader {

	private static final Logger ourLog = LoggerFactory.getLogger(ConfigLoader.class);
	public static final String CLASSPATH_PREFIX = "classpath:";

	public static String loadResourceContent(String theResourcePath) {
		if(theResourcePath.startsWith(CLASSPATH_PREFIX)) {
			theResourcePath = theResourcePath.substring(CLASSPATH_PREFIX.length());
		}
		return ClasspathUtil.loadResource(theResourcePath);
//		FIXME the code below seems to be failing in the build server...
//		try {
//			URL url = ResourceUtils.getURL(theResourcePath);
//			File file = ResourceUtils.getFile(url);
//			return IOUtils.toString(new FileReader(file));
//		} catch (Exception e) {
//			throw new RuntimeException(String.format("Unable to load resource %s", theResourcePath), e);
//		}
	}

	public static Properties loadProperties(String theResourcePath) {
		String propsString = loadResourceContent(theResourcePath);
		Properties props = new Properties();
		try {
			props.load(new StringReader(propsString));
		} catch (IOException e) {
			throw new RuntimeException(String.format("Unable to load properties at %s", theResourcePath), e);
		}
		return props;
	}

	public static <T> T loadJson(String theResourcePath, Class<T> theModelClass) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(loadResourceContent(theResourcePath), theModelClass);
		} catch (Exception e) {
			throw new RuntimeException(String.format("Unable to parse resource at %s", theResourcePath), e);
		}
	}

}
