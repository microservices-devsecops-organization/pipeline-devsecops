pipeline {

    agent any

    // Set Maven as tool build
    tools { 
        maven 'Maven 3.3.9' 
        jdk 'jdk8' 
    }

    environment {
        NEXUS_REPOSITORY = 'helloworld'
        NEXUS_PROJECT_SOURCE = './target'
        HARBOR_PROJECT = 'helloworld'
        HARBOR_IMAGE = 'helloworld-image'
        SONAR_PROJECT_KEY = 'helloworld'
        SONAR_PROJECT_NAME = 'helloworld'
        SONAR_PROJECT_SOURCE = './target'
    }

    // Import Shared Library source
    libraries {
        lib('my-shared-lib@master')
    }

    options {
        // Update status to Gitlab
        gitLabConnection('Gitlab-Connection')
    }

    stages {

        stage('Build & Tests') {
            steps {
                // IMPORT BY SHARED LIBRARY
                mavenSharedLib()
            }
        }

        stage('Code Quality') {
            steps {
                // IMPORT BY SHARED LIBRARY
                sonarqubeSharedLib()
            }
        }

        stage("Repository Artifacts") {
            parallel {
                stage("Publish to Nexus") {
                    steps {
                        script {
                            // IMPORT BY SHARED LIBRARY
                            nexusSharedLib()
                        }
                    }
                }
                stage("Publish to Harbor") {
                    steps {
                        // IMPORT BY SHARED LIBRARY
                        harborSharedLib()
                    }
                }
            }
        }

        stage('Deploy QA') {
            input {
                message "Approve deploy? (QA Env)"
            }       
            steps {
                // IMPORT BY SHARED LIBRARY
                kubernetesQASharedLib()
            }
        }

    }
}
