ALTER TABLE temperature ADD COLUMN id SERIAL PRIMARY KEY;
alter table devices add primary key (deviceid);
create sequence devices_deviceid_seq;
ALTER TABLE devices ALTER COLUMN deviceid SET DEFAULT nextval('devices_deviceid_seq');
ALTER TABLE devices ALTER COLUMN deviceid SET NOT NULL;
alter table humidity add column id serial primary key;
alter table flag add column id serial primary key;
