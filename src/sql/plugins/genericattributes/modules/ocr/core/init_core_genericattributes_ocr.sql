DELETE FROM core_admin_right WHERE id_right = 'MAPPING_OCR_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('MAPPING_OCR_MANAGEMENT','module.genericattributes.ocr.adminFeature.ocr.name',2,'jsp/admin/plugins/genericattributes/modules/ocr/ModifyMappingOcr.jsp','module.genericattributes.ocr.adminFeature.ocr.description',0,'ocr',NULL,NULL,NULL,5);
