environment = "dev"

# VPC default
vpc_id = "vpc-abaae4d0"

# Subnets públicas (us-east-1a, us-east-1b)
app_subnet_ids = [
  "subnet-0c5bbb22",
  "subnet-7138923b"
]

# Configuración ECS
desired_count = 1
