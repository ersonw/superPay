
#基础镜像
FROM tomcat:9.0.64-jdk8
#作者信息
MAINTAINER ersonw asoonnode@gmail.com
#定义变量、后续会使用，具体路径可以先启动容器然后进入进行查看
ENV DIR_WEBAPP /usr/local/tomcat/webapps/
#删除webapp下所有文件，因为当前应用作为根应用
RUN  rm -rf $DIR_WEBAPP/*
#添加本地的war包到远程容器中
ADD ./target/superPay-0.0.1.war $DIR_WEBAPP/ROOT.war
#配置文件夹映射
VOLUME /usr/local/tomcat/webapps
#配置工作目录
WORKDIR /usr/local/tomcat/webapps
#解压war包到ROOT目录
#RUN unzip $DIR_WEBAPP/ROOT.war -d $DIR_WEBAPP/ROOT/
#暴露端口
EXPOSE 8080
#启动tomcat
CMD ["catalina.sh", "run"]


#基础镜像 打包镜像
#FROM centos:centos7.4.1708
#
#MAINTAINER erosnw  "asoonnode@gmail.com"
#
#RUN mkdir -p /usr/local/java/corretto-1.8.0_332
#
#RUN mkdir -p /usr/local/apache-tomcat-9.0.64
#
#RUN yum install -y net-tools.x86_64 wget vim openssl-dev*
## name jdk8  本地的jdk目录name
#ADD corretto-1.8.0_332 /usr/local/java/corretto-1.8.0_332
#
## name tomcat 本地的tomcat目录name
#ADD apache-tomcat-9.0.64 /usr/local/apache-tomcat-9.0.64
#
#ENV JAVA_HOME /usr/local/java/corretto-1.8.0_332
#
#ENV CATALINA_HOME /usr/local/apache-tomcat-9.0.64
#
#ENV PATH $PATH:$JAVA_HOME/bin:$CATALINA_HOME/bin
#
#EXPOSE 8080

#CMD ["/usr/local/apache-tomcat-9.0.64/bin/catalina.sh","run"]

#FROM ersonw/tomcat9:latest
##作者信息
#MAINTAINER ersonw asoonnode@gmail.com
##定义变量、后续会使用，具体路径可以先启动容器然后进入进行查看
#ENV DIR_WEBAPP /usr/local/apache-tomcat-9.0.64/webapps/
##删除webapp下所有文件，因为当前应用作为根应用
#RUN  rm -rf $DIR_WEBAPP/*
##添加本地的war包到远程容器中
#ADD ./target/superPay-0.0.1.war $DIR_WEBAPP/ROOT.war
##配置文件夹映射
#VOLUME /usr/local/apache-tomcat-9.0.64/webapps
##配置工作目录
#WORKDIR /usr/local/apache-tomcat-9.0.64/webapps
##解压war包到ROOT目录
##RUN unzip $DIR_WEBAPP/ROOT.war -d $DIR_WEBAPP/ROOT/
##暴露端口
#EXPOSE 8080
##启动tomcat
#CMD ["catalina.sh", "run"]
