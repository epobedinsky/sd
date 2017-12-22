CREATE DATABASE SECURE_DIARY;
USE secure_diary;

--Users

-- DROP TABLE "us$user";
CREATE TABLE us$user
(
  id bigint NOT NULL auto_increment,
  PRIMARY KEY (id),
  login character varying(150),
  password character varying(150)
);

--DROP TABLE us$useratts;
CREATE TABLE us$useratts
(
  id bigint NOT NULL auto_increment,
  primary key(id),
  fname character varying(300),
  lastname character varying(300),
  surname character varying(300),
  nickname character varying(300),
  CONSTRAINT fk_useratts_user FOREIGN KEY (id)
      REFERENCES us$user(id)
      ON UPDATE NO ACTION ON DELETE CASCADE
);

-- Records

--DROP TABLE rec$diary;
CREATE TABLE rec$diary
(
  id bigint NOT NULL auto_increment,
  primary key(id),
  diary_name character varying(300),
  createdate timestamp,
  creator_id bigint,
  CONSTRAINT fk_diary_user FOREIGN KEY (creator_id)
      REFERENCES us$user (id)
      ON UPDATE NO ACTION ON DELETE SET NULL
);

INSERT INTO rec$diary
(id,
diary_name)
VALUES
(0,
'default');

-- DROP TABLE rec$record
CREATE TABLE rec$record
(
  id bigint NOT NULL auto_increment,
  primary key(id),
  diary_id bigint,
  creator_id bigint,
  create_date timestamp,
  title character varying(255),
  scrambler character varying(255),
  CONSTRAINT fk_record_diary FOREIGN KEY (diary_id)
      REFERENCES rec$diary (id)
      ON UPDATE NO ACTION ON DELETE CASCADE,
  CONSTRAINT fk_record_user FOREIGN KEY (creator_id)
      REFERENCES us$user (id)
      ON UPDATE NO ACTION ON DELETE SET NULL
);


-- DROP TABLE rec$recordpage
CREATE TABLE rec$recordpage
(
  id bigint NOT NULL auto_increment,
  primary key(id),
  record_id bigint,
  order_num integer,
  page_content character varying(10000),
  CONSTRAINT fk_recordpage_record FOREIGN KEY (record_id)
      REFERENCES rec$record (id)
      ON UPDATE NO ACTION ON DELETE CASCADE
);

-- Tags
-- DROP TABLE tag$tag;

CREATE TABLE tag$tag
(
  id bigint NOT NULL auto_increment,
  primary key(id),
  title character varying(255) NOT NULL
);

-- DROP TABLE "tag$tag_record";

CREATE TABLE tag$tag_record
(
  tag_id bigint NOT NULL,
  record_id bigint NOT NULL,
  primary key(tag_id, record_id),
  CONSTRAINT fk_tag FOREIGN KEY (tag_id)
      REFERENCES tag$tag (id)
      ON UPDATE NO ACTION ON DELETE CASCADE,
  CONSTRAINT fk_record FOREIGN KEY (record_id)
      REFERENCES rec$record (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
);
