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

import java.util.List;

import fr.paris.lutece.plugins.genericattributes.modules.ocr.service.GenericattributesOcrPlugin;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;

/**
 * This class provides instances management methods (create, find, ...) for mapping objects
 */
public final class MappingHome
{
    // Static variable pointed at the DAO instance
    private static IMappingDAO _dao = SpringContextService.getBean( "genericattributes-ocr.mappingDAO" );
    private static Plugin _plugin;

    /**
     * Private constructor - this class need not be instantiated
     */
    private MappingHome( )
    {
    }

    /**
     * Creates the mapping.entry
     *
     * @param mapping
     *            the mapping object
     */
    public static void create( Mapping mapping )
    {
        _dao.insert( mapping, getPlugin( ) );
    }

    /**
     * Remove mapping object line
     * 
     * @param nIdTargetEntry
     *            The target entry id
     * @param strResourceType
     *            The Resource type
     * @param nIdEntry
     *            the Entry Id
     */
    public static void remove( int nIdTargetEntry, String strResourceType, int nIdEntry )
    {
        _dao.delete( nIdTargetEntry, strResourceType, nIdEntry, getPlugin( ) );
    }

    /**
     * Load object list mapped by target entry id and reource type
     * 
     * @param nIdTargetEntry
     *            The id target entry
     * @param strResourceType
     *            The resoruce type
     * @return the list of mapping object
     */
    public static List<Mapping> loadMappingByTargetEntry( int nIdTargetEntry, String strResourceType )
    {
        return _dao.loadMappingByTargetEntry( nIdTargetEntry, strResourceType, getPlugin( ) );
    }

    /**
     * Get the generic attributes ocr module
     * 
     * @return The generic attributes ocr module
     */
    private static Plugin getPlugin( )
    {
        if ( _plugin == null )
        {
            _plugin = GenericattributesOcrPlugin.getPlugin( );
        }

        return _plugin;
    }
}
