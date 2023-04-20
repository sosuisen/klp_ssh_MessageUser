package com.example.model;

import java.util.ArrayList;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class Messages extends ArrayList<MessageDTO> {

}
