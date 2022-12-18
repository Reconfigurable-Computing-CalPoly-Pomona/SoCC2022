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
	reg [127:0] data_wr, text_wr, key, nounce, tagin;
	reg clock, reset, start, data_wr_en, text_wr_en, result_rd_en;
	reg [2:0] mode;

	wire [127:0] result, tagout;
	wire data_afull, data_full, text_afull, text_full, result_aempty, result_empty, done, warning, tag_valid;

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
        .data_wr(data_wr),
        .text_wr(text_wr),
        .data_wr_en(data_wr_en),
        .text_wr_en(text_wr_en),
        .data_afull(data_afull),
        .data_full(data_full),
        .text_afull(text_afull),
        .text_full(text_full),
          
        .result_rd_en(result_rd_en),
        .result(result),
        .tagout(tagout),
        .result_aempty(result_aempty),
        .result_empty(result_empty),
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
        data_wr = 128'h00000000000000000000000000000000;
        text_wr = 128'h00000000000000000000000000000000;
		data_wr_en = 1'b0;
		text_wr_en = 1'b0;
		result_rd_en = 1'b0;
		//special cases
		@(negedge clock);
	    reset=1'b0;
//		data_wr = 128'h0001020304050607;
//		text_wr = 128'h0001020304050607;
//		data_wr = 128'h000102030405060708090A0B0C0D0E0F;
//		text_wr = 128'h000102030405060708090A0B0C0D0E0F;
        data_wr = 128'h000102030405060708090A0B0C0D0E0F;
        text_wr = 128'h52499ac9c84323a4ae24eaeccf45c137;
//      data_wr = 128'h0001020304050607;
		data_wr_en = 1'b1;
		text_wr_en = 1'b1;
		@(negedge clock);
//		data_wr = 128'h8000000000000000;
//		text_wr = 128'h8000000000000000;
//		data_wr = 128'h80000000000000000000000000000000;
//		text_wr = 128'h80000000000000000000000000000000;
		data_wr = 128'h80000000000000000000000000000000;
		text_wr = 128'h00000000000000000000000000000000;
//		data_wr = 128'h0880000000000000;
//	    data_wr_en = 1'b1;
//		text_wr_en = 1'b1;
		data_wr_en = 1'b1;
		text_wr_en = 1'b0;
		@(negedge clock);
		data_wr = 128'h00000000000000000000000000000000;
		text_wr = 128'h00000000000000000000000000000000;
		data_wr_en = 1'b0;
		text_wr_en = 1'b0;
		start=1'b1;
		@(negedge clock);
		start=1'b0;
		@(negedge clock);
		@(negedge clock);
		@(negedge clock);
		wait_until_done();
		
		@(negedge clock);
		result_rd_en = 1'b1;
		@(negedge clock);
        @(negedge clock);
        @(negedge clock);
        @(negedge clock);
        result_rd_en = 1'b0;
        @(negedge clock);
		$display("@@@Passed\n");
		$finish;
	end

endmodule

