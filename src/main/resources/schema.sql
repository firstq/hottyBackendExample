DROP TABLE IF EXISTS menuitem;

CREATE TABLE menuitem (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  link varchar(255) DEFAULT NULL,
  name varchar(255) NOT NULL,
  priority int(11) NOT NULL,
  toplevel tinyint(1) DEFAULT NULL,
  version int(11) DEFAULT NULL,
  parent bigint(20) DEFAULT NULL,
  PRIMARY KEY (id)
);
