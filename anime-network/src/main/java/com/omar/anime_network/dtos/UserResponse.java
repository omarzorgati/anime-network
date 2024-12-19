package com.omar.anime_network.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserResponse {
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private Boolean accountLocked;
    private Boolean enabled;
    private String role;
    private String avatar;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private Date createdDate;
}
