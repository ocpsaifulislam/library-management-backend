package dev.shoaibsuad.library_management.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private String companyName;
    private String version;
    private String supportEmail;

}
