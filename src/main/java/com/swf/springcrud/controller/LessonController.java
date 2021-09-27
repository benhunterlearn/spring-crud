package com.swf.springcrud.controller;

import com.swf.springcrud.model.Lesson;
import com.swf.springcrud.repository.LessonRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;


@RestController
@RequestMapping("/lessons")
public class LessonController {

    private final LessonRepository repository;

    public LessonController(LessonRepository repository) {
        this.repository = repository;
    }

    @GetMapping("")
    public Iterable<Lesson> getAllLessons() {
        return this.repository.findAll();
    }

    @PostMapping("")
    public Long addLesson(@RequestBody Lesson lesson) {
        return this.repository.save(lesson).getId();
    }

    @GetMapping("/{id}")
    public Optional<Lesson> getLessonById(@PathVariable Long id) {
        return this.repository.findById(id);
    }

    @DeleteMapping("/{id}")
    public boolean deleteLessonById(@PathVariable Long id) {
        this.repository.deleteById(id);

        return this.repository.findById(id).isEmpty();
    }

    @PatchMapping("/{id}")
    public boolean updateLessonById(@PathVariable Long id, @RequestBody Lesson updateLesson) {
        boolean result = false;
        Optional<Lesson> optionalLesson = this.repository.findById(id);
        if (optionalLesson.isPresent()) {
            if (updateLesson.getTitle() != null) {
                optionalLesson.get().setTitle(updateLesson.getTitle());
                this.repository.save(optionalLesson.get());
                result = true;
            }
            if (updateLesson.getDeliveredOn() != null) {
                optionalLesson.get().setDeliveredOn(updateLesson.getDeliveredOn());
                this.repository.save(optionalLesson.get());
                result = true;
            }
        }
        return result;
    }

    @GetMapping("/find/{title}")
    public Iterable<Lesson> getLessonByTitle(@PathVariable String title){
        return this.repository.findByTitle(title);
    }

    @GetMapping("/between")
    public Iterable<Lesson> getLessonsBetweenTwoDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date1,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date2) {

        return this.repository.findLessonsByDeliveredOnBetween(date1, date2);
    }
}
