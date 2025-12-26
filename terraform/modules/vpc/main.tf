# Data source para VPC existente
data "aws_vpc" "main" {
  id = var.vpc_id
}

# Data sources para subnets existentes
data "aws_subnet" "app" {
  for_each = toset(var.app_subnet_ids)
  id       = each.value
}

data "aws_subnet" "db" {
  for_each = toset(var.db_subnet_ids)
  id       = each.value
}

# Data source para route tables (para agregar VPC endpoints)
data "aws_route_tables" "main" {
  vpc_id = var.vpc_id
}
