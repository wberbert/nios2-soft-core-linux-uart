
module NIOS2_Design (
	clk_clk,
	reset_reset_n,
	sdram_0_wire_addr,
	sdram_0_wire_ba,
	sdram_0_wire_cas_n,
	sdram_0_wire_cke,
	sdram_0_wire_cs_n,
	sdram_0_wire_dq,
	sdram_0_wire_dqm,
	sdram_0_wire_ras_n,
	sdram_0_wire_we_n,
	uart_0_external_connection_rxd,
	uart_0_external_connection_txd);	

	input		clk_clk;
	input		reset_reset_n;
	output	[12:0]	sdram_0_wire_addr;
	output	[1:0]	sdram_0_wire_ba;
	output		sdram_0_wire_cas_n;
	output		sdram_0_wire_cke;
	output		sdram_0_wire_cs_n;
	inout	[31:0]	sdram_0_wire_dq;
	output	[3:0]	sdram_0_wire_dqm;
	output		sdram_0_wire_ras_n;
	output		sdram_0_wire_we_n;
	input		uart_0_external_connection_rxd;
	output		uart_0_external_connection_txd;
endmodule
