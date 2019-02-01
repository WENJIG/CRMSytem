package cn.wenjig.crm.service.impl;

import cn.wenjig.crm.data.entity.Employee;
import cn.wenjig.crm.data.entity.JobInfo;
import cn.wenjig.crm.data.entity.Permission;
import cn.wenjig.crm.repository.EmployeeRepository;
import cn.wenjig.crm.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Service
public final class PermissionServiceImpl implements PermissionService, UserDetailsService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    private PermissionServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     *  key : 员工ID, Value : sessionID
     *  Key : sessionID, Value : 员工ID
     *  两个MAP互相维护, 保证登录员工的唯一性。
     */
    private ConcurrentHashMap<Long, String> uidMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Long> sidMap = new ConcurrentHashMap<>();

    /**
     *  Key : 员工id, Value : 职位
     *  一个员工可能对应多个职位
     */
    private ConcurrentHashMap<Long, ConcurrentSkipListSet<JobInfo>> eAjMap = new ConcurrentHashMap<>();

    /**
     *  Key : 职位, Value : 权限
     *  一个职位对应多个权限
     */
    private ConcurrentHashMap<JobInfo, ConcurrentSkipListSet<Permission>> jApMap = new ConcurrentHashMap<>();

    /**
     * @Description: 初始化 所有的用户 权限加载进本类 eAjMap jApMap 容器中
     * ----------------- 可使用NoSQL类型的数据库替换 ( CouchDB , redis , MongoDB , Neo4j , HBase)
     * @param
     * @Return void
     */
    @Override
    public void init() {

    }

    /**
     * @Description: 暂定： 当一个用户 (职员 <--> 职位 <--> 权限) 的 所属权限被修改时 , 重新加载被修改用户的权限集
     * @param
     * @Return void
     */
    @Override
    public void reload() {

    }

    /**
     * @Description: 登录成功以后进行签到, 确认已经登录
     * @param uid, sid
     * @Return void
     */
    @Override
    public void checkIn(long uid, String sid) {
        uidMap.put(uid, sid);
        sidMap.put(sid, uid);
    }

    /**
     * @Description: 登录验证完成后 以及 注销时调用
     * @param sid
     * @Return void
     */
    @Override
    public void logout(String sid) {
        uidMap.remove(sidMap.get(sid));
        sidMap.remove(sid);
    }

    /**
     * @Description: 根据accountId 返回权限集合
     * @param uid, permissionName, level
     * @Return boolean
     */
    private List<SimpleGrantedAuthority> findPermissionByUid(long uid) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (JobInfo jobInfo : eAjMap.get(uid)) {
            for (Permission permission : jApMap.get(jobInfo)) {
                authorities.add(new SimpleGrantedAuthority(permission.getName()));
            }
        }
        return authorities;
    }

    /**
     * @Description: 根据用户名加载用户名和密码, 以及 权限集 (用户所属角色(职位) --> 所拥有的权限)
     * @param s
     * @Return org.springframework.security.core.userdetails.UserDetails
     */
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByName(s);
        if (employee == null) throw new UsernameNotFoundException("用户名不存在");

        /**
         * 这里权限集是取自 本类初始化时 从数据库取出到 map , 也可以在这里直接采用数据库取出
         */
        List<SimpleGrantedAuthority> authorities = findPermissionByUid(employee.getId());
        return new User(employee.getEmail(), employee.getPassword(), authorities);
    }
}
