package com.teamtreehouse.web.controller;

import com.teamtreehouse.domain.Favorite;
import com.teamtreehouse.service.FavoriteNotFoundException;
import com.teamtreehouse.service.FavoriteService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static com.teamtreehouse.domain.Favorite.FavoriteBuilder;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import static org.junit.Assert.*;

//this FindAll method, is called.
//In the case of testing the index method, we'll mock
//the service and configure what we want the mock
// object to return when its FindAll method is called.


//@runwith=run by mockitoJunitRunner. Mockito's JUnitRunner instead of JUnit's JUnitRunner.
@RunWith(MockitoJUnitRunner.class)
public class FavoriteControllerTest {
    private MockMvc mockMvc;

//create an instance of a favorite controller,
//using the default constructor of the controller class.
//And then it will inject any field annotated with the mock annotation
//into this FavoriteController.
    @InjectMocks
    private FavoriteController controller;

//mock object created by mockito and stored in the service field
//and injected into the favorite service field of the
// favorite controller here.
    @Mock
    private FavoriteService service;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

//this test method will do is ensure that the proper
//favorite objects were added to the model before the
// view is rendered.
    @Test
    public void index_ShouldIncludeFavoritesInModel() throws Exception {
        // Arrange the mock behavior
        List<Favorite> favorites = Arrays.asList(
            new FavoriteBuilder(1L).withAddress("Chicago").withPlaceId("chicago1").build(),
            new FavoriteBuilder(2L).withAddress("Omaha").withPlaceId("omaha1").build()
        );
        when(service.findAll()).thenReturn(favorites);

        // Act (perform the MVC request) and Assert results
        mockMvc.perform(get("/favorites"))
            .andExpect(status().isOk())
            .andExpect(view().name("favorite/index"))
            .andExpect(model().attribute("favorites",favorites));
        verify(service).findAll();
    }

    @Test
    public void add_ShouldRedirectToNewFavorite() throws Exception {
        // Arrange the mock behavior
        doAnswer(invocation -> {
            Favorite f = (Favorite)invocation.getArguments()[0];
            f.setId(1L);
            return null;
        }).when(service).save(any(Favorite.class));

        // Act (perform the MVC request) and Assert results
        mockMvc.perform(
          post("/favorites")
            .param("formattedAddress","chicago, il")
            .param("placeId","windycity")
        ).andExpect(redirectedUrl("/favorites/1"));
        verify(service).save(any(Favorite.class));
    }

    @Test
    public void detail_ShouldErrorOnNotFound() throws Exception {
        // Arrange the mock behavior
        when(service.findById(1L)).thenThrow(FavoriteNotFoundException.class);

        // Act (perform the MVC request) and Assert results
        mockMvc.perform(get("/favorites/1"))
            .andExpect(view().name("error"))
                .andExpect(model().attribute("ex", Matchers.instanceOf(FavoriteNotFoundException.class)));
        verify(service).findById(1L);
    }
}


//If you want to run your unit tests without the MockitoJUnitRunner,
// you can delete the @RunWith annotation from the class.
// Then, you'll need to tell Mockito to initialize the mocks
// according to your @InjectMocks and @Mock annotations:
//
//public class FavoriteControllerTest {
//  private MockMvc mockMvc;
//
//  @InjectMocks
//  private FavoriteController controller;
//
//  @Mock
//  private FavoriteService service;
//
//  @Before
//  public void setup() {
//    MockitoAnnotations.initMocks(this); // Initialize according to annotations
//    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
//  }
//}


//Construct Mocks Yourself
//
//If you'd like to construct and initialize the mocks yourself, you have options. Here is one way to do this:
//
//public class FavoriteControllerTest {
//  private MockMvc mockMvc;
//  private FavoriteController controller;
//  private FavoriteService service;
//
//  @Before
//  public void setup() {
//    // Construct the service mock
//    service = Mockito.mock(FavoriteService.class);
//    controller = new FavoriteController(service,null,null);
//    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
//  }
//}

//Of course, this will require a constructor in the
// FavoriteController class. Because we don't want to break
// Spring's dependency injection, you can mark this
// constructor as @Autowired, which means that any parameter
// values will be injected according to available beans.
// This is a nice, test-friendly alternative to autowired
// fields, which require reflection for DI. Here's the
// relevant part of the FavoriteController class:
//
//@Controller
//public class FavoriteController {
//  // @Autowired annotations removed from fields
//
//  private FavoriteService favoriteService;
//  private PlacesService placesService;
//  private WeatherService weatherService;
//
//  @Autowired // Added for Spring DI
//  public FavoriteController(
//        FavoriteService favoriteService,
//        PlacesService placesService,
//        WeatherService weatherService) {
//     this.favoriteService = favoriteService;
//     this.placesService = placesService;
//     this.weatherService = weatherService;
//  }
//}