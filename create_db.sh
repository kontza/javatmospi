#!/bin/sh
createdb atmospi
psql -d atmospi < create_user.sql
gunzip log.sqlite.gz
pgloader log.sqlite postgresql:///atmospi
