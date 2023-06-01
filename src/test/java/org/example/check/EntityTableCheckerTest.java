package org.example.check;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class EntityTableCheckerTest {
    @Test
    public void getEntityClass() throws IOException, ClassNotFoundException {
        EntityTableChecker entityTableChecker = new EntityTableChecker();
        entityTableChecker.getEntityClass("user");
    }
}