#!/usr/bin/env groovy
def serviceName = 'utavi-ledger-client'
def repository = "git@github.com:admin-utavi/${serviceName}.git"
def repositoryPrefix = "owner-admin-utavi:repo-${serviceName}"
def repositoryCredentialsId = 'scm-key'
def jobFolderName = "utavi-ledger-client"

folder(jobFolderName)
multibranchPipelineJob("${jobFolderName}/service") {
  branchSources {
    git {
      id "${repositoryPrefix}"
      remote(repository)
      credentialsId(repositoryCredentialsId)
      includes('master develop devops* improvement* qa')
    }
/*
    github {
      id("owner-admin-utavi:repo-utavi-payment-service")
      checkoutCredentialsId('scm-key')
      repoOwner('admin-utavi')
      repository('utavi-user-service')
      includes('master devops*')
      buildOriginPRMerge(true)
    }
*/
  }

  triggers {
    //periodic(2)
    cron('H/5 * * * *')
  }

  orphanedItemStrategy {
    discardOldItems {
      daysToKeep(5)
      numToKeep(20)
    }
  }
}

queue("${jobFolderName}/service")
