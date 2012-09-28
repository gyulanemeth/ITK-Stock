SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

DROP TABLE IF EXISTS users; 
DROP TABLE IF EXISTS watchers;
DROP FUNCTION IF EXISTS watchers_triggers;
DROP TRIGGER IF EXISTS watchers_insert;
DROP TRIGGER IF EXISTS watchers_update;

CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `email` varchar(60) NOT NULL,
  `password` varchar(60) NOT NULL,
  `money` bigint(20) DEFAULT '0',
  `is_admin` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=8 ;

CREATE TABLE watchers (
	id int(11) NOT NULL AUTO_INCREMENT,
	username varchar(20) NOT NULL,
	stockname varchar(20) NOT NULL,
	boundvalue  float(7,4) NOT NULL,
	boundtype int (11) NOT NULL DEFAULT '0',
	PRIMARY KEY(id),
	FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE,
	FOREIGN KEY (stockname) REFERENCES stocks(stockname) ON DELETE CASCADE
) AUTO_INCREMENT=8;

CREATE FUNCTION watchers_trigger(
   IN other_stockname varchar(20),
   IN boundvalue float(7,4),
   IN boundtype int (11)
)
RETURNS BOOLEAN
	BEGIN
	  DECLARE ret BOOLEAN;
	  DECLARE stockprice float(7,4);
	  
	  SELECT price FROM stocks WHERE other_stockname = stockname INTO stockprice;
	  
	  IF boundtype = 1 THEN
		IF boundvalue > price THEN
			ret = TRUE;
		ELSE
			ret = FALSE;
	    END IF
	  ELSE
		IF boundvalue < price THEN
			SET ret = TRUE;
		ELSE
			SET ret = FALSE;
		END IF
	  END IF
	  
	  RETURN ret;
	  
	END;
	
CREATE TRIGGER watchers_insert
    BEFORE INSERT ON watchers
    FOR EACH ROW
BEGIN
	IF watchers_trigger(NEW.stockname, NEW.boundvalue, NEW.boundtype) = FALSE THEN
	SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Boundvalue is not OK';
	END IF
END;

CREATE TRIGGER watchers_update
    BEFORE UPDATE ON watchers
    FOR EACH ROW
BEGIN
	IF watchers_trigger(NEW.stockname, NEW.boundvalue, NEW.boundtype) = FALSE THEN
	SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Boundvalue is not OK';
	END IF
END;