package fr.paris.lutece.plugins.genericattributes.modules.ocr.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.paris.lutece.plugins.genericattributes.business.IOcrProvider;
import fr.paris.lutece.plugins.genericattributes.modules.ocr.business.Mapping;
import fr.paris.lutece.plugins.genericattributes.modules.ocr.business.MappingHome;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

public class OcrProviderUtils {
	
	public static HtmlTemplate builtTempalteConfiog ( ReferenceList listEntry, IOcrProvider ocrProvider, int nIdTargetEntry, String strResourceType ){
		
		 Map<String, Object> model = new HashMap<String, Object>();		 
		 ReferenceList listEntryFiltred= new ReferenceList();
		 listEntryFiltred.addAll(listEntry);
		 List<Mapping> listMapping= MappingHome.loadMappingByTargetEntry(nIdTargetEntry, strResourceType);
		 
		 listEntryFiltred.removeIf(p-> listMapping.stream().anyMatch(ma -> p.getCode().equals(String.valueOf(ma.getIdEntry()))));
				 
		 model.put(OCRConstants.MARK_ENTRY_LIST_FILTRED, listEntryFiltred);
		 model.put(OCRConstants.MARK_ENTRY_LIST, listEntry);
		 model.put(OCRConstants.MARK_FIELD_LIST, ocrProvider.getListField() );
		 model.put(OCRConstants.MARK_MAPPING_LIST, listMapping );
		 model.put(OCRConstants.MARK_OCR_PROVIDER_KEY, ocrProvider.getKey());
		 model.put(OCRConstants.MARK_ID_TARGET_ENTRY, nIdTargetEntry);
		 model.put(OCRConstants.MARK_RESOURCE_TYPE, strResourceType);
		 
		 HtmlTemplate htmlTemplate= AppTemplateService.getTemplate(OCRConstants.TEMPLATE_MODIFY_MAPPING, null, model);
		 
		
		return htmlTemplate;
	}

	
	public static HtmlTemplate builtTempalteCode( int nIdTargetEntry, String strResourceType ){
		
		 Map<String, Object> model = new HashMap<String, Object>();
		 
		 model.put(OCRConstants.MARK_ID_TARGET_ENTRY, nIdTargetEntry);
		 model.put(OCRConstants.MARK_RESOURCE_TYPE, strResourceType);		 
		 HtmlTemplate htmlTemplate= AppTemplateService.getTemplate(OCRConstants.TEMPLATE_FILL_ENTRY_OCR, null, model);
		 
		
		return htmlTemplate;
	}

}
