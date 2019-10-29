
def call() {

    // ----------------------------------------
    // ENVIRONMENT
    SONAR_HOST = 'http://sonar.dev.k8s.claro.com.br' //'http://172.31.27.27:9000'
    // ----------------------------------------

    echo "Run code analysis"
    echo "Run SAST with FindSecBugs" 
    echo "Run dependency check with FindSecBugs + OWASP"

    script {
        scannerHome = tool 'SonarScanner' //'SonarQube-Scanner'
    }

    // SonarQube
    //withSonarQubeEnv('SonarQube-Connection') {
    withSonarQubeEnv('Sonarqube') {    
        sh "${scannerHome}/bin/sonar-scanner -Dsonar.host.url=${SONAR_HOST} -Dsonar.projectKey=${SONAR_PROJECT_KEY} -Dsonar.projectName=${SONAR_PROJECT_NAME} -Dsonar.sources=${SONAR_PROJECT_SOURCE} -Dsonar.scm.disabled='True'"
    }

    timeout(time: 5, unit: 'MINUTES') {
        waitForQualityGate abortPipeline: true
    }
}
