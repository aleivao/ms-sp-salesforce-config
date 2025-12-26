variable "environment" {
  description = "Environment name"
  type        = string
}

variable "vpc_id" {
  description = "VPC ID"
  type        = string
}

variable "existing_alb_name" {
  description = "Name of existing ALB to use"
  type        = string
}

variable "listener_port" {
  description = "Port of the existing listener"
  type        = number
  default     = 8080
}

variable "listener_rule_priority" {
  description = "Priority for the listener rule (must be unique)"
  type        = number
  default     = 100
}
