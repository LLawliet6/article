package com.LS.article.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private Integer uid;
    private String username;
    private String userPwd;
    private Integer role;
}

