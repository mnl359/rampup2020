FROM postgres:12.2-alpine

MAINTAINER Manuela Carrasco
COPY ./mail2clients/db_initial_script.sql /docker-entrypoint-initdb.d/1-db_initial_script.sql