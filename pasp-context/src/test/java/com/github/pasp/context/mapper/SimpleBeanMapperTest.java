package com.github.pasp.context.mapper;

import com.github.pasp.core.IBeanMapper;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/3/22.
 */
public class SimpleBeanMapperTest {
    private IBeanMapper beanMapper = SimpleBeanMapper.INSTANCE;
    @Test
    public void testMapObject(){
        RoleDTO role = new RoleDTO();
        role.setId(1L);
        role.setName("Role1");
        UserDTO user = new UserDTO();
        user.setId(1L);
        user.setName("User1");
        user.setRole(role);
        HashMap<Long, RoleDTO> roleMap = new HashMap<>();
        roleMap.put(1L, role);
        user.setRoleMap(roleMap);
        ArrayList<RoleDTO> roleList = new ArrayList<>();
        roleList.add(role);
        user.setRoleList(roleList);
        User u = beanMapper.map(user, User.class);
        Assert.assertEquals("User1",u.getName());
        Assert.assertTrue(1 == u.getId());
        Assert.assertNotNull(u.getRole());
        Assert.assertEquals("Role1", u.getRole().getName());
        Assert.assertNotNull(u.getRoleMap());
        Assert.assertNotNull(u.getRoleMap().get(1L));
        Assert.assertEquals("Role1", u.getRoleMap().get(1L).getName());
        Assert.assertNotNull(u.getRoleList());
        Assert.assertTrue(1 == u.getRoleList().size());
        Assert.assertEquals("Role1", u.getRoleList().get(0).getName());

    }
}
