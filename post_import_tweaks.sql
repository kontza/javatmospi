-- Tweak tables after importing them from sqlite.
ALTER TABLE temperature ADD COLUMN id SERIAL PRIMARY KEY;
alter table devices add primary key (deviceid);
create sequence devices_deviceid_seq;
ALTER TABLE devices ALTER COLUMN deviceid SET DEFAULT nextval('devices_deviceid_seq');
ALTER TABLE devices ALTER COLUMN deviceid SET NOT NULL;
alter table humidity add column id serial primary key;
alter table flag add column id serial primary key;

-- Convert integer-columns into bigints.
alter table devices alter deviceid type bigint;
alter table flag alter deviceid type bigint;
alter table flag alter 'timestamp' type bigint;
alter table humidity alter deviceid type bigint;
alter table humidity alter 'timestamp' type bigint;
alter table temperature alter deviceid type bigint;
alter table temperature alter 'timestamp' type bigint;

-- Resync sequences.
select setval('devices_deviceid_seq', (select max(deviceid) from devices));
select setval('temperature_id_seq', (select max(deviceid) from temperature));
select setval('flag_id_seq', (select max(deviceid) from flag));
select setval('humidity_id_seq', (select max(deviceid) from humidity));

-- Add user and grant privileges.
create user atmospi with password 'atmospi';
grant all privileges on all tables in schema public to atmospi;
grant all privileges on all sequences in schema public to atmospi;
