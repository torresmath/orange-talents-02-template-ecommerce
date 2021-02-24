package com.zup.mercadolivre.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {

    private JPAUtil() {
    }

    private static final EntityManagerFactory factory =
            Persistence.createEntityManagerFactory("tests");

    public static EntityManager getEntityManager() {
        return factory.createEntityManager();
    }
}
