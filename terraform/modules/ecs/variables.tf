variable "environment" {
  description = "Environment name"
  type        = string
}

variable "aws_region" {
  description = "AWS region"
  type        = string
}

variable "vpc_id" {
  description = "VPC ID"
  type        = string
}

variable "private_subnet_ids" {
  description = "List of private subnet IDs for ECS tasks"
  type        = list(string)
}

variable "ecr_repository_url" {
  description = "ECR repository URL"
  type        = string
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

variable "container_cpu" {
  description = "CPU units for the container"
  type        = number
}

variable "container_memory" {
  description = "Memory (MB) for the container"
  type        = number
}

variable "desired_count" {
  description = "Desired number of ECS tasks"
  type        = number
}

variable "alb_target_group_arn" {
  description = "ARN of the ALB target group"
  type        = string
}

variable "alb_security_group_id" {
  description = "Security group ID of the ALB"
  type        = string
}

variable "use_existing_cluster" {
  description = "Whether to use an existing ECS cluster"
  type        = bool
  default     = false
}

variable "existing_cluster_name" {
  description = "Name of existing ECS cluster (if use_existing_cluster is true)"
  type        = string
  default     = ""
}
