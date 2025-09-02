package com.dinhnguyendev.springsecuritysection1.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


public record LoginRequestDto(String username, String password){

}