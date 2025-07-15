#!/usr/bin/env bash

set -e
export TERM=ansi
export AWS_DEFAULT_REGION=eu-west-1
export AWS_ACCESS_KEY_ID=foobar
export AWS_SECRET_ACCESS_KEY=foobar

export PROVIDER_OVERRIDE_S3=stream
export S3_SKIP_CHECKSUM_VALIDATION=true

# export LOCALSTACK_TMP_FOLDER="${LOCALSTACK_TMP_FOLDER:=/tmp/localstack/test-data/}"
export LOCALSTACK_TMP_FOLDER="${LOCALSTACK_TMP_FOLDER:=/data/test-data/}"

echo "S3 Configuration started"

echo "LOCALSTACK_TMP_FOLDER=${LOCALSTACK_TMP_FOLDER}"

#aws configure set default.s3.disable_multipart true
aws configure set default.s3.signature_version s3v4

aws --endpoint-url=http://localhost:4566 s3 mb s3://mojap-adjudications-insights

# Debug: List files in the chart directory to see what's available
echo "Listing files in ${LOCALSTACK_TMP_FOLDER}/chart/"
ls -la ${LOCALSTACK_TMP_FOLDER}/chart/ || echo "Chart directory not found or empty"

# Upload each chart file explicitly to ensure they're all available
for chart in 1a 1b 1c 1d 1f 2a 2b 2d 2e 2f 2g 3a 3b 4a 4b 4c 4d 5a 5b 5c; do
  if [ -f "${LOCALSTACK_TMP_FOLDER}/chart/${chart}.json" ]; then
    echo "Uploading chart/${chart}.json"
    aws --endpoint-url=http://localhost:4566 s3api put-object \
      --bucket mojap-adjudications-insights \
      --key chart/${chart}.json \
      --body "${LOCALSTACK_TMP_FOLDER}/chart/${chart}.json"
  else
    echo "Warning: ${LOCALSTACK_TMP_FOLDER}/chart/${chart}.json does not exist!"
    # Create an empty placeholder file if it doesn't exist
    echo "Creating empty placeholder for chart/${chart}.json"
    echo '{"MDI": []}' > /tmp/${chart}.json
    aws --endpoint-url=http://localhost:4566 s3api put-object \
      --bucket mojap-adjudications-insights \
      --key chart/${chart}.json \
      --body "/tmp/${chart}.json"
  fi
done

# Still run the recursive copy for any other files
aws --endpoint-url=http://localhost:4566 s3 cp ${LOCALSTACK_TMP_FOLDER} s3://mojap-adjudications-insights --recursive --exclude "chart/*.json"

# List the contents of the S3 bucket to verify
echo "Listing contents of S3 bucket:"
aws --endpoint-url=http://localhost:4566 s3 ls s3://mojap-adjudications-insights/chart/ --recursive

echo "S3 Configured"
