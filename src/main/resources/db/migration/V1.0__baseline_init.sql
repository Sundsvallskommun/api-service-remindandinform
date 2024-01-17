    create table reminder (
        reminder_date date,
        sent bit,
        created datetime(6),
        id bigint not null auto_increment,
        modified datetime(6),
        case_link varchar(512),
        action varchar(8192) not null,
        case_id varchar(255),
        case_type varchar(255),
        created_by varchar(255),
        external_case_id varchar(255),
        modified_by varchar(255),
        party_id varchar(255),
        reminder_id varchar(255),
        note longtext,
        primary key (id)
    ) engine=InnoDB;

    create table schema_history (
        applied datetime(6) not null,
        comment varchar(8192) not null,
        schema_version varchar(255) not null,
        primary key (schema_version)
    ) engine=InnoDB;

    create index reminder_id_index
       on reminder (reminder_id);

    create index party_id_index
       on reminder (party_id);

    alter table if exists reminder
       add constraint uq_reminder_id unique (reminder_id);