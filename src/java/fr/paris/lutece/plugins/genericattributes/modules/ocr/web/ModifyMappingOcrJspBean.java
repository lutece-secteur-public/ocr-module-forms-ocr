/*
 * Copyright (c) 2002-2018, Mairie de Paris
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
package fr.paris.lutece.plugins.genericattributes.modules.ocr.web;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.paris.lutece.plugins.genericattributes.business.IOcrProvider;
import fr.paris.lutece.plugins.genericattributes.business.OcrProviderManager;
import fr.paris.lutece.plugins.genericattributes.modules.ocr.business.Mapping;
import fr.paris.lutece.plugins.genericattributes.modules.ocr.business.MappingHome;
import fr.paris.lutece.plugins.genericattributes.modules.ocr.utils.OCRConstants;
import fr.paris.lutece.plugins.genericattributes.modules.ocr.utils.OcrProviderUtils;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

/**
 * This class provides the user interface to manage ocr features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ModifyMappingOcr.jsp", controllerPath = "jsp/admin/plugins/genericattributes/modules/ocr", right = "MAPPING_OCR_MANAGEMENT" )
public class ModifyMappingOcrJspBean extends MVCAdminJspBean
{
    private static final long serialVersionUID = 8210813454305009601L;
    
    // template
    
    public static final String TEMPLATE_MODIFY_MAPPING = "/admin/plugins/genericattributes/modules/ocr/modify_mapping.html";
   
    /**
     * Right to manage forms
     */
    public static final String RIGHT_MAPPING_OCR_MANAGEMENT= "MAPPING_OCR_MANAGEMENT";

    // Views
    private static final String VIEW_MODIFY_MAPPING = "modifyMapping";

    // Actions
    private static final String ACTION_CREATE_MAPPING = "createMapping";
    private static final String ACTION_REMOVE_MAPPING = "removeMapping";

    // other constants

    private static final String EXTENSION_APPLICATION_JSON = "application/json";



    @View( VIEW_MODIFY_MAPPING )
    public String getModifyMapping( HttpServletRequest request ){
    	
        String strResourceType = request.getParameter( OCRConstants.PARAMETER_RESOURCE_TYPE );
        String strOcrKeyProvider = request.getParameter( OCRConstants.PARAMETER_OCR_PROVIDER_KEY );
        String strIdTargetEntry = request.getParameter( OCRConstants.PARAMETER_ID_TARGET_ENTRY );
        
        int nIdTargetEntry = Integer.parseInt( strIdTargetEntry );
        IOcrProvider provider= OcrProviderManager.getOcrProvider(strOcrKeyProvider);
        ReferenceList listEntry= new ReferenceList();
        
        HtmlTemplate htmlTemplate= OcrProviderUtils.builtTempalteConfiog(listEntry, provider, nIdTargetEntry, strResourceType);
 
        
    	return htmlTemplate.getHtml();
    }
    
    /**
     * Do create mapping.
     *
     * @param request
     *            the request
     * @return the string
     */
    @Action( ACTION_CREATE_MAPPING )
    public String doCreateMapping( HttpServletRequest request )
    {
       
        String strIdTargetEntry = request.getParameter( OCRConstants.PARAMETER_ID_TARGET_ENTRY );
        String strIdEntry = request.getParameter( OCRConstants.PARAMETER_ID_ENTRY );
        String strIdFieldOcr = request.getParameter( OCRConstants.PARAMETER_ID_FIELD_OCR );
        String strResourceType = request.getParameter( OCRConstants.PARAMETER_RESOURCE_TYPE );
       	String referHeader= request.getParameter(OCRConstants.PARAMETER_HEADRE_REFER);

        int nIdTargetEntry = Integer.parseInt( strIdTargetEntry );
        int nIdFieldOcr = Integer.parseInt( strIdFieldOcr );
        int nIdEntry = Integer.parseInt( strIdEntry );

        Mapping mapping = new Mapping( );
        mapping.setIdFieldOcr( nIdFieldOcr );
        mapping.setIdTargetEntry( nIdTargetEntry );
        mapping.setResourceType(strResourceType);
        mapping.setIdEntry(nIdEntry);

        MappingHome.create( mapping );
        
        UrlItem url = new UrlItem(referHeader);
        return redirect(request, url.getUrl());
    }

    /**
     * Perform suppression field
     * 
     * @param request
     *            The HTTP request
     * @return The URL to go after performing the action
     */
    @Action( ACTION_REMOVE_MAPPING )
    public String doRemoveMapping( HttpServletRequest request )
    {
        String strIdTargetEntry = request.getParameter( OCRConstants.PARAMETER_ID_TARGET_ENTRY );
        String strResourceType = request.getParameter( OCRConstants.PARAMETER_RESOURCE_TYPE );
        String strIdEntry = request.getParameter( OCRConstants.PARAMETER_ID_ENTRY );
     	String referHeader= request.getParameter(OCRConstants.PARAMETER_HEADRE_REFER);

        
        int nIdTargetEntry = Integer.parseInt( strIdTargetEntry );
        int nIdEntry = Integer.parseInt( strIdEntry );
       
        MappingHome.remove( nIdTargetEntry, strResourceType,  nIdEntry );
  
        UrlItem url = new UrlItem(referHeader);
        return redirect(request, url.getUrl());

    }

   
   /**
    * init field ocr
    */
    public void initFieldMappingOcr( HttpServletRequest request, HttpServletResponse response ) throws IOException
    {
        response.setContentType( EXTENSION_APPLICATION_JSON );
        String strIdEntry = request.getParameter( OCRConstants.PARAMETER_ID_TARGET_ENTRY );
        String strResourceType = request.getParameter( OCRConstants.PARAMETER_RESOURCE_TYPE );

        int nIdTargetEntry = Integer.parseInt( strIdEntry );
        List<Mapping> mappingList= MappingHome.loadMappingByTargetEntry(nIdTargetEntry, strResourceType);
        List<Integer> entryList= mappingList.stream().map( c -> c.getIdEntry() ).collect( Collectors.toList() );
        try
        {
            response.getWriter( ).print( entryList );
        }
        catch( IOException e )
        {
            AppLogService.error( e.getMessage( ), e );
        }
    }

}
