terraform {
  required_providers {
    docker = {
      source  = "kreuzwerker/docker"
      version = "~> 2.13.0"
    }
  }
}

provider "docker" {}

resource "docker_image" "carbonlog-backend" {
  name         = "thomasburguiere/carbonlog-backend:latest"
  keep_locally = false
}

resource "docker_container" "carbonlog-backend" {
  image = docker_image.carbonlog-backend.latest
  name  = "carbonlog-backend"
  ports {
    internal = 8080
    external = 8080
  }
  env = [
    "mongourl.measurements.db-name=carbon_measurements",
    "mongourl.measurements.url=mongodb://host.docker.internal:27017/carbon_measurements"
  ]
}
