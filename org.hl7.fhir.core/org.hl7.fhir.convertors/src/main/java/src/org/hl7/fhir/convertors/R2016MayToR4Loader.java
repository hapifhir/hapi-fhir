package org.hl7.fhir.convertors;

/*-
 * #%L
 * org.hl7.fhir.convertors
 * %%
 * Copyright (C) 2014 - 2019 Health Level 7
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hl7.fhir.dstu2016may.formats.JsonParser;
import org.hl7.fhir.dstu2016may.formats.XmlParser;
import org.hl7.fhir.dstu2016may.model.Resource;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.conformance.ProfileUtilities;
import org.hl7.fhir.r4.context.SimpleWorkerContext.IContextResourceLoader;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.MetadataResource;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.StructureDefinition.StructureDefinitionKind;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.ValueSet;

public class R2016MayToR4Loader implements IContextResourceLoader, VersionConvertorAdvisor40 {

  private List<CodeSystem> cslist = new ArrayList<>();
  private boolean patchUrls;
  private boolean killPrimitives;;
  
  @Override
  public Bundle loadBundle(InputStream stream, boolean isJson) throws FHIRException, IOException {
    Resource r2016may = null;
    if (isJson)
      r2016may = new JsonParser().parse(stream);
    else
      r2016may = new XmlParser().parse(stream);
    org.hl7.fhir.r4.model.Resource r4 = VersionConvertor_14_40.convertResource(r2016may);
    
    Bundle b;
    if (r4 instanceof Bundle)
      b = (Bundle) r4;
    else {
      b = new Bundle();
      b.setId(UUID.randomUUID().toString().toLowerCase());
      b.setType(BundleType.COLLECTION);
      b.addEntry().setResource(r4).setFullUrl(r4 instanceof MetadataResource ? ((MetadataResource) r4).getUrl() : null);
    }
    
    for (CodeSystem cs : cslist) {
      BundleEntryComponent be = b.addEntry();
      be.setFullUrl(cs.getUrl());
      be.setResource(cs);
    }
    if (killPrimitives) {
      List<BundleEntryComponent> remove = new ArrayList<BundleEntryComponent>();
      for (BundleEntryComponent be : b.getEntry()) {
        if (be.hasResource() && be.getResource() instanceof StructureDefinition) {
          StructureDefinition sd = (StructureDefinition) be.getResource();
          if (sd.getKind() == StructureDefinitionKind.PRIMITIVETYPE)
            remove.add(be);
        }
      }
      b.getEntry().removeAll(remove);
    }
    for (BundleEntryComponent be : b.getEntry()) {
      if (be.hasResource() && be.getResource() instanceof StructureDefinition) {
        StructureDefinition sd = (StructureDefinition) be.getResource();
        new ProfileUtilities(null, null, null).setIds(sd, false);
        if (patchUrls) {
          sd.setUrl(sd.getUrl().replace("http://hl7.org/fhir/", "http://hl7.org/fhir/2016May/"));
          sd.addExtension().setUrl("http://hl7.org/fhir/StructureDefinition/elementdefinition-namespace").setValue(new UriType("http://hl7.org/fhir"));
        }
      }
    }
    return b;
  }

  @Override
  public boolean ignoreEntry(BundleEntryComponent src) {
    return false;
  }

  @Override
  public org.hl7.fhir.dstu2.model.Resource convertR2(org.hl7.fhir.r4.model.Resource resource) throws FHIRException {
    return null;
  }

  @Override
  public Resource convertR2016May(org.hl7.fhir.r4.model.Resource resource) throws FHIRException {
    return null;
  }

  @Override
  public org.hl7.fhir.dstu3.model.Resource convertR3(org.hl7.fhir.r4.model.Resource resource) throws FHIRException {
    return null;
  }

  @Override
  public void handleCodeSystem(CodeSystem cs, ValueSet vs) {
    cs.setId(vs.getId());
    cs.setValueSet(vs.getUrl());
    cslist.add(cs);
    
  }

  @Override
  public CodeSystem getCodeSystem(ValueSet src) {
    return null;
  }

  public boolean isPatchUrls() {
    return patchUrls;
  }

  public R2016MayToR4Loader setPatchUrls(boolean patchUrls) {
    this.patchUrls = patchUrls;
    return this;
  }

  public boolean isKillPrimitives() {
    return killPrimitives;
  }

  public R2016MayToR4Loader setKillPrimitives(boolean killPrimitives) {
    this.killPrimitives = killPrimitives;
    return this;
  }

}
