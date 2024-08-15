package com.example.expensetracker.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.AuthRepositoryImpl
import com.example.expensetracker.data.Resource
import com.example.expensetracker.data.utils.Expense
import com.example.expensetracker.data.utils.Profile
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepositoryImpl,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    //  =----------Authentication----------->
    private val _loginFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val loginFlow: StateFlow<Resource<FirebaseUser>?> = _loginFlow

    private val _signUpFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val signUpFlow: StateFlow<Resource<FirebaseUser>?> = _signUpFlow

    val currentUser: FirebaseUser?
        get() = repository.currentUser

//    init {
//        repository.currentUser?.let {
//            _loginFlow.value = Resource.Success(it)
//        }
//    }

    fun login(email: String, password: String) = viewModelScope.launch {
        _loginFlow.value = Resource.Loading
        val result = repository.login(email, password)
        _loginFlow.value = result
    }

    fun signUp(email: String, password: String, name: String) = viewModelScope.launch {
        _signUpFlow.value = Resource.Loading
        val result = repository.signup(name, email, password)
        _signUpFlow.value = result
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _loginFlow.value = null
            _signUpFlow.value = null
        }
    }

    //    =----------------------------F-I-R-E-S-T-O-R-E------------------->


    private val _expenses = MutableStateFlow<Resource<List<Expense>>>(Resource.Success(emptyList()))
    val expenses: StateFlow<Resource<List<Expense>>> = _expenses

    private val _getProfile =
        MutableStateFlow<Resource<Profile>?>(Resource.Success(Profile(null.toString())))
    val getProfile: StateFlow<Resource<Profile>?> = _getProfile


    fun addProfile(profile: Profile) {
        val uid = currentUser?.uid ?: run {
            Log.e("FirestoreError", "User not authenticated")
            return
        }
        val profileMap = mapOf(
            "name" to profile.name,
            "age" to profile.age,
            "bio" to profile.bio,
            "income" to profile.income
        )

        firestore.collection("users").document(uid).set(profileMap)
            .addOnSuccessListener {
                Log.d("Firestore", "Profile added successfully")
            }
            .addOnFailureListener {
                Log.e("FirestoreError", "Error adding profile", it)
            }
    }

    fun viewProfile() {
        val uid = currentUser?.uid
        if (uid == null) {
            // Handle the case where the user is not logged in
            _getProfile.value = Resource.Failure(Exception("User not logged in"))
            return
        }

        firestore.collection("users").document(uid).get()
            .addOnSuccessListener { value ->
                if (value != null && value.exists()) {
                    // Parse the document snapshot to a Profile object
                    val profile = value.toObject(Profile::class.java)
                    if (profile != null) {
                        _getProfile.value = Resource.Success(profile)
                    } else {
                        _getProfile.value = Resource.Failure(Exception("Profile data is null"))
                    }
                } else {
                    _getProfile.value = Resource.Failure(Exception("No profile data found"))
                }
            }
    }

    fun addExpense(expense: Expense) {
        val uid = currentUser?.uid
        val expenseMap = hashMapOf(
            "category" to expense.category,
            "amount" to expense.amount,
            "date" to System.currentTimeMillis()
        ).toMap()

        if (uid != null) {
            firestore.collection("users").document(uid).collection("expenses").add(expenseMap)
                .addOnSuccessListener {
                    Log.d(
                        "Firestore",
                        "Note added successfully"
                    )
                }
                .addOnFailureListener {
                    Log.e("FirestoreError", "Error adding note", it)
                }
        } else {
            Log.e("FirestoreError", "User not authenticated")

        }

    }

    fun deleteExpense(expenseId: String) {
        val uid = currentUser?.uid
        if (uid != null) {
            firestore.collection("users").document(uid!!).collection("expenses")
                .document(expenseId)
                .delete()
        } else Log.e("FirestoreError", "User not authenticated")

    }

    fun getExpenses() {
        val uid = currentUser?.uid ?: run {
            Log.e("FirestoreError", "User not authenticated")
            return
        }

        firestore.collection("users").document(uid).collection("expenses")
            .get()
            .addOnSuccessListener { result ->
                val expenses = result.mapNotNull { it.toObject(Expense::class.java) }
                _expenses.value = Resource.Success(expenses)
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreError", "Error getting expenses", exception)
            }
    }

}

