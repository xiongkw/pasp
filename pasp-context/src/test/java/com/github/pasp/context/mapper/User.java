package com.github.pasp.context.mapper;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/22.
 */
public class User {
    private Long id;

    private String name;

    private Role role;

    private Map<Long, Role> roleMap;

    private List<Role> roleList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Map<Long, Role> getRoleMap() {
        return roleMap;
    }

    public void setRoleMap(Map<Long, Role> roleMap) {
        this.roleMap = roleMap;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }
}
