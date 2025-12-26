output "alb_dns_name" {
  description = "DNS name of the existing ALB"
  value       = data.aws_lb.existing.dns_name
}

output "alb_arn" {
  description = "ARN of the existing ALB"
  value       = data.aws_lb.existing.arn
}

output "target_group_arn" {
  description = "ARN of the target group"
  value       = aws_lb_target_group.main.arn
}

output "alb_security_group_id" {
  description = "Security group ID of the existing ALB"
  value       = tolist(data.aws_lb.existing.security_groups)[0]
}
