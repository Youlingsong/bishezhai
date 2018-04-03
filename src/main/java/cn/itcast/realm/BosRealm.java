package cn.itcast.realm;

import cn.itcast.domain.Teacher;
import cn.itcast.service.TeacherService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

public class BosRealm extends AuthorizingRealm {
    @Autowired
    TeacherService teacherService;
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("shiro 授权管理...");
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        Subject subject = SecurityUtils.getSubject();
        //从Subject里拿到当前用户，在认证的时候保存在Subject里了


        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {


     UsernamePasswordToken usernamePasswordToken=(UsernamePasswordToken)authenticationToken;
        System.out.println("shiro 认证管理... ");
        String username = usernamePasswordToken.getUsername();
        // 根据用户名 查询 用户信息
        Teacher teacher = teacherService.findByUsername(username);
        System.out.println(teacher+"---");
        if (teacher == null) {
            // 用户名不存在
            // 返回null
            return null;
        }else{
            // 用户名存在
            // 参数一： 期望登录后，将用户信息保存在Subject中的信息
            // 参数二： 当返回用户密码时，securityManager安全管理器，自动比较返回密码和用户输入密码是否一致
            //       如果密码一致 登录成功， 如果密码不一致 报密码错误异常
            // 参数三 ：realm名称
            return new SimpleAuthenticationInfo(teacher,teacher.getPassword(),getName());
        }

    }
}
