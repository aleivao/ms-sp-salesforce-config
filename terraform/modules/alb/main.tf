# Data source para ALB existente
data "aws_lb" "existing" {
  name = var.existing_alb_name
}

# Data source para Listener existente
data "aws_lb_listener" "existing" {
  load_balancer_arn = data.aws_lb.existing.arn
  port              = var.listener_port
}

# Target Group para este servicio
resource "aws_lb_target_group" "main" {
  name        = "${var.environment}-ms-sf-config-tg"
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

# Listener Rule para rutear tráfico a este servicio
resource "aws_lb_listener_rule" "main" {
  listener_arn = data.aws_lb_listener.existing.arn
  priority     = var.listener_rule_priority

  action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.main.arn
  }

  condition {
    path_pattern {
      values = ["/api/salesforce/*", "/actuator/*"]
    }
  }
}
