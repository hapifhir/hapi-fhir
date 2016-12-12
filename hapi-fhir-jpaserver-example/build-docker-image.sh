#!/bin/sh

mvn package && \
  docker build -t hapi-fhir/hapi-fhir-jpaserver-example .

