output "vpc_id" {
  description = "VPC ID"
  value       = data.aws_vpc.main.id
}

output "vpc_cidr" {
  description = "VPC CIDR block"
  value       = data.aws_vpc.main.cidr_block
}

output "app_subnet_ids" {
  description = "List of app subnet IDs"
  value       = var.app_subnet_ids
}

output "db_subnet_ids" {
  description = "List of database subnet IDs"
  value       = var.db_subnet_ids
}

output "route_table_ids" {
  description = "List of route table IDs"
  value       = data.aws_route_tables.main.ids
}
