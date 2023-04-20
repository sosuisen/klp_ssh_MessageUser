package com.example.model;

import javax.ws.rs.FormParam;

@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class UserDTO {
	@FormParam("name")
	private String name;
	@FormParam("role")
	private String role;
	@FormParam("password")
	private String password;
}

