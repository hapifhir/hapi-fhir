import datetime
import os
import sys
from time import sleep
import requests

complete_statuses = ["failed", "success", "canceled"]
trigger_token = os.getenv("TRIGGER_TOKEN")
project_api_read_token = os.getenv("READ_API_TOKEN")
current_hapi_branch = os.getenv("HAPI_BRANCH")
target_cdr_branch = os.getenv("CDR_BRANCH")

if not target_cdr_branch:
    print("Defaulting CDR branch to master as this is an automatic build.")
    target_cdr_branch = "master"

form_data = {
    "token": trigger_token,
    "ref": target_cdr_branch,
    "variables[HAPI_BRANCH]": current_hapi_branch
}

print(f"About to start job. [target_cdr_branch={target_cdr_branch}, current_hapi_branch={current_hapi_branch}]")
print("Triggering Remote CI process on gitlab.com.")
result = requests.post("https://gitlab.com/api/v4/projects/1927185/trigger/pipeline", data=form_data)
if result.status_code > 399:
    print(result.json())
trigger_json = result.json()
pipeline_id = trigger_json["id"]


def poll_for_pipeline_status(pipeline_id):
    query_params = {
        "private_token": project_api_read_token
    }
    resp = requests.get(f"https://gitlab.com/api/v4/projects/1927185/pipelines/{pipeline_id}", params=query_params)
    pipeline_status_json = resp.json()
    return pipeline_status_json


print(f"Generated pipeline. [pipeline_id={pipeline_id}]")
status = None
status_json = poll_for_pipeline_status(pipeline_id)
start_time = datetime.datetime.now()

while status not in complete_statuses:
    status = status_json["status"]
    now = datetime.datetime.now()
    print(f"Job not yet complete. [status={status}, duration={(now - start_time).total_seconds()}s]")
    sleep(10)
    status_json = poll_for_pipeline_status(pipeline_id)

if status == "success":
    print(f"CDR compiled against this branch! Please visit: {status_json['web_url']}")
    sys.exit(0)
else:
    print(f"CDR failed against this branch! Please visit: {status_json['web_url']}")
    sys.exit(1)