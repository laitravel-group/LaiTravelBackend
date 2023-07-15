---
description: Used for creating a new user account.
---

# Sign Up

#### [.](./ "mention")<-

## URL

```
POST /signup
```

## Request Body

<table><thead><tr><th width="161">Field</th><th width="88.33333333333331">Type</th><th>Description</th></tr></thead><tbody><tr><td>username</td><td>String</td><td>The username for the new user account to login. Also known as user id.</td></tr><tr><td>display_name</td><td>String</td><td>The display name for the new user account. It is what the website should call the user.</td></tr><tr><td>password</td><td>String</td><td>The password for the new user account to login.</td></tr></tbody></table>

## Response Codes

<table><thead><tr><th width="191">Code</th><th>Description</th></tr></thead><tbody><tr><td>201 Created</td><td>The sign up is successful.</td></tr><tr><td>409 Conflict</td><td>The sign up is failed because the username has already existed.</td></tr></tbody></table>

## Response Body

<table><thead><tr><th width="156">Field</th><th width="88.33333333333331">Type</th><th>Description</th></tr></thead><tbody><tr><td>message</td><td>String</td><td>The message sent to indicate a successful sign up.</td></tr><tr><td>error</td><td>String</td><td>The messge sent to indicate something went wrong. Empty if no error has occurred.</td></tr></tbody></table>

