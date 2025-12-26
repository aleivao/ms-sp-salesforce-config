#!/bin/bash

# ECS Module
cat > terraform/modules/ecs/main.tf << 'EOF'
resource "aws_ecs_cluster" "main" {
  name = "${var.environment}-ms-sp-salesforce-config"
  
  setting {
    name  = "containerInsights"
    value = "enabled"
  }
}

resource "aws_ecs_task_definition" "main" {
  family                   = "${var.environment}-ms-sp-salesforce-config"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = var.container_cpu
  memory                   = var.container_memory
  execution_role_arn       = aws_iam_role.ecs_execution.arn
  task_role_arn            = aws_iam_role.ecs_task.arn
  
  container_definitions = jsonencode([{
    name  = "ms-sp-salesforce-config"
    image = "${var.ecr_repository_url}:${var.environment}-latest"
    
    environment = [
      { name = "SPRING_PROFILES_ACTIVE", value = var.environment }
    ]
    
    secrets = [
      { name = "SALESFORCE_USERNAME", valueFrom = aws_secretsmanager_secret.salesforce.arn ":username::" },
      { name = "SALESFORCE_PASSWORD", valueFrom = aws_secretsmanager_secret.salesforce.arn ":password::" },
      { name = "SALESFORCE_CLIENT_ID", valueFrom = aws_secretsmanager_secret.salesforce.arn ":client_id::" },
      { name = "SALESFORCE_CLIENT_SECRET", valueFrom = aws_secretsmanager_secret.salesforce.arn ":client_secret::" }
    ]
    
    portMappings = [{
      containerPort = 8080
      protocol      = "tcp"
    }]
    
    logConfiguration = {
      logDriver = "awslogs"
      options = {
        "awslogs-group"         = aws_cloudwatch_log_group.main.name
        "awslogs-region"        = "us-east-2"
        "awslogs-stream-prefix" = "ecs"
      }
    }
    
    healthCheck = {
      command     = ["CMD-SHELL", "curl -f http://localhost:8080/actuator/health || exit 1"]
      interval    = 30
      timeout     = 5
      retries     = 3
      startPeriod = 60
    }
  }])
}

resource "aws_ecs_service" "main" {
  name            = "${var.environment}-ms-sp-salesforce-config-service"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.main.arn
  desired_count   = var.desired_count
  launch_type     = "FARGATE"
  
  network_configuration {
    subnets          = var.private_subnet_ids
    security_groups  = [aws_security_group.ecs_tasks.id]
    assign_public_ip = false
  }
  
  load_balancer {
    target_group_arn = var.alb_target_group_arn
    container_name   = "ms-sp-salesforce-config"
    container_port   = 8080
  }
  
  depends_on = [aws_iam_role.ecs_execution]
}

resource "aws_security_group" "ecs_tasks" {
  name        = "${var.environment}-ecs-tasks-sg"
  description = "Security group for ECS tasks"
  vpc_id      = var.vpc_id
  
  ingress {
    from_port       = 8080
    to_port         = 8080
    protocol        = "tcp"
    security_groups = [var.alb_security_group_id]
  }
  
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}
EOF

cat > terraform/modules/ecs/iam.tf << 'EOF'
resource "aws_iam_role" "ecs_execution" {
  name = "${var.environment}-ecs-execution-role"
  
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Action = "sts:AssumeRole"
      Effect = "Allow"
      Principal = {
        Service = "ecs-tasks.amazonaws.com"
      }
    }]
  })
}

resource "aws_iam_role_policy_attachment" "ecs_execution" {
  role       = aws_iam_role.ecs_execution.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

resource "aws_iam_role" "ecs_task" {
  name = "${var.environment}-ecs-task-role"
  
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Action = "sts:AssumeRole"
      Effect = "Allow"
      Principal = {
        Service = "ecs-tasks.amazonaws.com"
      }
    }]
  })
}
EOF

cat > terraform/modules/ecs/cloudwatch.tf << 'EOF'
resource "aws_cloudwatch_log_group" "main" {
  name              = "/ecs/ms-sp-salesforce-config-${var.environment}"
  retention_in_days = 30
}
EOF

cat > terraform/modules/ecs/secrets.tf << 'EOF'
resource "aws_secretsmanager_secret" "salesforce" {
  name = "${var.environment}/salesforce-credentials"
}

resource "aws_secretsmanager_secret_version" "salesforce" {
  secret_id = aws_secretsmanager_secret.salesforce.id
  secret_string = jsonencode(var.salesforce_credentials)
}
EOF

cat > terraform/modules/ecs/variables.tf << 'EOF'
variable "environment" {
  type = string
}

variable "vpc_id" {
  type = string
}

variable "private_subnet_ids" {
  type = list(string)
}

variable "ecr_repository_url" {
  type = string
}

variable "salesforce_credentials" {
  type = object({
    username      = string
    password      = string
    client_id     = string
    client_secret = string
  })
  sensitive = true
}

variable "container_cpu" {
  type = number
}

variable "container_memory" {
  type = number
}

variable "desired_count" {
  type = number
}

variable "alb_target_group_arn" {
  type = string
}

variable "alb_security_group_id" {
  type = string
}
EOF

cat > terraform/modules/ecs/outputs.tf << 'EOF'
output "cluster_name" {
  value = aws_ecs_cluster.main.name
}
EOF

# ALB Module
cat > terraform/modules/alb/main.tf << 'EOF'
resource "aws_lb" "main" {
  name               = "${var.environment}-alb"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.alb.id]
  subnets            = var.public_subnet_ids
  
  enable_deletion_protection = var.environment == "prod"
}

resource "aws_lb_target_group" "main" {
  name        = "${var.environment}-tg"
  port        = 8080
  protocol    = "HTTP"
  vpc_id      = var.vpc_id
  target_type = "ip"
  
  health_check {
    enabled             = true
    healthy_threshold   = 2
    interval            = 30
    matcher             = "200"
    path                = "/actuator/health"
    port                = "traffic-port"
    protocol            = "HTTP"
    timeout             = 5
    unhealthy_threshold = 3
  }
  
  deregistration_delay = 30
}

resource "aws_lb_listener" "main" {
  load_balancer_arn = aws_lb.main.arn
  port              = "80"
  protocol          = "HTTP"
  
  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.main.arn
  }
}
EOF

cat > terraform/modules/alb/security-groups.tf << 'EOF'
resource "aws_security_group" "alb" {
  name        = "${var.environment}-alb-sg"
  description = "Security group for ALB"
  vpc_id      = var.vpc_id
  
  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  
  ingress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}
EOF

cat > terraform/modules/alb/variables.tf << 'EOF'
variable "environment" {
  type = string
}

variable "vpc_id" {
  type = string
}

variable "public_subnet_ids" {
  type = list(string)
}
EOF

cat > terraform/modules/alb/outputs.tf << 'EOF'
output "alb_dns_name" {
  value = aws_lb.main.dns_name
}

output "target_group_arn" {
  value = aws_lb_target_group.main.arn
}

output "alb_security_group_id" {
  value = aws_security_group.alb.id
}
EOF

# QA and PROD tfvars
cat > terraform/environments/qa/terraform.tfvars << 'EOF'
environment = "qa"
vpc_cidr    = "10.1.0.0/16"
availability_zones = ["us-east-2a", "us-east-2b"]
desired_count = 2
EOF

cat > terraform/environments/qa/backend-config.tfvars << 'EOF'
bucket = "aje-terraform-state"
key    = "ms-sp-salesforce-config/qa/terraform.tfstate"
region = "us-east-2"
EOF

cat > terraform/environments/prod/terraform.tfvars << 'EOF'
environment = "prod"
vpc_cidr    = "10.2.0.0/16"
availability_zones = ["us-east-2a", "us-east-2b"]
desired_count = 3
container_cpu = 1024
container_memory = 2048
EOF

cat > terraform/environments/prod/backend-config.tfvars << 'EOF'
bucket = "aje-terraform-state"
key    = "ms-sp-salesforce-config/prod/terraform.tfstate"
region = "us-east-2"
EOF

echo "Remaining Terraform files created!"
