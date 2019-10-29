# Pipeline DevSecOps

Implementando as ferramentas do pipeline através do Docker. 



## Jenkins

Utilize este [Dockerfile](http://NET002PRDLNX395.dcing.corp/claro-poc/pipeline-devsecops/blob/master/Dockerfile) para incluir o kubectl na imagem do Jenkins, essencial para manipular os cluster do Kubernetes nos diferentes ambientes (QA, HOM e PROD) na entrega contínua.

Criando o container:

```bash
sudo docker run \
  -u root \
  -p 8080:8080 \
  -p 50000:50000 \
  --privileged \
  --restart always --detach \
  -v jenkins-data:/var/jenkins_home \
  -v /var/run/docker.sock:/var/run/docker.sock \
  -v "$HOME":/home \
  --name jenkins \
  jenkins # imagem gerada a partir do Dockerfile
```

Obs: liberar a porta 8080 na regra de firewall.



## GitLab

Criando o container:

```bash
sudo docker run --detach \
  --publish 443:443 --publish 80:80 \
  --name gitlab \
  --restart always \
  --volume /srv/gitlab/config:/etc/gitlab \
  --volume /srv/gitlab/logs:/var/log/gitlab \
  --volume /srv/gitlab/data:/var/opt/gitlab \
  gitlab/gitlab-ce:latest
```

Obs: liberar as portas 80 e 443 na regra de firewall.

Referência: [GitLab Docs](https://docs.gitlab.com/omnibus/docker/README.html)



## SonarQube

Criando o container:

```bash
sudo docker run -d \
  --restart always \
  --name sonarqube \
  -p 9000:9000 
  sonarqube
```

Incluindo o Sonar Scanner ao container:

```bash
sudo docker cp sonar-scanner-cli-3.3.0.1492-linux.zip sonarqube:/opt/sonarqube
```

Obs: liberar a porta 9000 na regra de firewall.

Donwload do [Sonar Scanner](https://github.com/SonarSource/sonar-scanner-cli/releases)



## FindSecBugs

O FindSecBugs é a solução de SAST (static application security testing) para Java utilizada no pipeline. 

A ferramenta foi instalada como um plugin no SonarQube e referenciada como Quality Profile ao projeto.

[SonarQube] Adminstrator > Marketplace > FindBugs



## Nexus Sonatype

Criando o container:

```bash
sudo docker run \
  -p 8081:8081 \
  --restart always --detach \
  -v nexus-data:/nexus-data \
  --name nexus \
  sonatype/nexus3
```

Obs: liberar a porta 8081 na regra de firewall.



## Maven

O Maven está sendo instalado automaticamente através do Jenkins, conforme configurado em: 
- Manage Jenkins > Global Tool Configuration > Maven
  - Informar nome a ser referenciado no Jenkinsfile
  - Marcar opção Install automatically
  - Selecionar a versão desejada

Incluir no Jenkinsfile:

```bash
 agent {
     docker {
         // Use Maven with Docker
         image 'maven:3-alpine' 
         args '-v /root/.m2:/root/.m2'
     }
 }
```


## OWASP Dependency Check

Conforme exemplo utilizado (case java), adicione o seguinte plugin ao pom.xml:

```bash
<plugins>
    ...
    <plugin>
        <groupId>org.owasp</groupId>
        <artifactId>dependency-check-maven</artifactId>
        <executions>
            <execution>
                <goals>
                    <goal>check</goal>
                </goals>
            </execution>
        </executions>
    </plugin>
    ...
</plugins>
```

Referência: [POM.xml de exemplo](http://NET002PRDLNX395.dcing.corp/claro-poc/pipeline-devsecops/blob/master/pom.xml)



## Harbor

Para a instalação do Harbor (nosso repositório de images Docker) siga o passo a passo da documentação oficial.

Referência: [Documentação Oficial do Harbor](https://github.com/goharbor/harbor/blob/master/docs/installation_guide.md)


Para habilitar a verificação se segurança no Harbor, siga os passos abaixo.

```
docker-compose down -v
./prepare --with-clair
sudo docker-compose up -d
```
Enable Clair Security: [Referência GitHub](https://github.com/goharbor/harbor/issues/5463)


## Kubernetes

Para a instalação do Kubernetes (no nosso exemplo, utilizado nos ambientes de QA, HOM e PROD), siga o passo a passo da documentação oficial do Kubernetes.

Referência: [Documentação KOPS - Kubernetes](https://github.com/kubernetes/kops)



# Integration Gitlab Repo with Jenkins job
#### Essential for automatic trigger by commit or merge requests

#### GITLAB

User > Settings > Access Token

- Name: Jenkins-Access-Token
- Scope: API

----

#### JENKINS

Manage Jenkins > Credentials > System > Global credentials > Add Credentials

- Kind: Gitlab Personal Access Token
- Scope: Global
- Token: (colar secret gerada no Gitlab - Jenkins-Access-Token)

----

Job > Master (secundário) > View Configuration > Copiar endereço em "build when a change is pushed"

----

#### GITLAB

Repositório > Settings > Integrations 

- URL: (copiada no job)
- Secret Token: (colar secret gerada no próprio Gitlab - Jenkins-Access-Token)
- Trigger: Push Events
- Enable SSL: None


