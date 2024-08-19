package org.example.configuration;

import org.example.utils.CryptoTool;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NodeConfiguration {
    @Value("${salt}")
    private String salt;

    @Bean
    public CryptoTool getCryptoTool() {
        return new CryptoTool(salt);
    }

    @Bean
    public Hashids getHashids() {
        return new Hashids(salt, 10);
    }
}
