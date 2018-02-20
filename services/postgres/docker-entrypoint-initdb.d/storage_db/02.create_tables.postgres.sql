-- Create tables

create table "REPOSITORIES" (
    "UUID" UUID default uuid_generate_v4() not null,
    "IID" TEXT,
    "PATH" TEXT not null,
    "DESCRIPTION" TEXT not null,
    "BACKEND" TEXT not null,
    "CREATED" BIGINT,
    primary key ("UUID")
);

create unique index "IDX_IID_BACKEND_REPOSITORIES"
on "REPOSITORIES" ("IID", "BACKEND");

-- Grant access to user "graph-wal"

grant all privileges on table "REPOSITORIES" to "storage";

