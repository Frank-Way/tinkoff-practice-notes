-- SCHEMA: public

-- DROP SCHEMA public ;

CREATE SCHEMA IF NOT EXISTS public;


-- SEQUENCE: public.users_id_seq

-- DROP SEQUENCE public.users_id_seq;

CREATE SEQUENCE IF NOT EXISTS public.users_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;


-- Table: public.users

-- DROP TABLE public.users;

CREATE TABLE IF NOT EXISTS public.users
(
    id bigint NOT NULL DEFAULT nextval('users_id_seq'::regclass),
    login character varying(16) COLLATE pg_catalog."default" NOT NULL,
    password character varying(32) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;



-- SEQUENCE: public.notes_id_seq

-- DROP SEQUENCE public.notes_id_seq;

CREATE SEQUENCE IF NOT EXISTS public.notes_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;




-- Table: public.notes

-- DROP TABLE public.notes;

CREATE TABLE IF NOT EXISTS public.notes
(
    id bigint NOT NULL DEFAULT nextval('notes_id_seq'::regclass),
    name character varying(64) COLLATE pg_catalog."default",
    body character varying(1024) COLLATE pg_catalog."default" NOT NULL,
    created date NOT NULL,
    updated date NOT NULL,
    user_id bigint NOT NULL,
    status boolean NOT NULL,
    CONSTRAINT notes_pkey PRIMARY KEY (id),
    CONSTRAINT notes_users_pkey FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
        NOT VALID
)

TABLESPACE pg_default;


-- Index: fki_user_id

-- DROP INDEX public.fki_user_id;

CREATE INDEX IF NOT EXISTS fki_user_id
    ON public.notes USING btree
    (user_id ASC NULLS LAST)
    TABLESPACE pg_default;