package org.wit.thegreatfilter


import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
//import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import org.wit.thegreatfilter.data.COLLECTION_USER
import org.wit.thegreatfilter.data.Event
import org.wit.thegreatfilter.data.UserData
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

    private fun createOrUpdateProfile(
        name: String? = null,
        username: String? = null,
        bio: String? = null,
        imageURL: String? = null
    ){
        val uid = auth.currentUser?.uid
        val userData = UserData(
            userId = uid,
            name = name,
            username = username,
            imageURL = imageURL,
            bio = bio
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
            // Lambda Find out what that is????
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