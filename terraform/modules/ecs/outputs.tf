output "cluster_id" {
  description = "ECS Cluster ID"
  value       = local.cluster_id
}

output "cluster_arn" {
  description = "ECS Cluster ARN"
  value       = local.cluster_arn
}

output "service_name" {
  description = "ECS Service name"
  value       = aws_ecs_service.main.name
}

output "task_definition_arn" {
  description = "ECS Task Definition ARN"
  value       = aws_ecs_task_definition.main.arn
}

output "ecs_security_group_id" {
  description = "Security group ID for ECS tasks"
  value       = aws_security_group.ecs_tasks.id
}
