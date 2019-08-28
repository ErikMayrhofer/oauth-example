# OAuth Auth-Server
This is a sample implementation of a OAuth-Authorization
server that uses JWT and Social APIs

## Endpoints
### Auth
`/auth/[auth-strategy]?[params]`
Where `[auth-strategy]` may be:

|`[auth-strategy]`|`?[params]`|Description|
|---|---|---|
|`local`|`username=[XXX]&password=[XXX]`|The local strategy, use no identity provider and auth agains the local DB|
|`google`| |Redirects to google for a Login with google|

### Info
`/auth/info/[??]`

|`[??]`|Description|
|---|---|
|`me`|Requests info about the current user|

### User Object
```json

```