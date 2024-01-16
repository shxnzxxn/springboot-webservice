package com.shxnzxxn.book.springbootwebservice.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
/**
 * @SpringBootTest에서는 @BootstrapWith(SpringBootTestContextBootstrapper.class)를 상속받는다.
 * @WebMvcTest는 @BootstrapWith(WebMvcTestContextBootstrapper.class)를 상속받고
 * WebMvcTestContextBootstrapper는 SpringBootTestContextBootstrapper를 상속받는다.
 *
 * 따라서 @WebMvcTest는 @SpringBootTest의 일부 기능을 상속받을 수 있을 것이라고 추측한다.
 */
@WebMvcTest // Web 테스트에 집중할 수 있는 어노테이션
public class HelloControllerTest {

    @Autowired private MockMvc mvc;

    @Test
    public void hello가_리턴된다() throws Exception {
        // given
        String hello = "hello";

        // when
        mvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string(hello));
    }

    @Test
    public void helloDto가_리턴된다() throws Exception {
        // given
        String name = "hello";
        int amount = 1000;

        // then
        mvc.perform(get("/hello/dto")
                .param("name", name)
                .param("amount", String.valueOf(amount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.amount", is(amount)));
    }
}