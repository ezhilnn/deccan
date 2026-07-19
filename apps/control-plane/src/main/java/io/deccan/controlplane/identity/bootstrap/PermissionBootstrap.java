package io.deccan.controlplane.identity.bootstrap;

import io.deccan.controlplane.identity.entity.Permission;
import io.deccan.controlplane.identity.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PermissionBootstrap implements CommandLineRunner {

    private final PermissionRepository permissionRepository;

    @Override
    public void run(String... args) {

        create("workflow.read");
        create("workflow.write");
        create("workflow.execute");

        create("connector.read");
        create("connector.write");

        create("secret.read");
        create("secret.write");

        create("user.read");
        create("user.write");

        create("role.read");
        create("role.write");
    }

    private void create(String permissionName) {

        if (permissionRepository.findByName(permissionName).isPresent()) {
            return;
        }

        Permission permission = new Permission();
        permission.setName(permissionName);

        permissionRepository.save(permission);

    }
}