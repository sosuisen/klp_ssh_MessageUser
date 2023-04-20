package com.example.model;

import javax.ws.rs.FormParam;

// setter/getterその他を追加
@lombok.Data
// MessageDAOクラス内では、new でMessageDTOのインスタンスを作りたいので、
// 全フィールドを引数にもつコンストラクタを追加
@lombok.AllArgsConstructor
// 上記のようにコンストラクタを明示的に追加した場合に限り、
// @BeanParam 指定の際に必要なデフォルトコンストラクタ（NoArgsConstructor）も
// 明示的に追加する必要があります。
@lombok.NoArgsConstructor
public class MessageDTO {
	@FormParam("id")
	private int id;
	@FormParam("name")
	private String name;
	@FormParam("message")
	private String message;
}

