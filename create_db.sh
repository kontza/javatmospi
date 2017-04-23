#!/bin/sh
echo "### Create the database        ###"
sudo -u postgres createdb atmospi
echo "### Extract the SQLite data    ###"
zcat log.sqlite.gz > /tmp/log.sqlite
echo "### Import the SQLite data     ###"
sudo -u postgres pgloader /tmp/log.sqlite postgresql:///atmospi
echo "### Run the post-import tweaks ###"
sudo -u postgres psql -d atmospi < post_import_tweaks.sql
