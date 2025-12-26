# Backend S3 - descomentar cuando el bucket exista en la cuenta destino
# terraform {
#   backend "s3" {
#     bucket         = "aje-terraform-state"
#     key            = "ms-sp-salesforce-config/terraform.tfstate"
#     region         = "us-east-2"
#     encrypt        = true
#     dynamodb_table = "terraform-lock"
#   }
# }
