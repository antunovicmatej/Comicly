package com.example.comicly

class Comic(
    var uid: Long?,
    var name: String? = "",
    var author: String? = "",

    ) {
    constructor() : this(null, null, null)


}

