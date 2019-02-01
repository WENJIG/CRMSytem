package cn.wenjig.crm.service;

import org.springframework.stereotype.Service;

/**
  * @Description: 系统权限管理服务
  */
@Service
public interface PermissionService {

    void init();

    void checkIn(long uid, String sid);

    void logout(String sid);

    void reload();

}
