package com.amlich.alproxy.models

import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.RepositoryDefinition
import org.springframework.stereotype.Repository
import javax.persistence.*

@Entity
@Table(name = "calendars")
data class Calendar (
    @Id
    val id: Int = 0,

    @Column(name = "owner_id")
    val ownerId: Int = 0,

    @Column(name = "name")
    val name: String? = null,

    @Column(name = "description", length=10000, columnDefinition="TEXT")
    val description: String? = null,

    @Column(name = "privacy")
    val privacy: Int = 0
)

@Repository
public interface CalendarRepository: CrudRepository<Calendar, Int>