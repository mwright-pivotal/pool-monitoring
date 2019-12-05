FROM alpine:3.8

ENV DOCKER_CHANNEL=stable \
    DOCKER_VERSION=18.09.6 \
    DOCKER_COMPOSE_VERSION=1.19.0 \
    DOCKER_SQUASH=0.2.0

# Install Docker, Docker Compose, Docker Squash
RUN apk --update --no-cache add \
        bash \
        curl \
        device-mapper \
        py-pip \
        iptables \
        ca-certificates \
        perl \
        perl-utils \
        libstdc++ \
        npm \
        maven \
        && \
    apk upgrade && \
    curl -fL "https://download.docker.com/linux/static/${DOCKER_CHANNEL}/x86_64/docker-${DOCKER_VERSION}.tgz" | tar zx && \
    mv /docker/* /bin/ && chmod +x /bin/docker* && \
    pip install docker-compose==${DOCKER_COMPOSE_VERSION} && \
    curl -fL "https://github.com/jwilder/docker-squash/releases/download/v${DOCKER_SQUASH}/docker-squash-linux-amd64-v${DOCKER_SQUASH}.tar.gz" | tar zx && \
    mv /docker-squash* /bin/ && chmod +x /bin/docker-squash* && \
    rm -rf /var/cache/apk/* && \
    rm -rf /root/.cache

RUN curl -sSL https://sdk.cloud.google.com | bash

RUN wget -O- https://k14s.io/install.sh | bash

ENV PATH $PATH:/root/google-cloud-sdk/bin

ADD https://github.com/buildpack/pack/releases/download/v0.2.1/pack-v0.2.1-linux.tgz /tmp/
RUN tar -C /usr/local/bin -xvzf /tmp/pack-v0.2.1-linux.tgz

COPY pb-linux-0.0.3-rc.41 /usr/local/bin/pb
RUN chmod a+x /usr/local/bin/pb

RUN gcloud auth configure-docker

RUN npm install -g snyk
# RUN mkdir /etc/docker;mkdir /etc/docker/certs.d;mkdir /etc/docker/certs.d/harbor.haas-245.pez.pivotal.io
# COPY ca.crt /etc/docker/certs.d/harbor.haas-245.pez.pivotal.io/
