# build & push to GCP artifact repository
image_name='europe-west6-docker.pkg.dev/carbon-logger/docker-images/carbonlog-backend:0.0.3'

docker build . -t $image_name
docker push $image_name
