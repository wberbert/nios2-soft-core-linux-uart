/*
sopc2dts - Devicetree generation for Altera systems

Copyright (C) 2011 - 2012 Walter Goossens <waltergoossens@home.nl>

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
*/
package sopc2dts.lib.components.altera;

import sopc2dts.lib.BoardInfo;
import sopc2dts.lib.Connection;
import sopc2dts.lib.components.SopcComponentDescription;
import sopc2dts.lib.components.BasicComponent;
import sopc2dts.lib.devicetree.DTNode;
import sopc2dts.lib.devicetree.DTProperty;

public class SICSgdma extends BasicComponent {

	public static final String[] TYPE_NAMES = {
		"MEMORY_TO_MEMORY",
		"MEMORY_TO_STREAM",
		"STREAM_TO_MEMORY",
		"STREAM_TO_STREAM",
		"UNKNOWN"
	};
	
	public SICSgdma(String cName, String iName, String ver, SopcComponentDescription scd) {
		super(cName, iName, ver, scd);
	}

	@Override
	public DTNode toDTNode(BoardInfo bi, Connection conn)
	{
		DTNode node = super.toDTNode(bi, conn);
		int iType = 0;
		while(iType<(TYPE_NAMES.length-1))
		{
			if(TYPE_NAMES[iType].equals(getParamValByName("transferMode")))
			{
				break;
			}
			iType++;
		}
		node.addProperty(new DTProperty("type",null,TYPE_NAMES[iType],Long.valueOf(iType)));
		return node;
	}
}
