
	
-- 
-- Add a new table for the mapping of automatic file reading
-- 
DROP TABLE IF EXISTS genatt_ocr_mapping_file_reading;
CREATE TABLE genatt_ocr_mapping_file_reading (

  id_target_entry INT DEFAULT 0 NOT NULL,
  resource_type varchar(255) NOT NULL DEFAULT '',
  id_entry INT DEFAULT 0 NOT NULL,
  id_field_ocr INT DEFAULT 0 NOT NULL,
  PRIMARY KEY (id_target_entry, resource_type, id_entry)
);