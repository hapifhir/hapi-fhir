package ca.uhn.fhir.model.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Extension {

	/**
	 * This parameter affects how the extension is treated when the element
	 * definition containing this resource is exported to a profile.
	 * 
	 * <p>
	 * If set to <b><code>true</code></b>, the resource is taken to be a local
	 * resource and its definition is exported along with the reference. Use
	 * this option for extension defintions that you have added locally (i.e.
	 * within your own organization)
	 * </p>
	 * 
	 * <p>
	 * If set to <b><code>false</code></b>, the resource is taken to be a remote
	 * resource and its definition is <b>not</b> exported to the profile. Use
	 * this option for extensions that are defined by other organizations (i.e.
	 * by regional authorities or jurisdictional governments)
	 * </p>
	 */
	boolean definedLocally();
	
	/**
	 * Returns <code>true</code> if this extension is a
	 * <a href="http://www.hl7.org/implement/standards/fhir/extensibility.html#modifierExtension">modifier extension</a>
	 */
	boolean isModifier();
	
	String url();

}
