package org.wit.thegreatfilter.data

data class UserData(
    var userId: String? = "",
    var name: String? = "",
    var username: String? = "",
    var imageURL: String? = "",
    var bio: String? = "",
    var gender: String? = "",
    var genderPreference: String? = "",
    var swipesLeft: List<String> = listOf(),
    var swipesRight: List<String> = listOf(),
    var matches: List<String> = listOf(),

    var positionTitle: String? = "",
    var yearsExperience: String? = "",
    var minEducationLevel: String? = "",
    var minSalary: String? = "",
    var workInOfficeDays: String? = "",
    var location: String? = "",
    var programmingLanguages: String? = "",
    var speakingLanguages: String? = "",
    var weekendsAvailable: String? = "",
    var mondayToFriday: String? = "",
    var interviewRounds: String? = ""

){

    fun toMap() = mapOf(
        "userId" to userId,
        "name" to name,
        "username"  to username,
        "imageURL" to  imageURL,
        "bio" to bio,
        "gender" to gender,
        "genderPreference" to genderPreference,
        "swipesLeft" to  swipesLeft,
        "swipesRight" to swipesRight,
        "matches:" to matches,
        "genderPreference" to genderPreference,

        "positionTitle" to positionTitle,
        "yearsExperience" to yearsExperience,
        "minEducationLevel" to minEducationLevel,
        "minSalary" to minSalary,
        "workInOfficeDays" to workInOfficeDays,
        "location" to  location,
        "programmingLanguages" to programmingLanguages,
        "speakingLanguages" to speakingLanguages,
        "weekendsAvailable" to weekendsAvailable,
        "mondayToFriday" to mondayToFriday,
       "interviewRounds" to interviewRounds
    )

}
data class ChatData(

    var chatId: String? = "",
    var user1: ChatUser = ChatUser(),
    var user2: ChatUser = ChatUser()
)

data class ChatUser(

    var userId: String? = "",
    var name: String? = "",
    var imageURL: String? = ""
)

data class Message(
    val sentBy: String? = null,
    val message: String? = null,
    val timeStamp: String? = null
)