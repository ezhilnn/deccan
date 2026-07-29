$ErrorActionPreference = "Continue"

$baseUrl = "http://localhost:8088/api"

$results = @()

function Add-Result {
    param(
        [string]$Step,
        [bool]$Success
    )

    $results += [PSCustomObject]@{
        Step   = $Step
        Status = if($Success){"PASS"}else{"FAIL"}
    }
}

function Show-Response {
    param(
        [string]$Title,
        $Response
    )

    Write-Host ""
    Write-Host "========== $Title ==========" -ForegroundColor Cyan
    $Response | ConvertTo-Json -Depth 30
    Write-Host ""
}

Write-Host ""
Write-Host "========================================"
Write-Host "DECCAN MILESTONE 9 VERIFICATION"
Write-Host "========================================"
Write-Host ""

##############################################################
# LOGIN
##############################################################

try{

$login=@{
    email="admin@deccan.dev"
    password="change-me"
}|ConvertTo-Json

$loginResponse=Invoke-RestMethod `
-Method POST `
-Uri "$baseUrl/auth/login" `
-ContentType "application/json" `
-Body $login

Show-Response "LOGIN" $loginResponse

$token=$loginResponse.data.accessToken

$headers=@{
    Authorization="Bearer $token"
    "Content-Type"="application/json"
}

Add-Result "Login" $true

}catch{

Add-Result "Login" $false
throw

}

##############################################################
# WHO AM I
##############################################################

try{

$me=Invoke-RestMethod `
-Method GET `
-Headers $headers `
-Uri "$baseUrl/me"

Show-Response "WHO AM I" $me

$organizationId=$me.data.organizationId

Add-Result "Who Am I" $true

}catch{

Add-Result "Who Am I" $false

}

##############################################################
# CREATE WORKFLOW
##############################################################

try{

$workflowBody=@{
    organizationId=$organizationId
    name="Execution Workflow-$([guid]::NewGuid().ToString().Substring(0,8))"
    description="Milestone 9 Verification"
}|ConvertTo-Json

$workflow=Invoke-RestMethod `
-Method POST `
-Headers $headers `
-Uri "$baseUrl/workflows" `
-Body $workflowBody

Show-Response "CREATE WORKFLOW" $workflow

$workflowId=$workflow.data.id

Add-Result "Create Workflow" $true

}catch{

Add-Result "Create Workflow" $false
throw

}

##############################################################
# PUBLISH
##############################################################

try{

$publish=@{
definition=@{
schemaVersion=1
trigger=@{
type="manual"
}
variables=@()
nodes=@(
@{
id="start"
type="manual-trigger"
name="Start"
configuration=@{}
},
@{
id="http"
type="http"
name="HTTP"
configuration=@{
method="GET"
url="https://jsonplaceholder.typicode.com/todos/1"
}
},
@{
id="response"
type="response"
name="Response"
configuration=@{}
}
)
edges=@(
@{
source="start"
target="http"
},
@{
source="http"
target="response"
}
)
}
}|ConvertTo-Json -Depth 20

$response=Invoke-RestMethod `
-Method POST `
-Headers $headers `
-Uri "$baseUrl/workflows/$workflowId/publish" `
-Body $publish

Show-Response "PUBLISH WORKFLOW" $response

Add-Result "Publish Workflow" $true

}catch{

Add-Result "Publish Workflow" $false

}

##############################################################
# EXECUTE
##############################################################

try{

$executionBody=@{
customerId=123
amount=2500
}|ConvertTo-Json

$execution=Invoke-RestMethod `
-Method POST `
-Headers $headers `
-Uri "$baseUrl/executions/workflows/$workflowId" `
-Body $executionBody

Show-Response "EXECUTE WORKFLOW" $execution

$executionId=$execution.data.id

Add-Result "Execute Workflow" $true

}catch{

Add-Result "Execute Workflow" $false

}

##############################################################
# GET EXECUTION
##############################################################

try{

$r=Invoke-RestMethod `
-Method GET `
-Headers $headers `
-Uri "$baseUrl/executions/$executionId"

Show-Response "GET EXECUTION" $r

Add-Result "Get Execution" $true

}catch{

Add-Result "Get Execution" $false

}

##############################################################
# LIST EXECUTIONS
##############################################################

try{

$r=Invoke-RestMethod `
-Method GET `
-Headers $headers `
-Uri "$baseUrl/executions/workflows/$workflowId"

Show-Response "LIST EXECUTIONS" $r

Add-Result "List Executions" $true

}catch{

Add-Result "List Executions" $false

}

##############################################################
# RETRY
##############################################################

try{

$r=Invoke-RestMethod `
-Method POST `
-Headers $headers `
-Uri "$baseUrl/executions/$executionId/retry"

Show-Response "RETRY EXECUTION" $r

Add-Result "Retry Execution" $true

}catch{

Write-Host "Retry skipped." -ForegroundColor Yellow
Add-Result "Retry Execution" $false

}

##############################################################
# CANCEL
##############################################################

try{

$r=Invoke-RestMethod `
-Method POST `
-Headers $headers `
-Uri "$baseUrl/executions/$executionId/cancel"

Show-Response "CANCEL EXECUTION" $r

Add-Result "Cancel Execution" $true

}catch{

Write-Host "Cancel skipped." -ForegroundColor Yellow
Add-Result "Cancel Execution" $false

}

##############################################################
# SUMMARY
##############################################################

Write-Host ""
Write-Host "========================================"
Write-Host "VERIFICATION SUMMARY"
Write-Host "========================================"

$results | Format-Table -AutoSize

Write-Host ""

$passed=($results|Where-Object{$_.Status-eq"PASS"}).Count
$failed=($results|Where-Object{$_.Status-eq"FAIL"}).Count

Write-Host "Passed : $passed" -ForegroundColor Green
Write-Host "Failed : $failed" -ForegroundColor Red

if($failed -eq 0){

Write-Host ""
Write-Host "MILESTONE 9 VERIFIED SUCCESSFULLY" -ForegroundColor Green

}else{

Write-Host ""
Write-Host "MILESTONE 9 VERIFICATION FAILED" -ForegroundColor Red

}