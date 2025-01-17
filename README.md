# DAI-Practical-Work-3

This project was realized by Dylan Langumier, Guillaume Fragnière and Killian Viquerat in the scope of the DAI class, followed at HEIG-VD.

---
## Table of Contents

1. [Purpose](#Purpose)
2. [Setup Development WorkPlace](#Setup Development WorkPlace)
3. [Docker](#Docker)
4. [API](#API)
5. [How to use](#How-to-use)
6. [Web infrastructures](#Web-infrastructures)
---
## Purpose
This web application allows you to host an API.
This API is based on the Binding Of Isaac game.
It procures functionality to manipulate item and characters.

You can find the API documentation [here]()

---
## Setup Development WorkPlace

### Prerequisite

This application was built against Temurin 21, and thus requires Temurin 21+ to run.

### Clone

First you need to clone the project. You can use this command:

```bash
git clone git@github.com:Dylan-Langumier/DAI-Practical-Work-3.git
```
Or by downloading the [source code](https://github.com/Dylan-Langumier/DAI-Practical-Work-3/archive/refs/heads/master.zip) and then unzipping in into a directory.

### How to build

#### From Intellij IDEA

Use the Run the application configuration

#### From your CLI

If you have Maven installed
```bash
mvn dependency:go-offline clean compile package
```
If you do not have Maven, we included a Maven Wrapper, which you can use to build the application:
```bash
./mvnw dependency:go-offline clean compile package
```
---
## Docker

---
## API

The TboI API allows to get items or characters from the game The Binding of Isaac. It uses the HTTP protocol and the JSON
format.

The API is based on the CRUD pattern. It has the following operations:

- Create a new item/character
- Get many items that you can filter by first item pool or quality
- Get many characters
- Get one item by its id
- Update an item/character
- Delete an item/character

Users are also able to get a random item from a given item pool or given quality.

## Endpoints

### Get many items

- `GET /items`

Get all items from the database.

#### Request

None.

#### Response

The response body contains a JSON array with the following properties:
- `id` - The id of the item
- `name` - The name of the item
- `type` - The type of the item
- `itemPools` - An array of the item pools the item is in
- `statistics` - An array of JSON arrays containing the statistics modifiers of the item.
- `description` - The text description rendered on item pickup
- `quality` - The quality of the item
- `gameVersions` - The version of the game the item was introduced in
- `note` (optional) - A note on the item that can contain trivia or item effect info

#### Status codes

- `200` (OK) - Returns all the items
- `304` (Not Modified) - The items have not been modified since the last access, so they are retrieved through cache

### Get one item

- `GET /item/{id}`

Get one item by its id.

#### Request

The request must contain the following query parameters:

- `id` - The id of the item (of the form `cXXX`)

#### Response

The response body contains a JSON object with the following properties:

- `id` - The id of the item
- `name` - The name of the item
- `type` - The type of the item
- `itemPools` - An array of the item pools the item is in
- `statistics` - An array of JSON arrays containing the statistics modifiers of the item.
- `description` - The text description rendered on item pickup
- `quality` - The quality of the item
- `gameVersions` - The version of the game the item was introduced in
- `note` (optional) - A note on the item that can contain trivia or item effect info

#### Status codes

- `200` (OK) - Returns the desired item
- `304` (Not Modified) - The item has not been modified since the last access, so it is retrieved through cache
- `404` (Not Found) - The item with the given id was not found
-
### Get all items by quality

- `GET /items/quality/{quality}`

Get all items of a given quality.

#### Request

The request path must contain the desired quality.

#### Response

The response body contains a JSON array with the following properties:

- `id` - The id of the item
- `name` - The name of the item
- `type` - The type of the item
- `itemPools` - An array of the item pools the item is in
- `statistics` - An array of JSON arrays containing the statistics modifiers of the item.
- `description` - The text description rendered on item pickup
- `quality` - The quality of the item
- `gameVersions` - The version of the game the item was introduced in
- `note` (optional) - A note on the item that can contain trivia or item effect info

#### Status codes

- `200` (OK) - Returns all the items
- `304` (Not Modified) - The items of the quality have not been modified since the last access, so they are retrieved through cache

### Get all items by pool

- `GET /items/pool/{pool}`

Get all items of a given pool.

#### Request

The request path must contain the desired pool.

#### Response

The response body contains a JSON array with the following properties:

- `id` - The id of the item
- `name` - The name of the item
- `type` - The type of the item
- `itemPools` - An array of the item pools the item is in
- `statistics` - An array of JSON arrays containing the statistics modifiers of the item.
- `description` - The text description rendered on item pickup
- `quality` - The quality of the item
- `gameVersions` - The version of the game the item was introduced in
- `note` (optional) - A note on the item that can contain trivia or item effect info

#### Status codes

- `200` (OK) - Returns all the items

### Get a random item

-  `GET /items/d6`

Get a random item.

#### Request

Empty.

#### Response

The response body contains a JSON object with the following properties:

- `id` - The id of the item
- `name` - The name of the item
- `type` - The type of the item
- `itemPools` - An array of the item pools the item is in
- `statistics` - An array of JSON arrays containing the statistics modifiers of the item.
- `description` - The text description rendered on item pickup
- `quality` - The quality of the item
- `gameVersions` - The version of the game the item was introduced in
- `note` (optional) - A note on the item that can contain trivia or item effect info

#### Status codes

- `200` (OK) - Return a random item

### Get a random item from a given quality

-  `GET /items/d6/{quality}`

Get a random item from a given quality.

#### Request

The request path must contain the desired quality.

#### Response

The response body contains a JSON object with the following properties:

- `id` - The id of the item
- `name` - The name of the item
- `type` - The type of the item
- `itemPools` - An array of the item pools the item is in
- `statistics` - An array of JSON arrays containing the statistics modifiers of the item.
- `description` - The text description rendered on item pickup
- `quality` - The quality of the item
- `gameVersions` - The version of the game the item was introduced in
- `note` (optional) - A note on the item that can contain trivia or item effect info

#### Status codes

- `200` (OK) - Return a random item from the given quality
- `404` (Not Found) - The quality is invalid

### Get a random item from a given pool

-  `GET /items/d6/{pool}`

Get a random item from a given pool.

#### Request

The request path must contain the desired poll.

#### Response

The response body contains a JSON object with the following properties:

- `id` - The id of the item
- `name` - The name of the item
- `type` - The type of the item
- `itemPools` - An array of the item pools the item is in
- `statistics` - An array of JSON arrays containing the statistics modifiers of the item.
- `description` - The text description rendered on item pickup
- `quality` - The quality of the item
- `gameVersions` - The version of the game the item was introduced in
- `note` (optional) - A note on the item that can contain trivia or item effect info

#### Status codes

- `200` (OK) - Return a random item from the given quality
- `404` (Not Found) - The pool is invalid

### Spindown an item

- `GET /item/spindown/{id}`

Gets the item with an id of 1 lower than the given id.

### Request

The request path must contain the desired item's id.

### Response

The response body contains a JSON object with the following properties:

- `id` - The id of the item
- `name` - The name of the item
- `type` - The type of the item
- `itemPools` - An array of the item pools the item is in
- `statistics` - An array of JSON arrays containing the statistics modifiers of the item.
- `description` - The text description rendered on item pickup
- `quality` - The quality of the item
- `gameVersions` - The version of the game the item was introduced in
- `note` (optional) - A note on the item that can contain trivia or item effect info

Note that the response can be a `null` object, if the item has no spindown (id `c1`) or if the given id is not a valid item.
#### Status codes

- `200` (OK) - Return a random item from the given quality

### Create a new item

- `POST /item/{id}`

Create a new item.

#### Request

The request body must contain a JSON object with the following properties:

- `id` - The id of the item
- `name` - The name of the item
- `type` - The type of the item
- `itemPools` - An array of the item pools the item is in
- `statistics` - An array of JSON arrays containing the statistics modifiers of the item.
- `description` - The text description rendered on item pickup
- `quality` - The quality of the item
- `gameVersions` - The version of the game the item was introduced in
- `note` (optional) - A note on the item that can contain trivia or item effect info

#### Response

The response body contains a JSON object with the following properties:

- `id` - The id of the item
- `name` - The name of the item
- `type` - The type of the item
- `itemPools` - An array of the item pools the item is in
- `statistics` - An array of JSON arrays containing the statistics modifiers of the item.
- `description` - The text description rendered on item pickup
- `quality` - The quality of the item
- `gameVersions` - The version of the game the item was introduced in
- `note` (optional) - A note on the item that can contain trivia or item effect info

#### Status codes

- `201` (Created) - The user has been successfully created
- `409` (Conflict) - The given item id is already taken

### Update an item

- `PATCH /item/{id}`

Update an item by its id.

#### Request

The request path must contain the id of the item.

The request body must contain a JSON object with the following properties:

- `name` - The name of the item
- `type` - The type of the item
- `itemPools` - An array of the item pools the item is in
- `statistics` - An array of JSON arrays containing the statistics modifiers of the item.
- `description` - The text description rendered on item pickup
- `quality` - The quality of the item
- `gameVersions` - The version of the game the item was introduced in
- `note` (optional) - A note on the item that can contain trivia or item effect info

#### Response

The response body contains a JSON object with the following properties:

- `id` - The id of the item
- `name` - The name of the item
- `type` - The type of the item
- `itemPools` - An array of the item pools the item is in
- `statistics` - An array of JSON arrays containing the statistics modifiers of the item.
- `description` - The text description rendered on item pickup
- `quality` - The quality of the item
- `gameVersions` - The version of the game the item was introduced in
- `note` (optional) - A note on the item that can contain trivia or item effect info

#### Status codes

- `200` (OK) - The item has been successfully updated
- `404` (Not Found) - The item does not exist

### Delete an item

- `DELETE /item/{id}`

Delete an item by its id.

#### Request

The request path must contain the id of the item.

#### Response

The response body is empty.

#### Status codes

- `204` (No Content) - The item has been successfully deleted
- `404` (Not Found) - The item does not exist

### Get many characters

- `GET /characters`

Get all characters from the database.

#### Request

None.

#### Response

The response body contains a JSON array with the following properties:
- `id` - The id of the character
- `name` - The name of the character
- `hearts` - The starting hearts of the character - can be red, soul, black or bone hearts
- `damage` - The starting damage of the character
- `shotSpeed` - The starting shot speed of the character
- `range` - The starting range of the character
- `speed` - The starting speed of the character
- `luck` - The starting luck of the character
- `startingPickups` - The starting pickups of the character (bombs, keys, pennies)
- `startingItems` - The starting items of the character

#### Status codes

- `200` (OK) - Returns all the characters
- `304` (Not Modified) - The characters have not been modified since the last access, so they are retrieved through cache

### Get one item

- `GET /character/{id}`

Get one character by its id.

#### Request

The request must contain the following query parameters:

- `id` - The id of the item

#### Response

The response body contains a JSON object with the following properties:

- `id` - The id of the character
- `name` - The name of the character
- `hearts` - The starting hearts of the character - can be red, soul, black or bone hearts
- `damage` - The starting damage of the character
- `shotSpeed` - The starting shot speed of the character
- `range` - The starting range of the character
- `speed` - The starting speed of the character
- `luck` - The starting luck of the character
- `startingPickups` - The starting pickups of the character (bombs, keys, pennies)
- `startingItems` - The starting items of the character

#### Status codes

- `200` (OK) - Returns the desired character
- `304` (Not Modified) - The character has not been modified since the last access, so it is retrieved through cache
- `404` (Not Found) - The character with the given id was not found

### Update a character

- `PATCH /character/{id}`

Update an character by its id.

#### Request

The request path must contain the id of the character.

The request body must contain a JSON object with the following properties:

- `name` - The name of the character
- `hearts` - The starting hearts of the character - can be red, soul, black or bone hearts
- `damage` - The starting damage of the character
- `shotSpeed` - The starting shot speed of the character
- `range` - The starting range of the character
- `speed` - The starting speed of the character
- `luck` - The starting luck of the character
- `startingPickups` - The starting pickups of the character (bombs, keys, pennies)
- `startingItems` - The starting items of the character

#### Response

The response body contains a JSON object with the following properties:

- `id` - The id of the character
- `name` - The name of the character
- `hearts` - The starting hearts of the character - can be red, soul, black or bone hearts
- `damage` - The starting damage of the character
- `shotSpeed` - The starting shot speed of the character
- `range` - The starting range of the character
- `speed` - The starting speed of the character
- `luck` - The starting luck of the character
- `startingPickups` - The starting pickups of the character (bombs, keys, pennies)
- `startingItems` - The starting items of the character

#### Status codes

- `200` (OK) - The character has been successfully updated
- `404` (Not Found) - The character does not exist

### Delete an item

- `DELETE /character/{id}`

Delete a character by its id.

#### Request

The request path must contain the id of the character.

#### Response

The response body is empty.

#### Status codes

- `204` (No Content) - The character has been successfully deleted
- `404` (Not Found) - The character does not exist

---
## How to use

### Query All Items

- **Command:**

```bash
curl -i -X GET http://localhost:8080/items
```

- Result:
```http request
HTTP/1.1 200 OK
Date: Fri, 17 Jan 2025 12:54:53 GMT
Content-Type: application/json
Last-Modified: 2025-01-17T13:54:53.264587946
Content-Length: 11150

[{"id":"c362","name":"Sworn Protector","type":"Passive","statistics":[],"description":"Protective friend","itemPools":["BabyShop","AngelRoom"],"quality":3,"gameVersions":["Afterbirth"],"note":"An orbital angel which does 7 contact damage per tick and blocks shots."},
{"id":"c11","name":"1up!","type":"Passive","statistics":[],"description":"Extra life","itemPools":["SecretRoom"],"quality":2,"gameVersions":["Flash"],"note":"Gives Isaac an extra life."}
]
```

*Only part of the body because it would be too big otherwise*

---
### Query All Items by Pool

<details>
<summary>Available Pools</summary>
<ul>
    <li>AngelRoom</li>
    <li>BabyShop</li>
    <li>BatteryBum</li>
    <li>Beggar</li>
    <li>BombBum</li>
    <li>Boss</li>
    <li>CraneGame</li>
    <li>CurseRoom</li>
    <li>DevilBeggar</li>
    <li>DevilRoom</li>
    <li>GoldenChest</li>
    <li>KeyMaster</li>
    <li>Library</li>
    <li>MomsChest</li>
    <li>OldChest</li>
    <li>Planetarium</li>
    <li>RedChest</li>
    <li>RottenBeggar</li>
    <li>SecretRoom</li>
    <li>ShellGame</li>
    <li>Shop</li>
    <li>TreasureRoom</li>
    <li>UltraSecretRoom</li>
    <li>WoodenChest</li>
</ul>
</details>

- **Command:**


```bash
curl -i -X GET http://localhost:8080/items/pool/TreasureRoom
```
*Example with TreasureRoom Pool*
- Result:
```http request
HTTP/1.1 200 OK
Date: Fri, 17 Jan 2025 13:20:30 GMT
Content-Type: application/json
Last-Modified: 2025-01-17T14:20:30.470678908
Content-Length: 8807

[{"id":"c670","name":"Montezuma's Revenge","type":"Passive","statistics":[],"description":"Oh no...","itemPools":["TreasureRoom"],"quality":2,"gameVersions":["Repentance"],"note":"While firing tears, you now charge up a poop attack for 3 seconds that when released, fires from Isaac's behind."},
{"id":"c222 ","name":"Anti-Gravity","type":"Passive","statistics":[{"type":"TearRate","value":"+1","gameVersions":null}],"description":"Anti-gravity tears + tears up","itemPools":["TreasureRoom"],"quality":1,"gameVersions":["Rebirth"],"note":"Holding the fire buttons causes tears to hover in midair. When released, the tears will all shoot in the direction they were originally fired."}
]
```

*Only part of the body because it would be too big otherwise*

---
### Query All Items by Quality

- **Command:**

<details>
<summary>Available Qualities</summary>
<ul>
    <li>0</li>
    <li>1</li>
    <li>2</li>
    <li>3</li>
    <li>4</li>
</ul>
</details>

```bash
curl -i -X GET http://localhost:8080/items/quality/0
```
*Example with quality 0*
- Result:
```http response
HTTP/1.1 200 OK
Date: Fri, 17 Jan 2025 13:31:31 GMT
Content-Type: application/json
Last-Modified: 2025-01-17T14:24:57.554567174
Content-Length: 221

[{"id":"c9","name":"Skatole","type":"Passive","statistics":[],"description":"Fly Love","itemPools":["ShellGame"],"quality":0,"gameVersions":["Flash"],"note":"A lot of fly enemies are no longer aggressive towards Isaac."}]
```
---
### Query an Item

- **Command:**

```bash
curl -i -X GET http://localhost:8080/item/c1
```
*Example with item id c1*

- Result:
```http response
HTTP/1.1 200 OK
Date: Fri, 17 Jan 2025 13:42:52 GMT
Content-Type: application/json
Last-Modified: 2025-01-17T14:37:15.959667568
Content-Length: 233

{"id":"c1","name":"The Sad Onion","type":"Passive","statistics":[{"type":"Tears","value":"+0.7","gameVersions":null}],"description":"Tears Up","itemPools":["TreasureRoom","CraneGame"],"quality":3,"gameVersions":["Flash"],"note":null}
```
---
### Query a Random Item

- **Command:**

```bash
curl -i -X GET http://localhost:8080/items/d6
```

- Result:
```http response
HTTP/1.1 200 OK
Date: Fri, 17 Jan 2025 13:44:49 GMT
Content-Type: application/json
Content-Length: 696

{"id":"c2","name":"The Inner Eye","type":"Passive","statistics":[{"type":"Tears","value":"-49%","gameVersions":null},{"type":"TearAmount","value":"x3","gameVersions":null}],"description":"Triple shot","itemPools":["TreasureRoom"],"quality":2,"gameVersions":["Flash"],"note":null}
```
*Random item obtained is The Inner Eye*

---
### Query a Random Item by Pool

- **Command:**

```bash
curl -i -X GET http://localhost:8080/items/d6/pool/TreasureRoom
```
*Example with TreasureRoom Pool*

- Result:
```http response
HTTP/1.1 200 OK
Date: Fri, 17 Jan 2025 13:48:56 GMT
Content-Type: application/json
Content-Length: 284

{"id":"c8","name":"Brother Bobby","type":"Passive","statistics":[],"description":"Friends 'till the end","itemPools":["BabyShop","DevilRoom","TreasureRoom"],"quality":1,"gameVersions":["Flash"],"note":"Spawns a familiar that follows Isaac around shooting tears that deal 3.5 damage."}
```
*Random item obtained is Brother Bobby*

---
### Query a Random Item by Quality

- **Command:**

```bash
curl -i -X GET http://localhost:8080/items/d6/quality/1
```
*Example with Quality 1*

- Result:
```http response
HTTP/1.1 200 OK
Date: Fri, 17 Jan 2025 13:59:06 GMT
Content-Type: application/json
Content-Length: 387

{"id":"c222 ","name":"Anti-Gravity","type":"Passive","statistics":[{"type":"TearRate","value":"+1","gameVersions":null}],"description":"Anti-gravity tears + tears up","itemPools":["TreasureRoom"],"quality":1,"gameVersions":["Rebirth"],"note":"Holding the fire buttons causes tears to hover in midair. When released, the tears will all shoot in the direction they were originally fired."}
```
*Random item obtained is Anti-Gravity*

---
### Query a Spindown Item

*A spindown item is the item having the id juste under the one specified*
- **Command:**

```bash
curl -i -X GET http://localhost:8080/item/spindown/c2
```
*Example with item c2*

- Result:
```http response
HTTP/1.1 200 OK
Date: Fri, 17 Jan 2025 14:06:14 GMT
Content-Type: application/json
Last-Modified: 2025-01-17T15:06:14.850641989
Content-Length: 233

{"id":"c1","name":"The Sad Onion","type":"Passive","statistics":[{"type":"Tears","value":"+0.7","gameVersions":null}],"description":"Tears Up","itemPools":["TreasureRoom","CraneGame"],"quality":3,"gameVersions":["Flash"],"note":null}
```
*Spindown item obtained is **The Sad Onion** with the id c1*

---
### Create a new Item

*id should be of this form **cX** replace *X* by the number of the item*

<details>
<summary>Available type</summary>
<ul>
    <li>Active</li>
    <li>Card</li>
    <li>Passive</li>
    <li>Pill</li>
    <li>Trinket</li>
</ul>
</details>

<details>
<summary>Available itemPools</summary>
<ul>
    <li>AngelRoom</li>
    <li>BabyShop</li>
    <li>BatteryBum</li>
    <li>Beggar</li>
    <li>BombBum</li>
    <li>Boss</li>
    <li>CraneGame</li>
    <li>CurseRoom</li>
    <li>DevilBeggar</li>
    <li>DevilRoom</li>
    <li>GoldenChest</li>
    <li>KeyMaster</li>
    <li>Library</li>
    <li>MomsChest</li>
    <li>OldChest</li>
    <li>Planetarium</li>
    <li>RedChest</li>
    <li>RottenBeggar</li>
    <li>SecretRoom</li>
    <li>ShellGame</li>
    <li>Shop</li>
    <li>TreasureRoom</li>
    <li>UltraSecretRoom</li>
    <li>WoodenChest</li>
</ul>
</details>

<details>
<summary>Available Qualities</summary>
<ul>
    <li>0</li>
    <li>1</li>
    <li>2</li>
    <li>3</li>
    <li>4</li>
</ul>
</details>

<details>
<summary>Available Statistics Type</summary>
<ul>
    <li>Damage</li>
    <li>Speed</li>
    <li>Tears</li>
    <li>Attack</li>
    <li>Range</li>
    <li>ShotSpeed</li>
    <li>Luck</li>
    <li>Size</li>
    <li>DevilDealChance</li>
    <li>AngelDealChance</li>
    <li>PlanetariumChance</li>
    <li>Health</li>
    <li>TearAmount</li>
    <li>TearEffect</li>
    <li>TearRate</li>
    <li>TearSize</li>
</ul>
</details>

<details>
<summary>Available GameVersion</summary>
<ul>
    <li>Flash</li>
    <li>Rebirth</li>
    <li>Afterbirth</li>
    <li>AfterbirthPlus</li>
    <li>Repentance</li>
    <li>RepentancePlus</li>
    <li>All</li>
</ul>
</details>

- **Command:**

```bash
curl -i -X POST \
     -H "Content-Type: application/json" \
     -d '{"id":"c90","name":"The Small Rock","type":"Passive","itemPools":["CraneGame"],"statistics":[{"type":"Damage","value": "+1"},{"type":"Speed","value": "-0.2"},{"type":"Tears","value": "+0.2"}],"description": "DMG up + speed down","quality":3,"gameVersions":["Rebirth"],"note": "Has a chance to drop when exploding a tinted rock"}' \
      http://localhost:8080/items
```
*Example create the new item **The Small Rock***

- Result:
```http response
HTTP/1.1 201 Created
Date: Fri, 17 Jan 2025 14:36:02 GMT
Content-Type: application/json
Last-Modified: 2025-01-17T15:36:02.111718491
Content-Length: 366

{"id":"c90","name":"The Small Rock","type":"Passive","statistics":[{"type":"Damage","value":"+1","gameVersions":null},{"type":"Speed","value":"-0.2","gameVersions":null},{"type":"Tears","value":"+0.2","gameVersions":null}],"description":null,"itemPools":["CraneGame"],"quality":3,"gameVersions":["Rebirth"],"note":"Has a chance to drop when exploding a tinted rock"}
```
---
### Update an Item

- **Command:**

```bash
curl -i -X PATCH \
     -H "Content-Type: application/json" \
     -d '{"name":"The Small Rocks","type":"Passive","itemPools":["CraneGame"],"statistics":[{"type":"Damage","value": "+1"},{"type":"Speed","value": "-0.2"},{"type":"Tears","value": "+0.2"}],"description": "DMG up + speed down","quality":3,"gameVersions":["Rebirth"],"note": "Has a chance to drop when exploding a tinted rock"}' \
      http://localhost:8080/item/c90
```
*Example update the new item The Small Rock change its name to **The Small Rocks***

- Result:
```http response
HTTP/1.1 200 OK
Date: Fri, 17 Jan 2025 14:44:31 GMT
Content-Type: application/json
Last-Modified: 2025-01-17T15:44:31.467168856
Content-Length: 367

{"id":"c90","name":"The Small Rocks","type":"Passive","statistics":[{"type":"Damage","value":"+1","gameVersions":null},{"type":"Speed","value":"-0.2","gameVersions":null},{"type":"Tears","value":"+0.2","gameVersions":null}],"description":null,"itemPools":["CraneGame"],"quality":3,"gameVersions":["Rebirth"],"note":"Has a chance to drop when exploding a tinted rock"}
```
---
### Delete an Item

- **Command:**

```bash
curl -i -X DELETE http://localhost:8080/item/c90
```
*Example delete the added item **The Small Rock***

- Result:
```http response
HTTP/1.1 204 No Content
Date: Fri, 17 Jan 2025 15:04:00 GMT
Content-Type: text/plain

```
---
### Query All Characters

- **Command:**

```bash
curl -i -X GET http://localhost:8080/characters
```

- Result:
```http response
HTTP/1.1 200 OK
Date: Fri, 17 Jan 2025 17:41:02 GMT
Content-Type: application/json
Last-Modified: 2025-01-17T18:40:59.993261487
Content-Length: 167

[{"id":"1","name":"Isaac","hearts":"3 red hearts","damage":3.5,"shotSpeed":1.0,"range":6.5,"speed":1.0,"luck":1.0,"startingPickups":"1 bomb","startingItems":"The D6"}]
```

---
### Query a Character

- **Command:**

```bash
curl -i -X GET http://localhost:8080/character/1
```
*Example query the character with the id **1***

- Result:
```http response
HTTP/1.1 200 OK
Date: Fri, 17 Jan 2025 17:43:28 GMT
Content-Type: application/json
Last-Modified: 2025-01-17T18:43:28.328415099
Content-Length: 165

{"id":"1","name":"Isaac","hearts":"3 red hearts","damage":3.5,"shotSpeed":1.0,"range":6.5,"speed":1.0,"luck":1.0,"startingPickups":"1 bomb","startingItems":"The D6"}
```
---
### Create a Character

*id should be a number, all the other field are Strings, there are no predefined type*

- **Command:**

```bash
curl -i -X POST \
     -H "Content-Type: application/json" \
     -d '{"id":"2","name":"Azazel","hearts":"3 black hearts","damage":5.25,"shotSpeed":1.0,"range":4.5,"speed":1.25,"luck":0.0,"startingPickups":"0 - The Fool","startingItems":"Short range Brimstone"}' \
     http://localhost:8080/characters
```
*Example create the character **Azazel***

- Result:
```http response
HTTP/1.1 201 Created
Date: Fri, 17 Jan 2025 17:57:26 GMT
Content-Type: application/json
Last-Modified: 2025-01-17T18:57:27.046896609
Content-Length: 191

{"id":"2","name":"Azazel","hearts":"3 black hearts","damage":5.25,"shotSpeed":1.0,"range":4.5,"speed":1.25,"luck":0.0,"startingPickups":"0 - The Fool","startingItems":"Short range Brimstone"}
```
---
### Update a Character

- **Command:**

```bash
curl -i -X PATCH \
     -H "Content-Type: application/json" \
     -d '{"id":"2","name":"Azazel","hearts":"4 black hearts","damage":5.25,"shotSpeed":1.0,"range":4.5,"speed":1.25,"luck":0.0,"startingPickups":"0 - The Fool","startingItems":"Short range Brimstone"}' \
     http://localhost:8080/character/2
```
*Example update the added character **Azazel** change 3 heart to 4 heats*

- Result:
```http response
HTTP/1.1 200 OK
Date: Fri, 17 Jan 2025 18:01:00 GMT
Content-Type: application/json
Last-Modified: 2025-01-17T19:01:00.584206
Content-Length: 191

{"id":"2","name":"Azazel","hearts":"4 black hearts","damage":5.25,"shotSpeed":1.0,"range":4.5,"speed":1.25,"luck":0.0,"startingPickups":"0 - The Fool","startingItems":"Short range Brimstone"}
```
---
### Delete a Character

- **Command:**

```bash
curl -i -X DELETE http://localhost:8080/character/2
```
*Example delete the added character **Azazel***

- Result:
```http response
HTTP/1.1 204 No Content
Date: Fri, 17 Jan 2025 18:02:01 GMT
Content-Type: text/plain

```
---
## Web infrastructures


