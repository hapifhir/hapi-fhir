#!/bin/sh
echo '[Git Hook] Executing Spotless autoformat before commit'
# Only compare files that have changed compared to master
mvn spotless:apply -DratchetFrom=origin/master
exit $?
