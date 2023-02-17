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