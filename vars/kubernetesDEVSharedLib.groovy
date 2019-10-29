def call(){

    // ----------------------------------------
    // ENVIRONMENT
    KUBERNETES_CONTEXT_DEV = 'vsphere-development-claro' //'qa.k8s.local'
    KUBERNETES_NAMESPACE = 'poc-arqtec' //'development'
    HARBOR_HOST = 'harbor.claro.com.br' //'52.14.127.219'
    // ----------------------------------------

    script {
        if (env.BRANCH_NAME == 'develop') {
            // Deploy K8s (cluster DEV)
            // sh "kubectl config use-context ${KUBERNETES_CONTEXT_DEV}" //TODO Dhiego verificar se vai precisar com o Filipe
            // sh "kubectl config set-context ${KUBERNETES_CONTEXT_QA} --namespace=${KUBERNETES_NAMESPACE}" //TODO Dhiego verificar se vai precisar com o Filipe
            // sh "kubectl delete -f ./${PROJECT_NAME}/k8s"
            sh "kubectl apply -f ./${PROJECT_NAME}/k8s"
            sh "kubectl set image deployment/${PROJECT_NAME}-deployment ${PROJECT_NAME}-container=${HARBOR_HOST}/${HARBOR_PROJECT}/${HARBOR_IMAGE}:${GIT_COMMIT}"

        } else {
            echo "Skiped"
        }
    }

}