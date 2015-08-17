package org.hl7.fhir.instance.conf;

/*
 * #%L
 * HAPI FHIR Structures - DSTU2 (FHIR v0.5.0)
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.Conformance;
import org.hl7.fhir.instance.model.Conformance.ConditionalDeleteStatus;
import org.hl7.fhir.instance.model.Conformance.ConformanceRestComponent;
import org.hl7.fhir.instance.model.Conformance.ConformanceRestResourceComponent;
import org.hl7.fhir.instance.model.Conformance.ConformanceRestResourceSearchParamComponent;
import org.hl7.fhir.instance.model.Conformance.ConformanceStatementKind;
import org.hl7.fhir.instance.model.Conformance.ResourceInteractionComponent;
import org.hl7.fhir.instance.model.Conformance.RestfulConformanceMode;
import org.hl7.fhir.instance.model.Conformance.SystemRestfulInteraction;
import org.hl7.fhir.instance.model.Conformance.TypeRestfulInteraction;
import org.hl7.fhir.instance.model.Conformance.UnknownContentCode;
import org.hl7.fhir.instance.model.Enumerations.ConformanceResourceStatus;
import org.hl7.fhir.instance.model.Enumerations.ResourceType;
import org.hl7.fhir.instance.model.OperationDefinition;
import org.hl7.fhir.instance.model.OperationDefinition.OperationDefinitionParameterComponent;
import org.hl7.fhir.instance.model.OperationDefinition.OperationParameterUse;

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Initialize;
import ca.uhn.fhir.rest.annotation.Metadata;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.method.BaseMethodBinding;
import ca.uhn.fhir.rest.method.DynamicSearchMethodBinding;
import ca.uhn.fhir.rest.method.IParameter;
import ca.uhn.fhir.rest.method.OperationMethodBinding;
import ca.uhn.fhir.rest.method.OperationMethodBinding.ReturnType;
import ca.uhn.fhir.rest.method.OperationParameter;
import ca.uhn.fhir.rest.method.SearchMethodBinding;
import ca.uhn.fhir.rest.method.SearchParameter;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.IServerConformanceProvider;
import ca.uhn.fhir.rest.server.ResourceBinding;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

/**
 * Server FHIR Provider which serves the conformance statement for a RESTful server implementation
 * 
 * <p>
 * Note: This class is safe to extend, but it is important to note that the same instance of {@link Conformance} is always returned unless
 * {@link #setCache(boolean)} is called with a value of <code>false</code>. This means that if you are adding anything to the returned conformance instance on
 * each call you should call <code>setCache(false)</code> in your provider constructor.
 * </p>
 */
public class ServerConformanceProvider implements IServerConformanceProvider<Conformance> {

  private boolean myCache = true;
  private volatile Conformance myConformance;
  private IdentityHashMap<OperationMethodBinding, String> myOperationBindingToName;
  private HashMap<String, List<OperationMethodBinding>> myOperationNameToBindings;
  private String myPublisher = "Not provided";
  private final RestfulServer myRestfulServer;

  public ServerConformanceProvider(RestfulServer theRestfulServer) {
    myRestfulServer = theRestfulServer;
  }

  private void checkBindingForSystemOps(ConformanceRestComponent rest, Set<SystemRestfulInteraction> systemOps,
      BaseMethodBinding<?> nextMethodBinding) {
    if (nextMethodBinding.getRestOperationType() != null) {
      String sysOpCode = nextMethodBinding.getRestOperationType().getCode();
      if (sysOpCode != null) {
        SystemRestfulInteraction sysOp;
        try {
          sysOp = SystemRestfulInteraction.fromCode(sysOpCode);
        } catch (Exception e) {
          sysOp = null;
        }
        if (sysOp == null) {
          return;
        }
        if (systemOps.contains(sysOp) == false) {
          systemOps.add(sysOp);
          rest.addInteraction().setCode(sysOp);
        }
      }
    }
  }

  private Map<String, List<BaseMethodBinding<?>>> collectMethodBindings() {
    Map<String, List<BaseMethodBinding<?>>> resourceToMethods = new TreeMap<String, List<BaseMethodBinding<?>>>();
    for (ResourceBinding next : myRestfulServer.getResourceBindings()) {
      String resourceName = next.getResourceName();
      for (BaseMethodBinding<?> nextMethodBinding : next.getMethodBindings()) {
        if (resourceToMethods.containsKey(resourceName) == false) {
          resourceToMethods.put(resourceName, new ArrayList<BaseMethodBinding<?>>());
        }
        resourceToMethods.get(resourceName).add(nextMethodBinding);
      }
    }
    for (BaseMethodBinding<?> nextMethodBinding : myRestfulServer.getServerBindings()) {
      String resourceName = "";
      if (resourceToMethods.containsKey(resourceName) == false) {
        resourceToMethods.put(resourceName, new ArrayList<BaseMethodBinding<?>>());
      }
      resourceToMethods.get(resourceName).add(nextMethodBinding);
    }
    return resourceToMethods;
  }

  private String createOperationName(OperationMethodBinding theMethodBinding) {
    return theMethodBinding.getName().substring(1);
  }

  /**
   * Gets the value of the "publisher" that will be placed in the generated conformance statement. As this is a mandatory element, the value should not be null
   * (although this is not enforced). The value defaults to "Not provided" but may be set to null, which will cause this element to be omitted.
   */
  public String getPublisher() {
    return myPublisher;
  }

  @Override
  @Metadata
  public Conformance getServerConformance(HttpServletRequest theRequest) {
    if (myConformance != null && myCache) {
      return myConformance;
    }

    Conformance retVal = new Conformance();

    retVal.setPublisher(myPublisher);
    retVal.setDate(new Date());
    retVal.setFhirVersion("0.5.0"); // TODO: pull from model
    retVal.setAcceptUnknown(UnknownContentCode.EXTENSIONS); // TODO: make this configurable - this is a fairly big effort since the parser
    // needs to be modified to actually allow it

    retVal.getImplementation().setDescription(myRestfulServer.getImplementationDescription());
    retVal.setKind(ConformanceStatementKind.INSTANCE);
    retVal.getSoftware().setName(myRestfulServer.getServerName());
    retVal.getSoftware().setVersion(myRestfulServer.getServerVersion());
    retVal.addFormat(Constants.CT_FHIR_XML);
    retVal.addFormat(Constants.CT_FHIR_JSON);

    ConformanceRestComponent rest = retVal.addRest();
    rest.setMode(RestfulConformanceMode.SERVER);

    Set<SystemRestfulInteraction> systemOps = new HashSet<SystemRestfulInteraction>();
    Set<String> operationNames = new HashSet<String>();

    Map<String, List<BaseMethodBinding<?>>> resourceToMethods = collectMethodBindings();
    for (Entry<String, List<BaseMethodBinding<?>>> nextEntry : resourceToMethods.entrySet()) {

      if (nextEntry.getKey().isEmpty() == false) {
        Set<TypeRestfulInteraction> resourceOps = new HashSet<TypeRestfulInteraction>();
        ConformanceRestResourceComponent resource = rest.addResource();
        String resourceName = nextEntry.getKey();
        RuntimeResourceDefinition def = myRestfulServer.getFhirContext().getResourceDefinition(resourceName);
        resource.getTypeElement().setValue(def.getName());
        resource.getProfile()
            .setReference((def.getResourceProfile(myRestfulServer.getServerBaseForRequest(theRequest))));

        TreeSet<String> includes = new TreeSet<String>();

        // Map<String, Conformance.RestResourceSearchParam> nameToSearchParam = new HashMap<String,
        // Conformance.RestResourceSearchParam>();
        for (BaseMethodBinding<?> nextMethodBinding : nextEntry.getValue()) {
          if (nextMethodBinding.getRestOperationType() != null) {
            String resOpCode = nextMethodBinding.getRestOperationType().getCode();
            if (resOpCode != null) {
              TypeRestfulInteraction resOp;
              try {
                resOp = TypeRestfulInteraction.fromCode(resOpCode);
              } catch (Exception e) {
                resOp = null;
              }
              if (resOp != null) {
                if (resourceOps.contains(resOp) == false) {
                  resourceOps.add(resOp);
                  resource.addInteraction().setCode(resOp);
                }
                if ("vread".equals(resOpCode)) {
                  // vread implies read
                  resOp = TypeRestfulInteraction.READ;
                  if (resourceOps.contains(resOp) == false) {
                    resourceOps.add(resOp);
                    resource.addInteraction().setCode(resOp);
                  }
                }

                if (nextMethodBinding.isSupportsConditional()) {
                  switch (resOp) {
                  case CREATE:
                    resource.setConditionalCreate(true);
                    break;
                  case DELETE:
                    resource.setConditionalDelete(ConditionalDeleteStatus.SINGLE);
                    break;
                  case UPDATE:
                    resource.setConditionalUpdate(true);
                    break;
                  default:
                    break;
                  }
                }
              }
            }
          }

          checkBindingForSystemOps(rest, systemOps, nextMethodBinding);

          if (nextMethodBinding instanceof SearchMethodBinding) {
            handleSearchMethodBinding(rest, resource, resourceName, def, includes,
                (SearchMethodBinding) nextMethodBinding);
          } else if (nextMethodBinding instanceof DynamicSearchMethodBinding) {
            handleDynamicSearchMethodBinding(resource, def, includes, (DynamicSearchMethodBinding) nextMethodBinding);
          } else if (nextMethodBinding instanceof OperationMethodBinding) {
            OperationMethodBinding methodBinding = (OperationMethodBinding) nextMethodBinding;
            String opName = myOperationBindingToName.get(methodBinding);
            if (operationNames.add(opName)) {
              // Only add each operation (by name) once
              rest.addOperation().setName(methodBinding.getName()).getDefinition()
                  .setReference("OperationDefinition/" + opName);
            }
          }

          Collections.sort(resource.getInteraction(), new Comparator<ResourceInteractionComponent>() {
            @Override
            public int compare(ResourceInteractionComponent theO1, ResourceInteractionComponent theO2) {
              TypeRestfulInteraction o1 = theO1.getCode();
              TypeRestfulInteraction o2 = theO2.getCode();
              if (o1 == null && o2 == null) {
                return 0;
              }
              if (o1 == null) {
                return 1;
              }
              if (o2 == null) {
                return -1;
              }
              return o1.ordinal() - o2.ordinal();
            }
          });

        }

        for (String nextInclude : includes) {
          resource.addSearchInclude(nextInclude);
        }
      } else {
        for (BaseMethodBinding<?> nextMethodBinding : nextEntry.getValue()) {
          checkBindingForSystemOps(rest, systemOps, nextMethodBinding);
          if (nextMethodBinding instanceof OperationMethodBinding) {
            OperationMethodBinding methodBinding = (OperationMethodBinding) nextMethodBinding;
            String opName = myOperationBindingToName.get(methodBinding);
            if (operationNames.add(opName)) {
              rest.addOperation().setName(methodBinding.getName()).getDefinition()
                  .setReference("OperationDefinition/" + opName);
            }
          }
        }
      }
    }

    myConformance = retVal;
    return retVal;
  }

  private void handleDynamicSearchMethodBinding(ConformanceRestResourceComponent resource,
      RuntimeResourceDefinition def, TreeSet<String> includes, DynamicSearchMethodBinding searchMethodBinding) {
    includes.addAll(searchMethodBinding.getIncludes());

    List<RuntimeSearchParam> searchParameters = new ArrayList<RuntimeSearchParam>();
    searchParameters.addAll(searchMethodBinding.getSearchParams());
    sortRuntimeSearchParameters(searchParameters);

    if (!searchParameters.isEmpty()) {

      for (RuntimeSearchParam nextParameter : searchParameters) {

        String nextParamName = nextParameter.getName();

        // String chain = null;
        String nextParamUnchainedName = nextParamName;
        if (nextParamName.contains(".")) {
          // chain = nextParamName.substring(nextParamName.indexOf('.') + 1);
          nextParamUnchainedName = nextParamName.substring(0, nextParamName.indexOf('.'));
        }

        String nextParamDescription = nextParameter.getDescription();

        /*
         * If the parameter has no description, default to the one from the resource
         */
        if (StringUtils.isBlank(nextParamDescription)) {
          RuntimeSearchParam paramDef = def.getSearchParam(nextParamUnchainedName);
          if (paramDef != null) {
            nextParamDescription = paramDef.getDescription();
          }
        }

        ConformanceRestResourceSearchParamComponent param = resource.addSearchParam();

        param.setName(nextParamName);
        // if (StringUtils.isNotBlank(chain)) {
        // param.addChain(chain);
        // }
        param.setDocumentation(nextParamDescription);
        // param.setType(nextParameter.getParamType());
      }
    }
  }

  private void handleSearchMethodBinding(ConformanceRestComponent rest, ConformanceRestResourceComponent resource,
      String resourceName, RuntimeResourceDefinition def, TreeSet<String> includes,
      SearchMethodBinding searchMethodBinding) {
    includes.addAll(searchMethodBinding.getIncludes());

    List<IParameter> params = searchMethodBinding.getParameters();
    List<SearchParameter> searchParameters = new ArrayList<SearchParameter>();
    for (IParameter nextParameter : params) {
      if ((nextParameter instanceof SearchParameter)) {
        searchParameters.add((SearchParameter) nextParameter);
      }
    }
    sortSearchParameters(searchParameters);
    if (!searchParameters.isEmpty()) {
      // boolean allOptional = searchParameters.get(0).isRequired() == false;
      //
      // OperationDefinition query = null;
      // if (!allOptional) {
      // RestOperation operation = rest.addOperation();
      // query = new OperationDefinition();
      // operation.setDefinition(new ResourceReferenceDt(query));
      // query.getDescriptionElement().setValue(searchMethodBinding.getDescription());
      // query.addUndeclaredExtension(false, ExtensionConstants.QUERY_RETURN_TYPE, new CodeDt(resourceName));
      // for (String nextInclude : searchMethodBinding.getIncludes()) {
      // query.addUndeclaredExtension(false, ExtensionConstants.QUERY_ALLOWED_INCLUDE, new StringDt(nextInclude));
      // }
      // }

      for (SearchParameter nextParameter : searchParameters) {

        String nextParamName = nextParameter.getName();

        String chain = null;
        String nextParamUnchainedName = nextParamName;
        if (nextParamName.contains(".")) {
          chain = nextParamName.substring(nextParamName.indexOf('.') + 1);
          nextParamUnchainedName = nextParamName.substring(0, nextParamName.indexOf('.'));
        }

        String nextParamDescription = nextParameter.getDescription();

        /*
         * If the parameter has no description, default to the one from the resource
         */
        if (StringUtils.isBlank(nextParamDescription)) {
          RuntimeSearchParam paramDef = def.getSearchParam(nextParamUnchainedName);
          if (paramDef != null) {
            nextParamDescription = paramDef.getDescription();
          }
        }

        ConformanceRestResourceSearchParamComponent param = resource.addSearchParam();
        param.setName(nextParamUnchainedName);
        if (StringUtils.isNotBlank(chain)) {
          param.addChain(chain);
        }
        param.setDocumentation(nextParamDescription);
        if (nextParameter.getParamType() != null) {
          param.getTypeElement().setValueAsString(nextParameter.getParamType().getCode());
        }
        for (Class<? extends IResource> nextTarget : nextParameter.getDeclaredTypes()) {
          RuntimeResourceDefinition targetDef = myRestfulServer.getFhirContext().getResourceDefinition(nextTarget);
          if (targetDef != null) {
            ResourceType code;
            try {
              code = ResourceType.fromCode(targetDef.getName());
            } catch (Exception e) {
              code = null;
            }
            if (code != null) {
              param.addTarget(code.toCode());
            }
          }
        }
      }
    }
  }

  @Initialize
  public void initializeOperations() {
    myOperationBindingToName = new IdentityHashMap<OperationMethodBinding, String>();
    myOperationNameToBindings = new HashMap<String, List<OperationMethodBinding>>();

    Map<String, List<BaseMethodBinding<?>>> resourceToMethods = collectMethodBindings();
    for (Entry<String, List<BaseMethodBinding<?>>> nextEntry : resourceToMethods.entrySet()) {
      List<BaseMethodBinding<?>> nextMethodBindings = nextEntry.getValue();
      for (BaseMethodBinding<?> nextMethodBinding : nextMethodBindings) {
        if (nextMethodBinding instanceof OperationMethodBinding) {
          OperationMethodBinding methodBinding = (OperationMethodBinding) nextMethodBinding;
          if (myOperationBindingToName.containsKey(methodBinding)) {
            continue;
          }

          String name = createOperationName(methodBinding);
          myOperationBindingToName.put(methodBinding, name);
          if (myOperationNameToBindings.containsKey(name) == false) {
            myOperationNameToBindings.put(name, new ArrayList<OperationMethodBinding>());
          }
          myOperationNameToBindings.get(name).add(methodBinding);
        }
      }
    }
  }

  @Read(type = OperationDefinition.class)
  public OperationDefinition readOperationDefinition(@IdParam IdDt theId) {
    if (theId == null || theId.hasIdPart() == false) {
      throw new ResourceNotFoundException(theId);
    }
    List<OperationMethodBinding> sharedDescriptions = myOperationNameToBindings.get(theId.getIdPart());
    if (sharedDescriptions == null || sharedDescriptions.isEmpty()) {
      throw new ResourceNotFoundException(theId);
    }

    OperationDefinition op = new OperationDefinition();
    op.setStatus(ConformanceResourceStatus.ACTIVE);
    op.setIdempotent(true);

    Set<String> inParams = new HashSet<String>();
    Set<String> outParams = new HashSet<String>();

    for (OperationMethodBinding sharedDescription : sharedDescriptions) {
      if (isNotBlank(sharedDescription.getDescription())) {
        op.setDescription(sharedDescription.getDescription());
      }
      if (!sharedDescription.isIdempotent()) {
        op.setIdempotent(sharedDescription.isIdempotent());
      }
      op.setCode(sharedDescription.getName());
      if (sharedDescription.isCanOperateAtInstanceLevel()) {
        op.setInstance(sharedDescription.isCanOperateAtInstanceLevel());
      }
      if (sharedDescription.isCanOperateAtServerLevel()) {
        op.setSystem(sharedDescription.isCanOperateAtServerLevel());
      }
      if (isNotBlank(sharedDescription.getResourceName())) {
        op.addTypeElement().setValue(sharedDescription.getResourceName());
      }

      for (IParameter nextParamUntyped : sharedDescription.getParameters()) {
        if (nextParamUntyped instanceof OperationParameter) {
          OperationParameter nextParam = (OperationParameter) nextParamUntyped;
          OperationDefinitionParameterComponent param = op.addParameter();
          if (!inParams.add(nextParam.getName())) {
            continue;
          }
          param.setUse(OperationParameterUse.IN);
          if (nextParam.getParamType() != null) {
            param.setType(nextParam.getParamType());
          }
          param.setMin(nextParam.getMin());
          param.setMax(nextParam.getMax() == -1 ? "*" : Integer.toString(nextParam.getMax()));
          param.setName(nextParam.getName());
        }
      }

      for (ReturnType nextParam : sharedDescription.getReturnParams()) {
        if (!outParams.add(nextParam.getName())) {
          continue;
        }
        OperationDefinitionParameterComponent param = op.addParameter();
        param.setUse(OperationParameterUse.OUT);
        if (nextParam.getType() != null) {
          param.setType(nextParam.getType());
        }
        param.setMin(nextParam.getMin());
        param.setMax(nextParam.getMax() == -1 ? "*" : Integer.toString(nextParam.getMax()));
        param.setName(nextParam.getName());
      }
    }

    return op;
  }

  /**
   * Sets the cache property (default is true). If set to true, the same response will be returned for each invocation.
   * <p>
   * See the class documentation for an important note if you are extending this class
   * </p>
   */
  public void setCache(boolean theCache) {
    myCache = theCache;
  }

  /**
   * Sets the value of the "publisher" that will be placed in the generated conformance statement. As this is a mandatory element, the value should not be null
   * (although this is not enforced). The value defaults to "Not provided" but may be set to null, which will cause this element to be omitted.
   */
  public void setPublisher(String thePublisher) {
    myPublisher = thePublisher;
  }

  private void sortRuntimeSearchParameters(List<RuntimeSearchParam> searchParameters) {
    Collections.sort(searchParameters, new Comparator<RuntimeSearchParam>() {
      @Override
      public int compare(RuntimeSearchParam theO1, RuntimeSearchParam theO2) {
        return theO1.getName().compareTo(theO2.getName());
      }
    });
  }

  private void sortSearchParameters(List<SearchParameter> searchParameters) {
    Collections.sort(searchParameters, new Comparator<SearchParameter>() {
      @Override
      public int compare(SearchParameter theO1, SearchParameter theO2) {
        if (theO1.isRequired() == theO2.isRequired()) {
          return theO1.getName().compareTo(theO2.getName());
        }
        if (theO1.isRequired()) {
          return -1;
        }
        return 1;
      }
    });
  }
}
