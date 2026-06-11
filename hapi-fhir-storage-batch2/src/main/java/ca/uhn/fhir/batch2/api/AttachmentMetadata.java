package ca.uhn.fhir.batch2.api;

import org.springframework.data.domain.Pageable;

/**
 * Return type for {@link IJobPersistence#listAttachmentsForJobInstance(Pageable, String)}
 */
public record AttachmentMetadata(String attachmentId, String filename) {}
