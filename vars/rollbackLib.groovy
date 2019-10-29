
def call() {

    script {

        def userInput = true
        def didTimeout = false
        def var_POST_PROD_TIME = POST_PROD_TIME  
        def var_POST_PROD_UNIT = POST_PROD_UNIT

        try {
            timeout(time: var_POST_PROD_TIME, unit: var_POST_PROD_UNIT) {
                input(
                    message: 'Rollback to the previous deploy?'
                )
            }

            echo "Rollback applied! This deploy was NOT running successfully!"

            // Rollback QA
            // sh "kubectl config use-context ${KUBERNETES_CONTEXT_QA}" //TODO Dhiego verificar se vai precisar com o Filipe
            // sh "kubectl config set-context ${KUBERNETES_CONTEXT_QA} --namespace=${KUBERNETES_NAMESPACE}" //TODO Dhiego verificar se vai precisar com o Filipe
            sh "kubectl rollout undo deployment/${PROJECT_NAME}-deployment"

            // Rollback PROD
            // sh "kubectl config use-context ${KUBERNETES_CONTEXT_PROD}" //TODO Dhiego verificar se vai precisar com o Filipe
            // sh "kubectl config set-context ${KUBERNETES_CONTEXT_QA} --namespace=${KUBERNETES_NAMESPACE}" //TODO Dhiego verificar se vai precisar com o Filipe
            sh "kubectl rollout undo deployment/${PROJECT_NAME}-deployment"

            currentBuild.result = 'FAILURE'

        } catch(org.jenkinsci.plugins.workflow.steps.FlowInterruptedException ex) { // timeout reached or input false
            echo "Rollback NOT applied! This deploy was running successfully!"
            userInput = false
            currentBuild.result = 'SUCCESS'
            return

            // def user = env.BUILD_USER_ID
            // echo env.BUILD_USER
            // echo env.BUILD_USER_FIRST_NAME
            // echo env.BUILD_USER_LAST_NAME
            // echo env.BUILD_USER_ID
            // echo env.BUILD_USER_EMAIL
            // echo '*********************************'

            // if('SYSTEM' == user.toString()) { // SYSTEM means timeout.
            // echo '***************************************** 3 ex'
            //     didTimeout = true

            //     echo "ROLLBACK NOT APPLIED!"

            // } else {
            //     echo '***************************************** 4 ex'
            //     userInput = false
            //     echo "Stage aborted by: [${user}] - ROLLBACK NOT APPLIED!"
            // }
            
            

            // println(ex)
            // println(ex.getMessage()) //return null
            // println(ex.getCause()) //return null
            // println(ex.printStackTrace())
            // StringBuilder sb = new StringBuilder();
            // for (StackTraceElement element : ex.getStackTrace()) {
            //     sb.append(element.toString());
            //     sb.append("\n");
            // }
            // println(sb.toString())
            // def user = ex.getCause()[0].getUser() //getCause return null, não da para fazer esse tratamento de pegar o usuário
            // echo '***************************************** 2 ex'
            // if('SYSTEM' == user.toString()) { // SYSTEM means timeout.
            // echo '***************************************** 3 ex'
            //     didTimeout = true

            //     echo "ROLLBACK NOT APPLIED!"

            // } else {
            //     echo '***************************************** 4 ex'
            //     userInput = false
            //     echo "Stage aborted by: [${user}] - ROLLBACK NOT APPLIED!"
            // }
        } 

    }

}

