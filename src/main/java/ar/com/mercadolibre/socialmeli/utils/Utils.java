package ar.com.mercadolibre.socialmeli.utils;

import ar.com.mercadolibre.socialmeli.entity.Product;
import ar.com.mercadolibre.socialmeli.entity.User;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static List<User> createDefaultUsers(){
        List<User> users = new ArrayList<>();

        User user1 = new User(1, "Fernando Baldrich");
        User user2 = new User(2, "Matias Gregorat");
        user2.setFollowedIds(List.of(1));
        User user3 = new User(3, "Stephanie Castillo");
        User user4 = new User(4, "Maria Emilia");
        User user5 = new User(5, "Delfina Glavas");

        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);
        users.add(user5);
        return users;
    }
}
