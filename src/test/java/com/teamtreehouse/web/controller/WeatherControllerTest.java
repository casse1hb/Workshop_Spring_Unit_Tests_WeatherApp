package com.teamtreehouse.web.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;

//allows for .perform(.get) as opposed to mockMvcReqBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//replaces "matchers" for simply "view".
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class WeatherControllerTest {
    private MockMvc mockMvc;
    private WeatherController controller;


////method to set up our controller and
////mock NBC objects.
////This object is set up with the builder
////pattern so mock MVC builders right here and
////will choose the standalone set up and
////pass to it the controller that
////we just created and
////to build the object and store it into
////the mock MVC field.
////we'll call this builders build method.
    @Before
    public void setup() {
        controller = new WeatherController();
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

//we'll start by using the mockMvc object to
//perform a get request, so mockMvc.perform And
//what we can do here is use the mock N.B.C. request
//builder's class.
//So if you arrow down there you see it right there and
//we'll use the get method and in parentheses here the parameter
//value we want to pass is the U.R.I. that we'd like to perform a get request to.
//Now for convenience, let's do a static import of this
//mockMvcRequestBuilders class, all of its methods.
//So here we have that right here.
//I'm going to move this down here and
//do an import static of all its methods.
//And I'll be sure to change that second import statement to static.
//Now I can remove this right here, and
//that get method is called in a much more succinct
//after this call to the mock NBC's perform method, we can write
//expectations which are like assertions by chaining method calls right here.
//For example, let's expect that the view name is weather slash detail.
//So we'll write andExpect.
//And in here, we can use a MockMvcResult matcher and
//all say that it's view name so, view it's
//name is equal to weather/detail.

////we'll start by using the mockMvc object to perform a get
////request, so mockMvc.perform And what we can do here is use the mock N.B.C. request builder's class.
////So if you arrow down there you see it right there and
////we'll use the get method and in parentheses here the parameter
////value we want to pass is the U.R.I. that we'd like to
////perform a get request to.
////Now for convenience, let's do a static import of this
////mockMvcRequestBuilders class, all of its methods.
////that get method is called in a much more succinct
////after this call to the mock NBC's perform method, we can write
////expectations which are like assertions by chaining method calls right here.
////For example, let's expect that the view name is weather slash detail.
////So we'll write andExpect.
////And in here, we can use a MockMvcResult matcher and
////all say that it's view name so, view it's
////name is equal to weather/detail.
////Let me put this on a couple lines so you can see it in my code window here.
////Give us a little more room there we go let's do that right there.
////Okay, let's do the same thing for this mock NBC result matchers methods
////as we did for the mock NBC request builders right here.
////So I'm going to move that down here under my static imports.
////So import static will say all its methods and
////I can get rid of this using its static methods quite a bit.
////So this allows us to write much shorter code and
////just like that we have our first test.

    @Test
    public void home_ShouldRenderDetailView() throws Exception {
        mockMvc.perform(get("/"))
            .andExpect(view().name("weather/detail"));
    }

//second test down here.
//I'll name this one public void search_ShouldRedirectWithPathParam().
//In that method, let's perform a get request to the /search URI, so mockMvc.perform.
//And again this is a GET request to the slash search you are I
//know what we need to do is we need to add to that query string parameter to the GET
//request so after the call to get I will call the param method.
//The parameter name is q and the value I'd like to pass is 60657 and
//what we expect is that this request
//redirects to /search/60657.
//So let's do that now let me put this on the next line here .andExpect.
//I will say redirectedUrl.
//That's another method that comes from this MockMvcResultMatchers class.
//So I want to expect that the redirected URL is /search/60657.
//I'll put my semi colon at the end here.
//There are lots of things that you can include as an expectation.

    @Test
    public void search_ShouldRedirectWithPathParam() throws Exception {
        mockMvc.perform(get("/search").param("q","60657"))
            .andExpect(redirectedUrl("/search/60657"));
    }
}
