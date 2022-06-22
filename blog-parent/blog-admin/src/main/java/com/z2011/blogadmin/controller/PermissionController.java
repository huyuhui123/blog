package com.z2011.blogadmin.controller;

import com.z2011.blogadmin.dao.Result;
import com.z2011.blogadmin.dao.popj.Permission;
import com.z2011.blogadmin.vo.params.PerParam;
import com.z2011.blogadmin.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    /**
     * currentPage: 1
     * pageSize: 10
     * queryString: null
     * total: 0
     * @return
     */
    @PostMapping("/permission/permissionList")
    public Result getPermissionList(@RequestBody PerParam perParam){
        return permissionService.getPagePermissionList(perParam);
    }
    @PostMapping("/permission/add")
    public Result add(@RequestBody Permission perParam){
        return permissionService.add(perParam);
    }
    @PostMapping("/permission/update")
    public Result update(@RequestBody Permission perParam){
        return permissionService.update(perParam);
    }
    @GetMapping("permission/delete/{id}")
    public Result delete(@PathVariable Long id){
        return permissionService.delete(id);
    }
}