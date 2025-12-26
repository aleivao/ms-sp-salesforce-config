resource "aws_cloudwatch_log_group" "main" {
  name              = "/ecs/ms-sp-salesforce-config-${var.environment}"
  retention_in_days = 30
}
