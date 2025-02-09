CREATE TABLE IF NOT EXISTS public.tasks_content
(
    username character varying(255) COLLATE pg_catalog."default" NOT NULL,
    contents character varying(400) COLLATE pg_catalog."default" NOT NULL,
    priority character varying(255) COLLATE pg_catalog."default" NOT NULL,
    dedlines character varying(255) COLLATE pg_catalog."default",
    status character varying(255) COLLATE pg_catalog."default" NOT NULL,
    id integer NOT NULL DEFAULT nextval('tasks_conten_id_seq'::regclass),
    CONSTRAINT tasks_content_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.tasks_content
    OWNER to postgres;