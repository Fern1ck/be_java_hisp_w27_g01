package ar.com.mercadolibre.socialmeli.util;

import ar.com.mercadolibre.socialmeli.dto.response.ProductResponseDTO;
import ar.com.mercadolibre.socialmeli.entity.Post;
import ar.com.mercadolibre.socialmeli.entity.Product;
import ar.com.mercadolibre.socialmeli.entity.User;
import ar.com.mercadolibre.socialmeli.utils.Utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestUtils {

    public static User createUsersWithPost() {
        Post post = new Post();
        post.setPostId(1);
        post.setDate(LocalDate.now().minusDays(1));
        post.setProduct(Utils.changeDtoToEntity(new ProductResponseDTO(), Product.class));
        post.setCategory(1);
        post.setPrice(100.0);

        User user = new User();
        user.setUserId(1);
        user.setFollowedIds(Arrays.asList(2, 3));
        user.setPosts(Collections.singletonList(post));

        return user;
    }

    public static User createUserWithFollowed() {

        User user = new User(1, "Maria");
        List<Integer> followed= new ArrayList<>();
        followed.add(2);
        followed.add(3);
        user.setFollowedIds(followed);
        return user;
    }

    public static List<User> createUsersWithPosts() {

        Product product1 = new Product(1, "Silla gamer", "Gamer",  "Racer", "Red", "Special Edition");
        Post post1 = new Post(1, product1, LocalDate.of(2024, 9, 28), 100, 15000.00, false, 0.0 );

        Product product2 = new Product(3, "Monitor 4K", "Monitor", "Samsung", "Negro", "Ultra HD");
        Post post2 = new Post(2, product2, LocalDate.of(2024, 9, 27), 300, 30000.00, true, 0.3);

        Product product3 = new Product(2, "Teclado mecánico", "Periférico", "Logitech", "Negro", "RGB");
        Post post3 = new Post(1, product3, LocalDate.of(2024, 9, 29), 200, 5000.00, false, 0.0 );

        Product product4 = new Product(2, "Teclado mecánico", "Periférico", "Logitech", "Negro", "RGB");
        Post post4 = new Post(1, product4, LocalDate.of(2020, 9, 26), 200, 5000.00, false, 0.0 );

        // User 1 tiene 2 post
        User user1 = new User();
        user1.setUserId(1);
        user1.setPosts(Arrays.asList(post1, post2));

        // User 2 tiene 1 post
        User user2 = new User();
        user2.setUserId(2);
        user2.setFollowedIds(Collections.singletonList(3));
        user2.setPosts(Collections.singletonList(post3));


        User user3 = new User();
        user3.setUserId(3);
        user3.setFollowedIds(List.of(1));
        user2.setPosts(Collections.singletonList(post4));

        return Arrays.asList(user1, user2, user3);
    }
}
