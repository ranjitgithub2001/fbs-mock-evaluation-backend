package com.fbs.mock_evaluation_system.entity;

import jakarta.persistence.*;

@Entity
@Table(
    name = "batch_module",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_batch_course_module",
            columnNames = {"batch_id", "course_module_id"}
        )
    }
)
public class BatchModule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(
        name = "batch_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_batch_module_batch")
    )
    private Batch batch;

    @ManyToOne(optional = false)
    @JoinColumn(
        name = "course_module_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_batch_module_course_module")
    )
    private CourseModule courseModule;

    public BatchModule() {
    }

    public BatchModule(Batch batch, CourseModule courseModule) {
        this.batch = batch;
        this.courseModule = courseModule;
    }

    public Long getId() {
        return id;
    }

    public Batch getBatch() {
        return batch;
    }

    public CourseModule getCourseModule() {
        return courseModule;
    }

    public void setBatch(Batch batch) {
        this.batch = batch;
    }

    public void setCourseModule(CourseModule courseModule) {
        this.courseModule = courseModule;
    }
}