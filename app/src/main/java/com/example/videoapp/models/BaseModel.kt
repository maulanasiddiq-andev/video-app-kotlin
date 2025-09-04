package com.example.videoapp.models

import java.util.Date

open class BaseModel {
    val description: String = ""
    val recordStatus: String = ""
    val createdTime: Date? = null
    val modifiedTime: Date? = null
    val createdBy: String = ""
    val modifiedBy: String = ""
    val version: Int = 0
}