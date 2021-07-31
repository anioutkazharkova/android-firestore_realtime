package com.azharkova.photoram

import com.azharkova.photoram.data.UserData
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import com.azharkova.photoram.util.Result
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest

class FirebaseAuthHelper {
    companion object {
        val instance = FirebaseAuthHelper()
    }

    private val auth: FirebaseAuth by lazy { Firebase.auth }

    var currentUser: FirebaseUser? = null
    get() =  auth.currentUser

    var isAuthorized: Boolean = false
    get() = auth.currentUser != null

    fun check(hasAuth: (Boolean) -> Unit) {
        auth.addAuthStateListener(object:FirebaseAuth.AuthStateListener{
            override fun onAuthStateChanged(fauth: FirebaseAuth) {
                hasAuth(fauth.currentUser != null)
            }
        })
    }

    fun getUser():UserData? {
        val user = auth.currentUser
        if (user != null) {
            return UserData(user.uid,user.displayName.orEmpty(),user.email.orEmpty())
        }
        return null
    }

    suspend fun signInWithEmail(email: String, password: String): Result<FirebaseUser?> {
        try {
            val response = auth.signInWithEmailAndPassword(email, password).await()
            return Result.Success(auth.currentUser)
        } catch (e: Exception) {
            return Result.Error(e)

        }
    }

    suspend fun register(name: String, email: String, password: String):Result<UserData?>{
     val createResponse = createUser(email,password)
        when(createResponse) {
            is Result.Success<*> -> signInWithEmail(email,password).also { signinResult ->
                when(signinResult) {
                    is Result.Success<*> -> {
                        val currentUser = signinResult.data
                        if (currentUser != null && currentUser is FirebaseUser) {
                          changeProfile(currentUser,name)
                          val newUser = UserData(currentUser.uid, name = name,email = email)
                          return Result.Success(newUser)
                        } else  {
                            return Result.Success(null)
                        }

                    }
                    is Result.Error -> {
                        return Result.Error(signinResult.exception)
                    }
                    is Result.Canceled -> {
                        return Result.Canceled(signinResult.exception)
                    }
                }
            }
            is Result.Error -> {
                return Result.Error(createResponse.exception)
            }
            is Result.Canceled -> {
                return Result.Canceled(createResponse.exception)
            }
        }
    }


    suspend fun changeProfile(currentUser: FirebaseUser, name: String ) {
        val request = userProfileChangeRequest {
            displayName = name
        }
       currentUser.updateProfile(request).await()
    }

    suspend fun createUser(email: String, password: String):Result<AuthResult?> {
        return try{
            val data = auth
                .createUserWithEmailAndPassword(email,password)
                .await()
            return Result.Success(data)
        }catch (e : Exception){
            return Result.Error(e)
        }
    }

    fun logout() {
        auth.signOut()
    }

    suspend fun loginUser(email: String, password: String): Result<FirebaseUser?> {
        try {
           val response = auth.signInWithEmailAndPassword(email, password).await()
            return Result.Success(auth.currentUser)
        }
        catch (e: Exception){
            return Result.Error(e)

        }
        /*try{
            return when(val authResult:Result<AuthResult?> =  auth.signInWithEmailAndPassword(email, password).await())
            {
                is Result.Success<*> -> {
                    val firebaseUser = auth.currentUser
                    Result.Success(firebaseUser)
                }
                is Result.Error -> {
                    Result.Error(authResult.exception)
                }
                is Result.Canceled ->{
                    Result.Canceled(authResult.exception)
                }
                else -> throw UnsupportedOperationException()
            }
        }
        catch (e: Exception){
            return Result.Error(e)

        }*/
    }

            /*  func register(name: String, email: String, password: String, completion: @escaping(Result<UserData,Error>)->Void) {

        //1 Create user with email and password
        Auth.auth().createUser(withEmail: email, password: password) { user, error in
            if error == nil {

                //2 Perform login to get session
                Auth.auth().signIn(withEmail: email, password: password) { user, error in
                    if let error = error {
                        completion(.failure(error))
                    }

                    //3 Check current user
                    if let signedInUser = Auth.auth().currentUser {

                        //4 Create profile change request to save name
                        let request = signedInUser.createProfileChangeRequest()
                        request.displayName = name

                        //5 Commit changes
                        request.commitChanges { (error) in
                            if let e = error {
                                completion(.failure(e))
                            } else {
                                let newUser = UserData(uid: signedInUser.uid, name: name, email: email)
                                self.userStorage?.saveUser(data: newUser)

                                //6 Save user to specific collection
                                self.saveUser(user: newUser) { (result) in
                                    switch result {
                                    case .success(_):
                                        completion(.success(newUser))
                                    case .failure(let error):
                                        completion(.failure(error))
                                    }
                                }
                            }
                        }
                    }

                }
            } else {
                completion(.failure(error!))
            }
        }
    }*/
}