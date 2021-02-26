#!/bin/bash

totalAgents=$SYSTEM_TOTALJOBSINPHASE
agentNumber=$SYSTEM_JOBPOSITIONINPHASE

if [[ $totalAgents -eq 0 ]]; then totalAgents=1; fi
if [ -z "$agentNumber" ]; then agentNumber=1; fi

echo Total agents: $totalAgents
echo Agent Number: $agentNumber
echo workspace2 is $AGENT_BUILDDIRECTORY

tests_to_skip=$(find . -name "*Test.java" | sed -e 's#^.*src/test/java/\(.*\)\.java#\1#' | tr "/" ".")
its_to_skip=$(find . -name "*IT.java" | sed -e 's#^.*src/test/java/\(.*\)\.java#\1#' | tr "/" "." )

echo "Tests to skip $tests_to_skip"
echo "ITs to skip $its_to_skip"

tests_to_skip_filename="tests_to_skip.txt"
ITs_to_skip_filename="ITs_to_skip.txt"

tests_to_skip_file=$AGENT_BUILDDIRECTORY/$tests_to_skip_filename
ITs_to_skip_file=$AGENT_BUILDDIRECTORY/$ITs_to_skip_filename

echo Absolute path of test exclusion is $tests_to_skip_file
echo Absolute path of IT exclusion is $ITs_to_skip_file

counter=0;
for i in $tests_to_skip; do
   if [[ $counter -ne $agentNumber ]]; then
      echo "$i" >> $tests_to_skip_file
   fi
   counter=$((counter+1))
   if [[ $counter -gt $totalAgents ]]; then counter=1; fi
done

counter=0;
for i in $its_to_skip; do
   if [[ $counter -ne $agentNumber ]]; then
      echo "$i" >> $ITs_to_skip_file
   fi
   counter=$((counter+1))
   if [[ $counter -gt $totalAgents ]]; then counter=1; fi
done
testCount=$(cat $tests_to_skip_file | wc -l)
itCount=$(cat $ITs_to_skip_file | wc -l)

echo "TESTS"
cat $tests_to_skip_file
echo "ITS"
cat $tests_to_skip_file
echo "Agent [$agentNumber] is skipping [$testCount] tests and [$itCount] ITs"
echo "##vso[task.setvariable variable=testExclusionFile]$tests_to_skip_file"
echo "##vso[task.setvariable variable=itExclusionFile]$ITs_to_skip_file"
