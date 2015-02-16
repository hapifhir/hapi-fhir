package org.hl7.fhir.instance.model;

/**
 * in a language with helper classes, this would be a helper class (at least, the base exgtension helpers would be)
 * @author Grahame
 *
 */
public class ExtensionHelper {

  
  /**
   * @param name the identity of the extension of interest
   * @return true if the named extension is on this element. Will check modifier extensions too if appropriate
   */
  public static boolean hasExtension(Element element, String name) {
  	if (element != null && element instanceof BackboneElement) 
  		return hasExtension((BackboneElement) element, name);
  	
    if (name == null || element == null || !element.hasExtension())
      return false;
    for (Extension e : element.getExtension()) {
      if (name.equals(e.getUrl()))
        return true;
    }
    return false;
  }
  
  /**
   * @param name the identity of the extension of interest
   * @return true if the named extension is on this element. Will check modifier extensions
   */
  public static boolean hasExtension(BackboneElement element, String name) {
    if (name == null || element == null || !(element.hasExtension() || element.hasModifierExtension()))
      return false;
    for (Extension e : element.getModifierExtension()) {
      if (name.equals(e.getUrl()))
        return true;
    }
    for (Extension e : element.getExtension()) {
      if (name.equals(e.getUrl()))
        return true;
    }
    return false;
  }
  
  
  /**
   * @param name the identity of the extension of interest
   * @return The extension, if on this element, else null. will check modifier extensions too, if appropriate
   */
  public static Extension getExtension(Element element, String name) {
  	if (element != null && element instanceof BackboneElement) 
  		return getExtension((BackboneElement) element, name);
  	
    if (name == null || element == null || !element.hasExtension())
      return null;
    for (Extension e : element.getExtension()) {
      if (name.equals(e.getUrl()))
        return e;
    }
    return null;
  }
  
  /**
   * @param name the identity of the extension of interest
   * @return The extension, if on this element, else null. will check modifier extensions too
   */
  public static Extension getExtension(BackboneElement element, String name) {
    if (name == null || element == null || !element.hasExtension())
      return null;
    for (Extension e : element.getModifierExtension()) {
      if (name.equals(e.getUrl()))
        return e;
    }
    for (Extension e : element.getExtension()) {
      if (name.equals(e.getUrl()))
        return e;
    }
    return null;
  }

  /**
   * set the value of an extension on the element. if value == null, make sure it doesn't exist
   * 
   * @param element - the element to act on. Can also be a backbone element 
   * @param modifier - whether this is a modifier. Note that this is a definitional property of the extension; don't alternate
   * @param uri - the identifier for the extension
   * @param value - the value of the extension. Delete if this is null
   * @throws Exception - if the modifier logic is incorrect
   */
  public static void setExtension(Element element, boolean modifier, String uri, Type value) throws Exception {
  	if (value == null) {
    	// deleting the extension
  		if (element instanceof BackboneElement)
  			for (Extension e : ((BackboneElement) element).getModifierExtension()) {
  				if (uri.equals(e.getUrl()))
  					((BackboneElement) element).getModifierExtension().remove(e);
  			}
  		for (Extension e : element.getExtension()) {
  			if (uri.equals(e.getUrl()))
  				element.getExtension().remove(e);
  		}
  	} else {
  		// it would probably be easier to delete and then create, but this would re-order the extensions
  		// not that order matters, but we'll preserve it anyway
  		boolean found = false;
  		if (element instanceof BackboneElement)
  			for (Extension e : ((BackboneElement) element).getModifierExtension()) {
  				if (uri.equals(e.getUrl())) {
  					if (!modifier)
  						throw new Exception("Error adding extension \""+uri+"\": found an existing modifier extension, and the extension is not marked as a modifier");
  					e.setValue(value);
  					found = true;
  				}
  			}
  		for (Extension e : element.getExtension()) {
  			if (uri.equals(e.getUrl())) {
					if (modifier)
						throw new Exception("Error adding extension \""+uri+"\": found an existing extension, and the extension is marked as a modifier");
					e.setValue(value);
					found = true;
  			}
  		}
  		if (!found) {
  			Extension ex = new Extension().setUrl(uri).setValue(value);
  			if (modifier) {
  	  		if (!(element instanceof BackboneElement))
						throw new Exception("Error adding extension \""+uri+"\": extension is marked as a modifier, but element is not a backbone element");
  				((BackboneElement) element).getModifierExtension().add(ex);
  				
  			} else {
  				element.getExtension().add(ex);
  			}
  		}
  	}
  }

  public static boolean hasExtensions(Element element) {
  	if (element instanceof BackboneElement)
  		return element.hasExtension() || ((BackboneElement) element).hasModifierExtension();
  	else
  		return element.hasExtension();
  }


}
