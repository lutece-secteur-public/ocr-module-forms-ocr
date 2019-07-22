package fr.paris.lutece.plugins.genericattributes.modules.ocr.service.listener;

import java.util.List;

import fr.paris.lutece.plugins.genericattributes.modules.ocr.business.Mapping;
import fr.paris.lutece.plugins.genericattributes.modules.ocr.business.MappingHome;
import fr.paris.lutece.plugins.genericattributes.util.CopyEntryEventParam;
import fr.paris.lutece.portal.business.event.EventRessourceListener;
import fr.paris.lutece.portal.business.event.ResourceEvent;

public class OcrEntryListener implements EventRessourceListener {

	private static final String LISTENER_NAME = "ocrEntryListener";
	public static final String BEAN_NAME = "genericattributes-ocr.ocrEntryListener";
	 
	@Override
	public void addedResource( ResourceEvent event )
	{
		Integer oldId = null;
		if ( event.getParam( ) != null && event.getParam( ) instanceof CopyEntryEventParam )
		{
			oldId = ( (CopyEntryEventParam) event.getParam( ) ).getValue( );
		}
		
		if ( oldId != null )
		{
			List<Mapping> mappingList = MappingHome.loadMappingByTargetEntry( oldId, event.getTypeResource( ) );
			for( Mapping mapping : mappingList )
			{
				mapping.setIdTargetEntry( Integer.valueOf( event.getIdResource( ) ) );
				MappingHome.create( mapping );
			}
		}
	}

	@Override
	public void deletedResource( ResourceEvent event )
	{
		List<Mapping> mappingList = MappingHome.loadMappingByTargetEntry( Integer.valueOf( event.getIdResource( ) ), event.getTypeResource( ) );
		
		for( Mapping mapping : mappingList )
		{
			MappingHome.remove( mapping.getIdTargetEntry( ), mapping.getResourceType( ), mapping.getIdEntry( ) );
		}
	}

	@Override
	public String getName( )
	{
		return LISTENER_NAME;
	}

	@Override
	public void updatedResource( ResourceEvent arg0 )
	{
	}
}
