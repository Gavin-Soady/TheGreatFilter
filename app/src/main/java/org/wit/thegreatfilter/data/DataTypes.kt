package org.wit.thegreatfilter.data

data class UserData(
    var userId: String? = "",
    var name: String? = "",
    var username: String? = "",
    var imageURL: String? = "",
    var bio: String? = "",

){
    fun toMap() = mapOf(
        "userId" to userId,
        "name" to name,
        "username"  to username,
        "imageURL" to  imageURL,
        "bio" to bio
    )

}