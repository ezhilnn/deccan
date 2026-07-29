$ErrorActionPreference="Stop"

$baseUrl="http://localhost:8088/api"

Write-Host ""
Write-Host "========================================"
Write-Host "DECCAN MILESTONE 8 VERIFICATION"
Write-Host "========================================"
Write-Host ""

function Show-Response {
    param($Title,$Response)
    Write-Host ""
    Write-Host "========== $Title =========="
    $Response | ConvertTo-Json -Depth 30
    Write-Host ""
}

# LOGIN
$login=@{email="admin@deccan.dev";password="change-me"}|ConvertTo-Json
$loginResponse=Invoke-RestMethod -Method POST -Uri "$baseUrl/auth/login" -ContentType "application/json" -Body $login
Show-Response "LOGIN" $loginResponse
$token=$loginResponse.data.accessToken
$headers=@{Authorization="Bearer $token"}
Write-Host "LOGIN........................PASS"

# WHOAMI
$me=Invoke-RestMethod -Headers $headers -Method GET -Uri "$baseUrl/me"
Show-Response "WHO AM I" $me
$organizationId=$me.data.organizationId
Write-Host "WHOAMI.......................PASS"

# LIST CONNECTORS
$list=Invoke-RestMethod -Headers $headers -Method GET -Uri "$baseUrl/connectors"
Show-Response "BOOTSTRAP CONNECTORS" $list
Write-Host "BOOTSTRAP CONNECTORS.........PASS"

# CREATE CONNECTOR
$connectorName="custom-http-$([guid]::NewGuid().ToString().Substring(0,8))"
$connector=@{
organizationId=$organizationId
name=$connectorName
displayName="Custom HTTP $([guid]::NewGuid().ToString().Substring(0,4))"
type="ACTION"
version="1.0.0"
configurationSchema=@{properties=@{url=@{type="string"};method=@{type="string"}}}
}|ConvertTo-Json -Depth 20
$createdConnector=Invoke-RestMethod -Headers $headers -Method POST -ContentType "application/json" -Uri "$baseUrl/connectors" -Body $connector
Show-Response "CREATE CONNECTOR" $createdConnector
$connectorId=$createdConnector.data.id
Write-Host "CREATE CONNECTOR............PASS"

# GET CONNECTOR
$r=Invoke-RestMethod -Headers $headers -Method GET -Uri "$baseUrl/connectors/$connectorId"
Show-Response "GET CONNECTOR" $r
Write-Host "GET CONNECTOR...............PASS"

# CONNECTOR VERSIONS
$r=Invoke-RestMethod -Headers $headers -Method GET -Uri "$baseUrl/connectors/$connectorName/versions"
Show-Response "CONNECTOR VERSIONS" $r
Write-Host "CONNECTOR VERSIONS..........PASS"

# CREATE CREDENTIAL
$credentialName="openai-$([guid]::NewGuid().ToString().Substring(0,8))"
$credential=@{
organizationId=$organizationId
name=$credentialName
type="API_KEY"
provider="OpenAI"
secretReference="local://openai/$([guid]::NewGuid().ToString().Substring(0,6))
"}|ConvertTo-Json
$createdCredential=Invoke-RestMethod -Headers $headers -Method POST -ContentType "application/json" -Uri "$baseUrl/credentials" -Body $credential
Show-Response "CREATE CREDENTIAL" $createdCredential
$credentialId=$createdCredential.data.id
Write-Host "CREATE CREDENTIAL...........PASS"

# GET CREDENTIAL
$r=Invoke-RestMethod -Headers $headers -Method GET -Uri "$baseUrl/credentials/$credentialId"
Show-Response "GET CREDENTIAL" $r
Write-Host "GET CREDENTIAL..............PASS"

# LIST CREDENTIALS
$r=Invoke-RestMethod -Headers $headers -Method GET -Uri "$baseUrl/credentials/organization/$organizationId"
Show-Response "LIST CREDENTIALS" $r
Write-Host "LIST CREDENTIALS............PASS"

# ASSIGN CREDENTIAL
$r=Invoke-RestMethod -Headers $headers -Method POST -Uri "$baseUrl/connectors/$connectorId/credential/$credentialId"
Show-Response "ASSIGN CREDENTIAL" $r
Write-Host "ASSIGN CREDENTIAL...........PASS"

# DELETE CONNECTOR
$r=Invoke-RestMethod -Headers $headers -Method DELETE -Uri "$baseUrl/connectors/$connectorId"
Show-Response "DELETE CONNECTOR" $r
Write-Host "DELETE CONNECTOR............PASS"

Write-Host ""
Write-Host "========================================"
Write-Host "MILESTONE 8 VERIFIED"
Write-Host "========================================"
