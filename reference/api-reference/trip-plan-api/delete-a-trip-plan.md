---
description: Delete a trip plan given the trip id.
---

# Delete a Trip Plan

[.](./ "mention")<-

## Description

When logged in, you can delete a saved trip plan that belongs to the logged in user. You cannot delete any trip plan that belongs to other users.

## Authorization

Require authorization token in the header.

## URL

```
DELETE /trip-plan-delete
```

## Java Method

### `PlaceService.java`

```java
public void deleteTripById(String tripId);
```

## Request Query Parameters

<table><thead><tr><th width="136.33333333333331">Parameter</th><th width="106">Type</th><th width="104" data-type="checkbox">Required</th><th>Description</th></tr></thead><tbody><tr><td>trip_id</td><td>String</td><td>true</td><td>The id of the trip plan you want to delete, can be obtained from <a data-mention href="get-a-list-of-saved-trip-plans.md">get-a-list-of-saved-trip-plans.md</a>. Attempting to delete a trip not belonging to the current user will result in a 403 Forbidden response code.</td></tr></tbody></table>

## Response Codes

<table><thead><tr><th width="238">Code</th><th>Description</th></tr></thead><tbody><tr><td>200 OK</td><td>Successfully delete a saved trip plan.</td></tr><tr><td>401 Unauthorized</td><td>Failed to delete a trip plan because the authorization token is either non-existent or invalid.</td></tr><tr><td>403 Forbidden</td><td>Failed to delete a trip plan because the trip plan corresponding to the trip id does not belong to the current logged in user.</td></tr><tr><td>500 Internal Server Error</td><td>Failed to delete a trip plan because the server is experiencing some issues.</td></tr></tbody></table>
