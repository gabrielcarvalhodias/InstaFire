package edu.stanford.rkpandey.instafire.models

data class Post(
    var description: String = ""
    @get:PropriertyName(value:"Image_url") @set:PropriertyName(value:"image_url") var imageURL: String = "",
    @get:PropriertyName(value:"creation_time_ms") @set:PropriertyName(value: "creation_time_ms") var creationTimeMs: Long = 0,
    var user: User? = null
)