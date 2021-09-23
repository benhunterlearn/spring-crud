package com.swf.springcrud.controller;

import com.swf.springcrud.model.Lesson;
import com.swf.springcrud.repository.LessonRepository;
import org.springframework.web.bind.annotation.*;

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

        if (this.repository.findById(id).isPresent()) {
            return false;
        } else {
            return true;
        }
    }

    @PatchMapping("/{id}")
    public boolean updateLessonById(@PathVariable Long id, @RequestBody Lesson updateLesson) {
        Optional<Lesson> optionalLesson = this.repository.findById(id);
        if (optionalLesson.isPresent()) {
            if (updateLesson.getTitle() != null) {
                optionalLesson.get().setTitle(updateLesson.getTitle());
                this.repository.save(optionalLesson.get());
                return true;
            }
            if (updateLesson.getDeliveredOn() != null) {
                optionalLesson.get().setDeliveredOn(updateLesson.getDeliveredOn());
                this.repository.save(optionalLesson.get());
                return true;
            }
        }
        return false;
    }
}
