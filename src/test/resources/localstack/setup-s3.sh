#!/usr/bin/env bash
set -e
export TERM=ansi
export AWS_ACCESS_KEY_ID=foobar
export AWS_SECRET_ACCESS_KEY=foobar
export AWS_DEFAULT_REGION=eu-west-2
export PAGER=

echo "S3 Configuration started 22"

ls
aws --endpoint-url=http://localhost:4566 s3 mb s3://mojap-adjudications-insights
#aws --endpoint-url=http://localhost:4566 s3 cp /tmp/test-data s3://mojap-adjudications-insights
#aws --endpoint-url=http://localhost:4566 s3 cp src/test/resources/test-data/ s3://mojap-adjudications-insights/chart

echo "S3 Configured"
