package com.example.expensetracker.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
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

    init {
        if (repository.currentUser != null) {
            _loginFlow.value = Resource.Success(repository.currentUser!!)
        }
    }

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

    var sortBy = "date"

    private val _tagg =
        mutableStateListOf(
            "Rent",
            "Groceries",
            "Utilities",
            "Transportation",
            "Dining Out",
            "Shopping",
            "Entertainment",
            "Health",
            "Bills",
            "Subscriptions",
            "Travel",
            "Miscellaneous"
        )
    val tagg: List<String> get() = _tagg

    fun addTag(tag: String) {
        if (tag.isNotEmpty() && !_tagg.contains(tag)) {
            _tagg.add(tag)
            // Optionally, you could save to a database or other persistent storage here
        }
    }

    private val _expenses = MutableStateFlow<Resource<List<Expense>>>(Resource.Success(emptyList()))
    val expenses: StateFlow<Resource<List<Expense>>> = _expenses

    private val _getProfile =
        MutableStateFlow<Resource<Profile>?>(Resource.Success(Profile(null.toString())))
    val getProfile: StateFlow<Resource<Profile>?> = _getProfile

    private val _expense = MutableStateFlow<Resource<Expense>?>(null)
    val expense: StateFlow<Resource<Expense>?> = _expense

    fun aggregateExpenses(expense: List<Expense>): Map<String, Double> {
        val aggregatedExpenses = expense
            .filter { it.amount != null }
            .groupBy { it.category }
            .mapValues { (_, expenses) -> expenses.sumOf { ((it.amount ?: 0.0).toDouble()) } }

        // Print the results
        aggregatedExpenses.forEach { (category, totalAmount) ->
            println("Category: $category, Total Amount: $totalAmount")
        }
        return aggregatedExpenses
    }

    fun aggregateExpensesByDate(expenses: List<Expense>): Map<String, Double> {
        return expenses
            .filter { it.amount != null }
            .groupBy { it.category }
            .mapValues { (_, expenses) -> expenses.sumOf { ((it.amount ?: 0.0).toDouble()) } }

    }

    fun editProfile(profile: Profile) {
        val uid = currentUser?.uid ?: run {
            return
        }
        val profileMap = mapOf(
            "name" to profile.name,
            "age" to profile.age,
            "bio" to profile.bio,
            "income" to profile.income,
        )
        firestore.collection("users").document(uid).update(profileMap)
            .addOnSuccessListener {
                Log.d("Firestore", "Profile added successfully")
            }
            .addOnFailureListener {
                Log.e("FirestoreError", "Error adding profile", it)
            }
    }


    fun addProfile(profile: Profile) {
        val uid = currentUser?.uid ?: run {
            Log.e("FireStoreError", "User not authenticated")
            return
        }
        val profileMap = mapOf(
            "name" to profile.name,
            "age" to profile.age,
            "bio" to profile.bio,
            "income" to profile.income,
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
        _getProfile.value = Resource.Loading

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
            "uid" to expense.id,
            "category" to expense.category,
            "amount" to expense.amount,
            "date" to expense.date,
            "tags" to expense.tags
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
            firestore.collection("users").document(uid).collection("expenses")
                .document(expenseId)
                .delete()
        } else Log.e("FirestoreError", "User not authenticated")

    }

    fun getExpenses() {
        val uid = currentUser?.uid ?: run {
            Log.e("FirestoreError", "User not authenticated")
            return
        }

        firestore.collection("users").document(uid).collection("expenses").orderBy(sortBy)
            .addSnapshotListener { value, error ->
                if (value != null && !value.isEmpty) {
                    val result = value.documents.mapNotNull {
                        it.toObject(Expense::class.java)?.copy(id = it.id)
                    }
                    _expenses.value = Resource.Success(result)
                } else if (error != null) {
                    _expenses.value = Resource.Failure(error)
                } else {
                    _expenses.value = Resource.Success(emptyList())

                }
            }

    }


    fun getExpenseById(id: String) {
        viewModelScope.launch {
            val uId = currentUser?.uid
            if (uId != null) {
                val documentReference =
                    firestore.collection("users").document(uId).collection("expenses").document(id)

                documentReference.addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.e("getExpenseById", "Error getting expense by id: $error")
                        _expense.value = Resource.Failure(error) // Handle error case
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        val result = snapshot.toObject(Expense::class.java)
                        if (result != null) {
                            _expense.value = Resource.Success(result)
                        } else {
                            Log.e(
                                "getExpenseById",
                                "Document conversion failed or document is null."
                            )
                            _expense.value =
                                Resource.Failure(Exception("Expense not found or conversion failed"))
                        }
                    } else {
                        Log.e("getExpenseById", "No such document exists.")
                        _expense.value = Resource.Failure(Exception("Expense not found"))
                    }
                }
            } else {
                Log.e("getExpenseById", "User not authenticated")
                _expense.value = Resource.Failure(Exception("User not authenticated"))
            }
        }

    }

    fun updateExpense(expense: Expense, expenseId: String) {
        val uid = currentUser?.uid
        if (uid != null && expenseId.isNotEmpty()) {
            val expenseMap = hashMapOf(
                "category" to expense.category,
                "amount" to expense.amount,
                "date" to expense.date,
                "tag" to expense.tags
            ).toMap()
            firestore.collection("users").document(uid).collection("expenses").document(expenseId)
                .update(expenseMap).addOnSuccessListener {
                    Log.d("Firestore", "Note updated successfully")
                }.addOnFailureListener {
                    Log.e("FirestoreError", "Error updating note", it)
                }

        }
    }
}
