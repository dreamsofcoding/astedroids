package com.udacity.asteroidradar.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Asteroid(
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
) : Parcelable