
FROM ubuntu:14.04.4

LABEL app.name="kamailio"

# install kamailio and supporting software
RUN apt-get update -qq && apt-get install -qqy kamailio kamailio-mysql-modules vim

VOLUME /etc/kamailio
RUN chown kamailio:kamailio /etc/kamailio/*

COPY kamailio /etc/default/

EXPOSE 5060 5060/udp 5061
EXPOSE 7000 7000/udp 7001
