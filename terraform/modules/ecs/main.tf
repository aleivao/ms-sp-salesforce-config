# Cluster ECS: crear nuevo o usar existente
resource "aws_ecs_cluster" "main" {
  count = var.use_existing_cluster ? 0 : 1
  name  = "${var.environment}-ms-sp-salesforce-config"

  setting {
    name  = "containerInsights"
    value = "enabled"
  }
}

data "aws_ecs_cluster" "existing" {
  count        = var.use_existing_cluster ? 1 : 0
  cluster_name = var.existing_cluster_name
}

locals {
  cluster_id  = var.use_existing_cluster ? data.aws_ecs_cluster.existing[0].id : aws_ecs_cluster.main[0].id
  cluster_arn = var.use_existing_cluster ? data.aws_ecs_cluster.existing[0].arn : aws_ecs_cluster.main[0].arn
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
      { name = "SALESFORCE_USERNAME", valueFrom = "${aws_secretsmanager_secret.salesforce.arn}:username::" },
      { name = "SALESFORCE_PASSWORD", valueFrom = "${aws_secretsmanager_secret.salesforce.arn}:password::" },
      { name = "SALESFORCE_CLIENT_ID", valueFrom = "${aws_secretsmanager_secret.salesforce.arn}:client_id::" },
      { name = "SALESFORCE_CLIENT_SECRET", valueFrom = "${aws_secretsmanager_secret.salesforce.arn}:client_secret::" },
      { name = "API_KEY", valueFrom = "${aws_secretsmanager_secret.salesforce.arn}:api_key::" }
    ]

    portMappings = [{
      containerPort = 8080
      protocol      = "tcp"
    }]

    logConfiguration = {
      logDriver = "awslogs"
      options = {
        "awslogs-group"         = aws_cloudwatch_log_group.main.name
        "awslogs-region"        = var.aws_region
        "awslogs-stream-prefix" = "ecs"
      }
    }

    healthCheck = {
      command     = ["CMD-SHELL", "curl -f http://localhost:8080/actuator/health || exit 1"]
      interval    = 15
      timeout     = 5
      retries     = 3
      startPeriod = 30
    }
  }])
}

resource "aws_ecs_service" "main" {
  name            = "${var.environment}-ms-sp-salesforce-config-service"
  cluster         = local.cluster_id
  task_definition = aws_ecs_task_definition.main.arn
  desired_count   = var.desired_count
  launch_type     = "FARGATE"

  network_configuration {
    subnets          = var.private_subnet_ids
    security_groups  = [aws_security_group.ecs_tasks.id]
    assign_public_ip = true
  }

  load_balancer {
    target_group_arn = var.alb_target_group_arn
    container_name   = "ms-sp-salesforce-config"
    container_port   = 8080
  }

  depends_on = [aws_iam_role.ecs_execution]
}

resource "aws_security_group" "ecs_tasks" {
  name        = "${var.environment}-ms-sf-config-ecs-sg"
  description = "Security group for ECS tasks"
  vpc_id      = var.vpc_id

  ingress {
    description     = "HTTP from ALB"
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
