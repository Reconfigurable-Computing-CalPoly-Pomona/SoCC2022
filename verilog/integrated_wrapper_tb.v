`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2021/08/10 23:49:27
// Design Name: 
// Module Name: hash_wrapper_tb
// Project Name: 
// Target Devices: 
// Tool Versions: 
// Description: 
// 
// Dependencies: 
// 
// Revision:
// Revision 0.01 - File Created
// Additional Comments:
// 
//////////////////////////////////////////////////////////////////////////////////


module integrated_wrapper_tb();
	reg [127:0] i_wr_data, i_wr_text, key, nounce, tagin;
	reg clock, reset, start, i_wr_en_data, i_wr_en_text, i_rd_en;
	reg [2:0] mode;

	wire [127:0] o_rd_data, tagout;
	wire o_af_data, o_full_data, o_af_text, o_full_text, o_ae, o_empty, done, warning, tag_valid;

	reg [31:0] cycle_count;


    integrated_wrapper m0(
        .clock(clock),
        .reset(reset),
        .start(start),
        .mode(mode),
        .done(done),
        .warning(warning),
        
        .key(key),
        .nounce(nounce), 
        .tagin(tagin),
        .i_wr_data(i_wr_data),
        .i_wr_text(i_wr_text),
        .i_wr_en_data(i_wr_en_data),
        .i_wr_en_text(i_wr_en_text),
        .o_af_data(o_af_data),
        .o_full_data(o_full_data),
        .o_af_text(o_af_text),
        .o_full_text(o_full_text),
          
        .i_rd_en(i_rd_en),
        .o_rd_data(o_rd_data),
        .tagout(tagout),
        .o_ae(o_ae),
        .o_empty(o_empty),
        .tag_valid(tag_valid)
    );
 
    
    always @(posedge clock)
        cycle_count <= cycle_count + 1;
       

	task wait_until_done;
		forever begin : wait_loop
			@(posedge done);
			@(negedge clock);
			if(done) disable wait_until_done;
		end
	endtask

	always begin
		#5;
		clock=~clock;
	end



	initial begin

		clock=0;
		start=1'b0;
        cycle_count=0;
        reset = 1'b1;
        mode = 3'd3;
        key = 128'h000102030405060708090A0B0C0D0E0F;
		nounce = 128'h000102030405060708090A0B0C0D0E0F;
		tagin = 128'h316d7ab17724ba67a85ecd3c0457c459;
        i_wr_data = 128'h00000000000000000000000000000000;
        i_wr_text = 128'h00000000000000000000000000000000;
		i_wr_en_data = 1'b0;
		i_wr_en_text = 1'b0;
		i_rd_en = 1'b0;
		//special cases
		@(negedge clock);
	    reset=1'b0;
//		i_wr_data = 128'h0001020304050607;
//		i_wr_text = 128'h0001020304050607;
//		i_wr_data = 128'h000102030405060708090A0B0C0D0E0F;
//		i_wr_text = 128'h000102030405060708090A0B0C0D0E0F;
      i_wr_data = 128'h000102030405060708090A0B0C0D0E0F;
      i_wr_text = 128'h52499ac9c84323a4ae24eaeccf45c137;
//        i_wr_data = 128'h0001020304050607;
		i_wr_en_data = 1'b1;
		i_wr_en_text = 1'b1;
		@(negedge clock);
//		i_wr_data = 128'h8000000000000000;
//		i_wr_text = 128'h8000000000000000;
//		i_wr_data = 128'h80000000000000000000000000000000;
//		i_wr_text = 128'h80000000000000000000000000000000;
		i_wr_data = 128'h80000000000000000000000000000000;
		i_wr_text = 128'h00000000000000000000000000000000;
//		i_wr_data = 128'h0880000000000000;
//	    i_wr_en_data = 1'b1;
//		i_wr_en_text = 1'b1;
		i_wr_en_data = 1'b1;
		i_wr_en_text = 1'b0;
		@(negedge clock);
		i_wr_data = 128'h00000000000000000000000000000000;
		i_wr_text = 128'h00000000000000000000000000000000;
		i_wr_en_data = 1'b0;
		i_wr_en_text = 1'b0;
		start=1'b1;
		@(negedge clock);
		start=1'b0;
		@(negedge clock);
		@(negedge clock);
		@(negedge clock);
		wait_until_done();
		
		@(negedge clock);
		i_rd_en = 1'b1;
		@(negedge clock);
        @(negedge clock);
        @(negedge clock);
        @(negedge clock);
        i_rd_en = 1'b0;
        @(negedge clock);
		$display("@@@Passed\n");
		$finish;
	end

endmodule

