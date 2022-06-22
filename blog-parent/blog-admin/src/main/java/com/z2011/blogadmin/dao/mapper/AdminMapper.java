package com.z2011.blogadmin.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.z2011.blogadmin.dao.popj.Admin;
import com.z2011.blogadmin.dao.popj.Permission;

import java.util.List;

public interface AdminMapper extends BaseMapper<Admin> {
    public List<Permission> findPermissionByAdminId(Long adminId);

}
