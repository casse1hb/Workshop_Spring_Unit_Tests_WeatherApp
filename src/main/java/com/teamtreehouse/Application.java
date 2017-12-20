//how to test controllers, views, services and
//DAOs as well as incorporate the marking of objects
// that are being tested directly.

//This will cover tests of application function.
//spring test framework and JUnit to test our app.
//Also testing database interactivity.

//Not Integration and end to end testing:
//  whether or not two components talk nicely with eachother

//set API properties(resources): https://developers.google.com/maps/documentation/geocoding/get-api-key
//code that we'll write during

//this workshop will live in the test source directory,

package com.teamtreehouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}