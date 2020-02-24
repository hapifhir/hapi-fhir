package ca.uhn.fhir.to.util;

/*
 * #%L
 * Smile CDR - CDR
 * %%
 * Copyright (C) 2016 - 2018 Simpatico Intelligent Systems Inc
 * %%
 * All rights reserved.
 * #L%
 */

import ca.uhn.fhir.context.ConfigurationException;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class WebUtil {
	public static final String BOOTSTRAP_ID = "bootstrap";
	public static final String BOOTSTRAP3_PKG = "org.webjars";
	public static final String JQUERY_ID = "jquery";
	public static final String JQUERY_PKG = "org.webjars.bower";

	public static void addStaticResourceWebJar(ResourceHandlerRegistry theRegistry, String pkg, String name) {
		Properties props = new Properties();
		String resourceName = "/META-INF/maven/" + pkg + "/" + name + "/pom.properties";
		try {
			InputStream resourceAsStream = WebUtil.class.getResourceAsStream(resourceName);
			if (resourceAsStream == null) {
				throw new ConfigurationException("Failed to load resource: " + resourceName);
			}
			props.load(resourceAsStream);
		} catch (IOException e) {
			throw new ConfigurationException("Failed to load resource: " + resourceName);
		}
		String version = props.getProperty("version");
		addWebjarWithVersion(theRegistry, name, version);
	}

	public static ResourceHandlerRegistration addWebjarWithVersion(ResourceHandlerRegistry theRegistry, String name, String version) {
		return theRegistry.addResourceHandler("/resources/" + name + "/**").addResourceLocations("classpath:/META-INF/resources/webjars/" + name + "/" + version + "/");
	}

	public static void webJarAddAwesomeCheckbox(ResourceHandlerRegistry theRegistry) {
		WebUtil.addStaticResourceWebJar(theRegistry, "org.webjars.bower", "awesome-bootstrap-checkbox");
	}

	public static void webJarAddBoostrap3(ResourceHandlerRegistry theRegistry) {
		WebUtil.addStaticResourceWebJar(theRegistry, BOOTSTRAP3_PKG, BOOTSTRAP_ID);
	}

	public static void webJarAddEonasdanBootstrapDatetimepicker(ResourceHandlerRegistry theRegistry) {
		WebUtil.addStaticResourceWebJar(theRegistry, "org.webjars", "Eonasdan-bootstrap-datetimepicker");
	}

	public static void webJarAddFontAwesome(ResourceHandlerRegistry theRegistry) {
		WebUtil.addStaticResourceWebJar(theRegistry, "org.webjars", "font-awesome");
	}

	public static void webJarAddJQuery(ResourceHandlerRegistry theRegistry) {
		WebUtil.addStaticResourceWebJar(theRegistry, JQUERY_PKG, JQUERY_ID);
	}

	public static void webJarAddJSTZ(ResourceHandlerRegistry theRegistry) {
		WebUtil.addStaticResourceWebJar(theRegistry, "org.webjars", "jstimezonedetect");
	}

	public static void webJarAddMomentJS(ResourceHandlerRegistry theRegistry) {
		WebUtil.addStaticResourceWebJar(theRegistry, "org.webjars.bower", "moment");
	}

	public static void webJarAddSelect2(ResourceHandlerRegistry theRegistry) {
		WebUtil.addStaticResourceWebJar(theRegistry, "org.webjars", "select2");
	}

}
