### Major Database Change

This release fixes a migration from 6.10.1 that was ineffective for SQL Server (MSSQL) instances. 
This may take several minutes on a larger system (e.g. 10 minutes for 100 million resources).
For zero-downtime, or for larger systems, we recommend you upgrade the schema using the CLI tools.
