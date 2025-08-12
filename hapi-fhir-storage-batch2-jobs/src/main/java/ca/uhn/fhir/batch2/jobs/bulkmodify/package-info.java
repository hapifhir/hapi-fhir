/**
 * This package contains the "bulk modification" job framework. Jobs may extend this if
 * they make changes to a collection of resources. There are two kinds of jobs that
 * can be created:
 * <ul>
 *     <li><b>Bulk Modify Jobs</b>: These jobs modify the current version of a resource, creating
 *     a new version if any changes are actually made.</li>
 *     <li><b>Bulk Rewrite Jobs</b>: These jobs modify all versions of a resource, rewriting history
 *     without creating a new version.</li>
 * </ul>
 *
 * TODO:
 * - A dry run option
 * - Ability to save the output as bundles instead of writing directly to the resource tables
 * - a limit option
 * - add the ability for modification to delete resources
 */
package ca.uhn.fhir.batch2.jobs.bulkmodify;

