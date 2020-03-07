package com.slack.exercise

/**
 * This annotation allows us to open some classes for mocking purposes while they are final in
 * release builds.
 */
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
annotation class OpenClass
