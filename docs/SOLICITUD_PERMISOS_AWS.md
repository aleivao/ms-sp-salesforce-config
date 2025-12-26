# Solicitud de Permisos AWS - Permission Set DevComercialV2 (REDUCIDA)

**Fecha:** 26 de Diciembre de 2024
**Solicitante:** Alvaro Leiva (alvaro.leiva@ajegroup.com)
**Cuenta AWS:** 623653227273 (Comercial Desarrollo V2)
**Permission Set actual:** DevComercialV2

---

## Resumen

Se requieren permisos adicionales para desplegar el microservicio **ms-sp-salesforce-config** reutilizando recursos existentes.

## Recursos existentes que se reutilizarán

| Recurso | ID/Nombre |
|---------|-----------|
| VPC | vpc-03dabe3cc12259b57 |
| ALB | alb-comercial-dev2-desa |
| ECS Cluster | cluster-microservicios-aini |
| VPC Endpoint Secrets Manager | Ya existe |
| Transit Gateway (internet) | tgw-061add2454d84a5bd |

## Recursos a crear (permisos requeridos)

| Recurso | Permiso necesario |
|---------|-------------------|
| Target Group | `elasticloadbalancing:CreateTargetGroup` |
| Listener Rule | `elasticloadbalancing:CreateRule` |
| ECS Task Definition | `ecs:RegisterTaskDefinition` |
| ECS Service | `ecs:CreateService` |
| IAM Roles (2) | `iam:CreateRole`, `iam:PassRole` |
| Secret | `secretsmanager:CreateSecret` |
| Log Group | `logs:CreateLogGroup` |
| Security Group (ECS) | `ec2:CreateSecurityGroup` |

## Política IAM mínima requerida

```json
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "EC2SecurityGroups",
            "Effect": "Allow",
            "Action": [
                "ec2:CreateSecurityGroup",
                "ec2:DeleteSecurityGroup",
                "ec2:AuthorizeSecurityGroupIngress",
                "ec2:AuthorizeSecurityGroupEgress",
                "ec2:RevokeSecurityGroupIngress",
                "ec2:RevokeSecurityGroupEgress",
                "ec2:DescribeSecurityGroups",
                "ec2:CreateTags"
            ],
            "Resource": "*",
            "Condition": {
                "StringEquals": {
                    "ec2:Vpc": "arn:aws:ec2:us-east-2:623653227273:vpc/vpc-03dabe3cc12259b57"
                }
            }
        },
        {
            "Sid": "ElasticLoadBalancingLimited",
            "Effect": "Allow",
            "Action": [
                "elasticloadbalancing:CreateTargetGroup",
                "elasticloadbalancing:DeleteTargetGroup",
                "elasticloadbalancing:DescribeTargetGroups",
                "elasticloadbalancing:ModifyTargetGroup",
                "elasticloadbalancing:ModifyTargetGroupAttributes",
                "elasticloadbalancing:CreateRule",
                "elasticloadbalancing:DeleteRule",
                "elasticloadbalancing:DescribeRules",
                "elasticloadbalancing:ModifyRule",
                "elasticloadbalancing:RegisterTargets",
                "elasticloadbalancing:DeregisterTargets",
                "elasticloadbalancing:DescribeTargetHealth",
                "elasticloadbalancing:DescribeLoadBalancers",
                "elasticloadbalancing:DescribeListeners",
                "elasticloadbalancing:AddTags"
            ],
            "Resource": "*"
        },
        {
            "Sid": "ECSAccess",
            "Effect": "Allow",
            "Action": [
                "ecs:CreateService",
                "ecs:UpdateService",
                "ecs:DeleteService",
                "ecs:DescribeServices",
                "ecs:RegisterTaskDefinition",
                "ecs:DeregisterTaskDefinition",
                "ecs:DescribeTaskDefinition",
                "ecs:DescribeTasks",
                "ecs:ListTasks",
                "ecs:DescribeClusters",
                "ecs:ListClusters"
            ],
            "Resource": "*"
        },
        {
            "Sid": "IAMRolesForECS",
            "Effect": "Allow",
            "Action": [
                "iam:CreateRole",
                "iam:DeleteRole",
                "iam:GetRole",
                "iam:AttachRolePolicy",
                "iam:DetachRolePolicy",
                "iam:PutRolePolicy",
                "iam:DeleteRolePolicy",
                "iam:GetRolePolicy",
                "iam:ListRolePolicies",
                "iam:ListAttachedRolePolicies",
                "iam:PassRole",
                "iam:TagRole"
            ],
            "Resource": [
                "arn:aws:iam::623653227273:role/*-ecs-*"
            ]
        },
        {
            "Sid": "SecretsManager",
            "Effect": "Allow",
            "Action": [
                "secretsmanager:CreateSecret",
                "secretsmanager:DeleteSecret",
                "secretsmanager:DescribeSecret",
                "secretsmanager:GetSecretValue",
                "secretsmanager:PutSecretValue",
                "secretsmanager:UpdateSecret",
                "secretsmanager:TagResource"
            ],
            "Resource": "arn:aws:secretsmanager:us-east-2:623653227273:secret:dev/*"
        },
        {
            "Sid": "CloudWatchLogs",
            "Effect": "Allow",
            "Action": [
                "logs:CreateLogGroup",
                "logs:DeleteLogGroup",
                "logs:DescribeLogGroups",
                "logs:PutRetentionPolicy",
                "logs:TagLogGroup"
            ],
            "Resource": "arn:aws:logs:us-east-2:623653227273:log-group:/ecs/*"
        }
    ]
}
```

## Comparación con solicitud anterior

| Aspecto | Antes | Ahora |
|---------|-------|-------|
| Crear ALB | Sí | **No** (usa existente) |
| Crear VPC Endpoints | Sí (3) | **No** (Transit Gateway) |
| Crear Security Group ALB | Sí | **No** (usa existente) |
| Permisos IAM | Amplios | **Restringidos a roles ECS** |

## Justificación de seguridad

1. **Sin creación de Load Balancers**: Solo se agregan reglas al ALB existente
2. **Restricción por VPC**: Security Groups limitados a la VPC específica
3. **Restricción por nombre**: Roles IAM solo con patrón `*-ecs-*`
4. **Restricción por path**: Secretos solo bajo `dev/`
5. **Sin acceso a producción**: Permisos limitados al ambiente de desarrollo

---

## Contacto

- **Nombre:** Alvaro Leiva
- **Email:** alvaro.leiva@ajegroup.com
- **Proyecto:** ms-sp-salesforce-config
- **Repositorio:** https://github.com/ajegroup-ayni/ms-sp-salesforce-config
