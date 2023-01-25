
    create table reminder (
       id bigint not null auto_increment,
        action varchar(8192) not null,
        case_id varchar(255),
        case_link varchar(512),
        case_type varchar(255),
        created datetime(6),
        created_by varchar(255),
        external_case_id varchar(255),
        modified datetime(6),
        modified_by varchar(255),
        note longtext,
        party_id varchar(255),
        reminder_date date,
        reminder_id varchar(255),
        sent bit,
        primary key (id)
    ) engine=InnoDB;

    create table schema_history (
       schema_version varchar(255) not null,
        applied datetime(6) not null,
        comment varchar(8192) not null,
        primary key (schema_version)
    ) engine=InnoDB;
create index reminder_id_index on reminder (reminder_id);
create index party_id_index on reminder (party_id);

    alter table reminder 
       add constraint UK_sef5hopmuh38iabf4vhl9bajj unique (reminder_id);
