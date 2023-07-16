---
description: Used for user login.
---

# Login

#### [.](./ "mention")<-

## URL

```
POST /login
```

## Request Body

<table><thead><tr><th width="137">Field</th><th width="88.33333333333331">Type</th><th>Description</th></tr></thead><tbody><tr><td>username</td><td>String</td><td>The username for login. Also known as user id.</td></tr><tr><td>password</td><td>String</td><td>The password for login.</td></tr></tbody></table>

## Response Codes

<table><thead><tr><th width="237">Code</th><th>Description</th></tr></thead><tbody><tr><td>200 OK</td><td>The login is successful.</td></tr><tr><td>400 Bad Request</td><td>Failed to login because the request is incorrectly formatted.</td></tr><tr><td>401 Unauthorized</td><td>Failed to login because the login credentials are incorrect.</td></tr><tr><td>500 Internal Server Error</td><td>Failed to login because the server is experiencing some issues.</td></tr></tbody></table>

## Response Body

<table><thead><tr><th width="156">Field</th><th width="88.33333333333331">Type</th><th>Description</th></tr></thead><tbody><tr><td>token</td><td>String</td><td>The encrypted token that is needed for other APIs when authentication is required.</td></tr></tbody></table>

