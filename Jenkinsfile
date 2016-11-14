#!/usr/bin/env groovy

/* Only keep the 20 most recent builds */
properties([[$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '20']]])

node {
  stage 'Checkout'
  checkout scm

  stage 'Build'

  /* Call the maven build */
  mvn 'clean install -B -V'

  stage 'Build extended'

  /* Call the maven build again, that will trigger additional tests */
  mvn 'clean install -B -V -Djenkins.version=1.580.1 -Djava.level=8 -Dworkflow-step-api.version=2.3 -Dworkflow-support.version=2.2 -Dworkflow-job.version=2.4 -Dworkflow-basic-steps.version=2.1 -Dworkflow-cps.version=2.10'

  /* Save Results */
  stage 'Results'

  /* Archive the test results */
  junit '**/target/surefire-reports/TEST-*.xml'
}

/* Run maven from tool 'mvn' */
void mvn(def args) {
  /* Get jdk tool */
  String jdktool = tool 'jdk8'

  /* Get the maven tool */
  def mvnHome = tool name: 'mvn'

  /* Set JAVA_HOME, and special PATH variables */
  List javaEnv = [
    "PATH+JDK=${jdktool}/bin", "JAVA_HOME=${jdktool}"
  ]

  /* Call maven tool with java envVars */
  withEnv(javaEnv) {
    timeout(time: 60, unit: 'MINUTES') {
      if (isUnix()) {
        sh "${mvnHome}/bin/mvn ${args}"
      } else {
        bat "${mvnHome}\\bin\\mvn ${args}"
      }
    }
  }
}