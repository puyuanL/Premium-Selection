package premium.stock.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public DirectExchange stockExchange() {
        return new DirectExchange("stock-exchange", true, false);
    }

    @Bean
    public Queue stockDeductQueue() {
        return QueueBuilder.durable("stock.deduct.queue").build();
    }

    @Bean
    public Binding stockDeductBinding() {
        return BindingBuilder.bind(stockDeductQueue())
                .to(stockExchange())
                .with("stock.deduct");
    }
}