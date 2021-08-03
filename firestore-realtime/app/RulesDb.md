### Rules Realtime Database

```markdown
  "rules": {
     ".write": true,
      ".read": true,
}
```

```markdown

  "rules": {
     ".write": "auth != null",
      ".read": "auth != null",
}
```

```markdown
{
  "rules": {
     //".write": "auth != null",
      ".read": "auth != null",
    "posts": {
      //".write": "auth != null",
      "$post": {
         "likeItems": {
         ".write": "auth != null"
       },
       "comments": {
          ".write": "auth != null"
       },
       ".write": "newData.child('userId').val() === auth.uid || data.child('userId').val() === auth.uid",
      }
    },
      "users": {
      ".write": "auth != null",
    }
  },
    
}
```