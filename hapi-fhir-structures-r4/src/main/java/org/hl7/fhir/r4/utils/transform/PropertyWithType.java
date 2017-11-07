package org.hl7.fhir.r4.utils.transform;

import org.hl7.fhir.r4.elementmodel.Property;
import org.hl7.fhir.r4.model.TypeDetails;


/**
 *
 */
public class PropertyWithType {
  /**
   * Path of property
   */
  private String path;
  /**
   * reference to the base property
   */
  private Property baseProperty;
  /**
   * Profile form of the property
   */
  private Property profileProperty;
  /**
   * type details of property
   */
  private TypeDetails types;

  /**
   * Constructor
   *
   * @param path            path
   * @param baseProperty    base property
   * @param profileProperty profile property
   * @param types           types
   */
  public PropertyWithType(String path, Property baseProperty, Property profileProperty, TypeDetails types) {
    super();
    this.baseProperty = baseProperty;
    this.profileProperty = profileProperty;
    this.path = path;
    this.types = types;
  }

  /**
   * get accessor for types
   *
   * @return types
   */
  public TypeDetails getTypes() {
    return types;
  }

  /**
   * get accessor for path
   *
   * @return path string
   */
  public String getPath() {
    return path;
  }

  /**
   * get accessor for baseProperty
   *
   * @return Property value of baseProperty
   */
  public Property getBaseProperty() {
    return baseProperty;
  }

  /**
   * set accessor for baseProperty
   *
   * @param baseProperty Property value for baseProperty
   */
  public void setBaseProperty(Property baseProperty) {
    this.baseProperty = baseProperty;
  }

  /**
   * get accessor for profileProperty
   *
   * @return Property value for profileProperty
   */
  public Property getProfileProperty() {
    return profileProperty;
  }

  /**
   * set accessor for pr
   *
   * @param profileProperty Property value for profileProperty
   */
  public void setProfileProperty(Property profileProperty) {
    this.profileProperty = profileProperty;
  }

  /**
   * get summery of path
   *
   * @return path string
   */
  public String summary() {
    return path;
  }

}
