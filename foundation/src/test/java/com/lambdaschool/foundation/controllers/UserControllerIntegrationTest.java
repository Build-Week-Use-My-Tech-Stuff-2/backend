package com.lambdaschool.foundation.controllers;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.matcher.RestAssuredMockMvcMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.util.JacksonJsonParser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration test for UserController so only looking at 100% coverage on UserController
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserControllerIntegrationTest
{
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    private MockMvc mockMvc;

    String token;

    @LocalServerPort
    private int port;

    @Before
    public void setUp() throws
            Exception
    {
        RestAssured.port = port;

        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Before
    public void initialiseRestAssuredMockMvcWebApplicationContext() {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilter(springSecurityFilterChain).build();
        /*
        token = given().
                param("username", "bob").
                param("password", "testpassword").
                param("grant_type", "password").
                header("Accept", ContentType.JSON.getAcceptHeader()).
                header("Authorization", "Basic bGFtYmRhLWNsaWVudDpsYW1iZGEtc2VjcmV0"). // + Base64.getEncoder().encode("lambda-client:lambda-secret".getBytes())).
                post("/login").
                then().
                statusCode(200).
                extract().
                response().
                jsonPath().getString("access_token");

         */
    }

    private String obtainAccessToken(String username, String password) throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", "OAUTHCLIENTID");
        params.add("client_secret", "OAUTHCLIENTSECRET");
        params.add("username", username);
        params.add("password", password);

        ResultActions result
                = mockMvc.perform(post("/login")
                .params(params)
                .with(httpBasic("OAUTHCLIENTID","OAUTHCLIENTSECRET"))
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));

        String resultString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("access_token").toString();
    }

    @After
    public void tearDown() throws
            Exception
    {
    }

    // hits a secured endpoint without a token expecting a 401 error
    @Test
    public void aaaaa_givenNoToken_whenGetSecureRequest_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/users/users"))
                .andExpect(status().isUnauthorized());
    }

    // checks response time?
    @Test
    public void aaaab_whenMeasuredResponseTime() throws
            Exception
    {

        long time = System.currentTimeMillis();
        this.mockMvc.perform(get("/users/users"))
                .andDo(print());
        long responseTime = (System.currentTimeMillis() - time);

        assertTrue("timestamp", (responseTime < 5000L));
    }

    // checks if all users are returned
    @Test
    public void aaaac_getAllUsers() throws
            Exception
    {
        String token = obtainAccessToken("admin", "password");
        this.mockMvc.perform(get("/users/users")
                .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)));

    }


    @WithUserDetails("admin")
    @Test
    public void aaaae_getUserInfo() throws
            Exception
    {
        this.mockMvc.perform(get("/users/getuserinfo"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("testbarn")));
    }



    @Test
    public void getUserById() throws
            Exception
    {
        String token = obtainAccessToken("admin", "password");
        this.mockMvc.perform(get("/users/user/{userid}",
                                 14)
                                  .header("Authorization", "Bearer " + token))

                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("items")));
    }

    @WithUserDetails("admin")
    @Test
    public void getUserByIdNotFound() throws
            Exception
    {
        this.mockMvc.perform(get("/users/user/{userid}",
                                 100))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("ResourceNotFoundException")));
    }

    @WithUserDetails("admin")
    @Test
    public void getUserByName() throws
            Exception
    {
        this.mockMvc.perform(get("/users/user/name/{userName}",
                                 "testcat"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("testcat")));
    }

    @WithUserDetails("admin")
    @Test
    public void getUserByNameNotFound() throws
            Exception
    {
        this.mockMvc.perform(get("/users/user/name/{userName}",
                                 "rabbit"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("ResourceNotFoundException")));
    }

    @WithUserDetails("cinnamon")
    @Test
    public void getCurrentUserName() throws
            Exception
    {
        this.mockMvc.perform(get("/users/getusername"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("testdog")));
    }

    @WithUserDetails("admin")
    @Test
    public void givenPostAUser() throws
            Exception
    {
        mockMvc.perform(post("/users/user")
                                .content("{\"username\": \"Ginger\", \"password\": \"EATEATEAT\", \"primaryemail\" : \"ginger@home.local\"}")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.header()
                                   .exists("location"));
    }

    @WithUserDetails("admin")
    @Test
    public void deleteUserById() throws
            Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/user/{id}",
                                                      13))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @WithUserDetails("admin")
    @Test
    public void deleteUserByIdNotFound() throws
            Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/user/{id}",
                                                      100))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @WithUserDetails("admin")
    @Test
    public void UpdateUser() throws
            Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.put("/users/user/{userid}",
                                                   7)
                                .content("{\"password\": \"EATEATEAT\"}")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithUserDetails("admin")
    @Test
    public void deleteUserRoleByIds() throws
            Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/user/{userid}/role/{roleid}",
                                                      7,
                                                      2))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @WithUserDetails("admin")
    @Test
    public void postUserRoleByIds() throws
            Exception
    {
        mockMvc.perform(post("/users/user/{userid}/role/{roleid}",
                                                    7,
                                                    1))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }
}