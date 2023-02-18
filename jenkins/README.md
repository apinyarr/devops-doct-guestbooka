# SIT Deployment

## Application tag

Please follows the steps below before performing SIT deployment
1. Commit changes to the code repository.
2. Tag current version using the command.
```
git tag <tagname>
```
*For example*
```
git tag DL-2
```
3. Publish all change to the remote repository.

## Start new container using the base image

1. Pull docker base image using the command.
```
docker pull <image>:<tag>
```
2. Create container using the command.
```
docker create <options> <image> <command> <args>
```
*For example*
```
docker create --name guestbooka-dl-2 -e MONGODB_ADDON_URI=mongodb://172.17.0.1:27017/admin -p 8080:8080 apinyarr/guestbooka:base
```
3. Clone the remote code repository and checkout to desired tagname.
4. Copy the desired code to the running directory inside the container using the command.
```
docker cp <option> <src_path> <container:dest_path>
```
*For example*
```
docker cp app/. guestbooka-dl-2:/
```

## Run the new container

1. Remove the previous backup container using the command.
```
docker rm <rollback_container>
```
2. Stop the old container using the command.
```
docker stop <option> <container>
```
*For example*
```
docker stop guestbooka
```
3. Rename the old container for rollback purpose using the command.
```
docker rename <old_name> <new_name>
```
*For example*
```
docker rename guestbooka guestbooka-bak
```
4. Rename the new container to standard name using the command.
```
docker rename <old_name> <new_name>
```
*For example*
```
docker rename guestbooka-dl-2 guestbooka
```
5. Start the container using the command.
```
docker start <option> <container>
```
*For example*
```
docker start guestbooka
```

# PRE and PRD Deployment

## Build and tag new image

We create new tags in the non production environment first, then we can use them to deploy in the production.
1. Check out source code to the version we would like to deploy using the command.
```
git checkout <tag name>
```
2. Change directory to the Dockerfile path.
3. Build and tag a new docker images using the command.
```
docker built -t <repository>:<tag> --no-cache .
```
4. Push a new docker image to the registry (DockerHub)
```
docker push <repository>:<tag>
```

## Deploy a new container

1. Pull a new image using the command.
```
docker pull <image>:<tag>
```
2. Stop and rename the running container for a backup (in case we cannot redeploy the older version).
```
docker stop <container name>
docker rename <old name> <old name>-bak
```
3. Run a new container.
```
docker run --name <container name> -d -e <env variable>=<value> -p <host port>:<container port> <image>:<tag>
```
*For example*
```
docker run --name guestbooka -d -e MONGODB_ADDON_URI=mongodb://172.17.0.1:27017/admin -p 8080:8080 apinyarr/guestbooka:v1.0.0
```
4. Test liveness of the application
```
(curl -I http://localhost:8080 | grep "200 OK") > /dev/null 2>&1 && echo yes || echo no
```
5. Remove the backup container
```
docker rm <container name>
```