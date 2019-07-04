package fr.paris.lutece.plugins.genericattributes.modules.ocr.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.paris.lutece.plugins.genericattributes.business.Entry;
import fr.paris.lutece.plugins.genericattributes.business.EntryHome;
import fr.paris.lutece.plugins.genericattributes.business.Field;
import fr.paris.lutece.plugins.genericattributes.business.IOcrProvider;
import fr.paris.lutece.plugins.genericattributes.business.Response;
import fr.paris.lutece.plugins.genericattributes.modules.ocr.business.Mapping;
import fr.paris.lutece.plugins.genericattributes.modules.ocr.business.MappingHome;
import fr.paris.lutece.plugins.genericattributes.modules.ocr.exceptions.CallOcrException;
import fr.paris.lutece.portal.service.editor.EditorBbcodeService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.httpaccess.HttpAccess;
import fr.paris.lutece.util.httpaccess.HttpAccessException;
import net.sf.json.JSONObject;

public class OcrProviderUtils {
	
	private static final String PROPERTY_OCR_URL = "forms-ocr.ws.ocr.url";
    private static final String JSON_UTF8_CONTENT_TYPE = "application/json; charset=UTF-8";
    
    //OCR type entry
    public static final String ENTRY_TYPE_CHECKBOX = AppPropertiesService.getProperty( "genericattributes-ocr.entry.checkbox" );
    public static final String ENTRY_TYPE_DATE = AppPropertiesService.getProperty( "genericattributes-ocr.entry.date" );
    public static final String ENTRY_TYPE_RADIOBUTTON = AppPropertiesService.getProperty( "genericattributes-ocr.entry.radiobutton" );
    public static final String ENTRY_TYPE_SELECT = AppPropertiesService.getProperty( "genericattributes-ocr.entry.select" );
    public static final String ENTRY_TYPE_TEXTAREA = AppPropertiesService.getProperty( "genericattributes-ocr.entry.textarea" );
    
    //Error message
    private static String MESSAGE_ERROR_OCR = "module.genericattributes.ocr.error.callOcr";
	
	public static HtmlTemplate builtTempalteConfiog ( ReferenceList listEntry, IOcrProvider ocrProvider, int nIdTargetEntry, String strResourceType ){
		
		 Map<String, Object> model = new HashMap<>();		 
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
		 
		 return AppTemplateService.getTemplate(OCRConstants.TEMPLATE_MODIFY_MAPPING, null, model);
	}

	
	public static HtmlTemplate builtTempalteCode( int nIdTargetEntry, String strResourceType ){
		
		 Map<String, Object> model = new HashMap<>();
		 
		 model.put(OCRConstants.MARK_ID_TARGET_ENTRY, nIdTargetEntry);
		 model.put(OCRConstants.MARK_RESOURCE_TYPE, strResourceType);		 
		 return AppTemplateService.getTemplate(OCRConstants.TEMPLATE_FILL_ENTRY_OCR, null, model);
	}
	
	/**
     * Call WS OCR with the uploaded file.
     *
     * @param fileUploaded fileUploaded
     * @return the Ocr result
	 * @throws CallOcrException 
     */
	public static List<Response> process( FileItem fileUploaded, int nIdTargetEntry, String strResourceType, String fileTypeKey, ReferenceList referenceListField ) throws CallOcrException
    {
		List<Response> listResponse = new ArrayList<>();
		
        JSONObject jsonContent = buildJsonContent( fileUploaded, fileTypeKey );
        if ( jsonContent == null )
        {
            return listResponse;
        }
 
        try
        {
            HttpAccess httpAccess = new HttpAccess( );
            Map<String, String> headersRequest = new HashMap<>( );
            headersRequest.put( "Content-Type", JSON_UTF8_CONTENT_TYPE);
            Map<String, String> headersResponse = new HashMap<>( );

            String strOcrRestUrl = AppPropertiesService.getProperty( PROPERTY_OCR_URL );
            //call WS OCR (Exemple of response of RIB document for mock: "{\"Rib result\":\"repRib\",\"Code Banque\":\"repCode\",\"IBAN\":\"repIban\",\"Account number\":\"repAccount\",\"Code Guichet\":\"repCode\",\"ClÃ© RIB\":\"repCle\",\"BIC\":\"repBic\"}" ) 
            String strReponse = httpAccess.doPostJSON( strOcrRestUrl, jsonContent.toString( ), headersRequest, headersResponse );
            Map<String, String> ocrResponse = new ObjectMapper( ).readValue( strReponse, HashMap.class );
            
            return buildResponseFromOcrWSResponse(ocrResponse, nIdTargetEntry, strResourceType, referenceListField);

        } catch (  IOException | HttpAccessException | IllegalArgumentException e)
        {
        	throw new CallOcrException(I18nService.getLocalizedString( MESSAGE_ERROR_OCR, Locale.getDefault() ), e);
        }
    }
	
	/**
	 * Builds the responses list from ocr WS response.
	 *
	 * @param ocrResponse the ocr response
	 * @param nIdTargetEntry the n id target entry
	 * @param strResourceType the str resource type
	 * @return the list
	 */
	private static List<Response> buildResponseFromOcrWSResponse( Map<String, String> ocrResponse, int nIdTargetEntry, String strResourceType, ReferenceList referenceListField ) {
		// Get the mapping list from the entry
		List<Mapping> mappingList = MappingHome.loadMappingByTargetEntry(nIdTargetEntry, strResourceType);
		
		List<Response> listResponse = new ArrayList<>();
		
		for( Mapping mapping : mappingList ) {
			//Get the entry of the mapping, to have the type, then the responses
			Entry entry = EntryHome.findByPrimaryKey(mapping.getIdEntry());
			
			if(entry != null) {
				String strIdType = String.valueOf(entry.getEntryType().getIdType());
				
				//Create the response by entry type
				if(strIdType.equals(ENTRY_TYPE_CHECKBOX)) {
					listResponse.addAll(buildMultiFieldResponse( ocrResponse, entry, referenceListField, mapping.getIdFieldOcr()));
				} 
				else if (strIdType.equals(ENTRY_TYPE_DATE)) {
					listResponse.addAll(buildDateResponse( ocrResponse, entry, referenceListField, mapping.getIdFieldOcr()));
				} 
				else if (strIdType.equals(ENTRY_TYPE_RADIOBUTTON)) {
					listResponse.addAll(buildMultiFieldResponse( ocrResponse, entry, referenceListField, mapping.getIdFieldOcr()));
				} 
				else if (strIdType.equals(ENTRY_TYPE_SELECT)) {
					listResponse.addAll(buildMultiFieldResponse( ocrResponse, entry, referenceListField, mapping.getIdFieldOcr()));
				} 
				else if (strIdType.equals(ENTRY_TYPE_TEXTAREA)) {
					listResponse.addAll(buildTextAreaResponse( ocrResponse, entry, referenceListField, mapping.getIdFieldOcr()));
				} 
				else {
					listResponse.addAll(buildTextResponse( ocrResponse, entry, referenceListField, mapping.getIdFieldOcr()));
				}
			}
		}
		
		return listResponse;
	}
	
	/**
	 * Builds the text response.
	 *
	 * @param ocrResponse the ocr response
	 * @param entry the entry
	 * @param referenceListField the reference list field
	 * @param mappingFieldOcr the mapping field ocr
	 * @return the list
	 */
	private static List<Response> buildTextResponse( Map<String, String> ocrResponse, Entry entry, ReferenceList referenceListField, int mappingFieldOcr) {
		List<Response> listResponse = new ArrayList<>();
		Response response = new Response( );
        
		response.setEntry( entry );
        response.setResponseValue( ocrResponse.get(referenceListField.get(mappingFieldOcr).getName() ) );
        response.setToStringValueResponse(response.getResponseValue());
        listResponse.add(response);
        
        return listResponse;
	}
	
	/**
	 * Builds the text area response.
	 *
	 * @param ocrResponse the ocr response
	 * @param entry the entry
	 * @param referenceListField the reference list field
	 * @param mappingFieldOcr the mapping field ocr
	 * @return the list
	 */
	private static List<Response> buildTextAreaResponse( Map<String, String> ocrResponse, Entry entry, ReferenceList referenceListField, int mappingFieldOcr) {
		List<Response> listResponse = new ArrayList<>();
		Response response = new Response( );
        
		response.setEntry( entry );
		
		String responseValue = ocrResponse.get(referenceListField.get(mappingFieldOcr).getName() );
		
		if(entry.isFieldInLine( )) {
			response.setResponseValue( EditorBbcodeService.getInstance( ).parse(responseValue) );
        }
        else
        {
            response.setResponseValue( responseValue );
        }
		
		if ( StringUtils.isNotBlank( response.getResponseValue( ) ) && entry.isFieldInLine( ) )
        {
            // if we use a rich text, we set the toStringValueResponse to the BBCode string
            response.setToStringValueResponse( responseValue );
        }
		else
        {
            response.setToStringValueResponse( EditorBbcodeService.getInstance( ).parse(responseValue) );
        }
		
        listResponse.add(response);
        
        return listResponse;
	}
	
	/**
	 * Builds the date response.
	 *
	 * @param ocrResponse the ocr response
	 * @param entry the entry
	 * @param referenceListField the reference list field
	 * @param mappingFieldOcr the mapping field ocr
	 * @return the list
	 */
	private static List<Response> buildDateResponse( Map<String, String> ocrResponse, Entry entry, ReferenceList referenceListField, int mappingFieldOcr) {
		List<Response> listResponse = new ArrayList<>();
		Response response = new Response( );
        
		response.setEntry( entry );
		
		String responseValue = ocrResponse.get(referenceListField.get(mappingFieldOcr).getName() );

		Date tDateValue = DateUtil.formatDate( responseValue, Locale.getDefault() );

        if ( tDateValue != null )
        {
            response.setResponseValue( DateUtil.getDateString( tDateValue, Locale.getDefault() ) );
        }
        else
        {
            response.setResponseValue( responseValue );
        }

        if ( StringUtils.isNotBlank( response.getResponseValue( ) ) )
        {
            Date date = DateUtil.formatDate( response.getResponseValue( ), Locale.getDefault() );

            if ( date != null )
            {
                response.setToStringValueResponse( response.getResponseValue( ) );
            }
            else
            {
                response.setToStringValueResponse( StringUtils.EMPTY );
            }
        }
        else
        {
            response.setToStringValueResponse( StringUtils.EMPTY );
        }
		
		listResponse.add(response);
        
        return listResponse;
	}
	
	/**
	 * Builds the multi field response.
	 *
	 * @param ocrResponse the ocr response
	 * @param entry the entry
	 * @param referenceListField the reference list field
	 * @param mappingFieldOcr the mapping field ocr
	 * @return the list
	 */
	private static List<Response> buildMultiFieldResponse( Map<String, String> ocrResponse, Entry entry, ReferenceList referenceListField, int mappingFieldOcr) {
		List<Response> listResponse = new ArrayList<>();
		Response response = new Response( );
        response.setEntry( entry );
        
        //Check if the values match with one of fields of the entry
        List<Field> listField = entry.getFields( ) ;
        
        for (Field field : listField ) {
        	if( field.getValue().equals(ocrResponse.get(referenceListField.get(mappingFieldOcr).getName()))) {
        		response.setResponseValue( field.getValue( ) );
                response.setField( field );
        	}
        }
        listResponse.add( response );
        
        return listResponse;
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
