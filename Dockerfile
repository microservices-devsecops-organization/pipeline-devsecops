FROM jenkinsci/blueocean:latest
ENV HOME /var/jenkins_home
WORKDIR ${HOME}
USER root
RUN curl -LO https://storage.googleapis.com/kubernetes-release/release/$(curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt)/bin/linux/amd64/kubectl && \
    chmod +x ./kubectl && \
    mv ./kubectl /usr/local/bin/kubectl && \
    chown :jenkins /usr/local/bin/kubectl