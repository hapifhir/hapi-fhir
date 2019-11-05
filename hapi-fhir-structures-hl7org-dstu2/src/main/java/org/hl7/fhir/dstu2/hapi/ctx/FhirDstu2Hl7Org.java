package org.hl7.fhir.dstu2.hapi.ctx;

/*
 * #%L
 * HAPI FHIR Structures - HL7.org DSTU2
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.dstu2.model.*;
import org.hl7.fhir.instance.model.api.*;

import ca.uhn.fhir.context.*;
import ca.uhn.fhir.context.support.IContextValidationSupport;
import ca.uhn.fhir.fluentpath.IFluentPath;
import ca.uhn.fhir.model.api.IFhirVersion;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.rest.api.IVersionSpecificBundleFactory;
import ca.uhn.fhir.rest.server.provider.dstu2hl7org.Dstu2Hl7OrgBundleFactory;
import ca.uhn.fhir.util.ReflectionUtil;

public class FhirDstu2Hl7Org implements IFhirVersion {

  private String myId;

  @Override
  public IFluentPath createFluentPathExecutor(FhirContext theFhirContext) {
    throw new UnsupportedOperationException("FluentPath is not supported in DSTU2 contexts");
  }

  @Override
  public IContextValidationSupport<?, ?, ?, ?, ?, ?> createValidationSupport() {
    throw new UnsupportedOperationException("Validation support is not supported in DSTU2 contexts");
  }

  @Override
  public StructureDefinition generateProfile(RuntimeResourceDefinition theRuntimeResourceDefinition, String theServerBase) {
    StructureDefinition retVal = new StructureDefinition();

    RuntimeResourceDefinition def = theRuntimeResourceDefinition;

    myId = def.getId();
    if (StringUtils.isBlank(myId)) {
      myId = theRuntimeResourceDefinition.getName().toLowerCase();
    }

    retVal.setId(myId);
    return retVal;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public Class<ArrayList> getContainedType() {
    return ArrayList.class;
  }

  @Override
  public InputStream getFhirVersionPropertiesFile() {
    String path = "/org/hl7/fhir/instance/model/fhirversion.properties";
    InputStream str = FhirDstu2Hl7Org.class.getResourceAsStream(path);
    if (str == null) {
      str = FhirDstu2Hl7Org.class.getResourceAsStream(path.substring(1));
    }
    if (str == null) {
      throw new ConfigurationException("Can not find model property file on classpath: " + path);
    }
    return str;
  }

  @Override
  public IPrimitiveType<Date> getLastUpdated(IBaseResource theResource) {
    return ((Resource) theResource).getMeta().getLastUpdatedElement();
  }

  @Override
  public String getPathToSchemaDefinitions() {
    return "/org/hl7/fhir/instance/model/schema";
  }

  @Override
  public Class<Reference> getResourceReferenceType() {
    return Reference.class;
  }

  @Override
  public Object getServerVersion() {
    return ReflectionUtil.newInstanceOfFhirServerType("org.hl7.fhir.dstu2.hapi.ctx.FhirServerDstu2Hl7Org2");
  }

  @Override
  public FhirVersionEnum getVersion() {
    return FhirVersionEnum.DSTU2_HL7ORG;
  }

  @Override
  public IVersionSpecificBundleFactory newBundleFactory(FhirContext theContext) {
    return new Dstu2Hl7OrgBundleFactory(theContext);
  }

  @Override
  public BaseCodingDt newCodingDt() {
    throw new UnsupportedOperationException();
  }

  @Override
  public IIdType newIdType() {
    return new IdType();
  }

}
