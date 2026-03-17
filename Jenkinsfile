pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build with Maven') {
            agent {
                docker {
                    image 'maven:3.9.6-eclipse-temurin-17'
                    args '-v $HOME/.m2:/root/.m2'
                }
            }
            steps {
                dir('auth-service') { sh 'mvn clean package -DskipTests' }
                dir('customer-service') { sh 'mvn clean package -DskipTests' }
                dir('policy-service') { sh 'mvn clean package -DskipTests' }
                dir('claim-service') { sh 'mvn clean package -DskipTests' }
                dir('document-service') { sh 'mvn clean package -DskipTests' }
                dir('notification-service') { sh 'mvn clean package -DskipTests' }
                dir('audit-service') { sh 'mvn clean package -DskipTests' }
                dir('api-gateway') { sh 'mvn clean package -DskipTests' }
            }
        }

        stage('Build Docker Images') {
            steps {
                sh 'docker compose build'
            }
        }

        stage('Deploy Stack') {
            steps {
                sh 'docker compose up -d'
            }
        }
    }

    post {
        success {
            echo 'Deployment successful'
        }
        failure {
            echo 'Deployment failed'
        }
    }
}