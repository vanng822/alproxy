package com.amlich.alproxy.models

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import javax.persistence.*

@Entity
@Table(name = "events")
data class Event(
    @Id
    val id: Int = 0,

    @Column(name = "calendar_id")
    val calendarId: Int = 0,

    @Column(name = "title", length=1000, columnDefinition="TEXT")
    val title: String? = null,

    @Column(name = "description", length=20000, columnDefinition="TEXT")
    val description: String? = null
)

@Repository
public interface EventRepository: CrudRepository<Event, Int>