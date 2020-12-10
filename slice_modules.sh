#!/bin/bash

totalAgents=$SYSTEM_TOTALJOBSINPHASE
agentNumber=$SYSTEM_JOBPOSITIONINPHASE

#totalAgents=4
#agentNumber=2

if [[ $totalAgents -eq 0 ]]; then totalAgents=1; fi
if [ -z "$agentNumber" ]; then agentNumber=1; fi

echo $totalAgents
echo $agentNumber

# Find all directories that have hapi- in the name, assuming they are all modules.
modules=$(ls | grep "hapi-" | grep -v "\." | grep -v "hapi-fhir-jpaserver-base" | grep -v "hapi-fhir-android-realm" | grep -v "hapi-fhir-narrativegenerator")

modulesToTest=""

if [ $agentNumber -eq 1 ]; then modulesToTest="hapi-fhir-jpaserver-base"; fi

responsibleAgentCounter=1

for i in $modules; do
   echo Counter is $responsibleAgentCounter
   if [[ $responsibleAgentCounter -eq $agentNumber ]]; then
      echo "adding $i to modules to test!";
      if [[ $modulesToTest == "" ]]; then
         modulesToTest=$i
      else
         modulesToTest=$modulesToTest,$i;
      fi;
   fi

   responsibleAgentCounter=$((responsibleAgentCounter+1))
   if [[ $responsibleAgentCounter -gt $totalAgents ]]; then responsibleAgentCounter=1; fi
done

echo This agent \[\#$agentNumber\] is responsible for these modules: $modulesToTest
echo "##vso[task.setvariable variable=slicedModules]$modulesToTest"
