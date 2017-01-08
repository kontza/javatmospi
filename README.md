# Introduction
This is a quick'n'dirty Spring Boot port of [Goatmospi](https://github.com/kontza/goatmospi).

# How to build

## Preparations & Requirements
The assumption is that you're trying this out on a Linux box, or a Mac. Here's a list of command line apps that you would need to be able to run:

- `createdb` and `psql`, usually from some PostgreSQL-package.
- `gunzip` & `gzip`
- pgloader, Mac:`brew install pgloader`, Apt-based Linux:`apt install pgloader`.
- `mvn`, Mac:`brew install maven`, Apt-based Linux:`apt install maven`.

These are mainly used for setting up the database, the database user, and the user's privileges.

### Setting Things Up
There's a simple shell-script to help out in setting things up. Here's how to run it:

    $ source create_db.sh

Here are the steps it does:
1. It creates the database for the app to use.
2. It creates the user.
3. It gunzips the included SQLite-dump, loads the data into the database, then re-gzips the SQLite-dump.
4. It post-processes the temperature-table in the database to add the id-columnd to the table.


## Building
Simply run the following command:

    $ mvn package

# Run
Run the generated JAR:

    $ java -jar ./target/atmospi-1.0.0-SNAPSHOT.jar
    
Next, open the following URL in your browser: [http://localhost:8080](http://localhost:8080)

# Miscellaneous
The challenge with this app was to keep it compatible with the data that the original [atmospi](https://github.com/mstenta/atmospi) generates. Especially timestamps were problematic, since they are Unixy seconds-based timestamps in the database, and the front-end JavaScript (and HighCharts) live on millisecond-based timestamps.
