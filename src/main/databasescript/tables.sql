--DROP TABLE player;
--DROP TABLE fight_result;

CREATE TABLE player (
    id serial PRIMARY KEY,
    player_name VARCHAR(200)
);

CREATE TABLE fight_result (
    id serial PRIMARY KEY,
    one integer, 
    two integer,
    result_one integer,
    result_two integer
);