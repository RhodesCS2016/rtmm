
FROM ubuntu:14.04.4

LABEL app.name="kamailio"

# correct apt repo
RUN echo deb     http://deb.kamailio.org/kamailio44 trusty main >> /etc/apt/sources.list
RUN echo deb-src http://deb.kamailio.org/kamailio44 trusty main >> /etc/apt/sources.list

# get gpg key
RUN apt-key adv --recv-keys --keyserver keyserver.ubuntu.com 0xfb40d3e6508ea4c8

# install kamailio and supporting software
RUN apt-get update -qq && apt-get install -qqy kamailio kamailio-mysql-modules vim

COPY etc/kamailio/kamailio.cfg /etc/kamailio/
COPY etc/kamailio/kamctlrc /etc/kamailio/
COPY etc/default/kamailio /etc/default/

EXPOSE 5060 5060/udp 5061
EXPOSE 7000 7000/udp 7001
