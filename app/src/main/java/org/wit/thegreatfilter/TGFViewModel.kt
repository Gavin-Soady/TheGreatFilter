package org.wit.thegreatfilter

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import org.wit.thegreatfilter.data.COLLECTION_USER
import org.wit.thegreatfilter.data.Event
import org.wit.thegreatfilter.data.UserData
import org.wit.thegreatfilter.ui.screens.Gender
import javax.inject.Inject


@HiltViewModel
class TGFViewModel @Inject constructor(
    val auth: FirebaseAuth,
    val db: FirebaseFirestore,
    //val storage: FirebaseStorage

    ): ViewModel() {

    val inProgress = mutableStateOf(false)
    val popupNotification = mutableStateOf<Event<String>?>(Event("Test"))
    val signedIn = mutableStateOf(false)
    val userData = mutableStateOf<UserData?>(null)


    init{
        auth.signOut()
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
                            if (task.isSuccessful)
                                createOrUpdateProfile(username = username)

                                else
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
        imageURL: String? = null,
        gender: Gender? = null,
        genderPreference: Gender? = null,
    ){

        //Elvis operator
        val uid = auth.currentUser?.uid
        val userData = UserData(
            userId = uid,
            name = name ?: userData.value?.name,
            username = username ?: userData.value?.username,
            imageURL = imageURL ?: userData.value?.imageURL,
            bio = bio ?: userData.value?.bio,
            gender = gender.toString() ?: userData.value?.gender,
            genderPreference = genderPreference.toString() ?: userData.value?.genderPreference
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

}