package com.teamtreehouse.service;

import com.teamtreehouse.config.AppConfig;
import com.teamtreehouse.service.dto.geocoding.Location;
import com.teamtreehouse.service.dto.weather.Weather;
import com.teamtreehouse.service.resttemplate.weather.WeatherServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.Instant;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.hasSize;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class WeatherServiceTest {
    @Autowired
    private WeatherService service;

    private Location loc;
    private Weather weather;

    private static final double ERROR_GEO = 0.0000001;
    private static final double ERROR_TIME = 5000;
//calls findbylocation method.
    @Before
    public void setup() {
        loc = new Location(41.9403795,-87.65318049999999);
        weather = service.findByLocation(loc);
    }
    //These 3 tests:
    //Make sure that the latitude and longitude that
    //come back from the API response match what we
    //included in the request. The reason I'm doing
    //this is because I want to make sure that the
    //forecast data I'm getting back is actually for
    //the location I requested and not some other location.
    //this is to ensure that our application properly
    //captures the results and sticks them into the proper objects

    //to test the coordinates in the test method,
    //I'll use Hamcrest and test the latitude and
    //longitude separately. So in here I will assertThat
    //and we'll say that the weather.getlatitude and
    //let's get that assertThat method imported here.
    //
    //Measuring margin of error:
    //I want to assert that weather.getlatitude
    //is close to the latitude that I made the API call with.
    //Now since geo coordinates may not be exact,
    //we can use a handy hamcrest matcher called close to.
    //That checks whether or not the actual value is close to an expected value
    //within a specified margin of error.
    //And since I'll be reusing this margin of error for longitude,
    //I'll quickly create a class field for that.
    //So up here, I'll say private static final double.
    //I'll call it ERROR_GEO.
    @Test
    public void findByLocation_ShouldReturnSameCoords() throws Exception {
        assertThat(weather.getLatitude(),closeTo(loc.getLatitude(),ERROR_GEO));
        assertThat(weather.getLongitude(),closeTo(loc.getLongitude(),ERROR_GEO));
    }

    @Test
    public void findByLocation_ShouldReturn8DaysForecastData() throws Exception {
        assertThat(weather.getDaily().getData(),hasSize(8));
    }

//For this one, we wanna make sure that the data we
//get back is time stamped with
//the current time within a reasonable margin of error.
    //field created for ERROR_TIME
// calculate the duration between that instant and
//the instant from the weather data returned by the
//service.
    //convert to milliseconds = toMillis()
    @Test
    public void findByLocation_ShouldReturnCurrentConditions() throws Exception {
        Instant now = Instant.now();
        double duration = Duration.between(now,weather.getCurrently().getTime()).toMillis();
        assertThat(duration,closeTo(0,ERROR_TIME));
    }

    //To get those properties from api.properties
    //annotate this class with the same type of PropertySource annotation
    @Configuration
    @PropertySource("api.properties")
    public static class TestConfig {
        @Autowired
        private Environment env;

        @Bean
        public RestTemplate restTemplate() {
            return AppConfig.defaultRestTemplate();
        }

        @Bean
        public WeatherService weatherService() {
            WeatherService service = new WeatherServiceImpl(
                env.getProperty("weather.api.name"),
                env.getProperty("weather.api.key"),
                env.getProperty("weather.api.host")
            );
            return service;
        }
    }
}
