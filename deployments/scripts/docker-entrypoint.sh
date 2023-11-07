#!/bin/sh

set -e

ENVIRONMENT=${ENVIRONMENT:-dev}
SERVICE=${SERVICE:-utavi-example-service}
AWS_DEFAULT_REGION=${AWS_DEFAULT_REGION:-us-east-1}
AWS_SERVICES_RETRY_NUM=${AWS_SERVICES_RETRY_NUM:-3}

if curl --retry $AWS_SERVICES_RETRY_NUM --retry-delay 0 --max-time 3 --fail -s http://169.254.169.254/latest/meta-data/ ; then
  $(aws ssm get-parameters-by-path --with-decryption  --path /${ENVIRONMENT}/services/${SERVICE}/ \
  | jq -r '.Parameters| .[] | "export " + .Name + "=\"" + .Value + "\""  '  \
  | sed -e "s~/${ENVIRONMENT}/services/${SERVICE}/~~" \
  | sed -e 's~"~~g')
fi

exec "$@"
