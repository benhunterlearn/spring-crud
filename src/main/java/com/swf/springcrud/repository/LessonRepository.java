package com.swf.springcrud.repository;

import com.swf.springcrud.model.Lesson;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;

public interface LessonRepository extends CrudRepository<Lesson, Long> {

    Iterable<Lesson> findByTitle(String title);

    Iterable<Lesson> findLessonsByDeliveredOnBetween(LocalDate date1, LocalDate date2);
}
