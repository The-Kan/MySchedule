package com.my.schedule.ui.log

var PROJECT_NAME = "My Schedule"

class LogManager {
    companion object{
        fun getPrefix(className : String) : String {
            return "[$PROJECT_NAME]$className"
        }
    }
}
