environment = "dev"

# VPC existente: VPC-COMERCIAL-DESA_V2
vpc_id = "vpc-03dabe3cc12259b57"

# Subnets de aplicación (privadas)
app_subnet_ids = [
  "subnet-0900c1a43aff028e2",  # PrivateSubnet-Comercial-Desa-app_v2-A (us-east-2a)
  "subnet-057a9da837af8a562",  # PrivateSubnet-Comercial-Desa-app_v2-B (us-east-2b)
  "subnet-068016b3d496a6567"   # PrivateSubnet-Comercial-Desa-app_v2-C (us-east-2c)
]

# Subnets de base de datos (privadas)
db_subnet_ids = [
  "subnet-0e89da98d1b8636bf",  # PrivateSubnet-Comercial-Desa-db_v2-A (us-east-2a)
  "subnet-012b33651d877432e",  # PrivateSubnet-Comercial-Desa-db_v2-B (us-east-2b)
  "subnet-00de63f1ec0a830e5"   # PrivateSubnet-Comercial-Desa-db_v2-C (us-east-2c)
]

# Configuración ECS
desired_count = 1

# Usar cluster ECS existente (opcional)
use_existing_ecs_cluster  = true
existing_ecs_cluster_name = "cluster-microservicios-aini"

# Credenciales Salesforce (definir en terraform.tfvars.secret o como variable de entorno)
# salesforce_credentials = {
#   username      = "..."
#   password      = "..."
#   client_id     = "..."
#   client_secret = "..."
# }
