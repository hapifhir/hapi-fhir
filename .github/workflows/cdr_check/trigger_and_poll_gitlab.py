import datetime
import os
import sys
import json
from time import sleep
import requests

ROBOGARY_URL = "https://slack-bots.azure.smilecdr.com/"

complete_statuses = ["failed", "success", "canceled"]
current_hapi_branch = os.getenv("HAPI_BRANCH")
target_cdr_branch = os.getenv("CDR_BRANCH")
github_pr = os.getenv("PR_NUMBER", "manual-trigger")
github_requester = os.getenv("REQUESTING_USER", "manual-user")
github_token = os.getenv("GITHUB_TOKEN", "")


def poll_for_pipeline_status(pipe_id):
    """
    Poll Robogary for pipeline status
    """
    resp = requests.get(
        f"{ROBOGARY_URL}/github/pipeline-status/{pipe_id}",
        headers={"temporary-github-token": github_token}
    )
    if resp.status_code > 399:
        print(f"Error polling for pipeline status: {resp.status_code}")
        print(resp.json())
        sys.exit(1)
    pipeline_status_json = resp.json()
    return pipeline_status_json

if not target_cdr_branch:
    print("Defaulting CDR branch to master as this is an automatic build.")
    target_cdr_branch = "master"

# Prepare data for Robogary request
request_data = {
    "github_pr": github_pr,
    "github_repo": "HAPI-FHIR",
    "github_requester": github_requester,
    "hapi_branch": current_hapi_branch,
    "cdr_branch": target_cdr_branch
}

print(f"About to start job. [target_cdr_branch={target_cdr_branch}, current_hapi_branch={current_hapi_branch}]")
print(f"Triggering Remote CI process via Robogary at {ROBOGARY_URL}.")
result = requests.post(
    f"{ROBOGARY_URL}/github/compile-against-cdr",
    json=request_data,
    headers={
        "Content-Type": "application/json",
        "temporary-github-token": github_token
    }
)
if result.status_code > 399:
    print(f"Error: {result.status_code}")
    print(result.json())
    sys.exit(1)

trigger_json = result.json()
pipeline_id = trigger_json["pipeline_id"]




print(f"Generated pipeline. [pipeline_id={pipeline_id}]")
if "web_url" in trigger_json:
    print(f"Pipeline URL: {trigger_json['web_url']}")

status = None
status_json = poll_for_pipeline_status(pipeline_id)
start_time = datetime.datetime.now()

while True:
    status = status_json["status"]
    complete = status_json["complete"]
    now = datetime.datetime.now()

    if complete:
        print(f"Job complete. [status={status}, duration={(now - start_time).total_seconds()}s]")
        break
    else:
        print(f"Job not yet complete. [status={status}, duration={(now - start_time).total_seconds()}s]")

    sleep(60)
    status_json = poll_for_pipeline_status(pipeline_id)

web_url = status_json["web_url"]

if status == "success":
    print(f"CDR compiled successfully! Please visit: {web_url}")
    sys.exit(0)
else:
    print(f"CDR compilation failed with status '{status}'! Please visit: {web_url}")
    sys.exit(1)
