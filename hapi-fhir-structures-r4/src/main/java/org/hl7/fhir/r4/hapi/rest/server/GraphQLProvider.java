package org.hl7.fhir.r4.hapi.rest.server;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.annotation.GraphQL;
import ca.uhn.fhir.rest.annotation.GraphQLQuery;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Initialize;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.context.IWorkerContext;
import org.hl7.fhir.r4.hapi.ctx.DefaultProfileValidationSupport;
import org.hl7.fhir.r4.hapi.ctx.HapiWorkerContext;
import org.hl7.fhir.r4.hapi.ctx.IValidationSupport;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.utils.GraphQLEngine;
import org.hl7.fhir.utilities.graphql.ObjectValue;
import org.hl7.fhir.utilities.graphql.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GraphQLProvider {
  private final IWorkerContext myWorkerContext;
  private Logger ourLog = LoggerFactory.getLogger(GraphQLProvider.class);
  private GraphQLEngine.IGraphQLStorageServices myStorageServices;

  /**
   * Constructor which uses a default context and validation support object
   *
   * @param theStorageServices The storage services (this object will be used to retrieve various resources as required by the GraphQL engine)
   */
  public GraphQLProvider(GraphQLEngine.IGraphQLStorageServices theStorageServices) {
    this(FhirContext.forR4(), new DefaultProfileValidationSupport(), theStorageServices);
  }

  /**
   * Constructor which uses the given worker context
   *
   * @param theFhirContext       The HAPI FHIR Context object
   * @param theValidationSupport The HAPI Validation Support object
   * @param theStorageServices   The storage services (this object will be used to retrieve various resources as required by the GraphQL engine)
   */
  public GraphQLProvider(FhirContext theFhirContext, IValidationSupport theValidationSupport, GraphQLEngine.IGraphQLStorageServices theStorageServices) {
    myWorkerContext = new HapiWorkerContext(theFhirContext, theValidationSupport);
    myStorageServices = theStorageServices;
  }

  @GraphQL
  public String graphql(ServletRequestDetails theRequestDetails, @IdParam IIdType theId, @GraphQLQuery String theQuery) {

    GraphQLEngine engine = new GraphQLEngine(myWorkerContext);
    engine.setServices(myStorageServices);
    try {
      engine.setGraphQL(Parser.parse(theQuery));
    } catch (Exception theE) {
      throw new InvalidRequestException("Unable to parse GraphQL Expression: " + theE.toString());
    }

    try {

      if (theId != null) {
        Resource focus = myStorageServices.lookup(theRequestDetails, theId.getResourceType(), theId.getIdPart());
        engine.setFocus(focus);
      }
      engine.execute();

      StringBuilder outputBuilder = new StringBuilder();
      ObjectValue output = engine.getOutput();
      output.write(outputBuilder, 0, "\n");

      return outputBuilder.toString();

    } catch (Exception theE) {
      throw new InvalidRequestException("Unable to execute GraphQL Expression: " + theE.toString());
    }
  }

  @Initialize
  public void initialize(RestfulServer theServer) {
    ourLog.trace("Initializing GraphQL provider");
    if (theServer.getFhirContext().getVersion().getVersion() != FhirVersionEnum.R4) {
      throw new ConfigurationException("Can not use " + getClass().getName() + " provider on server with FHIR " + theServer.getFhirContext().getVersion().getVersion().name() + " context");
    }
  }


}

