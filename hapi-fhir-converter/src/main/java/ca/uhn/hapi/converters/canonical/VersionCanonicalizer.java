package ca.uhn.hapi.converters.canonical;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.i18n.Msg;
import org.hl7.fhir.convertors.advisors.impl.BaseAdvisor_10_50;
import org.hl7.fhir.convertors.advisors.impl.BaseAdvisor_30_50;
import org.hl7.fhir.convertors.advisors.impl.BaseAdvisor_40_50;
import org.hl7.fhir.convertors.advisors.impl.BaseAdvisor_43_50;
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_10_50;
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_30_50;
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_40_50;
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_43_50;
import org.hl7.fhir.dstu2.model.Resource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.CapabilityStatement;

/**
 * This class converts versions of various resources to/from a canonical version
 * of the resource. The specific version that is considered canonical is arbitrary
 * for historical reasons, generally it will be R4 or R5 but this varies by resource
 * type.
 * <p>
 * This class is an internal HAPI FHIR API and can change without notice at any time.
 * Use with caution!
 * </p>
 */
public class VersionCanonicalizer {

	private static final BaseAdvisor_30_50 ADVISOR_30_50 = new BaseAdvisor_30_50(false);
	private static final BaseAdvisor_10_50 ADVISOR_10_50 = new BaseAdvisor_10_50(false);
	private static final BaseAdvisor_40_50 ADVISOR_40_50 = new BaseAdvisor_40_50(false);
	private static final BaseAdvisor_43_50 ADVISOR_43_50 = new BaseAdvisor_43_50(false);
	@SuppressWarnings("rawtypes")
	private final IStrategy myStrategy;

	public VersionCanonicalizer(FhirContext theTargetContext) {
		this(theTargetContext.getVersion().getVersion());
	}

	public VersionCanonicalizer(FhirVersionEnum theTargetVersion) {
		switch (theTargetVersion) {
			case DSTU2:
				myStrategy = new Dstu2Strategy();
				break;
			case DSTU3:
				myStrategy = new Dstu3Strategy();
				break;
			case R4:
				myStrategy = new R4Strategy();
				break;
			case R4B:
				myStrategy = new R4BStrategy();
				break;
			case R5:
				myStrategy = new R5Strategy();
				break;
			default:
				throw new IllegalStateException(Msg.code(193) + "Can't handle version: " + theTargetVersion);
		}
	}


	/**
	 * Canonical version: R5
	 */
	public CapabilityStatement capabilityStatementToCanonical(IBaseResource theCapabilityStatement) {
		return myStrategy.capabilityStatementToCanonical(theCapabilityStatement);
	}

	private interface IStrategy<T extends IBaseResource> {

		CapabilityStatement capabilityStatementToCanonical(T theCapabilityStatement);

	}

	private class Dstu2Strategy implements IStrategy<ca.uhn.fhir.model.dstu2.resource.BaseResource> {

		private final FhirContext myDstu2Hl7OrgContext = FhirContext.forDstu2Hl7OrgCached();

		private final FhirContext myDstu2Context = FhirContext.forDstu2Cached();

		@Override
		public CapabilityStatement capabilityStatementToCanonical(ca.uhn.fhir.model.dstu2.resource.BaseResource theCapabilityStatement) {
			org.hl7.fhir.dstu2.model.Resource reencoded = reencode(theCapabilityStatement);
			return (CapabilityStatement) VersionConvertorFactory_10_50.convertResource(reencoded, ADVISOR_10_50);
		}

		private Resource reencode(IBaseResource theInput) {
			return (Resource) myDstu2Hl7OrgContext.newJsonParser().parseResource(myDstu2Context.newJsonParser().encodeResourceToString(theInput));
		}

	}

	private class Dstu3Strategy implements IStrategy<org.hl7.fhir.dstu3.model.Resource> {

		@Override
		public CapabilityStatement capabilityStatementToCanonical(org.hl7.fhir.dstu3.model.Resource theCapabilityStatement) {
			return (CapabilityStatement) VersionConvertorFactory_30_50.convertResource(theCapabilityStatement, ADVISOR_30_50);
		}
	}

	private class R4Strategy implements IStrategy<org.hl7.fhir.r4.model.Resource> {
		@Override
		public CapabilityStatement capabilityStatementToCanonical(org.hl7.fhir.r4.model.Resource theCapabilityStatement) {
			return (CapabilityStatement) VersionConvertorFactory_40_50.convertResource(theCapabilityStatement, ADVISOR_40_50);
		}

	}

	private class R4BStrategy implements IStrategy<org.hl7.fhir.r4b.model.Resource> {

		@Override
		public CapabilityStatement capabilityStatementToCanonical(org.hl7.fhir.r4b.model.Resource theCapabilityStatement) {
			return (CapabilityStatement) VersionConvertorFactory_43_50.convertResource(theCapabilityStatement, ADVISOR_43_50);
		}

	}


	private class R5Strategy implements IStrategy<org.hl7.fhir.r5.model.Resource> {

		@Override
		public CapabilityStatement capabilityStatementToCanonical(org.hl7.fhir.r5.model.Resource theCapabilityStatement) {
			return (CapabilityStatement) theCapabilityStatement;
		}

	}


}






