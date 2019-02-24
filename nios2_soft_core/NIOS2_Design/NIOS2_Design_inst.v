	NIOS2_Design u0 (
		.clk_clk                        (<connected-to-clk_clk>),                        //                        clk.clk
		.reset_reset_n                  (<connected-to-reset_reset_n>),                  //                      reset.reset_n
		.sdram_0_wire_addr              (<connected-to-sdram_0_wire_addr>),              //               sdram_0_wire.addr
		.sdram_0_wire_ba                (<connected-to-sdram_0_wire_ba>),                //                           .ba
		.sdram_0_wire_cas_n             (<connected-to-sdram_0_wire_cas_n>),             //                           .cas_n
		.sdram_0_wire_cke               (<connected-to-sdram_0_wire_cke>),               //                           .cke
		.sdram_0_wire_cs_n              (<connected-to-sdram_0_wire_cs_n>),              //                           .cs_n
		.sdram_0_wire_dq                (<connected-to-sdram_0_wire_dq>),                //                           .dq
		.sdram_0_wire_dqm               (<connected-to-sdram_0_wire_dqm>),               //                           .dqm
		.sdram_0_wire_ras_n             (<connected-to-sdram_0_wire_ras_n>),             //                           .ras_n
		.sdram_0_wire_we_n              (<connected-to-sdram_0_wire_we_n>),              //                           .we_n
		.uart_0_external_connection_rxd (<connected-to-uart_0_external_connection_rxd>), // uart_0_external_connection.rxd
		.uart_0_external_connection_txd (<connected-to-uart_0_external_connection_txd>)  //                           .txd
	);

