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

import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * This class provides Data Access methods for Mapping objects
 */
public final class MappingDAO implements IMappingDAO
{
	
	
	private static final String SQL_QUERY_FIND_BY_KEY = "SELECT id_target_entry,resource_type,id_entry,id_field_ocr "
            + " FROM genatt_ocr_mapping_file_reading  WHERE id_target_entry = ? AND resource_type= ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO genatt_ocr_mapping_file_reading(id_target_entry,resource_type,id_entry,id_field_ocr)"
            + " VALUES(?,?,?,?)";
    private static final String SQL_QUERY_DELETE = "DELETE FROM genatt_ocr_mapping_file_reading WHERE id_target_entry = ? and resource_type = ? and id_entry = ?";
    

    /**
     * {@inheritDoc}
     */
	@Override
	public void insert(Mapping mapping, Plugin plugin) {
		
		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        
		daoUtil.setInt( 1, mapping.getIdTargetEntry());
        daoUtil.setString( 2, mapping.getResourceType( ));
        daoUtil.setInt( 3, mapping.getIdEntry());
        daoUtil.setInt( 4, mapping.getIdFieldOcr());
        
        daoUtil.executeUpdate( );
        daoUtil.close();

	}

	/**
     * {@inheritDoc}
     */
	@Override
	public void delete(int nIdTargetEntry, String strResourceType, int nIdEntry, Plugin plugin) {
		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nIdTargetEntry );
        daoUtil.setString( 2, strResourceType );
        daoUtil.setInt( 3, nIdEntry );
        daoUtil.executeUpdate( );
        daoUtil.close( );
	}

	
	/**
     * {@inheritDoc}
     */
	@Override
	public List<Mapping> loadMappingByTargetEntry( int nIdTargetEntry, String strResourceType,  Plugin plugin ) {
		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_KEY, plugin );
        daoUtil.setInt( 1, nIdTargetEntry );
        daoUtil.setString( 2, strResourceType );
        daoUtil.executeQuery( );

        List<Mapping> questionMappedList = new ArrayList<>();

        while ( daoUtil.next( ) )
        {
        	Mapping mapping = new Mapping( );
        	mapping.setIdTargetEntry( daoUtil.getInt( 1 ) );
        	mapping.setResourceType( daoUtil.getString( 2 ) );
        	mapping.setIdEntry( daoUtil.getInt( 3 ) );
        	mapping.setIdFieldOcr( daoUtil.getInt( 4 ) );

        	questionMappedList.add( mapping );
        }

        daoUtil.close( );

        return questionMappedList;
	}
    
	
}
