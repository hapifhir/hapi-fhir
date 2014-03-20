package ca.uhn.fhir.rest.server.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.UndeclaredExtension;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.dstu.resource.Conformance.Rest;
import ca.uhn.fhir.model.dstu.resource.Conformance.RestResource;
import ca.uhn.fhir.model.dstu.resource.Conformance.RestResourceSearchParam;
import ca.uhn.fhir.model.dstu.valueset.RestfulConformanceModeEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationSystemEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.Metadata;
import ca.uhn.fhir.rest.method.BaseMethodBinding;
import ca.uhn.fhir.rest.method.ReadMethodBinding;
import ca.uhn.fhir.rest.method.SearchMethodBinding;
import ca.uhn.fhir.rest.param.IParameter;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.ResourceBinding;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.util.DatatypeUtil;
import ca.uhn.fhir.util.ExtensionConstants;
import ca.uhn.fhir.util.VersionUtil;

public class ServerConformanceProvider implements IResourceProvider {

	private volatile Conformance myConformance;
	private final RestfulServer myRestfulServer;

	public ServerConformanceProvider(RestfulServer theRestfulServer) {
		myRestfulServer = theRestfulServer;
	}

	@Metadata
	public Conformance getServerConformance() {
		if (myConformance != null) {
			return myConformance;
		}

		Conformance retVal = new Conformance();
		retVal.getSoftware().setName("HAPI FHIR Server");
		retVal.getSoftware().setVersion(VersionUtil.getVersion());

		Rest rest = retVal.addRest();
		rest.setMode(RestfulConformanceModeEnum.SERVER);

		Set<RestfulOperationSystemEnum> systemOps = new HashSet<RestfulOperationSystemEnum>();

		for (ResourceBinding next : myRestfulServer.getResourceBindings()) {

			Set<RestfulOperationTypeEnum> resourceOps = new HashSet<RestfulOperationTypeEnum>();
			RestResource resource = rest.addResource();

			Class<? extends IResource> resourceType = next.getResourceProvider().getResourceType();
			RuntimeResourceDefinition def = myRestfulServer.getFhirContext().getResourceDefinition(resourceType);
			resource.getType().setValue(def.getName());
			resource.getProfile().setId(new IdDt(def.getResourceProfile()));

			Map<String, Conformance.RestResourceSearchParam> nameToSearchParam = new HashMap<String, Conformance.RestResourceSearchParam>();
			for (BaseMethodBinding nextMethodBinding : next.getMethodBindings()) {
				RestfulOperationTypeEnum resOp = nextMethodBinding.getResourceOperationType();
				if (resOp != null) {
					if (resourceOps.contains(resOp) == false) {
						resourceOps.add(resOp);
						resource.addOperation().setCode(resOp);
					}
				}

				RestfulOperationSystemEnum sysOp = nextMethodBinding.getSystemOperationType();
				if (sysOp != null) {
					if (systemOps.contains(sysOp) == false) {
						systemOps.add(sysOp);
						rest.addOperation().setCode(sysOp);
					}
				}

				if (nextMethodBinding instanceof SearchMethodBinding) {
					List<IParameter> params = ((SearchMethodBinding) nextMethodBinding).getParameters();
					// TODO: this would probably work best if we sorted these by required first, then optional
					
					RestResourceSearchParam searchParam = null;
					StringDt searchParamChain = null;
					for (IParameter nextParameter : params) {
						if (nextParameter.getName().startsWith("_")) {
							continue;
						}

						if (searchParam == null) {
							if (!nameToSearchParam.containsKey(nextParameter.getName())) {
								RestResourceSearchParam param = resource.addSearchParam();
								param.setName(nextParameter.getName());
								searchParam = param;
							} else {
								searchParam = nameToSearchParam.get(nextParameter.getName());
							}
							
							if (searchParam !=null) {
								searchParam.setType(nextParameter.getParamType());
							}
							
						} else if (searchParamChain == null) {
							searchParam.addChain(nextParameter.getName());
							searchParamChain = searchParam.getChain().get(searchParam.getChain().size()-1);
							UndeclaredExtension ext = new UndeclaredExtension();
							ext.setUrl(ExtensionConstants.CONF_CHAIN_REQUIRED);
							ext.setValue(new BooleanDt(nextParameter.isRequired()));
							searchParamChain.getUndeclaredExtensions().add(ext);
							
						} else {
							UndeclaredExtension ext = new UndeclaredExtension();
							ext.setUrl(ExtensionConstants.CONF_ALSO_CHAIN);
							searchParamChain.getUndeclaredExtensions().add(ext);
							
							UndeclaredExtension extReq = new UndeclaredExtension();
							extReq.setUrl(ExtensionConstants.CONF_CHAIN_REQUIRED);
							extReq.setValue(new BooleanDt(nextParameter.isRequired()));
							ext.getUndeclaredExtensions().add(extReq);

						}

					}
				}

			}

		}

		myConformance = retVal;
		return retVal;
	}

	@Override
	public Class<? extends IResource> getResourceType() {
		return Conformance.class;
	}

}
