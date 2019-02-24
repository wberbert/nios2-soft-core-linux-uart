/*
sopc2dts - Devicetree generation for Altera systems

Copyright (C) 2011-2013 Walter Goossens <waltergoossens@home.nl>

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
package sopc2dts.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import sopc2dts.generators.GeneratorFactory.GeneratorType;
import sopc2dts.lib.AvalonSystem;
import sopc2dts.lib.BoardInfo;

public class Sopc2DTSGui extends JFrame {
	private static final long serialVersionUID = 8613192420393857538L;
	JTabbedPane jtp = new JTabbedPane();
	LogPanel pnlLog = new LogPanel();
	InputPanel pnlInput;
	BoardInfoPanel pnlBoardInfo;
	OutputPanel pnlOutput;
	private AvalonSystem sys;
	private BoardInfo boardInfo;
	
	public Sopc2DTSGui(String inpFile, BoardInfo bInfo) {
		super("Sopc2DTS");
		boardInfo = bInfo;
		pnlInput = new InputPanel(inpFile, this);
		pnlBoardInfo = new BoardInfoPanel(
				(bInfo.getSourceFile() == null ? null : bInfo.getSourceFile().getName()), this);
		pnlOutput = new OutputPanel(GeneratorType.DTS, sys, boardInfo);
		jtp.addTab("Input", pnlInput);
		jtp.addTab("Boardinfo", pnlBoardInfo);
		jtp.addTab("Output", pnlOutput);
		pnlLog.setPreferredSize(new Dimension(800,200));
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(pnlLog, BorderLayout.SOUTH);
		this.getContentPane().add(jtp, BorderLayout.CENTER);
		this.pack();
	}
	public void setSys(AvalonSystem sys) {
		this.sys = sys;
		pnlBoardInfo.setBoardInfoAndSys(boardInfo, sys);
		pnlOutput.setSys(sys);
	}
	public AvalonSystem getSys() {
		return sys;
	}
	public BoardInfo getBoardInfo() {
		return boardInfo;
	}
	public void setBoardInfo(BoardInfo boardInfo) {
		this.boardInfo = boardInfo;
		pnlOutput.setBoardInfo(boardInfo);
		pnlBoardInfo.setBoardInfoAndSys(boardInfo, sys);		
	}
}
