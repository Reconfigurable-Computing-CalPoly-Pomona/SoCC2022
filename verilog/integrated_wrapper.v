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
  input  [127:0] data_wr,
  input  [127:0] text_wr,
  input         data_wr_en,
  input         text_wr_en,
  output        data_afull,
  output        data_full,
  output        text_afull,
  output        text_full,
  
  input         result_rd_en,
  output [127:0] result,
  output [127:0] tagout,
  output        result_aempty,
  output        result_empty,
  output        tag_valid
  );
    
    wire pull;
    wire push;
    wire [127:0] data;
    wire [127:0] text;
    wire [127:0] message;
    wire [127:0] cipher;
    wire io_empty;
    wire io_full;
    wire text_afull;
    wire text_empty;
    wire text_aempty;
    wire data_empty;
    wire data_aempty;
    wire result_afull;
    
    assign message = ((~mode[2] & mode[0]) & pull & ~data_empty) ? data : 
                     ((mode[2] | ~mode[0]) & pull & ~data_empty) ? {data[63:0], 64'd0} :
                     ((~mode[2] & mode[0]) & push & ~text_empty) ? text : 
                     ((~mode[2] & ~mode[0]) & push & ~text_empty) ? {text[63:0], 64'd0} : 128'd0;
                     
    assign io_full = (mode[2]) ? result_afull : (mode[1]) ? text_empty : text_aempty;
    assign io_empty = data_aempty;
    
    FIFO_v #(2, 128, 4, 1, 2) data_in (
			.data_out(data),
			.data_count(),
			.empty(data_empty),
			.full(data_full),
			.almst_empty(data_aempty),
			.almst_full(data_afull),
			.err(),
			.data_in(data_wr),
			.wr_en(data_wr_en),
			.rd_en(pull),
			.n_reset(~reset),
			.clk(clock)
	);
   
   FIFO_v #(2, 128, 4, 1, 2) text_in (
			.data_out(text),
			.data_count(),
			.empty(text_empty),
			.full(text_full),
			.almst_empty(text_aempty),
			.almst_full(text_afull),
			.err(),
			.data_in(text_wr),
			.wr_en(text_wr_en),
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
            .io_empty(io_empty),
            .io_full(io_full),
            .io_mode(mode),
            .io_push(push),
            .io_pull(pull),
            .io_cipher(cipher),
            .io_tagout(tagout),
            .io_done(done),
            .io_warning(warning),
            .io_valid(tag_valid)
    );
    
    
    FIFO_v #(2, 128, 4, 2, 2) cipher_out (
        .data_out(result),
        .data_count(),
        .empty(result_empty),
        .full(),
        .almst_empty(result_aempty),
        .almst_full(result_afull),
        .err(),
        .data_in(cipher),
        .wr_en(push),
        .rd_en(result_rd_en),
        .n_reset(~reset),
        .clk(clock)
    );
   
   
endmodule
