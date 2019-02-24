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
package sopc2dts.generators;

import java.util.Vector;

import sopc2dts.Logger;
import sopc2dts.Logger.LogLevel;
import sopc2dts.lib.AvalonSystem;
import sopc2dts.lib.AvalonSystem.SystemDataType;
import sopc2dts.lib.BoardInfo;
import sopc2dts.lib.Connection;
import sopc2dts.lib.components.BasicComponent;
import sopc2dts.lib.components.Interface;
import sopc2dts.lib.components.base.SICBridge;
import sopc2dts.lib.devicetree.DTHelper;
import sopc2dts.lib.uboot.UBootComponentLib;

public class UBootHeaderGenerator extends AbstractSopcGenerator {
	Vector<BasicComponent> vHandled;
	
	public UBootHeaderGenerator(AvalonSystem s) {
		super(s, true);
	}

	@Override
	public String getTextOutput(BoardInfo bi) {
		String res = getSmallCopyRightNotice("header", bi.isIncludeTime()) + 
				"#ifndef CUSTOM_FPGA_H_\n" +
				"#define CUSTOM_FPGA_H_\n\n" +
				"/* generated from " + sys.getSourceFile().getName() + " */\n\n";
		BasicComponent pov = getPovCpu(bi.getPov());
		vHandled = new Vector<BasicComponent>();
		if(pov!=null)
		{
			//Try the interfacename for the Nios2
			Interface intf = pov.getInterfaceByName("data_master");
			if(intf==null) {
				//Just use the first one...
				intf = pov.getInterfaces(SystemDataType.MEMORY_MAPPED, true).firstElement();
			}
			res += getInfoForSlavesOf(intf, 0, pov);
		} else {
			Logger.logln("Unable to find a CPU. U-Boot works best when run on a cpu.", LogLevel.ERROR);
		}
		res += "\n#endif\t//CUSTOM_FPGA_H_\n";
		return res;
	}
	String getInfoForSlavesOf(Interface master, long offset, BasicComponent irqMaster)
	{
		String res = "";
		for(Connection conn : master.getConnections())
		{
			BasicComponent comp = conn.getSlaveInterface().getOwner();
			if(res.length() == 0)
			{
				res = "/* Dumping slaves of " + master.getOwner().getInstanceName() + '.' + master.getName() + "*/\n";
			}
			if(!vHandled.contains(comp))
			{
				res += "\n" + getInfoFor(master.getOwner(),irqMaster, conn.getSlaveInterface(), offset);
			}
			if(comp instanceof SICBridge)
			{
				for(Interface intf : comp.getInterfaces())
				{
					if(intf.isMemoryMaster())
					{
						res += getInfoForSlavesOf(intf, offset + DTHelper.longArrToLong(conn.getConnValue()), irqMaster);
					}
				}
			}
		}
		return res;
	}
	String getInfoFor(BasicComponent memMaster, BasicComponent irqMaster, Interface intf, long offset)
	{
		BasicComponent comp = intf.getOwner();
		vHandled.add(comp);
		String res = "/* " + comp.getInstanceName() + '.' + 
				intf.getName() + " is a " + comp.getClassName() + " */\n";
		res += UBootComponentLib.getInstance().getCompFor(comp).getHeadersFor(
				memMaster, irqMaster, comp, comp.getInterfaces().indexOf(intf), offset);
		return res;
	}
	BasicComponent getPovCpu(String name)
	{
		BasicComponent pov = null;
		// First try find the cpu chosen.
		if(name != null)
		{
			pov = sys.getComponentByName(name);
			if(pov != null)
			{
				if(!pov.getScd().getGroup().equalsIgnoreCase("cpu"))
				{
					Logger.logln("Chosen pov-component is not a cpu. Trying to find a random cpu.", LogLevel.WARNING);
					pov = null;
				}
			} else {
				Logger.logln("Chosen pov-component not found. Trying to find a random cpu.", LogLevel.WARNING);
			}
		}
		// Find the first cpu and use it
		if(pov != null)
		{
			return pov;
		} else {
			for(BasicComponent bc : sys.getMasterComponents())
			{
				Logger.logln("Considering " + bc.getInstanceName());
				if(bc.getScd().getGroup().equalsIgnoreCase("cpu"))
				{
					return bc;
				}
			}
		}
		return null;
	}
}
