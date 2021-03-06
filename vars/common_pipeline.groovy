#!groovy

def call(Map pipelineParams) {
  pipeline {
    agent {
      node {
	label 'master'
	customWorkspace "/dev/shm/jenkins/${pipelineParams.name}"
      }
    }
    /** build options that are invariant for this pipeline are carried by the
     *  environment variables.
     */
    environment {
      JNK_THREADS=4
      SPACK_ROOT='/scratch/jenkins_spack'
      SPACK_ENV_FILE="../tests/test_automation/ornl_oxygen_spack_env_${pipelineParams.name}.sh"
    }
    options {
      buildDiscarder(logRotator(numToKeepStr: '10'))
    }
    stages {
      stage('CheckOut') {
	steps {
	  checkout scm
	}
      }
      stage('BuildAndTest') {
	failFast true
	matrix {
	  axes {
	    axis {
	      name 'NSPACE'
	      values 'real', 'complex'
	    }
	    axis {
	      name 'PRECISION'
	      values 'full', 'mixed'
	    }
	  }
	  stages {
            stage('Build') {
              steps {
		echo "building ${NSPACE} ${PRECISION} precision ..."
		dir ('./build')
		{
		  sh "../tests/test_automation/jenkins_ornl_oxygen.sh ${NSPACE} ${PRECISION}"
		}
              }
            }
            stage('Test') {
              steps {
		echo "testing ${NSPACE} ${PRECISION} precision ..."
		dir('./build')
		{
		  sh '../tests/test_automation/jenkins_ornl_test.sh ${NSPACE} ${PRECISION} ${JNK_THREADS}'
		}
              }
	    }
	  }
	}
      }
    }
    post {
      failure {
	emailext(body: '${DEFAULT_CONTENT}', mimeType: 'text/html',
		 attachLog: true, subject: '${DEFAULT_SUBJECT}',
		 to: "${qmcJGlobals.maintainer_emails}",
		 recipientProviders: [[$class: 'CulpritsRecipientProvider'],
				      [$class: 'RequesterRecipientProvider']])
      }
      aborted {
	emailext(body: '${DEFAULT_CONTENT}', mimeType: 'text/html',
		 subject: '${DEFAULT_SUBJECT}',
		 to: emailextrecipients([[$class: 'CulpritsRecipientProvider'],
					 [$class: 'RequesterRecipientProvider']]))
      }
    }
  }

}
