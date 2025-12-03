# CampusCoffee (WS 25/26)

## Prerequisites

* Install [Docker Desktop](https://www.docker.com/products/docker-desktop/) or a compatible open-source alternative such as [Rancher Desktop](https://rancherdesktop.io/).
* Install the [Temurin JDK 21](https://adoptium.net/temurin/releases/?version=21&os=any&arch=any) and [Maven 3.9](https://maven.apache.org/install.html) either via the provided [`mise.toml`](mise.toml) file (see [getting started guide](https://mise.jdx.dev/getting-started.html) for details) or directly via your favorite package manager. If you use `mise`, run `mise trust mise.toml` and then `mise install` in the project root to set up the required tool versions.
* Install a Java IDE. We recommend [IntelliJ](https://www.jetbrains.com/idea/), but you are free to use alternatives such as [VS Code](https://code.visualstudio.com/) with suitable extensions.
* Import the project into your IDE. In IntelliJ, you can do this via `File` -> `New` -> `Project from Existing Sources`. Select the root-level `pom.xml` and import the project. If you have the `mise` [plugin](https://plugins.jetbrains.com/plugin/24904-mise) installed, IntelliJ will ask you to select the appropriate tool versions.
* Ensure that your IDE as initialized the project correctly, including all `src`, `test`, and `resources` folders.

## Build application

First, make sure that the Docker daemon is running.
Then, to build the application, run the following command in the command line (or use the Maven integration of your IDE):

```shell
mvn clean install
```
**Note:** In the `dev` profile, all repositories are cleared before startup, the initial data is loaded (see [`LoadInitialData.java`](application/src/main/java/de/seuhd/campuscoffee/LoadInitialData.java)).

You can use the quiet mode to suppress most log messages:

```shell
mvn clean install -q
```

## Start application (dev)

First, make sure that the Docker daemon is running.
Before you start the application, you first need to start a Postgres docker container:

```shell
docker run -d --name db -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 postgres:17-alpine
```

Then, you can start the application:

```shell
cd application
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```
**Note:** The data source is configured via the [`application.yaml`](application/src/main/resources/application.yaml) file.

## REST API

You can use `curl` in the command line to send HTTP requests to the REST API.

### POS endpoint

#### Get POS

All POS:
```shell
curl http://localhost:8080/api/pos
```

POS by ID:
```shell
curl http://localhost:8080/api/pos/1 # add valid POS id here
```

POS by name:
```shell
curl http://localhost:8080/api/pos/filter?name=Schmelzpunkt # add valid POS name here
```

#### Create POS

Create a POS based on a JSON object provided in the request body:

```shell
curl --request POST --header "Content-Type: application/json" --data '{"name":"New Café","description":"Description","type":"CAFE","campus":"ALTSTADT","street":"Hauptstraße","houseNumber":"100","postalCode":69117,"city":"Heidelberg"}' http://localhost:8080/api/pos
```

Create a POS based on an OpenStreetMap node:

```shell
curl --request POST http://localhost:8080/api/pos/import/osm/5589879349?campus_type=ALTSTADT # set a valid OSM node ID here
```

IDs for testing:
* 5589879349 (Rada Coffee & Rösterei in ALTSTADT)
* 1864600258 (La Fée in ALTSTADT)
* 1864600236 (Café Moro in ALTSTADT) --> missing address

See bean validation in action:

```shell
curl --header "Content-Type: application/json" --request POST -i --data '{"name":"","description":"","type":"CAFE","campus":"ALTSTADT","street":"Hauptstraße","houseNumber":"100","postalCode":69117,"city":"Heidelberg"}' http://localhost:8080/api/pos
```

#### Update POS

Update title and description:
```shell
curl --header "Content-Type: application/json" --request PUT --data '{"id":4,"name":"New coffee","description":"Great croissants","type":"CAFE","campus":"ALTSTADT","street":"Hauptstraße","houseNumber":"95","postalCode":69117,"city":"Heidelberg"}' http://localhost:8080/api/pos/4 # set correct POS id here and in the body
```

#### Delete POS

Delete POS by ID:
```shell
curl --request DELETE -i http://localhost:8080/api/pos/1 # set existing POS ID here
```

### Users endpoint

#### Get users

All users:
```shell
curl http://localhost:8080/api/users
```

User by ID:
```shell
curl http://localhost:8080/api/users/1 # add valid user id here
```

User by login name:
```shell
curl http://localhost:8080/api/users/filter?login_name=jane_doe # add valid user login name here
```

#### Create users

```shell
curl --header "Content-Type: application/json" --request POST --data '{"loginName":"other_login_name","emailAddress":"other.person@uni-heidelberg.de","firstName":"New","lastName":"Person"}' http://localhost:8080/api/users
```

See bean validation in action:
```shell
curl --header "Content-Type: application/json" --request POST -i --data '{"loginName":"other_login_name!","emailAddress":"other.personATuni-heidelberg.de","firstName":"","lastName":""}' http://localhost:8080/api/users
```

#### Update user

Update the login name and the email address:
```shell
curl --header "Content-Type: application/json" --request PUT --data '{"id":1,"createdAt":"2025-06-03T12:00:00","updatedAt":"2025-06-03T12:00:00","loginName":"jane_doe_new","emailAddress":"jane.doe.new@uni-heidelberg.de","firstName":"Jane","lastName":"Doe"}' http://localhost:8080/api/users/1 # set correct user id here and in the body
```

#### Delete user

Delete user by ID:
```shell
curl --request DELETE -i http://localhost:8080/api/users/1 # set existing POS ID here
```

### Reviews endpoint

#### Get reviews

All reviews:
```shell
curl http://localhost:8080/api/reviews
```

Review by ID:
```shell
curl http://localhost:8080/api/reviews/1 # add valid user id here
```

Get approved reviews for a POS:
```shell
curl http://localhost:8080/api/reviews/filter?pos_id=1&approved=true # add valid POS id here
```

#### Create reviews

```shell
curl --header "Content-Type: application/json" --request POST --data '{"posId":2,"authorId":1,"review":"Great place!"}' http://localhost:8080/api/reviews # use existing IDs for posId and authorId
```

Users cannot create more than one review per POS:
```shell
curl --header "Content-Type: application/json" --request POST --data '{"posId":2,"authorId":1,"review":"Great place!"}' http://localhost:8080/api/reviews # use existing IDs for posId and authorId
```

#### Approve reviews

Users cannot approve their own reviews:
```shell
curl --request PUT http://localhost:8080/api/reviews/4/approve?user_id=1 # use existing review ID and user ID (of the author)
```

However, users can approve the same review multiple times (which is a limitation of the current implementation):
```shell
curl --request PUT http://localhost:8080/api/reviews/4/approve?user_id=2 # use existing review ID and user ID (different from author)
```
```shell
curl --request PUT http://localhost:8080/api/reviews/4/approve?user_id=2 # use existing review ID and user ID (different from author)
```
```shell
curl --request PUT http://localhost:8080/api/reviews/4/approve?user_id=2 # use existing review ID and user ID (different from author)
```

## Docker

### Building an image from the Dockerfile

```shell
docker build -t campus-coffee:latest .
```

#### Manually create and run a Docker container based on the created image

First, create a new Docker network named `campus-coffee-net`,
then run a Postgres container and connect it to `campus-coffee-net`.
Finally, run the container with the application (in `dev` profile, do not use in production),
connect it to the network, and configure the application
to use the database provided in the started Postgres container.

```shell
docker network create campus-coffee-net 2>/dev/null || true
docker rm -f db 2>/dev/null || true
docker run -d --name db --net campus-coffee-net -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 postgres:16-alpine
docker run --net campus-coffee-net -e SPRING_PROFILES_ACTIVE=dev -e SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/postgres -p 8080:8080  -it --rm campus-coffee:latest
```

Explanation of selected options:

`docker run -p 8080:8080 ` runs the container with port 8080 exposed to the host machine. You can change the port mapping if needed.
`docker run ... -it`  runs a container in interactive mode with a pseudo-TTY (terminal).
`docker run ... --rm` automatically removes the container (and its associated resources) if it exists already.

#### Use Docker compose to run the app container together with the DB container

Build container image:

```shell
docker compose build
```

Delete existing DB container (if you manually created it before):

```shell
docker rm -f db 2>/dev/null || true
```

Create and start containers:

```shell
docker compose down && docker compose up
```

Stop and remove containers and networks:

```shell
docker compose down
```
