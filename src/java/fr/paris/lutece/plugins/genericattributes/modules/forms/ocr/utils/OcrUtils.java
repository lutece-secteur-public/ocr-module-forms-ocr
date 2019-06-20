/*
 * Copyright (c) 2002-2014, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.genericattributes.modules.forms.ocr.utils;


import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.httpaccess.HttpAccess;
import fr.paris.lutece.util.httpaccess.HttpAccessException;
import net.sf.json.JSONObject;

/**
 * The Class OcrUtils.
 */
public final class OcrUtils 
{
	private static final String PROPERTY_OCR_URL = "forms-ocr.ws.ocr.url";
    private static final String JSON_UTF8_CONTENT_TYPE = "application/json; charset=UTF-8";
    
    /**
     * Call WS OCR with the uploaded file.
     *
     * @param fileUploaded fileUploaded
     * @return the Ocr result
     */
	public static Map<String, String> processOcr( FileItem fileUploaded, String fileTypeKey )
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
