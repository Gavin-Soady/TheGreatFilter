package org.wit.thegreatfilter


import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import org.wit.thegreatfilter.data.COLLECTION_USER
import org.wit.thegreatfilter.data.Event
import javax.inject.Inject

@HiltViewModel
class TGFViewModel @Inject constructor(
    val auth: FirebaseAuth,
    val db: FirebaseFirestore,
    val storage: FirebaseStorage

    ): ViewModel() {

    val inProgress = mutableStateOf(false)
    val popupNotification = mutableStateOf<Event<String>?>(Event("Test"))


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
                                // Create user profile in database
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