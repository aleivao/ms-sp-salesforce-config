variable "environment" {
  description = "Environment name"
  type        = string
}

variable "vpc_id" {
  description = "ID of existing VPC"
  type        = string
}

variable "app_subnet_ids" {
  description = "List of existing app subnet IDs"
  type        = list(string)
}

variable "db_subnet_ids" {
  description = "List of existing database subnet IDs"
  type        = list(string)
}
