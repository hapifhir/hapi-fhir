package ca.uhn.fhir.jpa.provider.dstu3;

import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.provider.TrimmedBaseJpaResourceProvider;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.convertors.VersionConvertor_30_40;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IIdType;

import static ca.uhn.fhir.jpa.model.util.JpaConstants.*;

public abstract class TrimmedJpaResourceProviderDstu3<T extends IAnyResource> extends TrimmedBaseJpaResourceProvider<T> {


    public TrimmedJpaResourceProviderDstu3(IFhirResourceDao<T> theDao) {
        super(theDao);

    }




    @Operation(name = JpaConstants.OPERATION_EXPUNGE, idempotent = false, returnParameters = {
            @OperationParam(name = JpaConstants.OPERATION_EXPUNGE_OUT_PARAM_EXPUNGE_COUNT, type = IntegerType.class)
    })
    public Parameters expunge(
            @IdParam IIdType theIdParam,
            @OperationParam(name = JpaConstants.OPERATION_EXPUNGE_PARAM_LIMIT) IntegerType theLimit,
            @OperationParam(name = JpaConstants.OPERATION_EXPUNGE_PARAM_EXPUNGE_DELETED_RESOURCES) BooleanType theExpungeDeletedResources,
            @OperationParam(name = JpaConstants.OPERATION_EXPUNGE_PARAM_EXPUNGE_PREVIOUS_VERSIONS) BooleanType theExpungeOldVersions,
            RequestDetails theRequest) {
        org.hl7.fhir.r4.model.Parameters retVal = super.doExpunge(theIdParam, theLimit, theExpungeDeletedResources, theExpungeOldVersions, null, theRequest);
        try {
            return VersionConvertor_30_40.convertParameters(retVal);
        } catch (FHIRException e) {
            throw new InternalErrorException(e);
        }
    }

    @Operation(name = JpaConstants.OPERATION_EXPUNGE, idempotent = false, returnParameters = {
            @OperationParam(name = JpaConstants.OPERATION_EXPUNGE_OUT_PARAM_EXPUNGE_COUNT, type = IntegerType.class)
    })
    public Parameters expunge(
            @OperationParam(name = JpaConstants.OPERATION_EXPUNGE_PARAM_LIMIT) IntegerType theLimit,
            @OperationParam(name = JpaConstants.OPERATION_EXPUNGE_PARAM_EXPUNGE_DELETED_RESOURCES) BooleanType theExpungeDeletedResources,
            @OperationParam(name = JpaConstants.OPERATION_EXPUNGE_PARAM_EXPUNGE_PREVIOUS_VERSIONS) BooleanType theExpungeOldVersions,
            RequestDetails theRequest) {
        org.hl7.fhir.r4.model.Parameters retVal = super.doExpunge(null, theLimit, theExpungeDeletedResources, theExpungeOldVersions, null, theRequest);
        try {
            return VersionConvertor_30_40.convertParameters(retVal);
        } catch (FHIRException e) {
            throw new InternalErrorException(e);
        }
    }

    @Operation(name = OPERATION_META, idempotent = true, returnParameters = {
            @OperationParam(name = "return", type = Meta.class)
    })
    public Parameters meta(RequestDetails theRequestDetails) {
        Parameters parameters = new Parameters();
        Meta metaGetOperation = getDao().metaGetOperation(Meta.class, theRequestDetails);
        parameters.addParameter().setName("return").setValue(metaGetOperation);
        return parameters;
    }

    @Operation(name = OPERATION_META, idempotent = true, returnParameters = {
            @OperationParam(name = "return", type = Meta.class)
    })
    public Parameters meta(@IdParam IdType theId, RequestDetails theRequestDetails) {
        Parameters parameters = new Parameters();
        Meta metaGetOperation = getDao().metaGetOperation(Meta.class, theId, theRequestDetails);
        parameters.addParameter().setName("return").setValue(metaGetOperation);
        return parameters;
    }

    @Operation(name = OPERATION_META_ADD, idempotent = true, returnParameters = {
            @OperationParam(name = "return", type = Meta.class)
    })
    public Parameters metaAdd(@IdParam IdType theId, @OperationParam(name = "meta") Meta theMeta, RequestDetails theRequestDetails) {
        if (theMeta == null) {
            throw new InvalidRequestException("Input contains no parameter with name 'meta'");
        }
        Parameters parameters = new Parameters();
        Meta metaAddOperation = getDao().metaAddOperation(theId, theMeta, theRequestDetails);
        parameters.addParameter().setName("return").setValue(metaAddOperation);
        return parameters;
    }

    @Operation(name = OPERATION_META_DELETE, idempotent = true, returnParameters = {
            @OperationParam(name = "return", type = Meta.class)
    })
    public Parameters metaDelete(@IdParam IdType theId, @OperationParam(name = "meta") Meta theMeta, RequestDetails theRequestDetails) {
        if (theMeta == null) {
            throw new InvalidRequestException("Input contains no parameter with name 'meta'");
        }
        Parameters parameters = new Parameters();
        parameters.addParameter().setName("return").setValue(getDao().metaDeleteOperation(theId, theMeta, theRequestDetails));
        return parameters;
    }



    @Validate
    public MethodOutcome validate(@ResourceParam T theResource, @ResourceParam String theRawResource, @ResourceParam EncodingEnum theEncoding, @Validate.Mode ValidationModeEnum theMode,
                                  @Validate.Profile String theProfile, RequestDetails theRequestDetails) {
        return validate(theResource, null, theRawResource, theEncoding, theMode, theProfile, theRequestDetails);
    }

    @Validate
    public MethodOutcome validate(@ResourceParam T theResource, @IdParam IdType theId, @ResourceParam String theRawResource, @ResourceParam EncodingEnum theEncoding, @Validate.Mode ValidationModeEnum theMode,
                                  @Validate.Profile String theProfile, RequestDetails theRequestDetails) {
        return getDao().validate(theResource, theId, theRawResource, theEncoding, theMode, theProfile, theRequestDetails);
    }
}
