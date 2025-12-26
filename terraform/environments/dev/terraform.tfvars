environment = "dev"

# VPC existente: VPC-COMERCIAL-DESA_V2
vpc_id = "vpc-03dabe3cc12259b57"

# Subnets de aplicación (privadas)
app_subnet_ids = [
  "subnet-0900c1a43aff028e2",  # PrivateSubnet-Comercial-Desa-app_v2-A (us-east-2a)
  "subnet-057a9da837af8a562"   # PrivateSubnet-Comercial-Desa-app_v2-B (us-east-2b)
]

# Subnets de base de datos (privadas)
db_subnet_ids = [
  "subnet-0e89da98d1b8636bf",  # PrivateSubnet-Comercial-Desa-db_v2-A (us-east-2a)
  "subnet-012b33651d877432e",  # PrivateSubnet-Comercial-Desa-db_v2-B (us-east-2b)
  "subnet-00de63f1ec0a830e5"   # PrivateSubnet-Comercial-Desa-db_v2-C (us-east-2c)
]

# ALB existente
existing_alb_name      = "alb-comercial-dev2-desa"
alb_listener_port      = 8080
listener_rule_priority = 100

# ECS Cluster existente
use_existing_ecs_cluster  = true
existing_ecs_cluster_name = "cluster-microservicios-aini"

# Configuración ECS
desired_count = 1
