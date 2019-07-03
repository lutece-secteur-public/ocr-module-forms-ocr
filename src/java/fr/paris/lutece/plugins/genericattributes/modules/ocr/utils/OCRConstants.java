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

package fr.paris.lutece.plugins.genericattributes.modules.ocr.utils;


/**
 * 
 * Constants class for the plugin-form
 *
 */
public final class OCRConstants
{
	
	
	//Template 
	public static final String TEMPLATE_MODIFY_MAPPING = "/admin/plugins/genericattributes/modules/ocr/modify_mapping.html";
	public static final String TEMPLATE_FILL_ENTRY_OCR = "/skin/plugins/genericattributes/modules/ocr/fill_entry_type_ocr.html";
	//Parameters
	public static final String PARAMETER_ID_TARGET_ENTRY = "id_target_entry";
	public static final String PARAMETER_ID_FIELD_OCR = "id_field_ocr";
	public static final String PARAMETER_TYPE_DOCUMENT_KEY = "type_document_key";
	public static final String PARAMETER_RESOURCE_TYPE = "resource_type";
	public static final String PARAMETER_ENTRY_LIST = "entry_list";
	public static final String PARAMETER_ENTRY_LIST_FILTRED = "entry_list_filtred";
	public static final String PARAMETER_FIELD_LIST = "field_list";
	public static final String PARAMETER_MAPPING_LIST = "mapping_list";
    public static final String PARAMETER_ID_ENTRY = "id_entry";
	public static final String PARAMETER_OCR_PROVIDER_KEY = "ocr_provider_key";
	public static final String PARAMETER_HEADRE_REFER ="referer_url";
	
	//Merkers
	public static final String MARK_ENTRY_LIST_FILTRED= "entry_list_filtred";
	public static final String MARK_ENTRY_LIST= "entry_list";
	public static final String MARK_FIELD_LIST= "field_list";
	public static final String MARK_MAPPING_LIST= "mapping_list";
	public static final String MARK_OCR_PROVIDER_KEY= "ocr_provider_key";
	public static final String MARK_ID_TARGET_ENTRY= "id_target_entry";
	public static final String MARK_RESOURCE_TYPE= "resource_type";

	//Constant
	public static final String CONSTANT_FILE_TYPE = "file_type";
	
    /**
     * Default private constructor. Do not call
     */
    private OCRConstants( )
    {

        throw new AssertionError( );

    }
}
