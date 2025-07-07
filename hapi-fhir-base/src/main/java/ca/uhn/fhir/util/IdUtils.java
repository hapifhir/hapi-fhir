package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Utility methods for working with version-specific IDs in HAPI FHIR.
 * Based on the CQF Ids class.
 */
public class IdUtils {

	private IdUtils() {}

	/**
	 * Ensures the id contains the resource type
	 *
	 * @param id the String representation of the id
	 * @param resourceType the type of the resource
	 * @return the id
	 */
	public static String ensureIdType(String id, String resourceType) {
		checkNotNull(id);
		checkNotNull(resourceType);
		return id.contains("/") ? id : "%s/%s".formatted(resourceType, id);
	}

	/**
	 * Creates the appropriate IIdType for a given ResourceTypeClass
	 *
	 * @param <ResourceType> an IBase type
	 * @param <IdType> an IIdType type
	 * @param resourceTypeClass the type of the Resource to create an Id for
	 * @param id the String representation of the Id to generate
	 * @return the id
	 */
	public static <ResourceType extends IBaseResource, IdType extends IIdType> IdType newId(
		Class<? extends ResourceType> resourceTypeClass, String id) {
		checkNotNull(resourceTypeClass);
		checkNotNull(id);

		FhirVersionEnum versionEnum = FhirVersions.forClass(resourceTypeClass);
		return newId(versionEnum, resourceTypeClass.getSimpleName(), id);
	}

	/**
	 * Creates the appropriate IIdType for a given BaseTypeClass
	 *
	 * @param <BaseType> an IBase type
	 * @param <IdType> an IIdType type
	 * @param baseTypeClass the BaseTypeClass to use for for determining the FHIR Version
	 * @param resourceName the type of the Resource to create an Id for
	 * @param id the String representation of the Id to generate
	 * @return the id
	 */
	public static <BaseType extends IBase, IdType extends IIdType> IdType newId(
		Class<? extends BaseType> baseTypeClass, String resourceName, String id) {
		checkNotNull(baseTypeClass);
		checkNotNull(resourceName);
		checkNotNull(id);

		FhirVersionEnum versionEnum = FhirVersions.forClass(baseTypeClass);
		return newId(versionEnum, resourceName, id);
	}

	/**
	 * Creates a new random Id of the appropriate IIdType for a given FhirContext
	 *
	 * @param <IdType> an IIdType type
	 * @param fhirContext the FhirContext to use for Id generation
	 * @param resourceType the type of the Resource to create an Id for
	 * @return the id
	 */
	public static <IdType extends IIdType> IdType newRandomId(FhirContext fhirContext, String resourceType) {
		checkNotNull(fhirContext);
		checkNotNull(resourceType);

		return newId(
			fhirContext.getVersion().getVersion(),
			resourceType,
			UUID.randomUUID().toString());
	}

	/**
	 * Creates the appropriate IIdType for a given FhirContext
	 *
	 * @param <IdType> an IIdType type
	 * @param fhirContext the FhirContext to use for Id generation
	 * @param resourceType the type of the Resource to create an Id for
	 * @param id the String representation of the Id to generate
	 * @return the id
	 */
	public static <IdType extends IIdType> IdType newId(FhirContext fhirContext, String resourceType, String id) {
		checkNotNull(fhirContext);
		checkNotNull(resourceType);
		checkNotNull(id);

		return newId(fhirContext.getVersion().getVersion(), resourceType, id);
	}

	/**
	 * Creates the appropriate IIdType for a given FhirVersionEnum
	 *
	 * @param <IdType> an IIdType type
	 * @param fhirVersionEnum the FHIR version to generate an Id for
	 * @param resourceType the type of the Resource to create an Id for
	 * @param idPart the String representation of the Id to generate
	 * @return the id
	 */
	public static <IdType extends IIdType> IdType newId(
		FhirVersionEnum fhirVersionEnum, String resourceType, String idPart) {
		checkNotNull(fhirVersionEnum);
		checkNotNull(resourceType);
		checkNotNull(idPart);

		return newId(fhirVersionEnum, resourceType + "/" + idPart);
	}

	/**
	 * Creates the appropriate IIdType for a given FhirContext
	 *
	 * @param <IdType> an IIdType type
	 * @param fhirContext the FhirContext to use for Id generation
	 * @param id the String representation of the Id to generate
	 * @return the id
	 */
	public static <IdType extends IIdType> IdType newId(FhirContext fhirContext, String id) {
		checkNotNull(fhirContext);
		checkNotNull(id);

		return newId(fhirContext.getVersion().getVersion(), id);
	}

	/**
	 * The gets the "simple" Id for the Resource, without qualifiers or versions. For example,
	 * "Patient/123".
	 * <p>
	 * This is shorthand for resource.getIdElement().toUnqualifiedVersionless().getValue()
	 *
	 * @param resource the Resource to get the Id for
	 * @return the simple Id
	 */
	public static String simple(IBaseResource resource) {
		checkNotNull(resource);
		checkArgument(resource.getIdElement() != null);

		return simple(resource.getIdElement());
	}

	/**
	 * The gets the "simple" Id for the Id, without qualifiers or versions. For example,
	 * "Patient/123".
	 * <p>
	 * This is shorthand for id.toUnqualifiedVersionless().getValue()
	 *
	 * @param id the IIdType to get the Id for
	 * @return the simple Id
	 */
	public static String simple(IIdType id) {
		checkNotNull(id);
		checkArgument(id.hasResourceType());
		checkArgument(id.hasIdPart());

		return id.toUnqualifiedVersionless().getValue();
	}

	/**
	 * The gets the "simple" Id part for the Id, without qualifiers or versions or the resource
	 * Prefix. For example, "123".
	 * <p>
	 * This is shorthand for resource.getIdElement().toUnqualifiedVersionless().getIdPart()
	 *
	 * @param resource the Resource to get the Id for
	 * @return the simple Id part
	 */
	public static String simplePart(IBaseResource resource) {
		checkNotNull(resource);
		checkArgument(resource.getIdElement() != null);

		return simplePart(resource.getIdElement());
	}

	/**
	 * The gets the "simple" Id part for the Id, without qualifiers or versions or the resource
	 * Prefix. For example, "123".
	 * <p>
	 * This is shorthand for id.toUnqualifiedVersionless().getIdPart()
	 *
	 * @param id the IIdType to get the Id for
	 * @return the simple Id part
	 */
	public static String simplePart(IIdType id) {
		checkNotNull(id);
		checkArgument(id.hasResourceType());
		checkArgument(id.hasIdPart());

		return id.toUnqualifiedVersionless().getIdPart();
	}

	/**
	 * Creates the appropriate IIdType for a given FhirVersionEnum
	 *
	 * @param <IdType> an IIdType type
	 * @param fhirVersionEnum the FHIR version to generate an Id for
	 * @param id the String representation of the Id to generate
	 * @return the id
	 */
	@SuppressWarnings("unchecked")
	public static <IdType extends IIdType> IdType newId(FhirVersionEnum fhirVersionEnum, String id) {
		checkNotNull(fhirVersionEnum);
		checkNotNull(id);

		return newId(FhirContext.forVersion(fhirVersionEnum),id);
//		switch (fhirVersionEnum) {
//			case DSTU2:
//				return (IdType) new ca.uhn.fhir.model.primitive.IdDt(id);
//			case DSTU2_1:
//				return (IdType) new org.hl7.fhir.dstu2016may.model.IdType(id);
//			case DSTU2_HL7ORG:
//				return (IdType) new org.hl7.fhir.dstu2.model.IdType(id);
//			case DSTU3:
//				return (IdType) new org.hl7.fhir.dstu3.model.IdType(id);
//			case R4:
//				return (IdType) new org.hl7.fhir.r4.model.IdType(id);
//			case R4B:
//				return (IdType) new org.hl7.fhir.r4b.model.IdType(id);
//			case R5:
//				return (IdType) new org.hl7.fhir.r5.model.IdType(id);
//			default:
//				throw new IllegalArgumentException(
//					"newId does not support FHIR version %s".formatted(fhirVersionEnum.getFhirVersionString()));
//		}
	}
}
