#!/usr/bin/env bash

set -e
export TERM=ansi
export AWS_DEFAULT_REGION=eu-west-1
export AWS_ACCESS_KEY_ID=foobar
export AWS_SECRET_ACCESS_KEY=foobar

export LOCALSTACK_TMP_FOLDER="${LOCALSTACK_TMP_FOLDER:=/tmp/localstack/test-data/}"

echo "S3 Configuration started"

echo "LOCALSTACK_TMP_FOLDER=${LOCALSTACK_TMP_FOLDER}"

aws --endpoint-url=http://localhost:4566 s3 mb s3://mojap-adjudications-insights
aws --endpoint-url=http://localhost:4566 s3 cp ${LOCALSTACK_TMP_FOLDER} s3://mojap-adjudications-insights --recursive
echo "S3 Configured"
