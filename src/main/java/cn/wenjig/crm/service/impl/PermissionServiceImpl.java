package cn.wenjig.crm.service.impl;

import cn.wenjig.crm.data.entity.Employee;
import cn.wenjig.crm.data.entity.JobInfo;
import cn.wenjig.crm.data.entity.Permission;
import cn.wenjig.crm.repository.EmployeeRepository;
import cn.wenjig.crm.repository.JobInfoRepository;
import cn.wenjig.crm.repository.PermissionRepository;
import cn.wenjig.crm.service.PermissionService;
import cn.wenjig.crm.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Service
public final class PermissionServiceImpl implements PermissionService, UserDetailsService, AuthenticationProvider {

    private final EmployeeRepository employeeRepository;
    private final JobInfoRepository jobInfoRepository;
    private final PermissionRepository permissionRepository;
    private final SessionRegistry sessionRegistry;

    @Autowired
    private PermissionServiceImpl(EmployeeRepository employeeRepository, JobInfoRepository jobInfoRepository, PermissionRepository permissionRepository, SessionRegistry sessionRegistry) {
        this.employeeRepository = employeeRepository;
        this.jobInfoRepository = jobInfoRepository;
        this.permissionRepository = permissionRepository;
        this.sessionRegistry = sessionRegistry;
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
        try {
            /**
             * 加载 employee <---> job 关系 eAjMap
             */
            long startTime = System.currentTimeMillis();
            System.out.println("-------------------------> 正在初始化权限管理器(内存)");
            for (Employee employee : employeeRepository.findAll()) {
                ConcurrentSkipListSet<JobInfo> jobInfoSet = new ConcurrentSkipListSet<>(
                        // lambda 表达式 等价于下面的注释(匿名内部类)
                        (JobInfo o1, JobInfo o2) -> o1.equals(o2) ? 0 : -1
                );

                /*
                            new Comparator<JobInfo>() {
                        @Override
                        public int compare(JobInfo o1, JobInfo o2) {
                            return o1.equals(o2) ? 0 : -1;
                        }
                    });
                    */
                /**
                 * 加载 job <---> permission 关系 jApMap
                 */
                for (long jobId : jobInfoRepository.findJobByEmployeeId(employee.getId())) {
                    JobInfo job = jobInfoRepository.findById(jobId);
                    jobInfoSet.add(job);

                    ConcurrentSkipListSet<Permission> permissionSet = new ConcurrentSkipListSet<>(
                            (Permission p1, Permission p2) -> p1.equals(p2) ? 0 : -1
                    );
                    for (long permissionId : permissionRepository.findByJobId(jobId)) {
                        Permission permission = permissionRepository.findById(permissionId);
                        permissionSet.add(permission);
                    }
                    jApMap.put(job, permissionSet);
                }
                eAjMap.put(employee.getId(), jobInfoSet);
            }
            long endTime = System.currentTimeMillis();
            System.out.println("-------------------------> 权限管理器(内存) 初始化完成，用时: " + (endTime - startTime) + "ms");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("-------------------------> 权限集 初始化错误！");
            System.exit(0);
        }
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
     * @Description: 是否已经登录
     * @param uid
     * @Return boolean
     */
    @Override
    public boolean isLogin(long uid) {
        return uidMap.containsKey(uid) && sidMap.containsKey(uidMap.get(uid));
    }

    /**
     * @Description: 登录成功以后进行签到, 将已经认证的 sessionId 与 uid 保存
     * @param uid, sid
     * @Return void
     */
    @Override
    public void checkIn(long uid, String sid) {
        if (isLogin(uid)) {
            logout(uid);
        }
        uidMap.put(uid, sid);
        sidMap.put(sid, uid);
    }

    /**
     * @Description: 用于踢出用户
     * @param uid
     * @Return void
     */
    @Override
    public void logout(long uid) {

        List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
        for (Object allPrincipal : allPrincipals) {
            User user = (User) allPrincipal;
            if (user.getUsername().equals(employeeRepository.findById(uid).getEmail())) {
                List<SessionInformation> allSessions = sessionRegistry.getAllSessions(user, false);
                if (allSessions != null) {
                    for (SessionInformation sessionInformation : allSessions) {
                        // 这里只需要让该session到期就可以了 ConcurrentSessionFilter会执行用户注销时的步骤
                        sessionInformation.expireNow();
                        //sessionRegistry.removeSessionInformation(sessionInformation.getSessionId());
                    }
                }
            }
        }

        sidMap.remove(uidMap.get(uid));
        uidMap.remove(uid);

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
     * @Description: 自定义用户信息来源服务 包括 用户名 密码 权限集 (用户所属角色(职位) --> 所拥有的权限)
     * @param s
     * @Return org.springframework.security.core.userdetails.UserDetails
     */
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByName(s);
        if (employee == null || employee.getWorkStatus() != 1) throw new UsernameNotFoundException("用户名不存在");

        /**
         * 这里权限集是取自 本类初始化时 从数据库取出到 map , 也可以在这里直接采用数据库取出
         */
        List<SimpleGrantedAuthority> authorities = findPermissionByUid(employee.getId());
        return new User(employee.getEmail(), "{noop}" + employee.getPassword(), authorities);
    }

    /**
     * @Description: 自定义验证服务
     *               P:权限验证服务可配置多个, 然后根据投票规则(可自定义, 一票通过或者一票否决, 少数服从多数等)
     *               来决定是否通过验证, 我这里只自定义了一个(本类)
     *               使用场景: 外部账户, Token 之类的需要支持其他验证方式时
     * @param authentication
     * @Return org.springframework.security.core.Authentication
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String account = authentication.getName();
        String password = (String) authentication.getCredentials();
        UserDetails user = loadUserByUsername(account);
        if (!("{noop}" + MD5Util.md5Encode(password)).equals(user.getPassword())) throw new BadCredentialsException("密码错误");

        // 执行session 会话管理, 如果没有登录的将信息添加, 将已经登录的踢出, 禁止重复登录
        String sessionId = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest().getSession().getId();
        checkIn(employeeRepository.findByName(account).getId(),sessionId);


        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        return new UsernamePasswordAuthenticationToken(user, password, authorities);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
