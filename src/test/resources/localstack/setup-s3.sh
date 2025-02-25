#!/usr/bin/env bash

set -e
export TERM=ansi
export AWS_DEFAULT_REGION=eu-west-1
export AWS_ACCESS_KEY_ID=foobar
export AWS_SECRET_ACCESS_KEY=foobar

export PROVIDER_OVERRIDE_S3=legacy
#export AWS_S3_DISABLE_CHUNKED_ENCODING=true
#export S3_SKIP_CHECKSUM_VALIDATION=true

export LOCALSTACK_TMP_FOLDER="${LOCALSTACK_TMP_FOLDER:=/tmp/localstack/test-data/}"

echo "S3 Configuration started"

echo "LOCALSTACK_TMP_FOLDER=${LOCALSTACK_TMP_FOLDER}"

aws configure set default.s3.disable_multipart true
aws configure set default.s3.signature_version s3v4

aws --endpoint-url=http://localhost:4566 s3 mb s3://mojap-adjudications-insights

# Attempt upload 3 times with delay
for i in {1..3}; do
  aws --endpoint-url=http://localhost:4566 s3api put-object \
      --bucket mojap-adjudications-insights \
      --key chart/4b.json \
      --body "${LOCALSTACK_TMP_FOLDER}/chart/4b.json" && break || sleep 2
done

#aws --endpoint-url=http://localhost:4566 s3 cp ${LOCALSTACK_TMP_FOLDER} s3://mojap-adjudications-insights --recursive --exclude "chart/4b.json"
# Upload remaining files using sync instead of copy
aws --endpoint-url=http://localhost:4566 s3 sync "${LOCALSTACK_TMP_FOLDER}" s3://mojap-adjudications-insights --exact-timestamps

echo "Checking file existence:"
ls -la "${LOCALSTACK_TMP_FOLDER}/chart/4b.json"
md5sum "${LOCALSTACK_TMP_FOLDER}/chart/4b.json"

echo "S3 Configured"
