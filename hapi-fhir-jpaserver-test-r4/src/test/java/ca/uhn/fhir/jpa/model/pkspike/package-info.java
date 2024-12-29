/**
 * Various tests of a parent-child JPA relationship exercising configurable pk definition and joins.
 *<p>
 * We have a test template that does some basic queries ( {@link ca.uhn.fhir.jpa.model.pkspike.BasicEntityTestTemplate}).
 * <ul>
 *   <li>See {@link ca.uhn.fhir.jpa.model.pkspike.primitive.SimplePkJpaBindingTest} for the normal case.  Supports null partition_id.
 *   <li>See {@link ca.uhn.fhir.jpa.model.pkspike.embeddedid.EmbeddedIdPkJpaBindingTest} for an embedded Id class path. Does not support null partition_id.
 *   <li>See {@link ca.uhn.fhir.jpa.model.pkspike.partitionkey.PartitionJpaBindingTest} for the new Hibernate 6  @{@link org.hibernate.annotations.PartitionKey} annotation.  Does support null partition_id.
 *   <li>See {@link ca.uhn.fhir.jpa.model.pkspike.idclass.IdClassPkJpaBindingTest}.  Supports null partition_id.
 *   <li>See {@link ca.uhn.fhir.jpa.model.pkspike.idclass.IdClassPkCustomXmlJpaBindingTest} which adds PARTITION_ID to the pk and join to the mappings defined in IdClassPkJpaBindingTest by adding an orm.xml file.  Does not support null partition_id.
 * </ul>
 * <p>
 *     Things we learned:
 * <ul>
 *   <li>Hibernate can not fetch any entity with a composite key if any component is null.
 *   <li>It's a real pain to merge orm.xml with annotations when we have duplicate columns.  E.g. PARTITION_ID used as a key and join fight over who should be "insert=true" vs false.
 * <p>
 *     Further hacking Ideas:
 * <ul>
 *   <li>Try to wrap the partition_id column with a user type (e.g. Optional?) so we can pretend it isn't null.
 * </p>
 * <p>
 *  Add this to logback to explore the sql.
 *  <pre>
 *   <logger name="org.hibernate.SQL" level="trace"/>
 *  	<logger name="org.hibernate.orm.jdbc.bind" level="trace"/>
 *  </pre>
 */
package ca.uhn.fhir.jpa.model.pkspike;
