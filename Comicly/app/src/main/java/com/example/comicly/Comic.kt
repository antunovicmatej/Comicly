package com.example.comicly

class Comic(
    var uid: Long?,
    var name: String? = "",
    var author: String? = "",
    var description: String? = "",
    var isFavorite: Boolean = false

    ) {
    constructor() : this(
        null,
        null,
        null,
        null,
    )


}

