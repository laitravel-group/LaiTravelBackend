---
description: Edit the username, display name, password and upload image for avatar.
---

# Edit User Information

#### [.](./ "mention")<-

## Authorization

Require authorization token in the header.

## URL

```
POST /user-edit
```

## Request Body

<table><thead><tr><th width="156">Field</th><th width="88.33333333333331">Type</th><th>Description</th></tr></thead><tbody><tr><td>username</td><td>String</td><td>The new username for login. Also known as user id. Do NOT include this field if you do NOT want to edit this.</td></tr><tr><td>display_name</td><td>String</td><td>The new display name for the user account. It is what the website should call the user.</td></tr><tr><td>password</td><td>String</td><td>The new password for login. Do NOT include this field if you do NOT want to edit this.</td></tr><tr><td>avatar</td><td>File</td><td>Upload a new image for the user's avatar. Accepts only square image and can be either jpg or png.</td></tr></tbody></table>

## Response Codes

<table><thead><tr><th width="191">Code</th><th>Description</th></tr></thead><tbody><tr><td>200 OK</td><td>The action is successful.</td></tr><tr><td>401 Unauthorized</td><td>Failed to get user information because the token is either non-existent or invalid.</td></tr></tbody></table>

## Response Body

<table><thead><tr><th width="156">Field</th><th width="88.33333333333331">Type</th><th>Description</th></tr></thead><tbody><tr><td>username</td><td>String</td><td>The username of the user.</td></tr><tr><td>display_name</td><td>String</td><td>The display name of the user. It is what the website should call the user.</td></tr><tr><td>avatar</td><td>String</td><td>The url to the image of the user's avatar. It can be an empty string if the user has yet set an avatar.</td></tr><tr><td>error</td><td>String</td><td>The messge sent to indicate something went wrong. Empty if no error has occurred.</td></tr></tbody></table>

