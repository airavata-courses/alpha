#!/bin/bash

chmod 0700 /var/lib/postgresql/data
initdb /var/lib/postgresql/data
chmod 0700 /var/lib/postgresql/data
echo "host all  all    0.0.0.0/0  md5" >> /var/lib/postgresql/data/pg_hba.conf
echo "host all all ::/0 md5" >> /var/lib/postgresql/data/pg_hba.conf
echo "listen_addresses='*'" >> /var/lib/postgresql/data/postgresql.conf
pg_ctl start
psql < /app/dbscripts.sql
tail -f /dev/null

