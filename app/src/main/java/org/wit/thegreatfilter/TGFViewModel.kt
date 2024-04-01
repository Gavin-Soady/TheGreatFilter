package org.wit.thegreatfilter

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import org.wit.thegreatfilter.data.COLLECTION_USER
import org.wit.thegreatfilter.data.Event
import org.wit.thegreatfilter.data.UserData
import org.wit.thegreatfilter.ui.screens.GenderType
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class TGFViewModel @Inject constructor(
    val auth: FirebaseAuth,
    val db: FirebaseFirestore,
    val storage: FirebaseStorage

    ): ViewModel() {

    val inProgress = mutableStateOf(false)
    val popupNotification = mutableStateOf<Event<String>?>(Event(""))
    val signedIn = mutableStateOf(false)
    val userData = mutableStateOf<UserData?>(null)

    val matchProfiles  = mutableStateOf<List<UserData>>(listOf())
    val inProgressProfiles = mutableStateOf(false)


    init{
       // auth.signOut()
        val currentUser = auth.currentUser
        signedIn.value = currentUser != null
        currentUser?.uid?.let {
                uid -> getUserData(uid)
        }
    }


    fun onSignup (username: String, email: String, pass: String){

        if ( username.isEmpty() or email.isEmpty() or pass.isEmpty()){

            handleException(customMessage = "Please fill in ALL fields")
            return
        }
        inProgress.value = true
        db.collection(COLLECTION_USER).whereEqualTo("username", username)
            .get()
            .addOnSuccessListener {
                if (it.isEmpty)
                    auth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener{task ->
                            if (task.isSuccessful) {
                                signedIn.value = true
                                createOrUpdateProfile(username = username)
                            }else
                                    handleException(task.exception, "Signup Failed")

                        }
                else
                    handleException(customMessage = "Username already exists")
                inProgress.value = false
            }
            .addOnFailureListener{
                handleException(it)
            }
    }

    fun onLogin ( email: String, pass: String) {

        if (email.isEmpty() or pass.isEmpty()) {

            handleException(customMessage = "Please fill in ALL fields")
            return
        }

        inProgress.value = true
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    inProgress.value = false
                    signedIn.value = true
                    auth.currentUser?.uid?.let {
                        getUserData(it)

                    }
                } else
                    handleException(task.exception, "Login Failed")
            }
            .addOnFailureListener {
                handleException(it, "Login Failed")
            }

    }



    private fun createOrUpdateProfile(
        name: String? = null,
        username: String? = null,
        bio: String? = null,
        imageUrl: String? = null,
        gender: GenderType? = null,
        genderPreference: GenderType? = null,
    ){
        //Elvis operator
        val uid = auth.currentUser?.uid
        val userData = UserData(
            userId = uid,
            name = name ?: userData.value?.name,
            username = username ?: userData.value?.username,
            imageURL = imageUrl ?: userData.value?.imageURL,
            bio = bio ?: userData.value?.bio,
            gender = gender?.toString() ?: userData.value?.gender,
            genderPreference = genderPreference?.toString() ?: userData.value?.genderPreference
        )
        // Find out what name shadowed means
        uid?.let { uid ->
        inProgress.value = true
            db.collection(COLLECTION_USER)
                .document(uid)
                .get()
                .addOnSuccessListener {
                    if (it.exists())
                        it.reference.update(userData.toMap())
                            .addOnSuccessListener {
                                this.userData.value = userData
                                inProgress.value = false
                            }
                            .addOnFailureListener {
                                handleException(it, "Cannot update user")
                            }
                    else {
                        db.collection(COLLECTION_USER)
                            .document(uid)
                            .set(userData)
                        inProgress.value = false
                        getUserData(uid)
                    }
                }
                .addOnFailureListener{
                    handleException(it)
                }
        }
    }

    private fun getUserData(uid: String) {

        inProgress.value = true
        db.collection( COLLECTION_USER).document(uid)
            // Lambda Find out what that is???? Single Line function
            .addSnapshotListener { value, error ->

                if (error !=null)
                    handleException(error, " Cannot retrieve user data")
                if (value !=null) {
                    val user = value.toObject<UserData>()
                    userData.value = user
                    inProgress.value =false
                }

            }
    }

    fun onLogout() {

        //Built in functionality from Firebase
        auth.signOut()
        signedIn.value = false
        userData.value = null
        popupNotification.value = Event( " You have logged out, See you Soon!")

    }

    fun updateProfileData(
        name: String,
        username: String,
        bio: String,
        gender: GenderType,
        genderPreference: GenderType
    ) {
        createOrUpdateProfile(
            name = name,
            username = username,
            bio = bio,
            gender = gender,
            genderPreference = genderPreference
        )
    }

private fun uploadImage(uri: Uri, onSucess: (Uri) -> Unit){
    inProgress.value = true
    val storageRef = storage.reference
    val uuid = UUID.randomUUID()
    val imageRef = storageRef.child("images/$uuid")
    val uploadTask = imageRef.putFile(uri)

    uploadTask
        .addOnSuccessListener {
            val result = it.metadata?.reference?.downloadUrl
            result?.addOnSuccessListener(onSucess)
        }

        .addOnFailureListener{
            handleException(it)
            inProgress.value = false
        }

}

    fun uploadProfileImage(uri: Uri){
        uploadImage(uri){
            createOrUpdateProfile(imageUrl = it.toString())
        }
    }

    private fun handleException(exception : Exception? = null, customMessage: String = ""){
        Log.e("The Great Filter", "Exception", exception)
        exception?.printStackTrace()
        val errorMsg = exception?.localizedMessage ?: ""
        val message = if( customMessage.isEmpty())
                        errorMsg
                    else "$customMessage: $errorMsg"
        popupNotification.value = Event(message)
        inProgress.value =false

    }

    private fun  populateCards(){
        inProgressProfiles.value = true
        val g  = if (userData.value?.gender.isNullOrEmpty()) "ANY"
        else userData.value!!.gender!!.uppercase()
        val gPref  = if (userData.value?.genderPreference.isNullOrEmpty()) "ANY"
        else userData.value!!.genderPreference!!.uppercase()

        val cardsQuery =
            when (GenderType.valueOf(gPref)){
                GenderType.MALE -> db.collection(COLLECTION_USER)
                    .whereEqualTo("gender",GenderType.MALE)
                GenderType.FEMALE -> db.collection(COLLECTION_USER)
                    .whereEqualTo("gender",GenderType.FEMALE)
                GenderType.ANY -> db.collection(COLLECTION_USER)

            }

        val userGender = GenderType.valueOf(g)
        cardsQuery.where(
            Filter.and(
                Filter.notEqualTo("userId", userData.value?.userId),
                Filter.or(
                    Filter.equalTo("genderPreference", userGender),
                    Filter.equalTo("genderPreference", GenderType.ANY)
                )
            )
        )
            .addSnapshotListener { value, error ->
                if (error != null) {
                    inProgressProfiles.value = false
                    handleException(error)
                }
                if (value != null) {
                    val potentials = mutableListOf<UserData>()
                    value.documents.forEach {
                        it.toObject<UserData>()?.let { potential ->
                            var showUser = true
                            //if (userData.value?.swipesLeft?.contains(potential.userId) == true ||
                            //userData.value?swipesRight?.contains())


                        }
                    }

                }
            }}}
