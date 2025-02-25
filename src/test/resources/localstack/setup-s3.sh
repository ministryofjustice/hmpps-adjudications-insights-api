#!/usr/bin/env bash

set -e
export TERM=ansi
export AWS_DEFAULT_REGION=eu-west-1
export AWS_ACCESS_KEY_ID=foobar
export AWS_SECRET_ACCESS_KEY=foobar

export PROVIDER_OVERRIDE_S3=stream
#export AWS_S3_DISABLE_CHUNKED_ENCODING=true
export S3_SKIP_CHECKSUM_VALIDATION=true

export LOCALSTACK_TMP_FOLDER="${LOCALSTACK_TMP_FOLDER:=/tmp/localstack/test-data/}"

echo "S3 Configuration started"

echo "LOCALSTACK_TMP_FOLDER=${LOCALSTACK_TMP_FOLDER}"

aws --version
aws configure set default.s3.disable_multipart true

aws --endpoint-url=http://s3.localhost.localstack.cloud:4566 s3 mb s3://mojap-adjudications-insights
aws --endpoint-url=http://s3.localhost.localstack.cloud:4566 s3 cp ${LOCALSTACK_TMP_FOLDER} s3://mojap-adjudications-insights --recursive --multipart-chunk-size-mb 1000
echo "S3 Configured"
