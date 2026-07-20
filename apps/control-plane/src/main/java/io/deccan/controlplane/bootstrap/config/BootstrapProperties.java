package io.deccan.controlplane.bootstrap.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "deccan.bootstrap")
public class BootstrapProperties {

    private boolean enabled;

    private Organization organization = new Organization();

    private Admin admin = new Admin();

    @Getter
    @Setter
    public static class Organization {

        private String name;

        private String slug;

    }

    @Getter
    @Setter
    public static class Admin {

        private String firstName;

        private String lastName;

        private String email;

        private String password;

    }

}