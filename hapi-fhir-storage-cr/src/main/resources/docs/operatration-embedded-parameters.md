<!-- TODO: LD: find a home for these docs -->

# Rules
    * Provider method must be annotated with @Operation
    * Provider method may contain 0 to 1 RequestDetails parameters
    * Provider method must contain one and only one embedded parameters class
    * The method parameter for this class must NOT have any annotations, especially NOT @OperationParam
    * The parameters class must not have any top-level annotations
    * The parameters class must be immutable:  setters will not be respected by the reflection code
    * The parameters class must contain one and only one public constructor for all fields
    * The parameters class may contain a Builder class for developer convenience
    * The parameters class must contain at least one field annotated with @OperationEmbeddedParam
    * The parameters class must contain 0 to 1 @IdParam fields, with the same rules as @IdParam variables
    * The parameters class may not contain fields with any annotations other than @IdParam or @OperationEmbeddedParam
    * Parameters fields otherwise follow the same rules for @IdParam and @OperationParam fields, namely the types that are allowed, including Collections and IPrimitiveType
    * An @OperationEmbeddedParam is equivalent to an OperationEmbeddedParameter 
    * REST method parameters will be passed as separate values and converted by reflection code at runtime to a single instance of the embedded parameters class before being passed to the operation provider 

## Example

Here is a simplified example of how this would work in practice for $evaluate-measure:

```java
public class EvaluateMeasureSingleParams {
    @IdParam
    private final IdType myId;

    @OperationEmbeddedParam(name = "periodStart")
    private final String myPeriodStart;

    @OperationEmbeddedParam(name = "periodEnd")
    private final String myPeriodEnd;

    @OperationEmbeddedParam(name = "reportType")
    private final String myReportType;

    @OperationEmbeddedParam(name = "subject")
    private final String mySubject;
    
    public EvaluateMeasureSingleParams(
        IdType myId,
        String myPeriodStart,
        String myPeriodEnd,
        String myReportType,
        String mySubject ) {
        this.myId = myId;
        this.myPeriodStart = myPeriodStart;
        this.myPeriodEnd = myPeriodEnd;
        this.myReportType = myReportType;
        this.mySubject = mySubject;
    }
}
```

```java
	@Operation(name = ProviderConstants.CR_OPERATION_EVALUATE_MEASURE, idempotent = true, type = Measure.class)
	public MeasureReport evaluateMeasure(EvaluateMeasureSingleParams theParams, RequestDetails theRequestDetails)
			throws InternalErrorException, FHIRException {
        return myR4MeasureServiceFactory
            .create(theRequestDetails)
            .evaluate(theParams);
}
```

<!-- TODO: LD: converting to ZonedDateTime -->
<!-- TODO: LD: handling Eithers -->
