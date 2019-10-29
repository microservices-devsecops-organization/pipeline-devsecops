def call(){

    echo "Run unit tests with Surefire + JUnit"
    echo "Run integration tests with Failsafe" 
    echo "Run generate coverage code reports with JaCoCo"
    echo "Run OWASP Dependency Check"

    // Compile, package and run all tests
    //sh "mvn verify"
    sh "cd ${PROJECT_NAME} && mvn clean"
    sh "cd ${PROJECT_NAME} && mvn verify -U"
}
