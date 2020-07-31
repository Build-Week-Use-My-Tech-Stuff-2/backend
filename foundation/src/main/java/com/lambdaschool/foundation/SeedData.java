package com.lambdaschool.foundation;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import com.lambdaschool.foundation.models.*;
import com.lambdaschool.foundation.services.ItemService;
import com.lambdaschool.foundation.services.RoleService;
import com.lambdaschool.foundation.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Locale;

/**
 * SeedData puts both known and random data into the database. It implements CommandLineRunner.
 * <p>
 * CoomandLineRunner: Spring Boot automatically runs the run method once and only once
 * after the application context has been loaded.
 */
@Transactional
@Component
public class SeedData
        implements CommandLineRunner
{
    /**
     * Connects the Role Service to this process
     */
    @Autowired
    RoleService roleService;

    /**
     * Connects the item Service to this process
     */
    @Autowired
    ItemService itemService;

    /**
     * Connects the user service to this process
     */
    @Autowired
    UserService userService;

    /**
     * Generates test, seed data for our application
     * First a set of known data is seeded into our database.
     * Second a random set of data using Java Faker is seeded into our database.
     * Note this process does not remove data from the database. So if data exists in the database
     * prior to running this process, that data remains in the database.
     *
     * @param args The parameter is required by the parent interface but is not used in this process.
     */
    @Transactional
    @Override
    public void run(String[] args)
            throws
            Exception
    {
        Role r1 = new Role("admin");
        Role r2 = new Role("lender");
        Role r3 = new Role("user");

        r1 = roleService.save(r1);
        r2 = roleService.save(r2);
        r3 = roleService.save(r3);

        // admin, lender, user
        ArrayList<UserRoles> admins = new ArrayList<>();
        admins.add(new UserRoles(new User(),
                                 r1));
        admins.add(new UserRoles(new User(),
                                 r2));
        admins.add(new UserRoles(new User(),
                                 r3));
        User u1 = new User("admin",
                           "password",
                           "admin@lambdaschool.local",
                           admins);
        u1.getUseremails()
                .add(new Useremail(u1,
                                   "admin@email.local"));
        u1.getUseremails()
                .add(new Useremail(u1,
                                   "admin@mymail.local"));

        userService.save(u1);

        // lender, user
        ArrayList<UserRoles> lenders = new ArrayList<>();
        lenders.add(new UserRoles(new User(),
                                r3));
        lenders.add(new UserRoles(new User(),
                                r2));
        User u2 = new User("cinnamon",
                           "1234567",
                           "cinnamon@lambdaschool.local",
                           lenders);
        u2.getUseremails()
                .add(new Useremail(u2,
                                   "cinnamon@mymail.local"));
        u2.getUseremails()
                .add(new Useremail(u2,
                                   "hops@mymail.local"));
        u2.getUseremails()
                .add(new Useremail(u2,
                                   "bunny@email.local"));
        userService.save(u2);

        // user
        ArrayList<UserRoles> users = new ArrayList<>();
        users.add(new UserRoles(new User(),
                                r2));
        User u3 = new User("barnbarn",
                           "ILuvM4th!",
                           "barnbarn@lambdaschool.local",
                           users);
        u3.getUseremails()
                .add(new Useremail(u3,
                                   "barnbarn@email.local"));
        userService.save(u3);

        users = new ArrayList<>();
        users.add(new UserRoles(new User(),
                                r2));
        User u4 = new User("puttat",
                           "password",
                           "puttat@school.lambda",
                           users);
        userService.save(u4);

        users = new ArrayList<>();
        users.add(new UserRoles(new User(),
                                r2));
        User u5 = new User("misskitty",
                           "password",
                           "misskitty@school.lambda",
                           users);
        userService.save(u5);

        // using JavaFaker create a bunch of regular users
        // https://www.baeldung.com/java-faker
        // https://www.baeldung.com/regular-expressions-java

        FakeValuesService fakeValuesService = new FakeValuesService(new Locale("en-US"),
                new RandomService());
        Faker faker = new Faker(new Locale("en-US"));




        for (int i = 0; i < 5; i++) {
            Item fakeitem;
            String url = "https://picsum.photos/seed/" + faker.pokemon().name() + "/300/300";
            String quote = "this is a fake description of an item";

            fakeitem = new Item(
                    faker.gameOfThrones().character(),
                    faker.gameOfThrones().house(),
                    quote,
                    faker.gameOfThrones().city(),
                    true,
                    (float) faker.number().randomDouble(2, 1, 1000),
                    url,
                    u5);

            itemService.save(fakeitem);
        }

        for (int i = 0; i < 5; i++) {
            Item fakeitem;
            String url = "https://picsum.photos/seed/" + faker.pokemon().name() + "/300/300";
            String quote = "this is a fake description of an item";

            fakeitem = new Item(
                    faker.gameOfThrones().character(),
                    faker.gameOfThrones().house(),
                    quote,
                    faker.gameOfThrones().city(),
                    true,
                    (float) faker.number().randomDouble(2, 1, 1000),
                    url,
                    u3);

            itemService.save(fakeitem);
        }

        for (int i = 0; i < 5; i++) {
            Item fakeitem;
            String url = "https://picsum.photos/seed/" + faker.pokemon().name() + "/300/300";
            String quote = "this is a fake description of an item";

            fakeitem = new Item(
                    faker.gameOfThrones().character(),
                    faker.gameOfThrones().house(),
                    quote,
                    faker.gameOfThrones().city(),
                    true,
                    (float) faker.number().randomDouble(2, 1, 1000),
                    url,
                    u4);

            itemService.save(fakeitem);
        }


        /*
        Item newItem = new Item(
                faker.name().fullName(),
                "thisisatype",
                "thisisadescription",
                "thisisalocation",
                true,
                26.95f,
                "https://cnet3.cbsistatic.com/img/3Hrt9JA8KaYDM9ip1vXGxhEp4yc=/1200x675/2019/09/10/2e5b33b9-5b16-46a4-89d0-b7251ace8e71/google-logo-5.jpg",
                u5

        );

        itemService.save(newItem);
        */




        for (int i = 0; i < 5; i++)
        {
            new User();
            User fakeUser;

            users = new ArrayList<>();
            users.add(new UserRoles(new User(),
                                    r2));
            fakeUser = new User(faker.name()
                                        .username(),
                                "password",
                                faker.internet()
                                        .emailAddress(),
                                users);
            fakeUser.getUseremails()
                    .add(new Useremail(fakeUser,
                                       fakeValuesService.bothify("????##@gmail.com")));
            userService.save(fakeUser);
        }
    }
}