import datetime
import os
import sys
import json
import re
from time import sleep
import requests

ROBOGARY_URL = "https://slack-bots.azure.smilecdr.com/robogary/"

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

def check_pom_version_changed():
    """
    Check if pom.xml version has been changed in the PR
    """
    # Skip if not a PR
    if github_pr == "manual-trigger":
        return False

    # Get the list of files changed in the PR
    url = f"https://api.github.com/repos/hapifhir/hapi-fhir/pulls/{github_pr}/files"
    headers = {"Authorization": f"token {github_token}"}

    try:
        response = requests.get(url, headers=headers)
        response.raise_for_status()
        files = response.json()

        # Check if pom.xml is in the list of changed files
        for file in files:
            if file["filename"] == "pom.xml":
                # Get the diff and check if version has changed
                patch = file.get("patch", "")
                if patch:
                    # Look for version changes in the diff
                    version_pattern = r'[-+]\s*<version>([^<]+)</version>'
                    matches = re.findall(version_pattern, patch)
                    if len(matches) >= 2:  # At least one removal and one addition
                        print(f"Version change detected in pom.xml: {matches}")
                        return True

                # If we can't determine from the patch, get the full file content
                contents_url = f"https://api.github.com/repos/hapifhir/hapi-fhir/contents/pom.xml"
                contents_response = requests.get(contents_url, headers=headers)
                if contents_response.status_code == 200:
                    contents = contents_response.json()
                    content = contents.get("content", "")
                    if content:
                        import base64
                        decoded_content = base64.b64decode(content).decode('utf-8')
                        version_match = re.search(r'<version>([^<]+)</version>', decoded_content)
                        if version_match:
                            current_version = version_match.group(1)

                            # Get the content from the base branch
                            base_url = f"https://api.github.com/repos/hapifhir/hapi-fhir/contents/pom.xml?ref=master"
                            base_response = requests.get(base_url, headers=headers)
                            if base_response.status_code == 200:
                                base_contents = base_response.json()
                                base_content = base_contents.get("content", "")
                                if base_content:
                                    decoded_base_content = base64.b64decode(base_content).decode('utf-8')
                                    base_version_match = re.search(r'<version>([^<]+)</version>', decoded_base_content)
                                    if base_version_match:
                                        base_version = base_version_match.group(1)
                                        if current_version != base_version:
                                            print(f"Version change detected in pom.xml: {base_version} -> {current_version}")
                                            return True

                # If we got here, pom.xml was changed but we couldn't detect a version change
                print("pom.xml was modified but no version change was detected")
                return False

        # pom.xml not in the list of changed files
        return False

    except Exception as e:
        print(f"Error checking for pom.xml changes: {e}")
        return False

if not target_cdr_branch:
    print("Defaulting CDR branch to master as this is an automatic build.")
    target_cdr_branch = "master"

# Check if pom.xml version has been changed
if check_pom_version_changed():
    print("pom.xml version has been changed. Skipping CDR compilation and marking job as successful.")
    sys.exit(0)

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
