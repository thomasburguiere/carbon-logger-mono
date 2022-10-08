# build & push to GCP artifact repository
docker build . -t europe-west6-docker.pkg.dev/carbon-logger/docker-images/carbonlog-backend
docker push europe-west6-docker.pkg.dev/carbon-logger/docker-images/carbonlog-backend
