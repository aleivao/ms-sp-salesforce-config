variable "aws_region" {
  description = "AWS region"
  type        = string
  default     = "us-east-2"
}

variable "environment" {
  description = "Environment name"
  type        = string
}

variable "vpc_id" {
  description = "ID of existing VPC"
  type        = string
}

variable "app_subnet_ids" {
  description = "List of existing app subnet IDs for ECS tasks"
  type        = list(string)
}

variable "db_subnet_ids" {
  description = "List of existing database subnet IDs"
  type        = list(string)
  default     = []
}

variable "repository_name" {
  description = "ECR repository name"
  type        = string
  default     = "ms-sp-salesforce-config"
}

variable "container_cpu" {
  description = "CPU units for container"
  type        = number
  default     = 512
}

variable "container_memory" {
  description = "Memory for container"
  type        = number
  default     = 1024
}

variable "desired_count" {
  description = "Desired number of tasks"
  type        = number
  default     = 2
}

variable "salesforce_credentials" {
  description = "Salesforce credentials"
  type = object({
    username      = string
    password      = string
    client_id     = string
    client_secret = string
  })
  sensitive = true
}

variable "use_existing_ecs_cluster" {
  description = "Whether to use an existing ECS cluster"
  type        = bool
  default     = false
}

variable "existing_ecs_cluster_name" {
  description = "Name of existing ECS cluster (if use_existing_ecs_cluster is true)"
  type        = string
  default     = ""
}
