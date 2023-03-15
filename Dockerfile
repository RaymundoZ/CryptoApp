FROM ubuntu:23.04

ARG JAVA_VERSION="17.0.5-librca"
ARG MAVEN_VERSION="3.9.0"
ARG USER_UID="10000"
ARG USER_GID="10000"
ARG USER_NAME="cryptoapp"

RUN groupadd -g $USER_GID $USER_NAME && \
useradd -m -g $USER_GID -u $USER_UID $USER_NAME

RUN apt-get update && apt-get upgrade -y && \
apt-get install curl unzip zip -y

USER $USER_UID:$USER_GID

RUN curl -s "https://get.sdkman.io" | bash

RUN bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && \
yes | sdk install java $JAVA_VERSION && \
yes | sdk install maven $MAVEN_VERSION"

RUN bash -c "mkdir $HOME/app && \
chown $USER_NAME:$USER_NAME $HOME/app && \
chmod 700 $HOME/app"

WORKDIR /home/cryptoapp/app
COPY . .
EXPOSE 5000
RUN bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && \
mvn package"
ENTRYPOINT bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && \
mvn spring-boot:run"