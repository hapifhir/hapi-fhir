package org.hl7.fhir.r4.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hl7.fhir.r4.context.IWorkerContext;
import org.hl7.fhir.r4.model.ElementDefinition.ElementDefinitionBindingComponent;
import org.hl7.fhir.r4.model.ExpressionNode.CollectionStatus;
import org.hl7.fhir.exceptions.DefinitionException;
import org.hl7.fhir.utilities.Utilities;



public class TypeDetails {
  public static final String FHIR_NS = "http://hl7.org/fhir/StructureDefinition/";
  public static final String FP_NS = "http://hl7.org/fhirpath/";
  public static final String FP_String = "http://hl7.org/fhirpath/String";
  public static final String FP_Boolean = "http://hl7.org/fhirpath/Boolean";
  public static final String FP_Integer = "http://hl7.org/fhirpath/Integer";
  public static final String FP_Decimal = "http://hl7.org/fhirpath/Decimal";
  public static final String FP_Quantity = "http://hl7.org/fhirpath/Quantity";
  public static final String FP_DateTime = "http://hl7.org/fhirpath/DateTime";
  public static final String FP_Time = "http://hl7.org/fhirpath/Time";
  public static final String FP_SimpleTypeInfo = "http://hl7.org/fhirpath/SimpleTypeInfo";
  public static final String FP_ClassInfo = "http://hl7.org/fhirpath/ClassInfo";

  public static class ProfiledType {
    private String uri;
    private List<String> profiles; // or, not and
    private List<ElementDefinitionBindingComponent> bindings;
    
    public ProfiledType(String n) {
      uri = ns(n);    
    }
    
    public String getUri() {
      return uri;
    }

    public boolean hasProfiles() {
      return profiles != null && profiles.size() > 0;
    }
    public List<String> getProfiles() {
      return profiles;
    }

    public boolean hasBindings() {
      return bindings != null && bindings.size() > 0;
    }
    public List<ElementDefinitionBindingComponent> getBindings() {
      return bindings;
    }

    public static String ns(String n) {
      return Utilities.isAbsoluteUrl(n) ? n : FHIR_NS+n;
    }

    public void addProfile(String profile) {
      if (profiles == null)
        profiles = new ArrayList<String>();
      profiles.add(profile);
    }

    public void addBinding(ElementDefinitionBindingComponent binding) {
      bindings = new ArrayList<ElementDefinitionBindingComponent>();
      bindings.add(binding);
    }

    public boolean hasBinding(ElementDefinitionBindingComponent b) {
      return false; // todo: do we need to do this?
    }

    public void addProfiles(List<CanonicalType> list) {
      if (profiles == null)
        profiles = new ArrayList<String>();
      for (UriType u : list)
        profiles.add(u.getValue());
    }
    public boolean isSystemType() {
      return uri.startsWith(FP_NS);
    }
  }
  
  private List<ProfiledType> types = new ArrayList<ProfiledType>();
  private CollectionStatus collectionStatus;
  public TypeDetails(CollectionStatus collectionStatus, String... names) {
    super();
    this.collectionStatus = collectionStatus;
    for (String n : names) {
      this.types.add(new ProfiledType(n));
    }
  }
  public TypeDetails(CollectionStatus collectionStatus, Set<String> names) {
    super();
    this.collectionStatus = collectionStatus;
    for (String n : names) {
      addType(new ProfiledType(n));
    }
  }
  public TypeDetails(CollectionStatus collectionStatus, ProfiledType pt) {
    super();
    this.collectionStatus = collectionStatus;
    this.types.add(pt);
  }
  public String addType(String n) {
    ProfiledType pt = new ProfiledType(n);
    String res = pt.uri;
    addType(pt);
    return res;
  }
  public String addType(String n, String p) {
    ProfiledType pt = new ProfiledType(n);
    pt.addProfile(p);
    String res = pt.uri;
    addType(pt);
    return res;
  }
  public void addType(ProfiledType pt) {
    for (ProfiledType et : types) {
      if (et.uri.equals(pt.uri)) {
        if (pt.profiles != null) {
          for (String p : pt.profiles) {
            if (et.profiles == null)
              et.profiles = new ArrayList<String>();
            if (!et.profiles.contains(p))
              et.profiles.add(p);
          }
        }
        if (pt.bindings != null) {
          for (ElementDefinitionBindingComponent b : pt.bindings) {
            if (et.bindings == null)
              et.bindings = new ArrayList<ElementDefinitionBindingComponent>();
            if (!et.hasBinding(b))
              et.bindings.add(b);
          }
        }
        return;
      }
    }
    types.add(pt); 
  }
  
  public void addTypes(Collection<String> names) {
    for (String n : names) 
      addType(new ProfiledType(n));
  }
  
  public boolean hasType(IWorkerContext context, String... tn) {
    for (String n: tn) {
      String t = ProfiledType.ns(n);
      if (typesContains(t))
        return true;
      if (Utilities.existsInList(n, "boolean", "string", "integer", "decimal", "Quantity", "dateTime", "time", "ClassInfo", "SimpleTypeInfo")) {
        t = FP_NS+Utilities.capitalize(n);
        if (typesContains(t))
          return true;
      }
    }
    for (String n: tn) {
      String id = n.contains("#") ? n.substring(0, n.indexOf("#")) : n;
      String tail = null;
      if (n.contains("#")) {
        tail = n.substring( n.indexOf("#")+1);
        tail = tail.substring(tail.indexOf("."));
      }
      String t = ProfiledType.ns(n);
      StructureDefinition sd = context.fetchResource(StructureDefinition.class, t);
      while (sd != null) {
        if (tail == null && typesContains(sd.getUrl()))
          return true;
        if (tail == null && getSystemType(sd.getUrl()) != null && typesContains(getSystemType(sd.getUrl())))
          return true;
        if (tail != null && typesContains(sd.getUrl()+"#"+sd.getType()+tail))
          return true;
        if (sd.hasBaseDefinition()) {
          if (sd.getBaseDefinition().equals("http://hl7.org/fhir/StructureDefinition/Element") && !sd.getType().equals("string") && sd.getType().equals("uri"))
            sd = context.fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/string");
          else
            sd = context.fetchResource(StructureDefinition.class, sd.getBaseDefinition());
        } else
          sd = null;
      }
    }
    return false;
  }
  
  private String getSystemType(String url) {
    if (url.startsWith("http://hl7.org/fhir/StructureDefinition/")) {
      String code = url.substring(40);
      if (Utilities.existsInList(code, "string",  "boolean", "integer", "decimal", "dateTime", "time", "Quantity"))
        return FP_NS+Utilities.capitalize(code);
    }
    return null;
  }
  
  private boolean typesContains(String t) {
    for (ProfiledType pt : types)
      if (pt.uri.equals(t))
        return true;
    return false;
  }
  
  public void update(TypeDetails source) {
    for (ProfiledType pt : source.types)
      addType(pt);
    if (collectionStatus == null)
      collectionStatus = source.collectionStatus;
    else if (source.collectionStatus == CollectionStatus.UNORDERED)
      collectionStatus = source.collectionStatus;
    else
      collectionStatus = CollectionStatus.ORDERED;
  }
  public TypeDetails union(TypeDetails right) {
    TypeDetails result = new TypeDetails(null);
    if (right.collectionStatus == CollectionStatus.UNORDERED || collectionStatus == CollectionStatus.UNORDERED)
      result.collectionStatus = CollectionStatus.UNORDERED;
    else 
      result.collectionStatus = CollectionStatus.ORDERED;
    for (ProfiledType pt : types)
      result.addType(pt);
    for (ProfiledType pt : right.types)
      result.addType(pt);
    return result;
  }
  
  public TypeDetails intersect(TypeDetails right) {
    TypeDetails result = new TypeDetails(null);
    if (right.collectionStatus == CollectionStatus.UNORDERED || collectionStatus == CollectionStatus.UNORDERED)
      result.collectionStatus = CollectionStatus.UNORDERED;
    else 
      result.collectionStatus = CollectionStatus.ORDERED;
    for (ProfiledType pt : types) {
      boolean found = false;
      for (ProfiledType r : right.types)
        found = found || pt.uri.equals(r.uri);
      if (found)
        result.addType(pt);
    }
    for (ProfiledType pt : right.types)
      result.addType(pt);
    return result;
  }
  
  public boolean hasNoTypes() {
    return types.isEmpty();
  }
  public Set<String> getTypes() {
    Set<String> res = new HashSet<String>();
    for (ProfiledType pt : types)
      res.add(pt.uri);
    return res;
  }
  public TypeDetails toSingleton() {
    TypeDetails result = new TypeDetails(CollectionStatus.SINGLETON);
    result.types.addAll(types);
    return result;
  }
  public CollectionStatus getCollectionStatus() {
    return collectionStatus;
  }
  public boolean hasType(String n) {
    String t = ProfiledType.ns(n);
    if (typesContains(t))
      return true;
    if (Utilities.existsInList(n, "boolean", "string", "integer", "decimal", "Quantity", "dateTime", "time", "ClassInfo", "SimpleTypeInfo")) {
      t = FP_NS+Utilities.capitalize(n);
      if (typesContains(t))
        return true;
    }
    return false;
  }
  
  public boolean hasType(Set<String> tn) {
    for (String n: tn) {
      String t = ProfiledType.ns(n);
      if (typesContains(t))
        return true;
      if (Utilities.existsInList(n, "boolean", "string", "integer", "decimal", "Quantity", "dateTime", "time", "ClassInfo", "SimpleTypeInfo")) {
        t = FP_NS+Utilities.capitalize(n);
        if (typesContains(t))
          return true;
      }
    }
    return false;
  }
  public String describe() {
    return getTypes().toString();
  }
  public String getType() {
    for (ProfiledType pt : types)
      return pt.uri;
    return null;
  }
  @Override
  public String toString() {
    return (collectionStatus == null ? collectionStatus.SINGLETON.toString() : collectionStatus.toString()) + getTypes().toString();
  }
  public String getTypeCode() throws DefinitionException {
    if (types.size() != 1)
      throw new DefinitionException("Multiple types? ("+types.toString()+")");
    for (ProfiledType pt : types)
      if (pt.uri.startsWith("http://hl7.org/fhir/StructureDefinition/"))
        return pt.uri.substring(40);
      else
        return pt.uri;
    return null;
  }
  public List<ProfiledType> getProfiledTypes() {
    return types;
  }
  public boolean hasBinding() {
    for (ProfiledType pt : types) {
      if (pt.hasBindings())
        return true;
    }
    return false;
  }
  public ElementDefinitionBindingComponent getBinding() {
    for (ProfiledType pt : types) {
      for (ElementDefinitionBindingComponent b : pt.getBindings())
        return b;
    }
    return null;
  }
 
  
}