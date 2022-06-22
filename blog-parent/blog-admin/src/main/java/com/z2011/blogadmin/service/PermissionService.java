package com.z2011.blogadmin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.z2011.blogadmin.dao.PageResult;
import com.z2011.blogadmin.dao.Result;
import com.z2011.blogadmin.dao.mapper.PermissionMapper;
import com.z2011.blogadmin.vo.params.PerParam;
import com.z2011.blogadmin.dao.popj.Permission;
import com.z2011.blogadmin.vo.PermissionVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;

    public Result getPagePermissionList(PerParam perParam) {
        IPage<Permission> page = new Page(perParam.getCurrentPage(),perParam.getPageSize());
        LambdaQueryWrapper<Permission> perWrapper = new LambdaQueryWrapper();
        //写逻辑、copy函数、带总数
        //select * from ms_permission order by
        // perWrapper.
        if (StringUtils.isNotBlank(perParam.getQueryString()))
            perWrapper.like(Permission::getName,perParam.getQueryString());
        IPage<Permission> permissionIPage = permissionMapper.selectPage(page, perWrapper);
        PageResult pageResult = new PageResult();
        pageResult.setList(permissionIPage.getRecords());
        pageResult.setTotal(permissionIPage.getTotal());
        return Result.SUCCESS(pageResult);

    }

    public Result add(Permission permission) {
        permissionMapper.insert(permission);
        return Result.SUCCESS(null);
    }

    public Result update(Permission permission) {
        permissionMapper.updateById(permission);
        return Result.SUCCESS(null);
    }

    public Result delete(Long id) {
        permissionMapper.deleteById(id);
        return Result.SUCCESS(null);
    }

    private List<PermissionVo> copyVoList(List<Permission> permissionList) {
        ArrayList<PermissionVo> permissionVoList = new ArrayList();
        for (Permission permission : permissionList) {
            permissionVoList.add(copy(permission));
        }
        return permissionVoList;
    }

    private PermissionVo copy(Permission permission) {
        PermissionVo permissionVo = new PermissionVo();
        BeanUtils.copyProperties(permission, permissionVo);

        return permissionVo;
    }
}