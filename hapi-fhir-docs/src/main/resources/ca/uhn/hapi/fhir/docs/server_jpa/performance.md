# Performance

This page contains information for performance optimization.

# Bulk Loading

On servers where a large amount of data will be ingested, the following considerations may be helpful:

* Optimize your database thread pool count and HTTP client thread count: Every environment will have a different optimal setting for the number of concurrent writes that are permitted, and the maximum number of database connections allowed. 

* Disable deletes: If the JPA server is configured to have the FHIR delete operation disabled, it is able to skip some resource reference deletion checks during resource creation, which can have a measurable improvement to performance over large datasets. 
