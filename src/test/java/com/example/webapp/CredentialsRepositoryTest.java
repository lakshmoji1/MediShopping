package com.example.webapp;

import com.example.webapp.entity.Credentials;
import com.example.webapp.entity.Role;
import com.example.webapp.repository.CredentialsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql= false)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
class CredentialsRepositoryTest {
    @Autowired
    private CredentialsRepository repo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testCreateNewUserWithOneRole() {
        Role adminRole = entityManager.find(Role.class, 1);
        Credentials customerCredentials = new Credentials("lakshmoji@gmail.com", "lakshmoji");
        Set<Role> roles = customerCredentials.getRoles();
        roles.add(adminRole);
        repo.save(customerCredentials);

        assertThat(customerCredentials.getCredentialsId()).isPositive();
    }
}
