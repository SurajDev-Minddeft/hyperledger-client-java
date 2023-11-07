FROM openjdk:11.0-jre

RUN apk -v --update --no-cache add \
      curl \
      jq \
      python \
      py-pip \
      groff \
      less \
      mailcap \
      && \
    pip install --upgrade awscli==1.15.71 python-magic && \
    apk -v --purge del py-pip

COPY deployments/scripts/docker-entrypoint.sh /usr/local/bin/
RUN ln -s /usr/local/bin/docker-entrypoint.sh /

ARG PROFILE=docker
ARG ENVIRONMENT=dev
ARG SERVICE=utavi-ledger-client
ARG VERSION=0.0.1-SNAPSHOT

ENV PROFILE $PROFILE
ENV ENVIRONMENT $ENVIRONMENT
ENV JVM_OPT "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5009"
ENV JMX_OPT ""
ENV SERVICE $SERVICE
ENV VERSION $VERSION
ENV AWS_DEFAULT_REGION us-east-1

WORKDIR /opt/${SERVICE}

COPY ./target/${SERVICE}*.jar /opt/${SERVICE}/${SERVICE}-${VERSION}.jar

ENTRYPOINT ["/docker-entrypoint.sh"]

CMD java \
  ${JVM_OPT} \
  ${JMX_OPT} \
  -Dspring.profiles.active=${PROFILE} \
  -jar /opt/${SERVICE}/${SERVICE}-${VERSION}.jar
