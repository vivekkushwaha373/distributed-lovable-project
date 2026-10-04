package com.agenticIde.distributed_lovable.intelligence_service.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Bean
    ChatClient getChatClient(ChatClient.Builder builder){
         return builder.build();
    }

}
