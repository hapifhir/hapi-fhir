#!/bin/sh
echo '[Git Hook] Executing Spotless check before commit'
# Only compare files that have changed compared to master
mvn spotless:check -DratchetFrom=origin/master
exit $?
