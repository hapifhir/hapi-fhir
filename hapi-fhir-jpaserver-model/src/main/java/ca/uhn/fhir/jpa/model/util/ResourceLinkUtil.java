package ca.uhn.fhir.jpa.model.util;

import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import org.hl7.fhir.instance.model.api.IIdType;

import java.time.LocalDate;
import java.util.Date;

public class ResourceLinkUtil {

	public static ResourceLink forAbsoluteReference(
			String theSourcePath, ResourceTable theSourceResource, IIdType theTargetResourceUrl, Date theUpdated) {
		ResourceLink retVal = new ResourceLink();
		retVal.setSourcePath(theSourcePath);
		retVal.setSourceResource(theSourceResource);
		retVal.setTargetResourceUrl(theTargetResourceUrl);
		retVal.setUpdated(theUpdated);
		return retVal;
	}

	private ResourceLinkUtil() {
	}

	/**
	 * Factory for canonical URL
	 */
	public static ResourceLink forLogicalReference(
			String theSourcePath, ResourceTable theSourceResource, String theTargetResourceUrl, Date theUpdated) {
		ResourceLink retVal = new ResourceLink();
		retVal.setSourcePath(theSourcePath);
		retVal.setSourceResource(theSourceResource);
		retVal.setTargetResourceUrlCanonical(theTargetResourceUrl);
		retVal.setUpdated(theUpdated);
		return retVal;
	}

	public static ResourceLink forLocalReference(
			ResourceLinkForLocalReferenceParams theResourceLinkForLocalReferenceParams) {

		ResourceLink retVal = new ResourceLink();
		retVal.setSourcePath(theResourceLinkForLocalReferenceParams.getSourcePath());
		retVal.setSourceResource(theResourceLinkForLocalReferenceParams.getSourceResource());
		retVal.setTargetResource(
				theResourceLinkForLocalReferenceParams.getTargetResourceType(),
				theResourceLinkForLocalReferenceParams.getTargetResourcePid(),
				theResourceLinkForLocalReferenceParams.getTargetResourceId());

		retVal.setTargetResourcePartitionId(theResourceLinkForLocalReferenceParams.getTargetResourcePartitionId());
		retVal.setTargetResourcePartitionDate(theResourceLinkForLocalReferenceParams.getTargetResourcePartitionDate());
		retVal.setTargetResourceVersion(theResourceLinkForLocalReferenceParams.getTargetResourceVersion());
		retVal.setUpdated(theResourceLinkForLocalReferenceParams.getUpdated());

		return retVal;
	}

	public static class ResourceLinkForLocalReferenceParams {
		private String mySourcePath;
		private ResourceTable mySourceResource;
		private String myTargetResourceType;
		private Long myTargetResourcePid;
		private String myTargetResourceId;
		private Date myUpdated;
		private Long myTargetResourceVersion;
		private Integer myTargetResourcePartitionId;
		private LocalDate myTargetResourcePartitionDate;

		public static ResourceLinkForLocalReferenceParams instance() {
			return new ResourceLinkForLocalReferenceParams();
		}

		public String getSourcePath() {
			return mySourcePath;
		}

		public ResourceLinkForLocalReferenceParams setSourcePath(String theSourcePath) {
			mySourcePath = theSourcePath;
			return this;
		}

		public ResourceTable getSourceResource() {
			return mySourceResource;
		}

		public ResourceLinkForLocalReferenceParams setSourceResource(ResourceTable theSourceResource) {
			mySourceResource = theSourceResource;
			return this;
		}

		public String getTargetResourceType() {
			return myTargetResourceType;
		}

		public ResourceLinkForLocalReferenceParams setTargetResourceType(String theTargetResourceType) {
			myTargetResourceType = theTargetResourceType;
			return this;
		}

		public Long getTargetResourcePid() {
			return myTargetResourcePid;
		}

		public ResourceLinkForLocalReferenceParams setTargetResourcePid(Long theTargetResourcePid) {
			myTargetResourcePid = theTargetResourcePid;
			return this;
		}

		public String getTargetResourceId() {
			return myTargetResourceId;
		}

		public ResourceLinkForLocalReferenceParams setTargetResourceId(String theTargetResourceId) {
			myTargetResourceId = theTargetResourceId;
			return this;
		}

		public Date getUpdated() {
			return myUpdated;
		}

		public ResourceLinkForLocalReferenceParams setUpdated(Date theUpdated) {
			myUpdated = theUpdated;
			return this;
		}

		public Long getTargetResourceVersion() {
			return myTargetResourceVersion;
		}

		/**
		 * @param theTargetResourceVersion This should only be populated if the reference actually had a version
		 */
		public ResourceLinkForLocalReferenceParams setTargetResourceVersion(Long theTargetResourceVersion) {
			myTargetResourceVersion = theTargetResourceVersion;
			return this;
		}

		public Integer getTargetResourcePartitionId() {
			return myTargetResourcePartitionId;
		}

		public LocalDate getTargetResourcePartitionDate() {
			return myTargetResourcePartitionDate;
		}

		public ResourceLinkForLocalReferenceParams setTargetResourcePartitionId(Integer theTargetResourcePartitionId) {
			myTargetResourcePartitionId = theTargetResourcePartitionId;
			return this;
		}

		public ResourceLinkForLocalReferenceParams setTargetResourcePartitionDate(LocalDate theTargetResourcePartitionDate) {
			myTargetResourcePartitionDate = theTargetResourcePartitionDate;
			return this;
		}
	}
}
