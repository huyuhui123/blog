package com.z2011.blogapi.vo.params;

import lombok.Data;

@Data
public class LoginParam {
    private String account;
    private String password;
    private String nickname;
}
