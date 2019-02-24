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

import java.util.Date;
import java.util.Vector;

import sopc2dts.Logger;
import sopc2dts.Logger.LogLevel;
import sopc2dts.lib.AvalonSystem;
import sopc2dts.lib.BoardInfo;
import sopc2dts.lib.components.BasicComponent;

/** @brief Base class for all generators
 * 
 * @author Walter Goossens
 *
 */
public abstract class AbstractSopcGenerator {
	/** @brief LGPL Copyright notice for generated sources */
	protected static String copyRightNotice = "/*\n" +
	" * Copyright (C) 2010-2013 Walter Goossens <waltergoossens@home.nl>.\n" +
	" *\n" +
	" * This program is free software; you can redistribute it and/or modify\n" +
	" * it under the terms of the GNU General Public License as published by\n" +
	" * the Free Software Foundation; either version 2 of the License, or\n" +
	" * (at your option) any later version.\n" +
	" *\n" +
	" * This program is distributed in the hope that it will be useful, but\n" +
	" * WITHOUT ANY WARRANTY; without even the implied warranty of\n" +
	" * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE, GOOD TITLE or\n" +
	" * NON INFRINGEMENT.  See the GNU General Public License for more\n" +
	" * details.\n" +
	" *\n" +
	" * You should have received a copy of the GNU General Public License\n" +
	" * along with this program; if not, write to the Free Software\n" +
	" * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.\n" +
	" *\n" +
	" */\n";

	/** @brief AvalonSystem to generate output from */
	protected AvalonSystem sys;
	/** @brief Generate Text or Binary output
	 * 
	 * @see isTextOutput
	 */
	protected boolean generateTextOutput;

	/** @brief Constructor meant for subclasses
	 * 
	 * @param s The AvalonSystem to generate for
	 * @param isText Whether to generate text or binary output
	 */
	protected AbstractSopcGenerator(AvalonSystem s, boolean isText)
	{
		sys = s;
		generateTextOutput = isText;
	}
	

	/** @brief Make a string suitable for use as a define in C
	 * 
	 * In order to automagically generate C defines, this function creates a
	 * version of in that could be used in a define statement.
	 * 
	 * @param in The String to process
	 * @return preprocessor compatible version of in
	 */
	public static String definenify(String in)
	{
		return in.toUpperCase().replace("-", "_").replace(" ", "_");
	}
	
	protected static String getSmallCopyRightNotice(String componentName)
	{
		return getSmallCopyRightNotice(componentName, true);
	}
	/** @brief Get a small personalized copyright message
	 * 
	 * Use this to include in output that does not need to be covered / tainted
	 * by LGPL but just add a notice claiming the origin.
	 * 
	 * @param componentName to use in the message
	 * @param includeTime add timestamp to message. Set to false in order to 
	 * easily compare output files.
	 * @return a small personalized copyright message
	 */
	protected static String getSmallCopyRightNotice(String componentName, boolean includeTime)
	{
		return "/*\n"
			+ " * This " + componentName + " is generated by sopc2dts version " + AvalonSystem.getSopc2DtsVer() + (includeTime ? " on " + new Date().toString() : "") + "\n"
			+ " * Sopc2dts is written by Walter Goossens <waltergoossens@home.nl>\n"
			+ " * in cooperation with the nios2 community <nios2-dev@lists.rocketboards.org>\n"
			+ " */\n";
	}
	
	/** @brief returns true for text-generators
	 * 
	 * @return true if the output is text-based
	 */
	public boolean isTextOutput() {
		return generateTextOutput;
	}

	/** @brief Get textual generation result
	 * 
	 * Subclasses must implement this function to return their textual 
	 * generation result (if any).
	 * For binary generators, those who return false when calling isTextOutput,
	 * this function might return null indicating there is no human-readable 
	 * version of the output.
	 * 
	 * @param bi The BoardInfo to use when generating the output
	 * @return Textual result or null if isTextOutput returns false
	 */
	public abstract String getTextOutput(BoardInfo bi);
	/** @brief Get generation result in binary form
	 * 
	 * This function can be overridden by classes that only support binary. 
	 * 
	 * @param bi The BoardInfo to use when generating the output
	 * @return Binary result data or null on error
	 */
	public byte[] getBinaryOutput(BoardInfo bi)
	{
		String to = getTextOutput(bi);
		if(to!=null)
		{
			return to.getBytes();
		} else {
			return null;
		}
	}
	/** @brief Get the Point Of View component
	 * 
	 * This function tries to find and return the Point Of View component in sys
	 * as specified in the BoardInfo bi. The pov-component is usually a NiosII 
	 * processor but any avalon-master can be the pov-component. A PCI(e) core 
	 * is a nice example.
	 * The pov-component will be automagically searched for if not specified.
	 * First the system is searched for likely pov-components (processors, pci
	 * controllers) but if not found, we'll settle for the first master we can
	 * find. 
	 * Therefore this function should always return a result for any semi-sane 
	 * AvalonSystem.
	 * 
	 * @param bi The BoardInfo to use
	 * @return BasicComponent that should be used as pov or null if no masters 
	 * are found.
	 */
	protected BasicComponent getPovComponent(BoardInfo bi)
	{
		BasicComponent povComp = sys.getComponentByName(bi.getPov());
		if(povComp == null)
		{
			if((bi.getPov() == null) || (bi.getPov().isEmpty()))
			{
				Logger.logln("No point of view specified. Trying to find one.", LogLevel.INFO);
			} else {
				Logger.logln("Point of view: '" + bi.getPov() + "' could not be found. Trying to find another one.", LogLevel.WARNING);
			}
			Vector<BasicComponent> vMasters = sys.getMasterComponents();
			if(vMasters.isEmpty())
			{
				Logger.logln("System appears to not contain any master components!", LogLevel.ERROR);
			} else {
				switch(bi.getPovType())
				{
				case CPU: {
					//Find a CPU
					for(BasicComponent comp : vMasters)
					{
						if(comp.getScd().getGroup().equals("cpu"))
						{
							Logger.logln("Found a cpu of type " + comp.getClassName() + " named " + comp.getInstanceName(), LogLevel.INFO);
							return comp;
						}
					}
				} break;
				case PCI: {
					//First do a strict run
					for(BasicComponent comp : vMasters)
					{
						if(comp.getScd().getGroup().toLowerCase().contains("pci"))
						{
							Logger.logln("Found a master of type " + comp.getClassName() + " named " + comp.getInstanceName(), LogLevel.INFO);
							return comp;
						}
					}
					//Then a weaker one
					for(BasicComponent comp : vMasters)
					{
						if(comp.getClassName().toLowerCase().contains("pci"))
						{
							Logger.logln("Found a master of type " + comp.getClassName() + " named " + comp.getInstanceName(), LogLevel.INFO);
							return comp;
						}
					}
					//Then just return anything remotely matching
					for(BasicComponent comp : vMasters)
					{
						if(comp.getInstanceName().toLowerCase().contains("pci"))
						{
							Logger.logln("Found a master of type " + comp.getClassName() + " named " + comp.getInstanceName(), LogLevel.WARNING);
							return comp;
						}
					}
				} break;
				}
				povComp = vMasters.firstElement();
				Logger.logln("Unable to find a master of type " + bi.getPovType().name() + ". Randomly selecting the first master we find (" + povComp.getInstanceName() + ").", LogLevel.WARNING);
			}
		}
		return povComp;
	}
}