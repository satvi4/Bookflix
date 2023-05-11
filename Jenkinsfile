pipeline {
     environment{
        dockerimg = ''
        dockerimg2 = ''
        DOCKERHUB_CREDENTIALS = credentials('dockerhub')
    }
    agent any

    stages {
        stage('Git pull') {
            steps {
                git 'https://github.com/satvi4/Bookflix'
            }
        }
        stage('Maven Build'){
            steps {
                dir('backend'){
                    sh 'mvn clean install'
                }
            }
        }
        stage('Docker image build'){
            steps {
                dir('backend'){
                    script{
                    dockerimg = docker.build("satvi4/bookflix-backend:latest")
                    }
                }
                dir('frontend'){
                    script{
                    dockerimg2 = docker.build("satvi4/bookflix-frontend:latest")
                    }
                }
            }
        }
        stage('Push Docker Image') {
            steps {
                script{
                    sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
                    docker.withRegistry('','dockerhub'){
                    dockerimg.push()
                    }
                    sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
                    docker.withRegistry('','dockerhub'){
                    dockerimg2.push()
                    }
                }
            }
        }
        stage('Ansible Deploy'){
            steps{
                sh 'export LANG=en_US.UTF-8'
                ansiblePlaybook becomeUser: null, colorized: true, disableHostKeyChecking: true, installation: 'Ansible', inventory: 'inventory', playbook: 'playbook.yml', sudoUser: null
            }
        }
    }
}
