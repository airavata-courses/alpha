FROM postgres:9.5

EXPOSE 5432

COPY . /app

WORKDIR /app
ENV DB_USER='demo'
ENV DB_PASSWORD='p@ssw0rd'
RUN chmod 777 /app/entrypoint.sh
RUN echo "postgres ALL=(ALL) NOPASSWD: ALL" >> /etc/sudoers
RUN echo 'hosts: files mdns4_minimal [NOTFOUND=return] dns mdns4' >> /etc/nsswitch.conf
RUN chmod 777 /app  && sed -i 's/DB_USER/'"$DB_USER"'/' /app/dbscripts.sql && sed -i 's/DB_PASSWORD/'"$DB_PASSWORD"'/' /app/dbscripts.sql
USER postgres
#RUN chmod 0700 /var/lib/postgresql/data &&\
#    initdb /var/lib/postgresql/data &&    echo "host all  all    0.0.0.0/0  md5" >> /var/lib/postgresql/data/pg_hba.conf &&\
#    echo "host all all ::/0 md5" >> /var/lib/postgresql/data/pg_hba.conf &&\
#    echo "listen_addresses='*'" >> /var/lib/postgresql/data/postgresql.conf && pg_ctl start && psql < /app/dbscripts.sql

ENTRYPOINT ["./entrypoint.sh"]

