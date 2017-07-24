package com.github.pasp.context.mapper;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/22.
 */
public class UserDTO {
    private Long id;

    private String name;

    private RoleDTO role;

    private Map<Long, RoleDTO> roleMap;

    private List<RoleDTO> roleList;

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

    public RoleDTO getRole() {
        return role;
    }

    public void setRole(RoleDTO role) {
        this.role = role;
    }

    public Map<Long, RoleDTO> getRoleMap() {
        return roleMap;
    }

    public void setRoleMap(Map<Long, RoleDTO> roleMap) {
        this.roleMap = roleMap;
    }

    public List<RoleDTO> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<RoleDTO> roleList) {
        this.roleList = roleList;
    }
}
