CREATE DATABASE testdb;

USE testdb;

CREATE TABLE public.job(
        id SERIAL NOT NULL,
        companyname character varying(255) NOT NULL
    )

CREATE SCHEMA usernameone
    CREATE TABLE job(
        id SERIAL NOT NULL,
        companyname character varying(255) NOT NULL
    );

CREATE SCHEMA usernametwo
    CREATE TABLE job(
        id SERIAL NOT NULL,
        companyname character varying(255) NOT NULL
    );

GRANT CONNECT ON DATABASE testdb TO admin;
GRANT USAGE ON SCHEMA public TO admin;
GRANT USAGE ON SCHEMA usernameone TO admin;
GRANT USAGE ON SCHEMA usernametwo TO admin;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO admin;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA usernameone TO admin;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA usernametwo TO admin;

CREATE DATABASE testdb2;

USE testdb2;



CREATE TABLE public.job(
        id SERIAL NOT NULL,
        companyname character varying(255) NOT NULL
    )

CREATE SCHEMA usernameone
    CREATE TABLE job(
        id SERIAL NOT NULL,
        companyname character varying(255) NOT NULL
    );

CREATE SCHEMA usernametwo
    CREATE TABLE job(
        id SERIAL NOT NULL,
        companyname character varying(255) NOT NULL
    );

GRANT CONNECT ON DATABASE testdb2 TO admin;
GRANT USAGE ON SCHEMA public TO admin;
GRANT USAGE ON SCHEMA usernameone TO admin;
GRANT USAGE ON SCHEMA usernametwo TO admin;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO admin;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA usernameone TO admin;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA usernametwo TO admin;

CREATE DATABASE testdb3;

USE testdb3;

CREATE TABLE public.job(
        id SERIAL NOT NULL,
        companyname character varying(255) NOT NULL
    )

CREATE SCHEMA usernameone
    CREATE TABLE job(
        id SERIAL NOT NULL,
        companyname character varying(255) NOT NULL
    );

CREATE SCHEMA usernametwo
    CREATE TABLE job(
        id SERIAL NOT NULL,
        companyname character varying(255) NOT NULL
    );

GRANT CONNECT ON DATABASE testdb3 TO admin;
GRANT USAGE ON SCHEMA public TO admin;
GRANT USAGE ON SCHEMA usernameone TO admin;
GRANT USAGE ON SCHEMA usernametwo TO admin;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO admin;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA usernameone TO admin;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA usernametwo TO admin;


