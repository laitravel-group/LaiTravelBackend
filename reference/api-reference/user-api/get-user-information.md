---
description: Retrieve information of a user, the result will not contain a password.
---

# Get User Information

#### [.](./ "mention")<-

## Description

When logged in, you can retrieve username, display name and the image link of avatar of the current logged in user.&#x20;

## Authorization

Require authorization token in the header.

## URL

```
GET /user
```

## Response Codes

<table><thead><tr><th width="235">Code</th><th>Description</th></tr></thead><tbody><tr><td>200 OK</td><td>Successfully retrieved user information.</td></tr><tr><td>401 Unauthorized</td><td>Failed to get user information because the authorization token is either non-existent or invalid.</td></tr><tr><td>500 Internal Server Error</td><td>Failed to get user information the server is experiencing some issues.</td></tr></tbody></table>

## Response Body

<table><thead><tr><th width="156">Field</th><th width="88.33333333333331">Type</th><th>Description</th></tr></thead><tbody><tr><td>username</td><td>String</td><td>The username of the user. Also known as user id.</td></tr><tr><td>display_name</td><td>String</td><td>The display name of the user. It is what the website should call the user.</td></tr><tr><td>avatar</td><td>String</td><td>The url to the image of the user's avatar. It can be an empty string if the user has yet set an avatar.</td></tr><tr><td>error</td><td>String</td><td>The messge sent to indicate something went wrong. Empty if no error has occurred.</td></tr></tbody></table>

