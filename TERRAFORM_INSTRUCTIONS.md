# Archivos de Terraform - Instrucciones de Creación

Este proyecto incluye la estructura básica de Terraform. A continuación se detallan los archivos adicionales que debes crear para los módulos ECS y ALB.

## Módulo ECS (terraform/modules/ecs/)

### main.tf
```hcl
resource "aws_ecs_cluster" "main" {
  name = "aje-cluster-${var.environment}"
  
  setting {
    name  = "containerInsights"
    value = "enabled"
  }
  
  tags = var.tags
}

resource "aws_ecs_task_definition" "app" {
  family                   = "${var.app_name}-${var.environment}"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = var.cpu
  memory                   = var.memory
  execution_role_arn       = aws_iam_role.ecs_execution.arn
  task_role_arn            = aws_iam_role.ecs_task.arn
  
  container_definitions = jsonencode([{
    name  = var.app_name
    image = "${var.ecr_repository_url}:${var.environment}-latest"
    
    portMappings = [{
      containerPort = var.container_port
      protocol      = "tcp"
    }]
    
    environment = [
      {
        name  = "SPRING_PROFILES_ACTIVE"
        value = var.environment
      },
      {
        name  = "SALESFORCE_URL"
        value = var.salesforce_url
      },
      {
        name  = "SALESFORCE_USERNAME"
        value = var.salesforce_username
      }
    ]
    
    secrets = [
      {
        name      = "SALESFORCE_PASSWORD"
        valueFrom = "${aws_secretsmanager_secret.salesforce.arn}:password::"
      },
      {
        name      = "SALESFORCE_CLIENT_ID"
        valueFrom = "${aws_secretsmanager_secret.salesforce.arn}:clientId::"
      },
      {
        name      = "SALESFORCE_CLIENT_SECRET"
        valueFrom = "${aws_secretsmanager_secret.salesforce.arn}:clientSecret::"
      },
      {
        name      = "SALESFORCE_SECURITY_TOKEN"
        valueFrom = "${aws_secretsmanager_secret.salesforce.arn}:securityToken::"
      }
    ]
    
    logConfiguration = {
      logDriver = "awslogs"
      options = {
        "awslogs-group"         = aws_cloudwatch_log_group.app.name
        "awslogs-region"        = data.aws_region.current.name
        "awslogs-stream-prefix" = "ecs"
      }
    }
    
    healthCheck = {
      command     = ["CMD-SHELL", "wget --quiet --tries=1 --spider http://localhost:${var.container_port}/actuator/health || exit 1"]
      interval    = 30
      timeout     = 5
      retries     = 3
      startPeriod = 60
    }
  }])
  
  tags = var.tags
}

resource "aws_ecs_service" "app" {
  name            = "${var.app_name}-service-${var.environment}"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.app.arn
  desired_count   = var.desired_count
  launch_type     = "FARGATE"
  
  network_configuration {
    subnets          = var.private_subnet_ids
    security_groups  = [aws_security_group.ecs_tasks.id]
    assign_public_ip = false
  }
  
  load_balancer {
    target_group_arn = var.alb_target_group_arn
    container_name   = var.app_name
    container_port   = var.container_port
  }
  
  depends_on = [aws_iam_role.ecs_execution]
  
  tags = var.tags
}
```

### iam.tf
```hcl
resource "aws_iam_role" "ecs_execution" {
  name = "ecs-execution-role-${var.environment}"
  
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
  
  tags = var.tags
}

resource "aws_iam_role_policy_attachment" "ecs_execution" {
  role       = aws_iam_role.ecs_execution.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

resource "aws_iam_role_policy" "ecs_execution_secrets" {
  name = "ecs-execution-secrets-${var.environment}"
  role = aws_iam_role.ecs_execution.id
  
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Effect = "Allow"
      Action = [
        "secretsmanager:GetSecretValue"
      ]
      Resource = [aws_secretsmanager_secret.salesforce.arn]
    }]
  })
}

resource "aws_iam_role" "ecs_task" {
  name = "ecs-task-role-${var.environment}"
  
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
  
  tags = var.tags
}
```

### cloudwatch.tf
```hcl
resource "aws_cloudwatch_log_group" "app" {
  name              = "/ecs/${var.app_name}-${var.environment}"
  retention_in_days = var.environment == "prod" ? 30 : 7
  
  tags = var.tags
}
```

### secrets.tf
```hcl
resource "aws_secretsmanager_secret" "salesforce" {
  name = "${var.environment}/salesforce-credentials"
  description = "Salesforce credentials for ${var.environment}"
  
  tags = var.tags
}

resource "aws_secretsmanager_secret_version" "salesforce" {
  secret_id = aws_secretsmanager_secret.salesforce.id
  secret_string = jsonencode({
    password      = "PLACEHOLDER_UPDATE_MANUALLY"
    clientId      = "PLACEHOLDER_UPDATE_MANUALLY"
    clientSecret  = "PLACEHOLDER_UPDATE_MANUALLY"
    securityToken = "PLACEHOLDER_UPDATE_MANUALLY"
  })
  
  lifecycle {
    ignore_changes = [secret_string]
  }
}
```

### security-groups.tf
```hcl
resource "aws_security_group" "ecs_tasks" {
  name        = "ecs-tasks-sg-${var.environment}"
  description = "Security group for ECS tasks"
  vpc_id      = var.vpc_id
  
  ingress {
    from_port       = var.container_port
    to_port         = var.container_port
    protocol        = "tcp"
    security_groups = [var.alb_security_group_id]
  }
  
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
  
  tags = merge(
    var.tags,
    {
      Name = "ecs-tasks-sg-${var.environment}"
    }
  )
}
```

### variables.tf
```hcl
variable "environment" { type = string }
variable "app_name" { type = string }
variable "vpc_id" { type = string }
variable "private_subnet_ids" { type = list(string) }
variable "alb_security_group_id" { type = string }
variable "alb_target_group_arn" { type = string }
variable "ecr_repository_url" { type = string }
variable "container_port" { type = number }
variable "desired_count" { type = number }
variable "cpu" { type = string }
variable "memory" { type = string }
variable "salesforce_url" { type = string }
variable "salesforce_username" { type = string }
variable "tags" { type = map(string); default = {} }
```

### outputs.tf
```hcl
output "cluster_name" {
  value = aws_ecs_cluster.main.name
}

output "service_name" {
  value = aws_ecs_service.app.name
}

output "task_definition_arn" {
  value = aws_ecs_task_definition.app.arn
}
```

## Módulo ALB (terraform/modules/alb/)

### main.tf
```hcl
resource "aws_lb" "main" {
  name               = "salesforce-alb-${var.environment}"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.alb.id]
  subnets            = var.public_subnet_ids
  
  enable_deletion_protection = var.environment == "prod"
  
  tags = merge(
    var.tags,
    {
      Name = "salesforce-alb-${var.environment}"
    }
  )
}

resource "aws_lb_target_group" "app" {
  name        = "salesforce-tg-${var.environment}"
  port        = var.app_port
  protocol    = "HTTP"
  vpc_id      = var.vpc_id
  target_type = "ip"
  
  health_check {
    enabled             = true
    healthy_threshold   = 2
    unhealthy_threshold = 3
    timeout             = 5
    interval            = 30
    path                = var.health_check_path
    matcher             = "200"
  }
  
  tags = var.tags
}

resource "aws_lb_listener" "http" {
  load_balancer_arn = aws_lb.main.arn
  port              = "80"
  protocol          = "HTTP"
  
  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.app.arn
  }
}
```

### security-groups.tf
```hcl
resource "aws_security_group" "alb" {
  name        = "alb-sg-${var.environment}"
  description = "Security group for ALB"
  vpc_id      = var.vpc_id
  
  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
  
  tags = merge(
    var.tags,
    {
      Name = "alb-sg-${var.environment}"
    }
  )
}
```

### variables.tf
```hcl
variable "environment" { type = string }
variable "vpc_id" { type = string }
variable "public_subnet_ids" { type = list(string) }
variable "app_port" { type = number }
variable "health_check_path" { type = string }
variable "tags" { type = map(string); default = {} }
```

### outputs.tf
```hcl
output "alb_dns_name" {
  value = aws_lb.main.dns_name
}

output "alb_arn" {
  value = aws_lb.main.arn
}

output "target_group_arn" {
  value = aws_lb_target_group.app.arn
}

output "alb_security_group_id" {
  value = aws_security_group.alb.id
}
```

## Data Sources

Agrega al main.tf de cada módulo que lo necesite:
```hcl
data "aws_region" "current" {}
data "aws_caller_identity" "current" {}
```

## Notas Importantes

1. **Secrets Manager**: Después del primer `terraform apply`, actualiza manualmente los secrets en AWS Secrets Manager con las credenciales reales de Salesforce.

2. **Auto-scaling**: Para producción, considera agregar auto-scaling policies basadas en CPU o memoria.

3. **HTTPS**: Para producción, configura un certificado SSL en ACM y agrega un listener HTTPS al ALB.

4. **Monitoring**: Los logs están configurados en CloudWatch. Considera agregar alarmas para monitoreo proactivo.
