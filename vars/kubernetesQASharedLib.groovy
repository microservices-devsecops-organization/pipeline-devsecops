def call(){

    // ----------------------------------------
    // ENVIRONMENT
    KUBERNETES_CONTEXT_QA = 'vsphere-development-claro' //'qa.k8s.local'
    KUBERNETES_NAMESPACE = 'poc-arqtec' //'stage'
    HARBOR_HOST = 'harbor.claro.com.br' //'52.14.127.219'
    // ----------------------------------------

    script {

        def str = env.BRANCH_NAME
        def STR_BRANCH_NAME = str.reverse().take(7).reverse()
        
        echo "BRANCH: $str | STR_BRANCH_NAME: $STR_BRANCH_NAME"

        // Expected receive 'release' in the end of the name for RELEASE BRANCHS
        if (STR_BRANCH_NAME == 'release' || env.BRANCH_NAME == 'master') {
            // // Deploy K8s (cluster QA)
            // sh "kubectl config use-context ${KUBERNETES_CONTEXT_QA}" //TODO Dhiego verificar se vai precisar com o Filipe
            // sh "kubectl config set-context ${KUBERNETES_CONTEXT_QA} --namespace=${KUBERNETES_NAMESPACE}" //TODO Dhiego verificar se vai precisar com o Filipe
            // sh "kubectl delete -f ./${PROJECT_NAME}/k8s"
            sh "kubectl apply -f ./${PROJECT_NAME}/k8s"
            sh "kubectl --record deployment/${PROJECT_NAME}-deployment set image deployment/${PROJECT_NAME}-deployment ${PROJECT_NAME}-container=${HARBOR_HOST}/${HARBOR_PROJECT}/${HARBOR_IMAGE}:${GIT_COMMIT}"

        } else if (env.BRANCH_NAME == 'develop') {
            echo "Skiped - Delivery only to DEV"
        } else {
            echo "NONE"
        }
    }
}
