/*
sopc2dts - Devicetree generation for Altera systems

Copyright (C) 2013 Walter Goossens <waltergoossens@home.nl>

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
package sopc2dts.lib.devicetree;

public abstract class DTPropVal {
	enum DTPropType { STRING, NUMBER, BYTE, BOOL, PHANDLE };
	DTPropType type;
	String opening;
	String closing;
	String seperator;
	public DTPropVal(DTPropType t, String open, String close, String sep) {
		type = t;
		opening = open;
		closing = close;
		seperator = sep;
	}
	protected abstract byte[] getValueBytes();
	boolean isTypeCompatible(DTPropType t) {
		if(type == t) {
			return true;
		} else {
			if(((type == DTPropType.NUMBER) && (t == DTPropType.PHANDLE)) ||
					((t == DTPropType.NUMBER) && (type == DTPropType.PHANDLE)))
			{
				return true;
			}
		}
		return false;
	}
}
