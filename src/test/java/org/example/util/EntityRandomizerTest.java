package org.example.util;

import org.example.entity.User;
import org.junit.Test;

import static org.example.util.EntityRandomizer.getRandomizedEntity;
import static org.junit.Assert.*;

public class EntityRandomizerTest {

    @Test
    public void getRandomizedEntityTest() throws IllegalAccessException, InstantiationException {
        User user = getRandomizedEntity(User.class);
        System.out.println(user);
    }
}