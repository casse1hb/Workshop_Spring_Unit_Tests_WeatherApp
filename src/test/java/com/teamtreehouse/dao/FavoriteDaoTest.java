//have our entire application's beans available
// if we wanted them. When using Spring Data,
// this is often a reasonable approach, since
//generating implementations for those repositories
//would be unreasonably time consuming. And, part of
//these tests is making sure that those implementations are indeed
//generated correctly. So how do we make this an integration test?
//Well, easy, we add spring boots spring application configuration annotation to
//the class SpringApplicationConfiguration just like that.
//And we tell it which class contains the top level annotations,
//you could also specify a list of configuration classes here.



// testing the favorite Dao.
//principal = principal coming from spring security.
    //authenticated user ID thats getting injected into this query.
package com.teamtreehouse.dao;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.teamtreehouse.Application;
import com.teamtreehouse.domain.Favorite;
import com.teamtreehouse.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static com.teamtreehouse.domain.Favorite.FavoriteBuilder;

//@springapp = letting the system know what top level annotations are used.
//start with a preexisting set of data
    //= DV unit(database setup):inject a set of starter data into our database so that we have
    //some users, roles and favorites ready to go when we want to test.
    //And to set this up in the test we'll apply DB unit's database setup annotation.
//test execution listeners' annotation:
    //We need this DependencyInjectionTestExecutionListener
    //so that our autowired dependencies can be injected from the loaded application
    //context.
//starting data set in xml form. "favorites.xml"
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@DatabaseSetup("classpath:favorites.xml")
@TestExecutionListeners({
    DependencyInjectionTestExecutionListener.class,
    DbUnitTestExecutionListener.class
})
public class FavoriteDaoTest {
    @Autowired
    private FavoriteDao dao;

//each of our DAO methods that we're
//testing contains a custom query that
//injects authentication data. That means
//our DAO under test is going to need that
//authentication data available for injection.
    @Before
    public void setup() {
        User user = new User();
        user.setId(1L);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user,null));
    }

    @Test
    public void findAll_ShouldReturnTwo() throws Exception {
        assertThat(dao.findAll(),hasSize(2));
    }

    @Test
    public void save_ShouldPersistEntity() throws Exception {
        String placeId = "treehouseASKDFJASD#";
        Favorite fave = new FavoriteBuilder()
            .withPlaceId(placeId).build();
        dao.saveForCurrentUser(fave);
        assertThat(dao.findByPlaceId(placeId),notNullValue(Favorite.class));
    }
}
