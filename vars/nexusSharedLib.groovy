def call(){

    // ----------------------------------------
    // ENVIRONMENT
    NEXUS_VERSION = 'nexus3'
    NEXUS_PROTOCOL = 'http'
    NEXUS_HOST = 'nexus.dev.k8s.claro.com.br' //'172.31.38.103:8081'
    NEXUS_CREDENTIAL_ID = 'nexus' //'a1bea961-c634-4aad-8642-60b29d1b4521'
    // ----------------------------------------

    // Read POM xml file using 'readMavenPom' step , this step 'readMavenPom' is included in: https://plugins.jenkins.io/pipeline-utility-steps
    //pom = readMavenPom file: "pom.xml";
    pom = readMavenPom file: "${PROJECT_NAME}/pom.xml";
    filesByGlob = findFiles(glob: "${NEXUS_PROJECT_SOURCE}*.${pom.packaging}");

    // Plugin: Nexus Artifact Uploader
    nexusArtifactUploader(
        nexusVersion: NEXUS_VERSION,
        protocol: NEXUS_PROTOCOL,
        nexusUrl: NEXUS_HOST,
        groupId: NEXUS_REPOSITORY,
        version: GIT_COMMIT ,
        repository: NEXUS_REPOSITORY,
        credentialsId: NEXUS_CREDENTIAL_ID,
        artifacts: [
            [artifactId: NEXUS_REPOSITORY,
            classifier: '',
            file: NEXUS_FILE,
            type: pom.packaging]
        ]
    )
}
