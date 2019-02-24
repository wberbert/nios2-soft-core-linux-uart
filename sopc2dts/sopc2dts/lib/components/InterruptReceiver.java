/*
sopc2dts - Devicetree generation for Altera systems

Copyright (C) 2014 Matthew Gerlach <mgerlach@altera.com>

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
package sopc2dts.lib.components;

import java.util.Vector;

import sopc2dts.Logger;
import sopc2dts.Logger.LogLevel;
import sopc2dts.lib.AvalonSystem;
import sopc2dts.lib.AvalonSystem.SystemDataType;
import sopc2dts.lib.Connection;
import sopc2dts.lib.components.Interface;
import sopc2dts.lib.components.BasicComponent;
import sopc2dts.lib.components.SopcComponentDescription;

public class InterruptReceiver extends BasicComponent
{
	private boolean removed = false;

	public InterruptReceiver(String cName, String iName, String ver, SopcComponentDescription scd) {
		super(cName, iName, ver, scd);
	}

	protected void removeConnection(Connection conn)
	{
		conn.disconnect();
	}
	@Override
	public boolean removeFromSystemIfPossible(AvalonSystem sys)
	{
		if (!removed) {
			removed = true;
			Vector<Interface> vIrqMasters = getInterfaces(SystemDataType.INTERRUPT, true);
			if (vIrqMasters.size() != 1) {
				Logger.logln("Invalid number of interrupt receivers: "+vIrqMasters, LogLevel.ERROR);
				return false;
			}
			Interface irqMaster = vIrqMasters.firstElement();
			Logger.logln("irq receiver is " + irqMaster.getName(),LogLevel.DEBUG);
			while(!irqMaster.getConnections().isEmpty()) {
				removeConnection(irqMaster.getConnections().firstElement());
			}
			return true;
		} else {
			return false;
		}
	}
}
