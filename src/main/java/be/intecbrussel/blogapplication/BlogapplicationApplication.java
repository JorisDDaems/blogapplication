package be.intecbrussel.blogapplication;

import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BlogapplicationApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogapplicationApplication.class, args);
    }

//     Saving manualy fake user for login test
/*    @Bean
    CommandLineRunner runner(UserRepository repository){
        return args -> {
            repository.save(new User("fooUser@gmail.com", "foofoo", "yvo", "urvoy"));
        };
    }*/

}
