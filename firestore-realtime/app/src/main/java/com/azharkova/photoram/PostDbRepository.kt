package com.azharkova.photoram

import com.azharkova.photoram.data.CommentItem
import com.azharkova.photoram.data.LikeItem
import com.azharkova.photoram.data.PostItem
import com.azharkova.photoram.util.Result
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.tasks.await
import javax.xml.transform.sax.TransformerHandler

class PostDbRepository {
    companion object {
        val instance = PostDbRepository()
    }

    private var postListener: ValueEventListener? = null
    private var commentListener: ValueEventListener? = null

    private val database: DatabaseReference by lazy {
        Firebase.database.reference
    }

    fun startListenToPosts(completed: (Result<List<PostItem>>)->Unit) {

        postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                var posts: MutableList<PostItem> = mutableListOf()
                for (child in dataSnapshot.children) {
                    val post = child.getValue<PostItem>()
                    post?.let { posts.add(post) }
                    completed(Result.Success(checkLiked(posts)))
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                completed(Result.Error(databaseError.toException()))
            }
        }
        val postReference = database?.child("posts")
        postReference?.addValueEventListener(postListener!!)
    }

    fun checkLiked(posts: List<PostItem>): List<PostItem> {
        val currentUser = FirebaseAuthHelper.instance.currentUser
        if (currentUser != null) {
            for (item in posts) {
                item.hasLike = item.likeItems.any { it.userId == currentUser.uid }
            }
        }
        return posts
    }

    fun stopListen() {
        postListener?.let {
            val postReference = database?.child("posts")
            postReference?.removeEventListener(it)
        }
    }

    fun loadPost(id: String, completed: (Result<PostItem>) -> Unit) {
        val postReference = database?.child("posts")
        val child = postReference?.child(id).orderByChild("timeStamp")
        child.get().addOnSuccessListener {
            val postItem = it.getValue<PostItem>()
            completed(Result.Success(postItem!!))
        }.addOnFailureListener {
            completed(Result.Error(it))
        }
    }

    fun createPost(postItem: PostItem, completed: (Result<PostItem>)->Unit) {
        val currentUser = FirebaseAuthHelper.instance.currentUser
        postItem.userId = currentUser?.uid.orEmpty()
        postItem.userName = currentUser?.displayName.orEmpty()

        val postReference = database?.child("posts")
        val child = postReference?.child(postItem.uuid)
        child.setValue(postItem).addOnSuccessListener {
            completed(Result.Success(postItem))
        }.addOnFailureListener {
            completed(Result.Error(it))
        }
    }

    fun changeLike(postItem: PostItem, onCompleted: (Result<Boolean>) -> Unit) {
        val currentUser = FirebaseAuthHelper.instance.currentUser
        val postRef = database?.child("posts").child(postItem.uuid)
        if (currentUser != null) {
            val needLike = postItem.likeItems.filter {
                it.userId == currentUser.uid
            }.count() == 0
            val like = LikeItem()
            like.postId = postItem.uuid
            like.userId = currentUser.uid
            var newLikes = arrayListOf<LikeItem>()
            newLikes.addAll(postItem.likeItems)
            if (needLike) {
                newLikes.add(like)
            } else {
                newLikes = newLikes.filter{it.userId != like.userId} as ArrayList<LikeItem>
            }

            postRef.runTransaction(object :Transaction.Handler {
                override fun doTransaction(currentData: MutableData): Transaction.Result {
                    val p = currentData.getValue(PostItem::class.java)
                        ?: return Transaction.success(currentData)
                    p.likeItems = newLikes
                    currentData.value = p
                    return Transaction.success(currentData)
                }

                override fun onComplete(
                    error: DatabaseError?,
                    committed: Boolean,
                    currentData: DataSnapshot?
                ) {
                    if (error != null) {
                        onCompleted(Result.Error(error.toException()))
                    } else {
                        onCompleted(Result.Success(true))
                    }
                }

            })
        }
    }

    fun editPost(postItem: PostItem, completed: (Result<PostItem>)->Unit) {
        val currentUser = FirebaseAuthHelper.instance.currentUser
        postItem.editor = FirebaseAuthHelper.instance.currentUser?.uid.toString()

        val postReference = database?.child("posts")
        val child = postReference?.child(postItem.uuid)
        child.setValue(postItem).addOnSuccessListener {
            completed(Result.Success(postItem))
        }.addOnFailureListener {
            completed(Result.Error(it))
        }
    }

    fun deletePost(id: String,completed: (Result<Boolean>)->Unit) {
        val postReference = database?.child("posts")
        val child = postReference?.child(id)
        child.removeValue().addOnSuccessListener {
            completed(Result.Success(true))
        }.addOnFailureListener {
            completed(Result.Error(it))
        }
    }

    fun startListenToComments(postId: String, result: (Result<List<CommentItem>>) -> Unit) {
      commentListener =  object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                var comments: MutableList<CommentItem> = mutableListOf()
                for (child in dataSnapshot.children) {
                    val commentItem = child.getValue<CommentItem>()
                    commentItem?.let { comments.add(commentItem) }
                    result(Result.Success(comments))
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
               result(Result.Error(databaseError.toException()))
            }
        }
        val commentRef = database?.child("posts").child(postId)
            .child("comments").orderByChild("date")
        commentRef.addValueEventListener(commentListener!!)
    }

    fun stopCommentListening(postId: String = "") {
        val commentRef = database?.child("posts").child(postId)
            .child("comments")
        commentRef.removeEventListener(commentListener!!)
        commentListener = null
    }

    fun sendComment(commentItem: CommentItem, completed: (Result<CommentItem>) -> Unit) {
        val currentUser = FirebaseAuthHelper.instance.currentUser
        commentItem.userId = currentUser?.uid.orEmpty()
        commentItem.userName = currentUser?.displayName.orEmpty()
        val commentRef = database?.child("posts").child(commentItem.postId)
            .child("comments")

        val child = commentRef.child(commentItem.uuid)
       child.setValue(commentItem).addOnSuccessListener {
           completed(Result.Success(commentItem))
       }.addOnFailureListener {
           completed(Result.Error(it))
       }
    }
}