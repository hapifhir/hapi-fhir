package org.hl7.fhir.instance.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.utilities.xhtml.XhtmlNode;

public abstract class Base implements Serializable, IBase {

  /**
   * User appended data items - allow users to add extra information to the class
   */
private Map<String, Object> userData; 

  /**
   * Round tracking xml comments for testing convenience
   */
  private List<String> formatCommentsPre; 
   
  /**
   * Round tracking xml comments for testing convenience
   */
  private List<String> formatCommentsPost; 
   
  
  public Object getUserData(String name) {
    if (userData == null)
      return null;
    return userData.get(name);
  }
  
  public void setUserData(String name, Object value) {
    if (userData == null)
      userData = new HashMap<String, Object>();
    userData.put(name, value);
  }

  public void setUserDataINN(String name, Object value) {
    if (value == null)
      return;
    
    if (userData == null)
      userData = new HashMap<String, Object>();
    userData.put(name, value);
  }

  public boolean hasUserData(String name) {
    if (userData == null)
      return false;
    else
      return userData.containsKey(name);
  }

	public String getUserString(String name) {
    return (String) getUserData(name);
  }

  public int getUserInt(String name) {
    if (!hasUserData(name))
      return 0;
    return (Integer) getUserData(name);
  }

  public boolean hasFormatComment() {
  	return (formatCommentsPre != null && !formatCommentsPre.isEmpty()) || (formatCommentsPost != null && !formatCommentsPost.isEmpty());
  }
  
  public List<String> getFormatCommentsPre() {
    if (formatCommentsPre == null)
      formatCommentsPre = new ArrayList<String>();
    return formatCommentsPre;
  }
  
  public List<String> getFormatCommentsPost() {
    if (formatCommentsPost == null)
      formatCommentsPost = new ArrayList<String>();
    return formatCommentsPost;
  }  
  
	protected abstract void listChildren(List<Property> result) ;

  /**
   * Supports iterating the children elements in some generic processor or browser
   * All defined children will be listed, even if they have no value on this instance
   * 
   * Note that the actual content of primitive or xhtml elements is not iterated explicitly.
   * To find these, the processing code must recognise the element as a primitive, typecast
   * the value to a {@link Type}, and examine the value
   *  
   * @return a list of all the children defined for this element
   */
  public List<Property> children() {
  	List<Property> result = new ArrayList<Property>();
  	listChildren(result);
  	return result;
  }

  public Property getChildByName(String name) {
    List<Property> children = new ArrayList<Property>();
    listChildren(children);
    for (Property c : children)
      if (c.getName().equals(name))
        return c;
    return null;
  }  
  
  public List<Base> listChildrenByName(String name) {
    List<Property> children = new ArrayList<Property>();
    listChildren(children);
    for (Property c : children)
      if (c.getName().equals(name) || (c.getName().endsWith("[x]") && name.startsWith(c.getName())))
        return c.getValues();
    return new ArrayList<Base>();
  }

	public boolean isEmpty() {
	  return true; // userData does not count
  }

	public boolean equalsDeep(Base other) {
	  return other != null;
  }  
  
	public boolean equalsShallow(Base other) {
	  return other != null;
  }  
  
	public static boolean compareDeep(List<? extends Base> e1, List<? extends Base> e2, boolean allowNull) {
		if (noList(e1) && noList(e2) && allowNull)
			return true;
		if (noList(e1) || noList(e2))
			return false;
		if (e1.size() != e2.size())
			return false;
		for (int i = 0; i < e1.size(); i++) {
			if (!compareDeep(e1.get(i), e2.get(i), allowNull))
				return false;
		}
		return true;
	}
	
	private static boolean noList(List<? extends Base> list) {
    return list == null || list.isEmpty();
  }

	public static boolean compareDeep(Base e1, Base e2, boolean allowNull) {
		if (e1 == null && e2 == null && allowNull)
			return true;
		if (e1 == null || e2 == null)
			return false;
		return e1.equalsDeep(e2);
	}
	
	public static boolean compareDeep(XhtmlNode div1, XhtmlNode div2, boolean allowNull) {
		if (div1 == null && div2 == null && allowNull)
			return true;
		if (div1 == null || div2 == null)
			return false;
		return div1.equalsDeep(div2);
  }


	public static boolean compareValues(List<? extends PrimitiveType> e1, List<? extends PrimitiveType> e2, boolean allowNull) {
		if (e1 == null && e2 == null && allowNull)
			return true;
		if (e1 == null || e2 == null)
			return false;
		if (e1.size() != e2.size())
			return false;
		for (int i = 0; i < e1.size(); i++) {
			if (!compareValues(e1.get(i), e2.get(i), allowNull))
				return false;
		}
		return true;
	}

	public static boolean compareValues(PrimitiveType e1, PrimitiveType e2, boolean allowNull) {
		if (e1 == null && e2 == null && allowNull)
			return true;
		if (e1 == null || e2 == null)
			return false;
		return e1.equalsShallow(e2);
  }
}
