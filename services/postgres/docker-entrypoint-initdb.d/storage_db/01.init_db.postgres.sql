-- Create extension for uuid auto-gen support

create extension "uuid-ossp";

revoke all on schema "public" from "public";

-- Create user

create user "storage" password 'storage';
grant connect on database "storage" to "storage";
grant usage on schema "public" to "storage";
