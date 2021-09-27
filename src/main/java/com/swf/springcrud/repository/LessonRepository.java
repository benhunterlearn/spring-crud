package com.swf.springcrud.repository;

import com.swf.springcrud.model.Lesson;
import org.springframework.data.repository.CrudRepository;

public interface LessonRepository extends CrudRepository<Lesson, Long> {

    Iterable<Lesson> findByTitle(String title);
}
