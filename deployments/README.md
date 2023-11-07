The following document describes on high level deployment prerequisites and flow.

# Deploy to local environment

In order to deploy service locally we use `docker-compose` tool and start service based
on configuration defined in `docker-compose.yml` file.
Compose file contains all needed dependencies for service to start and configuration
to be set.

## Prerequisites

- Docker latest version installed. [Docker installation documentation](install_docker)
- Docker-compose  tool installed [Docker- compose installation documentation](install_docker_compose)
- Your project should be already built and packaged so needed `jar` files are available in `target` folder.

## Start service

From root of your repository run next command:

```bash
$ docker-compose -f deployments/docker-compose.yml build
$ docker-compose -f deployments/docker-compose.yml up -d
```

First command will build fresh docker image for your service.  
Second command will use built image and start service with it's dependencies.

Logs can be checked via next command:

```bash
$ docker-compose -f deployments/docker-compose.yml logs -f
```

## Stop/Clean up

In order to stop service and clean up resources used for docker container run next command:

```bash
$ docker-compose -f deployments/docker-compose.yml down
```

# Deploy to development environment

In order to deploy service on development environment we use AWS ECS and
`ecs-cli` tool to start service based on configuration defined in
`docker-compose-development.yml` file.  
File `docker-compose-development.yml` contains basic configuration for service
and its task to start on ECS cluster.  
Since we are using ECS it is required to specify additional parameters for ECS
based service. Those parameters are stored in  `ecs-params-development.yml` file.
More information can be found on [ecs-cli compose documentation](ecs_params) page.
Other service configurations and parameters like roles, load balancer, basic infrastructure
setup are done using `tarragrunt` based on configuration defined in
`aws-infrastructure` repository.

## Prerequisites

- Docker latest version installed. [Docker installation documentation](install_docker)
- `ecs-cli`  tool installed. [ECS CLI installation documentation](install_ecs_cli)
- aws profile and credentials configured according to [AWS recommendations](ecs_cli_conf)
- Your project should be already built, packaged and docker image built and pushed
to remote repository. That step is usually done on Jenkins side but in case there's
need to do it manually wokflow is described in section [How to build and publish docker image](#build_and-publish-docker-image)
- ECS cluster and all needed infrastructure setup for service is prepared by administrator
and matches `docker-compose-development.yml` and `ecs-params-development.yml`
configuration files

## Build and publish docker image

Before proceeding with that step make sure you have access to AWS ECR service to
push docker image by checking it with administrator.  
Make sure project is built and `jar` files are generated.

### Build image

```bash
$ docker build -t utavi-ledger-client .
```

### Tag and push image to ECR

That process is fully described in AWS on [docker push ecr image](docker-push-ecr-image) documentation page.
Make sure that source image for tagging and publishing is the same you've built in previous step.

## Start service

Service configuration is managed via compose like file processed by `ecs-cli` tool.
Certain options are specified as arguments despite those may be put into configuration
file just for visibility purpose and making sure deployment is performed on correct cluster.

Pay attention on specific parameter such as:
- `--cluster dev` - name of the cluster created in advance.
- `--region us-east-1` - AWS region where your ECS cluster is running
- `--project-name utavi-ledger-client` - name of the project and service on ECS.
That should match with one created in advance by administrator.

Full list of parameters can be checked in AWS documentation.

**start service command**
```bash
$ ecs-cli compose \
  --verbose \
  --cluster dev \
  --region us-east-1 \
  --project-name utavi-ledger-client \
  -f deployments/docker-compose-development.yml \
  --ecs-params deployments/ecs-params-development.yml  \
  service up
```

## Stop/Clean up

**remove service command**
```bash
$ ecs-cli compose \
  --verbose \
  --cluster dev \
  --region us-east-1 \
  --project-name utavi-ledger-client \
  -f deployments/docker-compose-development.yml \
  --ecs-params deployments/ecs-params-development.yml  \
  service rm
```

[install_docker]: https://docs.docker.com/v17.09/engine/installation/
[install_docker_compose]: https://docs.docker.com/compose/install/
[ecs_cli_conf]: https://docs.aws.amazon.com/AmazonECS/latest/developerguide/ECS_CLI_Configuration.html
[install_ecs_cli]: https://docs.aws.amazon.com/AmazonECS/latest/developerguide/ECS_CLI_installation.html
[ecs_params]: https://docs.aws.amazon.com/AmazonECS/latest/developerguide/cmd-ecs-cli-compose.html
[docker-push-ecr-image]: https://docs.aws.amazon.com/AmazonECR/latest/userguide/docker-push-ecr-image.html
