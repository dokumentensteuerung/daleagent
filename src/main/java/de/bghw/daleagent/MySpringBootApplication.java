package de.bghw.daleagent;
 
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean; 


@SpringBootApplication
public class MySpringBootApplication {

 
    /**
     * A main method to start this application.
     */
    public static void main(String[] args) {
        SpringApplication.run(MySpringBootApplication.class, args);
    }

  @Bean
  DirectExchange exchange() {
      return new DirectExchange("dokumentensteuerung", true, false);
  }
//    @Bean
//    TopicExchange exchange() {
//        return new TopicExchange("dokumentensteuerung", true, false);
//    }

//    @Bean
//    public ApplicationRunner runner(RabbitTemplate template) {
//        return args -> {
//            template.convertAndSend("dale", "Hello, world!");
//        };
//    }

    @Bean
    public Queue myQueue() {
        return new Queue("dokumentensteuerung", true);
    }

    @Bean
    Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("dale");
    }

//    @RabbitListener(queues = "dokumentensteuerung")
//    public void listen(String in) {
//        System.out.println("Message read from dokumentensteuerung : " + in);
//    }



}
