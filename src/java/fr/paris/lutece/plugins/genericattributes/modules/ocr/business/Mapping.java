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
package fr.paris.lutece.plugins.genericattributes.modules.ocr.business;

import java.io.Serializable;

/**
 * class Field
 */
public class Mapping implements Serializable
{
	private static final long serialVersionUID = -4198261124477108458L;
	
	private String _strResourceType;
    private int _nIdEntry;
    private int _nIdFieldOcr;
    private int _nIdTargetEntry;
    
    /**
     * Returns the ResourceType
     * @return The ResourceType
     */ 
     public String getResourceType()
     {
         return _strResourceType;
     }
 
    /**
     * Sets the ResourceType
     * @param strResourceType The ResourceType
     */ 
     public void setResourceType( String strResourceType )
     {
         _strResourceType = strResourceType;
     }
 
    /**
     * Returns the IdEnttry
     * @return The IdEnttry
     */ 
     public int getIdEntry()
     {
         return _nIdEntry;
     }
 
    /**
     * Sets the IdEnttry
     * @param nIdEnttry The IdEnttry
     */ 
     public void setIdEntry( int nIdEnttry )
     {
         _nIdEntry = nIdEnttry;
     }
 
    /**
     * Returns the IdFieldOcr
     * @return The IdFieldOcr
     */ 
     public int getIdFieldOcr()
     {
         return _nIdFieldOcr;
     }
 
    /**
     * Sets the IdFieldOcr
     * @param nIdFieldOcr The IdFieldOcr
     */ 
     public void setIdFieldOcr( int nIdFieldOcr )
     {
         _nIdFieldOcr = nIdFieldOcr;
     }
 
    /**
     * Returns the IdTargetEntry
     * @return The IdTargetEntry
     */ 
     public int getIdTargetEntry()
     {
         return _nIdTargetEntry;
     }
 
    /**
     * Sets the IdTargetEntry
     * @param nIdTargetEntry The IdTargetEntry
     */ 
     public void setIdTargetEntry( int nIdTargetEntry )
     {
         _nIdTargetEntry = nIdTargetEntry;
     }
}
