### Image

```markdown
suspend fun uploadImage(bytes: ByteArray): Uri? {
val reference = storage.reference.child("image-${Date()}.jpg")
var uploadTask = reference.putBytes(bytes)
uploadTask.await()
return reference.downloadUrl.await()
}
```
