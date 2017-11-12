package com.github.spoptchev.scientist

import java.time.Duration
import java.time.Instant

data class Observation<T>(
        val id: String,
        val name: String,
        val outcome: Outcome<T>,
        val startedAt: Instant,
        val stoppedAt: Instant
) {

    val duration: Duration by lazy {
        Duration.between(startedAt, stoppedAt)
    }

    val result: T by lazy {
        when(outcome) {
            is Success<T> -> outcome.value
            is Failure<T> -> throw outcome.exception
        }
    }

    fun matches(other: Observation<T>, comparator: Comparator<T>): Boolean {
        return comparator(outcome, other.outcome)
    }

    fun isIgnored(other: Observation<T>, ignores: List<Ignore<T>>): Boolean {
        return ignores.any { it(outcome, other.outcome) }
    }

}