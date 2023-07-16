---
description: Edit the username, display name, password and upload image for avatar.
---

# Edit User Information

#### [.](./ "mention")<-

## Description

When logged in, you can edit the username, display name, password and upload new avatar image for the logged in user.

## Authorization

Require authorization token in the header.

## URL

```
POST /user-edit
```

## Request Body

<table><thead><tr><th width="155">Field</th><th width="88.33333333333331">Type</th><th>Description</th></tr></thead><tbody><tr><td>username</td><td>String</td><td>The new username for login. Also known as user id. Exclude this field if you do NOT want to edit username.</td></tr><tr><td>display_name</td><td>String</td><td>The display name for the user account. It is what the website should call the user. Exclude this field if you do NOT want to edit display name.</td></tr><tr><td>password</td><td>String</td><td>The password for login. Exclude this field if you do NOT want to change password.</td></tr><tr><td>avatar</td><td>File</td><td>An image file to set as avatar. It must be a squared image, either jpg or png are accepted. Exclude this field if you do NOT want to edit avatar.</td></tr></tbody></table>

## Response Codes

<table><thead><tr><th width="237">Code</th><th>Description</th></tr></thead><tbody><tr><td>200 OK</td><td>Successfully edited user information.</td></tr><tr><td>400 Bad Request</td><td>Failed to edit user information because some of the fields do not meet the requirements or the request is incorrectly formatted.</td></tr><tr><td>401 Unauthorized</td><td>Failed to edit user information because the authorization token is either non-existent or invalid.</td></tr><tr><td>500 Internal Server Error</td><td>Failed to edit user information because the server is experiencing some issues.</td></tr></tbody></table>
