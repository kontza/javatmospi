#!/bin/sh
createdb atmospi
gunzip log.sqlite.gz
pgloader log.sqlite postgresql:///atmospi
gzip log.sqlite
psql -d atmospi < create_temperature_id.sql
psql -d atmospi < create_user.sql
