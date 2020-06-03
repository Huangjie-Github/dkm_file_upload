package com.dkm.jwt.Interceptor;

import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import com.dkm.jwt.entity.DkmFacilityAccountBO;
import com.dkm.jwt.entity.UserLoginQuery;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * @Author qf
 * @Date 2019/10/1
 * @Version 1.0
 */
public class JwtParseUtil {

    /**
     * Token的解密
     * @param token 加密后的token
     * @param facilityAccountBO  用户的对象
     * @return
     */
    public static Claims parseJWT(String token, DkmFacilityAccountBO facilityAccountBO) {
        //签名秘钥，和生成的签名的秘钥一模一样

        Double id = facilityAccountBO.getId();

        if (id == null) {
            throw new ApplicationException(CodeType.OVENDU_ERROR,"token有误，请重新登录");
        }

        String key = id.toString();

        //得到DefaultJwtParser
        Claims claims;
        try {
            claims = Jwts.parser()
                    //设置签名的秘钥
                    .setSigningKey(key)
                    //设置需要解析的jwt
                    .parseClaimsJws(token).getBody();
        }catch (Exception e) {
            throw new ApplicationException(CodeType.OVENDU_ERROR,"token错误");
        }

        return claims;
    }
}
