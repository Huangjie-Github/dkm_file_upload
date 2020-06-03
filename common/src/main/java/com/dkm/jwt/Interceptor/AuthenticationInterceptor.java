package com.dkm.jwt.Interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import com.dkm.jwt.JwtVerfy;
import com.dkm.jwt.contain.LocalUser;
import com.dkm.jwt.entity.CityJwtBo;
import com.dkm.jwt.entity.DkmFacilityAccountBO;
import com.dkm.jwt.entity.PrivilegeMenuQuery;
import com.dkm.jwt.entity.UserLoginQuery;
import com.dkm.jwt.islogin.CheckToken;
import com.dkm.jwt.islogin.LoginToken;
import com.dkm.utils.StringUtils;
import com.sun.tools.javac.jvm.Code;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;


/**
 * @Author qf
 * @Date 2019/9/24
 * @Version 1.0
 */
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    private LocalUser user;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {

        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //检查是否有LoginToken注释，有则跳过认证
        if (method.isAnnotationPresent(LoginToken.class)) {
            LoginToken loginToken = method.getAnnotation(LoginToken.class);
            if (loginToken.required()) {
                return true;
            }
        }

        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(CheckToken.class)) {
            CheckToken checkToken = method.getAnnotation(CheckToken.class);
            if (checkToken.required()) {
                // 执行认证
                if (StringUtils.isBlank(token)) {
                    throw new ApplicationException(CodeType.OVENDU_ERROR,"token 为空");
                }
                // 获取 token 中的 user信息
//                UserLoginQuery query = new UserLoginQuery();
                DkmFacilityAccountBO facilityAccountBO = new DkmFacilityAccountBO();
                try {
//                    query.setId(JWT.decode(token).getClaim("id").asLong());
//                    query.setAccount(JWT.decode(token).getClaim("userName").asString());
//                    query.setCname(JWT.decode(token).getClaim("cname").asString());
//                    query.setCompanyId(JWT.decode(token).getClaim("companyId").asLong());
//                    query.setMenuList(JWT.decode(token).getClaim("menuList").asList(PrivilegeMenuQuery.class));
//                    query.setRoleName(JWT.decode(token).getClaim("roleName").asString());
//                    query.setTel(JWT.decode(token).getClaim("tel").asString());
//                    query.setStatus(JWT.decode(token).getClaim("status").asInt());
//                    query.setCity(JWT.decode(token).getClaim("city").asList(CityJwtBo.class));

                    facilityAccountBO.setId(JWT.decode(token).getClaim("id").asDouble());
                    facilityAccountBO.setAccountName(JWT.decode(token).getClaim("accountName").asString());
                    facilityAccountBO.setAccountPassWord(JWT.decode(token).getClaim("accountPassWord").asString());
                    facilityAccountBO.setFacilityName(JWT.decode(token).getClaim("facilityName").asString());
                    facilityAccountBO.setFacilityDescription(JWT.decode(token).getClaim("facilityDescription").asString());

                } catch (JWTDecodeException j) {
                    throw new ApplicationException(CodeType.OVENDU_ERROR,"token错误");
                }

                Boolean verify = JwtVerfy.isVerify(token, facilityAccountBO);
                if (!verify) {
                    throw new ApplicationException(CodeType.OVENDU_ERROR, "身份验证失败");
                }

                //获得解密后claims对象
                Date date = new Date();
                Claims jwt = JwtParseUtil.parseJWT(token,facilityAccountBO);

                String audience = jwt.getAudience();
                long erp = Long.parseLong(audience);
                Date erpDate = new Date(erp);
                //判断token时间是否过期
                if (erpDate.before(date)) {
                    throw new ApplicationException(CodeType.OVENDU_ERROR);
                }

                user.setUser(facilityAccountBO);
                return true;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
