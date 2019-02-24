/*
sopc2dts - Devicetree generation for Altera systems

Copyright (C) 2011 - 2014 Walter Goossens <waltergoossens@home.nl>

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
package sopc2dts.lib;

import sopc2dts.Logger;
import sopc2dts.Logger.LogLevel;
import sopc2dts.lib.AvalonSystem.SystemDataType;
import sopc2dts.lib.components.BasicComponent;
import sopc2dts.lib.components.Interface;


public class Connection extends BasicElement {
	protected Interface masterInterface;
	protected Interface slaveInterface;	
	protected SystemDataType type = SystemDataType.CONDUIT;
	protected long connValue[];
	
	public Connection(Interface master, Interface slave) {
		this(master,slave, master.getType());
	}
	public Connection(Interface master, Interface slave, boolean connect) {
		this(master,slave, master.getType(), connect);
	}
	public Connection(Interface master, Interface slave, SystemDataType t)
	{
		this(master, slave, t, false);
	}
	public Connection(Interface master, Interface slave, SystemDataType t, boolean connect)
	{
		type = t;
		if(connect)
		{
			connectMaster(master);
			connectSlave(slave);
		} else {
			masterInterface = master;
			slaveInterface = slave;
		}
	}
	
	public Connection(Connection org) {
		type = org.type;
		masterInterface = org.masterInterface;
		slaveInterface = org.slaveInterface;
		connValue = org.connValue;
	}
	
	public void connect(Interface intf)
	{
		if(intf.isMaster())
		{
			connectMaster(intf);
		} else {
			connectSlave(intf);
		}
	}
	public void connectMaster(Interface intf)
	{
		if(masterInterface!=null)
		{
			masterInterface.getConnections().remove(this);
		}
		masterInterface = intf;
		masterInterface.getConnections().add(this);
	}
	public void connectSlave(Interface intf)
	{
		if(slaveInterface!=null)
		{
			slaveInterface.getConnections().remove(this);
		}
		slaveInterface = intf;
		slaveInterface.getConnections().add(this);
	}
	public void disconnect() {
		disconnect(masterInterface);
		disconnect(slaveInterface);
	}
	public void disconnect(Interface intf) {
		if(intf!=null) {
			intf.getConnections().remove(this);
			if(slaveInterface==intf) {
				slaveInterface = null;
			}
			if(masterInterface==intf) {
				masterInterface = null;
			}
		}
	}
	public long[] getConnValue()
	{
		return connValue;
	}
	public void setConnValue(long[] val)
	{
		if(getMasterInterface().getPrimaryWidth()==val.length) {
			connValue = val;			
		} else {
			Logger.logln("Passed incorrect connValue parameter!!! Width was " + val.length + " expexted: " + masterInterface.getPrimaryWidth(), LogLevel.ERROR);
		}
	}

	public Interface getMasterInterface() {
		return masterInterface;
	}
	public BasicComponent getMasterModule() {
		if(masterInterface!=null)
		{
			return masterInterface.getOwner();
		} else {
			return null;
		}
	}
	public void setMasterInterface(Interface masterInterface) {
		this.masterInterface = masterInterface;
	}

	public Interface getSlaveInterface() {
		return slaveInterface;
	}
	
	public BasicComponent getSlaveModule() {
		if(slaveInterface!=null)
		{
			return slaveInterface.getOwner();
		} else {
			return null;
		}
	}

	public void setSlaveInterface(Interface slaveInterface) {
		this.slaveInterface = slaveInterface;
	}
	public SystemDataType getType()
	{
		return type;
	}
}
