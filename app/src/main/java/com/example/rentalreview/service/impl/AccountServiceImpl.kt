package com.example.rentalreview.service.impl

import com.example.rentalreview.model.User
import com.example.rentalreview.service.AccountService
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class AccountServiceImpl
    @Inject constructor
    (private val firestore: FirebaseFirestore,
     private val auth: FirebaseAuth) : AccountService {

    override val currentUserId: String
        get() = auth.currentUser?.uid.orEmpty()

    override val hasUser: Boolean
        get() = auth.currentUser != null

    override val currentUser: Flow<User> = callbackFlow {
        val listenerRegistration = firestore
            .collection("users")
            .document(currentUserId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val user = snapshot.toObject(User::class.java)
                    if (user != null) {
                        trySend(user).isSuccess
                    }
                }
            }

        // Remove listener quando o flow for cancelado
        awaitClose { listenerRegistration.remove() }
    }


    override suspend fun register(email: String, password: String, name: String) {

            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user

            val profileUpdate = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()
            user?.updateProfile(profileUpdate)?.await()

        val userCollection = User(
            id = user?.uid ?: "",
            name = user?.displayName ?: "",
            email = user?.email ?: ""
        )

        firestore
            .collection("users")
            .document(user!!.uid)
            .set(userCollection)
            .await()
    }

    override suspend fun authenticate(email: String, password: String) {

        val result = auth.signInWithEmailAndPassword(email, password).await()

    }

}