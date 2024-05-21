pipeline {
    agent any

    environment {
        DB_PASSWORD = credentials('db-password')
        DB_USER = credentials('db-user')
        DB_NAME = credentials('db-name')
        DB_HOST = credentials('db-host')
        DB_PORT = credentials('db-port')
        SECRET_KEY = credentials('secret-key')
    }

    stages {
        stage('Create .env files') {
            steps {
                script {
                    // Cria o arquivo .env para o banco de dados
                    writeFile file: '.env', text: """
                        POSTGRES_PASSWORD = ${env.DB_PASSWORD}
                        POSTGRES_USER = ${env.DB_USER}
                        POSTGRES_DB = ${env.DB_NAME}
                    """

                    // Cria o arquivo .env para o backend
                    writeFile file: '/backend/.env', text: """
                        DB_NAME = ${env.DB_NAME}
                        DB_USER = ${env.DB_USER}
                        DB_PASS = ${env.DB_PASSWORD}
                        DB_HOST = ${env.DB_HOST}
                        DB_PORT = ${env.DB_PORT}
                        SECRET_KEY = ${env.SECRET_KEY}
                    """
                }
            }
        }

        stage('Build and run docker containers') {
            steps {
                script {
                    sh 'docker-compose down'
                    sh 'docker-compose build'
                    sh 'docker-compose up -d'
                }
            }
        }
    }
}