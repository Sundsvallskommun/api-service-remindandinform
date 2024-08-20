alter table reminder
    add column municipality_id varchar(255);

create index municipality_id_index
    on reminder (municipality_id);
