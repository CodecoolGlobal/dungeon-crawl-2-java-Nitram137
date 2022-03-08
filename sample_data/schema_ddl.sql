DROP TABLE IF EXISTS public.game_state;
CREATE TABLE public.game_state (
    id serial NOT NULL PRIMARY KEY,
    current_map text NOT NULL,
    saved_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    player_id integer NOT NULL
);

DROP TABLE IF EXISTS public.player;
CREATE TABLE public.player (
    id serial NOT NULL PRIMARY KEY,
    player_name text NOT NULL,
    hp integer NOT NULL,
    x integer NOT NULL,
    y integer NOT NULL
);

DROP TABLE IF EXISTS public.inventory;
CREATE TABLE public.inventory (
    id serial NOT NULL PRIMARY KEY,
    game_state_id integer NOT NULL,
    iron_key integer,
    potion integer,
    big_potion integer,
    Thunderfury boolean,
    Mjolnir boolean,
    The_Grim_Reaper boolean,
    Stormbreaker boolean,
    Frostmourne boolean,
    Stick_of_Truth boolean
);

ALTER TABLE ONLY public.game_state
    ADD CONSTRAINT fk_player_id FOREIGN KEY (player_id) REFERENCES public.player(id);
ALTER TABLE ONLY public.inventory
    ADD CONSTRAINT fk_game_state_id FOREIGN KEY (game_state_id) REFERENCES public.game_state(id);