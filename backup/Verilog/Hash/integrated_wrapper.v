`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2021/08/10 23:10:40
// Design Name: 
// Module Name: hash_wrapper
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


module integrated_wrapper(
  input         clock,
  input         reset,
  input         start,
  input  [2:0]  mode,
  output        done,
  output        warning,
  
  input  [127:0] key,
  input  [127:0] nounce,
  input [127:0] tagin,
  input  [127:0] i_wr_data,
  input  [127:0] i_wr_text,
  input         i_wr_en_data,
  input         i_wr_en_text,
  output        o_af_data,
  output        o_full_data,
  output        o_af_text,
  output        o_full_text,
  
  input         i_rd_en,
  output [127:0] o_rd_data,
  output [127:0] tagout,
  output        o_ae,
  output        o_empty,
  output        tag_valid
  );
    
    wire pull;
    wire push;
    wire [127:0] data;
    wire [127:0] text;
    wire [127:0] message;
    wire [127:0] cipher;
    wire ae;
    wire af;
    wire af_en;
    wire af_de;
    wire af_hash;
    
    assign message = ((~mode[2] & mode[0]) & pull) ? data : 
                     ((mode[2] | ~mode[0]) & pull) ? {data[63:0], 64'd0} :
                     ((~mode[2] & mode[0]) & push) ? text : 
                     ((~mode[2] & ~mode[0]) & push) ? {text[63:0], 64'd0} : 128'd0;
                     
    assign af = (mode[2]) ? af_hash : (mode[1]) ? af_de : af_en;
    
    FIFO_v #(2, 128, 4, 1, 2) data_in (
			.data_out(data),
			.data_count(),
			.empty(),
			.full(o_full_data),
			.almst_empty(ae),
			.almst_full(o_af_data),
			.err(),
			.data_in(i_wr_data),
			.wr_en(i_wr_en_data),
			.rd_en(pull),
			.n_reset(~reset),
			.clk(clock)
	);
   
   FIFO_v #(2, 128, 4, 1, 2) text_in (
			.data_out(text),
			.data_count(),
			.empty(af_de),
			.full(o_full_text),
			.almst_empty(af_en),
			.almst_full(o_af_text),
			.err(),
			.data_in(i_wr_text),
			.wr_en(i_wr_en_text),
			.rd_en(push),
			.n_reset(~reset),
			.clk(clock)
	);
    
    ascon ascon_core (
    .clock(clock),
    .reset(reset),
    .io_key(key),
    .io_nounce(nounce),
    .io_tagin(tagin),
    .io_message(message),
    .io_start(start),
    .io_empty(ae),
    .io_full(af),
    .io_mode(mode),
    .io_push(push),
    .io_pull(pull),
    .io_cipher(cipher),
    .io_tagout(tagout),
    .io_done(done),
    .io_warning(warning),
    .io_valid(tag_valid)
    );
    
    
    FIFO_v #(2, 128, 4, 2, 2) fifo_out (
        .data_out(o_rd_data),
        .data_count(),
        .empty(o_empty),
        .full(),
        .almst_empty(o_ae),
        .almst_full(af_hash),
        .err(),
        .data_in(cipher),
        .wr_en(push),
        .rd_en(i_rd_en),
        .n_reset(~reset),
        .clk(clock)
    );
   
   
endmodule
