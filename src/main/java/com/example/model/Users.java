package com.example.model;

import java.util.ArrayList;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class Users extends ArrayList<UserDTO> {

}
