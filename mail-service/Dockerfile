FROM maven:3.8-openjdk-17-slim

ENV HOME=/home/app
RUN mkdir -p $HOME

ADD ./settings.xml /usr/share/maven/ref/
ENV MVN_SETTINGS=/usr/share/maven/ref/settings.xml

RUN mkdir -p $HOME/common
ADD ./common $HOME/common
RUN mvn -f $HOME/common/pom.xml -s $MVN_SETTINGS clean package
RUN mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=$HOME/common/target/common-0.0.1.jar --debug

RUN mkdir -p $HOME/mail-service
ADD ./mail-service/pom.xml $HOME/mail-service
ADD ./mail-service/src $HOME/mail-service/src
RUN mvn -f $HOME/mail-service/pom.xml -s $MVN_SETTINGS clean package --debug

CMD java -jar $HOME/mail-service/target/mail-service-0.0.1.jar