package de.bghw.daleagent;

import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;


@SpringBootApplication
public class MySpringBootApplication {

    private TopicExchange exchange;

    /**
     * A main method to start this application.
     */
    public static void main(String[] args) {
        SpringApplication.run(MySpringBootApplication.class, args);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange("dokumentensteuerung", true, false);
    }

    @Bean
    public ApplicationRunner runner(RabbitTemplate template) {
        return args -> {
            template.convertAndSend("dale", "Hello, world!");
        };
    }

    @Bean
    public Queue myQueue() {
        return new Queue("dokumentensteuerung", true);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("dale");
    }

    @RabbitListener(queues = "dokumentensteuerung")
    public void listen(String in) {
        System.out.println("Message read from dokumentensteuerung : " + in);
    }



}
