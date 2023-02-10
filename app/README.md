# Guestbook A microservice

## Docker build command

```
docker build -t apinyarr/guestbooka:base .
```

## Testing docker container on local

### Run MongoDB container
```
docker run --name mongodbapp -d -p 27017:27017 mongo:6.0.4-jammy
```

### Run guestbooka container
```
docker run --name guestbooka -d -e MONGODB_ADDON_URI=mongodb://172.17.0.1:27017/admin -p 8080:8080 apinyarr/guestbooka:base
```

### Test step
Open http://localhost:8080 in your browser.