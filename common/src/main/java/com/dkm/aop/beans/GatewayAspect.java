package com.dkm.aop.beans;

import com.dkm.handle.ApplicationAdviceHandle;
import com.dkm.handle.GlobalResponseHandler;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

/**
 * @author qf
 * @date 2020/3/6
 * @vesion 1.0
 **/
public abstract class GatewayAspect extends SpringBootServletInitializer {

   @Bean
   public GlobalResponseHandler getGlobalResponseHandler() {
      return new GlobalResponseHandler();
   }

   @Bean
   public ApplicationAdviceHandle getApplicationAdviceHandle() {
      return new ApplicationAdviceHandle();
   }
}
