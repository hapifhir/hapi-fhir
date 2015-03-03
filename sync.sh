#!/bin/sh

for i in $(find ~/workspace/fhirbuild/trunk/build/source -name *-spreadsheet.xml | egrep "/[a-z0-9]+-spreadsheet"); do cp -v $i hapi-tinder-plugin/src/main/resources/res/dstu2/; done

for i in $(find ~/workspace/fhirbuild/trunk/build/source/datatypes | grep xml | grep -v spreadsheet); do echo $i; done
