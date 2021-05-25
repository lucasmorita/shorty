FROM jasonrivers/nagios

COPY ./shorty_nagios.cfg /opt/nagios/etc/conf.d/shorty_nagios.cfg

RUN htpasswd -bc /opt/nagios/etc/htpasswd.users nagiosadmin secret