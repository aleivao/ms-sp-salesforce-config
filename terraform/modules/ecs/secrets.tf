resource "aws_secretsmanager_secret" "salesforce" {
  name = "${var.environment}/salesforce-credentials"
}

resource "aws_secretsmanager_secret_version" "salesforce" {
  secret_id     = aws_secretsmanager_secret.salesforce.id
  secret_string = jsonencode(var.salesforce_credentials)
}

resource "aws_secretsmanager_secret" "database" {
  name = "${var.environment}/database-credentials"
}

resource "aws_secretsmanager_secret_version" "database" {
  secret_id     = aws_secretsmanager_secret.database.id
  secret_string = jsonencode(var.database_credentials)
}
