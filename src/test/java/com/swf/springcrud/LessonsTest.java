package com.swf.springcrud;


import com.swf.springcrud.model.Lesson;
import com.swf.springcrud.repository.LessonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.event.TransactionalEventListener;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.core.Is.is;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Date;


@SpringBootTest
@AutoConfigureMockMvc
public class LessonsTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    LessonRepository repository;

    Lesson testLesson;
    Lesson math101Lesson;

    @BeforeEach
    void setupRepository() {
        LocalDate inputDate = LocalDate.of(2017, 11, 10);
        this.testLesson = new Lesson("Math101", inputDate);
        this.math101Lesson = this.repository.save(testLesson);
    }

    @Transactional
    @Rollback
    @Test
    void getAllLessonsReturnsListOfAllLessons() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/lessons")
                .accept(MediaType.APPLICATION_JSON);
        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is(testLesson.getTitle())));
    }

    @Test
    @Transactional
    @Rollback
    void getLessonById() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/lessons/" + math101Lesson.getId())
                .accept(MediaType.APPLICATION_JSON);
        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(testLesson.getTitle())))
                .andExpect(jsonPath("$.deliveredOn", is(testLesson.getDeliveredOn().toString())));

    }

    @Test
    @Transactional
    @Rollback
    void addLesson() throws Exception{
        RequestBuilder request = MockMvcRequestBuilders.post("/lessons")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\" : \"Chemistry\" , \"DeliveredOn\" : \"2017-10-01\"}");

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(result -> content().toString().matches("^\\d+$"));
    }

    @Test
    @Transactional
    @Rollback
    void deleteLessonById() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.delete("/lessons/" + math101Lesson.getId())
                .contentType(MediaType.TEXT_PLAIN);
        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        assertTrue(this.repository.findById(math101Lesson.getId()).isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    void updateLessonByIdWithNewTitle() throws Exception{
        RequestBuilder request = MockMvcRequestBuilders.patch("/lessons/" + math101Lesson.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\" : \"Chemistry\"}");

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

    }

    @Test
    @Transactional
    @Rollback
    void updateLessonByIdWithNewTitleAndDeliveredOn() throws Exception{
        RequestBuilder request = MockMvcRequestBuilders.patch("/lessons/" + math101Lesson.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\" : \"Chemistry\", \"deliveredOn\":\"2102-07-12\"}");

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        String actualDeliveredOn = this.repository.findById(math101Lesson.getId()).get().getDeliveredOn().toString();
        assertEquals("2102-07-12", actualDeliveredOn);
    }

    @Test
    @Transactional
    @Rollback
    void getLessonByTitleSearch() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/lessons/find/" + math101Lesson.getTitle())
                .accept(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is(math101Lesson.getTitle())));
    }

    @Test
    @Transactional
    @Rollback
    void getLessonsBetweenTwoDates() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/lessons/between?date1=1900-01-01&date2=2100-01-01")
                .accept(MediaType.APPLICATION_JSON);
        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is(math101Lesson.getTitle())));
    }
}
