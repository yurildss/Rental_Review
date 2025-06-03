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

    override val currentUser: Flow<User>
        get() = callbackFlow {
            val listener = FirebaseAuth.AuthStateListener { auth ->
                val firebaseUser = auth.currentUser

                val user = if (firebaseUser != null) {
                    User(
                        id = firebaseUser.uid,
                        name = firebaseUser.displayName ?: "",
                        email = firebaseUser.email ?: ""
                    )
                } else {
                    User() // Usuário vazio ou não autenticado
                }

                trySend(user).onFailure { e -> e?.printStackTrace() }
            }

            Firebase.auth.addAuthStateListener(listener)
            awaitClose { Firebase.auth.removeAuthStateListener(listener) }
        }

    override suspend fun register(email: String, password: String, name: String) {

            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user
            val profileUpdate = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()
            user?.updateProfile(profileUpdate)?.await()
    }

    override suspend fun authenticate(email: String, password: String) {

        val result = auth.signInWithEmailAndPassword(email, password).await()

    }

}