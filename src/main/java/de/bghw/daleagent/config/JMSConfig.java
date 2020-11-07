package de.bghw.daleagent.config;

import javax.jms.ConnectionFactory;

import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.JmsTransactionManager;

@Configuration
public class JMSConfig {

    @Bean
    public JmsTransactionManager jmsTransactionManager(@Qualifier("jmsConnectionFactory") final ConnectionFactory connectionFactory) {
        JmsTransactionManager jmsTransactionManager = new JmsTransactionManager();
        jmsTransactionManager.setConnectionFactory(connectionFactory);
        return jmsTransactionManager;
    }
   
    @Bean
    public JmsComponent jmsComponent(@Qualifier("jmsConnectionFactory") final ConnectionFactory connectionFactory, final JmsTransactionManager jmsTransactionManager) {
        JmsComponent jmsComponent = JmsComponent.jmsComponentTransacted(connectionFactory, jmsTransactionManager);
        return jmsComponent;
    }
    
    @Bean
    public ServletRegistrationBean camelServletRegistrationBean() {
      ServletRegistrationBean registration = new ServletRegistrationBean(new CamelHttpTransportServlet(), "/camel/*");
      registration.setName("CamelServlet");
      return registration;
    }
}