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
package fr.paris.lutece.plugins.genericattributes.modules.forms.ocr.service;


import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.fileupload.FileItem;

import fr.paris.lutece.plugins.genericattributes.business.ITypeDocumentOcrProvider;
import fr.paris.lutece.plugins.genericattributes.modules.forms.ocr.utils.OcrUtils;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;


/**
 *
 * OcrProvider : provides ocr RIB support for Generic Attributes
 *
 */
public class OcrRibProvider implements ITypeDocumentOcrProvider
{
    private static final long serialVersionUID = 6224042984367506762L;
    private static final String PROPERTY_KEY = "genericattributes-ocr.RIB.key";
    private static final String PROPERTY_DISPLAYED_NAME = "genericattributes-ocr.RIB.displayName";
    private static final String PROPERTY_AUTHORIZED_ENTRY_TYPE = "genericattributes-ocr.RIB.authorizedEntryType";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey( )
    {
        return AppPropertiesService.getProperty( PROPERTY_KEY );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayedName( )
    {
        return AppPropertiesService.getProperty( PROPERTY_DISPLAYED_NAME );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReferenceItem toRefItem( )
    {
        ReferenceItem refItem = new ReferenceItem( );

        refItem.setCode( getKey( ) );
        refItem.setName( getDisplayedName( ) );

        return refItem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString( )
    {
        return "Ocr RIB Provider";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getParameter(int nKey) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReferenceList getListField() {
        ReferenceList refListField = new ReferenceList( );

        //TODO récupérer la liste des champs
        refListField.addItem(0, "Rib result");
        refListField.addItem(1, "Code Banque");
        refListField.addItem(2, "Code Guichet");
        refListField.addItem(3, "Account number");
        refListField.addItem(4, "Clé RIB");
        refListField.addItem(5, "IBAN");
        refListField.addItem(6, "BIC");
        refListField.addItem(7, "RIB Address");

        return refListField;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReferenceItem getFieldById(int idField) {
        return getListField().get(idField);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getAuthorizedEntryType() {
        String strAuthorizedEntryType = AppPropertiesService.getProperty( PROPERTY_AUTHORIZED_ENTRY_TYPE );
        Pattern pattern = Pattern.compile("-");
        return pattern.splitAsStream(strAuthorizedEntryType).map(Integer::valueOf).collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public Map<String, String> processOcr( FileItem fileUploaded )
    {
    	return OcrUtils.processOcr(fileUploaded, getKey( ) );
    }

    
}
