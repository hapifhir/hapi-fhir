import datetime
import os
import re
import sys
import json
from time import sleep
import requests

ROBOGARY_URL = "https://slack-bots.azure.smilecdr.com/robogary/"
GITHUB_API_URL = "https://api.github.com"
HAPI_REPO = "hapifhir/hapi-fhir"
# Marker that can be placed in a pull request description or comment to build
# against a specific CDR branch, e.g. !cdr-branch=my-cdr-branch
CDR_BRANCH_MARKER = re.compile(r"!cdr-branch=([\w.\-/]+)")

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

def fetch_pull_request_text():
    """
    Fetch the pull request description followed by the bodies of its comments.
    Returns an empty list if this is not a pull request build or if GitHub cannot be reached.
    """
    if not github_pr.isdigit() or not github_token:
        return []
    headers = {
        "Authorization": f"Bearer {github_token}",
        "Accept": "application/vnd.github+json",
    }
    texts = []
    for url in [
        f"{GITHUB_API_URL}/repos/{HAPI_REPO}/pulls/{github_pr}",
        f"{GITHUB_API_URL}/repos/{HAPI_REPO}/issues/{github_pr}/comments",
    ]:
        try:
            resp = requests.get(url, headers=headers)
            if resp.status_code > 399:
                print(f"Warning: could not read {url} [status={resp.status_code}]")
                continue
            payload = resp.json()
        except (requests.RequestException, ValueError) as e:
            print(f"Warning: could not read {url} [error={e}]")
            continue
        if isinstance(payload, list):
            texts.extend(comment.get("body") or "" for comment in payload)
        else:
            texts.append(payload.get("body") or "")
    return texts


def find_cdr_branch_in_pull_request():
    """
    Look for a !cdr-branch=<branch> marker in the pull request description, then in its comments.
    Returns the branch name, or None if no marker is present.
    """
    for text in fetch_pull_request_text():
        match = CDR_BRANCH_MARKER.search(text)
        if match:
            return match.group(1)
    return None


def resolve_cdr_branch(hapi_branch, explicit_cdr_branch):
    if explicit_cdr_branch:
        return explicit_cdr_branch
    marker_cdr_branch = find_cdr_branch_in_pull_request()
    if marker_cdr_branch:
        print(f"Using CDR branch from pull request marker. [cdr_branch={marker_cdr_branch}]")
        return marker_cdr_branch
    if hapi_branch and hapi_branch.startswith("rel_"):
        mapping_path = os.path.join(
            os.path.dirname(__file__), "hapi_cdr_branch_map.json"
        )
        with open(mapping_path) as f:
            mapping = json.load(f).get("branches", {})
        if hapi_branch not in mapping:
            print(
                f"ERROR: HAPI branch '{hapi_branch}' has no entry in "
                f"hapi_cdr_branch_map.json. Add the corresponding CDR "
                f"release branch and re-run."
            )
            sys.exit(1)
        return mapping[hapi_branch]
    print("Defaulting CDR branch to master (non-release HAPI branch).")
    return "master"

target_cdr_branch = resolve_cdr_branch(current_hapi_branch, target_cdr_branch)

# Prepare data for Robogary request
request_data = {
    "github_pr": github_pr,
    "github_repo": HAPI_REPO,
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

# Abort early if we have skipped the pipeline generation
if trigger_json["skipped"] == True:
    print("Job skipped, as we have detected that this pull request contains a pom.xml version bump.")
    sys.exit(0)

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
