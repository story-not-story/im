package com.example.im.service.impl;

import com.example.im.dao.LoginDao;
import com.example.im.entity.Login;
import com.example.im.service.LoginService;
import com.example.im.util.KeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author HuJun
 * @date 2020/4/11 6:22 下午
 */
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private LoginDao loginDao;
    public static final String UNKNOWN = "unknown";
    @Override
    public Login save(String userId, Boolean status, HttpServletRequest request) {
        Login login = new Login();
        login.setIp(getIpAddr(request));
        login.setStatus(status);
        login.setUserId(userId);
        login.setPort(request.getRemotePort());
        login.setId(KeyUtil.getUniqueKey());
        return loginDao.save(login);
    }

    @Override
    public boolean isLogin(String userId) {
        Login login = loginDao.findTopByUserId(userId);
        return login != null && login.getStatus();
    }

    public String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        int ch = ',';
        if (ip.indexOf(ch) > 0) {
            ip = ip.substring(0, ip.indexOf(ch));
        }
        return ip;
    }
}
