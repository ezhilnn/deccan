$ErrorActionPreference = "Stop"

$baseUrl = "http://localhost:8088/api"

function Show-Error {
    param($ErrorRecord)
    Write-Host ""
    Write-Host "====================================="
    Write-Host "HTTP ERROR"
    Write-Host "====================================="
    if ($ErrorRecord.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($ErrorRecord.Exception.Response.GetResponseStream())
        $reader.BaseStream.Position = 0
        $reader.DiscardBufferedData()
        Write-Host ($reader.ReadToEnd())
    } else {
        Write-Host $ErrorRecord.Exception.Message
    }
    throw $ErrorRecord
}

function Invoke-DeccanApi {
    param(
        [string]$Method,
        [string]$Uri,
        [hashtable]$Headers = $null,
        [string]$Body = $null
    )

    try {
        if ([string]::IsNullOrWhiteSpace($Body)) {
            Invoke-RestMethod -Method $Method -Uri $Uri -Headers $Headers
        } else {
            Invoke-RestMethod -Method $Method -Uri $Uri -Headers $Headers -ContentType "application/json" -Body $Body
        }
    } catch {
        Show-Error $_
    }
}

Write-Host "====================================="
Write-Host "DECCAN MILESTONE 7 VERIFICATION"
Write-Host "====================================="

$login = @{ email="admin@deccan.dev"; password="change-me" } | ConvertTo-Json
$loginResponse = Invoke-DeccanApi -Method POST -Uri "$baseUrl/auth/login" -Body $login
$token = $loginResponse.data.accessToken
$headers = @{ Authorization = "Bearer $token" }

$me = Invoke-DeccanApi -Method GET -Uri "$baseUrl/me" -Headers $headers
$organizationId = $me.data.organizationId

$workflowBody = @{
 organizationId=$organizationId
 name="Workflow-$([guid]::NewGuid())"
 description="Milestone7 Test"
} | ConvertTo-Json

$created = Invoke-DeccanApi -Method POST -Uri "$baseUrl/workflows" -Headers $headers -Body $workflowBody
$workflowId = $created.data.id
Write-Host "WorkflowId: $workflowId"
$created | ConvertTo-Json -Depth 10

$get = Invoke-DeccanApi -Method GET -Uri "$baseUrl/workflows/$workflowId" -Headers $headers
$get | ConvertTo-Json -Depth 10

$definition=@{
 definition=@{
  schemaVersion=1
  trigger=@{type="manual";configuration=@{}}
  nodes=@(
   @{id="start";type="manual-trigger";name="Start";x=100;y=100;configuration=@{};inputs=@();outputs=@(@{name="output";type="object"});bindings=@()},
   @{id="http";type="http";name="Http";x=400;y=100;configuration=@{method="GET";url="https://api.github.com"};inputs=@(@{name="request";type="object";required=$true});outputs=@(@{name="response";type="object"});bindings=@()}
  )
  edges=@(@{source="start";sourcePort="output";target="http";targetPort="request"})
  variables=@()
 }
}|ConvertTo-Json -Depth 20

Invoke-DeccanApi -Method POST -Uri "$baseUrl/workflows/$workflowId/publish" -Headers $headers -Body $definition | ConvertTo-Json -Depth 10
Invoke-DeccanApi -Method GET -Uri "$baseUrl/workflows/$workflowId/versions" -Headers $headers | ConvertTo-Json -Depth 10
$export=Invoke-DeccanApi -Method GET -Uri "$baseUrl/workflows/$workflowId/export/1" -Headers $headers
$export | ConvertTo-Json -Depth 20
$importBody=$export.data|ConvertTo-Json -Depth 30
Invoke-DeccanApi -Method POST -Uri "$baseUrl/workflows/import/$organizationId" -Headers $headers -Body $importBody | ConvertTo-Json -Depth 10
Invoke-DeccanApi -Method GET -Uri "$baseUrl/workflows/organizations/$organizationId?page=0&size=5" -Headers $headers | ConvertTo-Json -Depth 10
Invoke-DeccanApi -Method GET -Uri "$baseUrl/workflows/organizations/$organizationId?status=ACTIVE&page=0&size=5" -Headers $headers | ConvertTo-Json -Depth 10
Invoke-DeccanApi -Method POST -Uri "$baseUrl/workflows/$workflowId/archive" -Headers $headers | ConvertTo-Json -Depth 10
Invoke-DeccanApi -Method POST -Uri "$baseUrl/workflows/$workflowId/activate" -Headers $headers | ConvertTo-Json -Depth 10

Write-Host "====================================="
Write-Host "MILESTONE 7 VERIFIED"
Write-Host "====================================="
