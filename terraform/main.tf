terraform {
  required_version = ">= 1.5.0"

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = var.aws_region

  default_tags {
    tags = {
      Project     = "ms-sp-salesforce-config"
      Environment = var.environment
      ManagedBy   = "Terraform"
    }
  }
}

# Módulo VPC: referencia VPC y subnets existentes
module "vpc" {
  source = "./modules/vpc"

  environment    = var.environment
  vpc_id         = var.vpc_id
  app_subnet_ids = var.app_subnet_ids
  db_subnet_ids  = var.db_subnet_ids
}

# Módulo ECR: repositorio de imágenes
module "ecr" {
  source = "./modules/ecr"

  environment     = var.environment
  repository_name = var.repository_name
}

# Módulo ALB: usa ALB existente, crea target group y listener rule
module "alb" {
  source = "./modules/alb"

  environment            = var.environment
  vpc_id                 = module.vpc.vpc_id
  existing_alb_name      = var.existing_alb_name
  listener_port          = var.alb_listener_port
  listener_rule_priority = var.listener_rule_priority
}

# Módulo ECS: task definition y service
module "ecs" {
  source = "./modules/ecs"

  environment            = var.environment
  vpc_id                 = module.vpc.vpc_id
  private_subnet_ids     = var.app_subnet_ids
  ecr_repository_url     = module.ecr.repository_url
  salesforce_credentials = var.salesforce_credentials
  container_cpu          = var.container_cpu
  container_memory       = var.container_memory
  desired_count          = var.desired_count
  alb_target_group_arn   = module.alb.target_group_arn
  alb_security_group_id  = module.alb.alb_security_group_id

  # Usar cluster ECS existente
  use_existing_cluster  = var.use_existing_ecs_cluster
  existing_cluster_name = var.existing_ecs_cluster_name
}
