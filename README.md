# Android Firestore/Realtime db sample

## Sample app "Photoram"

Sample app to demonstrate using Firebase features

Uses: 
1. Firebase authentification for login and registrations

2. Firebase storage to keep image

3.1. Firestore database as a remote storage for data

3.2 Firebase Realtime Database for same purpose

Users and posts are kept as [collections](https://github.com/anioutkazharkova/android-firestore_realtime/wiki/Work-with-collection).
Likes - field in post [item](https://github.com/anioutkazharkova/android-firestore_realtime/wiki/Change-complex-field-with-transaction)
Comments - [subcollection](https://github.com/anioutkazharkova/android-firestore_realtime/wiki/Work-with-sub-collection) for posts

Realtime Database logic is implemented in PostsDbRepository and featured view models. 
Comments are kept as an inner structrure of posts json hierarchy. LikeItems still be an inner array. 
You can find here, how to  work in Realtime Database with:
1. [Listening data](https://github.com/anioutkazharkova/android-firestore_realtime/wiki/Listen-to-data-values)
2. [Loading single items](https://github.com/anioutkazharkova/android-firestore_realtime/wiki/Work-with-node-in-Realtime-Database)
3. [Transaction and updates](https://github.com/anioutkazharkova/android-firestore_realtime/wiki/Realtime-database-transaction)
4. [Coroutines](https://github.com/anioutkazharkova/android-firestore_realtime/wiki/Realtime-Database---coroutines)

Also demonstrates how to use [Firestore rules](https://github.com/anioutkazharkova/android-firestore_realtime/wiki/Rules).
Realtime Database rules are also presented [here](https://github.com/anioutkazharkova/android-firestore_realtime/wiki/Rules-for-realtime-database)

More info about [Firebase Auth](https://github.com/anioutkazharkova/android-firestore_realtime/wiki/Authorization)

How to load images in [storage](https://github.com/anioutkazharkova/android-firestore_realtimep/wiki/Image-storage-using)
