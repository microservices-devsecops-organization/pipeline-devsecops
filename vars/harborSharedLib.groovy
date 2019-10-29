def call(){

    // ----------------------------------------
    // ENVIRONMENT
    HARBOR_HOST = 'harbor.claro.com.br' //'52.14.127.219'
    HARBOR_USER = '93353641' //'admin'
    HARBOR_PASSWORD = 'Mudar@025' //'Harbor12345'
    HARBOR_BASE64 = 'Basic OTMzNTM2NDE6TXVkYXJAMDI1'
    // ----------------------------------------
    
    sh "docker build -t ${HARBOR_IMAGE} ./${PROJECT_NAME}"
    sh "docker tag ${HARBOR_IMAGE} ${HARBOR_HOST}/${HARBOR_PROJECT}/${HARBOR_IMAGE}:${GIT_COMMIT}"
    sh "docker login ${HARBOR_HOST} --username ${HARBOR_USER} --password ${HARBOR_PASSWORD}"
    sh "docker push ${HARBOR_HOST}/${HARBOR_PROJECT}/${HARBOR_IMAGE}:${GIT_COMMIT}"
    sh "docker rmi ${HARBOR_HOST}/${HARBOR_PROJECT}/${HARBOR_IMAGE}:${GIT_COMMIT}"

    // TODO erro no fetch para atualizar imagem Docker, assim que parar o erro descomentar dockerfile e este script
    // script {
    //     sleep(time: 1, unit: 'MINUTES') 
    //     // Gate Harbor Scan
    //     HARBOR_SCAN_RETURN = sh (
    //         script: "curl --insecure https://${HARBOR_HOST}/api/repositories/${HARBOR_PROJECT}/${HARBOR_IMAGE}/tags/${GIT_COMMIT}/vulnerability/details -H 'Authorization: ${HARBOR_BASE64}'",
    //         returnStdout: true
    //     ).trim()

    //     //echo "HARBOR_SCAN_RETURN: ${HARBOR_SCAN_RETURN}"

    //     if(HARBOR_SCAN_RETURN != '[]') {
    //         currentBuild.result = 'FAILURE'
    //         error("[FAILURE] Docker image vulnerable - Harbor Scan ${HARBOR_SCAN_RETURN}")
    //     }
    // }
}
