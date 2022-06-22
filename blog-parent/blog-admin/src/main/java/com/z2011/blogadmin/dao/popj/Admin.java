package com.z2011.blogadmin.dao.popj;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * ms_admin
 * @author 
 */
@Data
public class Admin{
    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String password;
}