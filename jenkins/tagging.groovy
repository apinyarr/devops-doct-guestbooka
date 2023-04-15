def Branch = "develop"

pipeline {
    agent {
        label "master"
    }

    parameters {
        string(name: 'Branch', description: 'Branch name. Default branch is develop.')
        string(name: 'Tag', description: 'Give tag name, e.g. 1.0')
    }

    options {
        // skipDefaultCheckout()
        timeout(time: 5, unit: 'MINUTES')
        timestamps()
    }

    stages {
        stage('Preparation') {
            steps {
                script {
                    echo "********************* Stage: Preparation *********************"
                    echo "--------------------- Step: Reading Branch ---------------------"
                    if(params.Branch != '') {
                        Branch = params.Branch
                    }
                    echo "Branch/Tag: ${Branch}"
                }
            }
        }

        stage("Git Tagging") {
            agent {
                label "sit-app"
            }

            steps {
                script {
                    echo "********************* Stage: Git Tagging *********************"
                    echo "--------------------- Step: Checking out from ${params.Branch} ---------------------"
                    sh "git checkout origin/${params.Branch}"
                    echo "--------------------- Step: Tagging name ${params.Tag}.${BUILD_NUMBER} ---------------------"
                    sh "git tag v${params.Tag}.${BUILD_NUMBER}"
                    sh "git push origin v${params.Tag}.${BUILD_NUMBER}"
                }
            }
        }

        stage("Docker Tag") {
            agent {
                label "sit-app"
            }

            environment {
                DOCKERHUB_CRED = credentials('dockerhubcred')
            }

            steps {
                script {
                    echo "********************* Stage: Docker Tag *********************"
                    echo "--------------------- Step: Build an image from ${params.Branch} ---------------------"
                    dir('./app') {
                        sh "docker build -t apinyarr/guestbooka:v${params.Tag}.${BUILD_NUMBER} ."
                        sh "docker images"
                    }
                    // echo "--------------------- Step: Login to Docker Hub  ---------------------"
                    // ssh "docker login --username $DOCKERHUB_CRED"
                    echo "--------------------- Step: Push build image to Docker Hub  ---------------------"
                    sh "docker push apinyarr/guestbooka:v${params.Tag}.${BUILD_NUMBER}"
                    currentBuild.description = "Tagging: v${params.Tag}.${BUILD_NUMBER}"
                }
            }
        }

        stage('Clean Up') {
            when {
                expression { return params.Environment == "SIT" }
            }

            agent {
                label "sit-app"
            }

            options {
                skipDefaultCheckout()
            }

            steps {
                script {
                    echo "********************* Stage: Clean Up *********************"
                    echo "--------------------- Step: Clean Up Work Space ---------------------"
                    cleanWs()
                }
            }
        }
    }
}