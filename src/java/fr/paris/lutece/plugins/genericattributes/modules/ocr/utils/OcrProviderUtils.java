package fr.paris.lutece.plugins.genericattributes.modules.ocr.utils;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.paris.lutece.plugins.genericattributes.business.IOcrProvider;
import fr.paris.lutece.plugins.genericattributes.modules.ocr.business.Mapping;
import fr.paris.lutece.plugins.genericattributes.modules.ocr.business.MappingHome;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.httpaccess.HttpAccess;
import fr.paris.lutece.util.httpaccess.HttpAccessException;
import net.sf.json.JSONObject;


public class OcrProviderUtils {
	private static final String PROPERTY_OCR_URL = "forms-ocr.ws.ocr.url";
    private static final String JSON_UTF8_CONTENT_TYPE = "application/json; charset=UTF-8";
	
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
	
	/**
     * Call WS OCR with the uploaded file.
     *
     * @param fileUploaded fileUploaded
     * @return the Ocr result
     */
	public static Map<String, String> process( FileItem fileUploaded, String fileTypeKey )
    {
        JSONObject jsonContent = buildJsonContent( fileUploaded, fileTypeKey );
        if ( jsonContent == null )
        {
            return null;
        }

        try
        {
            HttpAccess httpAccess = new HttpAccess( );
            Map<String, String> headersRequest = new HashMap<>( );
            headersRequest.put( "Content-Type", JSON_UTF8_CONTENT_TYPE);
            Map<String, String> headersResponse = new HashMap<>( );

            String strOcrRestUrl = AppPropertiesService.getProperty( PROPERTY_OCR_URL );
            //call WS OCR
            String strReponse = httpAccess.doPostJSON( strOcrRestUrl, jsonContent.toString( ), headersRequest, headersResponse );

            return new ObjectMapper( ).readValue( strReponse, HashMap.class );

        } catch (  IOException | HttpAccessException e )
        {
            AppLogService.error( e.getMessage( ), e );
            return null;
        }
    }
	
	/**
	 * Build Json content to call OCR WS.
	 *
	 * @param fileUploaded            File upload for OCR
	 * @param fileTypeKey the file type key
	 * @return Json message
	 */
    private static JSONObject buildJsonContent( FileItem fileUploaded, String fileTypeKey )
    {
        if ( fileUploaded == null )
        {
            return null;
        }

        try
        {
            byte[] fileContent = IOUtils.toByteArray( fileUploaded.getInputStream( ) );
            String strEncodedFileContent = Base64.getEncoder( ).encodeToString( fileContent );
            String strFileExtension = fileUploaded.getContentType( ) != null ? fileUploaded.getContentType( ).split( "/" )[1]:null;

            JSONObject jsonObj = new JSONObject( );
            jsonObj.accumulate( "filecontent", strEncodedFileContent );
            jsonObj.accumulate( "fileextension", strFileExtension );
            jsonObj.accumulate( "documenttype", fileTypeKey );

            return jsonObj;

        } catch ( IOException e )
        {
            AppLogService.error( e.getMessage( ), e );
            return null;
        }
    }

}
