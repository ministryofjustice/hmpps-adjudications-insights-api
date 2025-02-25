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

aws configure set default.s3.disable_multipart true
aws configure set default.s3.signature_version s3v4

aws --endpoint-url=http://localhost:4566 s3 mb s3://mojap-adjudications-insights

aws --endpoint-url=http://localhost:4566 s3api put-object \
    --bucket mojap-adjudications-insights \
    --key chart/4b.json \
    --body "${LOCALSTACK_TMP_FOLDER}/chart/4b.json"

#aws --debug --endpoint-url=http://localhost:4566 s3 cp ${LOCALSTACK_TMP_FOLDER} s3://mojap-adjudications-insights --recursive --dryrun
#aws --endpoint-url=http://localhost:4566 s3 cp ${LOCALSTACK_TMP_FOLDER} s3://mojap-adjudications-insights --recursive

# Upload all JSON files individually, skipping chart/4b.json
find "${LOCALSTACK_TMP_FOLDER}" -type f -name '*.json' | while read -r file; do
    # Compute a key relative to LOCALSTACK_TMP_FOLDER.
    key="${file#${LOCALSTACK_TMP_FOLDER}/}"
    # Skip chart/4b.json if desired (or upload it separately)
    if [[ "$key" == "chart/4b.json" ]]; then
        echo "Skipping $key (to avoid multipart upload issues)"
        continue
    fi
    echo "Uploading ${file} as ${key}"
    aws --endpoint-url=http://localhost:4566 s3api put-object \
        --bucket mojap-adjudications-insights \
        --key "${key}" \
        --body "${file}"
done

#echo "Checking file existence:"
#ls -la "${LOCALSTACK_TMP_FOLDER}/chart/4b.json"
#md5sum "${LOCALSTACK_TMP_FOLDER}/chart/4b.json"

echo "S3 Configured"
