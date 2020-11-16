package de.bghw.daleagent.config;


import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;


@Configuration
public class QueueConfig {

    /*    @Bean
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
        */
	//Servlet Registrieren, damit wir Rest-Services unter /camel/xxx aufrufen können.
    @Bean
    public ServletRegistrationBean camelServletRegistrationBean() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new CamelHttpTransportServlet(), "/camel/*");
        registration.setName("CamelServlet");
        return registration; 
    }
    

    @Bean
    DirectExchange exchange() {
        return new DirectExchange("dokumentensteuerung", true, false);
    }
//      @Bean
//      TopicExchange exchange() {
//          return new TopicExchange("dokumentensteuerung", true, false);
//      }

    
//      @Bean
//      public ApplicationRunner runner(RabbitTemplate template) {
//          return args -> {
//              template.convertAndSend("dale", "Hello, world!");
//          };
//      }
    //Zipkin-Queue automatich anlegen
    @Bean
    @Primary
    public Queue myZipkinQueue() {
        return new Queue("zipkin", true);
    }
    
      @Bean
      public Queue myQueue() {
          return new Queue("dokumentensteuerung", true);
      }

      @Bean
      Binding binding(Queue queue, DirectExchange exchange) {
          return BindingBuilder.bind(queue).to(exchange).with("dale");
      }

      //Queue
//      @RabbitListener(queues = "dokumentensteuerung")
//      public void listen(String in) {
//          System.out.println("Message read from dokumentensteuerung : " + in);
//      }
}