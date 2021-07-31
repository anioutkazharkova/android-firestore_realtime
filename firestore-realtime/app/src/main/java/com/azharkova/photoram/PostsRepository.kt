package com.azharkova.photoram

import com.azharkova.photoram.data.CommentItem
import com.azharkova.photoram.data.LikeItem
import com.azharkova.photoram.data.PostItem
import com.azharkova.photoram.util.Result
import com.google.firebase.firestore.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowViaChannel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class PostsRepository {
    companion object {
        val instance = PostsRepository()
    }

    private var listener: ListenerRegistration? = null
    private var commentListener: ListenerRegistration? = null

    fun startListenToPosts(result: (List<PostItem>) -> Unit) {
        val collection = FirebaseFirestore.getInstance().collection("posts")
        listener = collection.orderBy("timeStamp", Query.Direction.DESCENDING)
            .addSnapshotListener(MetadataChanges.INCLUDE) { data, firebaseFirestoreExceptioor ->
                if (data != null) {
                    val posts = data.toObjects(PostItem::class.java)

                    result(checkLiked(posts))
                }
            }
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

    suspend fun loadPost(id: String): PostItem? {
        val collection = FirebaseFirestore.getInstance().collection("posts")
        val document = collection.document(id)
        val data = document.get().await()
        return data.toObject(PostItem::class.java)
    }

    suspend fun createPost(postItem: PostItem) {
        val currentUser = FirebaseAuthHelper.instance.currentUser
        postItem.userId = currentUser?.uid.orEmpty()
        postItem.userName = currentUser?.displayName.orEmpty()

        val collection = FirebaseFirestore.getInstance().collection("posts")
        val document = collection.document(postItem.uuid)
        document.set(postItem).await()
    }

    suspend fun editPost(postItem: PostItem) {
        postItem.editor = FirebaseAuthHelper.instance.currentUser?.uid.toString()

        val collection = FirebaseFirestore.getInstance().collection("posts")
        val document = collection.document(postItem.uuid)
        document.set(postItem).await()
    }

    suspend fun deletePost(id: String) {
        val collection = FirebaseFirestore.getInstance().collection("posts")
        val document = collection.document(id)
        document.delete().await()
    }

    fun stopListening() {
        listener?.remove()
        listener = null
    }

    fun startListenToComments(postId: String, result: (List<CommentItem>) -> Unit) {
        val collection = FirebaseFirestore.getInstance().collection("posts").document(postId)
            .collection("comments")
        commentListener = collection.orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener(MetadataChanges.INCLUDE) { data, firebaseFirestoreExceptioor ->
                if (data != null) {
                    val comments = data.toObjects(CommentItem::class.java)
                    result(comments)
                }
            }
    }

    fun stopCommentListening() {
        commentListener?.remove()
        commentListener = null
    }

    suspend fun sendComment(commentItem: CommentItem) {
        val currentUser = FirebaseAuthHelper.instance.currentUser
        commentItem.userId = currentUser?.uid.orEmpty()
        commentItem.userName = currentUser?.displayName.orEmpty()
        val collection =
            FirebaseFirestore.getInstance().collection("posts").document(commentItem.postId)
                .collection("comments")

        val document = collection.document(commentItem.uuid)
        document.set(commentItem).await()
    }

    fun changeLike(postItem: PostItem, onCompleted: (Result<Boolean>) -> Unit) {
        val currentUser = FirebaseAuthHelper.instance.currentUser
        val document = FirebaseFirestore.getInstance().collection("posts").document(postItem.uuid)
        if (currentUser != null) {
            val needLike = postItem.likeItems.filter {
                it.userId == currentUser.uid
            }.count() == 0
            val like = LikeItem()
            like.postId = postItem.uuid
            like.userId = currentUser.uid
            var newLikes = postItem.likeItems
            if (needLike) {
                newLikes.add(like)
            } else {
                newLikes.remove(like)
            }
            FirebaseFirestore.getInstance().runTransaction { transition ->
                transition.update(document, "likeItems", newLikes)
                newLikes
            }.addOnSuccessListener { result ->
                onCompleted(Result.Success(true))
            }.addOnFailureListener { e ->
                onCompleted(Result.Error(e))
            }

        }
    }
}