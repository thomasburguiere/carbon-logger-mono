provider "google" {
  project = "carbon-logger"
  # credentials = "./local-key.json"
}

// enables cloud run api for the project
resource "google_project_service" "cloud_run_service" {
  project = google_project.carbon_logger_project.project_id
  service = "run.googleapis.com"
}

// enables iam api for the project
resource "google_project_service" "iam_service" {
  project = google_project.carbon_logger_project.project_id
  service = "iam.googleapis.com"
}

// enables cloud resource manager api for the project
resource "google_project_service" "cloudresourcemanager_service" {
  project = google_project.carbon_logger_project.project_id
  service = "cloudresourcemanager.googleapis.com"
}

// enables serviceusage api for the project
resource "google_project_service" "serviceusage_service" {
  project = google_project.carbon_logger_project.project_id
  service = "serviceusage.googleapis.com"
}

resource "google_project" "carbon_logger_project" {
  project_id      = "carbon-logger"
  name            = "carbon-logger"
  billing_account = "019478-C054BA-BFFB44"
}

resource "google_service_account" "carbon_logger_service_account" {
  account_id   = "clogger-service-account"
  display_name = "Carbon Logger Service Account"
  # email        = "clogger-service-account@carbon-logger.iam.gserviceaccount.com"
}


# resource "google_cloud_run_service" "carbon_logger_mongo" {
#   name     = "carbonlog-mongo"
#   location = "europe-west6"
#   project  = google_project.carbon_logger_project.project_id

#   template {


#     spec {
#       container_concurrency = 80
#       service_account_name  = "clogger-service-account@carbon-logger.iam.gserviceaccount.com"
#       timeout_seconds       = 300

#       containers {
#         image = "europe-west6-docker.pkg.dev/carbon-logger/docker-images/carbonlog-backend@sha256:e027dde96b62a6bfc9a1f4152bcb9c92e68d4d5e6cf5970d5ee379d84e641a5f"

#         env {
#           name = "mongourl.measurements.url"

#           value_from {
#             secret_key_ref {
#               key  = "1"
#               name = "carbonlog-mongo-admin-url"
#             }
#           }
#         }
#         env {
#           name = "static.auth.token"

#           value_from {
#             secret_key_ref {
#               key  = "1"
#               name = "carbonlog-backend-static-token"
#             }
#           }
#         }

#         ports {
#           container_port = 8080
#           name           = "http1"
#         }
#       }
#     }
#   }
# }
