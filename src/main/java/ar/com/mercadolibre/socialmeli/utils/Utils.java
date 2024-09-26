package ar.com.mercadolibre.socialmeli.utils;

import ar.com.mercadolibre.socialmeli.entity.Post;
import ar.com.mercadolibre.socialmeli.entity.Product;
import ar.com.mercadolibre.socialmeli.entity.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class Utils {
    public static List<User> createDefaultUsers(){
        List<User> users = new ArrayList<>();

        User user1 = new User(1, "Fernando Baldrich");
        User user2 = new User(2, "Matias Gregorat");
        user2.setFollowedIds(List.of(4));
        User user3 = new User(3, "Stephanie Castillo");
        User user4 = new User(4, "Maria Emilia");
        User user5 = new User(5, "Delfina Glavas");
        user5.setFollowedIds(List.of(2, 4));

        Product product1 = new Product(1, "Silla gamer", "Gamer",  "Racer", "Red", "Special Edition");
        Post post1 = new Post(1, product1, LocalDate.of(2021, 6, 6), 100, 15000.00, false, 0.0 );


        Product product2 = new Product(2, "Teclado mecánico", "Periférico", "Logitech", "Negro", "RGB");
        Post post2 = new Post(2, product2, LocalDate.of(2021, 7, 7), 200, 5000.00, false, 0.0 );

        Product product3 = new Product(3, "Monitor 4K", "Monitor", "Samsung", "Negro", "Ultra HD");
        Post post3 = new Post(3, product3, LocalDate.of(2021, 8, 18), 300, 30000.00, true, 0.3);


        user2.setPosts(List.of(post1, post3));
        user4.setPosts(List.of(post2));

        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);
        users.add(user5);
        return users;
    }

    public static <T> void replaceIf(List<T> list, Predicate<? super T> pred, UnaryOperator<T> op) {
        list.replaceAll(t -> pred.test(t) ? op.apply(t) : t);
    }
}
