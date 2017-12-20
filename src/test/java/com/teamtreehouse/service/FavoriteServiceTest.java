//because the favorite service depends on
//the favorite DAO and
//we don't want to test the DAO here,
//we'll need to mark the DAO.

package com.teamtreehouse.service;

import com.teamtreehouse.dao.FavoriteDao;
import com.teamtreehouse.domain.Favorite;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FavoriteServiceTest {
//mocking the dao.
    @Mock
    private FavoriteDao dao;
//not interface so it cant be instantiated directly so well name it favoriteserviceimpl.
    @InjectMocks
    private FavoriteService service = new FavoriteServiceImpl();

    @Test
    public void findAll_ShouldReturnTwo() throws Exception {
        List<Favorite> faves = Arrays.asList(
            new Favorite(),
            new Favorite()
        );
//when the favorite dao.findall method is found, return faves.
        when(dao.findAll()).thenReturn(faves);
        //standard J Unit assertion to confirm that a list of
        // two items is returned by the services find all method
        assertEquals("findAll should return two favorites",2,service.findAll().size());
        verify(dao).findAll();
    }
    //Not really concerned here about the details of the favorite object,
    //just that what the DAO returns is indeed a favorite object.
    //in contrast to the controllers handling of this exception, in the case of
    //the service, the exception will actually bubble up to our test method.
    //So we can use that familiar expected element on the methods
    //test annotation like this.
    //Expected equals FavoriteNotFoundException.class.
    @Test
    public void findById_ShouldReturnOne() throws Exception {
        when(dao.findOne(1L)).thenReturn(new Favorite());
        assertThat(service.findById(1L),instanceOf(Favorite.class));
        verify(dao).findOne(1L);
    }

    @Test(expected = FavoriteNotFoundException.class)
    public void findById_ShouldThrowFavoriteNotFoundException() throws Exception {
        when(dao.findOne(1L)).thenReturn(null);
        service.findById(1L);
        verify(dao).findOne(1L);
    }
}
