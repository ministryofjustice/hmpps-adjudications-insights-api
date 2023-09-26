# hmpps-adjudications-insights-api
[![repo standards badge](https://img.shields.io/badge/dynamic/json?color=blue&style=flat&logo=github&label=MoJ%20Compliant&query=%24.result&url=https%3A%2F%2Foperations-engineering-reports.cloud-platform.service.justice.gov.uk%2Fapi%2Fv1%2Fcompliant_public_repositories%2Fhmpps-adjudications-insights-api)](https://operations-engineering-reports.cloud-platform.service.justice.gov.uk/public-github-repositories.html#hmpps-adjudications-insights-api "Link to report")
[![CircleCI](https://circleci.com/gh/ministryofjustice/hmpps-adjudications-insights-api/tree/main.svg?style=svg)](https://circleci.com/gh/ministryofjustice/hmpps-adjudications-insights-api)
[![Docker Repository on Quay](https://quay.io/repository/hmpps/hmpps-adjudications-insights-api/status "Docker Repository on Quay")](https://quay.io/repository/hmpps/hmpps-adjudications-insights-api)
[![API docs](https://img.shields.io/badge/API_docs_-view-85EA2D.svg?logo=swagger)](https://hmpps-adjudications-insights-api-dev.hmpps.service.justice.gov.uk/webjars/swagger-ui/index.html?configUrl=/v3/api-docs)

This is a skeleton project from which to create new kotlin projects from.

# Instructions

If this is a HMPPS project then the project will be created as part of bootstrapping - 
see https://github.com/ministryofjustice/dps-project-bootstrap.

## Creating a CloudPlatform namespace

When deploying to a new namespace, you may wish to use this template kotlin project namespace as the basis for your new namespace:

<https://github.com/ministryofjustice/cloud-platform-environments/tree/main/namespaces/live.cloud-platform.service.justice.gov.uk/hmpps-adjudications-insights-api>

Copy this folder, update all the existing namespace references, and submit a PR to the CloudPlatform team. Further instructions from the CloudPlatform team can be found here: <https://user-guide.cloud-platform.service.justice.gov.uk/#cloud-platform-user-guide>

## Renaming from Hmpps Adjudications Insights Api - github Actions

Once the new repository is deployed. Navigate to the repository in github, and select the `Actions` tab.
Click the link to `Enable Actions on this repository`.

Find the Action workflow named: `rename-project-create-pr` and click `Run workflow`.  This workflow will
execute the `rename-project.bash` and create Pull Request for you to review.  Review the PR and merge.

Note: ideally this workflow would run automatically however due to a recent change github Actions are not
enabled by default on newly created repos. There is no way to enable Actions other then to click the button in the UI.
If this situation changes we will update this project so that the workflow is triggered during the bootstrap project.
Further reading: <https://github.community/t/workflow-isnt-enabled-in-repos-generated-from-template/136421>

## Manually renaming from Hmpps Adjudications Insights Api

Run the `rename-project.bash` and create a PR.

The `rename-project.bash` script takes a single argument - the name of the project and calculates from it:
* The main class name (project name converted to pascal case) 
* The project description (class name with spaces between the words)
* The main package name (project name with hyphens removed)

It then performs a search and replace and directory renames so the project is ready to be used.

## Upload Chart json file to s3 bucket.

Please follow the steps below to deploy:

1. Open Terminal
2. Check Service Pod Status, Ensure that the service pod is up and running: 
     ```kubectl get pods -n <<NAME_SPACE>>```
3. Copy Chart Files to Service Pod, Use the following command to copy your chart files to the service pod:
     ```kubectl cp <<REPO_LOCATION>>/hmpps-adjudications-insights-api/src/test/resources/test-data <<NAME_SPACE>>/<<SERVICE_POD_NAME>>:/tmp```
4. Access the Service Pod, Exec into the service pod with: 
    ```kubectl exec -it <<SERVICE_POD_NAME>> -n <<NAME_SPACE>> -- /bin/sh```
5. Navigate to Directory The charts can be found in the `/tmp/test-data` directory. To navigate there, use:  ```cd /tmp/test-data```
6. Copy Files to S3 Bucket. Use the following command to sync your files to the desired S3 bucket: 
   ```aws s3 sync . s3://<<BUCKET_NAME>>```
   
NOTE: Make sure to replace placeholders like `<<NAME_SPACE>>`, `<<SERVICE_POD_NAME>>`, and `<<BUCKET_NAME>>` with appropriate values before running the commands.

## Running the service

Run localstack by running the below docker compose:

```bash
docker-compose up
```

### Running the tests

With localstack now up and running, run:
```bash
./gradlew test
```

To run the app, the following profiles need to be enabled: 'dev,localstack,local'
therefore with gradle, run:
```bash
./gradlew bootRun --args='--spring.profiles.active=localstack,local'
```



