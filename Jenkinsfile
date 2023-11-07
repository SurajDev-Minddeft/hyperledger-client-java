/**
 * Jenkins file uses shared libraries
 * Shared libraries location: git@github.com:admin-utavi/utavi-jenkins-shared-libs.git
 *
 * Include allows to override default source branch for utavi-jenkins-shared-libs
 * @Library('pipeline_shared_libs@my-new-feature-branch') _
 *
 */

@Library('pipeline_shared_libs@master') _

pipeline {
  agent none

  options {
    timeout(time: 3, unit: 'HOURS')
    skipDefaultCheckout()
    preserveStashes(buildCount: 20)
  }

  environment {
    AWS_ACCOUNT_ID       = '551641973471'
    AWS_REGION           = 'us-east-1'
    REGISTRY             = '551641973471.dkr.ecr.us-east-1.amazonaws.com'
    REGISTRY_URL         = "https://${REGISTRY}"
    REGISTRY_REPO        = 'ledger-client'
    PROJECT_NAME         = 'utavi-ledger-client'
    GIT_COMMIT_HASH      = ''
    SLACK_TEAM           = 'Utavi'
    SLACK_CHANNEL        = '#jenkins'
    SLACK_APPROVERS      = '#jenkins'
    VERSION_FILE         = 'version.info'
    DEFAULT_DEV_BRANCH   = 'develop' // 'develop'
    DEFAULT_QA_BRANCH    = 'qa'
    DEFAULT_STAGE_BRANCH = 'none'
    DEFAULT_PROD_BRANCH  = 'none'
    // Integration test related configuration
    K8S_ENV_NAME_PATERN  = "it-SALT" // NOTE: no more than 11 chars due to limitations
    K8S_HOST_PATERN      = "kube-ENV_NAME.dev.utavi.com"
  }

  stages {
    stage('Compile') {
      agent {
        docker {
          image 'maven:3-jdk-8-alpine'
          args '-v $HOME/.m2:/root/.m2'
        }
      }
      steps {
        checkout scm

        script {
          currentBuild.displayName = getServiceVersion('mvn', BRANCH_NAME, BUILD_NUMBER)
          writeFile file: "${VERSION_FILE}", text: currentBuild.displayName
          stash includes: "${VERSION_FILE}", name: 'version'
        }

        ansiColor('xterm') {
          sh 'mvn clean compile'
        }
      }
    }

    stage('Run test') {
      parallel {
        stage('Code check') {
          agent {
            docker {
              image 'maven:3-jdk-8-alpine'
              args '-v $HOME/.m2:/root/.m2'
            }
          }
          steps {
            checkout scm

            script {
              runInitStep(VERSION_FILE, true)

              ansiColor('xterm') {
                // sh 'mvn -Dsonar.host.url=http://172.17.0.1:9000 -Dsonar.jdbc.url="jdbc:h2:tcp://172.17.0.1/sonar" compile sonar:sonar checkstyle:checkstyle checkstyle:checkstyle-aggregate'
                sh 'mvn compile checkstyle:checkstyle checkstyle:checkstyle-aggregate'
              }

              //step([$class: 'CheckStylePublisher',
              //  canRunOnFailed: true,
              //  defaultEncoding: '',
              //  healthy: '',
              //  pattern: '**/target/checkstyle-result.xml',
              //  unHealthy: '',
              //  useStableBuildAsReference: true
              //])
              recordIssues(
                enabledForFailure: true, aggregatingResults: true,
                tools: [java(), checkStyle(pattern: '**/target/checkstyle-result.xml', reportEncoding: 'UTF-8')]
              )
            }
          }
        }
//         stage('Unit tests') {
//           agent {
//             docker {
//               image 'maven:3-jdk-8-alpine'
//               args '-v $HOME/.m2:/root/.m2'
//             }
//           }
//           steps {
//             checkout scm
//             script {
//               runInitStep(VERSION_FILE, true)
//             }
//
//             ansiColor('xterm') {
//               sh 'mvn test'
//             }
//
//             junit allowEmptyResults: true, healthScaleFactor: 1.0, testResults: '**/target/surefire-reports/*.xml'
//           }
//         }
      }
    }

//     stage('Integration tests') {
//       agent {
//         label 'slave'
//       }
//       when {
//         branch 'master'
//       }
//       steps {
//         script {
//           checkout([
//             $class: 'GitSCM',
//             branches: scm.branches,
//             userRemoteConfigs: scm.userRemoteConfigs,
//             doGenerateSubmoduleConfigurations: false,
//             extensions: scm.extensions + [
//               [$class: 'LocalBranch'],
//               [$class: 'SubmoduleOption',
//                 disableSubmodules: false,
//                 parentCredentials: true,
//                 recursiveSubmodules: true,
//                 reference: '',
//                 trackingSubmodules: false
//               ],
//               [$class: 'CleanBeforeCheckout'],
//               [$class: 'CleanCheckout']],
//             submoduleCfg: []
//           ])
//
//           def version = runInitStep(VERSION_FILE, true)
//           GIT_COMMIT_HASH = sh(script: "git log -n 1 --pretty=format:'%H'", returnStdout: true).trim()
//           def environmentName = K8S_ENV_NAME_PATERN.replace('SALT', "${GIT_COMMIT_HASH[0..5]}-${BUILD_NUMBER}")
//           def k8sHost = K8S_HOST_PATERN.replace('ENV_NAME', environmentName)
//           def postgresHost = 'db'
//           def postgresDB = 'utavi-ledger-db'
//           def postgresUser = 'postgres'
//           def postgresPassword = 'root'
//           def retries = 30
//
//           echo "Create kubernetes environment"
//           build(
//             job: '/devops/manage-awsminikube',
//             parameters: [
//               string(name: 'WORKSPACE', value: "${environmentName}"),
//               string(name: 'ACTION', value: "create"),
//               string(name: 'USE_SPOT_INSTANCE', value: "true"),
//             ],
//             propagate: true,
//             wait: true
//           )
//
//           echo "Deploy hyperledger"
//           build(
//             job: 'helm-charts/hyperledger-fabric/deploy',
//             parameters: [
//               string(name: 'ENVIRONMENT', value: "custom"),
//               string(name: 'CUSTOM_ENV_NAME', value: "${environmentName}"),
//               string(name: 'CHART_VERSION', value: "latest"),
//             ],
//             propagate: true,
//             wait: true
//           )
//
//           echo "Deploy resource-server"
//           build(
//             job: 'helm-charts/resource-server/deploy',
//             parameters: [
//               string(name: 'ENVIRONMENT', value: "custom"),
//               string(name: 'CUSTOM_ENV_NAME', value: "${environmentName}"),
//               string(name: 'SERVICE_VERSION', value: "latest"),
//               string(name: 'CHART_VERSION', value: "latest"),
//             ],
//             propagate: true,
//             wait: true
//           )
//
//           docker.image('postgres:10-alpine').withRun("-e 'POSTGRES_DB=${postgresDB}' -e 'POSTGRES_USER=${postgresUser}' -e 'POSTGRES_PASSWORD=${postgresPassword}'") { c ->
//             docker.image('postgres').inside("--link ${c.id}:${postgresHost}") {
//               /* Wait until db server is up */
//               sh """#!/bin/bash
//               RETRIES=${retries}
//
//               until PGPASSWORD=${postgresPassword} psql -h ${postgresHost} -U ${postgresUser} -d ${postgresDB} -c "select 1" > /dev/null 2>&1 || [ \$RETRIES -eq 0 ]; do
//                 echo "Waiting for postgres server, \$((RETRIES--)) remaining attempts..."
//                 sleep 1
//               done
//               """
//             }
//
//             docker.image('maven:3-jdk-8-alpine').inside("--link ${c.id}:${postgresHost}") {
//               withEnv([
//                   "DB_HOST=${postgresHost}",
//                   "DB_PORT=5432",
//                   "DB_SCHEMA=${postgresDB}",
//                   "DB_USER=${postgresUser}",
//                   "DB_PASSWORD=${postgresPassword}",
//                   "FABRIC_IP=${k8sHost}",
//                   "RESOURCE_SERVER_HOST=${k8sHost}",
//                   "RESOURCE_SERVER_PORT=30000",
//                   "LATOR_HOST=${k8sHost}",
//                   "LATOR_PORT=30059",
//                   "FABRIC_HOST=${k8sHost}",
//                   "PEER_ORG_NAME=main-utavi",
//                   "CHANNEL_NUM=2",
//                   "PEERS_PER_CHANNEL=3",
//                   "PEER_START_PORT=30400",
//                   "PEER_ORG_ICA_NUM=1",
//                   "PEER_ORG_ICA_START_PORT=31154",
//                   "ORDERER_ORG_NAME=orderer-utavi",
//                   "ORDERER_NUM=1",
//                   "ORDERER_START_PORT=30300",
//                   "ORDERER_ORG_ICA_NUM=1",
//                   "ORDERER_ORG_ICA_START_PORT=30154",
//                 ]) {
//                 ansiColor('xterm') {
//                   sh 'mvn clean verify -Dspring.profiles.active=test'
//                 }
//               }
//               archiveArtifacts artifacts: 'target/generated-docs/*.html'
//
//               def htmlFiles
//               dir ('target/generated-docs') {
//                   htmlFiles = findFiles glob: '*.html'
//               }
//               publishHTML (target: [
//                 allowMissing: false,
//                 alwaysLinkToLastBuild: false,
//                 keepAll: true,
//                 reportDir: 'target/generated-docs',
//                 reportFiles: htmlFiles.join(','),
//                 reportName: "API docs"
//               ])
//             }
//           }
//         }
//       }
//       post {
//         always {
//           echo "Run destroy integrtation test environment"
//           node('master') {
//             script {
//               def environmentName = K8S_ENV_NAME_PATERN.replace('SALT', "${GIT_COMMIT_HASH[0..5]}-${BUILD_NUMBER}")
//               def k8sHost = K8S_HOST_PATERN.replace('ENV_NAME', environmentName)
//
//               build(
//                 job: '/devops/manage-awsminikube',
//                 parameters: [
//                   string(name: 'WORKSPACE', value: "${environmentName}"),
//                   string(name: 'ACTION', value: "destroy"),
//                   string(name: 'USE_SPOT_INSTANCE', value: "true"),
//                 ],
//                 propagate: false,
//                 wait: false
//               )
//             }
//           }
//         }
//       }
//     }

    stage('Package') {
      agent {
        docker {
          image 'maven:3-jdk-8-alpine'
          args '-v $HOME/.m2:/root/.m2'
        }
      }
      steps {
        checkout scm
        script {
          runInitStep(VERSION_FILE, true)
        }
        ansiColor('xterm') {
          sh 'mvn package -Dmaven.test.skip=true'
        }
        stash includes: '**/target/*.jar', name: 'artifact'
      }
    }

    stage('Dockerize') {
      agent {
        label 'master'
      }
      steps {
        checkout scm
        unstash 'artifact'
        script {
          def version = runInitStep(VERSION_FILE, true)

          runDockerization(REGISTRY, REGISTRY_URL, REGISTRY_REPO, version, BRANCH_NAME)
        }
      }
    }

    stage('Deploy to dev') {
      agent none
      when {
        expression {
          node('master') {
            version = runInitStep(VERSION_FILE, true)
          }
          def environment = "development"
          def defaultBranch = DEFAULT_DEV_BRANCH

          return isDeploymentApproved(PROJECT_NAME, BRANCH_NAME, defaultBranch, environment, version, false, SLACK_TEAM, SLACK_APPROVERS, 30)
        }
      }
      steps {
        script {
          node('master') {
            def version = runInitStep(VERSION_FILE, true)
            def environment = "development"
            def ecsCluster = 'dev'
            def minHealthyPercent = 0
            def maxPercent = 100
            def sendSlack = true

            lock("${environment}-${PROJECT_NAME}-service") {
              runServiceDeployment(PROJECT_NAME, ecsCluster, environment, REGISTRY, REGISTRY_REPO, version, minHealthyPercent, maxPercent, AWS_REGION, sendSlack, SLACK_TEAM, SLACK_CHANNEL)
            }
          }
        }
      }
    }

    stage('Deploy to QA') {
      agent none
      when {
        // make nice displayName for build in case of QA re-run
        expression {
          node('master') {
            runInitStep(VERSION_FILE, true)
          }
          return true
        }
        anyOf {
          branch 'qa';
        }
        expression {
          node('master') {
            version = runInitStep(VERSION_FILE, true)
          }
          def environment = "qa"
          def defaultBranch = DEFAULT_QA_BRANCH

          return isDeploymentApproved(PROJECT_NAME, BRANCH_NAME, defaultBranch, environment, version, true, SLACK_TEAM, SLACK_APPROVERS,  30)
        }
      }
      steps {
        script {
          node('master') {
            def version = runInitStep(VERSION_FILE, true)
            def environment = "qa"
            def ecsCluster = 'qa'
            def minHealthyPercent = 0
            def maxPercent = 100
            def sendSlack = true

            lock("${environment}-${PROJECT_NAME}-service") {
              runServiceDeployment(PROJECT_NAME, ecsCluster, environment, REGISTRY, REGISTRY_REPO, version, minHealthyPercent, maxPercent, AWS_REGION, sendSlack, SLACK_TEAM, SLACK_CHANNEL)
            }
          }
        }
      }
    }

    stage('Deploy to stage') {
      agent none
      when {
        // make nice displayName for build in case of stage re-run
        expression {
          node('master') {
            runInitStep(VERSION_FILE, true)
          }
          return true
        }
        anyOf {
          branch 'master';
        }
        expression {
          node('master') {
            version = runInitStep(VERSION_FILE, true)
          }
          def environment = "staging"
          def defaultBranch = DEFAULT_STAGE_BRANCH

          return isDeploymentApproved(PROJECT_NAME, BRANCH_NAME, defaultBranch, environment, version, true, SLACK_TEAM, SLACK_APPROVERS,  60)
        }
      }
      steps {
        script {
          node('master') {
            def version = runInitStep(VERSION_FILE, true)
            def environment = "staging"
            def ecsCluster = 'stage'
            def minHealthyPercent = 0
            def maxPercent = 100
            def sendSlack = true

            lock("${environment}-${PROJECT_NAME}-service") {
              runServiceDeployment(PROJECT_NAME, ecsCluster, environment, REGISTRY, REGISTRY_REPO, version, minHealthyPercent, maxPercent, AWS_REGION, sendSlack, SLACK_TEAM, SLACK_CHANNEL)
            }
          }
        }
      }
    }

    stage('Deploy to production') {
      agent none
      when {
        // make nice displayName for build in case of stage re-run
        expression {
          node('master') {
            runInitStep(VERSION_FILE, true)
          }
          return true
        }
        anyOf {
          branch 'master';
        }
        expression {
          node('master') {
            version = runInitStep(VERSION_FILE, true)
          }
          def environment = "production"
          def defaultBranch = DEFAULT_PROD_BRANCH
          def sendSlack = false

          return isDeploymentApproved(PROJECT_NAME, BRANCH_NAME, defaultBranch, environment, version, sendSlack, SLACK_TEAM, SLACK_APPROVERS,  20)
        }
      }
      steps {
        script {
          node('master') {
            def version = runInitStep(VERSION_FILE, true)
            def environment = "production"
            def ecsCluster = 'prod'
            def minHealthyPercent = 0
            def maxPercent = 100
            def sendSlack = true

            lock("${environment}-${PROJECT_NAME}-service") {
              runServiceDeployment(PROJECT_NAME, ecsCluster, environment, REGISTRY, REGISTRY_REPO, version, minHealthyPercent, maxPercent, AWS_REGION, sendSlack, SLACK_TEAM, SLACK_CHANNEL)
            }
          }
        }
      }
    }
  }
  post {
    success {
      script {
        runSlackNotify("${SLACK_TEAM}", "${SLACK_CHANNEL}", "SUCCESS", "[SUCCESS] build status *${PROJECT_NAME}* version *${currentBuild.displayName}*")
      }
    }
    failure {
      script {
        runSlackNotify("${SLACK_TEAM}", "${SLACK_CHANNEL}", "FAILURE", "[FAILURE] build status *${PROJECT_NAME}* version *${currentBuild.displayName}*")
      }
    }
    always {
      node('master') {
        script {
          version = currentBuild.displayName.split('\\.').last().contains('replay') ? currentBuild.displayName.split('\\.').dropRight(1).join('.') : currentBuild.displayName
          runDockerCleanup(REGISTRY, REGISTRY_REPO, version)
        }
      }
    }
  }
}
